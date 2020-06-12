package org.jdraft;

import junit.framework.TestCase;

public class _arrayDimensionTest extends TestCase {

    public void testArrayDimension(){
        _arrayDimension _ad = _arrayDimension.of();
        System.out.println(_ad);
        assertNull(_ad.getExpression());

        _ad = _arrayDimension.of("0");
        assertTrue( _ad.isExpression(0));
        assertFalse( _ad.isExpression(1));

        _ad = _arrayDimension.of("[0]");
        assertTrue( _ad.isExpression(0));
        assertFalse( _ad.isExpression(1));

        //type
        assertTrue( _ad.is(_expr._literal.class));
        assertTrue( _ad.is(_intExpr.class));

        //type & predicate
        assertTrue( _ad.is(_intExpr.class, _i-> _i.is(0)));
    }

}