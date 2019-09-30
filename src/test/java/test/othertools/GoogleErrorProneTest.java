package test.othertools;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.stmt.ReturnStmt;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._class;
import org.jdraft.pattern.*;

/**
 * this example came from Google's Error-Prone project:
 * https://github.com/google/error-prone/wiki/Writing-a-check
 *
 */
public class GoogleErrorProneTest extends TestCase {

    public void testMember(){
        //a $member is just a BodyDeclaration
        //
        Range searchRange = new Range(new Position(0, 0), new Position(100, 100));

        //$isInRange( Range )
        //$isInRange(startLine, startColumn, endLine, endColumn)
        //$isInRange(startLine, endLine)


        BodyDeclaration bd = Ast.method("int i(){return 3;}");
        assertTrue( searchRange.strictlyContains( bd.getRange().get() ) );
        assertTrue(
                $node.of(BodyDeclaration.class).match(Ast.method("int i(){return 1;}")) );
    }
    //https://errorprone.info/bugpattern/AndroidInjectionBeforeSuper
    public void testAndroidInjectBeforeSuper(){
        //here we have the components
        $ex $onCreate = $.expr("super.onCreate($any$)");
        $ex $onAttach = $.expr("super.onAttach($any$)");
        $ex $inject = $ex.of("AndroidInjection.inject(this)");
        $anno $suppress = $anno.of("@SuppressWarnings(\"AndroidInjectionBeforeSuper\")");

        /*

        $inject.$notMember($member.of($suppress))

        $inject.$before(3, $pattern...)
        $inject.$after(3, $pattern...)

        $inject.$notAfter(3, $onCreate, $onAttach);
        $inject.$notBefore(3, $onCreate, $onAttach);
        */
        _class _c = _class.of("public class WrongOrder extends Activity {",
            "    @Override",
            "    public void onCreate(Bundle savedInstanceState) {",
            "        super.onCreate(savedInstanceState);",
            "        // BUG: Diagnostic contains: AndroidInjectionBeforeSuper",
            "        AndroidInjection.inject(this);",
            "    }",
            "}");
            assertEquals(1, $onCreate.count(_c));
            assertEquals(1, $inject.count(_c));
    }

    /**
     * Look through code to find return null;
     */
    public void testFindReturnNull(){

        //this models all return statements that return the null literal
        $stmt<ReturnStmt> $returnNull =
                $.returnStmt("return null;");

        class retNull{
            String v(){
                return null;
            }
        }
        //verify we can find such statements in the above
        assertEquals(1, $returnNull.count(retNull.class));
    }


    //we just need this for test purposes
    @interface Nullable{}

    /**
     * <A HREF="https://github.com/google/error-prone/wiki/Writing-a-check#create-a-bugchecker">Error Prone Bug Checker</A>
     * "The use of return null; is banned, except in methods annotated with @Nullable."
     */
    public void testFindReturnsNullsIfNotAnnotatedWithNullable(){

        //match any callable declaration that is annotated with Nullable
        $node $memberAnnotatedWithNullable = $node.of(CallableDeclaration.class)
                .$and(cd-> ((CallableDeclaration)cd).getAnnotationByName("Nullable").isPresent());

        //match any Return null where that is NOT within a Member of
        $stmt<ReturnStmt> $returnNull = $.returnStmt("return null;")
                .$and(r -> !$.hasAncestor(r, $memberAnnotatedWithNullable));


        class FFF{
            @Nullable Object m(){ //this shouldnt match
                return null;
            }

            String name(){ //this should match
                /** comment */
                return null;
            }
        }
        assertEquals(1, $returnNull.count(FFF.class));
        //System.out.println( "FOUND " + $returnNull.firstIn(FFF.class) );

        assertTrue( $returnNull.firstIn(FFF.class)
                .stream(Node.TreeTraversal.PARENTS).anyMatch( p-> $method.of().$name("name").match(p) ) );

    }

}
