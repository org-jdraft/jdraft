package org.jdraft;

import junit.framework.TestCase;

public class _blockStmtTest extends TestCase {

    public void testCount(){
        _blockStmt _bs = _blockStmt.of();
        //count with predicate
        assertEquals(0, _bs.count(_s -> _s instanceof _assertStmt));
        //count with class
        assertEquals(0, _bs.count(_assertStmt.class));
        //count with multiple classes
        assertEquals(0, _bs.count(_assertStmt.class, _ifStmt.class));
        //count with _stmt interface
        assertEquals(0, _bs.count(_stmt._goto.class));

        //modify
        _bs.add( ()-> {assert(0==0): "message";});
        //count with predicate
        assertEquals(1, _bs.count(_s -> _s instanceof _assertStmt));
        //with class and (typed) predicate
        assertEquals(1, _bs.count(_assertStmt.class, _a -> _a.hasMessage() ));
        //count with class
        assertEquals(1, _bs.count(_assertStmt.class));
        //count with multiple classes
        assertEquals(1, _bs.count(_assertStmt.class, _ifStmt.class));
        assertEquals(0, _bs.count(_stmt._goto.class));

        assertEquals( _intExpr.of(0), _bs.walk().first(_intExpr.class));
        assertTrue(_bs.walk().first(_stringExpr.class).isText("message"));
    }

    public void testCountLoop(){
        _blockStmt _bs = _blockStmt.of( ()->{
            int j = 1;
            for(int i=0; i<j; i++){
                System.out.println( " "+i +" "+ j );
            }
        });

        assertEquals(1, _bs.count(_stmt._loop.class));
    }

    public void testAddAt(){
        _blockStmt _bs = _blockStmt.of();
        //_bs.addAt(0, "System.out.println(0);");

        _bs.addAt(0, "System.out.println(0);");
        _bs.addAt(1, _stmt.of("System.out.println(1);"));
        _bs.removeAt(0);
        assertTrue( _bs.size() == 1);
        _bs.remove(_s -> _s instanceof _exprStmt);

        _bs.add(()-> {assert(1==1) : "message";});
        assertEquals(1, _bs.count( _assertStmt.class ));
        _bs.remove(_assertStmt.class, _as-> _as.hasMessage());

        assertTrue( _bs.isEmpty());
        //add multiple statements with a singe string
        _bs.addAt(0, "print(0); print(1);");
        assertEquals(2, _bs.size());

        _bs.clear();
        assertEquals(0, _bs.size());

        _bs.removeFromAst();
        //remove when empty
        _bs.remove( t-> true);
        assertEquals(0, _bs.size());

        //cant remove from AST because is root
        assertFalse( _bs.removeFromAst() );

        _blockStmt _parent = _blockStmt.of();
        _parent.add( _bs );

        //with a parent I CAN remove from AST
        assertTrue( _bs.removeFromAst() );
    }

    public void testMutateAndWalk(){
        _blockStmt _bs = _blockStmt.of();
        assertTrue(_bs.isEmpty());
        _bs.add("System.out.println( 1 );");
        _bs.add(()->{System.out.println(1);});
        System.out.println( _bs );
        assertTrue( _bs.size() == 2 );
        _bs.removeAt(0);
        assertTrue( _bs.size() == 1 );
        _bs.add( ()-> {
           if(true){
               assert 1==1 : "message";
           }
        });

        //verify I can find assertStmts
        assertTrue(_bs.walk().has(_assertStmt.class));
        //verify there is exactly 1 assertStmt in the blockStmt
        assertEquals(1, _bs.walk().count(_assertStmt.class));

        //walk & remove all assertsStmts that have messages
        _bs.walk().forEach(_assertStmt.class,_as-> _as.hasMessage(), _as-> _as.removeFromAst());

        //verify its been removed
        assertFalse( _bs.walk().has(_assertStmt.class) ); //verify there are none
        assertEquals(0, _bs.walk().count(_assertStmt.class)); //verify count is 0 after removing
    }
}