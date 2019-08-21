package org.jdraft;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import junit.framework.TestCase;
import org.jdraft.proto.$stmt;

import java.util.List;
import java.util.function.Consumer;

/**
 * Verify that we can parse these Strings to create Ast Node entities
 *
 * @author Eric
 */
public class AstTest extends TestCase {

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

        Statement s5 = $stmt.of( ()->System.out.println(5)).firstIn(_c);
        Statement s1 = $stmt.of( ()->System.out.println(1)).firstIn(_c);

        Node commonAncestor = Ast.commonAncestor(s5, s1);
        assertTrue( Ast.isParent( commonAncestor, Ast.METHOD_DECLARATION));
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
                Ast.isParent( _c.getField("x").getFieldDeclaration(),
                        ClassOrInterfaceDeclaration.class, c-> c.getNameAsString().equals("V")) );
    }
    public static class Outer{

    }

    public void testOrganizeImports(){
        _class _c = _class.of("C");//.imports(Outer.class);
        assertTrue(_c.getImports().isEmpty());
        Ast.organizeImports(  _c.astCompilationUnit() );
        assertTrue(_c.getImports().isEmpty());
        _c.imports("gggg.G");
        assertEquals( Ast.importDeclaration("gggg.G"), _c.getImport(0));
        Ast.organizeImports(  _c.astCompilationUnit() );


        _c.imports("A");
        _c.imports("import static aaaa.bbbb.B.*;");
        _c.imports("aaaa.bbbb.C");
        _c.imports("import static zzzz.Z");

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
        Ast.flattenLabel(_c.astCompilationUnit(), "label");
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
        TypeDeclaration td = Ast.type("public static class F{}");
        assertTrue( td.isStatic() );        
    }
    
    public void testCC(){
        class C{
            
        }
        TypeDeclaration td = Ast.type(C.class);
        
    }
    /**
     * Test switch "cases"
     */
    public void testSwitchEntry(){
        SwitchEntry se = Ast.switchEntry("case 1: System.out.println(1);");
        assertTrue( se.getLabels().get(0).equals(Expr.of(1)) );
        assertEquals( se.getStatement(0), Stmt.of("System.out.println(1);"));
        
        //the default case is always empty
        se = Ast.switchEntry("default: System.out.println(1);");
        assertTrue( se.getLabels().isEmpty() );
        assertEquals( se.getStatement(0), Stmt.of("System.out.println(1);"));
        int a = 1;
        switch (a){
            case 1:
            case 2: break;
        }
        se = Ast.switchEntry("case 0:");
        assertTrue( se.getLabels().get(0).equals( Expr.of(0)) );
        System.out.println( se.getType() );
        assertTrue( se.getStatements().isEmpty() );
        
        se = Ast.switchEntry("case 2: System.out.println(12);");
        assertTrue( se.getLabels().get(0).equals(Expr.of(2)) );
        assertEquals( se.getStatement(0), Stmt.of("System.out.println(12);"));
       // assertTrue( se.getLabels().get(1).equals(Expr.of(2)) );
        
        
    }
    
    public void testAnnoExprEqualsAndHash(){
        _anno _a = _anno.of("a(1)");
        _anno _b = _anno.of("a(value=1)");
        
        assertEquals( _a, _b);
        assertEquals(_a.hashCode(), _b.hashCode());
        
        assertNotSame(_a.ast(), _b.ast() );
        
        //make sure I can equate them to be equal
        assertTrue( Expr.equivalent(_a.ast(), _b.ast()) );
        //assertTrue( Ast.annotationEqual(_a.ast(), _b.ast()) );
        assertEquals( Expr.hash(_a.ast() ), Expr.hash( _b.ast() ) );
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
        Ast.typesEqual( Ast.typeRef("java.util.List<java.lang.String>"),
                Ast.typeRef("List<String>") );

        Ast.typesEqual( Ast.typeRef(int.class), Ast.typeRef("int"));
    }

    public void testTypeParameterAst(){
        TypeParameter tp = Ast.typeParameter("<T extends Map>");
        assertFalse( tp.getParentNode().isPresent() );
    }

    public void testTypeParametersAst(){
        NodeList<TypeParameter> tps = Ast.typeParameters("<T extends Map, A, R, F>");
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

        LambdaExpr le = _walk.first(cu,
                LambdaExpr.class,
                (n) -> n.getRange().isPresent() && n.getRange().get().begin.line == 47 );

        System.out.println( le );
    }


    //walk first in Ast

    public void testTokenizeType(){
        List<String> toks = Ast.tokenizeType( "String" );
        assertEquals(1, toks.size());
        assertEquals( "String", toks.get(0) );

        toks = Ast.tokenizeType( "List<String>" );
        assertEquals(4, toks.size());
        assertEquals( "List", toks.get(0) );
        assertEquals( "<", toks.get(1) );
        assertEquals( "String", toks.get(2) );
        assertEquals( ">", toks.get(3) );

        toks = Ast.tokenizeType( "List < String > " );
        assertEquals(4, toks.size());
        assertEquals( "List", toks.get(0) );
        assertEquals( "<", toks.get(1) );
        assertEquals( "String", toks.get(2) );
        assertEquals( ">", toks.get(3) );

        toks = Ast.tokenizeType("java.util.List<java.lang.String>");
        assertEquals(4, toks.size());
        assertEquals( "java.util.List", toks.get(0) );
        assertEquals( "<", toks.get(1) );
        assertEquals( "java.lang.String", toks.get(2) );
        assertEquals( ">", toks.get(3) );


        toks = Ast.tokenizeType("aaa.bbb.MyClass<java.lang.String>");

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
        Node n = Ast.type(L.class);
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
        System.out.println( Ast.listComments( Ast.type( Ex.class) ));

        _walk.in(Ast.type( Ex.class ), Comment.class, c-> System.out.println(c.getClass()) );
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
        assertEquals(Expr.of( 1 ), sa.getMemberValue());

        ae = Ast.anno( "@a(k=1,v=2)");
        NormalAnnotationExpr na = (NormalAnnotationExpr)ae;

        assertEquals("k", na.getPairs().get( 0 ).getNameAsString());
        assertEquals(Expr.of(1), na.getPairs().get( 0 ).getValue());
        assertEquals("v", na.getPairs().get( 1 ).getNameAsString());
        assertEquals(Expr.of(2), na.getPairs().get( 1 ).getValue());
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
        AnnotationDeclaration ad = Ast.annotationDeclaration( "@interface A{}");

        ad = Ast.annotationDeclaration( "/** JAVADOC*/ @ann @interface A{ }");
        assertTrue( ad.getAnnotations().isNonEmpty() );
        assertTrue( ad.getJavadocComment().isPresent());
    }



    public void testParseAnnotationMember(){
        Ast.annotationMember( "int VALUE();" );

        AnnotationMemberDeclaration amd =
                Ast.annotationMember( "/** JAVADOC*/ @ann int VALUE();" );

        amd = Ast.annotationMember( "/** JAVADOC*/ @ann int VALUE() default 100;" );
        assertTrue( amd.hasJavaDocComment() );
        assertTrue( amd.getAnnotations().isNonEmpty());

    }
    public void testParseClass(){
        //ClassOrInterfaceDeclaration cd = Ast.classDecl( "class C{}");
        ClassOrInterfaceDeclaration cd  = Ast.classDeclaration( "/** Javadoc */ @ann class C{}");
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
        EnumDeclaration ed = Ast.enumDeclaration( "enum E { ; }");
        ed = Ast.enumDeclaration( "/** JAVADOC */ @ann enum E { ; }");
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
        ClassOrInterfaceDeclaration coid = Ast.interfaceDeclaration( "interface I{}" );
        coid = Ast.interfaceDeclaration( "/** JAVADOC*/ @ann interface I {}");
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
        assertNull( Ast.typeParameter( "") );
        Ast.typeParameter( "A");
        Ast.typeParameter( "<A>");
        Ast.typeParameter( "<A extends B>");
        Ast.typeParameter( "<J extends A & B>");
        Ast.typeParameter( "<T extends B1 & B2 & B3>" );
    }

    public void testTypeParameters(){
        assertTrue( Ast.typeParameters( "" ).isEmpty());
        assertTrue( Ast.typeParameters( "A").size() == 1);
        assertTrue( Ast.typeParameters( "<A>").size() == 1);
        assertTrue( Ast.typeParameters( "A extends R").size() == 1);
        assertTrue( Ast.typeParameters( "<A extends R>").size() == 1);
        assertTrue( Ast.typeParameters( "A, B").size() == 2);
        assertTrue( Ast.typeParameters( "A, B extends C").size() == 2);
        assertTrue( Ast.typeParameters( "A extends T, B").size() == 2);

        assertTrue( Ast.typeParameters( "<A extends T, B>").size() == 2);

        assertTrue( Ast.typeParameters(  "<T extends B1 & B2 & B3>" ).size() == 1);
        assertTrue( Ast.typeParameters(  "T extends B1 & B2 & B3, R extends A" ).size() == 2);

    }


    public void testStatements(){
        Stmt.assertStmt( "assert(1==1);" );
        Stmt.block( "{ int i=1; System.out.println(i);}");
        Stmt.breakStmt( "break;" );
        Stmt.breakStmt( "break out;" );
        Stmt.thisConstructor( "this();" );
        Stmt.continueStmt( "continue;");
        Stmt.continueStmt( "continue out;");
        ExpressionStmt es = new ExpressionStmt( Expr.of("3+4"));
        Stmt.expressionStmt( "c=a+b;");
        Stmt.forEachStmt( "for( int x : expressions){}");
        Stmt.doStmt( "do{ System.out.println(1); }while(n<100);");
        Stmt.forStmt( "for(int i=0;i<100;i++){System.out.printlnt(1);}");
        Stmt.ifStmt( "if(true){ System.out.println(1);}");
        Stmt.labeledStmt( "label: System.out.println(1);");
        Stmt.localClass( "class F{ int x; }");
        Stmt.returnStmt( "return 1;");
        //AST.switchEntryStmt("case 1: System.out.println(1);");
        // AST.switchEntryStmt( "(5+4): ");
        Stmt.switchStmt("switch(i){}");
        Stmt.switchStmt("switch(i){"
                + "case 1: System.out.println(12);"
                + "}");
        Stmt.synchronizedStmt( "synchronized(var){}");
        Stmt.throwStmt( "throw e;");
        Stmt.throwStmt( "throw new Ception();");
        Stmt.tryStmt( "try{ System.out.println(1); }finally{}");

        Stmt.whileStmt( "while(true) { System.out.println(1); }");


    }

    public void testType(){
        Type t = Ast.typeRef("MyType");
        t = Ast.typeRef("MyType<String,Integer>");
    }
    public void testComment() {
        Comment c = Ast.comment("// hello");
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
               _walk.list( _class.of(L.class),
                    _javadoc._hasJavadoc.class,
                    jd -> jd.hasJavadoc() && jd.getJavadoc().getContent().startsWith("TODO")));

        //List all entities that have TODO tags within the Javadocs
        System.out.println(
                _walk.list(_class.of(L.class),
                _javadoc._hasJavadoc.class,
                jd -> jd.hasJavadoc() && jd.getJavadoc().getContent().startsWith("TODO") ) );

        //find any TODO tags within the code
        _walk.in( _class.of(L.class),
        //_class.of(L.class).walk(
                _body._hasBody.class,
                m -> _walk.in(m.getBody().ast(),
                        Comment.class,
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
        Expression astExpr = Ast.expr("a - b");

        astExpr = Ast.expr("Math.sqrt((a * a) + (b * b))");

        //AST accepts String var args, where there is an inferred line break
        //between each element in the String array
        Statement astStmt = Ast.stmt("System.out.println(",
                "\"this is the first line\" +",
                "somevar +",
                "\"this is the third line\");" );

        //when the AST node is parsed, the empty spaces are removed, so the result of these two are equal
        // this way you can compare the "content" of the Statements rather than trying toDir accept up spaces in Strings
        assertEquals(astStmt, Ast.stmt("System.out.println(\"this is the first line\"   +   somevar   +   \"this is the third line\");"));
    }

    /** Convert text into AST nodes toDir compare the "real meat" content of the data
     * rather than worrying about internal spacing and code formatting */
    public void testParseAssertEqualityRegardlessOfSpaces(){
        assertEquals( Ast.expr("3 + 4"), Ast.expr("3+4") );
        //verify that the statements with the same contents are the same object (regardless of spaces)
        //we should test Statements verses other statements, not do String comparisons (because of spaces and indentation
        assertEquals( Ast.stmt("System.out.println(3);"), Ast.stmt("System.out.println( "," 3 "," );" ));

        assertEquals( Ast.anno("@ann(key=1,key2=\"Er\")"), Ast.anno("@ann( key = 1 , key2 = \"Er\" )"));

        assertEquals( Ast.blockStmt("{", "assert(true); assert(false);", "}"), Ast.blockStmt("{assert(true); assert(false);}"));

        assertEquals( Ast.type("interface i{}"), Ast.interfaceDeclaration("interface i", "{", "}"));
        assertEquals( Ast.type("enum e{}"), Ast.enumDeclaration("enum e", "{", "}"));
        assertEquals( Ast.type("class c{}"), Ast.classDeclaration("class c", "{", "}"));
        assertEquals( Ast.type("@interface at{}"), Ast.annotationDeclaration("@interface at", "{", "}"));

        assertEquals( Ast.of( "package h;", "import java.util.*;", "public class V{", "}"),
                Ast.of("package h;", "import java.util.*;", "", "public class V", "{}" ) );

        Ast.typeRef("String" );
        Ast.typeRef("List<String>" );
        assertEquals( Ast.typeRef("Map< Integer,List< String >>"), Ast.typeRef("Map<Integer, List<String>>" ));
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
