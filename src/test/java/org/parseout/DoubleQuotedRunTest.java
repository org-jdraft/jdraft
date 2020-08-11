package org.parseout;

import junit.framework.TestCase;
import org.parseout.run.DoubleQuotedRun;

public class DoubleQuotedRunTest extends TestCase {

    public void testDQ(){
        DoubleQuotedRun dql = DoubleQuotedRun.INSTANCE;
        assertEquals(0, dql.len("123123"));
        assertEquals(3, dql.len(new State("\"a\"")));

        assertEquals(3, dql.len(new State("\"a\" ")));

        //escaped end delimiter
        assertEquals(5, dql.len("\"a\\\"\""));

        assertEquals(4, dql.next(new State("\"a\" ")).cursor);

        try{
            dql.len("\"");
            fail("Expected exception");
        }catch(Exception e){
            //expected
        }
    }

    public void testE(){
        assertEquals( 7, DoubleQuotedRun.INSTANCE.len( "\"quote\"" ));
        assertEquals( 7, DoubleQuotedRun.INSTANCE.len( new State(" \"quote\"",1)  ));
        State st = DoubleQuotedRun.INSTANCE.next("\"quote\"" );
        assertEquals( 7, st.cursor);
    }

    public void testMid(){
        State s = DoubleQuotedRun.INSTANCE.next( new State("public static final @_ANN(1,2) @a2(\"quote\"", 35));
        System.out.println(s.cursor);
        System.out.println( s );
    }
}
