package org.jdraft;

import junit.framework.TestCase;

public class _variableTest extends TestCase {

    public void testF(){
        _variable _v = _variable.of("int i");
        assertTrue(_v.isNamed("i"));
        assertTrue(_v.isType(int.class));
    }
}
