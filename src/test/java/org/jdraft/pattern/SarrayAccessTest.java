package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft._nameExpression;

public class SarrayAccessTest extends TestCase {

    public void testAny(){
        $arrayAccess $aa = $arrayAccess.of();
        assertTrue( $arrayAccess.of().matches("v[1]"));
        assertTrue( $arrayAccess.of().matches("arr[1][m(5)]"));
        assertFalse( $arrayAccess.of().matches("v"));

        $aa.$name($.nameExpr("x"));
        assertFalse( $aa.matches("v[1]"));
        assertTrue( $aa.matches("x[1]"));

    }
}
