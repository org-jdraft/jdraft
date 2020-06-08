package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.printer.ASCIITreePrinter;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.util.*;
import java.util.function.Function;

import static com.github.javaparser.utils.Utils.normalizeEolInTextBlock;

/**
 * Helpful things for printing the contents of the AST
 */
public interface Print {

    /**
     * a slight variant of the PrettyPrinter
     * that does (2) interesting things
     * <PRE>
     * (1)...it intercepts block statements which have NO statements and a single BlockComment
     * with the pattern /*<code> </code>* / and just prints the
     * {/*<code>$anything$</code>* /}
     *
     * (2) ...it intercepts EmptyStmts (usually printed as ";") that have comments, i.e.:
     * //LineComment
     * ;
     *
     * /* BlockComment* /
     * ;
     *
     * /** JavadocComment * /
     * ;
     *
     * and only prints out the comments (and NOT the ";")
     *
     * //LineComment
     * /* Block Comment * /
     * /** JavadocComment * /
     *
     */
    PrettyPrinterConfiguration EMPTY_STATEMENT_COMMENT_PRINTER
            = new PrettyPrinterConfiguration()
            .setVisitorFactory(EmptyStatementCommentPrinter::new);

    PrettyPrinterConfiguration PRINT_NO_TYPE_PARAMETERS = new PrettyPrinterConfiguration()
            .setPrintComments(false).setPrintJavadoc(false).setVisitorFactory(PrintNoTypeParams::new);
    /**
     * Dont print TypeParameters or Annotations... this is for dealing with {@link ClassOrInterfaceType} types
     * which MAY have "TYPE_UYSE" annotations AND erased generics in source code and we want to look at
     * ONLY the stripped/raw type:<PRE>
     *
     * for example:
     * Type t = Ast.typeRef("@ann aaaa.bbbb.MyType<T extends Blah>
     * assertEquals( "aaaa.bbbb.MyType", t.toString( PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS ) );
     *
     * if we want to compare the type (proper) to another type without annotations and Generics
     * </PRE>
     */
    PrettyPrinterConfiguration PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS
            = new PrettyPrinterConfiguration()
            .setPrintComments(false).setPrintJavadoc(false).setVisitorFactory(PrintNoAnnotationsOrTypeParams::new);

    PrettyPrinterConfiguration PRINT_NO_COMMENTS = new PrettyPrinterConfiguration()
            .setPrintComments(false).setPrintJavadoc(false);

    PrettyPrinterConfiguration PRINT_RAW_COMMENTS = new PrettyPrinterConfiguration()
            .setVisitorFactory(PrintRawComments::new);

    /**
     * a PrettyPrinterConfiguration designed to
     * <PRE>
     *  usage examples:
     *  _class _c = _class.of("C", new Object(){ @Deprecated int f; });
     *  _c.toString( Ast.PRINT_NO_ANNOTATIONS_OR_COMMENTS );
     * </PRE>
     */
    PrettyPrinterConfiguration PRINT_NO_ANNOTATIONS_OR_COMMENTS
            = new PrettyPrinterConfiguration()
                    .setPrintComments(false).setPrintJavadoc(false).setVisitorFactory(PrintNoAnnotations::new);

    /**
     * Describe the class contents as an ASCII tree
     * <PRE>
     * Print.tree(StaticJavaParser.parseExpression( "new Object(){\n    int i;\n}") );
     * </PRE>
     * prints:
     * <PRE>
     * "new Object() {...}" ObjectCreationExpr : (1,1)-(3,1)
     * ├─"Object" ClassOrInterfaceType : (1,5)-(1,10)
     * │ └─"Object" SimpleName : (1,5)-(1,10)
     * └─"int i;" FieldDeclaration : (2,5)-(2,10)
     *   └─"i" VariableDeclarator : (2,9)-(2,9)
     *     ├─"int" PrimitiveType : (2,5)-(2,7)
     *     └─"i" SimpleName : (2,9)-(2,9)
     * </PRE>
     * @param clazz
     */
    static void tree(Class clazz){
        _type _t = _type.of(clazz);
        if( _t.isTopLevel() ){
            ASCIITreePrinter.print( _t.astCompilationUnit() );
        } else {
            ASCIITreePrinter.print(_t.ast());
        }
    }

