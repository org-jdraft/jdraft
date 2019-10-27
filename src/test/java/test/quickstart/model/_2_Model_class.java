package test.quickstart.model;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import junit.framework.TestCase;
import org.jdraft._class;

import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * "read myself in" or read in the .java source code for the Class (_2_Quickstart)
 * at Runtime and build a _class (_c) to analyze and manipulate the .java source code
 * interaction with the JavaParser CompilationUnit, ClassOrInterfaceDeclaration &
 */
public class _2_Model_class extends TestCase {

    public void testModelForThisClass(){
        // create a _class (_c) model for the java source of this runtime Class(_2_Quickstart)
        _class _c = _class.of( _2_Model_class.class );

        // we can get properties of the _class (_c) similar to Reflection
        assertEquals( _2_Model_class.class.getSimpleName(), _c.getName() );
        assertEquals( _2_Model_class.class.getCanonicalName(), _c.getFullName() );
        assertEquals( _2_Model_class.class.getPackage().getName(), _c.getPackageName() );

        // the _class API is similar to Reflection API on a runtime Class
        assertTrue( _c.isExtends( TestCase.class ) );
        assertEquals(1, _c.listMethods("testModelForThisClass" ).size() );

        // some information is lost and not available using Reflection (on a Class) but
        // here are some properties NOT available to Reflection but present in the .java source and AST/model
        assertTrue( _c.getJavadoc().contains( "read myself in" ) ); //verify javadoc has the text "read myself in"
        assertTrue( _c.hasImport( TestCase.class ) );
        assertTrue( _c.hasImport( _class.class ) );

        // similar to how a _method is a jdraft wrapper to a JavaParser MethodDeclaration;
        // a _class(_c) is a jdraft wrapper to a JavaParser ClassOrInterfaceDeclaration (astCoid)
        ClassOrInterfaceDeclaration astCoid = _c.ast();

        // _class (_c) just wraps and accesses/modifies the state on the ClassOrInterfaceDeclaration (astCoid)
        assertTrue( astCoid.isPublic() );
        assertTrue( _c.isPublic() );
        assertEquals( astCoid.getNameAsString(), _c.getName() );
        assertEquals( astCoid.getFullyQualifiedName().get(), _c.getFullName() );

        // if we change the _class (_c) it manipulates the underlying ClassOrInterfaceDeclaration (astCoid)
        // NOTE: both are NON-final
        assertFalse( astCoid.isFinal() );
        assertFalse( _c.isFinal() );

        _c.setFinal( true ); //use the _class _c to mutate the _class/ClassOrInterfaceDeclaration

        // NOTE: BOTH are now final
        assertTrue( astCoid.isFinal() );
        assertTrue( _c.isFinal() );

        // Top-level Classes (classes that would normally be stored in a .java file)
        // have a Root AST entity in the JavaParser model called the CompilationUnit;
        // this CompilationUnit is where the package declaration and imports are defined
        // top level jdraft types (_type, _class, _enum, _interface, _annotation) really point to the
        // CompilationUnit AND the nested type:
        // <PRE>
        // jdraft _type   JavaParser TypeDeclaration
        // _class         ClassOrInterfaceDeclaration
        // _interface     ClassOrInterfaceDeclaration
        // _enum          EnumDeclaration
        // _annotation    AnnotationDeclaration
        // </PRE>
        //                                      /  CompilationUnit
        //  _class/_interface/_enum/_annotation |  ClassOrInterfaceDeclaration/EnumDeclaration/AnnotationDeclaration

        // we can access the CompilationUnit for a _class:
        CompilationUnit astCu = _c.astCompilationUnit();

        //_c can access & modify data that is on the CompilationUnit or ClassOrInterfaceDeclaration
        assertEquals( _c.getPackageName(), astCu.getPackageDeclaration().get().getNameAsString());

        //change the package name (directly on the CompilationUnit)
        astCu.setPackageDeclaration("aaaa.bbbb");
        assertEquals( "aaaa.bbbb", _c.getPackageName() ); //verify _class(_c) reflects the change

        //you can also change the package & add imports from the _class (_c)
        _c.setPackage("cccc.dddd");
        _c.imports(UUID.class, FileNotFoundException.class); //add these

        // ...the _class(_c) will reflect the changes
        assertTrue( _c.hasImport(UUID.class));
        assertTrue( _c.hasImport(FileNotFoundException.class));

        // ... the CompilationUnit will also have the changes
        assertTrue( astCu.getImports().contains( new ImportDeclaration(UUID.class.getCanonicalName(), false, false) ) );
        assertTrue( astCu.getImports().contains( new ImportDeclaration(FileNotFoundException.class.getCanonicalName(), false, false) ) );
    }
}
