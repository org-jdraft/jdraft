/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ReceiverParameter;
import org.jdraft.Ast;
import org.jdraft.Tree;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class ReceiverParameterUsage extends TestCase {
    
    /** 
     * This annotation is acceptable to be applied to a receiver 
     * parameter for the scope of the method or constructor
     */ 
    @Target(ElementType.TYPE_USE)
    public @interface AnnotatedUsage {}

    public class RP {
        
        /**
         * the REceiver parameter is NOT a simple paramter, but instead a way to 
         * APPLY AN ANNOTATION TO "this" only within the context of a method call
         * (i.e. we DID NOT apply the anotation to the RP class itself, but rather
         * the 
         * @return
         */
        public String getCode( @AnnotatedUsage RP this ){
            return "ss";
        }
    }
    
    public void testR(){
        ClassOrInterfaceDeclaration tp = (ClassOrInterfaceDeclaration)Ast.typeDeclaration( RP.class);
        MethodDeclaration md = tp.getMethodsByName("getCode").get(0);
        assertEquals( 0, md.getParameters().size());
        assertTrue( md.getReceiverParameter().isPresent() );
        System.out.println( md );
        Tree.in(tp, ReceiverParameter.class, t-> System.out.println(t) );
        //Ast.walk( tp, ReceiverParameter.class, t-> System.out.println(t) );                
    }
    
}
