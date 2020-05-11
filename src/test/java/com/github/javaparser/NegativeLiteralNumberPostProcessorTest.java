package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.printer.ASCIITreePrinter;
import junit.framework.TestCase;
import org.jdraft.Expressions;
import org.jdraft.Print;
import org.jdraft.bot.$double;
import org.jdraft.bot.$int;

public class NegativeLiteralNumberPostProcessorTest extends TestCase {

    public void testSimpleNegInt(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ int i = -1; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1 int literal in the (default) JavaParser
        assertFalse( $int.of(-1).isIn(cu));
        nnp.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1
        assertTrue( $int.of(-1).isIn(cu));
        ASCIITreePrinter.print(pr.getResult().get());
    }

    public void testSimpleNegDouble(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ double i = -1.0; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1.0 double literal in the (default) JavaParser
        assertFalse( $double.of(-1.0).isIn(cu));
        nnp.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1.0 double
        assertTrue( $double.of(-1.0).isIn(cu));
        ASCIITreePrinter.print(pr.getResult().get());
    }

    public void testSimpleNegFloat(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ float i = -3.141589; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1.0 double literal in the (default) JavaParser
        assertFalse( $double.of(-3.141589).isIn(cu));
        nnp.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1.0 double
        assertTrue( $double.of(-3.141589).isIn(cu));
        ASCIITreePrinter.print(pr.getResult().get());
    }

    public void testSpacingAroundUnary(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ int i = -  10000; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1.0 double literal in the (default) JavaParser
        assertFalse( $int.of(-  10000).isIn(cu));
        nnp.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1.0 double
        assertTrue( $int.of(-   10000).isIn(cu));
        ASCIITreePrinter.print(pr.getResult().get());
    }

    public void testDoubleNegative(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ int i = - - 10000; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1.0 double literal in the (default) JavaParser
        //assertFalse( $int.of(- - 10000).isIn(cu));
        nnp.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1.0 double
        //assertFalse( $int.of(- - 10000).isIn(cu));
        ASCIITreePrinter.print(pr.getResult().get());
    }

    /**
     * test when we have a negative i.e. IntLiteralExpr number
     * (and it is the child of a MINUS unaryExpression)
     *
     *  └─"--1" UnaryExpr : (1,20)-(1,22)
     *    └─"-1" IntegerLiteralExpr : (1,22)-(1,22)
     *
     * we convert this to a double UnaryExpr with a nested (positive) IntegerLiteralExpr
     *
     * └─"--1" UnaryExpr : (1,20)-(1,22)
     *   └─"-1" UnaryExpr : (1,21)-(1,22)
     *     └─"1" IntegerLiteralExpr : (1,22)-(1,22)
     */
    public void testNumNegativeUnaryNegative() {
        IntegerLiteralExpr ile = Expressions.intLiteralEx(-12);
        Print.tree(ile);
        UnaryExpr ue = new UnaryExpr(ile, UnaryExpr.Operator.MINUS);
        Expression res = NegativeLiteralNumberPostProcessor.replaceUnaryWithNegativeLiteral(ue);
        Print.tree(res);
    }

    public void testDoubleNegNum(){
        DoubleLiteralExpr dle = Expressions.doubleLiteralEx(-1.1);
        Print.tree(dle);
        UnaryExpr ue = new UnaryExpr(dle,UnaryExpr.Operator.MINUS);
        Expression res = NegativeLiteralNumberPostProcessor.replaceUnaryWithNegativeLiteral(ue);
        Print.tree(res);
    }

    public void testMultiLine(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ int i = -"+System.lineSeparator()+" 12345; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        NegativeLiteralNumberPostProcessor.doProcess(pr);

        //make sure we take the UnaryExprs startLocation
        ASCIITreePrinter.print(cu);
    }
}
