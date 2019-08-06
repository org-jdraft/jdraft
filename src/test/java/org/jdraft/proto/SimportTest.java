package org.jdraft.proto;

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
    
    public void testImportSwitch(){
        _class _c = _class.of("C").imports( _import.of("import draft.java.io.*;"));
        $import.of("import draft.java.$any$;").replaceIn(_c, "import org.jdraft.$any$;");
        System.out.println( _c ); 
        assertTrue( _c.hasImport(_i-> _i.is("import org.jdraft.io.*;") ));
        
    }
    
    public void testAnyWildcardReplace(){
        
        _class _c = _class.of("aaaa.F").imports(Map.class);
        $import.of("java.util.Map;").replaceIn(_c, "import java.util.*;");
        assertTrue( _c.hasImport("java.util.*;"));
        
        $import $i = $import.of("java.util.$any$;");
        _import _i = _import.of("java.util.*;");
        assertEquals("java.util", _i.getName());
        
        System.out.println( $i.importPattern );
        assertTrue( $i.matches("java.util.*"));
        
        _c = _class.of("F").imports("java.util.*;");
        assertEquals(1, $import.of("java.util.$any$;").count(_c ));
        
        $import.of("java.util.$any$;").replaceIn(_c, "import java.net.$any$;");
        assertTrue( _c.hasImport("java.net.*;"));       
        
        _c = _class.of("R").imports("draft.java.io.*;");
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
        assertEquals( _import.of("a.MyClass"), $i.compose() );
        
        //IF the prototype is marked static it WILL match
        $i = $import.of("import static a.MyClass");
        assertFalse( $i.matches("import a.MyClass"));
        assertTrue( $i.matches("import static a.MyClass"));
        //assertTrue( $i.matches("import static a.MyClass.*"));
        
        //the proto is static, the composed is ALSO static
        assertEquals( _import.of("a.MyClass").setStatic(), $i.compose() );
    }
    
    /**
     * When we have a wildcard that is part of a $var$ like $any$
     * verify it is commuted
     */
    public void testAssumeWildcardReplaceCommutability(){
        assertTrue( $import.of("a.$any$").matches("import a.MyClass.*"));
        
        _class _c = _class.of("F").imports("a.MyClass.*;");
        $import.of("a.$any$").replaceIn(_c, "b.$any$");
        assertTrue( _c.hasImport("b.MyClass.*;"));
    }
    
    public void testWildcardImports(){
        $import $i = $import.of("import a.MyClass");        
        assertTrue( $i.matches("import a.MyClass"));
        
        
          
        //should match a wildcard import        
        //assertTrue( $i.matches("import a.MyClass.*"));
        

        //the proto is not static, the composed is not static
        assertEquals( _import.of("a.MyClass"), $i.compose() );
        
        //IF the prototype is marked static it WILL match
        $i = $import.of("import a.MyClass.*");
        assertFalse( $i.matches("import a.MyClass"));
        assertTrue( $i.matches("import a.MyClass.*"));
        
        //the proto is wildcard, the composed is ALSO a wildcard
        assertEquals( _import.of("a.MyClass.*"), $i.compose() );
    }
    
    
    public void testImportWildcardStaticAssertions(){
        _class _c = _class.of("C").importStatic(Assert.class);
        $import.of("import static org.junit.Assert;").replaceIn( _c, 
            _import.of(MatcherAssert.class).setStatic().setWildcard() );
        
        System.out.println( _c );
        
        assertFalse( _c.hasImport(Assert.class) );
        assertTrue( _c.hasImportStatic(MatcherAssert.class));
        assertTrue( _c.hasImport(MatcherAssert.class) );        
        
        _c = _class.of("C").imports(Assert.class);
        $import.of(Assert.class.getCanonicalName()).replaceIn( _c, 
            _import.of(MatcherAssert.class.getCanonicalName()) );
        
        assertFalse( _c.hasImport(Assert.class) );
        assertTrue( _c.hasImport(MatcherAssert.class));
    }
    
    public void testMatch(){
        $import $i = $import.of(Map.class);
        assertTrue( $i.matches(_import.of(Map.class)));
        assertTrue( $i.matches(_import.of("java.util.Map")));
        
        //only static
        $i = $import.of("import static java.util.Map;");
        assertFalse( $i.matches(_import.of("java.util.Map")));
        assertFalse( $i.matches(_import.of("java.util.Map").setWildcard()));
        assertFalse( $i.matches(_import.of("java.util.Map").setWildcard().setStatic()));
        assertTrue( $i.matches(_import.of("java.util.Map").setStatic()));
        
        //wildcard AND static
        $i = $import.of("import static java.util.Map.*;");
        assertTrue( $i.matches(_import.of("java.util.Map").setWildcard().setStatic()));
        assertFalse( $i.matches(_import.of("java.util.Map").setWildcard()));
        assertFalse( $i.matches(_import.of("java.util.Map").setStatic()));
        assertFalse( $i.matches(_import.of("java.util.Map")) );
        
        $i = $import.of("import static java.util.Map;");
        assertTrue( $i.isStatic );
        assertFalse( $i.matches(_import.of("java.util.Map").setWildcard().setStatic()));
        assertFalse( $i.matches(_import.of("java.util.Map").setWildcard()));
        assertTrue( $i.matches(_import.of("java.util.Map").setStatic()));
        assertFalse( $i.matches(_import.of("java.util.Map")) );
    }
    
    public void testMatchRegularOrStatic(){
        _class _c = _class.of( "C").imports(Map.class);
        
        _class _cs = _class.of( "C").importStatic(Map.class);
        
        //System.out.println( _class.of("C").importStatic(Map.class));
        assertNotNull( $import.of(Map.class).firstIn(_c ) );
        $import $is = $import.of("import static java.util.Map;");
        assertTrue( $is.isStatic);
        assertTrue( $is.matches(_import.of(Map.class).setStatic()));
        assertTrue( $is.matches("import static java.util.Map"));
        
        assertTrue( _cs.isTopLevel() );
        System.out.println( _cs );
        assertNotNull( $is.firstIn(_cs ) );
        
        assertNotNull( $import.of("import static java.util.Map;").firstIn(_cs) );
        assertNotNull( $import.of(Map.class, true, false).firstIn(_cs) );
        assertNotNull( $import.of(Map.class).selectFirstIn(_c ) );
        assertNotNull( $import.of(Map.class, true, false).selectFirstIn(_cs ) );
        
        assertNotNull( $import.of(Map.class, true, false).firstIn(_cs, i-> i.isStatic() && !i.isWildcard()) );
        assertNotNull( $import.of(Map.class).firstIn(_c, i -> i.is(Map.class)) );
        assertNotNull( $import.of(Map.class).selectFirstIn(_c, i-> i.is(Map.class)) );
        assertNotNull( $import.of(Map.class).selectFirstIn(_cs, i-> i.isStatic()) );
        
        assertNotNull( $import.of(i-> i.isStatic()).firstIn(_cs) );
        
    }
   
    
    public void testConstantTemplate(){        
        _class _c = _class.of("C", new Object(){
            Map m = null;            
        });
        _c.imports(Assert.class);
        
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
