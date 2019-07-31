/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import test.subpkg.ann2;

/**
 *
 * @author Eric
 */
@ann @ann2(k='5',v=7)
public @interface ComplexAnnotationType {
    
    public static final int V = 102;
    
    /** javadoc */
    @ann2(k='3',v=2)
    @ann
    int value();
    
    String s() default "String";
    
    Class[] clazz() default {};
    
    int vval() default V;
    
    static class C{
        public int f = 123;
    }
    enum E{
        A;
        public int f = 123;
    }
    
    interface I{
        public static final int f = 123;
    }
    @interface nested{
        public static final int f = 123;
    }
}
