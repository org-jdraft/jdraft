package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.text.Stencil;

public class _breakStmtTest extends TestCase {

    public void testBS(){
        _breakStmt _b = _breakStmt.of();
        assertNull( _b.getLabel());
        assertFalse( _b.hasLabel() );
        assertFalse(_b.isLabel(Stencil.of("hey")));
        assertFalse( _b.isLabel("Yo"));

        _b.setLabel("ChaChing");
        assertEquals( "ChaChing", _b.getLabel());
        assertTrue( _b.hasLabel());
        assertTrue(_b.isLabel("ChaChing"));
        assertTrue( _b.isLabel(l-> l.startsWith("Cha")) );
    }

    public void testBreakLabelComment(){
        _breakStmt _bs = _breakStmt.of("break a;");


        _bs.getLabel();
        assertNull(_bs.getComment());
        _bs.setComment(_lineComment.of("Line Comment"));
        assertEquals(_bs.getComment(), _lineComment.of("Line Comment"));

    }
}
