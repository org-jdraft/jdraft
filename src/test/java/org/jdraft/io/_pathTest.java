package org.jdraft.io;

import junit.framework.TestCase;
import org.jdraft.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.io.File;
import java.util.Map;

public class _pathTest extends TestCase {

    static Path BASE_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "_batchTest");

    public void setUp() throws IOException {
        //write (3) files
        List<Path> written;
        written = _io.out(BASE_DIR,_class.of("aaaa.A"), _class.of("bbbb.B"), _class.of("C"), _packageInfo.of("package aaaa;"), _moduleInfo.of("module a{}"));

        //add a text file
        Files.write(Paths.get(BASE_DIR.toString(), "TextFile.txt"), "This is text".getBytes(), StandardOpenOption.CREATE_NEW);
    }

    public void testB(){
        _path _b = _path.of(BASE_DIR);
        assertEquals(6, _b.list().size()); //includes "textFile.txt" and (5) .java files
        assertEquals(5, _b.list(_path.JAVA_FILES_ONLY).size()); //excludes "textFile.txt
        assertEquals(3, _b.list(_path.JAVA_TYPES_ONLY).size()); //excludes "package-info.java" "module-info.java"

        List<_codeUnit> _cs = _b.for_code(_c -> _c.addImports(Map.class)); //add an import to all
        _cs.forEach( c-> assertTrue(c.hasImport(Map.class)));
        assertEquals( 5, _cs.size());

        List<_type> _ts = _b.for_types(t-> t.addField("public static final int ID=1;"));
        _ts.forEach( t-> assertTrue(t.getField("ID")!= null));
        assertEquals( 3, _ts.size());

        //look through
        List<_class>_c2 = _b.for_code(_class.class, c-> c.isInPackage("aaaa"), c-> c.addMethod("public int ff(){ return 123; }"));
        assertEquals(1, _c2.size());
        assertTrue( _c2.get(0).getMethod("ff").isType(int.class));
    }



    public void testRelativize(){
        Path basePath = Paths.get("C:\\ren");
        Path path = Paths.get("C:\\ren\\javaparser\\javaparser-core");
        Path rel = path.relativize(basePath);
        assertEquals( "javaparser\\javaparser-core", basePath.relativize(path).toString());
    }

    public void testBP(){
        Path base = Paths.get("C:/temp/MyProject/src/test/java");
        Path sub = Paths.get("C:/temp/MyProject/src/test/java", "aaaa", "bbbb", "C.java");
        Path other = Paths.get("blah/blorg");
        Path rel1 = sub.relativize(base);

        Path rel2 = base.relativize(sub);

        //System.out.println( rel1);
        //System.out.println( rel2);

        //System.out.println( base.relativize(other));
    }

    public static final String tmpDir = System.getProperty("java.io.tmpdir");
    public static final Path srcRoot = Paths.get(tmpDir, "draft_batchTest", "src", "main", "java");

    public static void buildExampleFiles(){
        /** this is setup routines for creating sample files */

        Path root = Paths.get(tmpDir, "draft_batchTest");

        //Path classesRoot = Paths.get(tmpDir, "draft_batchTest", "src", "main", "java", "classes");
        Path simpleJavaFileInPath =
                Paths.get(tmpDir, "draft_batchTest", "src", "main", "java", "com", "github", "javaparser", "ast", "A.java");
        simpleJavaFileInPath.getParent().toFile().mkdirs();

        Path packageInfoFileInPath =
                Paths.get(tmpDir, "draft_batchTest", "src", "main", "java", "com", "github", "javaparser", "ast", "package-info.java");

        Path moduleInfoPath =
                Paths.get(tmpDir, "draft_batchTest", "src","main", "java", "aaaa", "classes", "module-info.java");

        moduleInfoPath.getParent().toFile().mkdirs();

        try{

            Files.write(
                    simpleJavaFileInPath,
                    ("package com.github.javaparser.ast;"+System.lineSeparator()+"public class A{}").getBytes());

            Files.write(packageInfoFileInPath,
                    ("/* License Erics 101 */"+ System.lineSeparator() +
                            "package com.github.javaparser.ast;"+System.lineSeparator()+
                            "/** Javadoc Comment */").getBytes());

            Files.write(moduleInfoPath,
                    ("/* License 101 */"    + System.lineSeparator()+
                            "import java.util.Map;" + System.lineSeparator()+
                            "module aaaa {" + System.lineSeparator()+
                            "    requires bbbb;" + System.lineSeparator()+
                            "    requires static cccc;" + System.lineSeparator()+
                            "}").getBytes());
        }catch(Exception e){
            throw new RuntimeException("unable to write", e);
        }
    }

    public void tearDown(){
        deleteDirectory(BASE_DIR.toFile());
    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
