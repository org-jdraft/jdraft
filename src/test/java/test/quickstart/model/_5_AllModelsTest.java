package test.quickstart.model;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._field;
import org.jdraft._method;
import org.jdraft.macro._package;
import org.jdraft.macro._remove;

public class _5_AllModelsTest extends TestCase {

    public void testWaysToBuild_class(){
        //from a String Array
        _class _c = _class.of("package aaaa;",
                "public class C{ int i=0; }");

        //using shortcut name, then adding a _field via String
        assertEquals( _c, _class.of("aaaa.C").field("int i=0;"));

        //using a shortcut name, and adding a _field (instance)
        _field _i = _field.of("int i=0;");
        assertEquals( _c, _class.of("aaaa.C").field(_i));

        //from anonymous object
        assertEquals( _c, _class.of("aaaa.C", new Object(){
            int i=0;
        }));

        //local class
        @_package("aaaa") class C{
            int i=0;
        }
        assertEquals( _c, _class.of(C.class) );

        // from a JavaParser compilationUnit
        CompilationUnit astCu = StaticJavaParser.parse(
                "package aaaa;\n" +
                "public class C{ int i=0; }");

        assertEquals( _c, _class.of(astCu));

        //from an JavaParser Type( ClassOrInterfaceDeclaration)
        ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration) astCu.getType(0);
        assertEquals( _c, _class.of(coid));
    }

    public void testBuildMethods(){
        String mm = "public int getX(){ return this.x; }";
        _method _m = _method.of(mm);

        //from a JavaParser MethodDeclaration
        MethodDeclaration md = (MethodDeclaration)StaticJavaParser.parseBodyDeclaration(mm);
        assertEquals( _m, _method.of(md));

        //from an anonymous class body
        assertEquals( _m, _method.of( new Object(){
            public int getX(){
                return this.x;
            }
            @_remove int x;
        }));

        class Local{
            public int getX(){
                return this.x;
            }
            int x;
        }

        //from a runtime method
        assertEquals( _m, _method.of(Local.class, "getX") );
    }
}
