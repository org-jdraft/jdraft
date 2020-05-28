package com.github.javaparser.printer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft.Exprs;
import org.jdraft._class;
import org.jdraft.macro._final;
import org.jdraft.macro._public;
import org.jdraft.macro._static;

import java.util.List;
import java.util.function.Predicate;

public class ASCIITreePrinterTest extends TestCase {

    public void testPrintRecalc(){
        //create AST from String
        CompilationUnit cu = Ast.of("public class G{\n"+
                "    static public final int i = 0;\n"+
                "}");

        //print original tree
        ASCIITreePrinter.print(cu);

        TypeDeclaration td = cu.getType(0);
        List<Node> cs = td.getChildNodes();

        System.out.println( cs );
        //simple modification
        td.setStatic(true);

        System.out.println( td.getChildNodes() );



        //print out modified AST
        ASCIITreePrinter.print(cu);

        /*
        cu.recalculatePositions();
        System.out.println( "After recalc");
        ASCIITreePrinter.print(cu);
        */
    }

    public void testPrint(){
        ObjectCreationExpr oce = Exprs.newExpr(
                "new Object(){\n"+
                "    int i;\n"+
                "}");
        ASCIITreePrinter.print(oce);

        @_static @_public class G{
            @_static @_public @_final int i = 0;
        }
        _class _c = _class.of(G.class);
        System.out.println( _c );
        ASCIITreePrinter.print(Ast.reparse(_c.astCompilationUnit()));


    }

    public void testPrintCustomPrintFn(){
        ObjectCreationExpr oce = Exprs.newExpr( "new Object(){"+System.lineSeparator()+"int i;"+System.lineSeparator()+"}");

        //here we pass in a Function to tell how each node is printed (here uses the ClassSimpleName and Range)
        ASCIITreePrinter.print(oce, n-> "["+ ASCIITreePrinter.nodeSummary(n)+"] "+n.getClass().getSimpleName()+" : " +n.getRange().get() );

        @Deprecated
        class C{
            int i = 100;

            /** Javadoc */
            public void m(){
                assert(true);
                System.out.println(123);
                System.out.println("A" + 1 + "V");
                Predicate<String> ps = (s)-> s.startsWith("a");
            }
        }
        _class _c = _class.of(C.class);
        ASCIITreePrinter.print(_c.astCompilationUnit() );

        ASCIITreePrinter.print(_c.astCompilationUnit(), n-> "\""+ ASCIITreePrinter.nodeSummary(n)+"\" ["+n.getClass().getSimpleName()+"]");
    }
}
