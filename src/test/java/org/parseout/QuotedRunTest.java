package org.parseout;

import junit.framework.TestCase;
import org.parseout.run.QuotedRun;

public class QuotedRunTest extends TestCase {

    public void testDQ(){
        QuotedRun qt = QuotedRun.INSTANCE;
        assertEquals(0, qt.len("123123"));
        assertEquals(3, qt.len(new State("'a'")));

        assertEquals(3, qt.len(new State("'a' ")));
        assertEquals(4, qt.next(new State("'a' ")).cursor);

        try{
            qt.len("'");
            fail("Expected exception");
        }catch(Exception e){
            //expected
        }
    }
}
