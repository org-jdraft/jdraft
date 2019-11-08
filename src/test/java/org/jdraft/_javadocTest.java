package org.jdraft;

import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import junit.framework.TestCase;

import javax.print.URIException;
import java.io.IOException;
import java.util.List;

public class _javadocTest extends TestCase {

    public void testJD(){
        //Javadoc javadoc = com.github.javaparser.JavadocParser.parse("/**\n@throws A a a a a\n*/");
        JavadocComment jdc = Ast.javadocComment( "/**\n@throws A a a a a\n*/" );
        System.out.println( jdc );
    }

    public void testBlockTags(){
        /**
         * @author Eric
         * @deprecated see something else
         * @exception java.io.IOException
         * @param
         * @return
         * @see _initBlockTest
         * @serial
         * @serialData
         * @apiNote
         * @implSpec
         * @since
         * @serialData
         * @serialField
         * @implNote
         * @implSpec
         * @version
         * @unknown
         */
        class C {

            /**
             *
             * @param a
             * @return
             * @throws
             */
            String m(int a) throws IOException {
                return null;
            }
        }
        _class _c = _class.of(C.class);
        System.out.println( _c.getJavadoc().getContent() );
        //System.out.println( _c.getJavadoc().parseFirst("@author $name$") );
        //assertEquals( "Eric", _c.getJavadoc().parseFirst("@author $name$").get("name"));
        JavadocComment jdc = _c.getJavadoc().ast();
        Javadoc jd = _c.ast().getJavadoc().get();
        List<JavadocBlockTag> bts = jd.getBlockTags();
        System.out.println( bts );
    }
}
