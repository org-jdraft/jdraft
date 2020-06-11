package org.jdraft.pattern;

import org.jdraft._anno;
import org.jdraft._annos;
import org.jdraft._class;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SannoRefsTest extends TestCase {
    
    public void testAnnosSingleMember(){
        $annoRef $a = $annoRef.of("A(2)" );
        _anno _a = $a.draft();
        
        _anno _aa = _anno.of("A(4)");
        _annos _aaa = _annos.of("@A(2)");
        
        $annoRefs $as = $annoRefs.of("@A(1)");
        $as.draft();
        System.out.println( $as.draft() );
    }
    
    public void testAnnosNone(){
        @Deprecated
        class DF{
            @Deprecated int a;
            String s;
            @Deprecated void m(){}
            int g;
        }
        //s and g
        assertEquals(6, $annoRefs.none().countIn(DF.class));
    }
    
    public void testComposeAny(){
        $annoRefs $as = $annoRefs.of();
        _annos _as = $as.draft(); //should work fine... empty annos
        assertTrue( _as.isEmpty() );
        
        //here you can OVERRIDE
        _as = $as.draft("$annos", "@A" );
        
        assertTrue( _as.has(_anno.of("@A")));
        
    }
    
    @Target({ElementType.TYPE_USE, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
    @interface TU{
        
    }
    @interface R{
        
    }
    public void testLambda(){
        
        
        class C{
            int x;
            public void m(){}
        }        
        assertEquals(5, $annoRefs.none().countIn(C.class) );
        assertEquals(5, $annoRefs.of(ans-> ans.isEmpty() ).countIn(C.class));
        
        
        //System.out.println( $annos.any().listIn(D.class) );
        
        //THIS IS 
        //assertEquals(5, $annos.of( ans-> !ans.isEmpty() ).count(D.class));
    }
    public void testT(){
        $annoRefs $as = new $annoRefs();
        _class _c = _class.of("C");
        assertNotNull( $as.select(_c) );
        assertNotNull( $as.parse(_c.getAnnoExprs()) );
        _c.addAnnoExprs("@A");
        assertNotNull( $as.select(_c) );
        assertNotNull( $as.parse(_c.getAnnoExprs()) );

        
        
        $as = $annoRefs.of("@A");
        assertNotNull( $as.select(_c) );
        assertNotNull( $as.select(_c.getAnnoExprs()) );
        assertNotNull( $as.parse(_c.getAnnoExprs()) );
        _c.removeAnnoExprs("A");
        assertNull( $as.select(_c) );
        assertNull( $as.parse(_c.getAnnoExprs()) );
        
        $as = $annoRefs.of($annoRef.of(Deprecated.class) );
        
        assertFalse( $as.matches(_class.of("C")));
        assertTrue( $as.matches(_class.of("C").addAnnoExprs(Deprecated.class)));
    }
}
