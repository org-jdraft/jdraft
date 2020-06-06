package org.jdraft;

import junit.framework.TestCase;

public class _castExprTest extends TestCase {

    public void testCast(){
        _castExpr _ce = _castExpr.of();
        System.out.println( _ce );
        assertEquals("empty", _ce.getType().toString());
        assertEquals( "empty", _ce.getExpression().toString());

        assertTrue( _ce.is("(empty)empty"));
        assertTrue( _ce.isExpression("empty"));
        assertTrue( _ce.isType("empty"));

        _ce.setExpression("callMethod(arg1)");
        assertTrue( _ce.isExpression("callMethod(arg1)"));

        _ce.setType("String[]");
        assertTrue( _ce.isType("String[]"));

    }


}