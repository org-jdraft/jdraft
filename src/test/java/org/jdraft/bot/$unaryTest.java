package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._expr;
import org.jdraft._unaryExpr;

public class $unaryTest extends TestCase {

    public void testMatchAny() {
        $unaryExpr $u = $unaryExpr.of();
        assertTrue($u.matches("-a"));
        assertTrue($u.matches("+a"));
        assertTrue($u.matches("!true"));
        assertTrue($u.matches("a++"));
        assertTrue($u.matches("a--"));
        assertTrue($u.matches("++a"));
        assertTrue($u.matches("--a"));
        assertTrue($u.matches("~a"));
    }

    public void testCopy(){

        assertTrue( $unaryExpr.of(_unaryExpr.MINUS).matches("-e") );
        assertFalse( $unaryExpr.of(_unaryExpr.MINUS).matches("~e") );
        assertTrue( $unaryExpr.of(_unaryExpr.MINUS).copy().matches("-e") );
        assertFalse( $unaryExpr.of(_unaryExpr.MINUS).copy().matches("~e") );

        //predicate
        assertFalse( $unaryExpr.of().$not(_unaryExpr.MINUS).matches("-e") );
        assertFalse( $unaryExpr.of().$not(_unaryExpr.MINUS).copy().matches("-e") );
        assertTrue( $unaryExpr.of().$not(_unaryExpr.MINUS).matches("!e") );
        assertTrue( $unaryExpr.of().$not(_unaryExpr.MINUS).copy().matches("!e") );

        assertTrue($unaryExpr.minus().matches("-t"));

        //expression
        assertTrue( $unaryExpr.of(_expr.of("e")).$not(_unaryExpr.MINUS).copy().matches("!e") );
        assertFalse( $unaryExpr.of(_expr.of("e")).$not(_unaryExpr.MINUS).copy().matches("-e") );
    }

    public void testByOperator(){
        assertTrue( $unaryExpr.bitwiseComplement().matches("~a"));
        assertFalse( $unaryExpr.bitwiseComplement().matches("a++"));

        assertTrue( $unaryExpr.preIncrement().matches("++a"));
        assertFalse( $unaryExpr.preIncrement().matches("a++"));

        assertTrue( $unaryExpr.preDecrement().matches("--a"));
        assertFalse( $unaryExpr.preDecrement().matches("a--"));

        assertFalse( $unaryExpr.postIncrement().matches("++a"));
        assertTrue( $unaryExpr.postIncrement().matches("a++"));

        assertFalse( $unaryExpr.postDecrement().matches("--a"));
        assertTrue( $unaryExpr.postDecrement().matches("a--"));

        assertTrue( $unaryExpr.minus().matches("-a"));
        assertFalse( $unaryExpr.minus().matches("+a"));

        assertFalse( $unaryExpr.plus().matches("-a"));
        assertTrue( $unaryExpr.plus().matches("+a"));

        assertTrue( $unaryExpr.logicalComplement().matches("!a"));
        assertFalse( $unaryExpr.logicalComplement().matches("+a"));
    }

    public void testOrPostIT(){
        $unaryExpr $plusMinusLiteral =
                $unaryExpr.or( $unaryExpr.plus(), $unaryExpr.minus() ).$expression(_expr._literal.class);

        assertTrue( $plusMinusLiteral.matches("+1") );
        assertTrue( $plusMinusLiteral.matches("-1") );
        assertFalse( $plusMinusLiteral.matches("~1") );

        //the
        assertFalse( $plusMinusLiteral.matches("+a") );
        assertFalse( $plusMinusLiteral.matches("-a") );
    }
}
