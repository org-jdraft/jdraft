/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.proto;

import org.jdraft.proto.$parameter;
import org.jdraft._parameter;
import org.jdraft._typeRef;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SparameterTest extends TestCase {
    
    public void testCompose(){
        $parameter $p = $parameter.of("int i");        
        _parameter _p = _parameter.of("int i");
        
        assertEquals( _p, $p.construct() );
        
        assertEquals( _p, $parameter.of("$type$ i").construct("type", int.class));        
        assertEquals( _p, $parameter.of("int $name$").construct("name", "i"));
        assertEquals( _p, $parameter.of("$type$ $name$").construct("name", "i", "type", int.class));   
        
        $p = $parameter.of("final String... nm");
        
        assertEquals( _parameter.of("final String... nm"), $p.construct());
        
        //verify they MUST be both vararg and final
        assertNotSame( _parameter.of("String... nm"), $p.construct());
        assertNotSame( _parameter.of("final String nm"), $p.construct());        
        
        $p = $parameter.of("$type$ name");
        
        assertEquals(_parameter.of(" String name"), $p.construct("type", String.class));
        
        //make sure if I have (one or more) annos they are composed 
        $p = $parameter.of("@A int i");        
        assertEquals(_parameter.of("@A int i"), $p.construct());
        
        $p = $parameter.of("@A @B @C final int... i");
        assertEquals(_parameter.of("@A @B @C final int... i"), $p.construct());
        
    }
    
    public void testAnno(){
        $parameter $withAnno = $parameter.of("@Deprecated int i");
        assertTrue($withAnno.matches("@Deprecated int i") );
        assertTrue($withAnno.matches("@Deprecated @A int i") );
        assertTrue($withAnno.matches("@B @Deprecated @A int i") );
        
        assertFalse($withAnno.matches("int i") );
        assertFalse($withAnno.matches("@B @A int i") );
        assertFalse($withAnno.matches("@Deprecated String i") );
        assertFalse($withAnno.matches("@Deprecated int j") );        
    }
    
    public void testMatches(){
        assertTrue( $parameter.of("int i").matches("int i") );
        assertTrue( $parameter.of("final int i").matches("final int i") );
        assertTrue( $parameter.of("final int... i").matches("final int... i") );
        
        assertTrue( $parameter.of("int... i").matches("final int... i") );
        assertTrue( $parameter.of("int... i").matches("int... i") );
        
        assertTrue( $parameter.of("final int i").matches("final int... i") );
        assertTrue( $parameter.of("final int i").matches("final int i") );
        
        assertFalse( $parameter.of("final int i").matches("int i") );
        
        assertTrue( $parameter.of("final $type$ i").matches("final int i") );
        assertTrue( $parameter.of("final $type$ i").matches("final int... i") );
        assertTrue( $parameter.of("final $type$ i").matches("final String... i") );
        
        assertFalse( $parameter.of("final $type$ i").matches("String... i") );
        
        
        assertTrue( $parameter.of("final $type$ $name$").matches("final int i") );
        assertTrue( $parameter.of("final $type$ $name$").matches("final int... i") );
        assertTrue( $parameter.of("final $type$ $name$").matches("final String... i") );        
    }
    
    public void testSelect(){
        assertNotNull( $parameter.of("int i").select("int i") );
        assertTrue( $parameter.of("$type$ i").select("int i").is("type", int.class));
        assertTrue( $parameter.of("$type$ i").select("int i").isType(int.class));
        
        assertTrue( $parameter.of("$type$ i").select("final int i").isType(int.class));        
        assertTrue( $parameter.of("$type$ i").select("final int... i").isType(int.class));
        
        assertTrue( $parameter.of("$type$ i").select("final int... i").isFinal());
        assertTrue( $parameter.of("$type$ i").select("final int... i").isVarArg());
        
        
        //assertEquals( _typeRef.of("String"), _typeRef.of(String.class));
        //isType
        //isParameter
        //isAnno
        //isExpr
        //isName
        //
        assertTrue( $parameter.of("$type$ i").select("String i").is("type", String.class));
        assertTrue( $parameter.of("$type$ i").select("String i").isType(String.class));
        
    }
    
}
