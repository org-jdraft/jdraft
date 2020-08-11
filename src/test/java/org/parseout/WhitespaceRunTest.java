package org.parseout;

import junit.framework.TestCase;
import org.parseout.run.WhitespaceRun;

public class WhitespaceRunTest extends TestCase {

    public void testWS(){
        WhitespaceRun ws = new WhitespaceRun();
        assertEquals(0, ws.len(""));
        assertEquals(0, ws.len("abcdefg"));

        assertEquals(1, ws.len(" "));
        assertEquals(1, ws.len("\n"));
        assertEquals(1, ws.len("\t"));
        assertEquals(1, ws.len("\r"));

        assertEquals( 4, ws.len(" \t\r\n"));
        assertEquals( 4, ws.len(" \t\r\nabcdefg"));
    }
}
