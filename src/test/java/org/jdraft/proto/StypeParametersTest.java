package org.jdraft.proto;

import org.jdraft.proto.$typeParameters;
import org.jdraft._typeParameter;
import org.jdraft._typeParameter._typeParameters;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class StypeParametersTest extends TestCase {
    
    public void testCompose(){
        System.out.println( $typeParameters.of("A").construct());
        assertEquals( _typeParameters.of("A"), $typeParameters.of("A").construct());
        assertEquals( _typeParameters.of("A,B"), $typeParameters.of("A,B").construct());
        assertEquals( _typeParameters.of("A,B"), $typeParameters.of("<A,B>").construct());
        
        assertEquals( _typeParameters.of(), $typeParameters.any().construct()); //any
        assertEquals( _typeParameters.of(), $typeParameters.none().construct()); //none
        assertEquals( _typeParameters.of("NAME"), $typeParameters.of("$name$").construct("name","NAME"));
        
    }
    
    public void testComposeParameterOverride(){
        assertEquals( _typeParameters.of("A"), 
             $typeParameters.none().construct("$typeParameters", _typeParameters.of("A")));        
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
