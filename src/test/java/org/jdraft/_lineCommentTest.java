package org.jdraft;

import junit.framework.TestCase;

public class _lineCommentTest extends TestCase {

    public void testLineComment(){
        //dont use the prefix, it'll be added
        _lineComment _lc = _lineComment.of(" line comment");
        assertEquals( "// line comment"+System.lineSeparator(), _lc.toString());

        //or use the prefix
        _lc = _lineComment.of("// line comment");
        assertEquals( "// line comment"+System.lineSeparator(), _lc.toString());
    }
}
