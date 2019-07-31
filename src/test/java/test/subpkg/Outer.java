package test.subpkg;

import static junit.framework.Assert.assertTrue;

public class Outer {

    class Inner{

    }

    public static void main(String[] args){
        Class clazz = Inner.class;
        boolean b = !clazz.isLocalClass() && ! clazz.isAnonymousClass() && !clazz.isSynthetic();
        assertTrue( clazz.isMemberClass() );
    }
}
