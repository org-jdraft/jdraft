package org.jdraft;

import junit.framework.TestCase;

public class _methodCallTest extends TestCase {

    public void testAPI(){
        _methodCall _mc = _methodCall.of();
        //System.out.println( _mc );
        _mc.setName("methodName");
        assertEquals("methodName", _mc.getName() );
        assertTrue(_mc.isNamed("methodName") );
        assertTrue(_mc.isNamed(n -> n.startsWith("method")) );
        assertTrue( _mc.isArguments( as-> as.isEmpty()) );
        assertFalse( _mc.hasArguments() );
        assertFalse( _mc.hasScope() );
        assertFalse(_mc.isArgument(0, _int.of(1)));

        _mc.addArguments("1");
        assertTrue( _mc.hasArguments() );
        assertTrue( _mc.isArgument(0, _int.of(1)));
        assertTrue( _mc.isArgument(0, "1"));

    }
}
