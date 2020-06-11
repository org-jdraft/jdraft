package org.jdraft.macro;

import org.jdraft._interface;
import org.jdraft._method;
import junit.framework.TestCase;

public class _defaultTest extends TestCase {

    interface I{
        @_default static int getY(){ return 1; }
    }
    public void test_default(){
        _interface _i = _interface.of(I.class);
        //System.out.println( _i);

        assertTrue( _i.firstMethodNamed("getY").isDefault());
        assertFalse( _i.firstMethodNamed("getY").hasAnnoExpr(_default.class));

        //manually apply the _macro to the method
        _method _m = _method.of("public static int getY(){ return 1; }");
        _default.Act.to(_m.ast());
        assertTrue( _m.isDefault() );
        assertFalse( _m.isStatic());
    }
}
