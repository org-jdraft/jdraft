package org.jdraft.bot;

import junit.framework.TestCase;

public class $assertStmtTest extends TestCase {

    public void testA(){
        assertTrue( $assertStmt.of().matches("assert true;") );
        assertTrue( $assertStmt.of().matches("assert true : \"message\";") );

        assertTrue($assertStmt.of().$and(a-> a.hasMessage()).matches("assert true : \"message\";"));

        assertTrue($assertStmt.of("assert (1==1);").matches("assert(1==1);"));
        assertTrue($assertStmt.of("assert (1==1);").matches("assert ( 1 == 1 );")); //spacing doesn't matter

        assertTrue( $assertStmt.of().$check("1==1").matches( "assert 1==1;") );
        assertTrue( $assertStmt.of().$message("\"message\"").matches( "assert 1==1: \"message\";") );

        //if the message is \"hi\" or the check = (1==1)
        $assertStmt $aor = $assertStmt.or ($assertStmt.of().$message("\"hi\""), $assertStmt.of().$check("1==1") );

        //$hasComment($comment)
        //$hasComment(

        assertTrue($aor.matches("assert 1==1: \"hi\"")); //both true
        assertTrue($aor.matches("assert 1==1")); //
        assertTrue($aor.matches("assert true: \"hi\""));

        assertTrue($aor.matches("/*comment*/ assert true: \"hi\"")); //both match and comment

        $aor.$hasComment($comment.of("comment"));

        assertTrue($aor.matches("/*comment*/ assert true: \"hi\";")); //both match and comment
        assertTrue($aor.matches("/*comment*/ assert 1==1;")); //one match and comment
        assertTrue($aor.matches("/*comment*/ assert 1==1: \"hi\";")); //one and comment

        assertFalse($aor.matches("/*weiner*/ assert 1==1: \"hi\";")); //one and comment


    }
}
