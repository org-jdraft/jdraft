package org.jdraft.bot;

import junit.framework.TestCase;

public class $exprStmtTest extends TestCase {

    public void testEX(){
        class GHJ{
            void m(){
                System.out.println(1);
                System.out.println(2);
            }
            GHJ(){
                System.out.println(1);
            }
        }
        //here using $statement
        assertEquals(3, $stmt.of( (Object $any$)-> System.out.println($any$) ).countIn(GHJ.class));
        assertEquals(2, $stmt.of( ()-> System.out.println(1) ).countIn(GHJ.class));

        //using explicit $expressionStmt
        assertEquals(3, $exprStmt.of( (Object $any$)-> System.out.println($any$) ).countIn(GHJ.class));
        assertEquals(2, $exprStmt.of( ()-> System.out.println(1) ).countIn(GHJ.class));
        assertEquals(1, $exprStmt.of( ()-> System.out.println(2) ).countIn(GHJ.class));
    }
}
