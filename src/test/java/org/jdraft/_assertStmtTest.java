package org.jdraft;

import junit.framework.TestCase;

public class _assertStmtTest extends TestCase {

    public void testFromN(){
        _assertStmt _as = _assertStmt.of();
        System.out.println( _as);

        _as.setCheck("true");
        _as.setMessage("Message");

        assertTrue( _as.isCheck("true"));
        assertTrue( _as.isMessage("Message"));

        _as.setCheck("(true)");
        assertTrue( _as.isCheck("(true)"));
        assertTrue( _as.isMessage(e-> e instanceof _stringExpr));
    }

}