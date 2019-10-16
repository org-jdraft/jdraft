package org.jdraft.pattern;

import junit.framework.TestCase;

public class SannotationElementTest extends TestCase {

    public void testAN(){
        $annotationElement $a = $annotationElement.of();
        assertTrue($a.matches("int value();"));
        assertTrue($a.matches("int value() default 100;"));
        assertTrue($a.matches( "String[] names() default new String[]{\"1\"};"));

        $a = $annotationElement.of("int value();");
        assertTrue($a.matches("int value();"));
        assertTrue($a.matches("int value() default 100;"));
        assertTrue($a.matches("int[] value();"));

        assertFalse($a.matches( "String[] names() default new String[]{\"1\"};"));

        $a = $annotationElement.as("int value();");
        assertTrue($a.matches("int value();"));
        assertFalse($a.matches("int value() default 100;"));
        assertFalse($a.matches("int[] value();"));

    }
}
