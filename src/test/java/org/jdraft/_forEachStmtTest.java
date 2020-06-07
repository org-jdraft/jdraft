package org.jdraft;

import junit.framework.TestCase;

public class _forEachStmtTest extends TestCase {

    public void testEqualsHashcode(){
        _forEachStmt _a = _forEachStmt.of("for(Object o : objects){ System.out.println(o); }");
        _forEachStmt _b = _forEachStmt.of("for(Object o : objects){ System.out.println(o); }");


        assertEquals( _a, _b);
        assertEquals( _a.hashCode(), _b.hashCode());

        //verify it doesnt matter which order the variables are in for equals and hashcode
        _a.setVariable("Object o, p");
        _b.setVariable("Object p, o");

        assertEquals( _a, _b);
        assertEquals( _a.hashCode(), _b.hashCode());
    }

    public void testForEachFromScratch(){
        _forEachStmt _fe = _forEachStmt.of();
        //System.out.println( _fe );
        assertTrue(_fe.isIterable("empty"));

        _fe.setIterable("iterable");
        _fe.setVariable("varType variable");

        assertTrue( _fe.isIterable("iterable"));
        assertTrue( _fe.isVariable("varType variable"));
        assertTrue( _fe.hasVariable("varType variable"));

        //multi-variable
        assertTrue( _fe.setVariable("vartype a, b").hasVariable("vartype b") );
        assertTrue( _fe.setVariable("vartype a, b").hasVariable("vartype a") );
        assertTrue( _fe.setVariable("vartype a, b").isVariable("vartype a, b") );
        assertTrue( _fe.setVariable("vartype a, b").isVariable("vartype b, a") );

    }

}