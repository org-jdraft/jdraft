package org.jdraft.bot;

import junit.framework.TestCase;

public class $stringExprTest extends TestCase {

    public void testS(){
        assertTrue($stringExpr.of().matches("AnyString"));
    }
}