    /**
     * <PRE>
     * Print.tree(StaticJavaParser.parseExpression( "new Object(){\n    int i;\n}") );
     * </PRE>
     * prints:
     * <PRE>
     * "new Object() {...}" ObjectCreationExpr : (1,1)-(3,1)
     * ├─"Object" ClassOrInterfaceType : (1,5)-(1,10)
     * │ └─"Object" SimpleName : (1,5)-(1,10)
     * └─"int i;" FieldDeclaration : (2,5)-(2,10)
     *   └─"i" VariableDeclarator : (2,9)-(2,9)
     *     ├─"int" PrimitiveType : (2,5)-(2,7)
     *     └─"i" SimpleName : (2,9)-(2,9)
     * </PRE>
     * @param n
     */
    static void tree(Node n){
        ASCIITreePrinter.print(n);
    }

    static void tree(_tree._node _n ){
        ASCIITreePrinter.print(_n);
    }

    /**
     * <PRE>
     * Print.tree(StaticJavaParser.parseExpression( "new Object(){\n    int i;\n}") );
     * </PRE>
     * prints:
     * <PRE>
     * "new Object() {...}" ObjectCreationExpr : (1,1)-(3,1)
     * ├─"Object" ClassOrInterfaceType : (1,5)-(1,10)
     * │ └─"Object" SimpleName : (1,5)-(1,10)
     * └─"int i;" FieldDeclaration : (2,5)-(2,10)
     *   └─"i" VariableDeclarator : (2,9)-(2,9)
     *     ├─"int" PrimitiveType : (2,5)-(2,7)
     *     └─"i" SimpleName : (2,9)-(2,9)
     * </PRE>
     * @param n
     * @param nodeFormat
     */
    static void tree(Node n, Function<Node,String>nodeFormat){
        ASCIITreePrinter.print(n, nodeFormat);
    }

    class EmptyStatementCommentPrinter extends PrettyPrintVisitor {

        public EmptyStatementCommentPrinter(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        @Override
        public void visit(final BlockStmt n, final Void arg) {
            //if it's a block statement w/ 0 statements containing a single block comment
            if( n.isEmpty() && n.getOrphanComments().size() == 1 && n.getOrphanComments().get(0) instanceof BlockComment){
                //get the javadoc comment
                BlockComment bc = (BlockComment)n.getOrphanComments().get(0);
                if( bc.getContent().startsWith("<code>") && bc.getContent().endsWith("</code>") ) {
                    //just print the comment (without the block statement)
                    visit(bc, arg);
                }
                return;
            }
            super.visit( n, arg);
        }

        /** When we have an emptyStmt with a comment... print it as a simple comment */
        public void visit(final EmptyStmt es, final Void arg){
            if( es.getComment().isPresent() ){
                Comment com = es.getComment().get();
                if( com instanceof BlockComment ) {
                    visit( (BlockComment) com, arg);
                }
                if( com instanceof LineComment) {
                    visit( (LineComment) com, arg);
                }
                if( com instanceof JavadocComment) {
                    visit( (JavadocComment) com, arg);
                }
                return;
            }
            //its an empty statement, print the ;
            super.visit(es, arg);
        }
    }

    /**
     * An instance of a {@link com.github.javaparser.ast.visitor.VoidVisitor}
     * used to print out entities, will specifically Avoid printing ANNOTATIONS
     * and TypeParameters by simply overriding specific annotation METHODS with no-ops:
     *
     * @see #PRINT_NO_ANNOTATIONS_OR_COMMENTS
     *
     * This is also an example of an implementation of a Visitor for generating
     * the formatted .java source code, for the more comprehensive example:
     * @see PrettyPrintVisitor
     */
    class PrintNoTypeParams extends PrettyPrintVisitor {

        public PrintNoTypeParams(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        @Override
        public void visit(final ClassOrInterfaceType n, final Void arg) {
            if (!n.getAnnotations().isEmpty()) {
                NodeList<AnnotationExpr> annotations = n.getAnnotations();
                for (AnnotationExpr annotation : annotations) {
                    annotation.accept(this, arg);
                    printer.print(" ");
                }
                printer.print(" ");
            }
            if (n.getScope().isPresent()) {
                n.getScope().get().accept(this, arg);
                printer.print(".");
            }
            n.getName().accept(this, arg);
        }

        @Override
        public void visit(final MarkerAnnotationExpr n, final Void arg) {
            super.visit(n, arg);
        }

        public void visit(TypeParameter tp, Void arg){ }
    }

