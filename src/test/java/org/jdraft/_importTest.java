package org.jdraft;

import com.github.javaparser.ast.ImportDeclaration;
import org.jdraft.Ast;
import org.jdraft.Ast;
import java.io.IOException;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.jdraft.Ast.*; //add all static types on Ast 
import org.jdraft._class;
import org.jdraft._class;
import org.jdraft._classTest;
import org.jdraft._import;
import org.jdraft._import;
import org.jdraft._imports;
import java.util.Map;
//import static draft.java.Ast.importDeclaration; //add a single static methods on Ast

/**
 *
 * @author Eric
 */
//@cache
public class _importTest extends TestCase {

    public void testMultipleStaticImports(){
        _class _c = _class.of("A")
                .importStatic("com.mattel.Hoverboard.Boards.*;",
                "com.mattel.Hoverboard.createNimbus;",
                "java.util.Collections.*;");

    }
    public void testImportPackageWildcard(){
        _import _i = _import.of("java.util.*");
    }
    
    public void testToString(){
        assertEquals("import java.io.IOException;", _import.of(IOException.class).toString().trim());
        _class _c = _class.of("C").imports(IOException.class, Map.class);
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
