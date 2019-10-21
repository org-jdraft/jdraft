/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.pattern;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.jdraft.*;

import java.net.URL;
import junit.framework.TestCase;

/**
 * Verify the module info and package info classes work
 * @author Eric
 */
public class _codePatternTest extends TestCase {

    /**
     *  https://github.com/javaparser/javaparser/issues/2373
     *  Lord knows, I cant really figure out what this person is asking
     *
     */
    public void testJPExample(){
        _class _c = _class.of(
                "import net.weg.eng.service.object.ComponentBase;" + '\n' +
                "import net.weg.parserpoc.sampleobjects.*; //ObjectA is inside. " + '\n' +
                "public class TestClass { " + '\n' +
                "    public void execute(ComponentBase componentBase){ " + '\n' +
                "        String testString = componentBase.get(\"TEST_CHAR_1\"); " + '\n' +
                "    }" +'\n' +
                "}");
        //replace the parameters
        $parameters.of("(ComponentBase componentBase)")
                .replaceIn(_c, "(ObjectA componentBase)");

        $stmt $s = $stmt.of("String testString = componentBase.get(\"TEST_CHAR_1\");");
        $stmt $t = $stmt.of("MaestroString testString = componentBase.getTest_char_1();");
        $s.replaceIn(_c, $t);

        System.out.println( _c);
    }

    @interface N{
        int value();
    }

    public void testHasNoParent(){
        //find all literal 1's that ARE NOT direct children of binary expressions
        class C{
            @N(1)
            int i = 1 + 2;
            int y = (1);
        }
        $ex.intLiteralEx(1).forEachIn(C.class, e-> System.out.println( e.getParentNodeForChildren().getClass() ) );

        assertEquals( 3, $ex.of(1).count(C.class));
        assertEquals( 2, $ex.of(1).$hasNoParent( $ex.binaryEx() ).count(C.class));
        assertEquals( 1, $ex.of(1).$hasNoParent( $ex.binaryEx(), $ex.enclosedEx() ).count(C.class));
    }

    /**
     * int a = obj.getType()
     * int b = a
     * foo(b)
     */
    public void testFindWhiteListBlackList(){
        class F{
            F obj;

            public int getType(){
                return 4;
            }
            public void theCode(){
                int a = obj.getType();
                int b = a;
                foo(b);
            }
            public void foo(int a){ }
        }
        //an instance method call to method getType()

        //before
        //after
        //not

        //1)
        //find method calls
        //I'm calling getType() on some instance and returning it's value
        $stmt $assignCall = $.stmt("int $param$ = $instance$.getType();");

        //2) find all variable assignments
        $node $varAssignment = $node.of( "$param$" )
                .$hasParent(VariableDeclarator.class);
                //.$hasAncestor($.of(VariableDeclarator.class));

        //3) find
        $ex<MethodCallExpr> $mce = $.methodCall("$prefix$foo($any$)");

        //$varAssignment.hardcode$($args);

        $pattern.$tokens args = $assignCall.selectFirstIn(F.class).tokens();

        //first verify we can match & extract the parameter name ("a")
        assertEquals("a", args.get("param"));

        //how many times can we find the var
        //System.out.println( $var.of( args.get("param").toString() ).count(F.class) );
        //now I want to find
        $method.of("void hey($type$ $name$){}").hardcode$("name", "eric");

        assertTrue( $varAssignment.hardcode$(args).count(F.class) > 0);


        assertEquals( 1, $.stmt( "int $param$ = $obj$.getType();" ).count(F.class));

        assertEquals(1, $.methodCall("$var$.getType()").count(F.class));

        assertEquals( 1, $.methodCall( "foo($any$)" ).count(F.class));

    }

    public void testPkgInfo(){
        _code _pi = _packageInfo.of("package aaaa.ffff.gggg;", "import java.util.*;", "import java.net.URL;");
        assertEquals(1, $import.of(URL.class).count(_pi));
        assertEquals(1, $import.of("java.util.*").count(_pi));
        assertEquals(2, $import.of().count(_pi));
    }
    
    public void testModuleInfo(){
        _code _pi = _moduleInfo.of("import java.util.*;", "import java.net.URL;", "module aaaa.ffff.gggg{}" );
        assertEquals(1, $import.of(URL.class).count(_pi));
        assertEquals(2, $import.of().count(_pi));
    }

    public void testBodyNot(){
        assertTrue( $body.of("System.out.println(1);").matches("{ System.out.println(1);}") );
        assertTrue( $body.of("System.out.println(1);").matches("{ System.out.println(2); System.out.println(1);}") );

        assertFalse( $body.of("System.out.println(1);").$not($stmt.of("System.out.println(2);"))
                .matches("{ System.out.println(2); System.out.println(1);}") );
    }
}
