package test.othertools;

import com.github.javaparser.ast.stmt.*;
import junit.framework.TestCase;
import org.jdraft._method;
import org.jdraft.io._path;
import org.jdraft.pattern.$method;
import org.jdraft.pattern.$node;

import java.util.*;

/**
 * Emulates the
 */
public class RascalMPLMetricsTest extends TestCase {

    /*
    public void testFindTop10ComplexMethods(){
        _path _p = _path.of("C:\\jdraft\\project\\jdraft\\src\\main\\java");

        $method.of().streamIn( _p ) // find all methods in the _path (recursively)
                .map(m -> new _methodComplexity(m)) //map to a new _methodComplexity
                .sorted((mc1, mc2) -> mc2.complexity.compareTo(mc1.complexity) ) //sort highest complexity first
                .limit(10) //limit to the top (10)
                .forEach(t -> System.out.println( "COMPLEXITY ["+t.complexity+"] : \n"+ t._m)); //print
    }
    */

    /** calculate and store A rudimentary complexity metric with the _method */
    static class _methodComplexity{

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
        public static int calculate( _method _m){
            if( !_m.isImplemented() ){
                return 1;
            }
            //count all nodes of (any of these types) type found within a method (and add 1)
            return $node.of( DoStmt.class, WhileStmt.class, IfStmt.class, ForStmt.class,
                    ForEachStmt.class, SwitchStmt.class, SwitchEntry.class, CatchClause.class).count(_m) +1;
        }
    }

    public void testCalcMethodComplexity(){
        assertEquals(1, _methodComplexity.calculate( _method.of("void m();")) );
        assertEquals(1, _methodComplexity.calculate(_method.of("void m(){}")));
        assertEquals(2, _methodComplexity.calculate(_method.of(new Object() {
            public void m() {
                if (1 == 1) { }
            }
        })));
        //here test every flow control type
        assertEquals(12, _methodComplexity.calculate(_method.of(new Object() {
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
