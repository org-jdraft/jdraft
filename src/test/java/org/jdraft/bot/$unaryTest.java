package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._expression;

public class $unaryTest extends TestCase {

    public void testMatchAny() {
        $unary $u = $unary.of();
        assertTrue($u.matches("-a"));
        assertTrue($u.matches("+a"));
        assertTrue($u.matches("!true"));
        assertTrue($u.matches("a++"));
        assertTrue($u.matches("a--"));
        assertTrue($u.matches("++a"));
        assertTrue($u.matches("--a"));
        assertTrue($u.matches("~a"));
    }

    public void testByOperator(){
        assertTrue( $unary.bitwiseComplement().matches("~a"));
        assertFalse( $unary.bitwiseComplement().matches("a++"));

        assertTrue( $unary.preIncrement().matches("++a"));
        assertFalse( $unary.preIncrement().matches("a++"));

        assertTrue( $unary.preDecrement().matches("--a"));
        assertFalse( $unary.preDecrement().matches("a--"));

        assertFalse( $unary.postIncrement().matches("++a"));
        assertTrue( $unary.postIncrement().matches("a++"));

        assertFalse( $unary.postDecrement().matches("--a"));
        assertTrue( $unary.postDecrement().matches("a--"));

        assertTrue( $unary.minus().matches("-a"));
        assertFalse( $unary.minus().matches("+a"));

        assertFalse( $unary.plus().matches("-a"));
        assertTrue( $unary.plus().matches("+a"));

        assertTrue( $unary.logicalComplement().matches("!a"));
        assertFalse( $unary.logicalComplement().matches("+a"));
    }

    public void testOrPostIT(){
        $unary $plusMinusLiteral =
                $unary.or( $unary.plus(), $unary.minus() ).$expression(_expression._literal.class);

        assertTrue( $plusMinusLiteral.matches("+1") );
        assertTrue( $plusMinusLiteral.matches("-1") );
        assertFalse( $plusMinusLiteral.matches("~1") );

        //the
        assertFalse( $plusMinusLiteral.matches("+a") );
        assertFalse( $plusMinusLiteral.matches("-a") );
    }
}
