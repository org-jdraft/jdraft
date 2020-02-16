package test.quickstart;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.io._archive;
import org.jdraft.io._sources;
import org.jdraft.io._io;
import org.jdraft.io._path;
import org.jdraft.macro.*;
import org.jdraft.pattern.*;
import org.jdraft.runtime._runtime;

import java.nio.file.Path;
import java.util.List;

public class WhatKindaStuff2 extends TestCase {




    public void simpleGenerateJavaCode(){

        _class _c = _class.of("graph.Point", new @_dto Object(){
            @_private @_final double x,y;

            public double distanceTo(double x2, double y2){
                return Math.sqrt( (x - x2) * (x - x2) + (y - y2) * (y - y2) );
            }
        });

        //add overloaded method to do distance between another Point
        _c.addMethod("public double distanceTo(Point point){",
                "    return distanceTo(point.x, point.y);",
                "}");

        System.out.println( _c );

        //TESTING
        //_c.addOrReplace(_method.of("public String toString(){ return \"(\"+x))
        _runtime _r = _runtime.of(_c);
        assertEquals( 5.0d, _r.eval("new Point(0.0, 0.0).distanceTo(new Point(3.0, 4.0))"));
        assertTrue( (Boolean)_r.eval("new Point(23.1,1.23).equals(new Point(23.1,1.23))"));

    }

    public void testFindAllMainMethods(){

        $method.of("public static void main(String[] $name$){}"); //.count(_cache.of("C:\\temp\\MySrc.jar"));

        $method $MAIN = $method.of("public static void main(String[] $name$){}");
        class C{
            public @_static void main(String[] args){
                System.out.println("main");
            }
        }
        assertTrue($MAIN.isIn(C.class));

        //$method $MAIN_METHOD = $method.of("main", $.PUBLIC, $.STATIC, $.VOID, $parameters.of("String[] $name$"));
    }

    //build compile and run
    public void testBuildAndCallStaticHelloWorld(){
        //explain
        _runtime.of(_class.of("A").main(()->System.out.println("Hello World!"))).main();
    }


    public void lotsOfCode(){
        _archive _guava = _archive.of("C:\\guava-src.jar");
        _sources _source = _sources.of(_path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java"));
        _sources _tests = _sources.of(_path.of("C:\\jdraft\\project\\jdraft\\src\\test\\java"));

        //query
        //print all TODO tags in the source
        $comment.of("TODO").printIn(_source);
        $method.of("public static void main(String[] $name$){}").printIn(_tests);
        $field.of("public static long serialVersionUID").printIn(_source);

        //change & write out code
        _io.out("C:\\temp\\nocomment", $stmt.of( ($any$)->System.out.println($any$) ).commentOut(_tests));

        //_code._cache _noCommentTests = $stmt.of( ($any$)->System.out.println($any$) ).commentOut(_tests);


    }

    public void printAllTODOTagsInWholeJar(){
        _archive _a = _archive.of("C:\\guava-src.jar");


        $comment.of("TODO").printIn( _a );

        //print the number of main methods in the archive
        System.out.println( $method.of("public static void main(String[] $name$){}").countIn(_a));

    }

    public void allJavaFilesInSourceDirectory(){
        _path _p = _path.of("C:\\MyProject\\src\\test");

        List<Path> paths =
                _io.out("C:\\noprint", $stmt.of("System.out.println($any$);").commentOut(_p));

        /*
        _code._cache cc =
                $.stmt("System.out.println($any$);").commentOut(_path.of("C:\\myProject\\src\\test"));

        _io.out("C:\\Refactored", cc);
         */
    }

    public void testBuildAndEvalInstance(){

    }
    //build and use an @macro Class at runtime
    public void testDataTuple(){
        @_dto class Point{
            @_final double x,y;
        }
        System.out.println( _runtime.instanceOf(Point.class, 10.0d, 20.0d).toString() );
        //System.out.println( new Point() );
        //apply the @_dto macro and @_final macros to the Point class
        //this creates a new _draft _class based on the source of Point.class
        //
        //_runtime.of(Point.class).instance("Point", 10.0, 20.0);
    }

    public void addDebugLogicToExistingClasses(){

    }

    public void testCommentOutAllSystemOutsInTests(){

    }

    public interface Coord{
        double getX();
        double getY();
    }

    public void implementAnInterfaceAtRuntime(){
        Coord c = _runtime.impl( new @_dto Coord(){

            public double x, y;

            public double getX() {
                return x;
            }

            @Override
            public double getY() {
                return y;
            }
        });


    }


    public void testDebugTestHelper(){
        //after every assignment expression add a Log statement
        int i=0;
        class C{

        }
        //$stmt $print = $stmt.of("System.out.println( \"$name$ :\"+$name$);");

        $stmt $assign = $stmt.of( $.assign() );
        assertTrue( $assign.matches("i=0;"));
        assertFalse( $assign.matches("int i=0;"));
        $stmt $assignOrDeclare = $stmt.hasAny( $.assign(), $ex.varLocalEx() );
        assertTrue( $assignOrDeclare.matches("i=0;"));
        assertTrue( $assignOrDeclare.matches("int i=0;"));

        //$stmt.expressionStmt( $.assign("$name$ = $value$") );
        //$.assign().forEachIn(C.class, a-> {
        //    a.getTarget()
        //    Stmt.addStatementsAfter(a, );
        //})
    }
}
