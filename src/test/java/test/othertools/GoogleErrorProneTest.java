package test.othertools;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.stmt.ReturnStmt;
import junit.framework.TestCase;
import org.jdraft.proto.*;

/**
 * this example came from Google's Error-Prone project:
 * https://github.com/google/error-prone/wiki/Writing-a-check
 *
 */
public class GoogleErrorProneTest extends TestCase {

    /**
     * Look through code to find return null;
     */
    public void testFindReturnNull(){

        //this models all return statements that return the null literal
        $stmt<ReturnStmt> $returnNull =
                $$.returnStmt().$and(r -> r.getExpression().isPresent()
                        && r.getExpression().get().isNullLiteralExpr() );

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

        //match any Return null where that is NOT within a Member
        $stmt<ReturnStmt> $returnNull = $$.returnStmt().$and(r -> r.getExpression().isPresent()
                && r.getExpression().get().isNullLiteralExpr() )
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
        System.out.println( "FOUND " + $returnNull.firstIn(FFF.class) );

        assertTrue( $returnNull.firstIn(FFF.class)
                .stream(Node.TreeTraversal.PARENTS).anyMatch( p-> $method.of().$name("name").match(p) ) );

    }

}
