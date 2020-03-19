package org.jdraft;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import org.jdraft.macro._abstract;
import org.jdraft.macro._static;
import junit.framework.TestCase;
import org.jdraft.runtime._runtime;

/**
 *
 * @author Eric
 */
public class _methodTest extends TestCase {

    public void testBuildFromScratch(){
        _method _m = _method.of();
        System.out.println(_m);
    }
    public void testMethodMatch(){
        _class _c = _class.of("C", new Object(){
            void m(){}
            int i(){ return 1;}
            //primitive arrays are different
            boolean[] bb() { return null; }
            byte[] by() { return null; }
            short[] sh() { return null; }
            float[] ff() { return null; }
            char[] cc() { return null; }
            double[] dd() { return null; }
            long[] ll() { return null; }

            int[] ii(){ return new int[]{1,2};}

            void p(int p){}
            void tp(int i, String j){}

            void vararg(char...c){}
            List<UUID> vararg2(char...c){ return null;}

            List<String> listString(){ return null; }
            Map<String,Integer>map(){ return null; }

        });
        //create me a class with the signatures above
        Class clazz = _runtime.Class(_c.addImports(UUID.class));

        //verify I can match all signatures
        assertTrue( _c.listMethods().stream().allMatch( _m -> Arrays.stream(clazz.getDeclaredMethods()).anyMatch( m -> _method.match(_m, m) ) ));
    }

    public void testMethodMatch2(){
        _class _c = _class.of("C", new Object(){

            List<UUID>[] ii(){ return null;} //generic AND array
        });
        Class clazz = _runtime.Class(_c.addImports(UUID.class));

        //verify I can match all signatures
        assertTrue( _c.listMethods().stream().allMatch( _m -> Arrays.stream(clazz.getDeclaredMethods()).anyMatch( m -> _method.match(_m, m) ) ));
    }

    public void testModifierEqualsOrder(){

        _method _m1 = _method.of( "protected final static int g() { return 102; }");
        _method _m2 = _method.of( "protected static final int g() { return 102; }");

        assertEquals( _m1, _m2);
        assertEquals( _m1.hashCode(), _m2.hashCode());
    }

    public void testMM(){
        _method _m = _method.of("public int a(){ return 1;}");
        //_m.hasParametersOf(_m.ast());
    }
    public void testNewBodyMethodsFirstForEach(){
        _method _m = _method.of( new Object(){
            public void m(String name){
                System.out.println(name);
                assert 1==x;
                if( name.startsWith("mr")){
                    System.out.println( "Mr.");                    
                }                
            }            
            int x, y, z;
        });
        
        //verify that I 
        assertEquals( Tree.first( _m.getBody(), Ex.STRING_LITERAL), Ex.stringLiteralEx("mr") );
        /*
        //for All exprs and statements
        _m.forExprs(e-> System.out.println( e + " | " + e.getClass() ) );
        _m.forStmts(s-> System.out.println( s + " | " + s.getClass()) );
        
        assertNotNull( _m.firstExpr(BinaryExpr.class) );
        assertNotNull( _m.firstExpr(BinaryExpr.class, b-> b.getLeft().isIntegerLiteralExpr()));
        assertNotNull( _m.firstExpr(BinaryExpr.class, b-> b.getRight().isNameExpr()));
        assertNull( _m.firstExpr(CastExpr.class) );
        */
        assertNotNull( _m.firstStmt(Stmt.ASSERT));
        assertNotNull( _m.firstStmt(Stmt.ASSERT, a-> a.getCheck().isBinaryExpr()));
        
        assertNull( _m.firstStmt(Stmt.RETURN));

    }
    
    /**
     * Verify that if I have macro annotations on an Anonymous Object that
     * I pass into a 
     */
    public void testAnonymousObjectWithMacroAnnotations(){
        _method _m = _method.of( new Object(){
           @_abstract public void m(){} 
        });
        
        assertTrue( _m.isAbstract() ); //modifier is set
        assertTrue( !_m.ast().getBody().isPresent()); //body is removed
        assertTrue( !_m.hasAnno(_abstract.class)); //no annotation left
    }
    
