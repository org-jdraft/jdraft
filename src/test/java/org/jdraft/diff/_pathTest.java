package org.jdraft.diff;

import org.jdraft.diff._path;
import org.jdraft._anno;
import org.jdraft._java.Component;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _pathTest extends TestCase {
    
    public void testP(){
        _path p = _path.of(Component.ANNO, "N");
        assertTrue( p.isLeaf(_anno.class) );
    }
}
