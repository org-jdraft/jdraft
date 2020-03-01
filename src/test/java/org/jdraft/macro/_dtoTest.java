package org.jdraft.macro;

import org.jdraft._class;
import junit.framework.TestCase;

public class _dtoTest extends TestCase {

    public void testAutoDto(){
        _class _c = _dto.Act.of("aaa.bbb.C", new Object(){
           public int x,y,z;
           public final String s = "eric";
        });
        assertTrue( _c.getMethod("getZ").isTypeRef(int.class) );
        assertNotNull( _c.getMethod("getY"));
    }

    public void testD(){
        @_dto
        class AD{

        }
        _class _c = _class.of(AD.class);
        //even though this class is
        assertTrue( _c.getConstructor(0).getParameters().isEmpty() ); //no arg constructor
        assertTrue( _c.getMethod("toString").isTypeRef(String.class) ); //a toString method
        assertTrue( _c.getMethod("equals").isTypeRef(boolean.class)); //an typesEqual method
        assertTrue( _c.getMethod("equals").getParameter(0).isTypeRef(Object.class)); //accepts an object input

    }
}
