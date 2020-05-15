package org.jdraft;

import junit.framework.TestCase;

public class _arrayAccessExprTest extends TestCase {

    public void testArr(){
        _arrayAccessExpr _aa = _arrayAccessExpr.of("v[1][3]");
        Print.tree(_aa.ast());
        Print.tree( _aa.getIndex().ast() );
    }

    public void testArrAcc(){
        assertEquals( _arrayAccessExpr.of("a",1), _arrayAccessExpr.of("a[1]"));
        _arrayAccessExpr _aa1 = _arrayAccessExpr.of("v", 1,3);
        _arrayAccessExpr _aa2 = _arrayAccessExpr.of("v[1][3]");
        assertEquals( _aa1, _aa2);

    }
}
