package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft._enum;

public class SenumConstantTest extends TestCase {

    enum E{
        A,
        @Deprecated  B(),
        C{
            public String toString(){
                return "CEE";
            }
        }
    }
    public void testMatchOfEnumConstants(){
        //_enum._constant _ec = _enum._constant.of("A{ public String toString(){ return \"A\"; } }");
        //System.out.println(_ec);

        //match any
        $enumConstant $ec = $enumConstant.of();
        assertTrue($ec.matches("A"));
        assertTrue($ec.matches("@Deprecated A"));
        assertTrue($ec.matches("A()"));
        assertTrue($ec.matches("A(1)"));
        assertTrue($ec.matches("A(1, 2)"));
        assertTrue($ec.matches("A{ public String toString(){ return \"Ayyy\"; } } "));

        //match any name
        $ec = $enumConstant.of("$name$");
        assertTrue($ec.matches("A"));
        assertTrue($ec.matches("@Deprecated A"));
        assertTrue($ec.matches("A()"));
        assertTrue($ec.matches("A(1)"));
        assertTrue($ec.matches("A(1,2)"));
        assertTrue($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));
        assertTrue($ec.matches("B"));

        //match of any with name
        $ec = $enumConstant.of("A");
        assertTrue($ec.matches("A"));
        assertTrue($ec.matches("@Deprecated A"));
        assertTrue($ec.matches("A()"));
        assertTrue($ec.matches("A(1)"));
        assertTrue($ec.matches("A(1,2)"));
        assertTrue($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));

        assertFalse($ec.matches("B"));

        $ec = $enumConstant.of("A(1)");
        assertFalse($ec.matches("A"));
        assertFalse($ec.matches("A()"));
        assertTrue($ec.matches("A(1)"));
        assertTrue($ec.matches("@Deprecated A(1)"));
        assertTrue($ec.matches("A(1,2)"));
        assertTrue($ec.matches("A(2,1)"));
        assertFalse($ec.matches("A(2)"));
        assertFalse($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));

        $ec = $enumConstant.of("@Deprecated A()");
        assertFalse($ec.matches("A"));
        assertFalse($ec.matches("A()"));
        assertFalse($ec.matches("A(1)"));
        assertTrue($ec.matches("@Deprecated A"));
        assertTrue($ec.matches("@Deprecated A()"));
        assertTrue($ec.matches("@Deprecated A(1,2)"));
        assertFalse($ec.matches("A(2,1)"));
        assertFalse($ec.matches("A(2)"));
        assertFalse($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));

        $ec = $enumConstant.of("@Deprecated A(1){ public final int $name$=100; }");
        assertFalse($ec.matches("A"));
        assertFalse($ec.matches("A()"));
        assertFalse($ec.matches("A(1)"));
        assertFalse($ec.matches("@Deprecated A"));
        assertFalse($ec.matches("@Deprecated A()"));
        assertFalse($ec.matches("@Deprecated A(1,2)"));
        assertFalse($ec.matches("A(2,1)"));
        assertFalse($ec.matches("A(2)"));
        assertFalse($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));

        assertTrue( $ec.matches("@Deprecated A(1){ public final int any=100; }"));
        assertTrue( $ec.matches("@Deprecated @Ann A(1, 2){ public static final int any=100; }"));
    }

    public void testAsMatch(){
        //match any name
        $enumConstant $ec = $enumConstant.as("$name$");
        assertTrue($ec.matches("A"));
        assertFalse($ec.matches("@Deprecated A"));
        assertTrue($ec.matches("A()"));
        assertFalse($ec.matches("A(1)"));
        assertFalse($ec.matches("A(1,2)"));
        assertFalse($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));
        assertTrue($ec.matches("B"));

        //match of any with name
        $ec = $enumConstant.as("A");
        assertTrue($ec.matches("A"));
        assertFalse($ec.matches("@Deprecated A"));
        assertTrue($ec.matches("A()"));
        assertFalse($ec.matches("A(1)"));
        assertFalse($ec.matches("A(1,2)"));
        assertFalse($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));

        assertFalse($ec.matches("B"));

        $ec = $enumConstant.as("A(1)");
        assertFalse($ec.matches("A"));
        assertFalse($ec.matches("A()"));
        assertTrue($ec.matches("A(1)"));
        assertFalse($ec.matches("@Deprecated A(1)"));
        assertFalse($ec.matches("A(1,2)"));
        assertFalse($ec.matches("A(2,1)"));
        assertFalse($ec.matches("A(2)"));
        assertFalse($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));

        $ec = $enumConstant.as("A(1,2)");
        assertFalse($ec.matches("A"));
        assertFalse($ec.matches("A()"));
        assertFalse($ec.matches("A(1)"));
        assertTrue($ec.matches("A(1,2)"));
        assertFalse($ec.matches("A(2,1)"));
        assertFalse($ec.matches("@Deprecated A(1,2)"));
        assertFalse($ec.matches("A(1,2){ int x = 100; }"));

        $ec = $enumConstant.as("@Deprecated A()");
        assertFalse($ec.matches("A"));
        assertFalse($ec.matches("A()"));
        assertFalse($ec.matches("A(1)"));
        assertTrue($ec.matches("@Deprecated A"));
        assertTrue($ec.matches("@Deprecated A()"));
        assertFalse($ec.matches("@Deprecated A(1,2)"));
        assertFalse($ec.matches("A(2,1)"));
        assertFalse($ec.matches("A(2)"));
        assertFalse($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));

        $ec = $enumConstant.as("@Deprecated A(1){ public final int $name$=100; }");
        assertFalse($ec.matches("A"));
        assertFalse($ec.matches("A()"));
        assertFalse($ec.matches("A(1)"));
        assertFalse($ec.matches("@Deprecated A"));
        assertFalse($ec.matches("@Deprecated A()"));
        assertFalse($ec.matches("@Deprecated A(1,2)"));
        assertFalse($ec.matches("A(2,1)"));
        assertFalse($ec.matches("A(2)"));
        assertFalse($ec.matches("A{ public String toString(){ return \"Ayyy\"; } }"));

        assertTrue( $ec.matches("@Deprecated A(1){ public final int any=100; }"));
        assertFalse( $ec.matches("@Deprecated @Ann A(1, 2){ public static final int any=100; }"));
    }
}
