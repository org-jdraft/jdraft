package org.parseout;

import junit.framework.TestCase;
import org.parseout.run.DigitsRun;

public class DigitsRunTest extends TestCase {

    public void testDQ(){
        DigitsRun n = DigitsRun.INSTANCE;
        assertEquals(0, n.len("****"));
        assertEquals(0, n.next("****").cursor);

        assertEquals(1, n.len("1"));
        assertEquals(3, n.len(new State("123")));

        assertEquals(4, n.next(new State("123 ")).cursor);
    }
}
