package org.jdraft.runtime;

import java.io.IOException;
import java.util.*;
import javax.tools.*;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._java;

/**
 *
 * @author Eric
 */
public class _fileManagerTest extends TestCase {
    
    public void testFM() throws IOException{
         _runtime ahi = _runtime.of( "package aaaa.bbbb;", "public class GGGG{}");
        
        _fileManager fm = ahi.fileManager;
        ClassLoader cl =  fm.classLoader;
        fm.flush();
        assertEquals( cl, fm.getClassLoader(StandardLocation.SOURCE_PATH));
        
        JavaFileObject fo = 
            fm.getJavaFileForInput(StandardLocation.SOURCE_PATH, "aaaa.bbbb.GGGG", JavaFileObject.Kind.SOURCE);
        assertNotNull( fo );
        
        fo = 
            fm.getJavaFileForInput(StandardLocation.CLASS_OUTPUT, "aaaa.bbbb.GGGG", JavaFileObject.Kind.CLASS);
        assertNotNull( fo );
        Location location =StandardLocation.SOURCE_OUTPUT;
        String packageName = "aaaa.bbbb";
        Set<Kind> kinds = new HashSet<>();
        kinds.add(Kind.SOURCE);
        kinds.add(Kind.CLASS);
        
        boolean recurse = true;
        Iterable<JavaFileObject> listF =
            fm.list(location, packageName, kinds, recurse);
        List<FileObject> fos = new ArrayList<>();
        listF.forEach(f-> fos.add(f));
        assertEquals(2, fos.size() );               
    }
    
    /**
     * Tests the 
     * {@link _fileManager#list(javax.tools.JavaFileManager.Location, java.lang.String, java.util.Set, boolean)
     * function using: 
     * <UL>
     *    <LI>the package name 
     *    <LI>Kinds
     *    <LI>recurse options
     * </UL>
     * 
     * @throws IOException 
     */
    public void testMultipleListRecurse() throws IOException{
        _runtime ahi = _runtime.of(
            _class.of("public class Root{}"),
            _class.of("package basepkg;", "public class BasePkg{}"),
            _class.of("package basepkg.subpkg;", "public class SubPkg{}"),
            _class.of("package basepkg.subpkg;", "public class Another{}"),
            _class.of("package basepkg.subpkg.subsub;", "public class SS{}")
        );
        _fileManager ahfm = ahi.fileManager;
        
        Set<JavaFileObject.Kind> srcOnly = new HashSet<>();
        Set<JavaFileObject.Kind> srcClass = new HashSet<>();
        srcClass.add(JavaFileObject.Kind.SOURCE);
        srcOnly.add(JavaFileObject.Kind.SOURCE);
        
        List<JavaFileObject> jfos = (List<JavaFileObject>)
             ahfm.list(StandardLocation.SOURCE_PATH, "", srcOnly, true);
        assertEquals( 5, jfos.size());
        srcClass.add(Kind.CLASS);
        jfos = (List<JavaFileObject>)
             ahfm.list(StandardLocation.SOURCE_PATH, "", srcClass, true);
        assertEquals( 10, jfos.size());

        jfos = (List<JavaFileObject>)
             ahfm.list(StandardLocation.SOURCE_PATH, "basepkg", srcClass, true);
        assertEquals( 8, jfos.size());
        
        jfos = (List<JavaFileObject>)
             ahfm.list(StandardLocation.SOURCE_PATH, "basepkg.subpkg", srcClass, true);
        assertEquals( 6, jfos.size());
        
        jfos = (List<JavaFileObject>)
             ahfm.list(StandardLocation.SOURCE_PATH, "basepkg.subpkg", srcClass, false);
        assertEquals( 4, jfos.size());
        
        jfos = (List<JavaFileObject>)
             ahfm.list(StandardLocation.SOURCE_PATH, "basepkg.subpkg", srcOnly, false);
        assertEquals( 2, jfos.size());
        
    }
}
