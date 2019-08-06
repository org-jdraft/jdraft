package org.jdraft.proto;

import org.jdraft._anno;
import org.jdraft._anno._annos;
import org.jdraft._class;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SannosTest extends TestCase {
    
    public void testAnnosSingleMember(){
        $anno $a = $anno.of("A(2)" );
        _anno _a = $a.compose();
        
        _anno _aa = _anno.of("A(4)");
        _annos _aaa = _annos.of("@A(2)");
        
        $annos $as = $annos.of("@A(1)");
        $as.compose();
        System.out.println( $as.compose() );
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
        assertEquals(6, $annos.none().count(DF.class));
    }
    
    public void testComposeAny(){
        $annos $as = $annos.of();
        _annos _as = $as.compose(); //should work fine... empty annos
        assertTrue( _as.isEmpty() );
        
        //here you can OVERRIDE
        _as = $as.compose("$annos", "@A" );
        
        assertTrue( _as.contains(_anno.of("@A")));
        
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
        assertEquals(5, $annos.none().count(C.class) );
        assertEquals(5, $annos.of( ans-> ans.isEmpty() ).count(C.class));
        
        
        //System.out.println( $annos.any().listIn(D.class) );
        
        //THIS IS 
        //assertEquals(5, $annos.of( ans-> !ans.isEmpty() ).count(D.class));
    }
    public void testT(){
        $annos $as = new $annos();
        _class _c = _class.of("C");
        assertNotNull( $as.select(_c) );
        _c.annotate("@A");
        assertNotNull( $as.select(_c) );
        
        
        $as = $annos.of("@A");
        assertNotNull( $as.select(_c) );
        _c.removeAnnos("A");
        assertNull( $as.select(_c) );
        
        $as = $annos.of($anno.of(Deprecated.class) );
        
        assertFalse( $as.matches(_class.of("C")));
        assertTrue( $as.matches(_class.of("C").annotate(Deprecated.class)));
    }
}
