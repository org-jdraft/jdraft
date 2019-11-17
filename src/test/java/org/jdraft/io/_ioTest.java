package org.jdraft.io;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.Ast;
import org.jdraft._class;
import junit.framework.TestCase;
import org.jdraft._code;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Verify we can set use read and write files easily using the
 * _io system
 *
 */
public class _ioTest extends TestCase {

    public void testWriteCodeProvider(){
        _cache _cc = _cache.of(_class.of("BasePackage"), _class.of("aaaa.bbbb.SubPackage") );
        List<Path> paths = _io.out("C:\\temp", _cc);
        assertEquals( 2, paths.size());
        assertTrue(paths.contains(Paths.get("C:\\temp\\BasePackage.java")));
        assertTrue(paths.contains(Paths.get("C:\\temp\\aaaa\\bbbb\\SubPackage.java")));
    }

    public void testWriteOutCode(){
        _io.setOutDir( Paths.get( System.getProperty("java.io.tmpdir"), "_ioTest") );
        Path path = _io.out(_class.of("aaaa.bbbb.C"));
        assertEquals( Paths.get( System.getProperty("java.io.tmpdir"), "_ioTest", "aaaa", "bbbb", "C.java"), path);
        System.out.println( path );         
    }
    
    public void testConfig(){
        System.out.println( _io.config() );
    }

    public void testInConfig() throws IOException {
        String baseDir = System.getProperty("java.io.tmpdir");
        Files.createDirectories(Paths.get(baseDir));
        Files.write(Paths.get(baseDir, "SomeFile.txt"), "Some text in file".getBytes());
        assertNotNull( _io.in(_io.config().inFilesPath(baseDir), "SomeFile.txt") );

        Files.createDirectories(Paths.get(baseDir,"subdir"));
        Files.write(Paths.get(baseDir,"subdir","SomeFile.txt"), "Some text".getBytes());
        assertNotNull( _io.in(_io.config().inFilesPath(baseDir), "subDir//SomeFile.txt") );
    }
    public void testDefaultReadThisSourceCode(){
        //I SHOULD be able to read this from
        TypeDeclaration td = Ast.typeDecl(_ioTest.class);
        assertEquals( "_ioTest", td.getNameAsString());

        //explicitly use the project to read the .java source code
        td = Ast.typeDecl(_ioTest.class, new _inProject());
        assertEquals( "_ioTest", td.getNameAsString());

        String cn = _ioTest.class.getCanonicalName().replace(".", "/")+".class";
        String jn = _ioTest.class.getCanonicalName().replace(".", "/")+".java";

        assertNotNull( _io.in(_ioTest.class));
        assertNotNull( _io.in(cn));
        assertNotNull( _io.in(jn));

        assertNotNull( ".class file not resolveable", _inClassPath.INSTANCE.resolve(cn) );
        assertNull( "resolved .java file unexpectedly",_inClassPath.INSTANCE.resolve(jn) );

        //verify i cannot in this class using the project (only source code)
        assertNull( "file "+cn + " resolved unexpectedly", new _inProject().resolve(cn) );
    }

    public void testUpdateThreadLocalResolver(){
        //_io.setThreadLocalInResolver();
        //String nm = _ioTest.class.getCanonicalName().replaceIn(".", "/")+".class";

        _io.clearThreadLocalInResolver();
        assertNotNull( "should be able to in class", _io.in( _ioTest.class) );

        _io.setThreadLocalInResolver(_inFilePath.of("C:\\temp")); //change the _io resolver
        assertNull( "shouldnt be able to in class", _io.in( _ioTest.class) );

        _io.clearThreadLocalInResolver(); //clear the threadLocal resolver
        assertNotNull( "should be able to in class", _io.in( _ioTest.class) );

    }

