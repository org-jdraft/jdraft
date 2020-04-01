package org.jdraft;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft.pattern.$comment;
import org.jdraft.pattern.$stmt;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.github.javaparser.utils.Utils.normalizeEolInTextBlock;

/**
 * Verify that we can parse these Strings to create Ast Node entities
 * @author Eric
 */
public class AstTest extends TestCase {

    public static final PrettyPrinterConfiguration PRINT_COMMENTS = new PrettyPrinterConfiguration()
            .setVisitorFactory(PrintComments::new);

    public static class PrintComments extends PrettyPrintVisitor {

        public PrintComments(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        @Override
        public void visit(final ClassOrInterfaceDeclaration n, final Void arg) {
            System.out.println( "HELLO ");
            //System.out.println( n.getAllContainedComments() );
            System.out.println( "ORPHANS" + n.getOrphanComments() );
            super.visit(n, arg);

        }

        @Override
        public void visit(final LineComment n, final Void arg) {
            System.out.println( "Visiting Line "+n);
            if (configuration.isIgnoreComments()) {
                return;
            }
            printer.print("//");
            printer.println(normalizeEolInTextBlock(n.getContent(), "").trim());
        }

        @Override
        public void visit(final BlockComment n, final Void arg) {
            System.out.println( "Visiting Block "+n);
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
            System.out.println( "Visiting Javadoc "+n);
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

    public void testNodes(){
        TypeDeclaration td = StaticJavaParser.parseTypeDeclaration(
                "class C{\n"+
                        "    //not orphan\n"+
                        "    int i;\n"+
                        "    /*orphan*/\n"+
                        "}");
        System.out.println( td );
        assertEquals( 2, td.getAllContainedComments().size());
        td.setPublic(true); //orphan and /*orphan*/ are gone


        System.out.println( td.toString(PRINT_COMMENTS) );
        //td.toString(new PrettyPrinterConfiguration())
        assertEquals(2, td.getAllContainedComments().size());
    }

    public void testChangeModifiersRemovesOrphanedMethods(){
        TypeDeclaration td = StaticJavaParser.parseTypeDeclaration(
                "class C{\n"+
                "    //orphan\n"+
                "    /*orphan*/\n"+
                "}");
        System.out.println( td );
        assertEquals( 2, td.getAllContainedComments().size());
        td.setPublic(true); //orphan and /*orphan*/ are gone


        System.out.println( td.toString(PRINT_COMMENTS) );
        //td.toString(new PrettyPrinterConfiguration())
        assertEquals(2, td.getAllContainedComments().size());
    }

    public void testParseAndExtractOrphanComments(){
        CompilationUnit cu = StaticJavaParser.parse("class C{\n" +
                "   //comment\n"+
                "   /*comment*/\n"+
                "}");
        System.out.println( cu );

        TypeDeclaration td  = cu.getType(0);

        System.out.println( td );

        CompilationUnit cu2 = new CompilationUnit();
        cu2.addType( td );

        System.out.println( cu2 );

    }

    public void testParseNewCode(){
        SwitchExpr se = Ast.switchEx("switch(s) { default : yield 1; }");
        assertTrue(se.getEntry(0).getLabels().isEmpty());
        se = Ast.switchEx("switch(s) { default -> 1; }");
        assertTrue(se.getEntry(0).getLabels().isEmpty());
        se = Ast.switchEx("switch(s) { default : 1; }");
        assertTrue(se.getEntry(0).getLabels().isEmpty());

        se = Ast.switchEx("switch(s) { case 1: 1; }");
        assertTrue(se.getEntry(0).getLabels().get(0).isIntegerLiteralExpr());
    }

    public void testReplaceCommentMultiStatements(){
        class FF{
            void m(){
                assert(true);
                //Comment
                assert(true);
            }
        }
        _class _c = _class.of(FF.class);
        //replace the comment with multiple statements
        Comments.replace(_c.ast(), $comment.of().firstIn(_c),
                Statements.of("System.out.println(1);"),
                Statements.of("System.out.println(2);"));

        System.out.println( _c);
    }

    public void testReplaceCommentSingleStatement(){
        class HH{
            void onlyComment(){
                /**comment*/
            }

            void lastComment(){
                assert(true);
                /**comment*/
            }
            void commentInNestedBlock(){
                if(1==1){
                    assert(true);
                    /**comment*/
                    assert(true);
                }
            }
        }
        _class _c = _class.of(HH.class);
        MethodDeclaration md =  _c.getMethod("commentInNestedBlock").ast();
        List<com.github.javaparser.ast.comments.Comment> cs = md.getAllContainedComments();
        Comments.replace( md, cs.get(0), Statements.of("System.out.println(3);") );
        System.out.println( md );
        assertEquals(Statements.of("System.out.println(3);"), $stmt.ifStmt().firstIn(md).ast().getThenStmt().asBlockStmt().getStatement(1));

        md =  _c.getMethod("lastComment").ast();
        cs  =md.getAllContainedComments();
        Comments.replace( md, cs.get(0), Statements.of("System.out.println(2);") );
        System.out.println( md );
        assertEquals(Statements.of("System.out.println(2);"), _method.of(md).getStatement(1));

        md =  _c.getMethod("onlyComment").ast();
        cs  =md.getAllContainedComments();
        Comments.replace( md, cs.get(0), Statements.of("System.out.println(1);") );
        assertEquals(Statements.of("System.out.println(1);"), _method.of(md).getStatement(0));
    }

    /**
     * Replace a Comment with one or more statements
     * I need an insertAt(Node n, Statement s , Position startPosition )
     */
    public void testReplaceComment(){
        class T{
            void t(){
                /*Here*/
            }
        }
        _class _c = _class.of(T.class);
        MethodDeclaration md = _c.getMethod("t").ast();
        com.github.javaparser.ast.comments.Comment c = md.getAllContainedComments().get(0);
        Comments.replace(c, Statements.of( ()->System.out.println(1)));

        assertEquals( Statements.of( ()->System.out.println(1)), md.getBody().get().getStatement(0) );
        //insertStatement(_c.astCompilationUnit(), Stmt.of("System.out.println(1);"), c.getRange().get().begin);

        _c = _class.of(T.class);
        md = _c.getMethod("t").ast();
        c = md.getAllContainedComments().get(0);
        Comments.replace(c, Statements.of( ()->System.out.println(1)),
                Statements.of( ()->System.out.println(2)) );

        System.out.println( md );

        assertEquals( Statements.of( ()->System.out.println(1)), md.getBody().get().getStatement(0) );
        assertEquals( Statements.of( ()->System.out.println(2)), md.getBody().get().getStatement(1) );

        //System.out.println( _c );
    }

    public void testAddComment(){
        class C{
            public void m(){

            }
            public void m2(){
                System.out.println(1);
                //LineComment
                System.out.println("Hey");
                System.out.println(2);
            }
            public void m3(){
                /* block comment */
                System.out.println( 11 );
            }
        }
        _class _c =_class.of(C.class);
        System.out.println(_c );
        _method _m = _c.getMethod("m2");
        _m.getStatement(0).replace(Statements.of("assert(1==1)"));
        System.out.println(_c);

        //TODO does this make sense
        //Stmt.commentOut( _m.getStatement(0));
        Statements.REPLACE_WITH_EMPTY_STMT_COMMENT_FN.apply( _m.getStatement(0) );
        System.out.println( _m  );
        System.out.println( _m.toString(Print.EMPTY_STATEMENT_COMMENT_PRINTER) );
    }

    public void testReplaceStatementWithComment(){
        class C{
            int a;
            public void m(){
                System.out.println(1);
                //comment
                System.out.println(2);
                /* comment */
                System.out.println(3);
                /** comment */
                System.out.println(4);

                /*add*/

                System.out.println( "this is the first line"+
                        "this is the second line"+
                        "this is the third line"+
                        "this is the forth line"+
                        "this is the fifth line"+
                        "this is the sixth line");
            }
            public void b(){

            }
        }
        //_class _c = $stmt.of(($any$)->System.out.println($any$)).commentOut(C.class);
        _class _c = _class.of(C.class);

        System.out.println( _c );
        System.out.println( _c.toString(Print.EMPTY_STATEMENT_COMMENT_PRINTER));

        _c = _class.of(C.class);
        MethodDeclaration b = _c.getMethod("b").ast();
        System.out.println( b.getRange().get() );
        System.out.println( b.getOrphanComments() );

        BlockComment bc = new BlockComment("BLOCK");
        b.addOrphanComment(bc);
        System.out.println( b.getRange().get() );
        System.out.println( b.getOrphanComments() );

        MethodDeclaration md =_c.getMethod("m").ast();
        Statement st1 = md.getBody().get().getStatement(0);
        LineComment lc = new LineComment("//ORPHAN LINE");

        lc.setRange( new Range( st1.getRange().get().begin, st1.getRange().get().end));
        //st1.remove();
        md.addOrphanComment( lc );
        //md.addOrphanComment( new BlockComment("ORPHAN BLOCK"));
        System.out.println( _c );
        /*
        Node parent = st1.getParentNode().get();

        if( parent instanceof NodeWithBody){
            NodeWithBody nwbs = (NodeWithBody)parent;
            nwbs.a
        }
         */
        //st1.setComment()
    }

    public void testUnCommentOnlyStmt(){
        class C {
            //only statement
            void m() {
                /*<code>System.out.println(1);</code>*/
            }
        }
        _class _c = _class.of(C.class);
        _method _m = _c.getMethod("m");
        List<com.github.javaparser.ast.comments.Comment> cs = _m.ast().getAllContainedComments();
        //System.out.println( cs );
        com.github.javaparser.ast.comments.Comment c = cs.get(0);
        Range range = c.getRange().get();

        //$stmt.of().$isBefore()
        List<Statement> sts = Tree.list(_m, Statement.class, st-> st.getRange().get().isAfter(c.getRange().get().end));
        assertTrue( sts.isEmpty() );
        //if there are NO statements before
        if( sts.isEmpty() ){
            String content = c.getContent();
            c.remove();
            _m.add(0, content.substring("<code>".length(), content.length() -"</code>".length()));
        }
        //System.out.println( sts );
        System.out.println(_m);
    }
    /**
     *
     */
    public void testUnComment(){
        class C{
            //only statement
            void m(){
                /*<code>System.out.println(1);</code>*/
            }
            //last statement
            void m2(){
                assert(0==0);
                /*<code>System.out.println(1);</code>*/
            }
            //first statement
            void m3(){
                /*<code>System.out.println(1);</code>*/
                assert(0==0);
            }
            //middle statement
            void m4(){
                assert(0==0);
                assert(0==0);
                assert(0==0);
                /*<code>System.out.println(1);</code>*/
                assert(0==0);
            }
            //nested only statement
            void m5(){
                if( 1== 1 ){
                    /*<code>System.out.println(1);</code>*/
                }
            }
            //nested last statement
            void m6(){
                if(1==1){
                    assert(0==0);
                    /*<code>System.out.println(1);</code>*/
                }
            }

            void m7(){
                if(1==1){
                    assert(0==0);
                    /*<code>System.out.println(1);</code>*/
                    assert(1==1);
                }
            }
        }
        _class _c = _class.of(C.class);
        _method _m = _c.getMethod("m");
        List<com.github.javaparser.ast.comments.Comment> cs = _m.ast().getAllContainedComments();
        //System.out.println( cs );
        com.github.javaparser.ast.comments.Comment c = cs.get(0);
        Range range = c.getRange().get();

        List<Statement> sts = Tree.list(_m, Statement.class, st-> st.getRange().get().isAfter(c.getRange().get().end));
        System.out.println( sts );


    }

    public void testP( ){
        CompilationUnit cu = Ast.of(AstTest.class);
        cu.walk(VariableDeclarationExpr.class, v -> System.out.println(v + " "+ v.getRange().get()));
    }

    public void testMemberAt(){
        CompilationUnit cu = Ast.of(
                "class C{ ",
                "int f=1;",
                "}");
        At.memberAt(cu, 1);
        System.out.println( cu.getRange().get() );

        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        At.memberAt(cu, 1);
        Log.setAdapter(new Log.SilentAdapter());

    }

    /**
    @interface ann2{
        int k();
        char v();
    }
    @interface AGGGG{}

     * method JAVADOC

    @ann
    @ann2(k = 3, v = 'i')
    @Deprecated
    @AGGGG
    abstract static private void doIt(final int firstParameter, @ann @ann2(k = 5) final String xx, int... varArgs) throws DumbException, AnotherException, BlahException {
    }
    */

    public void testAstStaticAbstract(){
        //Ast.method("abstract static private void doIt();");
        //Ast.method("abstract static private void doIt(){}");
    }

    public void testCommonAncestor(){
        class FG{
            void m(){
                System.out.println(1);
                System.out.println(2);
                if(1 == 1 ){
                    System.out.println( 3);
                    label:
                    {
                        System.out.println( 4);
                        System.out.println(5 );
                    }
                    System.out.println(6);
                }
            }
        }
        _class _c = _class.of(FG.class);

        Statement s5 = $stmt.of( ()->System.out.println(5)).firstIn(_c).ast();
        Statement s1 = $stmt.of( ()->System.out.println(1)).firstIn(_c).ast();

        Node commonAncestor = Tree.commonAncestor(s5, s1);
        assertTrue( Tree.isParent( commonAncestor, Ast.METHOD_DECLARATION));
        //assertTrue( $.of().$hasParent($method.of()).match(commonAncestor) );

        assertTrue( commonAncestor instanceof BlockStmt );
    }
    public void testIsParent(){
        _class _c = _class.of("V", new Object(){
            int x = 0;
            void f(){
                int hh = 103;
            }
        });
        assertTrue(
                Tree.isParent( _c.getField("x").getFieldDeclaration(),
                        ClassOrInterfaceDeclaration.class, c-> c.getNameAsString().equals("V")) );
    }



    //within(Range)

    public static class Outer{

    }

    public void testOrganizeImports(){
        _class _c = _class.of("C");//.imports(Outer.class);
        assertTrue(_c.getImports().isEmpty());
        Ast.organizeImports(  _c.astCompilationUnit() );
        assertTrue(_c.getImports().isEmpty());
        _c.addImports("gggg.G");
        assertEquals( _import.of("gggg.G"), _c.getImport(0));
        Ast.organizeImports(  _c.astCompilationUnit() );


        _c.addImports("A");
        _c.addImports("import static aaaa.bbbb.B.*;");
        _c.addImports("aaaa.bbbb.C");
        _c.addImports("import static zzzz.Z");

        Ast.organizeImports(  _c.astCompilationUnit() );

        System.out.println( _c );


    }

    public void testRoot(){
        class ROOT{
            int a = 1;
            void m(){}
        }

        _class _c = _class.of(ROOT.class);
        Node n = Ast.root( _c.getField("a").getInit() );

        assertTrue( n instanceof CompilationUnit );

        _method _m = _method.of("public void m(){}").setBody(()-> {System.out.println( 1); System.out.println(2);});
        assertTrue( Ast.root(_m.ast()) instanceof MethodDeclaration );
        _m = _method.of(new Object(){
            public void m(){
                int a = 100;
            }
        });

        assertTrue( Ast.root(_m.ast()) instanceof MethodDeclaration );

        _constructor _ct = _constructor.of(new Object(){
            String name;

            public void C(String name){
                this.name = name;
            }
        });

        assertTrue( Ast.root(_ct.ast()) instanceof ConstructorDeclaration );

        _field _f = _field.of("int i=0;");

        assertTrue( Ast.root(_f.ast()) instanceof FieldDeclaration );

        _anno _a = _anno.of("@R");

        assertTrue( Ast.root(_a.ast()) instanceof AnnotationExpr );

    }
    public void testFlattenLabel(){
        class C {
            public void m() {
                label: System.out.println( 1 );
                if(System.getProperty("A") != null){
                    label: {
                    System.out.println(2);
                    System.out.println(3);
                    }
                }
            }
            public void r(){
                label:{}
            }

            public void t(){
                label: {;}
            }
        }
        _class _c = _class.of( C.class);
        Tree.flattenLabel(_c.astCompilationUnit(), "label");
        System.out.println( _c );
    }
   /*
    public void testJDK12SwitchExpression(){        
        //SwitchExpr astSwe = 
        //    Ast.switchExpr( "int a = switch(x) { case 5,6 -> 20; case 9 -> 30; default -> 40; };"); 
        
        Statement st = 
            Ast.stmt( "int a = switch(x) { case 5,6 -> 20; case 9 -> 30; default -> 40; };");
        
        System.out.println( st );
        
        SwitchExpr se = new SwitchExpr();
        se.setSelector( Expr.of("x") );
        NodeList<SwitchEntry> ses = new NodeList<>();
        ses.add( Ast.switchEntry("case 5 -> 20;") );
        se.setEntries( ses );
        System.out.println( se ); 
        //assertEquals(3, astSwe.getEntries().size());
    }
    */
    
    public void testParseStaticClass(){
        TypeDeclaration td = Ast.typeDecl("public static class F{}");
        assertTrue( td.isStatic() );        
    }
    
    public void testCC(){
        class C{
            
        }
        TypeDeclaration td = Ast.typeDecl(C.class);
        
    }
    /**
     * Test switch "cases"
     */
    public void testSwitchEntry(){
        SwitchEntry se = Ast.switchEntry("case 1: System.out.println(1);");
        assertTrue( se.getLabels().get(0).equals(Expressions.of(1)) );
        assertEquals( se.getStatement(0), Statements.of("System.out.println(1);"));
        
        //the default case is always empty
        se = Ast.switchEntry("default: System.out.println(1);");
        assertTrue( se.getLabels().isEmpty() );
        assertEquals( se.getStatement(0), Statements.of("System.out.println(1);"));
        int a = 1;
        switch (a){
            case 1:
            case 2: break;
        }
        se = Ast.switchEntry("case 0:");
        assertTrue( se.getLabels().get(0).equals( Expressions.of(0)) );
        System.out.println( se.getType() );
        assertTrue( se.getStatements().isEmpty() );
        
        se = Ast.switchEntry("case 2: System.out.println(12);");
        assertTrue( se.getLabels().get(0).equals(Expressions.of(2)) );
        assertEquals( se.getStatement(0), Statements.of("System.out.println(12);"));
       // assertTrue( se.getLabels().get(1).equals(Expr.of(2)) );
        
        
    }
    
    public void testAnnoExprEqualsAndHash(){
        _anno _a = _anno.of("a(1)");
        _anno _b = _anno.of("a(value=1)");
        
        assertEquals( _a, _b);
        assertEquals(_a.hashCode(), _b.hashCode());
        
        assertNotSame(_a.ast(), _b.ast() );
        
        //make sure I can equate them to be equal
        assertTrue( Expressions.equal(_a.ast(), _b.ast()) );
        //assertTrue( Ast.annotationEqual(_a.ast(), _b.ast()) );
        assertEquals( Expressions.hash(_a.ast() ), Expressions.hash( _b.ast() ) );
        //assertEquals( Ast.annotationHash(_a.ast() ), Ast.annotationHash( _b.ast() ) );
        
    }
    
    public static class F{
        
        static int a(){
            return 1;
        }
        public static int b(){
            return 2;
        }
    }
    
    public void testImpliedModifiers() throws NoSuchMethodException{
        Class FF = F.class;
        System.out.println("A MODIFIERS " + F.class.getDeclaredMethod("a", new Class[0]).getModifiers());
        System.out.println("B MODIFIERS " + F.class.getDeclaredMethod("b", new Class[0]).getModifiers());
        _class _c = _class.of( F.class );
        
        _method _m1 = _c.getMethod("a");
        _method _m2 = _c.getMethod("b");
        
        System.out.println( _m1.getEffectiveModifiers() );
        System.out.println( _m2.getEffectiveModifiers() );
        
    }
    
    public void testTypeEquals(){
        Types.equal( Types.typeRef("java.util.List<java.lang.String>"),
                Types.typeRef("List<String>") );

        Types.equal( Types.typeRef(int.class), Types.typeRef("int"));
    }

    public void testTypeParameterAst(){
        TypeParameter tp = Types.typeParameter("<T extends Map>");
        assertFalse( tp.getParentNode().isPresent() );
    }

    public void testTypeParametersAst(){
        NodeList<TypeParameter> tps = Types.typeParameters("<T extends Map, A, R, F>");
        assertFalse( tps.getParentNode().isPresent() );
    }

    public void testWalkFirst(){
        class P{
            int g;

            void m(){
                b( (a)-> System.out.println(a));
            }
            void b( Consumer c ){

            }
        }
        CompilationUnit cu = Ast.of(P.class);

        LambdaExpr le = Tree.first(cu,
                LambdaExpr.class,
                (n) -> n.getRange().isPresent() && n.getRange().get().begin.line == 47 );

        System.out.println( le );
    }


    //walk first in Ast

    public void testTokenizeType(){
        List<String> toks = Types.tokenize( "String" );
        assertEquals(1, toks.size());
        assertEquals( "String", toks.get(0) );

        toks = Types.tokenize( "List<String>" );
        assertEquals(4, toks.size());
        assertEquals( "List", toks.get(0) );
        assertEquals( "<", toks.get(1) );
        assertEquals( "String", toks.get(2) );
        assertEquals( ">", toks.get(3) );

        toks = Types.tokenize( "List < String > " );
        assertEquals(4, toks.size());
        assertEquals( "List", toks.get(0) );
        assertEquals( "<", toks.get(1) );
        assertEquals( "String", toks.get(2) );
        assertEquals( ">", toks.get(3) );

        toks = Types.tokenize("java.util.List<java.lang.String>");
        assertEquals(4, toks.size());
        assertEquals( "java.util.List", toks.get(0) );
        assertEquals( "<", toks.get(1) );
        assertEquals( "java.lang.String", toks.get(2) );
        assertEquals( ">", toks.get(3) );


        toks = Types.tokenize("aaa.bbb.MyClass<java.lang.String>");

    }

    /** Javadoc */
    class Ex{
        /*BlockComment*/
        //LineComment
    }

    public void testEnumConstructor(){
        //_constructor _ct = _constructor.of("E(int i){this.i = i;}");
        ConstructorDeclaration cd = Ast.constructor( "E(int i){this.i = i;}");
    }

    public void testAstCache(){
        @Ast.cache
        class L{
            int x;
        }

        assertFalse(Ast.AST_CACHE_MAP.containsKey(L.class));
        Node n = Ast.typeDecl(L.class);
        assertTrue( n instanceof ClassOrInterfaceDeclaration );
        ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)n;
        //verify that the AST DOESNT have the Ast.cache annotation
        assertFalse( coid.getAnnotationByClass(Ast.cache.class).isPresent());
        assertTrue(Ast.AST_CACHE_MAP.containsKey(L.class));

        //we can always clear it
        Ast.AST_CACHE_MAP.clear();
        assertFalse(Ast.AST_CACHE_MAP.containsKey(L.class));
    }
    public void testAstWalkList(){
        System.out.println( Comments.list( Ast.typeDecl( Ex.class) ));

        Tree.in(Ast.typeDecl( Ex.class ), com.github.javaparser.ast.comments.Comment.class, c-> System.out.println(c.getClass()) );
        //Ast.walk( Ast.type( Ex.class ), Comment.class, c-> System.out.println(c.getClass()) );

    }

    public void testPackageDeclaration(){
        assertNull( Ast.packageDeclaration( "")); //
        assertNotNull( Ast.packageDeclaration( "a"));
        assertNotNull( Ast.packageDeclaration( "package a"));
        assertNotNull( Ast.packageDeclaration( "package a;"));
        assertNotNull(Ast.packageDeclaration( "a.b.c"));
        assertNotNull(Ast.packageDeclaration( "a.b.c;"));
        assertNotNull(Ast.packageDeclaration( "package a.b.c;"));
    }

    public void testImportDeclaration(){
        assertNull( Ast.importDeclaration( ""));
        assertNotNull( Ast.importDeclaration( "a"));
        assertNotNull( Ast.importDeclaration( "a;"));
        assertNotNull( Ast.importDeclaration( "a.*"));
        assertNotNull( Ast.importDeclaration( "a.*;"));
        assertNotNull( Ast.importDeclaration( "import a.*"));
        assertNotNull( Ast.importDeclaration( "import a.*;"));

    }

    public void testParseAnno(){
        AnnotationExpr ae = Ast.anno( "@a");
        assertTrue( ae instanceof MarkerAnnotationExpr );

        ae = Ast.anno( "@a(1)");
        assertTrue( ae instanceof SingleMemberAnnotationExpr );
        SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr)ae;
        assertEquals(Expressions.of( 1 ), sa.getMemberValue());

        ae = Ast.anno( "@a(k=1,v=2)");
        NormalAnnotationExpr na = (NormalAnnotationExpr)ae;

        assertEquals("k", na.getPairs().get( 0 ).getNameAsString());
        assertEquals(Expressions.of(1), na.getPairs().get( 0 ).getValue());
        assertEquals("v", na.getPairs().get( 1 ).getNameAsString());
        assertEquals(Expressions.of(2), na.getPairs().get( 1 ).getValue());
    }

