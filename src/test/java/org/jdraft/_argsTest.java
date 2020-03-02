package org.jdraft;

import junit.framework.TestCase;

public class _argsTest extends TestCase {

    public void testA(){
        _args _as = _args.of(); //no args
        assertEquals(0, _as.size());
        assertTrue(_as.isEmpty());
        assertTrue(_as.allMatch( e-> e.is(Ex.of(1))));
        assertFalse(_as.anyMatch( e-> e.is(Ex.of(1))));
        assertEquals( -1, _as.indexOf(_int.of(1)));
        assertNull( _as.get(_e -> _e instanceof _expression._literal ));
        _as.forEach(e-> fail() ); //there are none so we shouldnt fail
        assertFalse(_as.has(_int.of(2)));

        try{
            _as.getAt(1); //fails?
            fail("expected failure");
        }catch(IndexOutOfBoundsException iobe){
            //expected
        }

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
        _as.setAt(0, 2); //change 1 to 2
        assertEquals(0, _as.indexOf(_int.of(2)));
    }

    public void testConstImpl(){
        _constant _c = _constant.of("A");
        _args _as = _c.getArgs();
        assertTrue(_as.isEmpty());
        _c.setArguments(_args.of("1,2,3, 'c', \"String\""));
        assertEquals( 5, _as.size());

        _c.forArguments( a-> System.out.println( a) );
    }
}