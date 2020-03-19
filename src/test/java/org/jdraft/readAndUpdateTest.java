package org.jdraft;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import junit.framework.TestCase;

import java.util.function.Predicate;

public class readAndUpdateTest extends TestCase {

    public static final Predicate<ExpressionStmt> MATCH_SYSTEM_OUT_ST_FN =
            m-> m.isExpressionStmt()
                    && ((ExpressionStmt)m).getExpression().isMethodCallExpr() &&
                    ((MethodCallExpr)m.asExpressionStmt().getExpression()).getScope().isPresent()
                    && ((MethodCallExpr)m.asExpressionStmt().getExpression()).getScope().get().toString().equals("System.out");

    public void testF() {
        _class _c = _class.of(_typeRef.class);
        Tree.in( _c, ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> System.out.println(es));
        //_c.walk(ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> System.out.println(es));
        Tree.in( _c, ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> es.getParentNode().get().remove(es));
        //_c.walk(ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> es.getParentNode().get().removeIn(es));
        System.out.println("REMOVED ");
        Tree.in(_c, ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> System.out.println(es));
        //_c.walk(ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> System.out.println(es));

        /*
        _types _ts = _types.of(_typeRef.class);
        _ts.walk(ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> System.out.println(es));
        _ts.walk(ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> es.getParentNode().get().removeIn(es));
        */
        Tree.in(_c, ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> System.out.println(es));
        //_c.walk(ExpressionStmt.class, MATCH_SYSTEM_OUT_ST_FN, es -> System.out.println(es));
    }
}
