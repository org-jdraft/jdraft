package test.quickstart;

import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._code;
import org.jdraft.io._archive;
import org.jdraft.io._path;
import org.jdraft.macro._dto;
import org.jdraft.pattern.*;
import org.jdraft.runtime._runtime;

import java.util.function.Function;

public class WhatKindaStuff extends TestCase {

    class A{
        A(int a, int b){}
        private A(String name){}

        void g(){
            assert(1==1);
        }
        void one(){
            System.out.println(1);
        }
    }

    public void testCT(){
        // you could do this manually
        // open up an IDE and manually make these changes

        // Simple Programs
        String aSrc = _class.of(A.class).forConstructors(m ->m.forParameters(p-> p.setFinal())).toString();
        System.out.println(aSrc);

        //build classes with @macros
        _class _c = _class.of("graph.Point", new @_dto Object(){ double x,y;});

        //create and run dynamic classes
        _runtime _r = _runtime.of(_c);

        int hashCode = (int)_r.eval( "new Point().hashCode()");
        assertEquals( hashCode, _r.eval("new Point().hashCode()"));

        //System.out.println(hashCode);

        //print the name and line counts for each non-static method
        $method.not($.STATIC).forEachIn(A.class, m-> System.out.println("  "+ m + ":" + m.listStatements().size()));

        //comment all (System.out.println() statements in the code and return the modified code
        String src = $stmt.of("System.out.println($any$);").removeIn(A.class).toString();

        System.out.println( src );

        _class.of(A.class).forMethods( m -> $.of( NodeWithBlockStmt.class ).count(m) );

        //print all TODO comments found in source jar file
        $.comment("TODO").printIn( _archive.of("C:\\spring-core-5.1.9.RELEASE-sources.jar") );

        // Applied to Large Codebases

        //but imagine you have a large codebase with 1000s of classes in the C:\\temp\\mycodebase,
        // here's where jdraft is a better option:
        _path _p = _path.of("C:\\temp");
        _code._cache _cc = _code._cache.of(_p);

        // set all constructor parameters to be final (for ALL constructors)
        $constructor.of().forEachIn(_cc, c-> c.forParameters(p-> p.setFinal()));

        //remove all Deprecated annotations
        $anno.of(Deprecated.class).removeIn(_cc);

        //remove all System.out.println() methods
        $stmt.of("System.out.println($any$);").removeIn(_cc);

    }

    public void testStmtComment(){

        class C{
            public int m(){
                System.out.println(1);
                System.out.println("Done");
                return 37;
            }
        }
        //assertEquals(37,
        //        _runtime.of( (_class)$stmt.of( "System.out.println($any$);").commentOut(C.class) ).eval("new C().m()"));
        //_class _c = $.assertStmt().commentOut(_class.of(C.class));
        //System.out.println( _c );
    }
}
