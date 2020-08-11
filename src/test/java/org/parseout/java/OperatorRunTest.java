package org.parseout.java;

import junit.framework.TestCase;

public class OperatorRunTest extends TestCase {

    public void testSymbol(){
        assertEquals(0, OperatorRun.INSTANCE.len("    "));

        assertEquals(1, OperatorRun.INSTANCE.len("?"));
        assertEquals(1, OperatorRun.INSTANCE.len("+ "));
        assertEquals(2, OperatorRun.INSTANCE.next("+ ").cursor);
        //make sure each symbol in sequence
        assertEquals(OperatorRun.CHARS_STRING.length(), OperatorRun.INSTANCE.len(OperatorRun.CHARS_STRING));

        //make sure it handles whitespace at end
        assertEquals(OperatorRun.CHARS_STRING.length() +1 , OperatorRun.INSTANCE.next(OperatorRun.CHARS_STRING+ " ").cursor);
    }
}
