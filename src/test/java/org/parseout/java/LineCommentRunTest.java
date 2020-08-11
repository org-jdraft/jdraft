package org.parseout.java;

import junit.framework.TestCase;

public class LineCommentRunTest extends TestCase {

    public void testLineComment(){
        CommentRun.LineCommentRun lc = CommentRun.LineCommentRun.INSTANCE;
        assertEquals(0, lc.next("abcded").cursor);
        assertEquals(2, lc.next("//").cursor); //no closing delimiter
        assertEquals(3, lc.next("//\n").cursor); //with closing delimiter
        assertEquals(15, lc.next("//line comment\n").cursor); //with closing delimiter
    }

}
