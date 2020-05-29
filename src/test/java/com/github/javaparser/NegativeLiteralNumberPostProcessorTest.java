package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.printer.ASCIITreePrinter;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.bot.$doubleExpr;
import org.jdraft.bot.$intExpr;

public class NegativeLiteralNumberPostProcessorTest extends TestCase {

    public void testP(){
        ParseResult<BlockStmt> pbs = Ast.JAVAPARSER.parseBlock("{}");
        System.out.println( pbs.getProblems() );
        assertTrue( pbs.isSuccessful() );
    }
    public void testSimpleNegInt(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ int i = -1; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1 int literal in the (default) JavaParser
        assertFalse( $intExpr.of(-1).isIn(cu));
        nnp.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1
        assertTrue( $intExpr.of(-1).isIn(cu));
        ASCIITreePrinter.print(pr.getResult().get());
    }

    public void testSimpleNegDouble(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ double i = -1.0; }");

        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1.0 double literal in the (default) JavaParser
        assertFalse( $doubleExpr.of(-1.0).isIn(cu));
        NegativeLiteralNumberPostProcessor.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1.0 double
        assertTrue( $doubleExpr.of(-1.0).isIn(cu));
        ASCIITreePrinter.print(pr.getResult().get());
    }

    public void testSimpleNegFloat(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ float i = -3.141589; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1.0 double literal in the (default) JavaParser
        assertFalse( $doubleExpr.of(-3.141589).isIn(cu));
        nnp.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1.0 double
        assertTrue( $doubleExpr.of(-3.141589).isIn(cu));
        ASCIITreePrinter.print(pr.getResult().get());
    }

    public void testSpacingAroundUnary(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ int i = -  10000; }");
        CompilationUnit cu = pr.getResult().get();
        ASCIITreePrinter.print(cu);

        //verify that I cant find a -1.0 double literal in the (default) JavaParser
        assertFalse( $intExpr.of(-  10000).isIn(cu));
        nnp.doProcess(pr);

        //verify that after condensing the unaryExpr and int literal I CAN find a -1.0 double
        assertTrue( $intExpr.of(-   10000).isIn(cu));
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

    public void testDoubleNegativeLong(){
        NegativeLiteralNumberPostProcessor nnp = new NegativeLiteralNumberPostProcessor();
        JavaParser jp = new JavaParser();
        ParseResult<CompilationUnit> pr = jp.parse("class C{ long l = - - 10000L; }");
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
        IntegerLiteralExpr ile = Exprs.intExpr(-12);
        Print.tree(ile);
        UnaryExpr ue = new UnaryExpr(ile, UnaryExpr.Operator.MINUS);
        Expression res = NegativeLiteralNumberPostProcessor.replaceUnaryWithNegativeLiteral(ue);
        Print.tree(res);
    }

    public void testLongNumNegativeUnaryNegative() {
        LongLiteralExpr ile = Exprs.longExpr(-12L);
        Print.tree(ile);
        UnaryExpr ue = new UnaryExpr(ile, UnaryExpr.Operator.MINUS);
        Expression res = NegativeLiteralNumberPostProcessor.replaceUnaryWithNegativeLiteral(ue);
        Print.tree(res);
    }

    public void testAll(){
        class C{
            int i = -1;
            double d = -2.0d;
            float f = -3.0f;
            long l = -100L;
        }

        _class _c = _class.of(C.class);
        Print.tree( _c.ast());
        //verify that we should NOT have UnaryExprs, only Int, Long, & Double literals
        assertFalse(_c.ast().findFirst(UnaryExpr.class).isPresent());

        assertEquals(1, Tree.list(_c, _intExpr.class, _i-> _i.getValue()==-1).size());

        //verify that we can find each
        assertTrue( _c.ast().findFirst(IntegerLiteralExpr.class, ile-> ile.getValue().startsWith("-")).isPresent());
        assertTrue( _c.ast().findFirst(DoubleLiteralExpr.class, ile-> ile.getValue().equals("-2.0d")).isPresent());
        assertTrue( _c.ast().findFirst(DoubleLiteralExpr.class, ile-> ile.getValue().equals("-3.0f")).isPresent());
        assertTrue( _c.ast().findFirst(LongLiteralExpr.class, ile-> ile.getValue().equals("-100L")).isPresent());
    }

    public void testDoubleNegNum(){
        DoubleLiteralExpr dle = Exprs.doubleExpr(-1.1);
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
