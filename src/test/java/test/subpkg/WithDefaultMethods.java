/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.subpkg;

/**
 * An interface with default METHODS
 * 
 * @author Eric
 */
public interface WithDefaultMethods<R> {
    
    default String aDefaultMethod(R aValue){
        return "SOME STRING "+ aValue;
    } 
    
    default int count(){
        return 1;
    }
}
