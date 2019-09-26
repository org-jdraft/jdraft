package org.jdraft.pattern;

import org.jdraft._typeParameter._typeParameters;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class StypeParametersTest extends TestCase {
    
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
