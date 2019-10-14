package org.jdraft.pattern;

import org.jdraft._typeParameter._typeParameters;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class StypeParametersTest extends TestCase {

    public void testOf() {
        $typeParameters $tp = $typeParameters.of();
        assertTrue($tp.matches(""));
        assertTrue($tp.matches("A"));
        assertTrue($tp.matches("A extends B"));
        assertTrue($tp.matches("A extends B & C"));
        assertTrue($tp.matches("A extends @NotNull B"));
        assertTrue($tp.matches("A extends @NotNull @Another(1) B & C"));
        assertTrue($tp.matches("A,B,C"));

    }
    public void testAs(){
        $typeParameters $tp = $typeParameters.as("");
        assertTrue($tp.matches(""));

        //none will match... expected NO typeParameters
        assertFalse( $tp.matches("A") );
        assertFalse( $tp.matches("A extends B") );
        assertFalse( $tp.matches("A extends B & C") );
        assertFalse( $tp.matches("A extends @NotNull B") );
        assertFalse( $tp.matches("A extends @NotNull @Another(1) B & C") );
        assertFalse( $tp.matches("A,B,C") );

        $tp = $typeParameters.as("A");
        assertFalse( $tp.matches("") );
        assertTrue( $tp.matches("A") );

        assertFalse( $tp.matches("A extends B") );

        assertFalse( $tp.matches("A extends B & C") );
        assertFalse( $tp.matches("A extends @NotNull B") );
        assertFalse( $tp.matches("A extends @NotNull @Another(1) B & C") );
        assertFalse( $tp.matches("A,B,C") );

        $tp = $typeParameters.as("A extends B");
        assertFalse( $tp.matches("") );
        assertFalse( $tp.matches("A") );

        assertTrue( $tp.matches("A extends B") );
        assertTrue( $tp.matches("A extends bbbb.B") );

        assertFalse( $tp.matches("A extends B & C") );
        assertFalse( $tp.matches("A extends @NotNull B") );
        assertFalse( $tp.matches("A extends @NotNull @Another(1) B & C") );
        assertFalse( $tp.matches("A,B,C") );

        $tp = $typeParameters.as("A extends @NotNull @Another(1) B & C, D");
        assertTrue( $tp.matches("A extends @NotNull @Another(1) B & C, D"));
        //this doesnt match, probably should
        //assertTrue( $tp.matches("A extends @NotNull @Another(1) bbbb.B & cccc.C, dddd.D"));

        assertFalse( $tp.matches("A extends B & C") );
        assertFalse( $tp.matches("A extends @NotNull B") );
        assertFalse( $tp.matches("A extends @NotNull @Another(1) B & C") );
        assertFalse( $tp.matches("A,B,C") );
    }


    public void testCompose(){
        System.out.println( $typeParameters.of("A").draft());
        assertEquals( _typeParameters.of("A"), $typeParameters.of("A").draft());
        assertEquals( _typeParameters.of("A,B"), $typeParameters.of("A,B").draft());
        assertEquals( _typeParameters.of("A,B"), $typeParameters.of("<A,B>").draft());
        
        assertEquals( _typeParameters.of(), $typeParameters.of().draft()); //any
        assertEquals( _typeParameters.of(), $typeParameters.none().draft()); //none
        assertEquals( _typeParameters.of("NAME"), $typeParameters.of("$name$").draft("name","NAME"));
        
    }
    
    public void testComposeParameterOverride(){
        assertEquals( _typeParameters.of("A"), 
             $typeParameters.none().draft("$typeParameters", _typeParameters.of("A")));
    }
    
    public void testSelectMatch(){
        assertTrue( $typeParameters.of().matches("A") );
        assertTrue( $typeParameters.of().matches("A extends B & C") );
        assertTrue( $typeParameters.of().matches("A extends B & C, D, E") );
        assertTrue( $typeParameters.of().matches(_typeParameters.of()) );
        
        assertTrue( $typeParameters.of("A extends B & C, D, E").matches( "A extends B & C, D, E"));
        assertTrue( $typeParameters.of("A extends B & C, D, E").matches( "D, E, A extends B & C"));
        
    }
}
