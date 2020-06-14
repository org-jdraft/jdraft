package org.jdraft;

import junit.framework.TestCase;

public class _arrayAccessExprTest extends TestCase {

    public void testArr(){
        _arrayAccessExpr _aa = _arrayAccessExpr.of("v[1][3]");
        Print.tree(_aa.ast());
        Print.tree( _aa.getIndex().ast() );

        assertTrue(_aa.isIndex(_intExpr.class));
        System.out.println( _aa.getIndex() );
        assertTrue(_aa.isIndex(_intExpr.class, _i->_i.is(3)));
        assertTrue(_aa.isExpression(_arrayAccessExpr.class));
        assertTrue(_aa.isExpression(_arrayAccessExpr.of("v[1]")));

        _arrayAccessExpr _ma = _arrayAccessExpr.of("call()[1]");
        assertTrue( _ma.isExpression(_methodCallExpr.class));
        assertTrue( _ma.isExpression(_methodCallExpr.of("call()")));

        _ma = _arrayAccessExpr.of("callAMethod()[1]");
        assertTrue( _ma.isExpression("call$any$()"));
        assertFalse( _ma.isExpression("ball$any$()"));
        assertTrue( _ma.isIndex("[1]"));
        assertTrue( _ma.isIndex("[$any$]"));
    }

    public void testArrAcc(){
        assertEquals( _arrayAccessExpr.of("a",1), _arrayAccessExpr.of("a[1]"));
        _arrayAccessExpr _aa1 = _arrayAccessExpr.of("v", 1,3);
        _arrayAccessExpr _aa2 = _arrayAccessExpr.of("v[1][3]");
        assertEquals( _aa1, _aa2);

    }
}
