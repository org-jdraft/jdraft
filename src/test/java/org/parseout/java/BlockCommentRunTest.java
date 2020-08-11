package org.parseout.java;

import junit.framework.TestCase;

public class BlockCommentRunTest extends TestCase {

    public void testBlockComment(){
        CommentRun.BlockCommentRun bc = CommentRun.BlockCommentRun.INSTANCE;
        assertEquals(0, bc.next("abcded").cursor);
        assertEquals(4, bc.next("/**/").cursor); //no closing delimiter
        assertEquals(5, bc.next("/*\n*/").cursor); //with closing delimiter
        assertEquals(18, bc.next("/*block comment\n*/").cursor); //with closing delimiter

        assertEquals(19, bc.next("/*block comment\n*/ ").cursor); //with closing delimiter & space

    }
}
