package org.jdraft.pattern;

import org.jdraft._method;
import junit.framework.TestCase;
import org.jdraft._modifier;
import org.jdraft._modifiers;

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
        assertNotNull( $modifiers.of(_m).select(_modifiers.of(_modifier.PUBLIC)) );
        assertNull( $modifiers.of(_m.node()).select(_modifiers.of()) );
        assertNotNull( $modifiers.of(_m.node()).select(_modifiers.of().setPublic()) );
    }
    
}
