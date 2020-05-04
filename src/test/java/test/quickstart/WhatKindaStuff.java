package test.quickstart;

import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import junit.framework.TestCase;
import org.jdraft.Statements;
import org.jdraft._class;
import org.jdraft._project;
import org.jdraft.io._path;
import org.jdraft.macro._dto;
import org.jdraft.macro._packageName;
import org.jdraft.pattern.*;
import org.jdraft.runtime._runtime;

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

    public void  stuffCT(){
        // you could do this manually
        // open up an IDE and manually make these changes

        // Simple Programs
        String aSrc = _class.of(A.class).forConstructors(m ->m.forParameters(p-> p.setFinal())).toString();
        System.out.println(aSrc);

        @_dto @_packageName("graph") class Point{
            double x, y;
        }

        //build classes with @macros
        _class _c = _class.of(Point.class);
        //_class _c = _class.of("graph.Point", new @_dto Object(){ double x,y;});

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

        _class.of(A.class).forMethods( m -> $.of( NodeWithBlockStmt.class ).countIn(m) );

        //print all TODO comments found in source jar file
        //$.comment("TODO").printIn( _archive.of("C:\\spring-core-5.1.9.RELEASE-sources.jar") );

        // Applied to Large Codebases

        //but imagine you have a large codebase with 1000s of classes in the C:\\temp\\mycodebase,
        // here's where jdraft is a better option:
        _path _p = _path.of("C:\\temp");
        _project _cc = _p.load();

        // set all constructor parameters to be final (for ALL constructors)
        $constructor.of().forEachIn(_cc, c-> c.forParameters(p-> p.setFinal()));

        //remove all Deprecated annotations
        $annoRef.of(Deprecated.class).removeIn(_cc);

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

        _class _c = _class.of(C.class);

        //a pattern representing System.out.println()
        $stmt $println = $stmt.of("System.out.println($any$);");
        //verify we can find (2) printlns
        assertEquals(2, $println.countIn(_c));

        //comment out all println statements in the class and return the _class
        _c = (_class)$println.commentOut(C.class);
        assertEquals( 0, $println.countIn(_c));

        //assertEquals( 2, $stmt.emptyStmt().count(_c));
        //assertEquals( 1, _c.getMethod("m").listStatements().size() );
        //assertEquals(37,
        //        _runtime.of( (_class)$stmt.of( "System.out.println($any$);").commentOut(C.class) ).eval("new C().m()"));
        //_class _c = $.assertStmt().commentOut(_class.of(C.class));
        //System.out.println( _c );
    }

    public void testAddAndRemoveSystemOutReturn(){
        class C{
            public int m(){
                return 100;
            }

            public int r(){
                return 200;
            }
        }
        _class _c = _class.of(C.class);
        $stmt $println = $stmt.of("System.out.println($any$);");
        $stmt.of("return $any$;").forSelectedIn(_c,
                s-> Statements.addStatementsBefore( s.ast(), $println.draft( "any", s.get("any")).ast()) );

        _runtime _r  = _runtime.of(_c);
        assertEquals( 100, _r.eval("new C().m()"));
        assertEquals( 200, _r.eval("new C().r()"));

        //Stmt.addStatementBefore(Statement targetStatement, Statement....sts )
        //Stmt.addStatementsAfter(Statement targetStatement, Statement...sts)
        //$stmt.returnStmt().forSelectedIn(C.class, s-> s.astStatement.replace)
    }
}
