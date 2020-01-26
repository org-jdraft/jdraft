package org.jdraft.text;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import junit.framework.TestCase;
import org.jdraft.Ast;

import java.io.IOException;
import java.util.List;

public class JDtagsTest extends TestCase {

    public void testCheckJavadoc(){

        class t{
            /**
             * this contains an inline tag {@code for(int i=0;i<100;i++){} }
             * @throws IOException
             */
            public void mm(){}
        }
        CompilationUnit cu = Ast.of(t.class);
        MethodDeclaration md = cu.getType(0).getMethods().get(0);
        JavadocComment jdc = md.getJavadocComment().get();

        Javadoc jd = md.getJavadoc().get();
        List<JavadocBlockTag> jdb = jd.getBlockTags();
        System.out.println( jdb );

    }
}
