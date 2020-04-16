package org.jdraft.bot;

import junit.framework.TestCase;

public class SexpressionStmtTest extends TestCase {

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
        assertEquals(3, $statement.of( (Object $any$)-> System.out.println($any$) ).countIn(GHJ.class));
        assertEquals(2, $statement.of( ()-> System.out.println(1) ).countIn(GHJ.class));

        //using explicit $expressionStmt
        assertEquals(3, $expressionStmt.of( (Object $any$)-> System.out.println($any$) ).countIn(GHJ.class));
        assertEquals(2, $expressionStmt.of( ()-> System.out.println(1) ).countIn(GHJ.class));
        assertEquals(1, $expressionStmt.of( ()-> System.out.println(2) ).countIn(GHJ.class));
    }
}
