package org.jdraft.proto;

import org.jdraft.proto.$modifiers;
import org.jdraft._method;
import org.jdraft._modifiers;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SmodifiersTest extends TestCase {
    
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
