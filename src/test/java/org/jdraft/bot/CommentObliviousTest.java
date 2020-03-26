package org.jdraft.bot;

import junit.framework.TestCase;

import java.util.function.Predicate;

/**
 * make sure I can match things with patterns that is comment oblivious
 * i.e. I can match against
 */
public class CommentObliviousTest extends TestCase {
    public void testCom(){
        class FF{
            int i = /*block comment*/ 11;
            /**
             * a javadoc
             */
            public void blah(){
                /** javadoc comment */
                System.out.println("javadoc comment");
                /* block comment */
                System.out.println("block comment");
                // line comment
                System.out.println("line comment");

                // a comment
                return;
            }

            void also(){

                return;
            }
        }
        //$int.of().printIn(FF.class);
        assertEquals( 1, $int.of("11").countIn(FF.class));

        $methodCall.of().printIn(FF.class);
        $returnStmt.of("return;").printIn(FF.class);
        assertEquals(2, $returnStmt.of("return;").countIn(FF.class));

    }

}
