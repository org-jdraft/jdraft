package org.jdraft;

import junit.framework.TestCase;

public class _argsTest extends TestCase {

    public void testGroupApi(){
        _args _as = _args.of( "1,2");
        assertTrue(_as.is("1,2"));
        assertFalse(_as.is("2,1")); //args are ordered, so thisll fail

        assertTrue( _as.anyMatch(_intExpr.class) );
        assertTrue( _as.anyMatch(_stringExpr.class, _intExpr.class) );
        assertFalse( _as.anyMatch(_binaryExpr.class));

        assertTrue( _as.allMatch(_intExpr.class) );
        assertTrue( _as.allMatch(_stringExpr.class, _intExpr.class) );
        assertFalse( _as.allMatch(_binaryExpr.class));

        assertEquals(2, _as.count(_intExpr.class));
        assertEquals(2, _as.count(_intExpr.class, _stringExpr.class));
        assertEquals(1, _as.count(_intExpr.class, _i-> _i.getValue() >=2));

        assertEquals(_intExpr.of(1), _as.first(_intExpr.class));
        assertEquals(_intExpr.of(2), _as.first(_intExpr.class, _i-> _i.getValue() >1));

        assertTrue(_as.has(_intExpr.class));
        assertTrue(_as.has(_stringExpr.class, _intExpr.class));
        assertTrue(_as.has(_expr._literal.class));
        assertFalse(_as.has(_stringExpr.class));

        //has with predicate
        assertTrue(_as.has(_intExpr.class, _i-> (_i.getValue() % 2) == 1));

        assertEquals(0, _as.indexOf(_intExpr.of(1)));
        assertEquals(1, _as.indexOf(_intExpr.of(2)));

        assertTrue(_as.is("1","2"));
        assertTrue(_as.isAt(0, 1));
        assertTrue(_as.isAt(1, 2));

        assertTrue(_as.isAt(0, _intExpr.class, _i-> _i.is(1)));
        assertTrue(_as.isAt(1, _intExpr.class, _i-> _i.is(2)));

        //list with impl type
        assertEquals( 2, _as.list(_intExpr.class).size());
        assertEquals( 2, _as.list(_expr._literal.class).size());

        //list with predicate
        assertEquals( 1, _as.list(_intExpr.class, _i-> _i.getValue() >=2).size());

        assertTrue(_as.isAt(0, _intExpr.class));
        assertTrue(_as.isAt(0, _intExpr.class, _i->!_i.has_Separators()));

    }

    public void testA(){
        _args _as = _args.of(); //no args
        assertEquals(0, _as.size());
        assertTrue(_as.isEmpty());
        assertTrue(_as.allMatch( e-> e.is(Expr.of(1))));
        assertFalse(_as.anyMatch( e-> e.is(Expr.of(1))));
        assertEquals( -1, _as.indexOf(_intExpr.of(1)));
        assertNull( _as.first(_e -> _e instanceof _expr._literal ));
        _as.toEach(e-> fail() ); //there are none so we shouldnt fail
        assertFalse(_as.has(_intExpr.of(2)));

        try{
            _as.getAt(1); //fails?
            fail("expected failure");
        }catch(IndexOutOfBoundsException iobe){
            //expected
        }

        _as.add("1");
        assertFalse(_as.isEmpty());
        assertTrue(_as.is(_intExpr.of(1)));

        assertTrue(_as.isAt(0, _intExpr.of(1)));
        assertTrue(_as.isAt(0, "1"));
        assertTrue(_as.isAt(0, Expr.of("1")));

        assertEquals(_intExpr.of(1), _as.getAt(0));
        assertEquals(-1, _as.indexOf( _intExpr.of(2)));
        assertTrue(_as.allMatch(e-> e instanceof _expr._literal));
        assertTrue(_as.anyMatch(e-> e instanceof _expr._literal));

        _as.removeAt(0);
        assertTrue(_as.allMatch(e-> e instanceof _expr._literal));
        assertFalse(_as.anyMatch(e-> e instanceof _expr._literal));

        _as.add(1);
        assertEquals(0, _as.indexOf(_intExpr.of(1)));
        _as.setAt(0, 2); //change 1 to 2
        assertEquals(0, _as.indexOf(_intExpr.of(2)));
    }

    public void testConstImpl(){
        _constant _c = _constant.of("A");
        _args _as = _c.getArgs();
        assertTrue(_as.isEmpty());
        _c.setArgs(_args.of("1,2,3, 'c', \"String\""));
        assertEquals( 5, _as.size());

        _c.forArgs(a-> System.out.println( a) );
    }
}
