package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.pattern.$;

public class _variableTest extends TestCase {

    public void testF(){
        class C{
            public final int a=12;
            public String h, j;
        }
        $.variables();
        assertEquals( 3, $.variable().countIn(C.class));
    }

    public void testAPI(){
        _variable _v = _variable.of("int i");
        assertTrue(_v.isNamed("i"));
        assertTrue(_v.isType(int.class));
        assertFalse( _v.hasInit());
        assertFalse( _v.isFinal());

        //now init
        _v.setInit(1);
        assertTrue( _v.hasInit());
        assertTrue( _v.isInit(1));
        //remove init
        _v.removeInit();
        assertFalse( _v.hasInit());
        assertFalse( _v.isInit(1));

        _v.setName("newName");
        assertEquals("newName", _v.getName());
    }

}
