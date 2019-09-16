package org.jdraft.runtime;

import org.jdraft._class;
import org.jdraft._enum;
import org.jdraft.macro._dto;
import junit.framework.TestCase;

public class _newTest extends TestCase {


    public void testRuntimeClass(){

        Class c = _runtime.Class( _class.of("xxxx.yyyy.C", new @_dto Object(){ int x, y, z;}) );
        assertEquals( "xxxx.yyyy.C", c.getCanonicalName() );
        assertEquals( 3, c.getDeclaredFields().length );


        //an enum
        Class e = _runtime.Class( _enum.of("aaaa.bbbb.E").constants("A", "B", "C") );
        assertTrue( e.isEnum() );
        assertEquals( "aaaa.bbbb.E", e.getCanonicalName());
        assertEquals(3, e.getEnumConstants().length);
    }


    public void testInstance() throws NoSuchMethodException {
        Object o = _runtime.instanceOf(
                _class.of("aaaa.VV", new @_dto Object() {
                    int x, y, z;
                })
        );
        assertEquals("VV", o.getClass().getSimpleName());
        assertEquals("aaaa.VV", o.getClass().getCanonicalName());
        assertNotNull( o.getClass().getMethod("getX", null));
        assertNotNull( o.getClass().getMethod("getY", null));
        assertNotNull( o.getClass().getMethod("getZ", null));

        assertNotNull( o.getClass().getMethod("setX", int.class));
        assertNotNull( o.getClass().getMethod("setY", int.class));
        assertNotNull( o.getClass().getMethod("setZ", int.class));
    }

    public void testRuntimeProxyInstance(){
        _proxy p = _runtime.proxyOf( _class.of("aaaa.VV", new @_dto Object(){
            int x, y, z;
            public int sum(){
                return  x + y + z;
            }
        }));

        p.set("x", 100);
        p.set("y", 200);
        p.set("z", 300);

        assertEquals( 100+200+300, p.call("sum"));
    }
}