    interface I{
        int m();
        default int n(){
            return 1;
        }
    }
    interface I2{
        public abstract int m();
        public default int n(){
            return 1;
        }
    }

    @interface A{
        int m();
    }

    @interface A2{
        public abstract int m();
    }

    public void testImpliedModifiers(){
        _method _m = _interface.of(I.class).getMethod("m");
        _method _m2 = _interface.of(I2.class).getMethod("m");
        _method _m3 = _method.of("int m();");
        _method _m4 = _method.of("public abstract int m();");

        /** Note that
         * int m();
         *    and
         * public abstract int m();
         *    ...are NOT the syntactically same,
         *    but they SHOULD be semantically the same
         */
        assertNotSame( _m.ast(), _m2.ast()); //checking the ASTs ARE NOT the same
        assertNotSame( _m.ast(), _m4.ast());//checking the ASTs ARE NOT the same
        assertNotSame( _m2.ast(), _m3.ast());//checking the ASTs ARE NOT the same

        /** here we check for SEMANTIC equality */
        assertEquals( _m, _m2);
        assertEquals( _m, _m3);
        assertEquals( _m, _m4);

        assertEquals( _m2, _m3);
        assertEquals( _m2, _m4);
    }

    public @interface annot{

    }

    public void testFullyQualified(){
        _method _m1 = _method.of("Map<B,C> getI( G g);");
        _method _m2 = _method.of("java.util.Map<aaaa.B, bbbbb.C> getI( G g);");
        //_method _m2 = _method.of("java.util.Map<aaaa.B, bbbbb.C> getI( fffff.G g);");

        assertEquals(
                Ast.typeHash(Ast.typeRef("Map")),
                Ast.typeHash(Ast.typeRef("java.util.Map")));

        assertTrue( Ast.typesEqual(
                Ast.typeRef("Map<B,C>"),
                Ast.typeRef("java.util.Map<B,C>")));

        assertEquals(
                Ast.typeHash(Ast.typeRef("Map<B,C>")),
                Ast.typeHash(Ast.typeRef("java.util.Map<B,C>")));

        assertEquals(
                Ast.typeHash(Ast.typeRef("Map<B,C>")),
                Ast.typeHash(Ast.typeRef("java.util.Map<aaaa.B,C>")));

        assertEquals(
                Ast.typeHash(Ast.typeRef("Map<B,C>")),
                Ast.typeHash(Ast.typeRef("java.util.Map<aaaa.B,bbbb.C>")));

        assertEquals( _m1, _m2);
        assertEquals( _m1.hashCode(), _m2.hashCode());
    }

    //find first

    // _parameter._hasParameters hasParametersOfType(Class<> clazz)
    //
    // Ast.block( lambda )
    // _body._hasBody.setBody( ()-> lambda)
    // _hasMethods.method( NAME, lambda)
    //
    // $method( lambda )
    // $ctor( lambda )
    //
    //ctor
    //STATIC_BLOCK via lambda

