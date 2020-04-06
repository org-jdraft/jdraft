package org.jdraft.bot;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.stmt.WhileStmt;
import junit.framework.TestCase;
import org.jdraft.Expressions;
import org.jdraft.Statements;
import org.jdraft._boolean;
import org.jdraft._jdraftException;

public class SbooleanTest extends TestCase {

    //the simple prototypes
    public static $boolean $ANY = $boolean.of();

    public static $boolean $TRUE = $boolean.of(true);
    public static $boolean $FALSE = $boolean.of(false);
    public static $boolean $TRUE_OR_FALSE = $boolean.or($boolean.of(true), $boolean.of(false));

    public void testAny(){
        $boolean $b = $boolean.of();
        assertTrue($b.matches(true));
        assertTrue($b.matches(false));

        $b.matches(_boolean.of(true));
        $b.matches(_boolean.of(false));

        $b.matches(new BooleanLiteralExpr(true));
        $b.matches(new BooleanLiteralExpr(false));

        assertTrue( $b.isMatchAny() );
    }
    public void testAny2(){
        assertTrue( $ANY.matches(true) );
        assertTrue( $ANY.matches(false) );
        assertFalse( $ANY.matches("blah"));
        Select<_boolean> s = $ANY.select("true");
        assertEquals(_boolean.of(true), s.selection);
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

    public static $boolean $CHILDOFWHILE = $boolean.of(). $and(b-> b.ast().getParentNode().isPresent() &&
            b.ast().getParentNode().isPresent() && b.ast().getParentNode().get().getClass() == WhileStmt.class);

    public void testChildOfWhile(){
        WhileStmt ws = (WhileStmt) Statements.of("while(true){ }");
        assertTrue($CHILDOFWHILE.matches(ws.getCondition()));
    }

    public void testDraft(){
        //I can draft these, (I don't need to "pass in" anything)
        assertEquals( _boolean.of(true), $TRUE.draft());
        assertEquals( _boolean.of(false), $FALSE.draft());

        assertEquals(_boolean.of(true), $ANY.draft("$boolean", true));
        assertEquals(_boolean.of(false), $ANY.draft("$boolean", "false"));

        try {
            assertEquals(_boolean.of(false), $ANY.draft("$boolean", "gonna blow up"));
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
        $boolean $b = $boolean.of(true);
        $b.$and( b-> b.isTrue() && b.ast().getParentNode().isPresent() );
        assertFalse( $b.matches(true));
        $b = $boolean.of().$not(b-> b.isFalse());
        assertTrue( $b.matches(true));
    }

    public void testAst(){
        Select<_boolean> s = $ANY.select(Expressions.booleanLiteralEx("true"));
        assertEquals(_boolean.of(true), s.selection);
    }

    public void testPInstance(){
        //assertNotNull(_BP.select(_boolean.of(true)));
        Select<_boolean> bs = $ANY.select(_boolean.of(true));
        assertEquals( _boolean.of(true), bs.selection);
        assertTrue( bs.tokens.isEmpty());
    }
}