    public void testParseAnnos(){
        NodeList<AnnotationExpr> ae = Ast.annos( "");
        assertTrue( ae.size() == 0 );
        ae = Ast.annos( "@ann");
        assertTrue( ae.size() == 1 );
        ae = Ast.annos( "@ann @ann2");
        assertTrue( ae.size() == 2 );
        ae = Ast.annos( "@ann(100)");
        assertTrue( ae.size() == 1 );
        ae = Ast.annos( "@ann(k=1,g=100)");
        assertTrue( ae.size() == 1 );
        ae = Ast.annos( "@ann(k=1,g=100) @ann2");
        assertTrue( ae.size() == 2 );
    }

    public void testParseAnnotation(){
        AnnotationDeclaration ad = Ast.annotationDecl( "@interface A{}");

        ad = Ast.annotationDecl( "/** JAVADOC*/ @ann @interface A{ }");
        assertTrue( ad.getAnnotations().isNonEmpty() );
        assertTrue( ad.getJavadocComment().isPresent());
    }



    public void testParseAnnotationMember(){
        Ast.annotationMemberDecl( "int VALUE();" );

        AnnotationMemberDeclaration amd =
                Ast.annotationMemberDecl( "/** JAVADOC*/ @ann int VALUE();" );

        amd = Ast.annotationMemberDecl( "/** JAVADOC*/ @ann int VALUE() default 100;" );
        assertTrue( amd.hasJavaDocComment() );
        assertTrue( amd.getAnnotations().isNonEmpty());

    }
    public void testParseClass(){
        //ClassOrInterfaceDeclaration cd = Ast.classDecl( "class C{}");
        ClassOrInterfaceDeclaration cd  = Ast.classDecl( "/** Javadoc */ @ann class C{}");
        assertTrue( cd.getAnnotations().isNonEmpty());
        assertTrue( cd.getJavadocComment().isPresent());
    }

