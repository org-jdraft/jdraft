package test.byexample.proto;

import junit.framework.TestCase;
import org.jdraft.Expr;
import org.jdraft._method;
import org.jdraft.proto.*;
import org.jdraft.proto.$node.Select;

import java.util.List;
import java.util.function.Predicate;

/**
 * Talk about what parts
 */
public class _2_Proto_API_AnatomyTest extends TestCase {

    // $proto.$java get_javaType
    // $proto.$ast  public <A extends Node> get_AstType

    public void testConstraint(){
        //this $m represents/matches ALL methods
        $method $m = $.method();

        //the basic function of the $method proto it to match against _method implementations
        _method _m = _method.of("public int m();");
        assertTrue( $m.matches(_m));

        // by convention prototypes should all have a field called "constraint"
        // it's a predicate for matching against...
        // by default the constraint will match ANYTHING (even null)
        assertTrue( $m.constraint.test(null));


        //we can modify the constraint by calling the $and(...) method
        $m.$and(m -> m.isImplemented());

        //now testing the $m against our example method will not match
        //(because the example _m is not implemented)
        assertFalse($m.matches(_m));
    }

    /**
     * one or more Stencil "patterns" can also be used for defining
     * the
     */
    public void testStencil(){
        //we can use a Stencil to match against the text/code of each com
        $method $getMethod = $method.of("public $type$ get$Name$(){ return this.$name$; }");


        /** Heres some example */
        class Get{
            int count;
            String name;
            boolean finished;

            public int getCount(){
                return this.count;
            }

            public String getName(){
                return this.name;
            }

            public boolean isFinished(){
                return finished;
            }
        }

        assertEquals( 2, $getMethod.count(Get.class));
    }

    public void testWhenToUsePatternAndWhenToUseConstraint(){
        //All if Stmts that call the thenDo(); method if the condition is true
        $stmt $ifBracesPattern = $$.ifStmt("if($cond$){ thenDo(); }");

        $stmt $ifConstraint = $$.ifStmt().$and(i ->
                $stmt.of("thenDo();").matches( i.getThenStmt())
                        || $stmt.blockStmt("{ thenDo(); }").matches(i.getThenStmt()) );
        //$stmt $ifConstraint = $.ifStmt().$hasDescendant( 2, $stmt.of("thenDo();") );

        class exampleClass{
            void thenDo(){}

            public void ifThenBraces(int a){
                if( a == 1){
                    thenDo();
                }
            }
            public void ifThenNoBraces(int a){
                if(a>1)
                    thenDo();
            }
        }
        //the problem with the pattern is ... it is related to the syntax (assumes we use braces)
        //rather than the semantics (what we want is syntactically not ...
        assertEquals(1, $ifBracesPattern.count(exampleClass.class) );

        assertEquals(2, $ifConstraint.count(exampleClass.class) );

        //if we wanted to select BOTH the braces version AND the no-braces version
        List<Select> sel = $.of( $stmt.of("if($cond$){ thenDo(); }"), $stmt.of("if($cond$) thenDo();") ).listSelectedIn(exampleClass.class);

        assertEquals( 2, sel.size());
        sel.get(0).is("cond", Expr.of("a==0"));
        sel.get(1).is("cond", Expr.of("a>1"));
    }
}
