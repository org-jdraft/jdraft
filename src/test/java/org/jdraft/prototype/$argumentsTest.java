package org.jdraft.prototype;

import junit.framework.TestCase;
import org.jdraft._arguments;
import org.jdraft._binaryExpression;
import org.jdraft._int;

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

    public void testMatchingAny(){
        assertTrue($arguments.of("$args$").matches("()"));
        assertTrue($arguments.of("$args$").matches("(i)"));
        assertTrue($arguments.of("$args$").matches("(i)"));
        assertTrue($arguments.of("$args$").matches("(i, 'c')"));

        assertEquals(_arguments.of("i, 1"),
                $arguments.of("$args$").select("(i, 1)").selection);
    }

    public void testFromString(){
        assertEquals( _arguments.of(), _arguments.of(""));
        //System.out.println(_arguments.of("()") );
        //System.out.println(_arguments.of(""));

        assertEquals( _arguments.of("()"), _arguments.of(""));

        assertTrue($arguments.of("").matches(_arguments.of()));
        assertTrue($arguments.of("()").matches(_arguments.of()));
        assertTrue($arguments.of("()").matches(_arguments.of("1")));
    }

    public void testIndv(){
        assertEquals(1, $arguments.of( $int.of() ).countIn(E.class));
    }

    public void testIndividualArgs(){

        assertEquals(1, $arguments.of( $e.of(_int.class) ).countIn(E.class));
        assertEquals(1, $arguments.of( $e.of(_binaryExpression.class) ).countIn(E.class));

        $arguments $oneExpr = $arguments.of( a->a.size() ==1 );
        assertEquals(1, $oneExpr.countIn(C.class));
        $arguments $twoExprs = $arguments.of( $e.of(), $e.of() );
        assertEquals(1, $twoExprs.countIn(C.class));
        $arguments $strAny = $arguments.of( $string.of(), $e.of() );
        assertEquals(1, $strAny.countIn(C.class));

    }

    public void testAny(){

        assertEquals(4, $arguments.of().countIn(C.class));
        assertEquals(3, $arguments.of().countIn(E.class));
    }

    public void testDraft(){
        assertEquals( _arguments.of("1,2"), $arguments.of("$a$,$b$").draft("a", 1, "b",2));
        assertEquals( _arguments.of("1,'a','b',2"), $arguments.of("1, $a$,$b$,2").draft("a", 'a', "b",'b'));
    }

    public void testPredicate(){
        assertEquals( 1, $arguments.of( a-> a.isEmpty()).countIn(E.class) );
        assertEquals( 1, $arguments.of( a-> a.isAt(0, e-> e instanceof _binaryExpression)).countIn(E.class) );
        assertEquals( 1, $arguments.of( a-> a.isAt(0, _binaryExpression.class)).countIn(E.class) );
    }

    public void testOr(){
        $arguments $anyInt = $arguments.of($int.of());
        $arguments $i = $arguments.of("i");

        assertNull($i.select("3.12f"));
        assertNull($anyInt.select("3.12f"));

        //match single arguments that are the variable i or (any int literal)
        $arguments.Or aor = $arguments.or( $arguments.of("i"), $arguments.of($int.of()) );
        
        assertNull(aor.select( _arguments.of("3.12f") ));

        assertTrue(aor.matches(_arguments.of("i")));
        assertTrue(aor.matches(_arguments.of("2")));
        assertTrue(aor.matches(_arguments.of(""+Integer.MAX_VALUE)));

        assertFalse( aor.matches(_arguments.of("3.12f")));
        assertFalse( aor.matches(_arguments.of("i,1")));
    }
}
