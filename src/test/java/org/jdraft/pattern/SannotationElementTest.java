package org.jdraft.pattern;

import junit.framework.TestCase;

public class SannotationElementTest extends TestCase {

    public void testAN(){
        $annotationEntry $a = $annotationEntry.of();
        assertTrue($a.matches("int value();"));
        assertTrue($a.matches("int value() default 100;"));
        assertTrue($a.matches( "String[] names() default new String[]{\"1\"};"));

        $a = $annotationEntry.of("int value();");
        assertTrue($a.matches("int value();"));
        assertTrue($a.matches("int value() default 100;"));
        assertTrue($a.matches("int[] value();"));

        assertFalse($a.matches( "String[] names() default new String[]{\"1\"};"));

        $a = $annotationEntry.as("int value();");
        assertTrue($a.matches("int value();"));
        assertFalse($a.matches("int value() default 100;"));
        assertFalse($a.matches("int[] value();"));

    }
}
