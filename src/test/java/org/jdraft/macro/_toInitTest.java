package org.jdraft.macro;

import junit.framework.TestCase;
import org.jdraft._class;

public class _toInitTest extends TestCase {

    public void testCreateBoth(){
        _class _c = _class.of("A", new Object(){
            @_toInit @_static void si(){
                System.out.println( "static init block");
            }

            @_toInit void i(){
                System.out.println( "init block");
            }
        });

        assertTrue( _c.getInitBlock(0).isStatic() );
        assertFalse( _c.getInitBlock(1).isStatic() );
    }
}
