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
        _method _getX = _method.of("public double getX(){ return x; }");
        _method _setX = _method.of("public void setX(double x){ this.x = x;}");
        //models fit together
        _point.field(_x).methods(_getX, _setX);
    }

    public void testQueryForTopMethodComplexity(){
        /** calculate and store A rudimentary complexity metric with the _method */
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
        //use the @_dto and
        _class _point = _class.of("graph.Point", new @_dto Object(){ @_final double x, y; });

        /* compile & load the model (_point) as class graph.Point.class */
        _runtime _r = _runtime.of(_point);

        System.out.println( _r.eval("new Point(10.0, 20.0)") );

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
        _class _autoTestSuite = _class.of("gen.jdraft.AutoTestSuite", new @_imports({Test.class, TestCase.class}) TestSuite(){

        });
    }
}
