package test.byexample;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import junit.framework.TestCase;
import org.jdraft.Stmt;
import org.jdraft.Tokens;
import org.jdraft.proto.$;
import org.jdraft.proto.$proto;
import org.jdraft.proto.$stmt;

import java.util.List;

/**
 * $proto
 *
 * MatchAny                   Parameterized                             Constant
 * -------------------------------------------------------------------------------------------------
 * $.assertStmt();            $.assertStmt("assert $cond$;");           $.assertStmt("assert true;")
 * $.of();                    $.of("assert $cond$;");                   $.of("assert true;)
 * $.expr();                  $.expr("$a$ + $b$");                      $.expr("a + 1");
 *
 */
public class ProtoByExampleTest extends TestCase {

    /**
     *
     */
    public void testProtoConstant(){

        //Statements are the backbone of code, here we build a statement from a String
        Statement st = Stmt.of("System.out.println(1);");

        // a $stmt is a prototype representing the criteria for some set of statement(s).
        // this $stmt $print represents a specific/ constant statement
        $stmt $printOne = $stmt.of("System.out.println(1);");

        //the are (2) main purposes for prototypes:

        //1) drafting (building a new instance represented by the prototype)
        Statement printOne = $printOne.draft(); //System.out.println(1);

        //2) matching (checking if a given Statement instances is compatible with the proto)
        assertTrue( $printOne.matches(printOne) ); //match
        assertTrue( $printOne.matches(st) ); //match

        // Each $stmt stores (3) parts:
        // (A) a Stencil for matching/extracting tokens from the Text of other Statements
        // (B) a lambda Predicate (for matching against other Statement implementations)
        // (C) a Statement Class for which type of Statement (AssertStmt, IfStmt, DoStmt...)

        // A) the Stencil is a regex-like pattern (for matching against the text of other Statements)
        // we can print the stencil:
        // "System.out.println(1);"
        System.out.println( $printOne.stmtStencil);
        assertTrue($printOne.stmtStencil.matches("System.out.println(1);"));

        // the $stmt also has a "constraint" (a lambda Predicate that tests Statements )
        // by default the constraint returns true for anything (ANY statement OR NULL)
        assertTrue( $printOne.constraint.test( null ) );

        //it is important to note that $stmt IS MUTABLE
    }

    /**
     * At the other end of the spectrum from matching a specific constant Statement
     * is a "MatchAny" prototype.  The only match criteria for a $stmt matchAny prototype
     * is that we pass in ANY valid statement
     */
    public void testProtoMatchAny(){
        //$anyStmt represents/ matches against ANY statement
        $stmt $anyStmt = $stmt.of();

        //Internals
        assertTrue( $anyStmt.isMatchAny());
        System.out.println( $anyStmt ); //(Statement) : "$any$"
        assertTrue( $anyStmt.stmtStencil.isMatchAny() ); //stencil matches any text
        assertTrue($anyStmt.statementClass == Statement.class); //any Statement will do


        //we can verify it matches against some statements directly
        assertTrue( $anyStmt.matches("System.out.println(1);"));
        assertTrue( $anyStmt.matches("assert true;"));

        //we normally would not do this, but we can use the individual properties:
        assertTrue($anyStmt.stmtStencil.matches("invalid statement"));
        assertTrue($anyStmt.constraint.test(null));
        assertEquals(Statement.class, $anyStmt.statementClass);

        //in addition to simple matching operations, proto defines walk/match
        //based operations for matching against existing code:

        class With4Statements{

            void threeIndividualStatementsAndABlockStmt(){
                assert true;
                int i = 3;
                System.out.println( i );
            }
        }

        //here call count & match the (3) individual statements
        // PLUS the BlockStmt holding the (3) individual statements makes (4) statements
        assertEquals(4, $anyStmt.count(With4Statements.class));

        //we can also collect and list the statements within some code:
        List<Statement> sts = $anyStmt.listIn(With4Statements.class);
        assertTrue( sts.get(0) instanceof BlockStmt);

        //If we want to match/extract/count
        $stmt<BlockStmt> $anyBlockStmt = $stmt.blockStmt();
        assertEquals( 1, $anyBlockStmt.count(With4Statements.class));
    }


