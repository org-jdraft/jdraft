package test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import junit.framework.TestCase;
import org.jdraft.Ast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class JavaParserInternalsTest extends TestCase {

    public void testNativeMethod(){
        MethodDeclaration md = (MethodDeclaration) StaticJavaParser.parseBodyDeclaration(
                "public native void bar(User instance, Object value);");

        md = (MethodDeclaration) StaticJavaParser.parseBodyDeclaration(
            "public native void bar(User instance, Object value)/*-{\n"+
            "instance.@org.treblereel.client.User::name = value;\n"+
            "}-*/;");

        System.out.println( md );
    }

    public void testBodyDeclaration(){
        TypeDeclaration td = Ast.typeDecl("public class A{}");
        td.getMembers();

        EnumDeclaration ed = null;
        EnumConstantDeclaration ecd = null;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface ArgTypes{
        int v();
        String s();
        Class<? extends Node> classInstance();
        Class[] classArray();
        Class<? extends Node>[] nodeArr() default {Node.class, VariableDeclarator.class};
    }

    @ArgTypes(v=1,s="s", classInstance=TypeDeclaration.class, classArray={int.class, String.class} )
    public void testUse(){


    }

}