    public void testParseConstructor(){
        ConstructorDeclaration cd = Ast.constructor( "C(){}");
        cd = Ast.constructor( "/** Javadoc */ @ann C(){}");
        assertTrue( cd.getAnnotations().isNonEmpty());
        assertTrue( cd.getJavadocComment().isPresent());
    }

    public void testParseEnum(){
        EnumDeclaration ed = Ast.enumDecl( "enum E { ; }");
        ed = Ast.enumDecl( "/** JAVADOC */ @ann enum E { ; }");
        assertTrue( ed.getAnnotations().isNonEmpty());
        assertTrue( ed.getJavadocComment().isPresent());
    }

    public void testField(){
        FieldDeclaration fd = Ast.field( "int a;");
        fd = Ast.field("/** JAVADOC */ @ann int f = 1002;");
        assertTrue( fd.getAnnotations().isNonEmpty());
        assertTrue( fd.getJavadocComment().isPresent());
    }

    public void testInterface(){
        ClassOrInterfaceDeclaration coid = Ast.interfaceDecl( "interface I{}" );
        coid = Ast.interfaceDecl( "/** JAVADOC*/ @ann interface I {}");
        assertTrue( coid.isInterface());
        assertTrue( coid.getAnnotations().isNonEmpty());
        assertTrue( coid.getJavadocComment().isPresent());
    }

