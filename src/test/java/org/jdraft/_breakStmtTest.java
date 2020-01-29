package org.jdraft;

import junit.framework.TestCase;

public class _breakStmtTest extends TestCase {
    public void testBS(){
        _breakStmt _b = _breakStmt.of();
        assertNull( _b.getLabel());
    }
}
