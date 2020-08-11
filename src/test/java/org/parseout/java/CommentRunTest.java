package org.parseout.java;

import junit.framework.TestCase;

public class CommentRunTest extends TestCase {

    public void testComment(){
        CommentRun com = CommentRun.INSTANCE;
        //pure "block comment"
        assertEquals(0, com.next("abcded").cursor);
        assertEquals(4, com.next("/**/").cursor); //no closing delimiter
        assertEquals(5, com.next("/*\n*/").cursor); //with closing delimiter
        assertEquals(18, com.next("/*block comment\n*/").cursor); //with closing delimiter
        assertEquals(19, com.next("/*block comment\n*/ ").cursor); //with closing delimiter & space

        //a "javadoc comment" is really just a block comment with an extra * at beginning
        assertEquals(5, com.next("/***/").cursor); //no closing delimiter
        assertEquals(6, com.next("/**\n*/").cursor); //with closing delimiter
        assertEquals(19, com.next("/**block comment\n*/").cursor); //with closing delimiter
        assertEquals(20, com.next("/**block comment\n*/ ").cursor); //with closing delimiter & space

        assertEquals(2, com.next("//").cursor); //no closing delimiter
        assertEquals(3, com.next("//\n").cursor); //with closing delimiter
        assertEquals(15, com.next("//line comment\n").cursor); //with closing delimiter
    }
}
