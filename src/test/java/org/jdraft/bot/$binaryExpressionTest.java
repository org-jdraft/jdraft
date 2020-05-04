package org.jdraft.bot;

import com.github.javaparser.ast.expr.BinaryExpr;
import junit.framework.TestCase;

public class $binaryExpressionTest extends TestCase {

    public void testAny(){
        $binaryExpression $be = $binaryExpression.of();
        assertTrue( $be.isMatchAny());
        assertTrue( $be.matches("1==2"));
        assertTrue( $be.matches("a>b"));
        assertNotNull( $be.select("1==2"));

    }

    public void testSpecific(){
        $binaryExpression $be = $binaryExpression.of("1==2");
        assertTrue( $be.matches("1 == 2"));
        assertFalse( $be.matches("1 == a"));
    }

    public void testVariables(){
        $binaryExpression $be = $binaryExpression.of("$a$==$b$");
        assertTrue( $be.matches("1 == 2"));
        assertTrue( $be.matches("a() == b()"));
        assertTrue( $be.matches("a() == b() + c"));
        assertTrue( $be.matches("1 == a"));
        assertFalse( $be.matches("1 > 2"));

        assertTrue( $be.select("1==2").is("a", "1", "b", "2"));
    }


    public void testPredicate(){
        $binaryExpression $be = $binaryExpression.of().$and( b-> b.getOperator() == BinaryExpr.Operator.GREATER);
        assertTrue($be.matches("a > b"));
        assertFalse($be.matches("a < b"));
    }

    public void testLeft(){

    }

    public void testRight(){

    }

    public void testOperators(){

    }

}