    public void testJavadoc(){
        JavadocComment jdc = Ast.javadocComment( "this", "JAVADOC");
        System.out.println( jdc );

        jdc = Ast.javadocComment( "/** this */" );
        System.out.println( jdc );
    }

    @interface ann {
    }

    public void testMethod(){
        //no BODY
        MethodDeclaration md = Ast.method( "void m();");
        assertFalse(md.getBody().isPresent());

        //no BODY &
        md = Ast.method( "/** JAVADOC*/ @ann void m();");
        assertFalse(md.getBody().isPresent());
        assertTrue( md.getJavadocComment().isPresent());
        assertTrue(md.getAnnotationByClass( ann.class ).isPresent());
    }

    public void testModifiers(){
        _modifiers mods = _modifiers.of("");
        assertFalse(mods.isAbstract());

        mods = _modifiers.of("public static final");
        assertTrue( mods.isPublic() );
        assertTrue( mods.isStatic() );
        assertTrue( mods.isFinal() );

        //..this is just an EnumSet.. this ISNT logic about which MODIFIERS
        // are mutually exclusive, etc.
        mods = _modifiers.of("abstract public static final transient volatile strictfp protected private native");
        assertTrue( mods.isAbstract() );
        assertTrue( mods.isPublic() );
        assertTrue( mods.isStatic() );
        assertTrue( mods.isFinal() );
        assertTrue( mods.isTransient() );
        assertTrue( mods.isProtected() );
        assertTrue( mods.isPrivate() );
        //assertTrue( mods.isDefault() );
        assertTrue( mods.isNative() );
    }

