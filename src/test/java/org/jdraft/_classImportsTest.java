
package org.jdraft;

import java.io.Serializable;
import java.util.Map;
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
        _c.addImports(String.class, UUID.class);
        System.out.println( _c );
        assertTrue( _c.hasImport(UUID.class));
    }

    public void test_Imports(){
        _class _c = _class.of("C");
        assertTrue(_c.hasImport("$any$"));
        _c.setPackage("aaaa.bbbb.cccc");
        assertTrue(_c.hasImport("$any$"));
        //we DONT have an import that is fixed text
        assertFalse( _c.hasImport("fixed.Text"));
        _c.addImports("fixed.Text");
        assertTrue( _c.hasImport("fixed.Text"));
        assertTrue(_c.hasImport("fixed.$any$"));

        assertFalse(_c.hasImport("aaaa.xxxx.F"));
        assertTrue(_c.hasImport("aaaa.$any$")); //because this very class is "aaaa.bbbb.cccc"
        assertTrue(_c.hasImport("aaaa.bbbb.$any$")); //because this very class is "aaaa.bbbb.cccc"
        assertTrue(_c.hasImport("aaaa.$any$.cccc")); //because this very class is "aaaa.bbbb.cccc"
        assertFalse(_c.hasImport("aaaa.$any$.xxxx")); //because this very class is "aaaa.bbbb.cccc"

        _c.addImports("aaaa.xxxx.*");
        System.out.println( _c );

        assertTrue(_c.isInPackage("aaaa.bbbb"));
        assertTrue(_c.isInPackage("aaaa.$any$"));

        assertTrue(_c.hasImport("aaaa.xxxx.*"));
        assertTrue(_c.hasImport("aaaa.xxxx.F"));
    }

    public void testIsName(){
        _class _c = _class.of("ASimpleDto");
        assertTrue( _c.isNamed("ASimpleDto"));
        assertTrue( _c.isNamed("ASimple$any$"));
        assertTrue( _c.isNamed("A$any$Dto"));
    }

    public void testImplements(){
        _class _c = _class.of("C");
        assertFalse( _c.hasImplements("A"));
        assertFalse( _c.hasImplements("aaaa.xxxx.A"));

        _c.addImplements("A");
        assertTrue( _c.hasImplements("A"));
        assertTrue( _c.hasImplements("aaaa.xxxx.A"));

        _c.removeImplements();
        assertFalse( _c.hasImplements("A"));
        assertFalse( _c.hasImplements("aaaa.xxxx.A"));

        //test implement exact class
        _c.addImplements(Map.class);
        assertTrue( _c.hasImplements(Map.class));
        assertFalse( _c.hasImplements(Serializable.class));

        _c.addImplements(Serializable.class, Map.class);
        assertTrue(_c.hasImplements(Serializable.class, Map.class));
        //doestn implement cloneable
        assertFalse(_c.hasImplements(Serializable.class, Map.class, Cloneable.class));

        _c.removeImplements(Cloneable.class);
    }
    
}
