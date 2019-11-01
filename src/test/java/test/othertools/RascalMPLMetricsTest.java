package test.othertools;

import com.github.javaparser.ast.stmt.*;
import junit.framework.TestCase;
import org.jdraft._method;
import org.jdraft._walk;
import org.jdraft.io._path;
import org.jdraft.pattern.$method;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Emulates the
 */
public class RascalMPLMetricsTest extends TestCase {

    public void testFindTop10ComplexMethods(){
        //this is the top number of methods to collect
        int topCount = 10;
        _path _p = _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java");

        // find and calculate the method complexity for all methods
        // sort them (highest first)
        // take the top (10)
        // print them the complexity and the method
        $method.of().streamIn( _p )
                .map(m -> new _methodComplexity(m, calculateComplexityOfMethod(m)))
                .sorted((mc1, mc2) -> mc2.complexity.compareTo(mc1.complexity) )
                .limit(topCount)
                .forEach(t -> System.out.println( "COMPLEXITY ["+t.complexity+"] : \n"+ t._m)); //print
    }

    static class _methodComplexity{
        public _method _m;
        public Integer complexity;

        public _methodComplexity (_method _m, Integer complexity){
            this._m = _m;
            this.complexity = complexity;
        }
    }

    /** this represents the complexity computation algorithm that Rascal has
     * https://www.rascal-mpl.org/#_Metrics
     */
    public static int calculateComplexityOfMethod( _method _m){
        if( !_m.isImplemented() ){
            return 1;
        }
        AtomicInteger com = new AtomicInteger(1);
        _walk.in(_m.getBody(), n -> {
            if( FLOW_CONTROL_CLASSES.contains( n.getClass() )) {
                com.incrementAndGet();
            }
        });
        return com.get();
    }

    static Set<Class> FLOW_CONTROL_CLASSES = Stream.of( DoStmt.class, WhileStmt.class, IfStmt.class, ForStmt.class,
            ForEachStmt.class, SwitchStmt.class, SwitchEntry.class, CatchClause.class).collect(Collectors.toSet());

    public void testCalcMethodComplexity(){
        assertEquals(1, calculateComplexityOfMethod( _method.of("void m();")) );
        assertEquals(1, calculateComplexityOfMethod(_method.of("void m(){}")));
        assertEquals(2, calculateComplexityOfMethod(_method.of(new Object() {
            public void m() {
                if (1 == 1) { }
            }
        })));
        //here test every flow control type
        assertEquals(12, calculateComplexityOfMethod(_method.of(new Object() {
            public void m(List<String> l) {
                int x=1;
                do{ x++; } while(x<100); //1
                while(x > 1){ x--; } //2
                if (1 == 1) { } //3
                if( 1==1){ } else{} //4
                for( String li: l){} //5 for each
                for( int i=0; i<x;i++){} //6
                switch( x){ //7
                    case 1: //8
                    case 2: //9
                    default :  //10
                }
                try{ }catch(Exception e){ } //11
            }
        })));
    }
}
