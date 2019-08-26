package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import org.jdraft.macro._static;
import org.jdraft.proto.$var;
import java.io.Serializable;
import java.util.List;
import junit.framework.TestCase;

public class _typeTest extends TestCase {

    public void testTypeInt(){
        class PP{

        }
        _type _t = _java.type(PP.class);
        List<_method> _ms = _t.listMembers(_method.class);
        assertTrue( _ms.isEmpty() );
        _ms = _t.listMembers(_method.class, _m-> ((_method)_m).isImplemented() );
        assertTrue( _ms.isEmpty() );
        _ms = _t.listMethods(_m -> ((_method)_m).isImplemented());
        assertTrue( _ms.isEmpty() );

        List<_field> _fs = _t.listFields();
        assertTrue( _fs.isEmpty() );
        _fs = _t.listFields( (_f)-> ((_field)_f).hasAnnos());
        assertTrue( _fs.isEmpty() );

        _t.setHeaderComment("This is a header comment", "on multiple lines");

        Comment c = _t.getHeaderComment();
        assertTrue( c.isBlockComment() );
    }

    /**
     * Here we play around with a CompilationUnit that has (2) package private classes
     */
    public void testGetPrimaryType(){
        _type _t = _java.type( TwoPackagePrivateClasses.class );
        assertEquals( 1, _t.listCompanionTypes().size());
        assertNotNull( _t.getCompanionType("AnotherPackagePrivateClass") );
        assertNotNull( _t.getCompanionType(_class.class, "AnotherPackagePrivateClass") );
        
        _t.forMembers(_method.class, _m-> System.out.println(_m) );
        
        List<_class> _cs = _t.listCompanionTypes(_class.class);
        assertEquals(1, _cs.size() );
        
        _cs = _t.listCompanionTypes(_class.class, c-> ((_class)c).getName().equals("AnotherPackagePrivateClass"));
        assertEquals(1 , _cs.size() );
        
        //_cs = _t.listPackagePrivateTypes(_class.class, (_class c)-> ((_class)c).listMembers().isEmpty() );
        
        _t.forCompanionTypes(_class.class, c-> ((_class)c).implement(Serializable.class));
        
        List<TypeDeclaration<?>> astTypes = _t.astCompilationUnit().getTypes();        
        assertNotNull(_t.astCompilationUnit().getPrimaryType().get() );
        
        _t = _java.type( PackagePrivateMultiClass.class );
        assertEquals( PackagePrivateMultiClass.class.getSimpleName(), _t.getName());
        assertEquals( PackagePrivateMultiClass.class.getCanonicalName(), _t.getFullName());
        
        //verify that I can find the 
        assertEquals( 5, $var.of("int a").count(_t));
        assertEquals( 5, $var.of("int a = 100").count(_t));        
        assertEquals( 5, $var.of("int a = 100").count(_t.astCompilationUnit()));        
        
    }
    
    public void testAddCompanionType(){
        CompilationUnit cu = StaticJavaParser.parse(
                "package aaaa.vvvvv;"+System.lineSeparator()+
                "public class C{"+System.lineSeparator()+
                "}");
        //assertTrue( cu.getPrimaryType().isPresent() );
        cu = cu.addType( Ast.enumDeclaration("enum E{}") );
        
        System.out.println( cu );
        
        //assertTrue( cu.getPrimaryType().isPresent() );
        
        
        _class _c = _class.of("aaaa.vvvv.C");
        _c.addCompanionTypes(_interface.of("public interface I{}"));
        System.out.println("AFTER ADDING TO CLASS "+  _c.astCompilationUnit() );
        
        System.out.println( "LIST CTS "+ _c.listCompanionTypes() );
        //assertTrue( _c.getCompanionType("I").isPackagePrivate() );
    }
    
