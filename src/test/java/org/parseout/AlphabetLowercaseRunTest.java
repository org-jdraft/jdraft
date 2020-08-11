package org.parseout;

import junit.framework.TestCase;
import org.parseout.run.AlphabetLowercaseRun;

public class AlphabetLowercaseRunTest extends TestCase {

    public void testAlphaStart(){
        AlphabetLowercaseRun ab = AlphabetLowercaseRun.INSTANCE;
        assertEquals(0, ab.len("12345"));
        assertEquals(7, ab.len("abcdefg"));
        assertEquals(8, ab.next("abcdefg ").cursor);//trailing space
        assertEquals(8, ab.next("abcdefg ").cursor);//trailing space
        assertEquals(26, ab.next("abcdefghijklmnopqrstuvwxyz").cursor);

        //test DelimitedByNumber
        assertEquals(7, ab.next("abcdefg1").cursor);

        //delimited by Symbol
        assertEquals(7, ab.next("abcdefg.").cursor);
    }

    public void testAlphaMiddle(){

        AlphabetLowercaseRun ab = AlphabetLowercaseRun.INSTANCE;
        assertEquals(7, ab.len(new State("          abcdefg", 10)));
        assertEquals(17, ab.next(new State("          abcdefg", 10)).cursor);
        assertEquals(18, ab.next(new State("          abcdefg ", 10)).cursor);//trailing space
        assertEquals(18, ab.next(new State("          abcdefg ", 10)).cursor);//trailing space
        assertEquals(36, ab.next(new State("          abcdefghijklmnopqrstuvwxyz",10)).cursor);

        //test DelimitedByNumber
        assertEquals(17, ab.next(new State("          abcdefg1", 10)).cursor);

        //delimited by Symbol
        assertEquals(17, ab.next(new State("          abcdefg.", 10)).cursor);
    }


}
