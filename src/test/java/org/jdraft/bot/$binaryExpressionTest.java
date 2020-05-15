package org.jdraft.bot;

import com.github.javaparser.ast.expr.BinaryExpr;
import junit.framework.TestCase;
import org.jdraft._binaryExpression;
import org.jdraft._expression;

import java.util.HashSet;
import java.util.Set;

public class $binaryExpressionTest extends TestCase {

    public void testAny(){
        $binaryExpression $be = $binaryExpression.of();
        assertTrue( $be.isMatchAny());
        assertTrue( $be.matches("1==2"));
        assertTrue( $be.matches("a>b"));
        assertNotNull( $be.select("1!=2"));
    }

    public void testOperatorOnly(){
        //assertTrue($binaryExpression.of(_binaryExpression.PLUS).matches("a+b"));
        //assertTrue($binaryExpression.of().$and(_binaryExpression.PLUS).matches("a+b"));
        assertTrue( $binaryExpression.of().$not(_binaryExpression.REMAINDER).matches("a + b"));
        assertFalse( $binaryExpression.of().$not(_binaryExpression.REMAINDER).matches("a % b"));

        $binaryExpression $be = $binaryExpression.of(_binaryExpression.PLUS);
        System.out.println( $be.operator.excludedValues );
        assertTrue( $binaryExpression.of(_binaryExpression.PLUS).matches("a + b"));
        assertFalse( $binaryExpression.of(_binaryExpression.PLUS).matches("a / b"));

        assertTrue( $binaryExpression.of(_binaryExpression.PLUS, _binaryExpression.DIVIDE).matches("a / b"));
        assertFalse( $binaryExpression.of(_binaryExpression.PLUS, _binaryExpression.DIVIDE).matches("a % b"));
        assertTrue( $binaryExpression.plus().matches("a + b"));
        assertFalse( $binaryExpression.plus().matches("a / b"));

        assertTrue($binaryExpression.of().$operators(_binaryExpression.PLUS, _binaryExpression.MINUS).matches("a + b"));
        assertTrue($binaryExpression.of().$operators(_binaryExpression.PLUS, _binaryExpression.MINUS).matches("a - b"));
        assertFalse($binaryExpression.of().$operators(_binaryExpression.PLUS, _binaryExpression.MINUS).matches("a / b"));

    }

    public void testSpecific(){
        $binaryExpression $be = $binaryExpression.of("1==2");
        //System.out.println("ALL"+$be.operator.allPossibleValues);
        //System.out.println("EX"+ $be.operator.excludedValues);
        //Set s = new HashSet<>();
        //s.addAll($be.operator.allPossibleValues);
        //s.removeAll($be.operator.excludedValues);
        //System.out.println( "REM "+ s);
        assertTrue( $be.matches("1 == 2"));
        assertFalse( $be.matches("1 == a"));
        assertFalse( $be.matches("1 > 2"));

        _binaryExpression _r = $be.draft();
        assertTrue( _r.is("1==2"));
    }

    public void testVariables(){
        $binaryExpression $be = $binaryExpression.of("$a$==$b$");
        assertTrue( $be.matches("1 == 2"));
        assertTrue( $be.matches("a() == b()"));
        assertTrue( $be.matches("a() == b() + c"));
        assertTrue( $be.matches("1 == a"));
        assertFalse( $be.matches("1 > 2"));

        assertTrue( $be.select("1==2").is("a", "1", "b", "2"));


        //draft/via fill
        assertTrue( $be.draft("a", 1,"b", 2).is("1==2"));
        assertTrue( $be.fill(1, 2).is("1==2"));
    }


    public void testPredicate(){
        //not the easiest way to do this, but supported
        $binaryExpression $be = $binaryExpression.of().$and( b-> b.getOperator() == BinaryExpr.Operator.GREATER);
        assertTrue($be.matches("a > b"));
        assertFalse($be.matches("a < b"));
    }

    public void testLeft(){
        $binaryExpression $be = $binaryExpression.of().$left(_expression._literal.class);
        assertTrue($be.matches( "1==2"));
        assertFalse($be.matches( "a==2"));
        assertTrue($be.matches( "2 > a()"));
        assertTrue($be.matches( "2 >= a() + 12"));

        $be = $binaryExpression.of().$left($int.of(1));
        assertTrue($be.matches( "1==2"));
        assertFalse($be.matches( "a==2"));
        assertTrue($be.matches( "1 > a()"));
        assertFalse($be.matches( "2 >= a() + 12"));
    }

    public void testRight(){
        $binaryExpression $be = $binaryExpression.of().$right(_expression._literal.class);
        assertTrue($be.matches( "1==2"));
        assertFalse($be.matches( "2==a"));
        assertTrue($be.matches( "a > 3"));
        assertTrue($be.matches( "a() + 12"));

        $be = $binaryExpression.of().$right($int.of(1));
        assertTrue($be.matches( "2==1"));
        assertFalse($be.matches( "2==a"));
        assertTrue($be.matches( "a() > 1"));
        assertFalse($be.matches( "a() + 12 >= 3"));
    }

    public void testOr(){

        $binaryExpression $oneLiteral =
                $binaryExpression.or( $binaryExpression.of().$left(_expression._literal.class),
                        $binaryExpression.of().$right(_expression._literal.class));

        assertTrue($oneLiteral.matches("1<2"));
        assertTrue($oneLiteral.matches("a>2"));
        assertTrue($oneLiteral.matches("1!=a"));


        //or base select
        $oneLiteral.$left("$a$");
        assertTrue($oneLiteral.select("1>2").is("a", "1"));

        //copy and change
        $binaryExpression $anotherLiteral = $oneLiteral.copy().$right("2");
        assertTrue($anotherLiteral.matches("1 >> 2"));
        assertFalse($anotherLiteral.matches("1 >> 3"));

        //verify the original isn't changed
        assertTrue($oneLiteral.matches("1 >> 3"));
    }

}
