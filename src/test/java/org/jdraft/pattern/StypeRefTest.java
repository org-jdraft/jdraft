package org.jdraft.pattern;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.Type;
import org.jdraft.*;
import org.jdraft.Tree;
import junit.framework.TestCase;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class StypeRefTest extends TestCase {


    public void testThread(){
        class G{
            public void testM(){
                Thread.currentThread().getStackTrace();
            }
        }
        _class _c = _class.of(G.class);
        assertEquals(1, $.methodCall("Thread.currentThread()").countIn(_c));
        //assertEquals(1, $.methodCall("Thread.currentThread()").count(_c));
    }

    public void testUnionType(){
        class C{
            void m (){
                try{
                    if( 1==1 ) {
                        throw new IOException();
                    }
                    throw new URISyntaxException("HELLO", "HI");
                }catch(IOException | URISyntaxException e){
                    //do nothing
                }
            }
        }
        //$catch.of().forEachIn(C.class, c -> Ast.describe(c) );

        //verify that we check WITHIN a Union Type
        $typeRef $t = $typeRef.of(URISyntaxException.class);
        assertEquals(2, $t.countIn(C.class));

        $typeRef $i = $typeRef.of(IOException.class);
        assertEquals(2, $t.countIn(C.class));

        /*
        //again we can query to the harts content
        System.out.println("*** Thread Usages ");

        $.or($.typeRef(Thread.class), $.methodCall("Thread.$methodCall$()")).forEachIn(_jdraftAllSrcTests, t-> System.out.println( t));
                //p-> p.ast().stream(Node.TreeTraversal.PARENTS).filter( n -> n instanceof BodyDeclaration).findFirst().ifPresent(n -> System.out.println(n)) );
        */
    }
    public void testTT(){
        $typeRef $tr = $typeRef.of("A<B,C>");
        assertTrue( $tr.matches("A<B,C>"));
        assertTrue( $tr.matches("aaaa.A<bbbb.B,ccc.C>"));
        assertFalse( $tr.matches("A<C,B>")); //wrong order
    }

    public void testType(){
        _typeRef _t = _typeRef.of("A<B>");
        assertEquals(_typeRef.of("A"), _t.getErasedType() );
        NodeList<Type> tas = _t.getTypeArguments();
        assertTrue( tas.isNonEmpty() );
        assertTrue(Types.equal( tas.get(0), Types.of("B")));

        _t = _typeRef.of("@NotNull A<? extends C, D>[][]");
        assertTrue( _t.isGenericType());
        assertTrue( _t.isArrayType());
        //System.out.println( _t);
        //Ast.describe(_t.ast());
        assertTrue( _t.hasAnnoExprs());
        //System.out.println( _t.getAnnos() );
        assertEquals( _annoExprs.of("@NotNull"), _t.getAnnoExprs() );
        //System.out.println( _t.getBaseType() );

        _t = _typeRef.of("@NotNull aaaaa.bbbb.A<? extends C, D>[][]");
        assertEquals(_typeRef.of("@NotNull aaaaa.bbbb.A[][]"), _t.getErasedType() );
        assertEquals(_typeRef.of("aaaaa.bbbb.A"), _t.getBaseType() );
    }

    public void testTypeOfGeneric(){
        $typeRef $t = $typeRef.of("A");
        assertTrue( $t.matches("A"));
        assertTrue( $t.matches("aaaa.bbbb.A"));
        assertTrue( $t.matches("A<B>"));
        assertTrue( $t.matches("aaaa.bbbb.A<B>"));
        assertTrue( $t.matches("A<? extends C>"));
        assertTrue( $t.matches("aaaaa.bbbb.A<? extends C>[]")); //generic AND array
        assertTrue( $t.matches("@NotNull aaaaa.bbbb.A<? extends C>[]")); //generic AND array

        //as for EXACT AS match
        $t = $typeRef.as("A");
        assertTrue( $t.matches("A"));
        assertTrue( $t.matches("aaaa.bbbb.A"));

        assertFalse( $t.matches("@NotNull A"));
        assertFalse( $t.matches("A<B>"));
        assertFalse( $t.matches("aaaa.bbbb.A<B>"));
        assertFalse( $t.matches("A<? extends C>"));
        assertFalse( $t.matches("aaaaa.bbbb.A<? extends C>[]")); //generic AND array
        assertFalse( $t.matches("@NotNull aaaaa.bbbb.A<? extends C>[]")); //annotated generic AND array
    }

    public void testTypeOfAnno(){
        $typeRef $t = $typeRef.of("A");
        Type t = Types.of("@NotNull A");
        assertTrue( $t.matches("@NotNull A"));
        assertTrue( $t.matches("@NotNull aaaa.bbbb.A<B,C>[]")); //annotated generic and array
    }

    public void testTypeOfArr(){
        $typeRef $t = $typeRef.of(int.class);
        assertTrue( $t.matches(int.class));
        assertTrue( $t.matches(int[].class));

        $t = $typeRef.of(int[].class);
        assertFalse( $t.matches(int.class));
        assertTrue( $t.matches(int[].class));

        $t = $typeRef.of(int[][].class);
        assertFalse( $t.matches(int.class));
        assertFalse( $t.matches(int[].class));
        assertTrue( $t.matches(int[][].class));
    }

    public void testHardcode$(){
        $typeRef $tr = $typeRef.of( "Map<$A$, $B$>" );

        assertTrue($tr.match(Types.of("Map<Integer, Integer>") ));
        $tr.$hardcode("A", "String");
        assertTrue($tr.match(Types.of("Map<String, Integer>") ));
        assertTrue($tr.match(Types.of("Map<java.lang.String, Integer>") ));
        assertFalse($tr.match(Types.of("Map<Integer, Integer>") ));

    }

    public static class OldT{
        public static final int F = 1;
        
        public static int m() {
            return 2;
        }
    }
    
    public static class NewT{
        public static final int F = 1;
        
        public static int m() {
            return 2;
        }
    }
    
    public void testTypeDecl(){
        class C{
            int a;
        }
        assertEquals(1, $typeRef.of().listIn(C.class).size() );
        assertTrue($typeRef.of().listSelectedIn(C.class).get(0).is("typeDecl", int.class) );
    }
    
    public void testFindAllTypeReferences(){
        
        _class _c = _class.of("AA", new Object(){
            public OldT field = new OldT();
            
            public OldT method( OldT one ){
                /** WORKING */                
                org.jdraft.pattern.StypeRefTest.OldT fv = new org.jdraft.pattern.StypeRefTest.OldT();
                
                //annotation ?
                org.jdraft.pattern.StypeRefTest.OldT[] arr = new org.jdraft.pattern.StypeRefTest.OldT[2];
                
                org.jdraft.pattern.StypeRefTest.OldT rr = (org.jdraft.pattern.StypeRefTest.OldT)one; //cast
                
                /* NOT WORKING  */
                OldT var = new OldT();
                
                System.out.println( OldT.F ); //field access
                OldT.m();
                System.out.println(org.jdraft.pattern.StypeRefTest.OldT.F ); //field access
                org.jdraft.pattern.StypeRefTest.OldT.m();
                return var;
            }            
        });
        
        $typeRef.of(OldT.class).replaceIn(_c, NewT.class );
        System.out.println( _c );
        
        //assertEquals( 12, $typeRef.of(OldT.class).listIn(_c).size() );
        
        $typeRef $t = $typeRef.of("OldT");
        $t.replaceIn(_c, _typeRef.of("NewT"));
        
        List<MethodCallExpr> ms = Tree.list(_c, Ast.METHOD_CALL_EXPR );
        ms.forEach(m -> System.out.println( "METHOD SCOPE " + m.getScope().get().toString() ));
        
        Tree.in(_c, MethodCallExpr.class, mc -> {
            if(mc.getScope().isPresent()){
                if( mc.getScope().get().toString().equals("draft.java.proto.$typeRefTest.OldT")){
                    mc.setScope(Exprs.of("draft.java.proto.$typeRefTest.NewT"));
                }
                if( mc.getScope().get().toString().equals("OldT")){
                    mc.setScope(Exprs.of("NewT"));
                }
            }
        });
        
        Tree.in(_c, Ast.FIELD_ACCESS_EXPR, mc -> {
            if(mc.getScope().toString().equals("draft.java.proto.$typeRefTest.OldT")){
                mc.setScope(Exprs.of("draft.java.proto.$typeRefTest.NewT"));
            }
            if( mc.getScope().toString().equals("OldT")){
                mc.setScope(Exprs.of("NewT"));
            }
        });
        
        System.out.println( _c );
        
        System.out.println("FOUND FIELDS + " + Tree.in(_c, Ast.FIELD_ACCESS_EXPR, f-> f.toString().equals( "draft.java.proto.$typeRefTest.OldT" ) ) );
        System.out.println("FOUND FIELDS + " + Tree.in(_c, Ast.FIELD_ACCESS_EXPR, f-> f.toString().equals( "OldT" ) ) );
        System.out.println("FOUND METHODS 1+ " + Tree.in(_c, Ast.METHOD_CALL_EXPR, m-> m.toString().equals( "draft.java.proto.$typeRefTest.OldT" ) ) );
        
        Tree.in( _c, Ast.METHOD_CALL_EXPR, mc -> mc.getScope().isPresent() && mc.getScope().get().toString().equals("draft.java.proto.$typeRefTest.OldT"),
                mc -> mc.setScope(Exprs.of("draft.java.proto.$typeRefTest.OldT")));
        
        System.out.println( _c );
        
        
        
        System.out.println(Tree.list(_c, Ast.FIELD_ACCESS_EXPR, f->{
                    Expression s = f.getScope(); 
                    System.out.println( "SCOPE CLASS " + s.getClass()+ " "+ f.getScope() );
                    return s != null; }) );
        
        //System.out.println( _walk.list(_c, Ast.TYPE ) );
        //System.out.println( $typeRef.list(_c, "$any$", t-> true) );
        
        //_c = _class.of( $typeRefTest.class);
        //_c = _class.of( II.class);
        //$typeRef $t = $typeRef.of(OldT.class);
        //$t.replaceIn(_c, NewT.class );
        //System.out.println( _c );
    }
    
    public void testStaticCalls(){
        class F{
            int a;
            public F( int b ){
                this.a = b;
            }
            public int getA(){
                return this.a;
            }
            public void setA( int a){
                this.a = a;
            }
        }
        _class _c = _class.of(F.class);
        assertEquals("expected (4) instance of int in _c", 4, $typeRef.of(int.class).listIn(_c).size());
        
        $typeRef.of(int.class).replaceIn(_c, float.class); //change all int TYPE references to float
        //System.out.println( _c );
        assertEquals("expected (0) instance of int in _c", 0, $typeRef.of(int.class).listIn(_c).size());
        assertEquals("expected (4) instance of float in _c", 4, $typeRef.of(float.class).listSelectedIn(_c).size());

        //find all float types in _c and replaceIn them with double
        $typeRef.of(float.class).replaceIn(_c, double.class);
        assertEquals("expected (4) instance of double in _class", 4, $typeRef.of(double.class).listSelectedIn(_c).size());
    }
    
    public void testSimple(){
        $typeRef $t = $typeRef.of(int.class);
        assertEquals(_typeRef.of(int.class), $t.draft());

        assertNotNull( $t.select(Types.INT_TYPE));
        assertNull( $t.select(Types.FLOAT_TYPE));
        class F{
            int a;
            public F( int b ){
                this.a = b;
            }
            public int getA(){
                return this.a;
            }
            public void setA( int a){
                this.a = a;
            }
        }
        _class _c = _class.of(F.class);
        assertEquals( "expected (4) instance of int in _c", 4, $t.listSelectedIn(_c).size());
        $t.replaceIn(_c, float.class); //change all int TYPE references to float
        //System.out.println( _c );
        assertEquals( "expected (0) instance of int in _c", 0, $t.listSelectedIn(_c).size());
        assertEquals("expected (4) instance of float in _c", 4, $typeRef.of(float.class).listSelectedIn(_c).size());

        //find all float types in _c and replaceIn them with double
        $typeRef.of(float.class).replaceIn(_c, double.class);
        assertEquals("expected (4) instance of double in _class", 4, $typeRef.of(double.class).listSelectedIn(_c).size());
    }


    public void testReplaceComplexType(){
        class FF{
            Set<Integer> is = new TreeSet<Integer>();
            Set<String> ss = new TreeSet<String>();

            public TreeSet<Double> m(){
                return new TreeSet<Double>();
            }

        }
        $typeRef $anyTreeSet = $typeRef.of("TreeSet<$any$>");
        _class _c = _class.of(FF.class);
        //verify I can find a
        assertEquals(4, $anyTreeSet.listSelectedIn(_c).size());

        $anyTreeSet.replaceIn(_c, $typeRef.of("HashSet<$any$>") ); //convert TreeSet to HashSet
        //System.out.println( _c );
    }

    public void testReplaceComplexTypeStatic(){
        class FF{
            //make sure it handled fields
            Set<Integer> is = new TreeSet<Integer>();
            Set<String> ss = new TreeSet<String>();

            //make sure it changes the return type
            public TreeSet<Double> m(){
                //make sure it changes types in body
                return new TreeSet<Double>();
            }
            
            //make sure it changes parameters
            public void b( TreeSet<Integer> i ){}

        }
        //$typeRef $anyTreeSet = $typeRef.of("TreeSet<$any$>");
        _class _c = _class.of(FF.class);
        //verify I can find a
        assertEquals(5, $typeRef.of("TreeSet<$any$>").listIn(_c).size());
        $typeRef.of("TreeSet<$any$>").replaceIn(_c, "HashSet<$any$>");
        //$anyTreeSet.replaceIn(_c, $typeRef.of("HashSet<$any$>") ); //convert TreeSet to HashSet
        System.out.println( _c );
    }
    
    public void testReplaceGenericPart(){
        class GG{
            List<Integer> li;
            Set<Integer> si;

            public Map<Integer,Integer> getInts(){
                return new HashMap<Integer,Integer>();
            }
        }
        _class _c = _class.of(GG.class);
        $typeRef.of(Integer.class).replaceIn(_c, $typeRef.of(Float.class));
        //System.out.println( _c );
    }
}
