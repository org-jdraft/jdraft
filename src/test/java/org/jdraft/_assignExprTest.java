package org.jdraft;

import com.github.javaparser.ast.expr.AssignExpr;
import junit.framework.TestCase;

public class _assignExprTest extends TestCase {

    public void testA(){
        _assignExpr _ae = _assignExpr.of("i=1");
        assertTrue(_ae.isTarget("i"));
        assertTrue( _ae.isTarget(t-> t instanceof _nameExpr));
        assertTrue(_ae.isValue("1"));
        assertTrue( _ae.isValue(v-> v instanceof _expr._literal));
        assertTrue( _ae.isValue(v-> v instanceof _intExpr));
        assertTrue( _ae.isValue(_intExpr.class));
        assertTrue( _ae.isValue(_expr._literal.class));

        assertFalse( _ae.isValue(_doubleExpr.class));

        assertTrue(_ae.isAssign());
        assertFalse(_ae.isBinaryLeftShiftAssign());

        _ae.setOperator(AssignExpr.Operator.BINARY_AND);
        assertTrue(_ae.isBinaryAndAssign() );
        assertTrue(_ae.is( "i&=1") );
    }

}