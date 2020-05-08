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
}