    //"constant" prototypes are of limited usefulness, since they are looking for "exact matches"
    // but "parameterized" prototypes are where the real fun comes in.
    public void test$Parameterized$Prototypes() {

        //a prototype to represent all calls to "System.out.println(???);"
        // the "$a$" represents a parameter named "a", it will match any pattern
        $stmt<?> $anyPrint = $stmt.of("System.out.print($a$);");

        //we can build new statements using $anyPrint and passing in
        // param/values that are required by the prototype (below the param "a" is required)
        // here we can draft new Statements by keyValuePairs
        Statement print1 =          $anyPrint.draft("a", 1);   //System.out.print(1);
        Statement printc =          $anyPrint.draft("a", 'c'); //System.out.print('c');
        Statement printMethodCall = $anyPrint.draft("a", "aMethodCall()"); //System.out.print(aMethodCall());
        Statement printString =     $anyPrint.draft("a", "\"String\""); //System.out.print("String");
        Statement printComposite =  $anyPrint.draft("a", "\"String\" + a + \" \" + b"); //System.out.print("String" + a + " " + b );

        //now that we've drafted $a$ will match anything
        assertTrue($anyPrint.matches(print1));
        assertTrue($anyPrint.matches(printc));
        assertTrue($anyPrint.matches(printMethodCall));
        assertTrue($anyPrint.matches(printString));
        assertTrue($anyPrint.matches(printComposite));

        assertTrue($anyPrint.matches("System.out.print(1);"));
        assertTrue($anyPrint.matches("System.out.print('c');"));
        assertTrue($anyPrint.matches("System.out.print(aMethodCall());"));
        assertTrue($anyPrint.matches("System.out.print(\"String\");"));
        assertTrue($anyPrint.matches("System.out.print(\"String\" + a + \" \" + b );"));
    }


    //sometimes the $parameters$ we match is important information we might need
    //to return the
    public void testSelect_List_First(){
        //
        $stmt<Statement> $anyPrint = $stmt.of("System.out.print($a$);");

        //select is like match, but it returns a $stmt.Select
        // containing
        // 1) the Ast (Statement)
        // 2) Tokenized parameters extracted from the
        $stmt.Select sel = $anyPrint.select("System.out.print(1);");


        assertNotNull(sel); //select returns null if it doesnt match

        Statement st = sel.ast(); //gets the Statement
        $proto.$tokens $ts = sel.tokens(); //gets the tokens {{"a", "1"}}

        //we can inspect each $token manually (here verify "a" is "1")
        assertEquals("1", $ts.get("a"));

        //we can test all of the tokens at once (verify there is (1) pair {{"a", "1"}}
        assertTrue($ts.is( Tokens.of("a","1")));

        //the Select is
        assertTrue(sel.is("a", 1)); //"System.out.println(1);"

        // this is just an example class with (4) println Statements
        class WithPrint{
            int a,b;
            void n(){
                System.out.print(1);
                System.out.print('c');
                System.out.print("String");
                System.out.print(a + b);
            }
        }
        //here we can
        List<$stmt.Select<Statement>> sels = $anyPrint.listSelectedIn(WithPrint.class);
        assertEquals( 4, sels.size());
        assertTrue( sels.get(0).is("a", 1));
        assertTrue( sels.get(1).is("a", 'c'));
        assertTrue( sels.get(2).is("a", "String"));
        assertTrue( sels.get(3).is("a", "a + b"));

        //we can select the first only from coded
        sel = $anyPrint.selectFirstIn(WithPrint.class);
        assertTrue( sels.get(0).is("a", 1));
    }

    //about matching comments
}
