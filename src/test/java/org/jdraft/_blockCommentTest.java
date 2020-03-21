package org.jdraft;

import junit.framework.TestCase;

public class _blockCommentTest extends TestCase {

    public void testBlockComment(){
        _blockComment _bc = _blockComment.of(" block comment ");
        assertEquals( "/* block comment */"+System.lineSeparator(), _bc.toString());

        _bc = _blockComment.of("/* block comment */");
        assertEquals( "/* block comment */"+System.lineSeparator(), _bc.toString());

    }
}
