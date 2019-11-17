package test.byexample.batch;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.io._archive;
import org.jdraft.io._cache;
import org.jdraft.io._path;
import org.jdraft.pattern.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * _code._provider
 *
 * jdraft has the _batch and _archive abstractions for reading in multiple
 */
public class _path_archive_cacheTest extends TestCase {

    /* _path */
    public void test_Read_path(){
        // _path represents a file System path & all files (i.e. .java source files) in the path
        _path _testsPath = _path.of("C:\\jdraft\\project\\jdraft\\src\\test\\java");

        // collect all the file Path (not just .java source files) in the test _path recursively
        List<Path> totalPaths = new ArrayList<>();
        _testsPath.forFilePaths( p-> totalPaths.add(p) );
        System.out.println( "FOUND ["+ totalPaths.size() +"] total files in "+_testsPath );

        // collect files that DO NOT end in "Test.java" in the test _path
        List<Path> notTestPaths = new ArrayList<>();
        _testsPath.forFilePaths( p -> !p.toString().endsWith("Test.java"), p-> notTestPaths.add(p));

        System.out.println( "FOUND ["+ notTestPaths.size() +"] total non-test files"+_testsPath );

        totalPaths.removeAll( notTestPaths );
        System.out.println( "FOUND ["+ totalPaths.size() +"] test files"+_testsPath );
    }

    public void test_path_type_code(){
        _path _testsPath = _path.of("C:\\jdraft\\project\\jdraft\\src\\test\\java");
        // print the name of all top level interfaces
        _testsPath.for_code(_interface.class, _i-> System.out.println( _i.getFullName() ) );

        //IF
        // find all types (this includes nested types & local classes)
        //System.out.println( $type.of().count(_testsPath) );
    }

    public void test_archive_code_cache(){
        // _archive represents the files and paths (i.e. .java source files) within a jar

        _archive _javaParserSrc = _archive.of("C:\\Users\\Eric\\.m2\\repository\\com\\github\\javaparser\\javaparser-core\\3.15.3\\javaparser-core-3.15.3-sources.jar");
        System.out.println("*** TODOs in JavaParser "); $comment.of("TODO").printIn(_javaParserSrc);

        _archive _jdraftSrc = _archive.of("C:\\Users\\Eric\\.m2\\repository\\org\\jdraft\\jdraft\\1.0\\jdraft-1.0-sources.jar");
        System.out.println("*** TODOs in Jdraft "); $comment.of("TODO").printIn(_jdraftSrc);

        // since we want to parse the source code to AST ONLY ONCE & NOT FOR EACH QUERY
        // ...we create a code.cache with the source code we want to query on (based on the code in the archives)
        _cache _jdraftAllSrc = _cache.of(_javaParserSrc, _jdraftSrc);

        //here just query all the code for some information
        System.out.println("*** TODOs "); $comment.of("TODO").printIn(_jdraftAllSrc);
        System.out.println("*** Synchronized statements "); $.synchronizedStmt().printIn(_jdraftAllSrc); //print any
        System.out.println("*** Package names "); $.packageDecl().streamIn(_jdraftAllSrc).map( p-> p.getNameAsString()).distinct().forEach(p -> System.out.println(p));

        System.out.println("*** Thread.currentThread() "); $node.of(BodyDeclaration.class).$hasDescendant($ex.methodCallEx("Thread.currentThread()")).printIn(_jdraftAllSrc );
        // create ANOTHER _code._cache which includes the jdraft test sources
        // (this will only PARSE the jdraft test sources, and reuse the pre-parsed models in _jdraftSrcAll)
        _cache _jdraftAllSrcTests = _cache.of( _jdraftAllSrc, _path.of("C:\\jdraft\\project\\jdraft\\src\\test\\java") );

    }

}
