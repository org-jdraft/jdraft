package org.jdraft.runtime;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _runtimeClassLoaderTest extends TestCase {
    
    /**
     * Verify we can get resources (like .java files and .class files) from
     * 
     * @throws IOException 
     */
    public void testClassLoaderApi() throws IOException{
        _runtime ah = _runtime.of("package xxxx.yyyy;", "public class ZZ{}");
        ClassLoader cl = ah.getClassLoader();
        /* JDK 9 > 
        cl.getDefinedPackage("xxxx.yyyy");
        Package[] pkgs = cl.getDefinedPackages();
        assertTrue( cl.isRegisteredAsParallelCapable() );
        assertEquals( AdhocClassLoader.class.getSimpleName(), cl.getName());
        */
        //URL url = cl.getResource("xxxx.yyyy.ZZ.java");
        //System.out.println(url);
        URL javaExpect = new URL("file://xxxx.yyyy.ZZ.java"); 
        URL classExpect = new URL("file://xxxx.yyyy.ZZ.class"); 
        assertEquals(javaExpect, cl.getResource("xxxx.yyyy.ZZ.java"));
        assertEquals(classExpect, cl.getResource("xxxx.yyyy.ZZ.class"));
        
        assertEquals(javaExpect, cl.getResources("xxxx.yyyy.ZZ.java").nextElement());
        assertEquals(classExpect, cl.getResources("xxxx.yyyy.ZZ.class").nextElement());
        //assertTrue( cl.getResources("xxxx.yyyy.ZZ.java").hasMoreElements() );
        //assertTrue( cl.getResources("xxxx.yyyy.ZZ.class").hasMoreElements() );        
    }
   
    /**
     * Verify I can "manually" create an AdHocClassLoader
     * 
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    public void testManualClassLoaderLoad() throws ClassNotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        
        List<_classFile> bcf = _runtime.compile("package abcd.efg;", "public class H{ public static final int STAT = 102;}"); //only compile (dont load the classes)
        List<_javaFile> scfs = new ArrayList<>();
        //scfs.add( sc);
        _classLoader cl = _classLoader.of(_runtimeClassLoaderTest.class.getClassLoader(), bcf, scfs);
        //System.out.println( cl.classNameToBytecodeFile );
        //System.out.println( cl.classNameToCodeModelFile );
        Class clazz = cl.loadClass("abcd.efg.H");
        assertNotNull(clazz);
        assertEquals( 102, clazz.getField("STAT").get(null));
    }
    
    public void testClassLoaderPackage() throws ClassNotFoundException, IOException{
        //StringCodeFile sc = StringCodeFile.of("abcd.DDD", "package abcd;", "public class DDD{ public static final int STAT = 102;}");        
        _javaFile _jf = _javaFile.of("package abcd;", "public class DDD{ public static final int STAT = 102;}");
        List<_classFile> bcf = _runtime.compile(_jf); //only compile (dont load the classes)
        List<_javaFile> scfs = new ArrayList<>();
        scfs.add( _jf);
        _classLoader cl = _classLoader.of(_runtimeClassLoaderTest.class.getClassLoader(), bcf, scfs);
        
        assertNotNull( cl.findClass("abcd.DDD") );
        assertTrue( cl.findResources("abcd.DDD.java").hasMoreElements());
        assertNotNull( cl.get_code(cl.loadClass("abcd.DDD")) );
        
        
        //verify that we've created a package in a new classLoader
        /* JDK 9 +
        Package[] pkgs = cl.getDefinedPackages();
        assertEquals(1, pkgs.length );
        
        Package pkg = cl.getDefinedPackage("abcd");
        assertNotNull( pkg );
        assertEquals(0, pkg.getAnnotations().length);
        assertEquals("abcd",  pkg.getName() );
        
        assertNull( pkg.getImplementationTitle() );
        assertNull( pkg.getImplementationVendor() );
        assertNull( pkg.getImplementationVersion() );
        
        assertNull( pkg.getSpecificationTitle() );
        assertNull( pkg.getSpecificationVendor() );
        assertNull( pkg.getSpecificationVersion() );        
        */
    }
    
    public void testClassLoaderAPI() throws ClassNotFoundException, IOException{
        _javaFile sc = _javaFile.of( "package abcd;", "public class DDD{ public static final int STAT = 102;}");        
        List<_classFile> bcf = _runtime.compile(sc); //only compile (dont load the classes)
        List<_javaFile> scfs = new ArrayList<>();
        scfs.add( sc);
        _classLoader cl = _classLoader.of(_runtimeClassLoaderTest.class.getClassLoader(), bcf, scfs);
        assertEquals( _runtimeClassLoaderTest.class.getClassLoader(), cl.getParent());
        //assertTrue( cl.isRegisteredAsParallelCapable() );
        
        //these are the extensions to ClassLoader specifically on AdhocClassLoader
        assertEquals( 1, cl.list_classFiles().size());
        assertEquals( 1, cl.list_javaFiles().size());
        assertEquals( 1, cl.list_code().size());
        assertEquals( 1, cl.listClassNames().size());        
    }
    
}
