package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.Print;
import org.jdraft._type;
import org.jdraft.pattern.$;

public class $statementRefactorTest extends TestCase {

    public void test$Refactor(){
        $refactorBot $r = $.refactor("System.out.println($any$);", "Log.debug($any$);");
        class C{
            int value = 100;
            void m(){
                System.out.println("starting method");
                if( true ){
                    System.out.println("got here");
                } else{
                    System.out.println("I can pass multiple " + value + " into statement");
                }
            }
        }
        _type _t = $r.in(C.class);
        Print.tree(_t);
        //verify after refactoring I cant find those System.out.printlns
        assertFalse($statement.of("System.out.println($any$);").isIn(_t));
    }
    public void testRefactor() {
        $statement.$refactor $r = $statement.refactor("System.out.println($any$);", "Log.debug($any$);");
        class C{
            int value = 100;
            void m(){
                System.out.println("starting method");
                if( true ){
                    System.out.println("got here");
                } else{
                    System.out.println("I can pass multiple "+value+" into statement");
                }
            }
        }
        _type _t = $r.in(C.class);
        Print.tree(_t);
    }
}