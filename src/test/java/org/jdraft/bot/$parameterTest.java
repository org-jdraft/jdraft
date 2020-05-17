package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._param;

import java.util.Map;

public class $parameterTest extends TestCase {

    public void testMatchAny(){
        assertTrue($param.of().matches("int i"));
        assertTrue($param.of().matches("@a @b(1) @c(k=1,v=2) final Map<String,Integer>... i"));
        assertTrue( $param.of().isMatchAny() );
    }

    public void testType(){
        assertTrue($param.of(Map.class).matches("Map m"));
        assertTrue($param.of(Map.class).matches("Map m"));

        assertTrue($param.of(Map.class).matches("@a final Map m"));
        assertTrue($param.of().$name("m").matches("Map m"));
    }

    public void testParameterized(){
        assertNotNull( $param.of("$type$ $name$").select("t n") );

        assertTrue( $param.of("$type$ $name$").select("t n")
                .is("type", "t", "name", "n"));
    }
    public void testExpectFinal(){
        assertTrue($param.of().$isFinal(true).matches("final int i"));
        assertFalse($param.of().$isFinal(true).matches("int i"));

        assertTrue($param.of().$isFinal(false).matches("int i"));
        assertFalse($param.of().$isFinal(false).matches("final int i"));
    }

    public void testExpectVararg(){
        assertTrue($param.of().$isVarArg(true).matches("int...i"));
        assertFalse($param.of().$isVarArg(true).matches("int i"));

        assertFalse($param.of().$isVarArg(false).matches("int...i"));
        assertTrue($param.of().$isVarArg(false).matches("int i"));
    }

    public void testLenientMatch(){
        assertTrue( $param.of("int i").matches(_param.of("int i")));
        assertTrue( $param.of("int i").matches("@a final int i"));
    }

    public void testAsMatch(){
        assertTrue( $param.as("int i").matches(_param.of("int i")));
        assertFalse( $param.as("int i").matches("@a final int i"));

        assertTrue( $param.as("@a final int...i").matches("@a final int...i"));
    }

    public void testCopy(){
        $param $a = $param.of("int a");

        assertTrue($a.matches("int a"));
        assertTrue($a.matches("int... a"));
        $param $acopy = $a.copy();
        assertTrue($acopy.matches("int a"));
        assertTrue($acopy.matches("int... a"));

        $acopy.$isVarArg(false);
        assertTrue($acopy.matches("int a"));
        assertFalse($acopy.matches("int... a"));

        assertTrue($a.matches("int a"));
        assertTrue($a.matches("int... a"));



        $param $s = $param.of("String s");
        assertTrue($s.matches("String s"));
        assertTrue($s.matches("String... s"));

    }
    public void testOrCopy(){
        $param $a = $param.of("int a");
        $param $s = $param.of("String s");
        $param $por = $param.or( $a, $s );

        assertTrue($por.matches("int a"));
        assertTrue($por.matches("String s"));
        assertTrue($por.matches("int... a"));
        assertTrue($por.matches("String... s"));

        $a.$isVarArg(false);
        $s.$isVarArg(false);

        assertFalse($por.matches("int... a"));
        assertFalse($por.matches("String... s"));

        $param $acopy = $a.copy();
        $param $scopy = $s.copy();


        //copy the
        $param $pcopy = $por.copy();
        assertTrue($pcopy.matches("int a"));
        assertTrue($pcopy.matches("String s"));

        assertFalse($pcopy.matches("int... a"));
        assertFalse($pcopy.matches("String... s"));

        //now mutate the copy
        //$pcopy.


    }
}
