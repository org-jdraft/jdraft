package test.byexample.proto;

import junit.framework.TestCase;
import org.jdraft.proto.$;
import org.jdraft.proto.$expr;
import org.jdraft.proto.$node;

/**
 * tags:
 *
 * $proto.$
 * $proto($expr) IntLiteral
 * $proto($node) composition
 * $proto($expr) query
 * $proto($expr) composition
 * $proto($expr) listIn()
 * $proto($expr) $hasParent()
 */
public class ProtoQueryListTest extends TestCase {

    @interface LitAnno{
        int value();
        String name() default "provided";
    }

    public void testProtoQuery(){
        class MostlyIntLiterals {
            int negative = -1;

            @LitAnno(0)
            int odd = 1;
            int even = 2;
            int addition = 3 + 4;
            int[] arr = {5,6,7,8,9,10};
            String name = "StringLiteral";

            public void inMatch( int a){
                a+=11;
                //assertTrue( -a );
            }
        }

        //this will print each node (from the CompilationUnit to each expression)
        System.out.println( $.of().listIn(MostlyIntLiterals.class));

        // $.expr() represents ALL expressions (Note: some expressions are composed of other expressions)
        //[-1, 1, @LitAnno(0), 0, 1, 2, 3 + 4, 3, 4, { 5, 6, 7, 8, 9, 10 }, 5, 6, 7, 8, 9, 10, "StringLiteral", a += 11, a, 11]
        System.out.println( $.expr().listIn(MostlyIntLiterals.class) );

        /**
         * $.literal() represents all {@link com.github.javaparser.ast.expr.LiteralExpr}
         * NOTE the first 1 (should it be -1**)
         * [1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, "StringLiteral", 11]
         */
        System.out.println( $.literal().listIn(MostlyIntLiterals.class) );

        //NOTE the first 1 (should it be -1?)
        //[1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        System.out.println( $.intLiteral().listIn(MostlyIntLiterals.class));

        // ** above the -1 is actually a unaryExpr with two child nodes
        //              UnaryExpr (-1)
        //               /           \
        //   (an Operator "-")  (an IntLiteralExpr "1")

        //lets build a $expr<UnaryExpr> that represents + or - and some int (i.e. "+5", "-3")
        $expr $plusMinusInt = $.unary($.PLUS,$.MINUS) // matches UnaryExpr with either operator
                .and( u-> u.getExpression().isIntegerLiteralExpr()); //verifies the UnaryExpr expression is an int literal

        //query the class and verify we can find the UnaryExpr -1
        //[-1]
        System.out.println( $plusMinusInt.listIn(MostlyIntLiterals.class));

        //match all int literals THAT ARE NOT children of of a +/- unaryExpression
        $expr $unsignedIntLiteral = $.intLiteral().$hasParent( p -> !$plusMinusInt.match(p) );

        //now combine the $proto matchers for finding:
        // 1) UnaryExpressions with +/- operators and Int literals
        // 2) IntLiterals that ARE NOT children of (1)
        $node $ints = $.of( $plusMinusInt, $unsignedIntLiteral );

        //here we use the prototype to list the numbers in order
        //[-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        System.out.println( $ints.listIn(MostlyIntLiterals.class));
    }
}
