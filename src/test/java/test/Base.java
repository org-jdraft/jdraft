/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author Eric
 */
public abstract class Base<S> {
    
    public static void baseClassStaticMethod(){
        System.out.println( "Base Class static method");
    }
    
    public abstract S abstractMustOverride(String s);
    
}
