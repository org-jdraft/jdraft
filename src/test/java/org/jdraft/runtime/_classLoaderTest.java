package org.jdraft.runtime;

import org.jdraft._class;
import org.jdraft.io._in;
import org.jdraft.io._in_classLoader;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _classLoaderTest extends TestCase {
    
     public void testCC(){
        _runtime adhoc = _runtime.of(_class.of("rrrr.ssss.DDDD"));
        _in_classLoader icl = _in_classLoader.of(adhoc.getClassLoader());
        _in in = icl.resolve("rrrr.ssss.DDDD");
        
        assertNotNull(in);
    }
    
}
