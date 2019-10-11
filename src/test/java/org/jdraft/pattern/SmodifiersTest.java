package org.jdraft.pattern;

import org.jdraft._method;
import org.jdraft._modifiers;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SmodifiersTest extends TestCase {

    public void test$modifiersAsTest(){

        $modifiers $ms = $modifiers.as("public static");

        assertTrue( $ms.matches("public static") );
        assertFalse( $ms.matches("public static final") );
        assertFalse( $ms.matches("public") );
        assertFalse( $ms.matches("static") );

    }

    static _method _m = _method.of(new Object(){
        public void m(){
        }
    });
    
    public void testAny(){
        assertNotNull( $modifiers.of().select(_modifiers.of()) );
        
    }
    public void testAll(){
        assertNull( $modifiers.of(_m).select(_modifiers.of()) );
        assertNotNull( $modifiers.of(_m).select(_modifiers.of(_modifiers.PUBLIC)) );
        assertNull( $modifiers.of(_m.ast()).select(_modifiers.of()) );
        assertNotNull( $modifiers.of(_m.ast()).select(_modifiers.of().setPublic()) );
    }
    
}
