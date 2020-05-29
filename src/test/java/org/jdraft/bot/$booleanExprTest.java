package org.jdraft.bot;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.stmt.WhileStmt;
import junit.framework.TestCase;
import org.jdraft.Expr;
import org.jdraft.Stmt;
import org.jdraft._booleanExpr;
import org.jdraft._jdraftException;

public class $booleanExprTest extends TestCase {

    //the simple prototypes
    public static $booleanExpr $ANY = $booleanExpr.of();

    public static $booleanExpr $TRUE = $booleanExpr.of(true);
    public static $booleanExpr $FALSE = $booleanExpr.of(false);
    public static $booleanExpr $TRUE_OR_FALSE = $booleanExpr.or($booleanExpr.of(true), $booleanExpr.of(false));

    public void testAny(){
        $booleanExpr $b = $booleanExpr.of();
        assertTrue($b.matches(true));
        assertTrue($b.matches(false));

        $b.matches(_booleanExpr.of(true));
        $b.matches(_booleanExpr.of(false));

        $b.matches(new BooleanLiteralExpr(true));
        $b.matches(new BooleanLiteralExpr(false));

        assertTrue( $b.isMatchAny() );
    }
    public void testAny2(){
        assertTrue( $ANY.matches(true) );
        assertTrue( $ANY.matches(false) );
        assertFalse( $ANY.matches("blah"));
        Select<_booleanExpr> s = $ANY.select("true");
        assertEquals(_booleanExpr.of(true), s.select);
    }

    public void testTrue() {
        assertTrue($TRUE.matches("true"));
        assertFalse($TRUE.matches("false"));
        assertFalse($TRUE.matches("neither"));
    }

    public void testFalse(){
        assertFalse($FALSE.matches("true"));
        assertTrue($FALSE.matches("false"));
        assertFalse($TRUE.matches("neither"));
    }

    public static $booleanExpr $CHILDOFWHILE = $booleanExpr.of(). $and(b-> b.ast().getParentNode().isPresent() &&
            b.ast().getParentNode().isPresent() && b.ast().getParentNode().get().getClass() == WhileStmt.class);

    public void testChildOfWhile(){
        WhileStmt ws = (WhileStmt) Stmt.of("while(true){ }");
        assertTrue($CHILDOFWHILE.matches(ws.getCondition()));
    }

    public void testDraft(){
        //I can draft these, (I don't need to "pass in" anything)
        assertEquals( _booleanExpr.of(true), $TRUE.draft());
        assertEquals( _booleanExpr.of(false), $FALSE.draft());

        assertEquals(_booleanExpr.of(true), $ANY.draft("$booleanExpr", true));
        assertEquals(_booleanExpr.of(false), $ANY.draft("$booleanExpr", "false"));

        try {
            assertEquals(_booleanExpr.of(false), $ANY.draft("$boolean", "gonna blow up"));
            fail("Expected exception");
        }catch(_jdraftException e){

        }
    }

    public void testOr(){
        assertTrue($TRUE_OR_FALSE.matches("true"));
        assertTrue($TRUE_OR_FALSE.matches("false"));
        assertFalse($TRUE_OR_FALSE.matches("neither"));
    }

    public void test$And$Not() {
        $booleanExpr $b = $booleanExpr.of(true);
        $b.$and( b-> b.isTrue() && b.ast().getParentNode().isPresent() );
        assertFalse( $b.matches(true));
        $b = $booleanExpr.of().$not(b-> b.isFalse());
        assertTrue( $b.matches(true));
    }

    public void testAst(){
        Select<_booleanExpr> s = $ANY.select(Expr.booleanExpr("true"));
        assertEquals(_booleanExpr.of(true), s.select);
    }

    public void testPInstance(){
        //assertNotNull(_BP.select(_boolean.of(true)));
        Select<_booleanExpr> bs = $ANY.select(_booleanExpr.of(true));
        assertEquals( _booleanExpr.of(true), bs.select);
        assertTrue( bs.tokens.isEmpty());
    }
}
