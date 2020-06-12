package org.jdraft.macro;

import org.jdraft._class;
import org.jdraft._field;
import org.jdraft._method;
import junit.framework.TestCase;

public class _staticTest extends TestCase {

    public void testStatic(){
        assertTrue( _static.Act.to(_class.of("C")).isStatic());
        assertTrue( _static.Act.to(_field.of("int a;")).isStatic());
        assertTrue( _static.Act.to(_method.of("void m(){}")).isStatic());
    }

    public void testT(){
        @_static class F{
            @_static int g;
            @_static int method(){ return 2; }
        }
        _class _c = _class.of(F.class);
        assertTrue( _c.isStatic() );
        assertFalse( _c.hasAnno(_static.class));
        assertTrue( _c.fieldNamed("g").isStatic() );
        assertFalse( _c.fieldNamed("g").hasAnno(_static.class));

        assertTrue( _c.firstMethodNamed("method").isStatic() );
        assertFalse( _c.firstMethodNamed("method").hasAnno(_static.class));

        System.out.println( _c);
        //System.out.println( _macro.to(F.class, _class.of(F.class)) );
    }
}
