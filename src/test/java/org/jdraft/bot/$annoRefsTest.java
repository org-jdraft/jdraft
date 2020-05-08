package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._annoRef;
import org.jdraft._annoRefs;

public class $annoRefsTest extends TestCase {

    public void testMatchAny(){
        //verify that "anyMatch" matches no annotations
        assertTrue( $annoRefs.of().matches(""));

        //singe annotations
        assertTrue( $annoRefs.of().matches("@A"));
        assertTrue( $annoRefs.of().matches("@A(1)"));
        assertTrue( $annoRefs.of().matches("@A(k=1,v=2)"));

        //multiple annotations
        assertTrue( $annoRefs.of().matches("@A @B"));
        assertTrue( $annoRefs.of().matches("@A(1) @B(1)"));
        assertTrue( $annoRefs.of().matches("@A(k=1,v=2) @C(k=1,v=2)"));
    }

    public void testMatchAsSingle() {
        assertTrue($annoRefs.as("@A").matches("@A"));
        assertFalse($annoRefs.as("@A").matches("@B"));
        assertFalse($annoRefs.as("@A").matches("@A @B"));
        assertFalse($annoRefs.as("@A").matches("@A(1)"));
        assertFalse($annoRefs.as("@A").matches("@A(key=1,value=2)"));

        assertTrue($annoRefs.as("@A(1)").matches("@A(1)"));
        assertFalse($annoRefs.as("@A(1)").matches("@A"));
        assertTrue($annoRefs.as("@A(1)").matches("@A(value=1)"));
        assertFalse($annoRefs.as("@A(1)").matches("@A(value=1,other=2)"));

        assertTrue($annoRefs.as("@A(k=1,v=2)").matches("@A(k=1,v=2)"));
        assertTrue($annoRefs.as("@A(k=1,v=2)").matches("@A(v=2,k=1)")); //order doenst matter for properties

        assertFalse($annoRefs.as("@A(k=1,v=2)").matches("@A(k=2,v=1)"));

        //draft
        assertEquals(_annoRefs.of("@A(k=1,v=2)"), $annoRefs.as("@A(k=1,v=2)").draft());
    }

    public void testMatchMultiple(){
        assertTrue($annoRefs.of("@A @B").matches("@A @B"));
        assertTrue($annoRefs.of("@A @B").matches("@A(1) @B(2)"));
        assertTrue($annoRefs.of("@A @B").matches("@A(key=1) @B(val=1,v=3)"));
        assertTrue($annoRefs.of("@A @B").matches("@B @A")); //order doesnt matter (for annotations)

        //As
        assertTrue($annoRefs.as("@A @B").matches("@B @A")); //order doesnt matter (for annotations)
        assertTrue($annoRefs.as("@A(1) @B").matches("@A(1) @B"));
        assertTrue($annoRefs.as("@A(1) @B").matches("@B @A(1)"));
        assertFalse($annoRefs.as("@A(1) @B").matches("@A(1) @B(2)"));

        assertTrue($annoRefs.as("@A @B").matches("@A @B"));
        assertTrue($annoRefs.as("@A @B").matches("@B @A")); //order doesnt matter (for annotations)
    }

    public void test$List(){
        assertEquals( $annoRefs.of("@my$name$(key=$value$)").$list().get(0), "name");
        assertEquals( $annoRefs.of("@my$name$(key=$value$)").$list().get(1), "value");
    }

    public void test$ListNormalized(){
        assertEquals( $annoRefs.of("@my$name$(key=$value$)").$listNormalized().get(0), "name");
        assertEquals( $annoRefs.of("@my$name$(key=$value$)").$listNormalized().get(1), "value");
    }

    @interface A{}
    @interface B{}
    @interface C{}

    public void testFromClass(){
        _annoRefs _ars = _annoRefs.of("@A");
        assertTrue(_ars.is("@A"));
        $annoRefs $ars = $annoRefs.of(_ars);
        assertTrue( $ars.matches("@A"));

        _ars = _annoRefs.of(B.class);
        assertTrue( _ars.is("@B"));

        assertTrue($annoRefs.of(_annoRef.of(B.class)).matches("@B"));

        $annoRefs.of(B.class).matches("@B");

        $annoRefs.of(_annoRef.of(B.class)).matches("@B");
    }

    public void test$Or(){

        $annoRefs $BandC = $annoRefs.of(B.class, C.class);
        assertTrue($BandC.matches("@B @C"));
        assertTrue($BandC.matches("@C @B"));

        //EITHER BOTH A OR B and C
        $annoRefs $ars = $annoRefs.or( $annoRefs.of(A.class), $annoRefs.of(B.class, C.class));

        assertTrue($ars.matches("@A"));
        assertTrue($ars.matches("@B @C"));
        assertTrue($ars.matches("@C @B"));

        assertTrue($ars.matches("@A(1)"));
        assertTrue($ars.matches("@A(1) @B @C"));
        //$annoRefs.of(A.class, B.class, C.class);
    }

    public void testHardCode(){
        $annoRefs $ars = $annoRefs.or( $annoRefs.of("@A($value$)"), $annoRefs.of("@my$name$($key$=2)") );

        assertTrue( $ars.matches("@A(1)"));
        assertTrue( $ars.matches("@A(2)"));
        $ars.$hardcode("value", "1"); //verify only @A(1) matches
        assertTrue( $ars.matches("@A(1)"));
        assertFalse( $ars.matches("@A(2)"));
    }

}
