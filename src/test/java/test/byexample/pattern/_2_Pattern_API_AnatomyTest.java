package test.byexample.pattern;

import junit.framework.TestCase;
import org.jdraft.Ex;
import org.jdraft._method;
import org.jdraft.pattern.*;
import org.jdraft.pattern.$node.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Talk about what parts
 */
public class _2_Pattern_API_AnatomyTest extends TestCase {

    // $proto.$java get_javaType
    // $proto.$ast  public <A extends Node> get_AstType

    public void test$andConstraint(){
        //this $m represents/matches ALL methods
        $method $m = $.method();

        //the basic function of the $method proto it to match against _method implementations
        _method _m = _method.of("public int m();");
        assertTrue( $m.matches(_m));

        // by convention prototypes have a field called "constraint"
        // it's a predicate for matching against the
        // meta-representation/AST representation/String representation...
        // by default the constraint will match ANYTHING (even null)
        assertTrue( $m.constraint.test(null));

        //we can modify the constraint by calling the $and(...) method
        $m.$and(m -> m.isImplemented());

        //now testing the $m against our example method will not match
        //(because the example _m is not implemented)
        assertFalse($m.matches(_m));

        //here is an example of a matching method (as a String representation)
        assertTrue( $m.matches("public void m(){}"));
    }

    /**
     * one or more Stencil "patterns" can also be used for defining
     * the
     */
    public void testParameterizedProto(){
        //this $method models a specific get method (with a specific form, type and name)
        // it is NOT-PARAMETERIZED (no $param$s) and can be used for matching
        // it is very strict in that the variable name must be "x" and type must be int
        $method $getX = $method.of("public int getX(){ return this.x; }");

        //If we want to be MORE GENERAL, i.e. if we want to accept/match the set of ALL
        //"getter methods", we can

        //we can create a "parameterized" prototype to help
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

    public void testExampleSerializableClassWithSerialVersionUID(){
        //this will represent all serialVersionUid fields within code
        $field $serialVersionUid = $field.of("static long serialVersionUID;").$and(f-> f.hasInit());

        //classes that implement Serializable AND have a serialVersionUnid
        $class $hasSVUID = $class.of().$implement(Serializable.class).$and( c-> $serialVersionUid.count(c) >= 1);

        //this represents a class that IMPLEMENTS serializable and DOES NOT have a SerialVersionUid field
        $class $missingSVUID = $class.of().$implement(Serializable.class).$and( c-> $serialVersionUid.count(c) ==0);

        class hasSVUID implements Serializable{
            public static final long serialVersionUID = 12345L;
        }
        class noSVUID implements Serializable{

        }
        assertEquals(1, $serialVersionUid.count(hasSVUID.class));
        assertEquals(0, $serialVersionUid.count(noSVUID.class));

        assertTrue( $hasSVUID.matches(hasSVUID.class));

        assertFalse( $hasSVUID.matches(noSVUID.class));

        assertTrue( $missingSVUID.matches(noSVUID.class) );
        assertFalse( $missingSVUID.matches(hasSVUID.class) );

    }

    public void testWhenToUsePatternAndWhenToUseConstraint(){
        //All if Stmts that call the thenDo(); method if the condition is true
        $stmt $ifBracesPattern = $.ifStmt("if($cond$){ thenDo(); }");

        $stmt $ifConstraint = $.ifStmt().$and(i ->
                $stmt.of("thenDo();").matches( i.ast().getThenStmt())
                        || $stmt.blockStmt("{ thenDo(); }").matches(i.ast().getThenStmt()) );
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
        sel.get(0).is("cond", Ex.of("a==0"));
        sel.get(1).is("cond", Ex.of("a>1"));
    }
}
