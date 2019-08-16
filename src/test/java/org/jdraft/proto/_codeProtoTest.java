/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.proto;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import org.jdraft.proto.$import;
import org.jdraft._code;
import org.jdraft._moduleInfo;
import org.jdraft._packageInfo;
import java.net.URL;
import junit.framework.TestCase;

/**
 * Verify the module info and package info classes work
 * @author Eric
 */
public class _codeProtoTest extends TestCase {

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
        $node $varAssignment = $node.of( "$param$" ).$hasAncestor(VariableDeclarator.class);

        //3) find
        $expr<MethodCallExpr> $mce = $.methodCall("$prefix$foo($any$)");

        //$varAssignment.hardcode$($args);

        $proto.$args args = $assignCall.selectFirstIn(F.class).args();

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
        assertEquals(2, $import.any().count(_pi));        
    }
    
    public void testModuleInfo(){
        _code _pi = _moduleInfo.of("import java.util.*;", "import java.net.URL;", "module aaaa.ffff.gggg{}" );
        assertEquals(1, $import.of(URL.class).count(_pi));
        assertEquals(2, $import.any().count(_pi));        
    }
}
