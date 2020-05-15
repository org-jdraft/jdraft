package test.byexample.pattern.refactor;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import junit.framework.TestCase;
import org.jdraft.Stmts;
import org.jdraft._type;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$stmt;

/**
 * This goes into more detail of using parameterized $pattern(s) for refactoring code
 *
 * What are the main steps to an individual refactoring?
 * 1) identify a particular (target) pattern in the code
 * 2) formulate a (replacement) pattern (often using the identified pattern as input)
 * 3) replace the (target) code pattern with the (replacement) code pattern
 */

public class LogicPositivizerDetailTest extends TestCase {

    static $stmt $sif = $stmt.of("if($left$ != $right$){ then(); }else{ el(); }")
            .$("then();", "then") //replace then(); method call to a parameter "then"
            .$("el();", "else"); //replace el(); method call to a parameter "else"

    public void testMatchVariants(){
        $stmt $protoIfStmt = $stmt.of( ()->{
            if(true != false){
                System.out.println("NOT EQUALS");
            } else{
                System.out.println("EQUALS");
            }
        });

        IfStmt exampleIfStatement = Stmts.ifStmt( ()-> {
            if(true != false){
                System.out.println("NOT EQUALS");
            } else{
                System.out.println("EQUALS");
            }
        });

        assertTrue( $sif.matches( exampleIfStatement) );

        //modify the exampleIfStmt manually to match or not match

        //if I change the operator from != to == or something else WONT MATCH

    }

    public void test$stmt$Use(){

        //we can print out the pattern
        System.out.println( $sif );

        Statement exampleIfStatement =  Stmts.of( ()-> {
            if(true != false){
                System.out.println("NOT EQUALS");
            } else{
                System.out.println("EQUALS");
            }
        });

        //matches
        assertTrue( $sif.matches( exampleIfStatement) );


        //select the pattern from within the statement
        $stmt.Select ss = $sif.select(exampleIfStatement);
        assertNotNull(ss);

        //here we can manually deconstruct the pattern based on arguments
        assertTrue( ss.is("left", true));
        assertTrue( ss.is("right", false));
        assertTrue( ss.is("then", Stmts.of( ()-> System.out.println("NOT EQUALS"))));
        assertTrue( ss.is("else", Stmts.of( ()-> System.out.println("EQUALS"))));
    }


    // a "source" parameterized prototype used for identify matching source IfStmts...
    // we also extract the $parameterized values of ("left", "right", "then" and "else")
    static $stmt $sourceIf = $.stmt("if($left$ != $right$){ then(); }else{ el(); }")
            .$("then();", "then").$("el();", "else");

    // a "target" parameterized prototype used for populating the target IfStmt
    // we change the operator from "!=" to "==" and swapped the "else" and "then" parameters
    static $stmt $targetIf = $.stmt("if($left$ == $right$){ then(); }else{ el(); }")
            .$("then();", "else").$("el();", "then");

    /**
     *
     */
    public void testMoreComplicatedCases(){
        //if a and b are method calls and "then" and "else" are multi-statements
        class TT {
            void m() {
                if (a() != b()) {
                    System.out.println("NOT EQUAL");
                    System.out.println("NOT EQUAL");
                } else {
                    System.out.println("EQUAL");
                    System.out.println("EQUAL");
                }
            }
            int a(){ return 1; }
            int b(){ return 2; }
        }
        //note here we are using
        assertNotNull( $sourceIf.firstIn(TT.class) );
        _type _t = $sourceIf.replaceIn(TT.class, $targetIf);
        System.out.println( _t );

    }
    /**
     * This describes a little more about what you can do with $parameterized prototypes
     */
    public void testDetail(){
        class F{
            int a, b;
            void m(){
                if( a != b ){
                    System.out.println("NOT EQUAL");
                } else{
                    System.out.println("EQUAL");
                }
            }
        }
        assertNotNull( $sourceIf.firstIn(F.class));    // match and return the first matching node
        assertEquals(1, $sourceIf.countIn(F.class) ); //count all matches in a Class
        assertEquals(1, $sourceIf.listIn(F.class).size()); //list all of the matching nodes in some Class


        _type _c = $sourceIf.replaceIn(F.class, $targetIf);
        System.out.println( _c );
    }

    public void test$ProtoMatches(){
        Statement st = Stmts.of(
                (Integer a, Integer b)-> {
                    if(a != b){
                        System.out.println("DO THEN");
                    }
                    else{
                        System.out.println("DO ELSE");
                    }
                });
        assertTrue( $sourceIf.matches( st ) );
    }

    /* we could also do this, but it's more work
        static $stmt<IfStmt> $sif = $.ifStmt(
            i->i.getCondition() instanceof BinaryExpr
                    && i.getCondition().asBinaryExpr().getOperator() == BinaryExpr.Operator.NOT_EQUALS
                    && i.hasElseBlock() );
     */
}
