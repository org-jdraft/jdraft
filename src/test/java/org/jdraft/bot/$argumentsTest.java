package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.*;

public class $argumentsTest extends TestCase {


    enum E{
        A,B(2), C(3+4);
        E(){}
        E(int i){ }
    }

    class B{}
    class C extends B{
        C(){
            super();//args for _constructorCall
        }
        C(int i){
            this("String", i); //args for _constructorCall
        }
        C(String s, int i){ }

        public void f() {
            System.out.println(1); //args for _methodcall
            new C();//args for _new
        }
    }

    public void testMatchAny(){
        assertTrue($args.of().matches("()"));
        assertTrue($args.of().matches("(i)"));
        assertTrue($args.of().matches("(i)"));
        assertTrue($args.of().matches("(i, 'c')"));
        assertTrue($args.of().matches("(i, 'c', (short)22)"));
        assertTrue($args.of().matches("(i, 'c', (byte)3)"));
    }

    public void testMatchSingleParam(){

        //assertNotNull($e.of("$args$").select("(i, 'c')"));

        assertTrue($args.of("$args$").matches("()"));
        assertTrue($args.of("$args$").matches("(i)"));
        assertTrue($args.of("$args$").matches("(i)"));
        assertTrue($args.of("$args$").matches("(i, 'c')"));
        assertTrue($args.of("$args$").matches("(i, 'c', (short)22)"));
        assertTrue($args.of("$args$").matches("(i, 'c', (byte)3)"));

        assertEquals(_args.of("i, 1"),
                $args.of("$args$").select("(i, 1)").selection);
    }

    public void testFromString(){
        assertEquals( _args.of(), _args.of(""));
        //System.out.println(_arguments.of("()") );
        //System.out.println(_arguments.of(""));

        assertEquals( _args.of("()"), _args.of(""));

        assertTrue($args.of("").matches(_args.of()));
        assertTrue($args.of("()").matches(_args.of()));
        assertTrue($args.of("()").matches(_args.of("1")));
    }

    public void testIndv(){
        assertEquals(1, $args.of( $intExpr.of() ).countIn(E.class));
    }

    public void testIndividualArgs(){

        assertEquals(1, $args.of( $e.of(_intExpr.class) ).countIn(E.class));
        assertEquals(1, $args.of( $e.of(_binaryExpr.class) ).countIn(E.class));

        $args $oneExpr = $args.of().$and(a->((_args)a).size() ==1 );
        assertEquals(1, $oneExpr.countIn(C.class));
        $args $twoExprs = $args.of( $expr.of(), $expr.of() );
        assertEquals(1, $twoExprs.countIn(C.class));
        $args $strAny = $args.of( $stringExpr.of(), $expr.of() );
        assertEquals(1, $strAny.countIn(C.class));

    }

    public void testAny(){

        assertEquals(4, $args.of().countIn(C.class));
        assertEquals(3, $args.of().countIn(E.class));
    }

    public void testDraft(){
        assertEquals( _args.of("1,2"), $args.of("$a$,$b$").draft("a", 1, "b",2));
        assertEquals( _args.of("1,'a','b',2"), $args.of("1, $a$,$b$,2").draft("a", 'a', "b",'b'));
    }

    public void testPredicate(){
        assertEquals( 1, $args.of().$and(a-> ((_args)a).isEmpty()).countIn(E.class) );
        assertEquals( 1, $args.of().$and(a-> ((_args)a).isAt(0, e-> e instanceof _binaryExpr)).countIn(E.class) );
        assertEquals( 1, $args.of().$and(a-> ((_args)a).isAt(0, _binaryExpr.class)).countIn(E.class) );
    }

    public void testToString(){
        $args $as = $args.of("1, 'c'");
        System.out.println( $as );
    }

    public void testOr(){
        $args $anyInt = $args.of($intExpr.of()); //.$and( a-> ((_arguments)a).isEmpty());
        $args $i = $args.of("i");

        assertNull($i.select("3.12f"));
        assertNull($anyInt.select("3.12f"));

        //match single arguments that are the variable i or (any int literal)
        $args aor = $args.or( $args.of("i"), $args.of($intExpr.of()) );


        assertNull(aor.select( _args.of("3.12f") ));

        assertTrue(aor.matches(_args.of("i")));
        assertTrue(aor.matches(_args.of("2")));
        assertTrue(aor.matches(_args.of(""+Integer.MAX_VALUE)));

        assertFalse( aor.matches(_args.of("3.12f")));
        assertFalse( aor.matches(_args.of("i,1")));

        $args $allLiterals =
                $args.of().$and(a-> !((_args)a).isEmpty()).$all( _expr._literal.class); //of( a-> a.allMatch(e->e instanceof _expression._literal));
        assertTrue( $allLiterals.matches("(1, 'c', \"String\", true, 1.23f, 3.45d, null)"));

        $args $allMethodCalls =
                $args.of().$and(a-> !((_args)a).isEmpty()).$all( _methodCallExpr.class );

        //matches all one or more argument argument lists that are either ALL literals or ALL methodCalls
        $args $or = $args.or($allLiterals, $allMethodCalls);

        assertFalse($or.matches( "()" )); //no arguments no match (must have at least 1 arg)
        assertTrue($or.matches( "(1)" )); //matches literal
        assertTrue($or.matches( "('v')" )); //matches literal
        assertTrue($or.matches( "(call())" )); //matches methodCall
        assertTrue($or.matches( "(1,2,3,4,5,6,'a','b',null)" )); //matches all literal
        assertTrue($or.matches( "(call(),call())" )); //matches all methodcalls

        assertFalse($or.matches( "(1,2,3,4,5,6,'a','b',null, call())" )); //NOT all literal or all methodCall

        assertTrue( $or.matches("(call(1))"));
        assertTrue( $or.matches("(c(1),c(2),c(3),c(4),c(5))"));

        //apply a predicate to the $or
        $args $orAnd = $or.$and(_as-> ((_args)_as).size() == 5);


        assertTrue( $orAnd.matches("(1,2,3,4,5)"));
        assertFalse( $orAnd.matches("(1,2,3,4,5,6)"));
        assertFalse( $orAnd.matches("(1,2,3,4)"));

        _args _as = _args.of("( c(1), c(2), c(3), c(4), c(5))");

        assertEquals(5, _as.size());
        assertTrue( $orAnd.matches(_as));
        assertFalse( $orAnd.matches("(1,2,3,c(4),5,6)"));
        assertFalse( $orAnd.matches("(1,2,3,c(4))"));

        System.out.println( $or );


        class GG{
            void a(){} //no match

        }
    }
}