    public void testMethodAnonymousObjectShortcut() {
        //_method _m = _method.of("print",
        //        () -> System.out.println("Hello World!"));
        _method _m = _method.of( new Object(){
            public void print(){
                System.out.println("Hello World!");
            } 
        });
        
        //find the first Return statement with a comment
        //_m.firstStmt(Stmt.RETURN, s-> s.getComment().isPresent());
        
        //_m.findFirst(Node.class);
        //_m.findFirst(Node.class, n-> n.getComment().isPresent()); //find first commented node
        //assertTrue(_m.hasParametersOfType());

        assertNotNull( Tree.first(_m, StringLiteralExpr.class));
        assertNotNull( Tree.first(_m, Ast.STRING_LITERAL_EXPR));
        //assertNotNull( _m.firstExpr(StringLiteralExpr.class) );
        //assertNotNull( _m.firstExpr(Ex.STRING_LITERAL) );
        //assertNotNull( _m.findFirst(_anno.class) );

        //_m.ast().accept( VoidVisitor );
        //_m.ast().hasParametersOfType(Class...cs)
        assertFalse(_m.isDefault());
        _m.ast().isDefault();
        assertTrue(_m.isPublic());
        assertTrue(_m.isVoid());
        assertEquals(_m.getBody().getStatement(0), Stmt.of( ()-> System.out.println("Hello World!")));
        System.out.println(_m);

        _m = _method.of( new Object(){ 
            void print(String msg){ System.out.println("message : "+ msg ); } } 
        );
        //_m = _method.of("print", (String msg) -> System.out.println("message : " + msg));
        System.out.println(_m);

        _m = _method.of( new Object(){
            public void print( @annot final String msg ){
                System.out.println( "message : " + msg );
            }
        });
        
        //_m = _method.of("print", (@annot final String msg)
        //        -> System.out.println("message : " + msg));
        System.out.println(_m);

        //_m = _method.of("print", (@annot final String msg)
        //        -> System.out.println("message : " + msg));
        System.out.println(_m);
        
        _m = _method.of( new Object(){ public @_static void main(String[] args){
            System.out.println("main method");
        }});
        assertTrue( _m.isStatic() );
        //_m = _method.of("public static void main", (String[] args) -> System.out.println("main method"));
        System.out.println(_m);

        _m = _method.of(new Object(){
           /** Javadoc */
            @annot
            public @_static void main(String[] args){
                System.out.println("main method");
            }
        });
        assertTrue( _m.isStatic() );
        assertTrue( _m.hasAnno(annot.class));
        assertTrue( _m.hasJavadoc( ));
        
        //_m = _method.of("/** Javadoc */ @annot public static void main",
        //        (String[] args) -> System.out.println("main method"));
        System.out.println(_m);
    }

    /*
    public void testLambdaMethod(){
        _method _m = _method.of("public String compose", ()-> {return "message";} );
        System.out.println(_m);

        _m = _method.of("public String compose", (String msg)-> {return "message : "+msg;} );
        System.out.println(_m);

        _m = _method.of("public String compose", (@annot final String msg, @annot final Integer i)-> {return "message : "+msg+" "+i;} );
        System.out.println(_m);
    }
    */

    Supplier<String> s = ()-> {return "message";};


    public void testM2(){
        _method _m = _method.of("void a(){}");
        assertTrue( _m.getAnnos().isEmpty() );
        assertEquals( _m.getName(), "a" );

        assertTrue( _m.getParameters().isEmpty() );
        assertFalse( _m.isVarArg() );
    }

    public class Internal{
        /*
         * Block comment
         *
         */
        public _javadoc add(String... content){
            return null;
        }
    }

    public static void testClassStuff(final Class Cl ){
        if( Cl.isSynthetic()){
            System.out.println( "ITS A SYNTH CLASS");
        }
        if( Cl.isLocalClass()){
            System.out.println( "ITS A LOCAL CLASS");
        }
        if( Cl.isAnonymousClass()){
            System.out.println( "ITS A ANON CLASS");
        }
        try {
            System.out.println( Cl.getDeclaringClass() );
        }catch( NoClassDefFoundError ncdf ) {
            System.out.println( "No declaring Class for "+Cl);
        }

        /*
        try{
            if( Cl.getDeclaringClass() == null){
                System.out.println( "DEclaring Class is null");
            }
            if( Cl.getDeclaringClass() == Cl){
                System.out.println( "Its declared in iteslef");
            }
        }catch(NoClassDefFoundError ncdf  ){
            System.out.println( "NoClassDefFoundError");
        }
        */
    }
    public void testBlockComment(){
        Class Cl = Internal.class;
        System.out.println( Cl );
        testClassStuff( Internal.class);
        testClassStuff( _methodTest.class);


        //if( Cl.isMemberClass()){
        //    System.out.println( "ITS A MEMBER CLASS");
        //}

        _class _c = _class.of( _methodTest.class ).getDeclared("Internal");
        //_class _c = _class.of( Internal.class );
        System.out.println( "JAVADOC "+ _c.getMethod("add").getJavadoc() );
        System.out.println( _c );
    }
    public void testIsMain(){
        _method _m = _method.of("public static void main(String[] args){}");
        assertTrue( _m.isMain() );
    }
    public void testPrintAbstractMethod(){
        _method _m = _method.of("public abstract void aMethod();");
        System.out.println( _m );
        _m = _method.of("public abstract void aMethod(){}");
        System.out.println( _m );

        _m = _method.of("public void aMethod(){ System.out.println(1);}");
        _m.setAbstract();
        System.out.println( _m );

    }


