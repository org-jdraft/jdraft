package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.Print;
import org.jdraft._binaryExpression;
import org.jdraft._string;
import org.jdraft._type;

public class $expressionRefactorTest extends TestCase {

    public void testIndRef(){
        $methodCall $print = $methodCall.of( (Object $any$)-> System.out.println($any$) );
        $expression.$refactor $toLogDebug = $print.refactorTo("Log.debug($any$);");

        $refactoring $r = $methodCall.of( (Object $any$)-> System.out.println($any$) ).$and(mc-> mc.isArgument(0, _string.class))
                .refactorTo("Log.debug($any$)");

        class FF{
            void m(){
                System.out.println("hello");
            }
        }
        _type _t = $r.in(FF.class);
        Print.tree(_t);
    }
    public void testRefactor(){
        class C{
            Integer a,b,c;
            Integer m1(){return 1;}
            Integer m2(){return 1;}
            void m(){
                assert a==b;
                assert c == m1();
                assert m1()==(m1() + m2() - m1());
            }
        }

        $expression.$refactor $r = $expression.refactor("$a$==$b$", "$a$.equals($b$)");
        _type _t = $r.in(C.class);
        Print.tree(_t);

        //verify there are NO (0) binaryExpressions that are == left in the class
        assertFalse( $e.of(_binaryExpression.class).$and(_e -> ((_binaryExpression)_e).isEqual()).isIn(_t) );

        // here I wanted to verify/add a constraint that $a$ is NOT a $literal, but i'd need to
        // have a fleshed out $binaryExpr bot to do this effectively
        //$r = $expression.refactor($e.of("$a$==$b$"), $e.of("$a$.equals($b$)"));
        //lets use Objects.equals( $a$, $b$) instead

        $r = $expression.refactor("$a$==$b$", "java.util.Object.equals($a$,$b$)");
        _t = $r.in(C.class);
        Print.tree(_t);
    }
}
