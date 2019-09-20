package test;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import junit.framework.TestCase;
import org.jdraft.Ast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class JavaParserInternalsTest extends TestCase {

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
