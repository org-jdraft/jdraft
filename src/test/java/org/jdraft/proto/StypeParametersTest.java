package org.jdraft.proto;

import org.jdraft._typeParameter._typeParameters;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class StypeParametersTest extends TestCase {
    
    public void testCompose(){
        System.out.println( $typeParameters.of("A").compose());
        assertEquals( _typeParameters.of("A"), $typeParameters.of("A").compose());
        assertEquals( _typeParameters.of("A,B"), $typeParameters.of("A,B").compose());
        assertEquals( _typeParameters.of("A,B"), $typeParameters.of("<A,B>").compose());
        
        assertEquals( _typeParameters.of(), $typeParameters.any().compose()); //any
        assertEquals( _typeParameters.of(), $typeParameters.none().compose()); //none
        assertEquals( _typeParameters.of("NAME"), $typeParameters.of("$name$").compose("name","NAME"));
        
    }
    
    public void testComposeParameterOverride(){
        assertEquals( _typeParameters.of("A"), 
             $typeParameters.none().compose("$typeParameters", _typeParameters.of("A")));
    }
    
    public void testSelectMatch(){
        assertTrue( $typeParameters.any().matches("A") );
        assertTrue( $typeParameters.any().matches("A extends B & C") );
        assertTrue( $typeParameters.any().matches("A extends B & C, D, E") );
        assertTrue( $typeParameters.any().matches(_typeParameters.of()) );
        
        assertTrue( $typeParameters.of("A extends B & C, D, E").matches( "A extends B & C, D, E"));
        assertTrue( $typeParameters.of("A extends B & C, D, E").matches( "D, E, A extends B & C"));
        
    }
}
