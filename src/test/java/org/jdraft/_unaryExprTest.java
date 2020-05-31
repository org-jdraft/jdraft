package org.jdraft;

import junit.framework.TestCase;

public class _unaryExprTest extends TestCase {


    public void testM(){
        assertTrue(_unaryExpr.of("--getValue()").isExpression("getValue()"));
        assertTrue(_unaryExpr.of("--getValue()").isOperator("--"));
        assertTrue(_unaryExpr.of("--getValue()").isPrefixOperator());
        assertTrue(_unaryExpr.of("--getValue()").isPrefixOperator("--"));
        assertTrue(_unaryExpr.of("--getValue()").isOperator(_unaryExpr.PRE_DECREMENT));

        assertTrue(_unaryExpr.of("getValue()--").isExpression("getValue()"));
        assertTrue(_unaryExpr.of("--getValue()").isOperator("--"));
        assertTrue(_unaryExpr.of("getValue()--").isPostfixOperator());
        assertTrue(_unaryExpr.of("getValue()--").isPostfixOperator("--"));
        assertTrue(_unaryExpr.of("getValue()--").isOperator(_unaryExpr.POST_DECREMENT));
    }

    /**
     * _unaryExpr is unique in that the order of the features
     * is dynamic depending on instance (based on the operator)
     * i.e.
     * prefix operators for a _unaryExpr have the OPERATOR before the EXPRESSION
     * postfix operators for the _unaryExpr have the OPERATOR after the EXPRESSION
     */
    public void testMetaList(){
        _unaryExpr _u = _unaryExpr.of("++a");
        assertTrue(_u.META.list(_u).get(0) == _unaryExpr.OPERATOR);
        assertTrue(_u.META.list(_u).get(1) == _unaryExpr.EXPRESSION);

        _u = _unaryExpr.of("a--");
        assertTrue(_u.META.list(_u).get(0) == _unaryExpr.EXPRESSION);
        assertTrue(_u.META.list(_u).get(1) == _unaryExpr.OPERATOR);
    }

}