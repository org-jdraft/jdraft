package org.jdraft;

import junit.framework.TestCase;

public class _methodRefTest extends TestCase {

    public void testMR(){

        _methodRefExpr _mr = _methodRefExpr.of("TypeRef::methodRef");

        System.out.println( _mr );

        assertEquals("TypeRef", _mr.getScope().toString());
        assertEquals("methodRef", _mr.getIdentifier());

        _mr = _methodRefExpr.of("HashSet<Person>::new");
        //assertTrue(_mr.node.getTypeArguments().isPresent());
        assertEquals("HashSet<Person>", _mr.getScope().toString());
        assertEquals("Person", _mr.getTypeArgs().toString()); //it SHOULD work this way, but broken
        assertEquals("Person", _mr.getTypeArg(0).toString());
        assertEquals("new", _mr.getIdentifier());
    }

    public void testGenericMethodReference(){
        //SomeTest <Integer> mRef =
        _methodRefExpr _mr = (_methodRefExpr) _expr.of("MyClass :: <Integer> myGenMeth");
        System.out.println( _mr );
    }
}
