package org.jdraft;

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
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;
import org.jdraft.text.Tokens;

/**
 *
 * @author Eric
 */
public class _annoExprTest extends TestCase {

    public void testFeatures() {
        _annoExpr _a = _annoExpr.of("@A");

        assertEquals("A", _annoExpr.NAME.get(_a));
        _annoExpr.NAME.set(_a, "Blah");
        assertEquals("Blah", _annoExpr.NAME.get(_a));
        System.out.println(_annoExpr.ENTRY_PAIRS.get(_a));
    }

    public void testMetaFeatureList(){
        _annoExpr _a = _annoExpr.of("@A");
        assertEquals( _annoExpr.FEATURES.list(_a).get(0), _annoExpr.NAME);
        assertEquals( _annoExpr.FEATURES.list(_a).get(1), _annoExpr.ENTRY_PAIRS);
    }

    public void testFeatureFunction() {
        _annoExpr _a = _annoExpr.of("@A");
        _annoExpr _b = _annoExpr.of("@Blah");

        Tokens keyValues = new Tokens();

        //this is an META Ensemble operation
        _annoExpr.FEATURES.forEach(_a, s -> keyValues.put(s.getFeatureId().name, s.get(_a)));

        System.out.println(keyValues);


        _annoExpr.FEATURES.forEach(_a, s -> s instanceof _feature._many,
                s -> keyValues.put(s.getFeatureId().name, s.get(_a)));
    }

    //_biMetaFunction
    public void testMetaEquals(){
        _annoExpr _a = _annoExpr.of("@A");
        _annoExpr _b = _annoExpr.of("@Blah");

        Map<_feature, Boolean> featureEqualsMap = featureEqualsMap(_annoExpr.FEATURES, _a, _b);

        //what about a compareTo
        System.out.println("FEATURES EQUAL " + featureEqualsMap);
    }

    /**
     * an example meta function to apply to all features within two _N instances
     * @param <_N> the underlying type (i.e. _annoExpr.class, _method.class)
     * @param _a instance of _N to compare to _b
     * @param _b instance of _N to compare to _a
     * @return the featureEquals map
     */
    public static <_N extends _tree._node> Map<_feature, Boolean> featureEqualsMap(_N _a, _N _b) {
        try {
            Field f = _a.getClass().getField("FEATURES");
            _feature._features<_N> _mn = (_feature._features<_N>) f.get(null);
            return featureEqualsMap(_mn, _a, _b);
        } catch(NoSuchFieldException | IllegalAccessException nsfe ){
            throw new _jdraftException("Unable to resolve META field");
        }
    }

    /**
     * an example meta function to apply to all features within two _N instances
     * @param <_N> the underlying type (i.e. _annoExpr.class, _method.class)
     * @param _features the meta implementation for the type (i.e. _annoExpr.META, _method.META)
     * @param _a instance of _N to compare to _b
     * @param _b instance of _N to compare to _a
     * @return the featureEquals map
     */
    public static <_N extends _tree._node> Map<_feature, Boolean> featureEqualsMap(_feature._features<_N> _features, _N _a, _N _b){
        Map<_feature, Boolean> featureEqualsMap = new HashMap<>();
        _features.forEach(_a, f-> {
            //I need to check between
            if( f instanceof _feature._many){
                _feature._many _fm = (_feature._many)f;
                List left = (List)_fm.get(_a);
                List right = (List)_fm.get(_b);
                if( left.containsAll(right) && left.size() == right.size() ){
                    featureEqualsMap.put(f, true);
                } else{
                    featureEqualsMap.put(f, false);
                }
            } else {
                Object a = f.get(_a);
                Object b = f.get(_b);
                featureEqualsMap.put(f, Objects.equals(a, b));
            }
        } );
        return featureEqualsMap;
    }

    static class _featurePath{

        List<_feature> pathList = new ArrayList<>();

        public _featurePath(){ }

        public _featurePath( _feature _f ){
            pathList.add(_f);
        }

