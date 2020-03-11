package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._blockStmt;
import org.jdraft._expressionStmt;

public class $returnStmtTest extends TestCase {

    public void testReturnStmt(){
        assertTrue($returnStmt.of().matches( "return;"));
        assertTrue($returnStmt.of().matches( "return 1;"));
        assertTrue($returnStmt.of().matches( "return m();"));
        assertTrue($returnStmt.of().matches( "return new Object();"));
    }

    public void testExpressionRequiredViaLambda(){
        $returnStmt $rs = $returnStmt.of( r-> r.hasExpression() );

        assertFalse( $rs.matches( "return;"));
        assertTrue( $returnStmt.of().matches( "return 1;"));
        assertTrue( $returnStmt.of().matches( "return m();"));
        assertTrue( $returnStmt.of().matches( "return new Object();") );
    }

    public void testExact(){
        $returnStmt $rs = $returnStmt.of("return 1;");
        assertFalse( $rs.matches( "return;"));
        assertTrue( $rs.matches( "return 1;"));
        assertFalse( $rs.matches( "return 2;"));
        assertFalse( $rs.matches( "return 'c';"));
    }

    public void testModifyByEmbeddedBot(){
        $returnStmt $rs = $returnStmt.of();
        assertTrue( $rs.matches("return;"));
        assertTrue( $rs.matches("return 1;"));
        assertTrue( $rs.matches("return 2;"));
        assertTrue( $rs.matches("return 'c';"));
        $rs.$expression("1");
        assertFalse( $rs.matches("return;"));
        assertTrue( $rs.matches("return 1;"));
        assertFalse( $rs.matches("return 2;"));
        assertFalse( $rs.matches("return 'c';"));
        $rs.$expression("'c'");
        assertFalse( $rs.matches("return;"));
        assertFalse( $rs.matches("return 1;"));
        assertFalse( $rs.matches("return 2;"));
        assertTrue( $rs.matches("return 'c';"));
    }

    public void testSelect(){
        class C{
            void m(){ //1 (blockStmt)
                System.out.println( 1 ); //2
                return; //3
            }
            int i(){ //4 (blockStmt)
                System.out.println( 1 ); //5
                return 3; //6
            }
        }
        assertEquals( 6, $statement.of().countIn(C.class));
        assertEquals( 2, $statement.of(_blockStmt.class).countIn(C.class));
        assertEquals( 2, $statement.of(_expressionStmt.class).countIn(C.class));
        assertEquals(2, $returnStmt.of().countIn(C.class));
    }

}
