package org.jdraft.proto;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._class;

public class SclassTest extends TestCase {

    public void testMatchAny(){
        $class $c = $class.of();
        assertTrue( $c.isMatchAny());

        //its matches ANY Class
        assertTrue($c.match(Ast.classDeclaration("public class A{}") ));
        assertTrue($c.match(Ast.of("public class A{}") ));
        assertTrue($c.match(_class.of("A") ));
        assertTrue($c.match(_class.of(SclassTest.class) ));
        $class.Select sel = $c.select(_class.of("aaaa.bbbb.C"));
        assertNotNull(sel);

    }
}
