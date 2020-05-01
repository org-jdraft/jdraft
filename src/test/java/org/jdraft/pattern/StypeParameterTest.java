package org.jdraft.pattern;

import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft._java;
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

    public void testTypeParameterOf(){
        $typeParameter $tp = $typeParameter.of("A");
        assertTrue( $tp.matches("A"));//regular
        assertTrue( $tp.matches("@Ann A"));//regular
        assertTrue($tp.matches("A extends B"));

        $tp = $typeParameter.of("A extends B");
        assertFalse( $tp.matches("A"));//regular
        assertTrue($tp.matches("A extends B"));
        assertTrue($tp.matches("A extends bbbb.B"));
        assertTrue($tp.matches("A extends @Ann bbbb.B"));

        $tp = $typeParameter.of("A");
        assertTrue( $tp.matches("A"));//fully qualified (diff type)

        $tp = $typeParameter.of("A extends B");
        assertTrue($tp.matches("A extends B"));
        assertTrue($tp.matches("A extends @Ann B"));
        assertTrue($tp.matches("A extends bbbb.B"));
        assertTrue($tp.matches("A extends @Ann bbbb.B"));

        $tp = $typeParameter.of("A extends @Ann B");
        assertTrue($tp.matches("A extends @Ann B"));
        assertTrue($tp.matches("A extends @Ann bbbb.B"));
        assertFalse($tp.matches("A extends B"));

        assertFalse($tp.matches("A extends C"));
        assertFalse($tp.matches("A extends @Ann C"));

    }

    public void testTypeParameterAs(){
        $typeParameter $tp = $typeParameter.as("A");
        assertTrue( $tp.matches("A"));//regular
        assertFalse( $tp.matches("@Ann A"));//anno
        assertFalse($tp.matches("A extends B"));


        $tp = $typeParameter.as("A extends B");

        assertTrue($tp.matches("A extends B"));
        assertTrue($tp.matches("A extends bbbb.B"));

        $tp = $typeParameter.as("A extends B");
        assertTrue($tp.matches("A extends B"));
        assertTrue($tp.matches("A extends bbbb.B"));

        assertFalse( $tp.matches("A extends @Ann B"));
        assertFalse( $tp.matches("A"));//regular
        assertTrue( $tp.matches("A extends @Ann bbbb.B"));

        $tp = $typeParameter.as("A extends @Ann B");
        assertTrue($tp.matches("A extends @Ann B"));
        assertTrue($tp.matches("A extends @fully.qualified.Ann B"));
        assertFalse($tp.matches("A extends B"));
        assertFalse($tp.matches("A extends @Cann B"));
    }

    /** THIS IS A KNOWN WEAKNESS THAT SHOULD BE FIXED */
    public void testFullyQualifiedTypeBoundWithAnnotation(){
        try {
            $typeParameter $tp = $typeParameter.as("A extends @Ann B");
            assertTrue($tp.matches("A extends @Ann bbbb.B")); //THIS WILL FAIL
            assertTrue($tp.matches("A extends @fully.qualified.Ann bbbb.B")); //THIS WILL FAIL
        }catch(Throwable e){
            //THIS NEEDS TO BE FIXED FOOL
        }
    }

    public void test$ann(){
        $annoRef $a = $annoRef.of("A");
        assertTrue( $a.matches("fully.qualified.A"));
        assertTrue( $a.matches("@fully.qualified.A"));

        $a = $annoRef.as("A");
        assertTrue( $a.matches("fully.qualified.A"));
        assertTrue( $a.matches("@fully.qualified.A"));
    }

    public void test$typeRefAnn(){
        $annoRef $a = $annoRef.of("@Ann");
        assertTrue( $a.matches("@fully.qualified.Ann"));

        $annoRefs $as = $annoRefs.of("@Ann");
        assertTrue( $as.matches("@fully.qualified.Ann"));


        $typeRef $t = $typeRef.of( "@Ann Map<String,Integer>");

        assertTrue( $t.matches("@Ann Map<String,Integer>"));
        //assertTrue(Ast.typesEqual(Ast.typeRef( "@Ann Map<String,Integer>"), Ast.typeRef( "@fully.qualified.Ann Map<String,Integer>")));

        assertTrue( $t.matches("@fully.qualified.Ann Map<String,Integer>"));


    }
    public void testTT(){
        $typeParameter $tp = $typeParameter.as("A extends B");

    }

    public void testTypeParameterName() {
        class F<I> {
            int a;
            String b;

            public <A extends Map & _java._multiPart> A m() {
                return null;
            }
        }

        assertEquals( 2, $typeParameter.of().countIn(F.class)); //A, I
        assertEquals( 1, $typeParameter.of().$name( $name.of("A")).countIn(F.class)); //A
        assertEquals( 1, $typeParameter.of().$name( $name.of("I")).countIn(F.class)); //A

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
        assertEquals(1, $typeParameter.of(tp-> tp.getTypeBound().isEmpty()).countIn(C.class));
        assertEquals(2, $typeParameter.of(tp-> tp.getTypeBound().isNonEmpty()).countIn(C.class));
        assertEquals(1, $typeParameter.of(tp-> tp.getTypeBound().size() == 1).countIn(C.class));
        assertEquals(1, $typeParameter.of(tp-> tp.getTypeBound().size() == 2).countIn(C.class));
        
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