    public void testParameter(){
        Parameter p = Ast.parameter( "int x" );

        p = Ast.parameter( "@ann @ann2 final int x" );
        assertTrue( p.isFinal());
        assertEquals( 2, p.getAnnotations().size() );
    }

    public void testParameters(){
        NodeList<Parameter> ps = Ast.parameters( "()");
        assertEquals(0, ps.size());

        ps = Ast.parameters( "int x");
        assertEquals(1, ps.size());

        ps = Ast.parameters( "(int x)");
        assertEquals(1, ps.size());

        ps = Ast.parameters( "int x", "int y");
        assertEquals(2, ps.size());


        ps = Ast.parameters( "(@ann @ann2 int x)");
        assertEquals(1, ps.size());

        ps = Ast.parameters( "(@ann @ann2 final int x, final String...s)");
        assertEquals(2, ps.size());
        assertEquals(Ast.parameter( "@ann @ann2 final int x" ), ps.get( 0 ));
        assertEquals(Ast.parameter( "final String...s" ), ps.get( 1 ));

        ps = Ast.parameters( "@ann @ann2 final int x","final String...s");
        assertEquals(2, ps.size());
        assertEquals(Ast.parameter( "@ann @ann2 final int x" ), ps.get( 0 ));
        assertEquals(Ast.parameter( "final String...s" ), ps.get( 1 ));
    }

