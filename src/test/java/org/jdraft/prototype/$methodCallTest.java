package org.jdraft.prototype;

import junit.framework.TestCase;
import org.jdraft.Ex;
import org.jdraft._methodCall;
import org.jdraft._string;

public class $methodCallTest extends TestCase {

    public void testMatchAny(){
        assertTrue($methodCall.of().isMatchAny());
        assertNotNull($methodCall.of().get$name());
        assertNotNull($methodCall.of().get$scope());
        assertNotNull($methodCall.of().get$arguments());
    }

    public void testPredicate(){
        assertTrue( $methodCall.of(m-> !m.hasArguments()).matches("m()") );
        assertFalse( $methodCall.of(m-> !m.hasArguments()).matches("m(1)") );
    }

    public void testT(){
        //assertTrue($methodCall.of("System.out.println($any$)").matches( "System.out.println()"));
        assertTrue($methodCall.of("System.out.println($any$)").matches( "System.out.println(1)"));
        assertTrue($methodCall.of("System.out.println($any$)").matches( "System.out.println(1,2)"));

    }
    public void testName(){

        //IF there is only a single token, and there is no ('s we assume you are presenting the name
        $methodCall $mc = $methodCall.of("split");
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Ex.methodCallEx(()-> System.out.println("a"))) );

        $mc = $methodCall.of("split").$scope($e.of("\"eric\""));
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Ex.methodCallEx(()-> System.out.println("a"))) );
    }

    public void testArguments(){

        $methodCall $mc = $methodCall.of($arguments.of(a->a.isEmpty()));
        assertTrue($mc.matches("System.out.println();"));
        assertFalse($mc.matches("System.out.println(1);"));

    }

    public String mc(){
        return "text";
    }
    public void testScope(){
        $methodCall $mc = $methodCall.of();
        assertTrue($mc.isMatchAny());

        //make sure it matches ANY method call
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertTrue( $mc.matches(Ex.methodCallEx(()-> System.out.println("a"))) );

        //scope that is a specific string
        $mc.$scope("\"eric\"");
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Ex.methodCallEx(()-> "deric".split("a"))) );

        //specific scope
        $mc.$scope( _string.of("eric"));
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Ex.methodCallEx(()-> "deric".split("a"))) );

        //by class type
        $mc = new $methodCall().$scope(_string.class);
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Ex.methodCallEx(()-> System.out.println("a"))) );

        //by more than one class type
        $mc = new $methodCall().$scope(_string.class, _methodCall.class);
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertTrue( $mc.matches(Ex.methodCallEx(()-> mc().split("a"))) );
        assertFalse( $mc.matches(Ex.methodCallEx(()-> System.out.println("a"))) );

        $mc = new $methodCall().$scope(e-> e instanceof _string);
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertFalse( $mc.matches(Ex.methodCallEx(()-> System.out.println("a"))) );

        $mc = new $methodCall().$scope(e-> e != null);
        assertTrue( $mc.matches(Ex.methodCallEx(()-> "eric".split("a"))) );
        assertTrue( $mc.matches(Ex.methodCallEx(()-> System.out.println("a"))) );
        assertFalse( $mc.matches( Ex.methodCallEx( ()-> mc())));

    }
}
