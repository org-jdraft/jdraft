package test.othertools;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$ex;

import java.util.function.Function;

/**
 * IntelliJ has a <A HREF="https://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/psi_elements.html">PSI</A>
 * (Program Structure Interface) Infrastructure with Files/Elements/Trees for representing
 * source code in the IntelliJ ecosystem
 * with the PSI you can do:
 * code inspection
 *
 *

 */
public class IntelliJPSIExample extends TestCase {

    /**
     * https://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/modifying_psi.html#creating-the-new-psi
     *
     * // binaryExpression holds a PSI expression of the form "x == y", which needs to be replaced with "x.equals(y)"
     * PsiBinaryExpression binaryExpression = (PsiBinaryExpression) descriptor.getPsiElement();
     * IElementType opSign = binaryExpression.getOperationTokenType();
     * PsiExpression lExpr = binaryExpression.getLOperand();
     * PsiExpression rExpr = binaryExpression.getROperand();
     *
     * // Step 1: Create a replacement fragment from text, with "a" and "b" as placeholders
     * PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
     * PsiMethodCallExpression equalsCall =
     *     (PsiMethodCallExpression) factory.createExpressionFromText("a.equals(b)", null);
     *
     * // Step 2: replace "a" and "b" with elements from the original file
     * equalsCall.getMethodExpression().getQualifierExpression().replace(lExpr);
     * equalsCall.getArgumentList().getExpressions()[0].replace(rExpr);
     *
     * // Step 3: replace a larger element in the original file with the replacement tree
     * PsiExpression result = (PsiExpression) binaryExpression.replace(equalsCall);
     */
    public void testConvertEqualBinaryExprToEqualsMethodCall(){
        //basically represent the BINARY EXPRESSION "x == y" (for any X and Y)
        $ex $binEqEq = $.ex("$x$ == $y$");
        //then convert to a METHOD CALL EXPRESSION  "x.equals(y)" for any x and y
        $ex $methodEq = $.ex("$x$.equals($y$)");

        class someEx{
            Integer a,b,c,d;

            public Integer a(){return 1;}
            public Integer b(){return 1;}

            public void v(){
                if( a==b ){ }
                if( c==d ){}
                //note: this should work ($x$, and $y$ represent a method call expression as well)
                if( a() == b() ){}
            }
        }

        _class _c = _class.of(someEx.class);
        //verify that there are (3) occurrences of X == Y in someEx
        assertEquals( 3, $binEqEq.countIn(_c));

        //refactor to convert (all (3)) "x == y" instances to "x.equals(y)"
        $binEqEq.replaceIn(_c, $methodEq);

        //verify there are (0) occurrences of x == y in someEx
        assertEquals( 0, $binEqEq.countIn(_c));

        //verify there are (3) occurrences of x.equals(y) in _c
        assertEquals( 3, $methodEq.countIn(_c));
    }

    /**
     * Top Down Navigation
     * https://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/navigating_psi.html#top-down-navigation
     */
    public void testFindAllLocalVariables(){
        class L{
            int nonLocalField;

            public void ff(){
                int local1,local2;
                String local3;
            }
        }
        //print the cursor locations of all of the local vars
        $.varLocal().forEachIn(L.class, v-> System.out.println("Found a variable at offset " + v.ast().getRange().get().begin) );
    }

    /**
     * Bottom Up navigation
     * https://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview/navigating_psi.html#bottom-up-navigation
     */
    public void testWalkParents(){
        class K{
            int g;

            public void gg(){
                Function<Integer,Integer> f = (Integer a) -> {return a + 54;};
            }
        }
        //first navigate to a part of the code (find the int literal 54)
        IntegerLiteralExpr ile = $.intLiteral(54).firstIn(K.class).ast();


        MethodDeclaration md = //walk parents of the literal node to get to the containing method
                (MethodDeclaration)ile.stream($.PARENTS).filter(n-> n instanceof MethodDeclaration ).findFirst().get();
        //verify we found the method;
        assertEquals("gg", md.getNameAsString());
        //print the method
        System.out.println( md );
    }
}