        public _featurePath add( _feature _f ){
            pathList.add(_f);
            return this;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<pathList.size(); i++){
                if( i > 0 ){
                    sb.append(".");
                }
                sb.append("<[");
                sb.append(pathList.get(i).getTargetClass().getSimpleName());
                sb.append("]");
                sb.append(pathList.get(i).getFeatureId());
                sb.append(">");
            }
            return sb.toString();
        }
    }



    /**
     * Comparison in a meta-function
     *
     */
    public void testMetaFunctionEntryPair() {
        _entryPair _epA = _annoExpr.of("@A(1)").getEntryPair(0);
        _entryPair _epB = _annoExpr.of("@A(1)").getEntryPair(0);
        Map<_feature, Boolean> m = featureEqualsMap(_epA, _epB);

        //verify all true
        assertTrue(m.values().stream().allMatch(b -> b.booleanValue()));

        m = featureEqualsMap( _entryPair.of("1"), _entryPair.of("1"));
        assertTrue(m.values().stream().allMatch(b -> b.booleanValue()));

        m = featureEqualsMap(_entryPair.of("1"), _entryPair.of("value=1"));
        assertTrue(m.values().stream().allMatch(b -> b.booleanValue()));


        //verify the value is not the same
        m = featureEqualsMap(_entryPair.of("1"), _entryPair.of("2"));
        assertFalse(m.values().stream().allMatch(b -> b.booleanValue()));
        assertTrue(m.get(_entryPair.VALUE) == false);

        m = featureEqualsMap(_entryPair.of("A=1"), _entryPair.of("B=1"));
        assertFalse(m.values().stream().allMatch(b -> b.booleanValue()));
        assertTrue(m.get(_entryPair.NAME) == false);
        assertTrue(m.get(_entryPair.VALUE) == true);
    }

    public void testMetaFunctionAnnoExpr(){
        //try a meta function
        Map<_feature, Boolean>m2 = featureEqualsMap(_annoExpr.of("@A(1)"), _annoExpr.of("@A(1)"));
        assertTrue( m2.values().stream().allMatch(b-> b.booleanValue()));

        m2 = featureEqualsMap(_annoExpr.of("@A(1)"), _annoExpr.of("@A(2)"));
        assertEquals( m2.get(_annoExpr.ENTRY_PAIRS), Boolean.FALSE );
    }

    public enum E{
        A,B,C,D;
    }

    public @interface A{
        int value() default 0;
    }

    public @interface AI{
        A a() default @A(1); //anonther annotation
        Class c() default String.class; //a class
        String s() default "S"; //a string
        E e() default E.A;
        //primitives
        boolean b() default true;
        char ch() default 'c';
        int i() default 0; // int
        float f() default 0.0f; //float
        double d() default 0.0d; //double
        long l() default 1L;

        //arrays
        A[] as() default { @A(1),@A(2)};
        Class[] cs() default {};
        String[] ss() default {};
        E[] es()  default {};
        boolean[] bs()  default {};
        char[] chs()  default {};
        int[] is()  default {};
        float[] fs()  default {};
        double[] ds() default {};
        long[] ls() default {};
    }

    public void testRemovePair(){
        _annoExpr _ae = _annoExpr.of("@A(1)");
        assertTrue( _ae.isSingleMember() );
        _ae.removeEntryPair(_entryPair.of("value", 1));
        assertTrue( _ae.isMarker() );

        System.out.println( _ae );

        _ae = _annoExpr.of("@A(k=1)");
        _ae.removeEntryPairs(p-> p.isNamed("k"));
        assertEquals(0, _ae.listEntryPairs().size() );
        assertTrue(_ae.isMarker() );
    }

    @AI(is={1,2,3,4,5})
    public void testAnnoUse(){
        _annoExpr.of("A").addEntryPair("i", 1);
        _annoExpr.of("A").addEntryPair("i", 1,2,3);

        _annoExpr.of("A").addEntryPair("i", 'a');
        _annoExpr.of("A").addEntryPair("i", 'a', 'b');

        _annoExpr.of("A").addEntryPair("i", true);
        _annoExpr.of("A").addEntryPair("i", true, false);
        _annoExpr.of("A").addEntryPair("i", new boolean[]{true});
        System.out.println( _annoExpr.of("A").addEntryPair("i", String.class, Map.class ) );

        _annoExpr.of("A").addEntryPair("i", _annoExpr.of("@A(1)"), _annoExpr.of("@A(2)") );

        _annoExpr _ap = _annoExpr.of("A").addEntryPair("i", 1L);


        System.out.println(_annoExpr.of("A").addEntryPair("i", 1L, 2L));


        System.out.println( _annoExpr.of("A").addEntryPair(_entryPair.of("i", 1,2,3,4)) );

        _annoExpr.of("A").addEntryPair("f", 1.0f);
        _annoExpr.of("A").addEntryPair("f", 1.0f, 2.0f);
        _annoExpr.of("A").addEntryPair(_entryPair.of("fs", 1.0f));
        _annoExpr.of("A").addEntryPair(_entryPair.of("fs", 1.0f,2.0f,3.0f,4.0f));

        @AI
        class C{ }

        _annotation _a = _annotation.of(AI.class);
        System.out.println( _a );
        _class _c = _class.of(C.class);

    }
    public void testMemberValuePair(){
        MemberValuePair mvp = new MemberValuePair();
        //System.out.println( mvp.getNameAsString() );
        //System.out.println( mvp.getValue() );

        _entryPair _mv = _entryPair.of("value");
        assertTrue( _mv.isValueOnly() );
        assertEquals("value", _mv.getName());
        assertEquals("value", _mv.getValue().toString());
        //System.out.println( _mv );

        assertTrue( _mv.isNamed("value") );
        assertTrue( _mv.isValue("value") );

        //key & value
        _mv = _entryPair.of("value=3");
        assertTrue( _mv.isNamed("value") );
        assertTrue( _mv.isValue("3") );
        assertTrue( _mv.isValue(3) );

        _mv = _entryPair.of("value='c'");
        assertTrue( _mv.isValue("'c'") );
        assertTrue( _mv.isValue('c') );
    }

    public void testHasMemberValuePair(){

        _annoExpr _a = _annoExpr.of("@A(k=1,v=2)");

        assertTrue( _a.hasEntryPair("k",1));
        assertTrue( _a.hasEntryPair("v",2));

        assertTrue( _a.hasEntryPair( (m, v)-> m.equals("k") && _intExpr.of(1).equals(v)));
        //System.out.println( "HASH1"+_a.listMemberValues().get(0).hashCode() );
        //System.out.println( "HASH1"+_a.listMemberValues().get(1).hashCode() );
        //assertTrue( _a.isMemberValues("k=1,v=2"));
        assertTrue( _a.isEntryPairs("v=2,k=1"));
        assertEquals(2, _a.listEntryPairs().size());

        //assertTrue(_a.hasKeys("k", "v"));
        assertTrue( _a.hasEntryPair("k", 1));
        assertTrue( _a.hasEntryPair("v", 2));
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
                .addEntryPair("i", 100);

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
        ExpressionStmt es = (ExpressionStmt) Stmt.expressionStmt("N n = new @Test N();");
        VariableDeclarationExpr vd = es.getExpression().asVariableDeclarationExpr();
        VariableDeclarator var = vd.getVariable(0);
        ObjectCreationExpr init = (ObjectCreationExpr)var.getInitializer().get();
        
        //assertFalse( init.getType().getAnnotations().isEmpty() );
    }
    
    public void testTypeAnn(){
        Statement st = Stmt.of("@Test List<@Test String> emails = new @Test ArrayList();");
        AtomicInteger ai = new AtomicInteger(0);
        st.walk(Ast.Classes.ANNOTATION_EXPR, a-> ai.incrementAndGet() );
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
        Statement st = Stmt.of( "Integer i = new @Test Integer(100);");
        System.out.println( st );
        assertEquals(1, $annoRef.of(Test.class).countIn(st));//verify we can find the anno
    }
    
    public void testCastAnno(){
        Statement st = Stmt.of( () -> {Integer i = (@Test Integer)100;} );
        assertEquals(1, $annoRef.of(Test.class).countIn(st));//verify we can find the anno
    }
    
    public void testInstanceOfAnno(){        
        Integer i = 0;        
        Statement st = Stmt.of( () ->{boolean b = i instanceof @Test Number;});
        assertEquals(1, $annoRef.of(Test.class).countIn(st));//verify we can find the anno
    }
    
    public void testVar(){
        Statement st = Stmt.of( () ->{ @Test boolean b = false;});
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
        Statement st = Stmt.of("NestedClass nc = new _annoTest. @Test NestedClass();");
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
        Statement st = Stmt.of("new  @Test  MyObject();");
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
        assertTrue( _a.hasEntryPair("value", Expr.of(1)) );
        assertTrue( _a.hasEntryPair("value", 1) );
        assertTrue( _b.hasEntryPair("x", Expr.of(1)) );
        assertTrue( _b.hasEntryPair("x", 1) );
        
        assertTrue( _b.hasEntryPair("x=1") );
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

        assertTrue( _a.hasEntryPair("value", 1) );
        assertTrue( _a.hasEntryPair("value", 1));

        Expr.arrayInitializerExpr(new int[]{1,2,3});
        _annoExpr _b = _annoExpr.of("B(k=1,v={'a','b'})");
        assertTrue( _b.hasEntryPair("k", 1) );
        assertTrue( _b.hasEntryPair("v", new char[]{'a', 'b'}) );
        assertTrue( _b.hasEntryPair("v", Expr.arrayInitializerExpr('a', 'b')) );
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
                .addAnnoExprs(_a);
        _a = _m.getAnnoExpr(0);
        _a.hasEntryPair("Accept", "application/json; charset=utf-8");
        _a.hasEntryPair("User-Agent", "Square Cash");

        Expression e = _a.getEntryValue("value");
        /*
        Map<String,Expression> keyValues = _a.getPairsMap();
        Expression val = keyValues.get("value");
        assertNotNull(val);
        assertEquals( e, val);

        System.out.println( _m);
         */
    }

    public void testAnnAst(){
        AnnotationExpr ae1 = Ast.annotationExpr( "@ann(k1=1,k2=2)");
        AnnotationExpr ae2 = Ast.annotationExpr( "@ann(k2=2,k1=1)");
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
        FieldDeclaration fd = Ast.fieldDeclaration( "@ann int f;");
        //go from a MarkerAnnotation
        _annoExpr _a = new _annoExpr(fd.getAnnotation( 0 ));

        //to a Single Value Annotation
        _a.setEntryPairValue( 0, 100 );
        assertEquals( _a.getEntryValue( 0 ), Expr.of(100) );

        //to a Normal Annotation
        _a.addEntryPair( "k", 200 );
        assertEquals( _a.getEntryValue( 0 ), Expr.of(200) );
        assertEquals( _a.getEntryValue( "k" ), Expr.of(200) );

        _a.addEntryPair( "v", 300 );
        assertEquals( _a.getEntryValue( 1 ), Expr.of(300) );
        assertEquals( _a.getEntryValue( "v" ), Expr.of(300) );

        assertEquals( 2, _a.listEntryPairs().size());

        _a.removeEntryPair("a");
        assertEquals( 2, _a.listEntryPairs().size());
        _a.removeEntryPair("v");
        assertEquals( 1, _a.listEntryPairs().size());
        _a.removeEntryPair(0);
        assertEquals( 0, _a.listEntryPairs().size());

        //_a.removeAttrs();

        assertFalse( _a.hasValues() );
    }

    public void testChildParent(){
        //the underlying field has to change the implementation from
        FieldDeclaration fd = Ast.fieldDeclaration( "@a(1) public int i=100;");
        _annoExpr _a = new _annoExpr(fd.getAnnotation( 0 ));
        _a.addEntryPair( "Key", 1000 );

        assertTrue( _a.is("@a(Key=1000)"));

        //parent child
        fd = Ast.fieldDeclaration( "@a(1) public int i=100;");

        AnnotationExpr ae = fd.getAnnotation(0).clone();
        System.out.println( ae.getParentNode().isPresent() );
        _annoExpr _aNoParent = new _annoExpr(ae);
        _aNoParent.addEntryPair( "Key", 9999 );
        assertTrue( _aNoParent.is("@a(Key=9999)") );
        //System.out.println( _aNoParent );
    }
}
