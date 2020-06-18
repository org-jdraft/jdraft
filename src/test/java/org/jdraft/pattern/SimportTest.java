package org.jdraft.pattern;

import com.github.javaparser.ast.ImportDeclaration;
import org.jdraft._class;
import org.jdraft._import;

import java.util.Map;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;

/**
 *
 * @author Eric
 */
public class SimportTest extends TestCase {

    public void testMatchOf(){
        $import $i = $import.of("aaaa.AAAA");
        assertTrue($i.matches("import aaaa.AAAA;"));
        assertTrue($i.matches("import static aaaa.AAAA;"));
        //assertTrue($i.matches("import aaaa.AAAA.*;"));
        //assertTrue($i.matches("import static aaaa.AAAA.*;"));

        $i = $import.of("aaaa.$name$");
        assertTrue($i.matches("import aaaa.AAAA;"));
        assertTrue($i.matches("import aaaa.AnyName;"));
        assertTrue($i.matches("import aaaa.AnyName.*;"));
        assertTrue($i.matches("import aaaa.subpackage.AnyName;"));
        assertTrue($i.matches("import static aaaa.AAAA;"));
        assertTrue($i.matches("import aaaa.subpackage.AnyName;"));
        assertFalse($i.matches("import aaa.subpackage.AnyName;"));

        $i = $import.of("$pkg$Map;");
        assertTrue($i.matches(_import.of(Map.class)));
        assertTrue($i.matches("aaa.Map"));
        assertTrue($i.matches("Map"));

        assertTrue($i.matches("pkg.Map.*"));
    }

    public void testMatchAs(){
        $import $i = $import.as("aaaa.AAAA");
        assertTrue( $i.matches("aaaa.AAAA"));
        assertFalse( $i.matches("import static aaaa.AAAA;"));
    }
    public void testImportSwitch(){
        _class _c = _class.of("C").addImports( _import.of("import draft.java.io.*;"));
        $import.of("import draft.java.$any$;").replaceIn(_c, "import org.jdraft.$any$;");
        System.out.println( _c ); 
        assertTrue( _c.hasImport(_i-> _i.is("import org.jdraft.io.*;") ));
    }
    
    public void testAnyWildcardReplace(){
        
        _class _c = _class.of("aaaa.F").addImports(Map.class);
        $import.of("java.util.Map;").replaceIn(_c, "import java.util.*;");
        assertTrue( _c.hasImport("java.util.*;"));
        
        $import $i = $import.of("java.util$any$;");
        _import _i = _import.of("java.util.*;");
        assertEquals("java.util", _i.getName());
        
        System.out.println( $i.importStencil);
        assertTrue( $i.matches("java.util.*"));
        
        _c = _class.of("F").addImports("java.util.*;");
        assertEquals(1, $import.of("java.util$any$;").countIn(_c ));
        
        $import.of("java.util$any$;").replaceIn(_c, "import java.net$any$;");
        assertTrue( _c.hasImport("java.net.*;"));       
        
        _c = _class.of("R").addImports("draft.java.io.*;");
        $import.of("draft.java.$any$;").replaceIn(_c, "com.draftj.$any$");
        System.out.println(_c);
        
        
    }
    
    public void testPostSet(){
        $import $i = $import.of("import a.MyClass");
        assertTrue( $i.matches("import a.MyClass"));
        
        $i = $import.of("import static a.MyClass");
        assertFalse( $i.matches("import a.MyClass"));        
        assertTrue( $i.matches("import static a.MyClass"));
        
        $i = $import.of("import static a.MyClass.*");
        assertFalse( $i.matches("import static a.MyClass"));
        assertTrue( $i.matches("import static a.MyClass.*"));
    }
    
