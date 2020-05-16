package org.jdraft.bot;

import com.github.javaparser.ast.expr.BinaryExpr;
import junit.framework.TestCase;
import org.jdraft._binaryExpr;
import org.jdraft._expr;

public class $binaryExprTest extends TestCase {

    public void testToString(){
        System.out.println( $binaryExpr.of().toString() );
        System.out.println( $binaryExpr.plus().toString() );
        System.out.println( $binaryExpr.of("a + b").toString() );
    }
    public void testSpecificOperator(){
        assertTrue( $binaryExpr.and().matches("a && b") );
        assertTrue( $binaryExpr.binaryAnd().matches("a & b") );
        assertTrue( $binaryExpr.binaryOr().matches("a | b") );
        assertTrue( $binaryExpr.divide().matches("a / b") );
        assertTrue( $binaryExpr.equals().matches("a == b") );
        assertTrue( $binaryExpr.greater().matches("a > b") );
        assertTrue( $binaryExpr.greaterEquals().matches("a >= b") );
        assertTrue( $binaryExpr.leftShift().matches("a << b") );
        assertTrue( $binaryExpr.less().matches("a < b") );
        assertTrue( $binaryExpr.lessEquals().matches("a <= b") );
        assertTrue( $binaryExpr.minus().matches("a - b") );
        assertTrue( $binaryExpr.multiply().matches("a * b") );
        assertTrue( $binaryExpr.notEquals().matches("a != b") );
        assertTrue( $binaryExpr.or().matches("a || b") );
        assertTrue( $binaryExpr.plus().matches("a + b") );
        assertTrue( $binaryExpr.rem().matches("a % b") );
        assertTrue( $binaryExpr.signedRightShift().matches("a >> b") );
        assertTrue( $binaryExpr.unsignedRightShift().matches("a >>> b") );
        assertTrue( $binaryExpr.xor().matches("a ^ b") );
    }

    public void testAny(){
        $binaryExpr $be = $binaryExpr.of();
        assertTrue( $be.isMatchAny());
        assertTrue( $be.matches("1==2"));
        assertTrue( $be.matches("a>b"));
        assertNotNull( $be.select("1!=2"));
    }

    public void testOperatorOnly(){
        //assertTrue($binaryExpression.of(_binaryExpression.PLUS).matches("a+b"));
        //assertTrue($binaryExpression.of().$and(_binaryExpression.PLUS).matches("a+b"));
        assertTrue( $binaryExpr.of().$not(_binaryExpr.REMAINDER).matches("a + b"));
        assertFalse( $binaryExpr.of().$not(_binaryExpr.REMAINDER).matches("a % b"));

        $binaryExpr $be = $binaryExpr.of(_binaryExpr.PLUS);
        System.out.println( $be.operator.excludedValues );
        assertTrue( $binaryExpr.of(_binaryExpr.PLUS).matches("a + b"));
        assertFalse( $binaryExpr.of(_binaryExpr.PLUS).matches("a / b"));

        assertTrue( $binaryExpr.of(_binaryExpr.PLUS, _binaryExpr.DIVIDE).matches("a / b"));
        assertFalse( $binaryExpr.of(_binaryExpr.PLUS, _binaryExpr.DIVIDE).matches("a % b"));
        assertTrue( $binaryExpr.plus().matches("a + b"));
        assertFalse( $binaryExpr.plus().matches("a / b"));

        assertTrue($binaryExpr.of().$operators(_binaryExpr.PLUS, _binaryExpr.MINUS).matches("a + b"));
        assertTrue($binaryExpr.of().$operators(_binaryExpr.PLUS, _binaryExpr.MINUS).matches("a - b"));
        assertFalse($binaryExpr.of().$operators(_binaryExpr.PLUS, _binaryExpr.MINUS).matches("a / b"));

    }

    public void testSpecific(){
        $binaryExpr $be = $binaryExpr.of("1==2");
        //System.out.println("ALL"+$be.operator.allPossibleValues);
        //System.out.println("EX"+ $be.operator.excludedValues);
        //Set s = new HashSet<>();
        //s.addAll($be.operator.allPossibleValues);
        //s.removeAll($be.operator.excludedValues);
        //System.out.println( "REM "+ s);
        assertTrue( $be.matches("1 == 2"));
        assertFalse( $be.matches("1 == a"));
        assertFalse( $be.matches("1 > 2"));

        _binaryExpr _r = $be.draft();
        assertTrue( _r.is("1==2"));
    }

    public void testVariables(){
        $binaryExpr $be = $binaryExpr.of("$a$==$b$");
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
        $binaryExpr $be = $binaryExpr.of().$and(b-> b.getOperator() == BinaryExpr.Operator.GREATER);
        assertTrue($be.matches("a > b"));
        assertFalse($be.matches("a < b"));
    }

    public void testLeft(){
        $binaryExpr $be = $binaryExpr.of().$left(_expr._literal.class);
        assertTrue($be.matches( "1==2"));
        assertFalse($be.matches( "a==2"));
        assertTrue($be.matches( "2 > a()"));
        assertTrue($be.matches( "2 >= a() + 12"));

        $be = $binaryExpr.of().$left($intExpr.of(1));
        assertTrue($be.matches( "1==2"));
        assertFalse($be.matches( "a==2"));
        assertTrue($be.matches( "1 > a()"));
        assertFalse($be.matches( "2 >= a() + 12"));
    }

    public void testRight(){
        $binaryExpr $be = $binaryExpr.of().$right(_expr._literal.class);
        assertTrue($be.matches( "1==2"));
        assertFalse($be.matches( "2==a"));
        assertTrue($be.matches( "a > 3"));
        assertTrue($be.matches( "a() + 12"));

        $be = $binaryExpr.of().$right($intExpr.of(1));
        assertTrue($be.matches( "2==1"));
        assertFalse($be.matches( "2==a"));
        assertTrue($be.matches( "a() > 1"));
        assertFalse($be.matches( "a() + 12 >= 3"));
    }

    public void testOr(){

        $binaryExpr $oneLiteral =
                $binaryExpr.or( $binaryExpr.of().$left(_expr._literal.class),
                        $binaryExpr.of().$right(_expr._literal.class));

        assertTrue($oneLiteral.matches("1<2"));
        assertTrue($oneLiteral.matches("a>2"));
        assertTrue($oneLiteral.matches("1!=a"));

        assertFalse($oneLiteral.matches("a!=b"));

        //or base select
        $oneLiteral.$left("$a$");
        assertTrue($oneLiteral.select("1>2").is("a", "1"));

        //copy and change
        $binaryExpr $anotherLiteral = $oneLiteral.copy().$right("2");
        assertTrue($anotherLiteral.matches("1 >> 2"));
        assertFalse($anotherLiteral.matches("1 >> 3"));

        //verify the original isn't changed
        assertTrue($oneLiteral.matches("1 >> 3"));
    }

    public void test$and(){
        assertTrue( $binaryExpr.of().$and(b-> b.hasComment(false)).matches("a + b"));
        assertFalse( $binaryExpr.of().$and(b-> b.hasComment(false)).matches(_binaryExpr.of("a + b").setComment("/*Hello*/") ) );

        //verify copies share the predicates
        assertTrue( $binaryExpr.of().$and(b-> b.hasComment(false)).copy().matches("a + b"));
        assertFalse( $binaryExpr.of().$and(b-> b.hasComment(false)).copy().matches(_binaryExpr.of("a + b").setComment("/*Hello*/") ) );
    }
}
