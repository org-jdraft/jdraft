package org.jdraft;

import com.github.javaparser.ast.ImportDeclaration;
import java.io.IOException;
import java.lang.reflect.Method;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Map;

/*** THE JUNIT TESTS REQUIRE THIS IMPORT */
import static org.jdraft.Ast.importDeclaration; /** DONT REMOVE */

/**
 *
 * @author Eric
 */
public class _importTest extends TestCase {

    public void testNewApiEmpty(){
        _imports _ts = _imports.of();
        assertEquals(_ts, _ts.copy());
        assertEquals(_ts.hashCode(), _ts.copy().hashCode());


        assertFalse(_ts.has(_import.of(IOException.class)));
        assertTrue(_ts.is( new ArrayList<>()));
        assertTrue(_ts.isEmpty());
        assertEquals(0, _ts.size());
        assertTrue(_ts.list().isEmpty());
        assertTrue(_ts.astList().isEmpty());
        _ts.toEach(t-> System.out.println(t));

    }

    public void testMultipleStaticImports(){
        _class _c = _class.of("A")
                .addImportStatic("com.mattel.Hoverboard.Boards.*;",
                "com.mattel.Hoverboard.createNimbus;",
                "java.util.Collections.*;");

    }
    public void testImportPackageWildcard(){
        _import _i = _import.of("java.util.*");
    }
    
    public void testToString(){
        assertEquals("import java.io.IOException;", _import.of(IOException.class).toString().trim());
        _class _c = _class.of("C").addImports(IOException.class, Map.class);
        System.out.println( _c.getImports().toString().trim() );
        assertEquals("import java.io.IOException;"+System.lineSeparator() + "import java.util.Map;", _c.getImports().toString().trim());
        //assertEquals("import java.io.IOException;", _c.getImports().toString().trim());
    }
    public void setStaticWildcardImport(){
        _import _i = _import.of(Ast.class).setStatic(true).setWildcard(true);


        //static wildcard
        ImportDeclaration id = Ast.importDeclaration(Ast.class)
                .setAsterisk( true).setStatic( true);
        assertTrue( _i.is(id) );        
        assertTrue( _i.hasImport(Ast.class) );                
    }
    
    public void testStaticImportMethod() throws NoSuchMethodException{
        Method m = Ast.class.getMethod("importDeclaration", Class.class);        
        _import _i = _import.of( m );
        assertTrue( _i.is(m) );
        assertTrue( _i.hasImport(m) );
        assertTrue( _i.isStatic() );
        assertFalse( _i.isWildcard() );
    }
    
    public void testImportClassWildcard() {
        _import _i = _import.of( Ast.class).setWildcard(true);
        assertTrue( _i.isWildcard() );
        assertFalse( _i.isStatic() );
    }
    
    public void testImportWildcardString(){
        _import _i = _import.of("import java.util.*;");
        assertTrue( _i.hasImport(Map.class));
        assertTrue( _i.isWildcard());
        assertFalse( _i.isStatic());
        
    }
    
    public void testImp() throws NoSuchMethodException{
        _import _i = _import.of( Ast.importDeclaration(IOException.class));
        assertTrue( _i.hasImport(IOException.class) );                
    }
    
    public void test_imports() throws NoSuchMethodException{
        //this is to just formalize the use of the static import
        importDeclaration("hey");

        _class _c = _class.of(_importTest.class);
        _imports _is = _imports.of( _c.astCompilationUnit());
        assertTrue( _is.hasImport(IOException.class));
        assertTrue( _is.hasImport(IOException.class.getCanonicalName()));
        assertFalse( _is.hasImport(java.net.URISyntaxException.class));
        assertFalse( _is.hasImport(java.net.URISyntaxException.class.getCanonicalName()));
        assertTrue( _is.hasImports(IOException.class, TestCase.class, _imports.class, Map.class ) );
        assertTrue( _is.hasImports(IOException.class.getCanonicalName(), 
                TestCase.class.getCanonicalName(), 
                _imports.class.getCanonicalName(), 
                Map.class.getCanonicalName() ) );
        
        Method m = Ast.class.getMethod("importDeclaration", Class.class);        
        assertTrue( _is.hasImport(m) );
        
        //by default I "Implied Import" top level classes in the same package
        assertTrue( _is.hasImport(_classTest.class) );
        
    }
}
