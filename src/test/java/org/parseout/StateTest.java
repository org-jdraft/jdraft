package org.parseout;

import junit.framework.TestCase;
import org.parseout.java.JavaParse;

public class StateTest extends TestCase {

    public void testState(){
        State s = new State("abcdefghij");
        assertEquals(0, s.getLine());
        assertEquals(0, s.getColumn());

        assertTrue( s.isAt("a"));
        assertTrue( s.isAt("ab"));
        assertTrue( s.isAt("abc"));
        assertTrue( s.isAt("abcdefghij"));

        assertFalse(s.isAt("b")); //not match
        assertFalse(s.isAt("abcdefghijk")); //overflow

        JavaParse.INSTANCE.next(s);
        assertEquals(1, s.getLine());
        assertEquals(10, s.getColumn());

        s = JavaParse.INSTANCE.next("\n\n\n\n\n\n");
        assertEquals( 6, s.getLine());
        assertEquals( 1, s.getColumn());
    }

    public void testIsAt(){

    }
}
