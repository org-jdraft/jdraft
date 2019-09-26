package org.jdraft.proto;

import org.jdraft.*;

import java.util.function.Predicate;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SfTest extends TestCase {

    public void testVar_v_VarDecl(){
        class D{
            int a;
            void m(int b){
                String c;
            }
        }

        System.out.println( $.var().listIn(D.class) ); //[a, c]
        System.out.println( $.field().listIn(D.class) );  //[int a;]
        System.out.println( $.varLocal().listIn(D.class) );  //[String c]
        System.out.println( $.parameter().listIn(D.class) ); //[int b]

        System.out.println( $.var().$type(int.class).listIn(D.class)); //[a]

        //expect int, void, int, int
        //System.out.println( $.typeRef().listIn(D.class) );

        //_class _c = _class.of(D.class);
        Ast.varDecl("int c");
        assertEquals( Ex.varLocalEx("String c"), $.varLocal().firstIn(D.class) ); //int c

        assertEquals(2, $.var().count(D.class));
        //assertEquals(5, $typeUse.of().listIn(_class.of(D.class).astCompilationUnit()));
        System.out.println( $typeUse.of().listIn(_class.of(D.class).astCompilationUnit()));
    }

    public void testSynch(){
        class R{
            Integer val;
            synchronized int g(){
                return 456;
            }
            public R(){
                synchronized (val){
                    System.out.println(val);
                }
            }
        }
        assertEquals(1, $.method( $modifiers.of("synchronized")).listIn(R.class).size());
        assertEquals(1, $.synchronizedStmt().listIn(R.class).size());
    }

    public void testFieldAsAnonymousObject(){
        $field $f = $field.of( new Object(){
            @Deprecated
            public static final int ID = 100; 
        });
        
        assertTrue( $f.matches( "@Deprecated public static final int ID=100;" ) );
    }
    
     public void testSimple$field(){
        //the field prototype specifies the type and name (only)
        $field $simpleField = $field.of( _field.of("int i") );
        $simpleField = $field.of( "int i" );
        
        
        //it will match the EXACT field definition
        assertTrue($simpleField.matches("int i") );
        assertTrue($simpleField.matches(_field.of("int i")) );
        
        //it WONT match fields that dont match the (2) provided features (type and name)
        assertFalse($simpleField.matches("String i") );
        assertFalse($simpleField.matches("int z") );
        
        // PROTOTYPE FEATURES 
        // it will also match fields that match on those (2) features with additional features
        // here with the init
        assertNotNull($simpleField.matches(_field.of("int i=100;")) );
        assertNotNull($simpleField.matches("int i=100;") );
        
        assertTrue($simpleField.select("int i=100;").isInit(100) );
        assertTrue($simpleField.select("int i=100;").isType(int.class) );
        
        assertNotNull($simpleField.select("/**javadoc*/ int i")); //.is("javadoc", "/**javadoc*/") );
        assertNotNull($simpleField.select("@Deprecated int i") );        
        assertNotNull($simpleField.select("/**javadoc*/ @Deprecated int i=0;") );
    }
     
    /** build a prototype from just a predicate */
    public void testOfPredicate(){
        $field $anyVolatile = $field.of( f-> f.isVolatile() );        
        assertTrue( $anyVolatile.matches( "volatile int v;" ) );
    }
    
    /** Build a $field prototype with only the name */
    public void testOfName(){
        $field $x = $field.of($name.of("x"));
        assertTrue( $x.matches("public static final List<String> x;") );
    }
    
    /** build a $field prototype with only the type */
    public void testAnyFieldOfType(){
        $field $anyInt = $field.of( $typeRef.of(int.class) );
        
        //it will match any field
        assertTrue( $anyInt.matches("int x"));
        assertTrue( $anyInt.matches("/** javadoc */ @ann public static final int x"));
        
        //it will not match fields with other types
        assertFalse( $anyInt.matches("String s"));
        assertFalse( $anyInt.matches("List<Integer> s"));        
    }
    
    /** Build a prototype with a dynamic variable */
    public void testDynamic$field(){
        $field $typeField = $field.of( _field.of("$type$ i") );
        
        assertTrue($typeField.select(_field.of("int i")).isType( int.class) );
        
        assertTrue($typeField.select("int i").isType( int.class) );
        
        assertTrue($typeField.select(_field.of("String i")).isType( "String") );
        
        assertTrue($typeField.select("String i").isType("String") );
        
        assertTrue($typeField.select("String i").isType(_typeRef.of(String.class)));        
    }
    
    public void testTypeGeneric$field(){
        $field $listType = $field.of($typeRef.of("List<$elType$>"));
        assertTrue( $listType.matches("List<String> f;") );
        assertTrue( $listType.matches("List<Map<Integer,String>> g;") );
        
        assertFalse( $listType.matches("Map<List<Integer>,String> g;") );
        
        $listType = $field.of($typeRef.of("List<$elType$>"));
        assertTrue( $listType.matches("List<String> f;") );
        assertTrue( $listType.matches("List<Map<Integer,String>> g;") );
        
        assertFalse( $listType.matches("Map<List<Integer>,String> g;") );
    }
    
    /**
     * Prototypes themselves are dynamic
     * 
     * ... this can help you "build" the appropriate prototype
     * when you want to remove / replace / list ... 
     * 
     * instead of starting with a VERY specific prototype
     * just build a simple prototype i.e. 
     * $f.any()
     * 
     * then start add constraints, stencils, etc. that will help you identify
     * only the things you want (to list/update/modify)
     */
    public void testBuild$f(){
        $field $b = $field.of(); //any field
        $b.$and(f-> f.hasAnno(Deprecated.class));
        
        assertFalse( $b.matches("int a") ); //expected Deprecated
        assertTrue( $b.matches("@Deprecated int a") ); //expected Deprecated
        
        
        
        //assertTrue( $b.matches("@Deprecated int a=1;") );

        $b.$and(f -> f.isType(String.class));
        //assertTrue( $b.matches("@Deprecated String a=1;") );
        
        $b.$and(f -> f.hasInit());
        
        assertTrue( $b.constraint.test( _field.of("@Deprecated String i = \"e\";") ));
        assertFalse( $b.constraint.test( _field.of("String i = \"e\";") ));
        
        //$b.constraint.test(t)
        assertFalse( $b.matches("int a") ); //expected Deprecated & init
        assertFalse( $b.matches("@Deprecated int a") ); //expected init
        
        
        assertTrue( $b.matches("@Deprecated String s = \"S\";") ); 
        
        assertFalse( $b.matches("@Deprecated String s;") ); 
        
    }
    
    public void testB(){
        Predicate<_field> pf = (_field f)-> {
            return f.hasAnno(Deprecated.class);
        };
        assertTrue( pf.test( _field.of("@Deprecated int i") ) );        
        
        pf = pf.and(f -> f.isType(int.class));        
        assertTrue( pf.test( _field.of("@Deprecated int i=100") ) );
        
        assertFalse( pf.test( _field.of("@Deprecated float i=1.0f") ) );
        
        pf.and(f -> f.hasInit() );
        assertTrue( pf.test( _field.of("@Deprecated int i=100") ) );
    }
    
    
    public void testConstruct$F(){
        _field _f = $field.of("int f").draft();
        System.out.println( _f );
        
        assertTrue( _f.is("int f") );
        
        _f = $field.of($name.of("f")).draft("type", int.class);
        assertTrue( _f.is("int f") );
        
        _f = $field.of($typeRef.of(int.class)).draft("name", "f");
        assertTrue( _f.is("int f") );
        
        
        $field $full = $field.of("/** javadoc */ @Deprecated public static final int IF = 100;");
        
        _f = $full.draft();
        assertTrue( _f.is("/** javadoc */ @Deprecated public static final int IF = 100;") );
    }
}
