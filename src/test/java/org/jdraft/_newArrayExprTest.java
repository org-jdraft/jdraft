package org.jdraft;

import junit.framework.TestCase;

public class _newArrayExprTest  extends TestCase {

    public void testNewArray(){
        int[] arr = new int[]{};
        int[] arr1 = new int[]{1};
        int[] arr2 = new int[0];

        _newArrayExpr _na = _newArrayExpr.of("new int[]");
        assertEquals("int", _na.getElementType().toString());
        assertEquals( 1, _na.listArrayDimensions().size());
        assertFalse(_na.hasInit());

        _na = _newArrayExpr.of("new int[]{}");
        assertTrue( _na.hasInit() );
        assertEquals(0, _na.getInit().getSize());
        assertEquals("int", _na.getElementType().toString());
        assertEquals( 1, _na.listArrayDimensions().size());
        System.out.println( _na.getInit() );

        _na = _newArrayExpr.of("new int[0]");
        _arrayDimension _ad = _na.getAt(0);
        assertTrue(_na.isAt(0, _arrayDimension.of(0)));
        assertTrue(_ad.isExpression(0));

        _na = _newArrayExpr.of("new int[0][a+b]");
        _ad = _na.getAt(0);
        assertTrue(_na.isAt(0, _arrayDimension.of(0)));
        assertTrue(_ad.isExpression(0));
        assertTrue(_na.isAt(1, _arrayDimension.of(_expr.of("a+b"))));

        _na = _newArrayExpr.of("new L<T>[2][a+b]{ l(0), l(2) }");
        _typeRef _tr = _na.getType();
        assertEquals( 2, _na.listArrayDimensions().size());

        assertTrue(_na.isAt(0, _arrayDimension.of(2) ));
        assertTrue(_na.isAt(0, _expr.of(2) ));
        assertTrue(_na.isAt(0, "2" ));

        assertTrue(_na.isAt(1, _expr.of("a + b")));
        assertTrue(_na.isAt(1, "a + b"));

        System.out.println( _tr );
        assertEquals(_tr, _typeRef.of("L<T>"));

        assertTrue( _na.isAt(0, _arrayDimension.of(_expr.of(2))));
        assertEquals( "l(0)", _na.getInit().getAt(0).toString());
        assertEquals( "l(2)", _na.getInit().getAt(1).toString());


        _na = _newArrayExpr.of("new aaaa.bbbb.L<T,B>[2][a+b]{ l(0), l(2) }");
        _tr = _na.getType();

        assertTrue(_na.isElementType("aaaa.bbbb.L<T,B>"));
        assertFalse(_na.isElementType("aaaa.bbbb.M<T,B>"));

    }

}