    public interface Dict {
        String getDefinition(String word);
    }

    public void testAbstractMethodsNoMethodBody(){

        Dict.class.isMemberClass();
        Class c = Dict.class;
        c.isMemberClass();

        _interface _dict = _interface.of(Dict.class)
                .setPackage("dictionary.spi");
        _dict.getImports().clear();

        System.out.println( _dict );
        assertTrue( _dict.getMethod( "getDefinition" ).isAbstract() );
        //_javac.of( _dict );
    }


    public static class DONNER{
        public static int aMethod(String a, Map<Integer,String> b){
            return 5;
        }
    }


    /*
    public static class INNER{
        public static int aMethod(String a, Map<Integer,String> b){
            return 5;
        }
    }
    */
    public static class  MyClass{
        public void doIt(){
            System.out.println(1);
            System.out.println(2);
            System.out.println(3);
            for(int i=0;i<100;i++){
                System.out.println( i );
            }
        }
    }


    class Inner{
        void ih(){
            System.out.println( "1" );
            if( 1 == 1){
                System.out.print( "hey "+ 1);

            }
            char c = 'a';
            switch( c ){
                case 'a': System.out.print( c ); break;
                default : System.out.println( "default" ); break;
            }
        }
    }

    public static final Predicate<ExpressionStmt> MATCH_SYSTEM_OUT_ST_FN =
            m-> m.isExpressionStmt()
                    && ((ExpressionStmt)m).getExpression().isMethodCallExpr() &&
                    ((MethodCallExpr)m.asExpressionStmt().getExpression()).getScope().isPresent()
                    && ((MethodCallExpr)m.asExpressionStmt().getExpression()).getScope().get().toString().equals("System.out");

    public void testMethodRemoveStatements(){
        _method _ih = _class.of( Inner.class).getMethod( "ih");
        final List<ExpressionStmt> sof = new ArrayList<>();
        Tree.in( _ih.getBody().ast(), ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, e-> sof.add(e) );
        //_ih.walkBody( ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, e-> sof.add(e) );

        assertEquals( 4, sof.size() );

        Tree.in( _ih.getBody().ast(), ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, e-> e.remove() );
        //_ih.walkBody( ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, e-> e.removeIn() );
        sof.clear();

        Tree.in( _ih.getBody().ast(), ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, e-> sof.add(e) );
        //_ih.walkBody( ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, e-> sof.add(e) );
        assertEquals( 0, sof.size() );
    }

    static class F{
        int k;
        String[] y;
        String name;
        List<Date>dates;
        List<Integer> counts;
        @Override
        public int hashCode(){
            int hash = 11;
            $for_fields: {
                hash = 179 * hash + this.k;
                hash = 179 * hash + java.util.Arrays.hashCode(this.y);
                hash = 179 * hash + java.util.Objects.hashCode(this.name);
                hash = 179 * hash + java.util.Objects.hashCode(this.name);
                hash = 179 * hash + java.util.Objects.hashCode(this.counts);
            }
            return hash;
        }
    }



    public interface Int{
        void a();
    }

    /** Make sure that order within METHODS doesnt matter for typesEqual evaluation
    public void testOutOfOrderEquals(){
        _method _m1 = _method.of("public int a();");
        _method _m2 = _method.of("public int b();");

        assertEquals( _methods.of( _m1, _m2), _methods.of( _m2, _m1));
    }
    */

    public void testSimpleMethod(){
        _method _m = _method.of("void a();");
        assertEquals( "a", _m.getName() );
        assertTrue( _m.isVoid() );
        assertFalse( _m.isVarArg() );
        assertTrue( _m.getParameters().isEmpty() );
        assertTrue( _m.getTypeRef().isVoid());
    }