    /**
     * An instance of a {@link com.github.javaparser.ast.visitor.VoidVisitor}
     * used to print out entities, will specifically Avoid printing ANNOTATIONS
     * and TypeParameters by simply overriding specific annotation METHODS with no-ops:
     *
     * @see #PRINT_NO_ANNOTATIONS_OR_COMMENTS
     *
     * This is also an example of an implementation of a Visitor for generating
     * the formatted .java source code, for the more comprehensive example:
     * @see PrettyPrintVisitor
     */
    class PrintNoAnnotationsOrTypeParams extends PrettyPrintVisitor {

        public PrintNoAnnotationsOrTypeParams(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        @Override
        public void visit(final ClassOrInterfaceType n, final Void arg) {
            if (n.getScope().isPresent()) {
                n.getScope().get().accept(this, arg);
                printer.print(".");
            }
            n.getName().accept(this, arg);
        }

        public void visit(TypeParameter tp, Void arg){ }

        @Override
        public void visit(MarkerAnnotationExpr n, Void arg) {
        }

        @Override
        public void visit(SingleMemberAnnotationExpr n, Void arg) {
        }

        @Override
        public void visit(NormalAnnotationExpr n, Void arg) {
        }
    }

    /**
     * An instance of a {@link com.github.javaparser.ast.visitor.VoidVisitor}
     * used to print out entities, will specifically Avoid printing ANNOTATIONS
     * by simply overriding specific annotation METHODS with no-ops:
     *
     * @see Print#PRINT_NO_ANNOTATIONS_OR_COMMENTS
     *
     * This is also an example of an implementation of a Visitor for generating
     * the formatted .java source code, for the more comprehensive example:
     * @see PrettyPrintVisitor
     */
    class PrintNoAnnotations extends PrettyPrintVisitor {

        public PrintNoAnnotations(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        private void printMemberAnnotations(NodeList<AnnotationExpr> annotations, Void arg) {
        }

        private void printAnnotations(NodeList<AnnotationExpr> annotations, boolean prefixWithASpace, Void arg) {
        }

        @Override
        public void visit(MarkerAnnotationExpr n, Void arg) {
        }

        @Override
        public void visit(SingleMemberAnnotationExpr n, Void arg) {
        }

        @Override
        public void visit(NormalAnnotationExpr n, Void arg) {
        }
    }

    class PrintRawComments extends PrettyPrintVisitor {

        public PrintRawComments(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        @Override
        public void visit(final LineComment n, final Void arg) {
            if (configuration.isIgnoreComments()) {
                return;
            }
            printer.print(normalizeEolInTextBlock(n.getContent(), "").trim());
        }

        @Override
        public void visit(final BlockComment n, final Void arg) {
            if (configuration.isIgnoreComments()) {
                return;
            }
            String[] lines = n.getContent().split("\\r?\\n");
            StringBuilder sb = new StringBuilder();
            Arrays.stream(lines).forEach(con -> {
                String tr = con.trim();
                if (tr.startsWith("* ")) {
                    tr = tr.substring(2).trim();
                    sb.append(tr).append(System.lineSeparator());
                } else {
                    sb.append(con.trim()).append(System.lineSeparator());
                }
            });
            String con = sb.toString().trim();
            printer.print(con);
        }

        @Override
        public void visit(final JavadocComment n, final Void arg) {
            if (configuration.isPrintComments() && configuration.isPrintJavadoc()) {
                String[] lines = n.getContent().split("\\r?\\n");

                StringBuilder sb = new StringBuilder();
                Arrays.stream(lines).forEach(con -> {
                    String tr = con.trim();
                    if (tr.startsWith("* ")) {
                        tr = tr.substring(2).trim();
                        sb.append(tr).append(System.lineSeparator());
                    } else {
                        sb.append(tr).append(System.lineSeparator());
                    }
                });
                String con = sb.toString().trim(); //n.getContent();
                printer.print(con);
            }
        }
    }
}
