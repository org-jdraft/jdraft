package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.Print;
import org.jdraft._binaryExpr;
import org.jdraft._stringExpr;
import org.jdraft._type;
import org.jdraft.pattern.$;

public class $exprRefactorTest extends TestCase {

    public void test$Refactor(){
        class MyClass{
            int x,y,z;
            boolean b = (x==y);
            boolean c = y==z;
        }
        Print.tree( $.refactor("$x$==$y$", "$x$.equals($y$)").in(MyClass.class) );
    }

    public void testIndRef(){
        $methodCallExpr $print = $methodCallExpr.of( (Object $any$)-> System.out.println($any$) );
        $expr.$refactor $toLogDebug = $print.refactor("Log.debug($any$);");

        $refactoring $r = $methodCallExpr.of( (Object $any$)-> System.out.println($any$) ).$and(mc-> mc.isArg(0, _stringExpr.class))
                .refactor("Log.debug($any$)");

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

        $expr.$refactor $r = $expr.refactor("$a$==$b$", "$a$.equals($b$)");
        _type _t = $r.in(C.class);
        Print.tree(_t);

        //verify there are NO (0) binaryExpressions that are == left in the class
        assertFalse( $e.of(_binaryExpr.class).$and(_e -> ((_binaryExpr)_e).isEqual()).isIn(_t) );

        // here I wanted to verify/add a constraint that $a$ is NOT a $literal, but i'd need to
        // have a fleshed out $binaryExpr bot to do this effectively
        //$r = $expression.refactor($e.of("$a$==$b$"), $e.of("$a$.equals($b$)"));
        //lets use Objects.equals( $a$, $b$) instead

        $r = $expr.refactor("$a$==$b$", "java.util.Object.equals($a$,$b$)");
        _t = $r.in(C.class);
        Print.tree(_t);
    }
}
