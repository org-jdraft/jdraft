package org.jdraft.pattern;

import org.jdraft._class;
import org.jdraft._parameter._parameters;
import org.jdraft._type;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SparametersTest extends TestCase {

    public void testAs(){
        //$parameters.as("int i, String s");
    }

    public void testEmptyParameters(){
        class M{
            void a(){}
            void b(int a){}
            void c(int...f){}
        }
        assertEquals(1, $parameters.none().count(M.class));
    }
    
    public void testParametersAny(){
        $parameters $ps = $parameters.of();
        
        //verify that ANY matches / Selects NO parameters at all
        assertNotNull( $ps.select("") );
        assertNotNull( $ps.select(_parameters.of("")) );
        assertNotNull( $ps.select("( )") );
        assertNotNull( $ps.select(_parameters.of("()")) );        
        assertTrue( $ps.matches("") );
        assertTrue( $ps.matches("()") );
        
        //selects a single
        assertNotNull( $ps.select("int i"));
        assertNotNull( $ps.select("(int i)"));
        
        assertTrue( $ps.select("int i, String j").is("parameters", "(int i, String j)"));
        assertTrue( $ps.select("(int i, String... j)").is("parameters", "(int i, String... j)"));
        
        assertNotNull( $ps.select(_parameters.of()) );
        assertTrue( $ps.matches(_parameters.of()) );
        
        //lets test compose...
        assertEquals( _parameters.of(), $ps.draft());
        //assertEquals( _parameters.of("int i"), $ps.construct("parameters", "int i"));        
    }
    
    public void testFixedParameters(){
        //single
        $parameters $ps = $parameters.of("int i");
        assertTrue($ps.matches("int i"));
        assertNotNull($ps.select("int i"));
        
        assertTrue($ps.matches("final int i"));
        assertTrue($ps.matches("int... i"));
        assertTrue($ps.matches("final int... i"));
        
        assertFalse( $ps.matches("int j"));
        assertFalse( $ps.matches("String i"));
        assertNull( $ps.select("int j"));
        assertNull( $ps.select("String i"));
        
        assertFalse( $ps.matches("int i, String j") ); //extra param
        assertNull( $ps.select("int i, String j") ); //extra param
        
        //finalVarArg
        $ps = $parameters.of("final int... f");
        assertTrue($ps.matches("final int...f"));
        assertNotNull($ps.select("final int... f"));
        
        assertFalse($ps.matches("int...f"));
        assertFalse($ps.matches("final int f"));
        assertFalse($ps.matches("final int...j"));
        assertFalse($ps.matches("final String...f"));
        
        
        $ps = $parameters.of("@A final String a, @B @C @D(1) final List<TT>... b");
        assertTrue( $ps.matches("@A final String a, @B @C @D(1) final List<TT>... b"));
        assertTrue( $ps.matches("@A final String a, @D(1) @B @C final List<TT>... b")); //out of order
        
        assertFalse( $ps.matches("@A final String a, @D(1) @B final List<TT>... b")); //missing anno
        assertFalse( $ps.matches("@A final String a, @D(1) @B @C List<TT>... b")); //missing final
        assertFalse( $ps.matches("@A final String a, @D(1) @B @C final List<TT> b")); //missing vararg
    }
    
    public void testDynamicParameters(){
        $parameters $ps = $parameters.of("$type$ a");
        assertEquals( _parameters.of("String a"), $ps.draft("type", String.class));
        
        //this will match any (single) parameters with only a
        assertTrue($ps.matches("int a") );
        assertTrue($ps.matches("String a") );
        assertTrue($ps.matches("Map<Integer,String> a") );        
        assertTrue($ps.matches("final Map<Integer,String>... a") );
        
        assertTrue($ps.select("int a").is("type", int.class) );
        
        $ps = $parameters.of("String $p1$, final int $p2$");
        
        assertTrue( $ps.matches("String s, final int i") );
        assertTrue( $ps.matches("@ann1 @ann2 final String s, @ann2 final int i") );
        assertTrue( $ps.matches("String tt, final int... z") );
        
        //when I select, I can easily access the parameter names
        assertTrue( $ps.select("String tt, final int... z")
            .is("p1", "tt", "p2", "z"));
        
        class c{
            void m( String tt, final int...z ){
                
            }
        }
        assertTrue( $ps.listIn(c.class).size() == 1 );
        assertTrue( $ps.listSelectedIn(c.class).size() == 1 );
        assertTrue( $ps.listSelectedIn(c.class).get(0).is("p1","tt","p2","z"));
        
        _class _c = _class.of(c.class);
        _type _rep = $ps.replaceIn(_c, $parameters.of("int i"));
        System.out.println(_rep);
    }
}