    public void testStaticBlock(){
        InitializerDeclaration sb = Ast.staticBlock( "static{}");

        sb = Ast.staticBlock( "System.out.println(1);");
        InitializerDeclaration sb2 = Ast.staticBlock( "static {System.out.println(1);}");
        assertEquals(sb, sb2);


        //sb.getBody()
    }

    public void testTypeParameter(){
        //with
        assertNull( Types.typeParameter( "") );
        Types.typeParameter( "A");
        Types.typeParameter( "<A>");
        Types.typeParameter( "<A extends B>");
        Types.typeParameter( "<J extends A & B>");
        Types.typeParameter( "<T extends B1 & B2 & B3>" );
    }

    public void testTypeParameters(){
        assertTrue( Types.typeParameters( "" ).isEmpty());
        assertTrue( Types.typeParameters( "A").size() == 1);
        assertTrue( Types.typeParameters( "<A>").size() == 1);
        assertTrue( Types.typeParameters( "A extends R").size() == 1);
        assertTrue( Types.typeParameters( "<A extends R>").size() == 1);
        assertTrue( Types.typeParameters( "A, B").size() == 2);
        assertTrue( Types.typeParameters( "A, B extends C").size() == 2);
        assertTrue( Types.typeParameters( "A extends T, B").size() == 2);

        assertTrue( Types.typeParameters( "<A extends T, B>").size() == 2);

        assertTrue( Types.typeParameters(  "<T extends B1 & B2 & B3>" ).size() == 1);
        assertTrue( Types.typeParameters(  "T extends B1 & B2 & B3, R extends A" ).size() == 2);

    }


