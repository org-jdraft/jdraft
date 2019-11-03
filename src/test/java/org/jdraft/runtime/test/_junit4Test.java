package org.jdraft.runtime.test;

import junit.framework.TestCase;
import org.jdraft._annosTest;
import org.jdraft.walkTest;
import org.junit.runner.Result;

public class _junit4Test extends TestCase {

    public void testRun(){
        Result r = new _junit4(_annosTest.class, walkTest.class).run();
        assertEquals(0, r.getFailureCount());
    }
}