    /* REmoved this... I';ll accept it
    public void testSignaturesEqual(){
        _method a = _method.of("int getValue(String s){ return i;}");
        _method b = _method.of("static String getValue(String s){ return s;}");
        assertTrue( a.getSignature().matchesInput( b.getSignature() ) );

        assertFalse( a.VAR_ARG() );
        try{
            _methods _ms = _methods.of( a, b);
            fail("expected Exception for signature accept");
        }
        catch( DraftException de ){
            //expected
        }
    }
    */

    public void testM(){
        _method _m =
                _method.of("/** blah*/ @p public int getM(int a){ return 120;}");

        assertTrue( !_m.getJavadoc().isEmpty() );
        assertEquals( "blah", _m.getJavadoc().getContent().trim() );

        assertEquals( 1, _m.getAnnos().list( "p" ).size());
        assertEquals( "int", _m.getTypeRef().toString() );

        assertTrue( _m.getModifiers().isPublic() );
        assertEquals( "getM", _m.getName() );

        _parameter _pa = _m.getParameters().getAt( 0 );
        assertEquals( _pa.getTypeRef().toString(), "int" );
        assertEquals( _pa.getName().toString(), "a" );
    }

    public void testMatch(){
        //Predicate<_method> mm = m-> !m.isAbstract() && m.isStatic() && m.hasAnno(ann.class);
        Predicate<_method> mm = (_method m) -> !m.isAbstract() && m.isStatic() && m.hasAnno(ann.class);
        assertTrue( mm.test( _method.of("@ann", "public static void doIt(){}")));
          
        
        //ideally we want
        assertTrue( Tree.list(_class.of(_methodTest.class),_method.class ).size() >=5);
        
        //_1_build a method match that
        mm = m-> m.isPublic() && m.isVoid() && !m.isStatic() && m.getName().startsWith("test");

        //_method.Match testMatch = _method.match( m-> m.isPublic() && m.isVoid())
        //    .NAME( n-> n.startsWith( "test"));
        
        Predicate<_method> testMatchP = m-> m.isPublic() && m.isVoid() && m.getName().startsWith( "test");
            
        assertTrue( Tree.list( _class.of(_methodTest.class),_method.class, mm ).size() >=5 );
        
        assertTrue( Tree.list(_class.of(_methodTest.class),_method.class ).size() >=5 );
        assertTrue( Tree.list(_class.of(_methodTest.class), _method.class,
                m -> m.isPublic() && m.isVoid() && m.getName().startsWith("test") ).size() >=5);
            //_method.match(m-> m.isPublic() && m.isVoid())
            //    .NAME( n -> n.startsWith( "test")) ).size() >=5 );
        
        //assertTrue( _java.findAllIn( _class.of(_methodTest.class), testMatchP ).size() >=5);
        
    }

    public void testMee(){
        _method _m = _method.of("void m();");
        assertFalse( _m.hasBody());
        assertFalse( _m.hasJavadoc());
        assertFalse( _m.hasParameters());
        assertFalse( _m.hasTypeParameters());
        assertFalse( _m.hasAnnos());
        
        _m.getThrows().add( IOException.class );
        System.out.println( _m );
        assertTrue( _m.hasThrow( IOException.class));
        
        
        assertEquals( _modifiers.of(), _m.getModifiers());
        assertEquals("m", _m.getName());
        assertTrue( _m.isTypeRef("void") );
        assertTrue( _m.isTypeRef( Ast.typeRef( "void")) );
        assertEquals("void", _m.getTypeRef().toString());
        assertNull( _m.getBody().ast() );        
    }
    
    @interface ann{
        
    }
    @interface ann2{
        int key();
        char value();
    }
    
