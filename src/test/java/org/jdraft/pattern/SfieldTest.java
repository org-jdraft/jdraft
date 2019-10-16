package org.jdraft.pattern;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SfieldTest extends TestCase {

    public void testFieldOfMatch(){
        $field $f = $field.of();
        assertTrue( $f.matches("int x;"));
        assertTrue( $f.matches("public static final int x;"));
        assertTrue( $f.matches("@Ann int x;"));
        assertTrue( $f.matches("int x=1;"));

        $f = $field.of("int $name$;");
        assertTrue( $f.matches("int x;"));
        assertTrue( $f.matches("int[] x;"));
        assertTrue( $f.matches("public static final int x;"));
        assertTrue( $f.matches("@Ann int x;"));
        assertTrue( $f.matches("int x=1;"));
        assertTrue( $f.matches("@Ann public static final int x=102;"));
        assertTrue( $f.matches("@Ann public static final int[] x=102;"));

        assertFalse( $f.matches("String x" )); //must be int

        $f = $field.of("$type$ x;");
        assertTrue( $f.matches("int x;"));
        assertTrue( $f.matches("int[] x;"));
        assertTrue( $f.matches("String x;"));
        assertTrue( $f.matches("@Ann public static final int[] x = 102;"));

        $f = $field.of("@Deprecated int x;");

        assertTrue( $f.matches("@Deprecated int x;"));
        assertTrue( $f.matches("@Deprecated @Another int x;"));
        assertTrue( $f.matches("@Deprecated int[] x;"));
        assertTrue( $f.matches("@Deprecated int x=123;"));
        assertTrue( $f.matches("@Deprecated public static final int x=123;"));

        assertFalse( $f.matches("int x;"));
        assertFalse( $f.matches("@Another int[] x;"));
        assertFalse( $f.matches("String x;"));
        assertFalse( $f.matches("@Ann public static final int[] x = 102;"));

        $f = $field.of("@Deprecated @Ann public static final Map<String,Integer> mm = new HashMap<>();");
        assertTrue( $f.matches("@Deprecated @Ann public static final Map<String,Integer> mm = new HashMap<>();"));
        assertTrue( $f.matches("@Ann @Deprecated @Gm public static final transient java.util.Map<java.lang.String,java.lang.Integer> mm = new HashMap<>();"));

        assertFalse( $f.matches("@Ann @Gm public static final transient java.util.Map<java.lang.String,java.lang.Integer> mm = new HashMap<>();"));

        assertFalse( $f.matches("@Ann @Deprecated @Gm public final transient java.util.Map<java.lang.String,java.lang.Integer> mm = new HashMap<>();"));

        assertFalse( $f.matches("@Ann @Deprecated @Gm public static final transient java.util.Map<Integer, String> mm2 = new HashMap<>();"));

        assertFalse( $f.matches("@Ann @Deprecated @Gm public static final transient java.util.Map<java.lang.String,java.lang.Integer> m2 = new HashMap<>();"));

    }

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