    /**
     * Here is a single "PrimaryClass"
     */
    public void testPublicClassWithPackagePrivateTypes(){
        _type _t = _java.type(test.PublicTypeWithPackagePrivateTypes.class);
        
        System.out.println( _t.getPrimaryType().getName() );
        
        _t.forCompanionTypes(t -> assertTrue( ((_type)t).
            getPrimaryType().getName().equals( test.PublicTypeWithPackagePrivateTypes.class.getSimpleName())) );
        
        List<_type> ppts = _t.listCompanionTypes();        
        assertEquals(3, ppts.size());        
    }
    /*
    public void testHasImports(){
        _type _t = _java.type(_typeTest.class);
        assertTrue( _t.hasImport(_javac.class) );
        assertTrue( _t.hasImport(TestCase.class) );
        
        //This is an "impled" import... it is a top level class in the same package
        // so it returns true
        assertTrue( _t.hasImport(_type.class) );        
        
        assertTrue( _t.hasImport(_javac.class.getCanonicalName()) );
        assertTrue( _t.hasImport(TestCase.class.getCanonicalName()) );
        
        //This is an "impled" import... it is a top level class in the same package
        // so it returns true
        assertTrue( _t.hasImport(_type.class.getCanonicalName()) );        
        
        
        assertTrue( _t.getImports().hasImport(_javac.class) );
        assertTrue( _t.getImports().hasImport(TestCase.class) );
        
        //implied import
        assertTrue( _t.getImports().hasImport(_type.class) );  
        
        
        assertTrue( _t.getImports().hasImport(_javac.class.getCanonicalName()) );
        assertTrue( _t.getImports().hasImport(TestCase.class.getCanonicalName()) );
        
        //implied import
        assertTrue( _t.getImports().hasImport(_type.class.getCanonicalName()) );  

    }
*/
    
    public void testTypeImport(){
        _class _c = _class.of("aaaa.vvvv.G");
        _class _c2 = _class.of("aaaa.xxxx.G")
            .method( new Object(){ public @_static final void m(){} } );
        
        assertFalse( _c.hasImport(_c2) );
        assertFalse( _c.hasImport(_c2.getFullName()) );
        
        _c.imports("aaaa.xxxx.G");
        
        assertTrue( _c.hasImport(_c2) );
        assertTrue( _c.hasImport(_c2.getFullName()) );
        
        _c.removeImports(_c2);
        
        assertFalse( _c.hasImport(_c2) );
        assertFalse( _c.hasImport(_c2.getFullName()) );
        
        //test import static on a single 
        _c.imports( _import.of(_c2.getFullName(),true, true) );
        
        assertTrue( _c.hasImport(_c2) );
        assertTrue( _c.hasImport(_c2.getFullName()) );
        assertTrue( _c.hasImport(_c2.getFullName()+".SomeNestedClass") );
        assertTrue( _c.hasImport(_c2.getFullName()+".someNestedMethod()") );
        //assertTrue( _c.hasImport(_c2.getFullName()+".SomeNestedClass") );
        
        //doesnt import a class in the same package... only the nested members
        assertFalse( _c.hasImport(_c2.getPackage()+"."+"SomeClass") );
        
        
    }
    
    
    
    public void testImportsFromOther_type(){
        _class _c = _class.of("aaaa.vvvv.C")
                .nest( _class.of("F", new Object(){
                    int x, y; }
                    )
                );

        _class _d = _class.of( "ffff.gggg.H")
                .imports(_c, _c.getNest("F") );

        //verify I can import another _type that hasnt been drafted yet
        assertTrue( _d.hasImport(_c) );

        //verify I can import a nested type that hasnt been drafted yet
        assertTrue( _d.hasImport(_c.getNest("F")) );
        //_javac.of( _c, _d); //verify they both compile

        System.out.println(_d );
    }

    public void testS(){
        _type _t = _class.of("C").field("int x");

        //Not sure WHY I have to do this (_field)cast
        _t.forFields( f-> ((_field)f).isType(int.class) );
    }
}
