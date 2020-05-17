package org.jdraft;

import junit.framework.TestCase;

public class _variablesExprTest extends TestCase {

    public void testVs(){
        _variablesExpr _vs = _variablesExpr.of("@A @B(1) @C(k=2) int x, y=-2, z = 100");
        assertEquals(3, _vs.size());
        assertEquals(3, _vs.list().size());
        assertEquals(2, _vs.list(v-> v.hasInit()).size());
        assertTrue( _vs.hasAnnoExpr("A"));
        assertTrue( _vs.hasAnnoExpr("B"));
        assertTrue( _vs.hasAnnoExpr("C"));


        assertNotNull(_vs.getAnnoExpr("A"));
        assertNotNull(_vs.getAnnoExpr("B"));
        assertNotNull(_vs.getAnnoExpr("C"));

        assertNotNull(_vs.getAnnoExpr(a-> a.hasPair("value", 1)));
        assertNotNull(_vs.getAnnoExpr(a-> a.hasPair("k", 2)));

        assertEquals(3, _vs.listAnnoExprs().size());

        assertEquals(1, _vs.list(_v-> _v.isInit(100)).size());
    }

    public void testVariablesAPI(){
        _variablesExpr _vs = _variablesExpr.of("int i");
        assertEquals(1, _vs.size());
        assertEquals( _variable.of("int i"), _vs.getAt(0) );

        assertEquals(1, _vs.listVariables(v-> v.isTypeRef(int.class)).size());
        assertEquals(0, _vs.listVariables(v-> v.isTypeRef(String.class)).size());

        assertEquals( 0, _vs.indexOf(_variable.of("int i")));
        assertEquals( -1, _vs.indexOf(_variable.of("int j")));

        assertFalse(_vs.isFinal());
        assertEquals( 1, _vs.size());
        assertEquals( _variable.of("int i"), _vs.getAt(0));

        _vs.forEach(v -> System.out.println( v) );
        _vs.forEach(v-> v.isNamed("i"), v-> System.out.println(1));
        try{
            _vs.getAt(2);
            fail("expected exception ");
        }catch(Exception e){
            //expected
        }
        assertTrue(_vs.has(v-> v.isTypeRef(int.class)));
    }

    public void testMods(){
        _variablesExpr _vs = _variablesExpr.of( "@A final int i, j");
        assertTrue(_vs.isFinal());
        assertEquals(2, _vs.size());
    }

    public void testAnno(){
        _variablesExpr _vs = _variablesExpr.of( "@A final int i");
        assertTrue( _vs.hasAnnoExprs() );
        assertTrue( _vs.hasAnnoExpr("A"));
        assertTrue( _vs.hasAnnoExpr(a->a.isNamed("A")));
        assertEquals( _annoExpr.of("@A"), _vs.getAnnoExpr(0) );
        _vs.addAnnoExprs(_annoExpr.of("@B(1)"), _annoExpr.of("@C(k=2)"));

        _vs.remove(_variable.of("int i"));

        assertEquals( 0, _vs.size());
        //_vs.add(_variable.of("int i"));
        System.out.println( _vs);
    }
}
