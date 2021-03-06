package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._anno;

public class $annoExprTest extends TestCase {

    @interface P{
        int a() default 3;
        int b() default 4;
    }

    public void testAnyPair(){
        $annoEntryPair $p = $annoEntryPair.of();
        assertTrue( $p.matches("1") );
        assertTrue( $p.matches("key=1") );

        class C{
            @P(a=1,b=2)
            int i;
        }
        assertEquals(2, $annoEntryPair.of().countIn(C.class));
        assertEquals(1, $annoEntryPair.of("a",1).countIn(C.class));
        assertEquals(1, $annoEntryPair.of("b",2).countIn(C.class));

        class D{
            @P
            int i;
        }
        assertEquals(0, $p.countIn(D.class));
    }

    public void testHardCode(){
        $anno $ar = $anno.of("@A($value$)");

        assertNotNull($ar.name.selectFrom(_anno.of("@A")));

        assertTrue( $ar.matches( "@A(1)"));
        assertTrue( $ar.matches( "@A(2)"));
        $ar.$hardcode("value", 1);

        assertNull( $ar.entryPairs.getMatchAllName() );

        assertTrue( $ar.matches( "@A(1)"));
        assertFalse( $ar.matches( "@A(2)"));
    }

    public void testHardCodeOr(){
        $anno $A = $anno.of("@A($value$)");
        assertTrue( $A.matches( "@A(1)"));
        assertTrue( $A.matches( "@A(k=1,m=4)"));

        $anno $aror = $anno.or( $anno.of("@A($value$)"), $anno.of("@my$name$($key$=2)") );
        assertTrue( $aror.matches("@A(1)"));
        assertTrue( $aror.matches("@A(2)"));
        $aror = $aror.$hardcode("value", "1"); //verify only @A(1) matches
        assertTrue( $aror.matches("@A(1)"));
        assertFalse( $aror.matches("@A(2)"));
    }

    public void testMatchAny() {
        $anno $ar = $anno.of();
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testSimple(){
        $anno $ar = $anno.of("A");

        _anno _ae = _anno.of("A");
        assertNotNull( $ar.name.selectFrom(_ae));
        assertNotNull( $ar.entryPairs.selectFrom(_ae));
        assertTrue( $ar.name.bot.matches("A"));
        assertTrue( $ar.entryPairs.selectFrom(_anno.of("@A")) != null);
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testInteresting(){
        $anno $ar = $anno.of("@A($keyValue$)");
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testParameterized(){
        $anno $ar = $anno.of("@$name$");
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );

        $ar = $anno.of("@A($keyValue$)");
        assertTrue( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );

        $ar = $anno.of("@A(key=$value$)");
        assertFalse( $ar.matches("@A") );
        assertFalse( $ar.matches("@A(1)") );
        assertTrue( $ar.matches("@A(key=1)") );
        assertTrue( $ar.matches("@A(key=1,value=2)") );
    }

    public void testMV(){
        $anno $ar = $anno.of("@A(key=$value$)");
        assertFalse( $ar.matches("@A(1)") );

    }
    public void testValueSingle(){
        $anno $ar = $anno.as("A(1)");
       // assertTrue($ar.$mvs.get(0).matches("1"));
        //assertTrue($ar.$mvs.get(0).matches("value=1"));
        assertFalse($ar.entryPairs.getBot(0).matches("key=1"));

    }
    public void testAs(){
        $anno $ar = $anno.as("A");
        assertTrue( $ar.matches("@A") );
        assertFalse( $ar.matches("@A(1)") );
        assertFalse( $ar.matches("@A(key=1)") );
        assertFalse( $ar.matches("@A(key=1,value=2)") );

        $ar = $anno.as("A(key=1)");
        assertTrue($ar.entryPairs.getBot(0).matches("key=1"));
        assertFalse($ar.entryPairs.getBot(0).matches("key=2"));
        assertFalse($ar.entryPairs.getBot(0).matches("value=1"));


        $ar = $anno.as("A(1)");
        assertFalse( $ar.matches("@A") );
        assertTrue( $ar.matches("@A(1)") );
        assertFalse( $ar.matches("@A(2)") );
        assertFalse( $ar.matches("@A(key=1)") );
        assertFalse( $ar.matches("@A(key=1,value=2)") );
    }
}
