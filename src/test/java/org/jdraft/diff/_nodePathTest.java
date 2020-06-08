package org.jdraft.diff;

import org.jdraft._annoExpr;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _nodePathTest extends TestCase {
    
    public void testP(){
        _nodePath p = _nodePath.of(Feature.ANNO_EXPR, "N");
        assertTrue( p.isLeaf(_annoExpr.class) );
    }
}