    public void testInFilesPath() throws IOException {
        try {

            //write a file to temp dir
            Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "tempfile.txt"), "Some temp file".getBytes());
            _io.clearInFilesPath();
            assertNull( "shouldnt be able to read it yet", _io.in("tempfile.txt")); //verify we can read it
            //add the temp path
            _io.setInFilesPath(System.getProperty("java.io.tmpdir"));
            _in _i = _io.in("tempfile.txt");
            assertNotNull( _i ); //verify we can read it
            _i.getInputStream().close();
            _io.clearInFilesPath();
            _i = _io.in("tempfile.txt");
            assertNull( "shouldnt be able to read it",_i); //verify we can read it
        } finally {
            Files.deleteIfExists(Paths.get(System.getProperty("java.io.tmpdir"), "tempfile.txt"));
            _io.clearInFilesPath();
        }
    }

    /**
     * Verify that we can set the projectPath and read in resources
     *
     * @throws IOException
     */
    public void testProjectFiles() throws IOException{
        //write a "project"
        File f = Paths.get(System.getProperty("java.io.tmpdir"), "tempproj").toFile();
        f.delete();
        //Files.deleteIfExists(Paths.get(System.getProperty("java.io.tmpdir"), "tempproj"));

        Files.createDirectories( Paths.get(System.getProperty("java.io.tmpdir"), "tempproj"));
        Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "tempfile.txt"), "Some temp file".getBytes());

        Files.createDirectories( Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "src"));
        Files.createDirectories( Paths.get(System.getProperty("java.io.tmpdir"),  "tempproj", "src", "main", "resources"));
        Files.createDirectories( Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "test"));

        Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "src", "A.java"), "public class A{}".getBytes());
        //Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "resources", "R.html"), "<HTML>R".getBytes());
        Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "test", "T.java"), "public class T{}".getBytes());

        Files.createDirectories( Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "src", "main", "java"));
        //Files.createDirectories( Paths.get(System.getProperty("java.io.tmpdir"),  "tempproj", "resources"));
        Files.createDirectories( Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "src", "test", "java"));

        Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "src", "main", "java", "A2.java"), "public class A2{}".getBytes());
        Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "src", "main", "resources", "RR.html"), "<HTML>R".getBytes());
        Files.write(Paths.get(System.getProperty("java.io.tmpdir"), "tempproj", "src", "test", "java", "T2.java"), "public class T2{}".getBytes());

        _io.clearInProjectsPath();
        assertNull( _io.in("A.java"));
        assertNull( _io.in("RR.html"));
        assertNull( _io.in("T.java"));
        assertNull( _io.in("A2.java"));
        assertNull( _io.in("T2.java"));
        _io.setInProjectsPath(System.getProperty("java.io.tmpdir")+"//tempproj");
        assertNotNull( _io.in("A.java"));
        assertNotNull( _io.in("T.java"));
        assertNotNull( _io.in("A2.java"));
        assertNotNull( _io.in("RR.html"));
        assertNotNull( _io.in("T2.java"));

    }

    /*** Requires external projects
    //this
    public static final String baseReadPath = "C:\\dev\\workspaces\\drafted\\src\\";

    public void testRead() throws FileNotFoundException {
        //read from an input Stream
        //FileInputStream fis = new FileInputStream(baseReadPath+"source\\SomeClass.java");

        //read the compilationUnit AST from an external file input stream
        CompilationUnit ast = Ast.of(new FileInputStream(baseReadPath+"source\\SomeClass.java"));
        assertNotNull( ast );

        _type _t = _type.of( new FileInputStream(baseReadPath+"source\\SomeClass.java") );
        assertEquals( "SomeClass", _t.getName() );

        _class _c = _class.of( new FileInputStream(baseReadPath+"source\\SomeClass.java") );
        assertEquals( "SomeClass", _c.getName() );
    }

    public void testUseInProjectsPath(){
        try{
            _io.setInProjectsPath("C:\\dev\\workspaces\\drafted\\");
            _in in = _io.in("source\\SomeClass.java");
            CompilationUnit ast = Ast.of( in.getInputStream() );
            assertNotNull(ast );
        }
        finally{
            _io.clearInProjectsPath();
        }

        //add multiple projects to in projects path
        try{
            _io.setInProjectsPath("C:\\dev\\workspaces\\drafted\\;C:\\dev\\workspaces\\sourceProj\\");
            _in in = _io.in("source\\SomeClass.java");
            CompilationUnit ast = Ast.of( in.getInputStream() );
            assertNotNull(ast );

            in = _io.in("source\\SourceProj.java");
            ast = Ast.of( in.getInputStream() );
            assertNotNull(ast );
        }
        finally{
            _io.clearInProjectsPath();
        }
    }


    public void testModifyIn(){
        try{
            _io.setThreadLocalInResolver( _inFilePath.of( baseReadPath ) );
            _in in = _io.in("source\\SomeClass.java");
            CompilationUnit ast = Ast.of( in.getInputStream() );
            assertNotNull(ast );

            in = _io.in("source/SomeClass.java");
            ast = Ast.of( in.getInputStream() );
            assertNotNull(ast );
        } finally{
            _io.clearThreadLocalInResolver();
        }
    }
    */

    public void tearDown(){
        _io.clearThreadLocalInResolver();
        _io.clearInFilesPath();
        _io.clearInProjectsPath();
        _io.clearOutDir();
    }

}
