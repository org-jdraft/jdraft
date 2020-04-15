package org.jdraft;

import junit.framework.TestCase;

public class _arrayAccessTest extends TestCase {

    public void testArr(){
        _arrayAccess _aa = _arrayAccess.of("v[1][3]");
        Print.tree(_aa.ast());
        Print.tree( _aa.getIndex().ast() );
    }

    public void testArrAcc(){
        assertEquals( _arrayAccess.of("a",1), _arrayAccess.of("a[1]"));
        _arrayAccess _aa1 = _arrayAccess.of("v", 1,3);
        _arrayAccess _aa2 = _arrayAccess.of("v[1][3]");
        assertEquals( _aa1, _aa2);

    }
}
