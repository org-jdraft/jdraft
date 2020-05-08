package org.jdraft.bot;

import junit.framework.TestCase;

public class $annoRefTest extends TestCase {


    public void testHardCode(){
        $annoRef $ar = $annoRef.of("@A($value$)");
        assertTrue( $ar.matches( "@A(1)"));
        assertTrue( $ar.matches( "@A(2)"));
        $ar.$hardcode("value", 1);

        assertTrue( $ar.matches( "@A(1)"));
        assertFalse( $ar.matches( "@A(2)"));
    }

    public void testHardCodeOr(){
        $annoRef $aror = $annoRef.or( $annoRef.of("@A($value$)"), $annoRef.of("@my$name$($key$=2)") );
        assertTrue( $aror.matches("@A(1)"));
        assertTrue( $aror.matches("@A(2)"));
        $aror = $aror.$hardcode("value", "1"); //verify only @A(1) matches
        assertTrue( $aror.matches("@A(1)"));
        assertFalse( $aror.matches("@A(2)"));
    }

    public void testMatchAny() {
        $annoRef $ar = $annoRef.of();
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testSimple(){
        $annoRef $ar = $annoRef.of("A");
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testInteresting(){
        $annoRef $ar = $annoRef.of("@A($keyValue$)");
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testParameterized(){
        $annoRef $ar = $annoRef.of("@$name$");
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );

        $ar = $annoRef.of("@A($keyValue$)");
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );

        $ar = $annoRef.of("@A(key=$value$)");
        assertFalse( $ar.matches("@A") );
        assertFalse( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testMV(){
        $annoRef $ar = $annoRef.of("@A(key=$value$)");
        assertFalse( $ar.matches("@A(1)") );

    }
    public void testValueSingle(){
        $annoRef $ar = $annoRef.as("A(1)");
       // assertTrue($ar.$mvs.get(0).matches("1"));
        //assertTrue($ar.$mvs.get(0).matches("value=1"));
        assertFalse($ar.$mvs.get(0).matches("key=1"));

    }
    public void testAs(){
        $annoRef $ar = $annoRef.as("A");
        assertTrue( $ar.matches("@A") );
        assertFalse( $ar.matches("@A(1)") );
        assertFalse( $ar.matches("@A(key=1)") );
        assertFalse( $ar.matches("@A(key=1,value=2)") );

        $ar = $annoRef.as("A(key=1)");
        assertTrue($ar.$mvs.get(0).matches("key=1"));
        assertFalse($ar.$mvs.get(0).matches("key=2"));
        assertFalse($ar.$mvs.get(0).matches("value=1"));


        $ar = $annoRef.as("A(1)");
        assertFalse( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertFalse( $ar.matches("@A(2)") );
        assertFalse( $ar.matches("@A(key=1)") );
        assertFalse( $ar.matches("@A(key=1,value=2)") );
    }
}
