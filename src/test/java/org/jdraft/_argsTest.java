package org.jdraft;

import junit.framework.TestCase;

public class _argsTest extends TestCase {

    public void testA(){
        _args _as = _args.of(); //no args
        assertEquals(0, _as.size());
        assertTrue(_as.isEmpty());
        assertTrue(_as.allMatch( e-> e.is(Ex.of(1))));
        assertFalse(_as.anyMatch( e-> e.is(Ex.of(1))));

        _as.add("1");
        assertFalse(_as.isEmpty());
        assertTrue(_as.is(_int.of(1)));

        assertTrue(_as.isAt(0, _int.of(1)));
        assertTrue(_as.isAt(0, "1"));
        assertTrue(_as.isAt(0, Ex.of("1")));

        assertEquals(_int.of(1), _as.getAt(0));
        assertEquals(-1, _as.indexOf( _int.of(2)));
        assertTrue(_as.allMatch(e-> e instanceof _expression._literal));
        assertTrue(_as.anyMatch(e-> e instanceof _expression._literal));

        _as.removeAt(0);
        assertTrue(_as.allMatch(e-> e instanceof _expression._literal));
        assertFalse(_as.anyMatch(e-> e instanceof _expression._literal));

        _as.add(1);
        assertEquals(0, _as.indexOf(_int.of(1)));
    }
}
