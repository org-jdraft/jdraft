package org.jdraft;

import junit.framework.TestCase;

public class _breakStmtTest extends TestCase {
    public void testBS(){
        _breakStmt _b = _breakStmt.of();
        assertNull( _b.getLabel());
    }

    //$name
    //$ref

    //isBreakLabel
    //isContinueLabel
    //isLabelStatementLabel

    public void testBreakLabelComment(){
        _breakStmt _bs = _breakStmt.of("break a;");


        _bs.getLabel();
        assertNull(_bs.getComment());
        _bs.setComment(_lineComment.of("Line Comment"));
        assertEquals(_bs.getComment(), _lineComment.of("Line Comment"));

    }
}
