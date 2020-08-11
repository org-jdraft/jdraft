package org.parseout.java;

import junit.framework.TestCase;

public class PunctuationRunTest extends TestCase {

    public void testPunctuation(){
        assertEquals(0, PunctuationRun.INSTANCE.len("    "));

        assertEquals(1, PunctuationRun.INSTANCE.len("{"));
        assertEquals(1, PunctuationRun.INSTANCE.len("} "));
        assertEquals(1, PunctuationRun.INSTANCE.next("( ").cursor);

        //each punctuation is treated as a separate event
        assertEquals(1, PunctuationRun.INSTANCE.len(PunctuationRun.CHARS_STRING));

        //punctuation events are always length 1
        for(int i = 0; i< PunctuationRun.CHARS.length; i++){
            assertEquals(1, PunctuationRun.INSTANCE.len(PunctuationRun.CHARS_STRING.substring(i, i+1)));
        }
    }

    /** When we encounter punctuation,we dont get a Run, it always is processed as a run of length 1*/
    public void testPunctuationSemi(){
        assertEquals(1, PunctuationRun.Semi.INSTANCE.len(";;"));
    }

    public void testPunctuationOpen(){
        assertEquals( 1, PunctuationRun.Open.OPEN_ANGLE.len("<<"));
    }

    public void testPunctuationClose(){
        assertEquals( 1, PunctuationRun.Close.CLOSE_ANGLE.len(">>"));
    }
}
