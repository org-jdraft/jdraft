package org.jdraft.pattern;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SfieldTest extends TestCase {
    
    public void testF(){
        $field $f = $field.of($anno.of(Deprecated.class));
    }
    
    public void testSimple(){
        $field $fi = $field.of("public int z=100;");
        assertTrue( $fi.matches("public int z = 100;") );
        assertFalse( $fi.matches("public int z = 5;") );
        
        $fi.$init();
        //System.out.println( $fi );
        assertTrue( $fi.matches("public int z = 100;") );
        assertTrue( $fi.matches("public int z = 5;") );
        
    }
    
    public void testIso(){
        assertTrue( $field.of("@A @B int i=0;").matches("@B @A int i=0;"));
    }
    
    public void testWithAnno(){
        
        assertTrue( $field.of("@A int i;").matches("@A int i;"));
        assertTrue( $field.of("@A int i;").matches("@A int i=0;"));        
        assertTrue( $field.of("@A int i=0;").matches("@A int i=0;"));        
        //expected annp
        assertFalse( $field.of("@A int i=0;").matches("int i=0;"));
        
        assertFalse( $field.of("@A @B int i=0;").matches("int i=0;"));
        assertFalse( $field.of("@A @B int i=0;").matches("@B int i=0;"));
        assertFalse( $field.of("@A @B int i=0;").matches("@A int i=0;"));        
    }    
}
