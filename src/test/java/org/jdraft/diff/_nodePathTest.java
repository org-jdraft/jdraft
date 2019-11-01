package org.jdraft.diff;

import org.jdraft._anno;
import org.jdraft._java.Component;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _nodePathTest extends TestCase {
    
    public void testP(){
        _nodePath p = _nodePath.of(Component.ANNO, "N");
        assertTrue( p.isLeaf(_anno.class) );
    }
}