    public void testStatements(){
        Statements.assertStmt( "assert(1==1);" );
        Statements.blockStmt( "{ int i=1; System.out.println(i);}");
        Statements.breakStmt( "break;" );
        Statements.breakStmt( "break out;" );
        Statements.constructorCallStmt( "this();" );
        Statements.continueStmt( "continue;");
        Statements.continueStmt( "continue out;");
        ExpressionStmt es = new ExpressionStmt( Expressions.of("3+4"));
        Statements.expressionStmt( "c=a+b;");
        Statements.forEachStmt( "for( int x : expressions){}");
        Statements.doStmt( "do{ System.out.println(1); }while(n<100);");
        Statements.forStmt( "for(int i=0;i<100;i++){System.out.printlnt(1);}");
        Statements.ifStmt( "if(true){ System.out.println(1);}");
        Statements.labeledStmt( "label: System.out.println(1);");
        Statements.localClassStmt( "class F{ int x; }");
        Statements.returnStmt( "return 1;");
        //AST.switchEntryStmt("case 1: System.out.println(1);");
        // AST.switchEntryStmt( "(5+4): ");
        Statements.switchStmt("switch(i){}");
        Statements.switchStmt("switch(i){"
                + "case 1: System.out.println(12);"
                + "}");
        Statements.synchronizedStmt( "synchronized(var){}");
        Statements.throwStmt( "throw e;");
        Statements.throwStmt( "throw new Ception();");
        Statements.tryStmt( "try{ System.out.println(1); }finally{}");

        Statements.whileStmt( "while(true) { System.out.println(1); }");


    }

    public void testType(){
        Type t = Types.typeRef("MyType");
        t = Types.typeRef("MyType<String,Integer>");
    }
    public void testComment() {
        com.github.javaparser.ast.comments.Comment c = Ast.comment("// hello");
        assertTrue(c instanceof LineComment);
        //System.out.println( c );
        c = Ast.comment("/* hi */");
        assertTrue(c instanceof BlockComment);

        c = Ast.comment("/** hi */");
        assertTrue(c instanceof JavadocComment);
        System.out.println( c );
    }

    public void testListTodoComments(){
        /** TODO : */
        class L{
            /** TODO: hey */
            int f;

            /** TODO: in Javadoc comment*/
            void m(){
                // TODO: this
                System.out.println( 1 );
                //TODO lineComment

                /* TODO: hey */
                /** TODO JAVADOC inside codeblock */
            }
        }

        //_class.of(L.class)
        //        .walk(_method.class,
        //        m-> m.walkBodyPostOrder(
        //                JavadocComment.class,
        //                jdc -> System.out.println( ((JavadocComment)jdc).getContent() )));

        System.out.println(
               Tree.list( _class.of(L.class),
                    _javadocComment._withJavadoc.class,
                    jd -> jd.hasJavadoc() && jd.getJavadoc().getContents().startsWith("TODO")));

        //List all entities that have TODO tags within the Javadocs
        System.out.println(
                Tree.list(_class.of(L.class),
                _javadocComment._withJavadoc.class,
                jd -> jd.hasJavadoc() && jd.getJavadoc().getContents().startsWith("TODO") ) );

        //find any TODO tags within the code
        Tree.in( _class.of(L.class),
        //_class.of(L.class).walk(
                _body._hasBody.class,
                m -> Tree.in(m.getBody().ast(),
                        com.github.javaparser.ast.comments.Comment.class,
                        c-> c.getContent().trim().startsWith("TODO"),
                        jdc -> System.out.println( jdc.getContent() )) );
                //m-> m.walkBody(
                //        Comment.class,
                //        c-> ((Comment)c).getContent().trim().startsWith("TODO"),
                //        jdc -> System.out.println( ((Comment)jdc).getContent() )));



        //m-> m.walkAST(Comment.class, c-> c.getContent().trim().startsWith("TODO"), jdc -> System.out.println( jdc.getContent() )));
        //_class.of(L.class).forAllTypes(_method.class, m-> m.walkAST(LineComment.class, jdc -> System.out.println( jdc.getContent() )));
        //_types.of( L.class ).forAllTypes(_java._hasJavadoc.class, jd-> jd.getJavadoc().has("TODO"), jd-> System.out.println( jd ) );
        //System.out.println( _method.of(L.class, "m").getBlockStmt());
        //System.out.println( _field.of(L.class, "f") );



        //_method _m = _method.of(L.class, "m").walkAST(LineComment.class, lc -> { System.out.println( lc); lc.setContent("TODO: changed");} );
        //System.out.println( _m );
        //_m = _method.of(L.class, "m").walkAST(BlockComment.class, lc -> { System.out.println( lc); lc.setContent("TODO: changed");} );
        //System.out.println( _m );
        //AST.comment("//")

    }

