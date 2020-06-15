package org.jdraft;

import junit.framework.TestCase;

public class _annoEntryPairsTest extends TestCase {

    public void testNone(){
         assertEquals(_annoEntryPairs.of(), _annoEntryPairs.of());
         assertEquals(_annoEntryPairs.of(""), _annoEntryPairs.of(""));
         assertEquals(_annoEntryPairs.of("").hashCode(), _annoEntryPairs.of("").hashCode());
         assertEquals(_annoEntryPairs.of().hashCode(), _annoEntryPairs.of().hashCode());

         _annoEntryPairs aps = _annoEntryPairs.of();
         assertFalse( aps.isValueOnly());
         assertTrue( aps.isEmpty());
         assertTrue(aps.is(""));
         assertTrue(aps.is("$any$"));
    }

    public void testSingleMember(){
        assertEquals(_annoEntryPairs.of("1"), _annoEntryPairs.of("1"));
        assertEquals(_annoEntryPairs.of("1").hashCode(), _annoEntryPairs.of("1").hashCode());

        _annoEntryPairs aps = _annoEntryPairs.of("1");
        assertTrue( aps.isValueOnly());
        assertFalse( aps.isEmpty() );
        assertTrue(aps.is("1"));
        assertTrue(aps.is("$any$"));

        assertTrue( aps.is( _expr.of(1) ));
        assertFalse( aps.is( _expr.of(2) ));

        assertTrue( aps.is(e-> e instanceof _expr._literal));
        //class and expression
        assertTrue( aps.is(_intExpr.class, _i -> _i.getValue() <= 2 ));

        assertTrue(aps.has("$any$"));
        assertTrue(aps.has("1"));
        assertTrue(aps.has("value=1"));
    }

    public void testOneKeyValue(){
        assertEquals(_annoEntryPairs.of("k=1"), _annoEntryPairs.of("k=1"));
        assertEquals(_annoEntryPairs.of("k=1").hashCode(), _annoEntryPairs.of("k=1").hashCode());

        _annoEntryPairs aps = _annoEntryPairs.of("k=1");
        assertFalse( aps.isValueOnly());
        assertFalse( aps.isEmpty() );
        assertTrue(aps.is("$any$"));
        assertTrue(aps.is("k=1"));
        assertTrue(aps.is("$any$=1"));
        assertTrue(aps.is("k=$any$"));

        assertTrue(aps.has("$any$"));
        assertTrue(aps.has("k=1"));
        assertTrue(aps.has("$any$=1"));
        assertTrue(aps.has("k=$any$"));

        assertTrue( aps.isAt(0, _annoEntryPair.of("k=1")));
    }

    public void testMultiKeyValue(){
        assertEquals(_annoEntryPairs.of("k=1,v=2"), _annoEntryPairs.of("k=1,v=2"));
        assertEquals(_annoEntryPairs.of("k=1,v=2").hashCode(), _annoEntryPairs.of("k=1,v=2").hashCode());

        //verify out of order
        assertEquals(_annoEntryPairs.of("k=1,v=2"), _annoEntryPairs.of("v=2,k=1"));
        assertEquals(_annoEntryPairs.of("k=1,v=2").hashCode(), _annoEntryPairs.of("v=2,k=1").hashCode());


        _annoEntryPairs aps = _annoEntryPairs.of("k=1,v=2");
        assertFalse( aps.isValueOnly());
        assertFalse( aps.isEmpty() );
        assertTrue(aps.is("$any$"));
        //both
        assertTrue(aps.is("k=1,v=2"));
        assertTrue(aps.is("v=2,k=1"));
        //both parameterized
        assertTrue(aps.is("k=$a$,v=$b$"));
        assertTrue(aps.is("$a$=1,$b$=2"));
        assertTrue(aps.is("$a$=2,$b$=1"));

        //either
        assertTrue(aps.is("$any$=1"));
        assertTrue(aps.is("$any$=2"));
        assertTrue(aps.is("k=$any$"));
        assertTrue(aps.is("v=$any$"));

        //by index
        assertTrue( aps.isAt(0, _annoEntryPair.of("k=1")));
        assertTrue( aps.isAt(1, _annoEntryPair.of("v=2")));

        assertTrue(aps.has("$any$"));
        assertTrue(aps.has("k=1"));
        assertTrue(aps.has("v=2"));
        assertTrue(aps.has("$any$=1"));
        assertTrue(aps.has("k=$any$"));
        assertTrue(aps.has("$any$=2"));
        assertTrue(aps.has("v=$any$"));


    }

}