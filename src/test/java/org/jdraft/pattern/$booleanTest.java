package org.jdraft.pattern;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.stmt.WhileStmt;
import junit.framework.TestCase;
import org.jdraft.Ex;
import org.jdraft.Stmt;
import org.jdraft._boolean;
import org.jdraft._jdraftException;

public class $booleanTest extends TestCase {

    //the simple prototypes
    public static $boolean $ANY = $boolean.of();

    public static $boolean $TRUE = $boolean.of(true);
    public static $boolean $FALSE = $boolean.of(false);

    public static $ex $TRUE_OR_FALSE = $boolean.or($boolean.of(true), $boolean.of(false));

    public void testAny() {
        assertTrue($ANY.matches(true));
        assertTrue($ANY.matches(false));
        assertFalse($ANY.matches("blah"));
        $ex.Select<BooleanLiteralExpr, _boolean> s = $ANY.select("true");
        assertEquals(_boolean.of(true), s._ex);
    }

    public void testTrue() {
        assertTrue($TRUE.matches("true"));
        assertFalse($TRUE.matches("false"));
        assertFalse($TRUE.matches("neither"));
    }

    public void testFalse() {
        assertFalse($FALSE.matches("true"));
        assertTrue($FALSE.matches("false"));
        assertFalse($TRUE.matches("neither"));
    }

    public static $boolean $CHILDOFWHILE =
            $boolean.of().$and(
                    (_boolean b) -> b.ast().getParentNode().isPresent() &&
                            b.ast().getParentNode().isPresent() &&
                            b.ast().getParentNode().get().getClass() == WhileStmt.class);

    public void testChildOfWhile() {
        WhileStmt ws = (WhileStmt) Stmt.of("while(true){ }");
        assertTrue($CHILDOFWHILE.match(ws.getCondition()));
    }

    public void testDraft() {
        //I can draft these, (I don't need to "pass in" anything)
        assertEquals(_boolean.of(true), $TRUE.draft());
        assertEquals(_boolean.of(false), $FALSE.draft());

        assertEquals(_boolean.of(true), $ANY.draft("$boolean", true));
        assertEquals(_boolean.of(false), $ANY.draft("$boolean", "false"));

        try {
            assertEquals(_boolean.of(false), $ANY.draft("$boolean", "gonna blow up"));
            fail("Expected exception");
        } catch (_jdraftException e) {

        }
    }

    public void testOr() {
        $boolean $t = $boolean.of(true);
        $boolean $f = $boolean.of(false);
        assertTrue($t.matches(true));
        assertFalse($t.matches(false));
        assertTrue($t.matches(_boolean.of(true)));
        $ex $TRUE_OR_FALSE = $boolean.or($t, $f);
        assertTrue($TRUE_OR_FALSE.matches("true"));
        assertTrue($TRUE_OR_FALSE.matches("false"));
        assertFalse($TRUE_OR_FALSE.matches("neither"));
    }


    public void testAst() {
        $ex.Select s = $ANY.select(Ex.booleanLiteralEx("true"));
        assertEquals(_boolean.of(true), s._ex);
    }

    public void testPInstance() {
        //assertNotNull(_BP.select(_boolean.of(true)));
        $ex.Select<BooleanLiteralExpr, _boolean> bs = $ANY.select(_boolean.of(true));
        assertEquals(_boolean.of(true), bs._ex);
        assertTrue(bs.tokens.isEmpty());
    }
}


