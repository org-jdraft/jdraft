package org.jdraft;

import junit.framework.TestCase;

import java.util.concurrent.atomic.AtomicInteger;

public class _methodCallExprTest extends TestCase {

    public void testAPI() {
        _methodCallExpr _mc = _methodCallExpr.of();
        _mc = _methodCallExpr.of( ()-> { System.out.println(1); });
        _mc = _methodCallExpr.of( ($any$)-> { System.out.println($any$); });
        _methodCallExpr.of("callMethod()");
        _methodCallExpr.of("callMethod(1,2,3)");
        _methodCallExpr.of(Exprs.methodCallExpr("callMethod()"));
        _methodCallExpr.of(Exprs.methodCallExpr("callMethod(4,5,6)"));

        System.out.println( _mc);
    }

    public void testName() {
        _methodCallExpr _mc = _methodCallExpr.of();
        _mc.setName("methodName");
        assertEquals("methodName", _mc.getName());
        assertTrue(_mc.isNamed("methodName"));
        assertTrue(_mc.isNamed(n -> n.startsWith("method")));
        assertEquals( "methodName", _mc.getName());
    }

    public void testArguments() {
        _methodCallExpr _mc = _methodCallExpr.of();
        assertTrue(_mc.isArgs(as -> as.isEmpty()));
        assertFalse(_mc.hasArgs());
        assertFalse(_mc.isArg(0, _intExpr.of(1)));
        _mc.setName("MN");
        assertEquals("MN", _mc.getName());
        _mc.addArgs("1");
        assertTrue(_mc.hasArgs());
        assertTrue(_mc.isArg(0, _intExpr.of(1)));
        assertTrue(_mc.isArg(0, "1"));
        _mc.addArg(5);
        assertTrue(_mc.isArg(1, _intExpr.of(5)));
        assertTrue(_mc.isArg(1, "5"));
        assertEquals(2, _mc.countArgs());

        //verify I can go through Arguments
        AtomicInteger ai = new AtomicInteger();
        //for all int literal arguments; add up the arguments int literal values
        _mc.forArgs(e -> e.ast().isIntegerLiteralExpr(),
                e -> ai.addAndGet(((_intExpr) e).getValue()));

        assertEquals(6, ai.get());
    }

    public void testScope() {
        _methodCallExpr _mc = _methodCallExpr.of();
        assertFalse(_mc.hasScope());
        _mc.setScope("scope");
        assertEquals("scope", _mc.getScope().toString());
        //System.out.println(_mc ); scope.MN();
        _mc.removeScope();
        assertFalse(_mc.hasScope());
    }


     public void testTypeArguments(){
        _methodCallExpr _mc = _methodCallExpr.of();
        assertFalse( _mc.hasTypeArgs());
        _mc.addTypeArgs("I");
        assertTrue( _mc.hasTypeArgs());
        _typeRef _tr = _mc.getTypeArg(0);
        assertEquals( "I", _tr.toString());
        _mc.removeTypeArg(0);
        assertTrue(_mc.isUsingDiamondOperator());
        assertTrue( _mc.hasTypeArgs());

        //remove ALL
        _mc.removeTypeArgs();
        assertFalse(_mc.isUsingDiamondOperator());
        assertFalse( _mc.hasTypeArgs());
    }
}
