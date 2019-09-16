package org.jdraft.macro;

import org.jdraft.macro._private;
import org.jdraft._class;
import org.jdraft._constructor;
import org.jdraft._field;
import org.jdraft._method;
import junit.framework.TestCase;

public class _privateTest extends TestCase {

    public void testDirectCall(){
        assertTrue( _private.Act.to(_class.of("C")).isPrivate());
        assertTrue( _private.Act.to(_constructor.of("C(){}")).isPrivate());
        assertTrue( _private.Act.to(_field.of("int a;")).isPrivate());
        assertTrue( _private.Act.to(_method.of("void m(){}")).isPrivate());
    }

    public void testAnnotationExpand(){
        @_private class F{
            @_private int g;
            @_private F(){}
            @_private void b(){}

        }
        _class _c = _class.of(F.class);
        assertTrue( _c.isPrivate() );
        assertFalse( _c.hasAnno(_private.class));
        assertTrue( _c.getConstructor(0).isPrivate());
        assertFalse( _c.getConstructor(0).hasAnno(_private.class));

        assertTrue( _c.getField("g").isPrivate());
        assertFalse( _c.getField("g").hasAnno(_private.class));
        assertTrue( _c.getMethod("b").isPrivate());
        assertFalse( _c.getMethod("b").hasAnno(_private.class));
    }
}