    public void testStaticImports(){
        $import $i = $import.of("import a.MyClass");
        assertTrue( $i.matches("import a.MyClass"));
        //assertTrue( $i.matches("import a.MyClass.*"));
        assertTrue( $i.matches("import static a.MyClass"));

        //the proto is not static, the composed is not static
        assertEquals( _import.of("a.MyClass"), $i.draft() );
        
        //IF the prototype is marked static it WILL match
        $i = $import.of("import static a.MyClass");
        assertFalse( $i.matches("import a.MyClass"));
        assertTrue( $i.matches("import static a.MyClass"));
        //assertTrue( $i.matches("import static a.MyClass.*"));
        
        //the proto is static, the composed is ALSO static
        assertEquals( _import.of("a.MyClass").setStatic(), $i.draft() );
    }
    
    /**
     * When we have a wildcard that is part of a $var$ like $any$
     * verify it is commuted
     */
    public void testAssumeWildcardReplaceCommutability(){
        assertTrue( $import.of("a.$any$").matches("import a.MyClass.*"));
        
        _class _c = _class.of("F").addImports("a.MyClass.*;");
        $import.of("a.$any$").replaceIn(_c, "b.$any$");
        assertTrue( _c.hasImport("b.MyClass.*;"));
    }
    
    public void testWildcardImports(){
        $import $i = $import.of("import a.MyClass");        
        assertTrue( $i.matches("import a.MyClass"));
        
        
          
        //should match a wildcard import        
        //assertTrue( $i.matches("import a.MyClass.*"));
        

        //the proto is not static, the composed is not static
        assertEquals( _import.of("a.MyClass"), $i.draft() );
        
        //IF the prototype is marked static it WILL match
        $i = $import.of("import a.MyClass.*");
        assertFalse( $i.matches("import a.MyClass"));
        assertTrue( $i.matches("import a.MyClass.*"));
        
        //the proto is wildcard, the composed is ALSO a wildcard
        assertEquals( _import.of("a.MyClass.*"), $i.draft() );
    }

    public void testImportWildcardStaticAssertions(){
        _class _c = _class.of("C").addImportStatic(Assert.class);
        $import.of("import static org.junit.Assert.*;").replaceIn( _c,
            _import.of(MatcherAssert.class).setStatic().setWildcard() );
        
        System.out.println( _c );
        
        assertFalse( _c.hasImport(Assert.class) );
        assertTrue( _c.hasImportStatic(MatcherAssert.class));
        assertTrue( _c.hasImport(MatcherAssert.class) );        
        
        _c = _class.of("C").addImports(Assert.class);
        $import.of(Assert.class.getCanonicalName()).replaceIn( _c, 
            _import.of(MatcherAssert.class.getCanonicalName()) );
        
        assertFalse( _c.hasImport(Assert.class) );
        assertTrue( _c.hasImport(MatcherAssert.class));
    }

    public void testFailMatch(){
        $import $i = $import.of("import static java.util.Map;");
        _import _i = _import.of("java.util.Map").setWildcard().setStatic();
        System.out.println( _i.node.getName() );
        assertTrue( $i.matches(_import.of("java.util.Map").setWildcard().setStatic()));
    }

    public void testM(){
        $import $i = $import.of("import static java.util.Map.*;");
        System.out.println( $i.importStencil );
        _import _ii = _import.of("java.util.Map").setWildcard().setStatic();
        System.out.println( _ii );
        assertTrue( $i.matches( _ii) );
    }
    public void testMatch(){
        $import $i = $import.of(Map.class);
        assertTrue( $i.matches(_import.of(Map.class)));
        assertTrue( $i.matches(_import.of("java.util.Map")));
        
        //only static
        $i = $import.of("import static java.util.Map;");
        assertFalse( $i.matches(_import.of("java.util.Map")));
        assertFalse( $i.matches(_import.of("java.util.Map").setWildcard()));
        assertTrue( $i.matches(_import.of("java.util.Map").setWildcard().setStatic()));
        assertTrue( $i.matches(_import.of("java.util.Map").setStatic()));
        
        //wildcard AND static
        $i = $import.of("import static java.util.Map.*;");
        assertTrue( $i.matches(_import.of("java.util.Map").setWildcard().setStatic()));
        assertFalse( $i.matches(_import.of("java.util.Map").setWildcard()));
        assertFalse( $i.matches(_import.of("java.util.Map").setStatic()));
        assertFalse( $i.matches(_import.of("java.util.Map")) );
        
        $i = $import.of("import static java.util.Map;");
        assertTrue( $i.isStatic );
        assertTrue( $i.matches(_import.of("java.util.Map").setWildcard().setStatic()));
        assertFalse( $i.matches(_import.of("java.util.Map").setWildcard()));
        assertTrue( $i.matches(_import.of("java.util.Map").setStatic()));
        assertFalse( $i.matches(_import.of("java.util.Map")) );
    }
    
