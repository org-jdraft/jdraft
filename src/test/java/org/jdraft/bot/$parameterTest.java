package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._parameter;

import java.util.Map;

public class $parameterTest extends TestCase {

    public void testMatchAny(){
        assertTrue($parameter.of().matches("int i"));
        assertTrue($parameter.of().matches("@a @b(1) @c(k=1,v=2) final Map<String,Integer>... i"));
        assertTrue( $parameter.of().isMatchAny() );
    }

    public void testType(){
        assertTrue($parameter.of(Map.class).matches("Map m"));
        assertTrue($parameter.of(Map.class).matches("Map m"));

        assertTrue($parameter.of(Map.class).matches("@a final Map m"));
        assertTrue($parameter.of().$name("m").matches("Map m"));
    }

    public void testParameterized(){
        assertTrue( $parameter.of("$type$ $name$").select("t n")
                .is("type", "t", "name", "n"));
    }
    public void testExpectFinal(){
        assertTrue($parameter.of().$isFinal(true).matches("final int i"));
        assertFalse($parameter.of().$isFinal(true).matches("int i"));

        assertTrue($parameter.of().$isFinal(false).matches("int i"));
        assertFalse($parameter.of().$isFinal(false).matches("final int i"));
    }

    public void testExpectVararg(){
        assertTrue($parameter.of().$isVarArg(true).matches("int...i"));
        assertFalse($parameter.of().$isVarArg(true).matches("int i"));

        assertFalse($parameter.of().$isVarArg(false).matches("int...i"));
        assertTrue($parameter.of().$isVarArg(false).matches("int i"));
    }

    public void testLenientMatch(){
        assertTrue( $parameter.of("int i").matches(_parameter.of("int i")));
        assertTrue( $parameter.of("int i").matches("@a final int i"));
    }

    public void testAsMatch(){
        assertTrue( $parameter.as("int i").matches(_parameter.of("int i")));
        assertFalse( $parameter.as("int i").matches("@a final int i"));

        assertTrue( $parameter.as("@a final int...i").matches("@a final int...i"));
    }

    public void testCopy(){
        $parameter $a = $parameter.of("int a");

        assertTrue($a.matches("int a"));
        assertTrue($a.matches("int... a"));
        $parameter $acopy = $a.copy();
        assertTrue($acopy.matches("int a"));
        assertTrue($acopy.matches("int... a"));

        $acopy.$isVarArg(false);
        assertTrue($acopy.matches("int a"));
        assertFalse($acopy.matches("int... a"));

        assertTrue($a.matches("int a"));
        assertTrue($a.matches("int... a"));



        $parameter $s = $parameter.of("String s");
        assertTrue($s.matches("String s"));
        assertTrue($s.matches("String... s"));

    }
    public void testOrCopy(){
        $parameter $a = $parameter.of("int a");
        $parameter $s = $parameter.of("String s");
        $parameter $por = $parameter.or( $a, $s );

        assertTrue($por.matches("int a"));
        assertTrue($por.matches("String s"));
        assertTrue($por.matches("int... a"));
        assertTrue($por.matches("String... s"));

        $a.$isVarArg(false);
        $s.$isVarArg(false);

        assertFalse($por.matches("int... a"));
        assertFalse($por.matches("String... s"));

        $parameter $acopy = $a.copy();
        $parameter $scopy = $s.copy();


        //copy the
        $parameter $pcopy = $por.copy();
        assertTrue($pcopy.matches("int a"));
        assertTrue($pcopy.matches("String s"));

        assertFalse($pcopy.matches("int... a"));
        assertFalse($pcopy.matches("String... s"));

        //now mutate the copy
        //$pcopy.


    }
}
