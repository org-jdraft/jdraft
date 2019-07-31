package org.jdraft.adhoc;

import org.jdraft._class;
import org.jdraft.io._in;
import org.jdraft.io._in_classLoader;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _adhocClassLoaderTest extends TestCase {
    
     public void testCC(){
        _adhoc adhoc = _adhoc.of(_class.of("rrrr.ssss.DDDD"));
        _in_classLoader icl = _in_classLoader.of(adhoc.getClassLoader());
        _in in = icl.resolve("rrrr.ssss.DDDD");
        
        assertNotNull(in);
    }
    
}