    public void testFull(){
        _method _m = _method.of( 
            "/** JAVADOC */",
            "@ann",
            "@ann2(key=7,VALUE='r')",
            "public static final <E extends element> List<String> aMethod( @ann @ann2(key=1,VALUE='v')final int val, String...varArg)",
            "throws A, B { ",
            "   //comment",
            "   System.out.println(1);",
            "   return null;",
            "}" );       
        assertTrue(_m.hasJavadoc() );
        assertTrue( _m.getJavadoc().getContent().contains("JAVADOC"));
        assertTrue(_m.hasAnnos());        
        assertTrue(_m.hasAnno( "ann"));
        assertTrue(_m.hasAnno( ann.class));
        assertTrue(_m.hasAnno( ann2.class));
        assertTrue(_m.getAnnos().getAt( 0 ).is( "@ann") );
        assertTrue(_m.getAnnos().getAt( 1 ).is( "@ann2(key=7,VALUE='r')") );
        
        assertTrue( _m.getModifiers().is("public static final"));
        assertTrue( _m.getModifiers().isPublic());
        assertTrue( _m.getModifiers().isStatic());
        assertTrue( _m.getModifiers().isFinal());
        
        assertNotNull(_m.getTypeParameters());
        assertTrue( _m.hasTypeParameters() );
        assertNotNull(_m.getTypeParameters().is("<E extends element>"));
        assertEquals(1, _m.getTypeParameters().size());
        
        assertTrue( _m.isTypeRef( "List<String>"));
        assertTrue( _m.isTypeRef( Ast.typeRef( "List<String>")) );
        
        assertEquals("aMethod", _m.getName());
        assertTrue( _m.hasParameters() );
        assertTrue( _m.getParameters().is( "@ann @ann2(key=1,VALUE='v')final int val, String...varArg") );
        
        //verify we can rearrange the order of ANNOTATIONS and STILL be equal
        assertTrue( _m.getParameters().is( "@ann2(VALUE='v',key=1) @ann final int val, String... varArg") );
        
       
        assertTrue( _m.hasThrows() );
        assertTrue( _m.getThrows().is("A, B") );
        assertTrue( _m.getThrows().is("B, A") );
        
        assertEquals( _throws.of("A,B").hashCode(), _throws.of("B,A").hashCode());
        assertTrue( _m.getThrows().equals(_throws.of("A, B") ) );
        
        assertTrue(_m.hasBody());
        //verify I can get the BODY, and it is what I think
        assertTrue(_m.getBody().is("{ ",
            "   //comment",
            "   System.out.println(1);",
            "   return null;",
            "}"));
        
        
    }
    
    /* I want to start with a dum method, and then mutate it to _1_build the target method below
        "/** JAVADOC ",
            "@ann",
            "@ann2(key=7,VALUE='r')",
            "public static final <E extends element> List<String> aMethod( @ann @ann2(key=1,VALUE='v')final int val, String...varArg)",
            "throws A, B { ",
            "   //comment",
            "   System.out.println(1);",
            "   return null;",
            "}" );       
    */        
     public void testFromScratch(){
        _method _m = _method.of( "void a();");
        _m.setJavadoc(" JAVADOC");
        _m.addAnnos( ann.class );
        _m.addAnnos( "@ann2(key=7,VALUE='r')");
        _m.setPublic().setStatic().setFinal();
        _m.setTypeRef("List<String>");
        _m.setName("aMethod");
        _m.typeParameters("<E extends element>");
        _m.addParameters("@ann @ann2(key=1, VALUE='v')final int val",
            "String...varArg");
        _m.addThrows("A", "B");
        _m.setBody("//comment", "System.out.println(1);", "return null;" );
        //System.out.println( _m );
        
        _method _test = _method.of( 
            "/** JAVADOC */",
            "@ann",
            "@ann2(key=7,VALUE='r')",
            "public static final <E extends element> List<String> aMethod( @ann @ann2(key=1,VALUE='v')final int val, String...varArg)",
            "throws A, B { ",
            "   // comment",
            "   System.out.println(1);",
            "   return null;",
            "}" );       
        assertEquals( _m, _test );
     }
    
    
    
    //this was just to ensure we MANUALLY put the Javadoc comment on the method after parsing
    public void testASTMethod(){
        String[] body = {"/** JAVADOC */",
            "@ann",
            "@ann2(key=7,VALUE='r')",
            "public static final <E extends element> List<String> aMethod( @ann @ann2(key=1,VALUE='v')final int val, String...varArg)",
            "throws A, B { ",
            "   //comment",
            "   System.out.println(1);",
            "   return null;",
            "}"}; 
        MethodDeclaration md = Ast.method( body );         
        assertTrue(md.getJavadoc().isPresent());        
        //System.out.println( md );        
    }
}
