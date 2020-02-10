package test.quickstart;

import com.github.javaparser.ast.stmt.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jdraft.*;
import org.jdraft.io._path;
import org.jdraft.macro.*;
import org.jdraft.pattern.$method;
import org.jdraft.pattern.$node;
import org.jdraft.runtime._runtime;

public class OverviewTest extends TestCase {

    public void testS(){
        _class _point = _class.of("package graph;","public class Point{}");
        _field _x = _field.of("public double x;");
        _field _y = _field.of("public double y;");
        _method _getX = _method.of("public double getX(){ return this.x; }");
        _method _setX = _method.of("public void setX(double x){ this.x = x;}");
        _method _getY = _method.of("public double getY(){ return this.y; }");
        _method _setY = _method.of("public void setY(double y){ this.y = y;}");

        //models compose
        _point.addFields(_x, _y)
                .methods(_getX, _setX, _getY, _setY);
        System.out.println( _point );
    }

    public void queryForTopMethodComplexity(){
        /** calculate and store rudimentary complexity metric with the _method */
        class _methodComplexity{

            public _method _m;
            public Integer complexity;

            public _methodComplexity(_method _m ){
                this._m = _m;
                this.complexity = calculate(_m);
            }

            /**
             * this represents the complexity computation algorithm that Rascal has
             * https://www.rascal-mpl.org/#_Metrics
             */
            public int calculate( _method _m){
                if( !_m.isImplemented() ){
                    return 1;
                }
                //count all nodes of (any of these types) found within a method (and add 1)
                return $node.of( DoStmt.class, WhileStmt.class, IfStmt.class, ForStmt.class,
                        ForEachStmt.class, SwitchStmt.class, SwitchEntry.class, CatchClause.class).count(_m) +1;
            }
        }

        _path _p = _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java");

        $method.of().streamIn( _p ) // find all methods in the _path (recursively)
                .map(m -> new _methodComplexity(m)) //map to a new _methodComplexity
                .sorted((mc1, mc2) -> mc2.complexity.compareTo(mc1.complexity) ) //sort highest complexity first
                .limit(10) //limit to the top (10)
                .forEach(t -> System.out.println( "COMPLEXITY ["+t.complexity+"] : \n"+ t._m)); //print
    }

    public void testMacroAndRuntimeEval(){
        //the @_dto @macro creates
        @_package("graph") @_dto class Point{
            @_final double x,y;
        }
        //_class _point = _class.of("graph.Point", new @_dto Object(){ @_final double x, y; });
        _class _point = _class.of(Point.class);
        //add a distance method
        _point.method(new Object(){
           public double distanceTo( double x, double y ){
               return Math.sqrt((this.y - y) * (this.y - y) + (this.x - x) * (this.x - x));
           }
           @_remove double x, y;
        });
        /* compile & load the model (_point) as class graph.Point.class */
        _runtime _r = _runtime.of(_point);

        //verify the simple 3-4-5 triangle
        assertEquals(5.0d,  _r.eval("new Point(0.0d, 0.0d).distanceTo(3.0d, 4.0d)") );

        //verify equals works
        assertEquals( _r.eval("new Point(5.0, 12.0)"),
                      _r.eval("new Point(5.0, 12.0)") );

        //verify hashcode works
        assertEquals( _r.eval("new Point(3.2, 8.32).hashCode()"),
                      _r.eval("new Point(3.2, 8.32).hashCode()"));
    }

    $method $testLaunch = $method.of( new Object(){
        public @_static Test suite(){
            TestSuite suite = new TestSuite($testClasses$);
            return suite;
        }
        @_remove Class $testClasses$;
    });
    public void testAutoTestSuite(){
        _class _autoTestSuite = _class.of("gen.jdraft.AutoTestSuite", new @org.jdraft.macro._imports({Test.class, TestCase.class}) TestSuite(){

        });
    }
}
