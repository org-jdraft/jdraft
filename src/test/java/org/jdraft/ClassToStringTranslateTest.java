package org.jdraft;

import junit.framework.TestCase;

import java.util.Map;

import org.jdraft.text.Translator;

public class  ClassToStringTranslateTest
        extends TestCase {

    public void testClassToString(){
        assertEquals("java.util.Map", Translator.ClassToString.translate(Map.class));
    }
}