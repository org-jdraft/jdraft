package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import org.jdraft.proto.$anno;
import org.jdraft.proto.$expr;
import org.jdraft.proto.$stmt;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _annoTest extends TestCase {

    
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
        
        _anno _a = _anno.of("a(1)");     
        _anno _b = _anno.of("a(value=1)");
        
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
        Statement st = Stmt.of("@Test List<@Test String> emails = new @Test ArrayList();");
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
    
    /** Fix not merged yet
    public void testObjectConstructionAnno(){
        //Statement st = Stmt.of( () -> {Integer i = new @Test Integer(100);} );
        Statement st = Stmt.of( "Integer i = new @Test Integer(100);");
        System.out.println( st );
        assertEquals(1, $anno.of(Test.class).count(st));//verify we can find the anno        
    }
    */ 
    
    public void testCastAnno(){
        Statement st = Stmt.of( () -> {Integer i = (@Test Integer)100;} );
        assertEquals(1, $anno.of(Test.class).count(st));//verify we can find the anno        
    }
    
    public void testInstanceOfAnno(){        
        Integer i = 0;        
        Statement st = Stmt.of( () ->{boolean b = i instanceof @Test Number;});
        assertEquals(1, $anno.of(Test.class).count(st));//verify we can find the anno                
    }
    
    public void testVar(){
        Statement st = Stmt.of( () ->{ @Test boolean b = false;});
        assertEquals(1, $anno.of(Test.class).count(st));//verify we can find the anno          
    }
    
    public void testThrowAnno(){
        class TTTT{
            void m() throws @Test IOException{}
        }
        _class _c = _class.of( TTTT.class );
        
        assertEquals(1, $anno.of(Test.class).count( _c.getMethod("m").getThrows().get(0) ));        
    }
    
    public class NestedClass{}
    
    public void testNestedClassAnno(){
        NestedClass nc = new _annoTest. @Test NestedClass();
        Statement st = Stmt.of("NestedClass nc = new _annoTest. @Test NestedClass();");
        System.out.println( st );
        assertEquals(1, $anno.of(Test.class).count( st ));        
    }
    
    public void testExtendsAnno(){        
        class B{}        
        class C extends @Test B{ }
        
        assertEquals(1, $anno.of(Test.class).count( C.class));
        
    }
    
    interface III{}    
    public void testImplementsAnno(){
        class P implements @Test III{}
        
        assertEquals(1, $anno.of(Test.class).count( P.class));
    }
    
    public void testIntersectionType(){
        class DDDDL{
            public <E extends @Test Serializable & @Test III> void foo() {  }        
        }
        _class _c = _class.of(DDDDL.class);
        assertEquals( 2, $anno.of(Test.class).count( _c.getMethod("foo") ));
        System.out.println( _c );
    }
    
    public static class MyObject<T>{}
    
    
    public void testT(){
        Statement st = Stmt.of("new  @Test  MyObject();");
        assertEquals( 1, $anno.of(Test.class).count( st ));
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
        assertEquals( 8, $anno.of("@Test").count(C.class));
        
        $anno $aa = $anno.of(Test.class);
            //.constraint( a->a.ast().getParentNode().isPresent() 
            //    && a.ast().getParentNode().get().findFirst(Statement.class).isPresent());
        
        $stmt $s = $stmt.of().and(
            (s)-> ((Statement)s).findFirst(
                AnnotationExpr.class, (AnnotationExpr a)-> $aa.matches(a) ).isPresent() 
                && !(s instanceof BlockStmt) );
        System.out.println( $s.listIn(C.class) );
        
        $expr $ex = $expr.of().and(o-> !(o instanceof AnnotationExpr)
            && o.findFirst(AnnotationExpr.class).isPresent());
        
        System.out.println( $ex.listIn(C.class) );
        
        $expr $e = $expr.objectCreation().and(o-> o.findFirst(AnnotationExpr.class).isPresent());
        
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
        _anno _a = _anno.of("a(1)");
        _anno _b = _anno.of("a(x=1)");
        assertTrue( _a.hasValue(Expr.of(1)) );
        assertTrue( _a.hasValue(1) );
        assertTrue( _b.hasValue(Expr.of(1)) );
        assertTrue( _b.hasValue(1) );
        
        assertTrue( _b.hasAttr("x=1") );
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

    public void test23Draft(){
        _anno _a = _anno.of(new Object(){
            @HeaderList({
                    @Header(name="Accept", value="application/json; charset=utf-8"),
                    @Header(name="User-Agent", value="Square Cash"),
            })
            class C{ }
        });

        _method _m = _method.of("public abstract LogReceipt recordEvent(LogRecord logRecord);")
                .annotate(_a);

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
        _anno _a1 = _anno.of( ae1 );
        _anno _a2 = _anno.of( ae2 );

        //they ARE equal
        assertEquals( _a1, _a2);

        //AND the hashCodes are equal
        assertEquals( _a1.hashCode(), _a2.hashCode());
    }

    public void test_annEqualsHashCode(){
        //here we compensate by
        _anno a1 = _anno.of( "@ann(k1=1,k2=2)");
        _anno a2 = _anno.of( "@ann(k2=2,k1=1)");

        //THIS SHOULD PRODUCE THE SAME HASHCODE (it does)
        assertEquals( a1.hashCode(), a2.hashCode() );

        //ae1 SHOULD equal ae2 (i.e. the contents MEAN the same thing,
        // the order of the values shouldnt matter
        assertEquals( a1, a2);
    }

    public void testExcalateImplementation(){
        FieldDeclaration fd = Ast.field( "@ann int f;");
        //go from a MarkerAnnotation
        _anno _a = new _anno(fd.getAnnotation( 0 ));

        //to a Single Value Annotation
        _a.setValue( 0, 100 );
        assertEquals( _a.getValue( 0 ), Expr.of(100) );

        //to a Normal Annotation
        _a.addAttr( "k", 200 );
        assertEquals( _a.getValue( 0 ), Expr.of(200) );
        assertEquals( _a.getValue( "k" ), Expr.of(200) );

        _a.addAttr( "v", 300 );
        assertEquals( _a.getValue( 1 ), Expr.of(300) );
        assertEquals( _a.getValue( "v" ), Expr.of(300) );

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
        _anno _a = new _anno(fd.getAnnotation( 0 ));
        _a.addAttr( "Key", 1000 );

        assertTrue( _a.is("@a(Key=1000)"));

        //parent child
        fd = Ast.field( "@a(1) public int i=100;");

        AnnotationExpr ae = fd.getAnnotation(0).clone();
        System.out.println( ae.getParentNode().isPresent() );
        _anno _aNoParent = new _anno(ae);
        _aNoParent.addAttr( "Key", 9999 );
        assertTrue( _aNoParent.is("@a(Key=9999)") );
        //System.out.println( _aNoParent );
    }


}
