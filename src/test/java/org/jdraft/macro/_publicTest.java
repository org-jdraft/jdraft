package org.jdraft.macro;

import org.jdraft.*;
import junit.framework.TestCase;

public class _publicTest extends TestCase {

    public void testDirectCall(){
        assertTrue( _public.Act.to(_class.of("C")).isPublic());
        assertTrue( _public.Act.to(_constructor.of("C(){}")).isPublic());
        assertTrue( _public.Act.to(_field.of("int a;")).isPublic());
        assertTrue( _public.Act.to(_method.of("void m(){}")).isPublic());
    }

    public void testAnnotationExpand(){
        @_public class F{
            @_public int g;
            @_public F(){}
            @_public void b(){}

        }
        _class _c = _class.of(F.class);
        assertTrue( _c.isPublic() );
        assertFalse( _c.hasAnno(_public.class));
        assertTrue( _c.getConstructor(0).isPublic());
        assertFalse( _c.getConstructor(0).hasAnno(_public.class));

        assertTrue( _c.fieldNamed("g").isPublic());
        assertFalse( _c.fieldNamed("g").hasAnno(_public.class));

        assertTrue( _c.firstMethodNamed("b").isPublic());
        assertFalse( _c.firstMethodNamed("b").hasAnno(_public.class));
    }
}
