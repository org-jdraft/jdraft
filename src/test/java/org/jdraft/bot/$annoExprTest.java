package org.jdraft.bot;

import junit.framework.TestCase;

public class $annoExprTest extends TestCase {


    public void testHardCode(){
        $annoExpr $ar = $annoExpr.of("@A($value$)");
        assertTrue( $ar.matches( "@A(1)"));
        assertTrue( $ar.matches( "@A(2)"));
        $ar.$hardcode("value", 1);

        assertTrue( $ar.matches( "@A(1)"));
        assertFalse( $ar.matches( "@A(2)"));
    }

    public void testHardCodeOr(){
        $annoExpr $aror = $annoExpr.or( $annoExpr.of("@A($value$)"), $annoExpr.of("@my$name$($key$=2)") );
        assertTrue( $aror.matches("@A(1)"));
        assertTrue( $aror.matches("@A(2)"));
        $aror = $aror.$hardcode("value", "1"); //verify only @A(1) matches
        assertTrue( $aror.matches("@A(1)"));
        assertFalse( $aror.matches("@A(2)"));
    }

    public void testMatchAny() {
        $annoExpr $ar = $annoExpr.of();
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testSimple(){
        $annoExpr $ar = $annoExpr.of("A");
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testInteresting(){
        $annoExpr $ar = $annoExpr.of("@A($keyValue$)");
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testParameterized(){
        $annoExpr $ar = $annoExpr.of("@$name$");
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );

        $ar = $annoExpr.of("@A($keyValue$)");
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );

        $ar = $annoExpr.of("@A(key=$value$)");
        assertFalse( $ar.matches("@A") );
        assertFalse( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testMV(){
        $annoExpr $ar = $annoExpr.of("@A(key=$value$)");
        assertFalse( $ar.matches("@A(1)") );

    }
    public void testValueSingle(){
        $annoExpr $ar = $annoExpr.as("A(1)");
       // assertTrue($ar.$mvs.get(0).matches("1"));
        //assertTrue($ar.$mvs.get(0).matches("value=1"));
        assertFalse($ar.pairs.get(0).matches("key=1"));

    }
    public void testAs(){
        $annoExpr $ar = $annoExpr.as("A");
        assertTrue( $ar.matches("@A") );
        assertFalse( $ar.matches("@A(1)") );
        assertFalse( $ar.matches("@A(key=1)") );
        assertFalse( $ar.matches("@A(key=1,value=2)") );

        $ar = $annoExpr.as("A(key=1)");
        assertTrue($ar.pairs.get(0).matches("key=1"));
        assertFalse($ar.pairs.get(0).matches("key=2"));
        assertFalse($ar.pairs.get(0).matches("value=1"));


        $ar = $annoExpr.as("A(1)");
        assertFalse( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertFalse( $ar.matches("@A(2)") );
        assertFalse( $ar.matches("@A(key=1)") );
        assertFalse( $ar.matches("@A(key=1,value=2)") );
    }
}
