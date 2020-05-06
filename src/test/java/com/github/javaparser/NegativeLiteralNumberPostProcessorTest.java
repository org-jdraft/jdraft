package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.ASCIITreePrinter;
import junit.framework.TestCase;
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
