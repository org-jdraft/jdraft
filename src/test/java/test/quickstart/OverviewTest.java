package test.quickstart;

import com.github.javaparser.ast.stmt.*;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro._dto;
import org.jdraft.macro._final;
import org.jdraft.pattern.$method;
import org.jdraft.runtime._runtime;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OverviewTest extends TestCase {

    public void testS(){
        _class _point = _class.of("package graph;","public class Point{}");
        _field _x = _field.of("public double x;");
        _method _getX = _method.of("public double getX(){ return x; }");
        _method _setX = _method.of("public void setX(double x){ this.x = x;}");
        //models fit together
        _point.field(_x).methods(_getX, _setX);
    }

    /**
     * These Statements effect flow control and can be used as "poor mans" metric for the
     * "complexity" of a method (by naively counting the number of these Statements within
     * a block of code)
     */
    static Set<Class> FLOW_CTRL_NODES = Stream.of( DoStmt.class, WhileStmt.class, IfStmt.class, ForStmt.class,
            ForEachStmt.class, SwitchStmt.class, SwitchEntry.class, CatchClause.class).collect(Collectors.toSet());

    public static int calcMethodComplexity(_method _m){
         if( !_m.isImplemented() ){
             return 0;
         }
         AtomicInteger com = new AtomicInteger();
         _walk.in( _m.getBody(), n-> FLOW_CTRL_NODES.contains(n.getClass()), n-> com.incrementAndGet());
         return com.get();
    }

    public void testQueryForTopMethodComplexity(){
        class C{
            public void m(){}
            public void one(){
                if( 1==1 ){}
            }
        }

        Map<_method, Integer> _methodToComplexityMap = $method.of().streamIn(C.class).collect(
                Collectors.toMap( Function.identity(), OverviewTest::calcMethodComplexity) );

        System.out.println( _methodToComplexityMap);
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
}
