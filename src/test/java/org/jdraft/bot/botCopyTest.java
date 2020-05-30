package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._binaryExpr;
import org.jdraft._expr;
import org.jdraft._method;
import org.jdraft._args;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * I Want to check what happens when
 *
 */
public class botCopyTest extends TestCase {

    public void testArgs(){
        $args $args = org.jdraft.bot.$args.of();
        class C{
            void m(){
                System.out.println(); //no arguments
                System.out.println( 1 ); //intLiteral
                System.out.println( 1 + 3 ); //binaryExpr

                va(1, new HashMap()/*this is also an args list*/, 3+4); //3 arguments
            }

            void va(Object...as){
            }
        }

        //arguments were all of the arguments are literals

        assertEquals( 2, org.jdraft.bot.$args.empty().countIn(C.class));
        assertEquals( 1, org.jdraft.bot.$args.of().$and(a-> ((_args)a).size() > 0).$all(e-> ((_expr)e).isLiteral()).countIn(C.class));
        assertEquals( 1, org.jdraft.bot.$args.of().$and(a-> ((_args)a).size() > 0).$all(_expr._literal.class).countIn(C.class));

        assertEquals( 1, org.jdraft.bot.$args.notEmpty().$all(e-> ((_expr)e).isLiteral()) .countIn(C.class));
        assertEquals( 1, org.jdraft.bot.$args.notEmpty().$all(_expr._literal.class) .countIn(C.class));
        //arguments lists where ANY ONE of the arguments are literals
        assertEquals( 2, org.jdraft.bot.$args.of().$any(e-> ((_expr)e).isLiteral() ).countIn(C.class) );
        assertEquals( 2, org.jdraft.bot.$args.of().$any( _expr._literal.class ).countIn(C.class) );


        assertEquals( 5, $args.countIn(C.class));
        assertEquals( 1, org.jdraft.bot.$args.of().$and(a-> ((_args)a).size() ==3).countIn(C.class));
        assertEquals( 2, org.jdraft.bot.$args.of().$any(_binaryExpr.class).countIn(C.class));
        assertEquals( 1, org.jdraft.bot.$args.of().$and(a-> ((_args)a).size() ==3).$any(_binaryExpr.class).countIn(C.class));

    }

    public void test$annoClone(){
        $annoExpr $a = $annoExpr.of();
        $annoExpr $b = $a.copy();
        $b.$name("A");

        assertTrue($a.matches("@A"));
        assertTrue($a.matches("@A(1)"));
        assertTrue($a.matches("@B"));
        assertTrue($a.matches("@A(2)"));

        assertTrue($b.matches("@A"));
        assertTrue($b.matches("@A(1)"));
        assertFalse($b.matches("@B"));
        assertFalse($b.matches("@B(2)"));

        $b.$and(a-> a.isEntryPairedMembers());
        assertTrue($b.matches("@A(key=2)"));
        assertFalse($b.matches("@A(2)"));

    }


    public void testG(){
        Predicate<_method> orig = (m)->m.isStatic();
        Predicate<_method> clone = orig.and(m-> m.isPublic());

        assertTrue( orig.test( _method.of( "public static void m(){}")));
        assertTrue( clone.test( _method.of( "public static void m(){}")));

        assertTrue( orig.test( _method.of( "private static void m(){}")));
        assertFalse( clone.test( _method.of( "private static void m(){}")));

        orig = orig.negate();
        assertFalse( orig.test( _method.of( "private static void m(){}")));
        assertFalse( clone.test( _method.of( "private static void m(){}")));

        assertFalse( orig.test( _method.of( "public static void m(){}")));
        assertTrue( clone.test( _method.of( "public static void m(){}")));
    }
}
