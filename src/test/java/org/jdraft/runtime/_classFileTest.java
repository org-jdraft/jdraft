package org.jdraft.runtime;

import java.nio.file.*;
import java.util.List;
import junit.framework.TestCase;

/**
 * Test building, reading and writing the BytecodeFile
 * @author Eric
 */
public class _classFileTest extends TestCase {
        
    /**
     * Verify I can write and read a Bytecode file 
     */
    public void testWriteAndReadBytecodeFileFromPath(){
        
        //write
        Path dirPath = Paths.get( System.getProperty("java.io.tmpdir"), "bcft" );
        List<_classFile> bcs = _runtime.compile( _javaFile.of("public class A{}") );
        bcs.forEach(b-> b.write(dirPath));
        
        //read
        _classFile bcf = _classFile.of(Paths.get(System.getProperty("java.io.tmpdir"), "bcft", "A.class"));
        //verify classPath
        assertEquals( dirPath, bcf.getClassPath());
        //verify name
        assertEquals("A", bcf.getFullyQualifiedClassName());
        assertEquals("A", bcf.getSimpleName());
        assertEquals("", bcf.getPackageName());        
    }
    
    /**
     * Verify I can write and read a Bytecode file 
     * to/from a directory and if I read it back in I can
     * know the path, resolve the classpath
     */
    public void testWriteAndReadBytecodeFileFromPackagePath(){
        
        //build & write
        Path dirPath = Paths.get( System.getProperty("java.io.tmpdir"), "bcft" );
        List<_classFile> bcs = _runtime.compile( "package aaaa.bbbb.cccc;", "public class D{}");
        bcs.forEach(b-> b.write(dirPath));
        
        //read
        _classFile bcf = _classFile.of(Paths.get(System.getProperty("java.io.tmpdir"), "bcft", "aaaa", "bbbb", "cccc", "D.class"));
        //verify classPath
        assertEquals( dirPath, bcf.getClassPath());
        //verify name
        assertEquals("aaaa.bbbb.cccc.D", bcf.getFullyQualifiedClassName());
        assertEquals("D", bcf.getSimpleName());
        assertEquals("aaaa.bbbb.cccc", bcf.getPackageName());        
    }
}
