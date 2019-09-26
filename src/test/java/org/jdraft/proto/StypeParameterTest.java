package org.jdraft.proto;

import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft._node;
import org.jdraft._typeParameter;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class StypeParameterTest extends TestCase {

    public void testTypeParameterName() {
        class F<I> {
            int a;
            String b;

            public <A extends Map & _node> A m() {
                return null;
            }
        }

        assertEquals( 2, $typeParameter.of().count(F.class)); //A, I
        assertEquals( 1, $typeParameter.of().$name( $name.of("A")).count(F.class)); //A
        assertEquals( 1, $typeParameter.of().$name( $name.of("I")).count(F.class)); //A

        //_class _c = _class.of(F.class);

        //System.out.println($typeParameter.of().$name("A").listIn(_c));
    }

    public void testCompose(){
        
        $typeParameter $tp = 
            $typeParameter.of("@A @C(k=1,v='c') G extends @UU RR & @VV GG");
        assertEquals(_typeParameter.of("@A @C(k=1,v='c') G extends @UU RR & @VV GG"), $tp.draft());
        
        assertEquals(_typeParameter.of("F"), $typeParameter.of("$name$").draft("name", "F"));
        assertEquals(_typeParameter.of("@A F"), $typeParameter.of("@A $name$").draft("name", "F"));
        assertEquals(_typeParameter.of("@A F extends Serializable"), $typeParameter.of("@A $name$ extends Serializable").draft("name", "F"));
    }
    
    public void testAny(){
        assertTrue( $typeParameter.of().matches("A"));
        assertTrue( $typeParameter.of().matches(new TypeParameter("A")));
        assertTrue( $typeParameter.of().matches(_typeParameter.of("A")));
        assertNotNull( $typeParameter.of().select("A"));
        
        assertTrue( $typeParameter.of().matches("A extends B"));
        assertTrue( $typeParameter.of().matches("A extends B & C"));
        
        $typeParameter.Select sel = 
            $typeParameter.of().select(_typeParameter.of("A"));
        assertNotNull( sel );
        
        assertEquals( _typeParameter.of("A"), 
            $typeParameter.of().draft("typeParameter", "A"));
    }
    
    public void testMatchTypeBound(){
        $typeParameter $tp = 
            $typeParameter.of("$name$ extends Serializable & Appendable");
        
        //doesnt matter the order of the things
        assertTrue($tp.matches("A extends Serializable & Appendable"));
        assertTrue($tp.matches("A extends Appendable & Serializable"));
        //can have additional type bounds
        assertTrue($tp.matches("A extends Appendable & Serializable & AnotherThing"));
        
        assertFalse($tp.matches("A extends Appendable"));
        assertFalse($tp.matches("A extends Serializable"));
        
        class GG <T extends Serializable & Appendable>{
            
        }
        assertTrue( $tp.matches("T extends Serializable & Appendable") );
        assertTrue($tp.selectFirstIn(GG.class).is("name", "T"));        
    }
    
    public void testMatchAnnos(){
        $typeParameter $tp = $typeParameter.of("@A I");
        assertTrue( $tp.matches("@A I"));
        assertTrue( $tp.matches(_typeParameter.of("@A I")));
        assertFalse( $tp.matches(_typeParameter.of("I")));
        assertFalse( $tp.matches(_typeParameter.of("@B I")));
    }
    
    public void testLambda(){
        class C<A, B extends Serializable, C extends Map & Serializable>{
        }
        assertEquals(1, $typeParameter.of(tp-> tp.getTypeBound().isEmpty()).count(C.class));
        assertEquals(2, $typeParameter.of(tp-> tp.getTypeBound().isNonEmpty()).count(C.class));
        assertEquals(1, $typeParameter.of(tp-> tp.getTypeBound().size() == 1).count(C.class));
        assertEquals(1, $typeParameter.of(tp-> tp.getTypeBound().size() == 2).count(C.class));
        
    }
    
    public void testForEachIn(){
        class C <T>{
            void m(Object r){}
        }
        AtomicInteger count = new AtomicInteger(0);
        $typeParameter.of().forEachIn(C.class, tp ->count.incrementAndGet());
        assertEquals( 1, count.get());
    }
    
}
