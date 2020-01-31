package org.jdraft.macro;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.runtime._proxy;

import java.util.UUID;

public class _toStaticInitTest extends TestCase {

    public void testStaticInitSetStaticField(){
        @_imports(UUID.class) class A{
            @_public @_static @_final String ID;
            @_toStaticInit void si(){
                System.out.println( "static init block");
                ID = UUID.randomUUID().toString();
            }
        }
        _class _c = _class.of(A.class);

        assertTrue( _c.getInitBlock(0).isStatic() );

        //verify that the styatic block is run and the ID static field is set
        assertNotNull(_proxy.of(_c).get("ID"));
    }
}
