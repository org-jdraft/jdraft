package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._annoExpr;
import org.jdraft._annoExprs;

public class $annoExprsTest extends TestCase {

    public void testMatchAny(){
        //verify that "anyMatch" matches no annotations
        assertTrue( $annoExprs.of().matches(""));

        //singe annotations
        assertTrue( $annoExprs.of().matches("@A"));
        assertTrue( $annoExprs.of().matches("@A(1)"));
        assertTrue( $annoExprs.of().matches("@A(k=1,v=2)"));

        //multiple annotations
        assertTrue( $annoExprs.of().matches("@A @B"));
        assertTrue( $annoExprs.of().matches("@A(1) @B(1)"));
        assertTrue( $annoExprs.of().matches("@A(k=1,v=2) @C(k=1,v=2)"));
    }

    public void testMatchAsSingle() {
        assertTrue($annoExprs.as("@A").matches("@A"));
        assertFalse($annoExprs.as("@A").matches("@B"));
        assertFalse($annoExprs.as("@A").matches("@A @B"));
        assertFalse($annoExprs.as("@A").matches("@A(1)"));
        assertFalse($annoExprs.as("@A").matches("@A(key=1,value=2)"));

        assertTrue($annoExprs.as("@A(1)").matches("@A(1)"));
        assertFalse($annoExprs.as("@A(1)").matches("@A"));
        assertTrue($annoExprs.as("@A(1)").matches("@A(value=1)"));
        assertFalse($annoExprs.as("@A(1)").matches("@A(value=1,other=2)"));

        assertTrue($annoExprs.as("@A(k=1,v=2)").matches("@A(k=1,v=2)"));
        assertTrue($annoExprs.as("@A(k=1,v=2)").matches("@A(v=2,k=1)")); //order doenst matter for properties

        assertFalse($annoExprs.as("@A(k=1,v=2)").matches("@A(k=2,v=1)"));

        //draft
        assertEquals(_annoExprs.of("@A(k=1,v=2)"), $annoExprs.as("@A(k=1,v=2)").draft());
    }

    public void testMatchMultiple(){
        assertTrue($annoExprs.of("@A @B").matches("@A @B"));
        assertTrue($annoExprs.of("@A @B").matches("@A(1) @B(2)"));
        assertTrue($annoExprs.of("@A @B").matches("@A(key=1) @B(val=1,v=3)"));
        assertTrue($annoExprs.of("@A @B").matches("@B @A")); //order doesnt matter (for annotations)

        //As
        assertTrue($annoExprs.as("@A @B").matches("@B @A")); //order doesnt matter (for annotations)
        assertTrue($annoExprs.as("@A(1) @B").matches("@A(1) @B"));
        assertTrue($annoExprs.as("@A(1) @B").matches("@B @A(1)"));
        assertFalse($annoExprs.as("@A(1) @B").matches("@A(1) @B(2)"));

        assertTrue($annoExprs.as("@A @B").matches("@A @B"));
        assertTrue($annoExprs.as("@A @B").matches("@B @A")); //order doesnt matter (for annotations)
    }

    public void test$List(){
        assertEquals( $annoExprs.of("@my$name$(key=$value$)").$list().get(0), "name");
        assertEquals( $annoExprs.of("@my$name$(key=$value$)").$list().get(1), "value");
    }

    public void test$ListNormalized(){
        assertEquals( $annoExprs.of("@my$name$(key=$value$)").$listNormalized().get(0), "name");
        assertEquals( $annoExprs.of("@my$name$(key=$value$)").$listNormalized().get(1), "value");
    }

    @interface A{}
    @interface B{}
    @interface C{}

    public void testFromClass(){
        _annoExprs _ars = _annoExprs.of("@A");
        assertTrue(_ars.is("@A"));
        $annoExprs $ars = $annoExprs.of(_ars);
        assertTrue( $ars.matches("@A"));

        _ars = _annoExprs.of(B.class);
        assertTrue( _ars.is("@B"));

        assertTrue($annoExprs.of(_annoExpr.of(B.class)).matches("@B"));

        $annoExprs.of(B.class).matches("@B");

        $annoExprs.of(_annoExpr.of(B.class)).matches("@B");
    }

    public void test$Or(){

        $annoExprs $BandC = $annoExprs.of(B.class, C.class);
        assertTrue($BandC.matches("@B @C"));
        assertTrue($BandC.matches("@C @B"));

        //EITHER BOTH A OR B and C
        $annoExprs $ars = $annoExprs.or( $annoExprs.of(A.class), $annoExprs.of(B.class, C.class));

        assertTrue($ars.matches("@A"));
        assertTrue($ars.matches("@B @C"));
        assertTrue($ars.matches("@C @B"));

        assertTrue($ars.matches("@A(1)"));
        assertTrue($ars.matches("@A(1) @B @C"));
        //$annoRefs.of(A.class, B.class, C.class);
    }

    public void testHardCode(){
        $annoExprs $ars = $annoExprs.or( $annoExprs.of("@A($value$)"), $annoExprs.of("@my$name$($key$=2)") );

        assertTrue( $ars.matches("@A(1)"));
        assertTrue( $ars.matches("@A(2)"));
        $ars.$hardcode("value", "1"); //verify only @A(1) matches
        assertTrue( $ars.matches("@A(1)"));
        assertFalse( $ars.matches("@A(2)"));
    }

}
