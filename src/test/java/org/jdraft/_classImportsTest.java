
package org.jdraft;

import java.util.UUID;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _classImportsTest extends TestCase {
    
    //Fix bug where we break when we encounter the first import that is already 
    //imported
    public void test_class(){
        _class _c = _class.of("aaaa.bbbb.N");
        _c.imports(String.class, UUID.class);
        System.out.println( _c );
        assertTrue( _c.hasImport(UUID.class));
    }
    
}
