/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.text.Translator;

/**
 *
 * @author Eric
 */
public class PrimitiveTranslatorTest extends TestCase {

    public void testPrim(){
        assertEquals( "2.0f", Translator.Primitive.translate( 2.0f ));
        //assertEquals( "2.0f", Translator.Primitive.translate( 2.0f ));
        assertEquals( "2.0f", Translator.Primitive.translate( new Float(2.0f) ));

        assertEquals( "2.0f", Translator.DEFAULT_TRANSLATOR.translate( 2.0f ));
        assertEquals( "2.0f", Translator.DEFAULT_TRANSLATOR.translate( new Float(2.0f) ));
        assertEquals( "2.0f", Translator.DEFAULT_TRANSLATOR.translate( new Float[]{2.0f} ));

        //primitive array and wrapper array types
        assertEquals( "2.0f,3.0f", Translator.DEFAULT_TRANSLATOR.translate( new Float[]{2.0f, 3.0f} ));
        assertEquals( "2.0f,3.0f", Translator.DEFAULT_TRANSLATOR.translate( new float[]{2.0f, 3.0f} ));
    }

}
