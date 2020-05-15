package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import org.jdraft.pattern.$annoRef;
import org.jdraft.pattern.$ex;
import org.jdraft.pattern.$stmt;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _annoExprTest extends TestCase {

    public void testMemberValuePair(){
        MemberValuePair mvp = new MemberValuePair();
        //System.out.println( mvp.getNameAsString() );
        //System.out.println( mvp.getValue() );

        _annoExpr._memberValue _mv = _annoExpr._memberValue.of("value");
        assertTrue( _mv.isValueOnly() );
        assertEquals("value", _mv.getName());
        assertEquals("value", _mv.getValue().toString());
        //System.out.println( _mv );

        assertTrue( _mv.isNamed("value") );
        assertTrue( _mv.isValue("value") );

        //key & value
        _mv = _annoExpr._memberValue.of("value=3");
        assertTrue( _mv.isNamed("value") );
        assertTrue( _mv.isValue("3") );
        assertTrue( _mv.isValue(3) );

        _mv = _annoExpr._memberValue.of("value='c'");
        assertTrue( _mv.isValue("'c'") );
        assertTrue( _mv.isValue('c') );
    }

    public void testHasMemberValuePair(){

        _annoExpr _a = _annoExpr.of("@A(k=1,v=2)");

        assertTrue( _a.hasMemberValue("k",1));
        assertTrue( _a.hasMemberValue("v",2));

        assertTrue( _a.hasMemberValue( (m,v)-> m.equals("k") && _intExpr.of(1).equals(v)));
        //System.out.println( "HASH1"+_a.listMemberValues().get(0).hashCode() );
        //System.out.println( "HASH1"+_a.listMemberValues().get(1).hashCode() );
        //assertTrue( _a.isMemberValues("k=1,v=2"));
        assertTrue( _a.isMemberValues("v=2,k=1"));
        assertEquals(2, _a.listMemberValues().size());

        assertTrue(_a.hasKeys("k", "v"));
        assertTrue( _a.hasValue(1));
        assertTrue( _a.hasValue(2));
    }

    /*
    public void testParts(){
        _anno _a = _anno.of("A");
        System.out.println( _a.partsMap() );

        _a.addMemberValue("A", 'c');
        System.out.println( _a.partsMap() );

        _a.addMemberValue("B", 4);
        System.out.println( _a.partsMap() );

    }
     */

    public void testAnn(){
        _annoExpr _a = _annoExpr.of()
                .setName("n")
                .addMemberValue("i", 100);

        System.out.println(_a);
    }

    @interface Headers{
        String accept();
        String userAgent();
    }

    public void testHeaders() {
        Headers hs = new Headers() {
            public Class annotationType() {
                return Headers.class;
            }

            public String accept() {
                return "application/json; charset=utf-8";
            }

            public String userAgent() {
                return "Square Cash";
            }
        };
        assertTrue(hs.annotationType().equals(Headers.class));
        assertTrue(hs.accept().equals("application/json; charset=utf-8"));
        assertTrue(hs.userAgent().equals("Square Cash"));
    }

    /*
    public void test_annoInstance(){
        _anno.of( new Headers(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String accept() {
                return null;
            }

            @Override
            public String userAgent() {
                return null;
            }
        });
    }
     */

    @interface i {
        int value();
    }
    
    /**
     * Verify that the anno:
     * _anno _a = _anno.of("a(1)");
     * //is equal to 
     * _anno _b = _anno.of("a(value=1)");    
     * 
     * the first anno:
     * @a(1)
     * 
     * has a single attr that has an implied attr name "value"
     */
    public void testEqualsExplicitVsImpliedValueAttr(){
        
        @i(1)
        class XXXX{}
        
        @i(value=1)
        class YYYY{}
        
        i ii = XXXX.class.getDeclaredAnnotation(i.class);
        i i2 = YYYY.class.getDeclaredAnnotation(i.class);
        
        //the runtime values of these annotations is equal
        assertEquals( ii, i2);
        
        _annoExpr _a = _annoExpr.of("a(1)");
        _annoExpr _b = _annoExpr.of("a(value=1)");
        
        assertEquals(_a, _b);        
        assertEquals(_a.hashCode(), _b.hashCode());
    }
    
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    public @interface Test {
        
    }
    
    public void testMissingTypeUseAnnotationOnObjectCreationExpr(){
        ExpressionStmt es = (ExpressionStmt)StaticJavaParser.parseStatement("N n = new @Test N();");        
        VariableDeclarationExpr vd = es.getExpression().asVariableDeclarationExpr();
        VariableDeclarator var = vd.getVariable(0);
        ObjectCreationExpr init = (ObjectCreationExpr)var.getInitializer().get();
        
        //assertFalse( init.getType().getAnnotations().isEmpty() );
    }
    
    public void testTypeAnn(){
        Statement st = Stmts.of("@Test List<@Test String> emails = new @Test ArrayList();");
        AtomicInteger ai = new AtomicInteger(0);
        st.walk(Ast.ANNOTATION_EXPR, a-> ai.incrementAndGet() );
        System.out.println( st );
        //lost the new $Test Array...
        assertEquals( 3, ai.get());
        
        Type t = st.asExpressionStmt().getExpression().asVariableDeclarationExpr().getVariable(0).getType();
        System.out.println( t );
        //System.out.println( "ANNS "+ t.getAnnotations() );
        //assertEquals( _anno.of(Test.class).ast(), t.getAnnotation(0));
    }
    
    /** Fix not merged yet */
    public void testObjectConstructionAnno(){
        //Statement st = Stmt.of( () -> {Integer i = new @Test Integer(100);} );
        Statement st = Stmts.of( "Integer i = new @Test Integer(100);");
        System.out.println( st );
        assertEquals(1, $annoRef.of(Test.class).countIn(st));//verify we can find the anno
    }
    
    public void testCastAnno(){
        Statement st = Stmts.of( () -> {Integer i = (@Test Integer)100;} );
        assertEquals(1, $annoRef.of(Test.class).countIn(st));//verify we can find the anno
    }
    
    public void testInstanceOfAnno(){        
        Integer i = 0;        
        Statement st = Stmts.of( () ->{boolean b = i instanceof @Test Number;});
        assertEquals(1, $annoRef.of(Test.class).countIn(st));//verify we can find the anno
    }
    
    public void testVar(){
        Statement st = Stmts.of( () ->{ @Test boolean b = false;});
        assertEquals(1, $annoRef.of(Test.class).countIn(st));//verify we can find the anno
    }
    
    public void testThrowAnno(){
        class TTTT{
            void m() throws @Test IOException{}
        }
        _class _c = _class.of( TTTT.class );
        
        assertEquals(1, $annoRef.of(Test.class).countIn( _c.getMethod("m").getThrows().getAt(0) ));
    }
    
    public class NestedClass{}
    
    public void testNestedClassAnno(){
        NestedClass nc = new _annoExprTest. @Test NestedClass();
        Statement st = Stmts.of("NestedClass nc = new _annoTest. @Test NestedClass();");
        System.out.println( st );
        assertEquals(1, $annoRef.of(Test.class).countIn( st ));
    }
    
    public void testExtendsAnno(){        
        class B{}        
        class C extends @Test B{ }
        
        assertEquals(1, $annoRef.of(Test.class).countIn( C.class));
        
    }
    
    interface III{}    
    public void testImplementsAnno(){
        class P implements @Test III{}
        
        assertEquals(1, $annoRef.of(Test.class).countIn( P.class));
    }
    
    public void testIntersectionType(){
        class DDDDL{
            public <E extends @Test Serializable & @Test III> void foo() {  }        
        }
        _class _c = _class.of(DDDDL.class);
        assertEquals( 2, $annoRef.of(Test.class).countIn( _c.getMethod("foo") ));
        System.out.println( _c );
    }
    
    public static class MyObject<T>{}
    
    
    public void testT(){
        Statement st = Stmts.of("new  @Test  MyObject();");
        assertEquals( 1, $annoRef.of(Test.class).countIn( st ));
        System.out.println( st );
    }
    
    public void testTypeAnnotation(){
        
        //in an implements (YES)
        class C implements @Test Serializable{
            //in type argument (YES)
            @Test List<@Test String> emails = new @Test ArrayList();
            
            void m() //on throws 
                    throws @Test RuntimeException {
                //in cast (YES)
                String t = (@Test String)emails.get(0);
                
                //in instanceof 
                boolean b = t instanceof @Test String;
                
                //in initializer (NO)
                N n = new @Test N();
                
                
            }
            class N{
                
            }            
        }
        _class _c = _class.of(C.class);
        
        System.out.println( _c );
       
        //this SHOULD be 8 if we can fix the issue in JavaParser with ObjectCreationExpr
        assertEquals( 8, $annoRef.of("@Test").countIn(C.class));
        
        $annoRef $aa = $annoRef.of(Test.class);
            //.constraint( a->a.ast().getParentNode().isPresent() 
            //    && a.ast().getParentNode().get().findFirst(Statement.class).isPresent());
        
        $stmt $s = $stmt.of().$and(
            (s)-> ((Statement)s.ast()).findFirst(
                AnnotationExpr.class, (AnnotationExpr a)-> $aa.matches(a) ).isPresent() 
                && !(s instanceof BlockStmt) );
        System.out.println( $s.listIn(C.class) );
        
        $ex $ex = org.jdraft.pattern.$ex.any().$and(o-> !(o.ast() instanceof AnnotationExpr)
            && o.ast().findFirst(AnnotationExpr.class).isPresent());
        
        System.out.println( $ex.listIn(C.class) );
        
        org.jdraft.pattern.$ex $e = org.jdraft.pattern.$ex.newEx().$and(o-> o.ast().findFirst(AnnotationExpr.class).isPresent());
        
        System.out.println( $e.listIn(C.class) );
        
        /*
        System.out.println( "Impl Anno "+ _c.listImplements().get(0).getAnnotation(0));
        
        
        System.out.println( $expr.objectCreation("new N()").listIn(C.class) );
        
        CastExpr cast = $expr.castAny().firstIn(C.class);
        
        System.out.println("CAST" + cast );
        System.out.println( cast.getType() );
        System.out.println( cast.getType().getAnnotation(0) );
        
        ObjectCreationExpr oce = $expr.objectCreation("new N()").listIn(C.class).get(0);
        //System.out.println( "***** ANNO " + oce.getType().getAnnotation(0) );
        */
        
        
        /*
        Type t = _c.getField("emails").getType().getElementType();
        ClassOrInterfaceType coit = (ClassOrInterfaceType)t;
        NodeList<Type> typeArgs = coit.getTypeArguments().get();
        
        System.out.println("TypeArgs" +  typeArgs );
        System.out.println( typeArgs.get(0).getAnnotation(0) );
        System.out.println("ANNOS" +  coit.getAnnotations() );
        
        
        System.out.println( t+ " "+ t.getClass());
        */
        
    }
    
    public void testAnnoHasAttr(){
        _annoExpr _a = _annoExpr.of("a(1)");
        _annoExpr _b = _annoExpr.of("a(x=1)");
        assertTrue( _a.hasValue(Exprs.of(1)) );
        assertTrue( _a.hasValue(1) );
        assertTrue( _b.hasValue(Exprs.of(1)) );
        assertTrue( _b.hasValue(1) );
        
        assertTrue( _b.hasMemberValue("x=1") );
        //assertTrue( _b.hasAttr("x", 1) );
        
        
    }
    
    public void testPattern(){
        String pattern = "\\$?\\d+\\$";
        String className = "draft.java._classTest$1$Hoverboard";

        String s = className.replaceAll(pattern, "." );

        System.out.println(s);
    }

    public @interface HeaderList{
        Header[] value();
    }
    public @interface Header{
        String name();
        String value();
    }


    public void testIsValue(){
        _annoExpr _a = _annoExpr.of("A(1)");

        assertTrue( _a.isValue(1) );
        assertTrue( _a.isValue("value", 1));

        Exprs.arrayInitializerEx(new int[]{1,2,3});
        _annoExpr _b = _annoExpr.of("B(k=1,v={'a','b'})");
        assertTrue( _b.isValue("k", 1) );
        assertTrue( _b.isValue("v", new char[]{'a', 'b'}) );
        assertTrue( _b.isValue("v", Exprs.charArray('a', 'b')) );
    }

    public void test23Draft(){
        _annoExpr _a = _annoExpr.of(new Object(){
            @HeaderList({
                    @Header(name="Accept", value="application/json; charset=utf-8"),
                    @Header(name="User-Agent", value="Square Cash"),
            })
            class C{ }
        });

        _method _m = _method.of("public abstract LogReceipt recordEvent(LogRecord logRecord);")
                .addAnnoRefs(_a);
        _a = _m.getAnnoRef(0);
        _a.isValue("Accept", "application/json; charset=utf-8");
        _a.isValue("User-Agent", "Square Cash");

        Expression e = _a.getValue("value");
        Map<String,Expression> keyValues = _a.getKeyValuesMap();
        Expression val = keyValues.get("value");
        assertNotNull(val);
        assertEquals( e, val);

        System.out.println( _m);
    }

    public void testAnnAst(){
        AnnotationExpr ae1 = Ast.anno( "@ann(k1=1,k2=2)");
        AnnotationExpr ae2 = Ast.anno( "@ann(k2=2,k1=1)");
        //THIS SHOULD PRODUCE THE SAME HASHCODE (it does)

        assertEquals( ae1.hashCode(), ae2.hashCode());
        //ae1 SHOULD equal ae2 (i.e. the contents MEAN the same thing,

        // the order of the values shouldnt matter, but it does in the AST variant
        //BOTH for typesEqual and hashCode
        assertTrue(!ae1.equals(ae2));

        //Note the hashcodes ARE equal
        //assertTrue(ae1.hashCode() != ae2.hashCode());

        //IF we wrap the AnnotationExprs as _anno s
        _annoExpr _a1 = _annoExpr.of( ae1 );
        _annoExpr _a2 = _annoExpr.of( ae2 );

        //they ARE equal
        assertEquals( _a1, _a2);

        //AND the hashCodes are equal
        assertEquals( _a1.hashCode(), _a2.hashCode());
    }

    public void test_annEqualsHashCode(){
        //here we compensate by
        _annoExpr a1 = _annoExpr.of( "@ann(k1=1,k2=2)");
        _annoExpr a2 = _annoExpr.of( "@ann(k2=2,k1=1)");

        //THIS SHOULD PRODUCE THE SAME HASHCODE (it does)
        assertEquals( a1.hashCode(), a2.hashCode() );

        //ae1 SHOULD equal ae2 (i.e. the contents MEAN the same thing,
        // the order of the values shouldnt matter
        assertEquals( a1, a2);
    }

    public void testExcalateImplementation(){
        FieldDeclaration fd = Ast.field( "@ann int f;");
        //go from a MarkerAnnotation
        _annoExpr _a = new _annoExpr(fd.getAnnotation( 0 ));

        //to a Single Value Annotation
        _a.setValue( 0, 100 );
        assertEquals( _a.getValue( 0 ), Exprs.of(100) );

        //to a Normal Annotation
        _a.addMemberValue( "k", 200 );
        assertEquals( _a.getValue( 0 ), Exprs.of(200) );
        assertEquals( _a.getValue( "k" ), Exprs.of(200) );

        _a.addMemberValue( "v", 300 );
        assertEquals( _a.getValue( 1 ), Exprs.of(300) );
        assertEquals( _a.getValue( "v" ), Exprs.of(300) );

        assertEquals( 2, _a.listKeys().size());

        _a.removeAttr("a");
        assertEquals( 2, _a.listKeys().size());
        _a.removeAttr("v");
        assertEquals( 1, _a.listKeys().size());
        _a.removeAttr(0);
        assertEquals( 0, _a.listKeys().size());

        //_a.removeAttrs();

        assertFalse( _a.hasValues() );
    }

    public void testChildParent(){
        //the underlying field has to change the implementation from
        FieldDeclaration fd = Ast.field( "@a(1) public int i=100;");
        _annoExpr _a = new _annoExpr(fd.getAnnotation( 0 ));
        _a.addMemberValue( "Key", 1000 );

        assertTrue( _a.is("@a(Key=1000)"));

        //parent child
        fd = Ast.field( "@a(1) public int i=100;");

        AnnotationExpr ae = fd.getAnnotation(0).clone();
        System.out.println( ae.getParentNode().isPresent() );
        _annoExpr _aNoParent = new _annoExpr(ae);
        _aNoParent.addMemberValue( "Key", 9999 );
        assertTrue( _aNoParent.is("@a(Key=9999)") );
        //System.out.println( _aNoParent );
    }
}