    public void testLineComment(){
        LineComment lc = Ast.lineComment("//hello");
        LineComment lc2 = Ast.lineComment("hello");
        assertEquals(lc.getContent(), lc2.getContent() );
    }

    public void testJavadocComment(){
        JavadocComment jdc = Ast.javadocComment("Hello");
    }



    public void testBlockComment(){
        BlockComment bc = Ast.blockComment("hi");
        System.out.println( bc );

        bc = Ast.blockComment("/*hi*/");
        System.out.println( bc );


        bc = Ast.blockComment("/*", "hi", "*/");
        System.out.println( bc );


        bc = Ast.blockComment("/*",
                " * hi",
                " * and more */");
        System.out.println( bc );

        bc = Ast.blockComment("/*",
                " * hi",
                " */");
        System.out.println( bc );

        bc = Ast.blockComment("first", "second", "third");
        System.out.println( bc );
    }

    //Thus far, we have been using the Stencil toDir produce Strings,
    // what we want toDir do is have these Strings ultimately represent valid Java code
    // one big step in that direction is toDir parse
    // we use the awesome JavaParser library for making AST nodes
    // the AST API
    public void testAST(){
        //AST is a simple API for converting from a String toDir and AST Node
        Expression astExpr = Ast.expression("a - b");

        astExpr = Ast.expression("Math.sqrt((a * a) + (b * b))");

        //AST accepts String var args, where there is an inferred line break
        //between each element in the String array
        Statement astStmt = Ast.statement("System.out.println(",
                "\"this is the first line\" +",
                "somevar +",
                "\"this is the third line\");" );

        //when the AST node is parsed, the empty spaces are removed, so the result of these two are equal
        // this way you can compare the "content" of the Statements rather than trying toDir accept up spaces in Strings
        assertEquals(astStmt, Ast.statement("System.out.println(\"this is the first line\"   +   somevar   +   \"this is the third line\");"));
    }

    /** Convert text into AST nodes toDir compare the "real meat" content of the data
     * rather than worrying about internal spacing and code formatting */
    public void testParseAssertEqualityRegardlessOfSpaces(){
        assertEquals( Ast.expression("3 + 4"), Ast.expression("3+4") );
        //verify that the statements with the same contents are the same object (regardless of spaces)
        //we should test Statements verses other statements, not do String comparisons (because of spaces and indentation
        assertEquals( Ast.statement("System.out.println(3);"), Ast.statement("System.out.println( "," 3 "," );" ));

        assertEquals( Ast.anno("@ann(key=1,key2=\"Er\")"), Ast.anno("@ann( key = 1 , key2 = \"Er\" )"));

        assertEquals( Ast.blockStmt("{", "assert(true); assert(false);", "}"), Ast.blockStmt("{assert(true); assert(false);}"));

        assertEquals( Ast.typeDecl("interface i{}"), Ast.interfaceDecl("interface i", "{", "}"));
        assertEquals( Ast.typeDecl("enum e{}"), Ast.enumDecl("enum e", "{", "}"));
        assertEquals( Ast.typeDecl("class c{}"), Ast.classDecl("class c", "{", "}"));
        assertEquals( Ast.typeDecl("@interface at{}"), Ast.annotationDecl("@interface at", "{", "}"));

        assertEquals( Ast.of( "package h;", "import java.util.*;", "public class V{", "}"),
                Ast.of("package h;", "import java.util.*;", "", "public class V", "{}" ) );

        Types.typeRef("String" );
        Types.typeRef("List<String>" );
        assertEquals( Types.typeRef("Map< Integer,List< String >>"), Types.typeRef("Map<Integer, List<String>>" ));
        assertEquals( Ast.of("public class MyClass<T> extends BaseClass implements IClass{} "),
                Ast.of("public class MyClass<T>","    extends BaseClass","     implements IClass","{", "}"));

        assertEquals( Ast.method("void b(){}"), Ast.method("void b ()", "{", "}"));
        assertEquals( Ast.constructor("@ann", "public Ctor()", "{", "System.out.println(1);", "}" ),
                Ast.constructor("@ann public Ctor() { System.out.println(1); }" ));

        assertEquals( Ast.field("public static final int V = 1;"), Ast.field("public static final int V=1;") );
        assertEquals( Ast.anno("@d(key=1,key1=100)"),Ast.anno("@d( key = 1 , key1 = 100 )") );


        //assertEquals( AST.enumConstant("E()"), AST.enumConstant("E()"));
        //AST.classDecl("class G{}");
        //CompilationUnit cu = JavaParser.parse("@interface A{}");
        //cu.getType(0);
    }
}
