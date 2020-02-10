/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.jdraft._class;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class ImpleInterfaceTest extends TestCase {
    
    
    /** Implementing An Interface */
    public interface MyInterface {
        int getR();
    }

    static _class _implementManual = _class.of("my.Impl")
            .implement(MyInterface.class) //will implement/ import the class
            .addMethod("public int getR(){", //manually implement the method
                    "   return 120;",
                    "}");
    static _class _implementManualLambda = _class.of("my.Impl")
            .implement(MyInterface.class) //will implement/ import the class
            .addMethod(new Object(){ public int getR(){ return 120; }} );

    /** Recommended */
    static _class _implementAnonymousBody = _class.of("my.Impl", new MyInterface(){ //I should Import the interface
        public int getR(){
            return 120;
        }
    });
    
    public void testN(){
        assertEquals(_implementManual, _implementManualLambda);
        assertEquals(_implementManual, _implementAnonymousBody);
    }
}