    public void testMatchRegularOrStatic(){
        _class _c = _class.of( "C").addImports(Map.class);
        
        _class _cs = _class.of( "C").addImportStatic(Map.class);
        
        //System.out.println( _class.of("C").importStatic(Map.class));
        assertNotNull( $import.of(Map.class).firstIn(_c ) );
        $import $is = $import.of("import static java.util.Map.*;");
        assertTrue( $is.isStatic);
        assertTrue( $is.matches(_import.of(Map.class).setWildcard().setStatic()));
        assertTrue( $is.matches("import static java.util.Map.*"));
        
        assertTrue( _cs.isTopLevel() );
        System.out.println( _cs );
        assertNotNull( $is.firstIn(_cs ) );
        
        assertNotNull( $import.of("import static java.util.Map.*;").firstIn(_cs) );
        assertNotNull( $import.of(Map.class, true, true).firstIn(_cs) );
        assertNotNull( $import.of(Map.class).selectFirstIn(_c ) );
        assertNotNull( $import.of(Map.class, true, true).selectFirstIn(_cs ) );
        
        assertNotNull( $import.of(Map.class, true, true).firstIn(_cs, i-> i.isStatic() && i.isWildcard()) );
        assertNotNull( $import.of(Map.class).firstIn(_c, i -> i.is(Map.class)) );
        assertNotNull( $import.of(Map.class).selectFirstIn(_c, i-> i.is(Map.class)) );
        assertNotNull( $import.of(i-> i.isStatic()).firstIn(_cs) );
        
    }

    public void testImportStatic(){
        _class _cs = _class.of( "C").addImportStatic(Map.class);
        //assertTrue( $import.of().count(_cs) == 1);
        //assertTrue( $import.of(Map.class, true, true).count(_cs) == 1);

        //assertNotNull( $import.of("java.util.Map").select(new ImportDeclaration("java.util.Map", false, false)) );
        //assertNotNull( $import.of("java.util.Map").select(new ImportDeclaration("java.util.Map", true, false)) );
        //assertNotNull( $import.of("java.util.*").select(new ImportDeclaration("java.util", false, true)) );
        _import _i = _import.of( new ImportDeclaration("java.util", false, true) );
        //System.out.println( _i );
        _i = _import.of( new ImportDeclaration("java.util.Map", true, true) );
        System.out.println( _i );
        assertTrue( _i.isStatic() );
        assertTrue( _i.isWildcard() );
        assertNull( $import.of(Map.class).isStatic);
        assertNull( $import.of(Map.class).isWildcard);

        assertNotNull( $import.of("java.util.Map").select(new ImportDeclaration("java.util.Map", true, true)) );

        assertTrue( $import.of(Map.class).countIn(_cs) == 1);
        System.out.println( _cs );


        //$import.of(Map.class).

        //assertNotNull( $import.of(Map.class).selectFirstIn(_cs, i-> i.isStatic()) );
    }
    
    public void testConstantTemplate(){        
        _class _c = _class.of("C", new Object(){
            Map m = null;            
        });
        _c.addImports(Assert.class);
        
        $import $i = $import.of(Assert.class);
        $i.replaceIn(_c, MatcherAssert.class );
        
        System.out.println(_c);
    }
    
    public void testDynamicTemplate(){
        //pImport $i = pImport.of("import static draft.java.Ast;").setWildcard();
        //assertTrue( $i.matches("import static draft.java.Ast.*;") );
        
        assertTrue( $import.of("import static draft.java.Ast.*;").matches("import static draft.java.Ast.*;"));                      
    }
}
