package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._method;

import java.util.function.Predicate;

/**
 * I Want to check what happens when
 *
 */
public class cloneTest extends TestCase {

    public void test$annoClone(){
        $anno $a = $anno.of();
        $anno $b = $a.copy();
        $b.$name("A");

        assertTrue($a.matches("@A"));
        assertTrue($a.matches("@A(1)"));
        assertTrue($a.matches("@B"));
        assertTrue($a.matches("@A(2)"));

        assertTrue($b.matches("@A"));
        assertTrue($b.matches("@A(1)"));
        assertFalse($b.matches("@B"));
        assertFalse($b.matches("@B(2)"));

        $b.$and(a-> a.isKeyValueAnnotation());
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
