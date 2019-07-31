/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft;

/**
 *
 * @author Eric
 */
class AnotherClass{
    public static final int a = 100;
}
class PackagePrivateMultiClass {
    public static final int a = 100;
}

interface A{
    int a = 100;
}

enum B{
    ;
    public static final int a = 100;    
}

@interface C{
    public static final int a = 100;
}