package org.jdraft.macro;

import org.jdraft._class;
import junit.framework.TestCase;

public class _dtoTest extends TestCase {

    public void testAutoDto(){
        _class _c = _dto.Act.of("aaa.bbb.C", new Object(){
           public int x,y,z;
           public final String s = "eric";
        });
        assertTrue( _c.firstMethodNamed("getZ").isType(int.class) );
        assertNotNull( _c.firstMethodNamed("getY"));
    }

    public void testD(){
        @_dto
        class AD{

        }
        _class _c = _class.of(AD.class);
        //even though this class is
        assertTrue( _c.getConstructor(0).getParams().isEmpty() ); //no arg constructor
        assertTrue( _c.firstMethodNamed("toString").isType(String.class) ); //a toString method
        assertTrue( _c.firstMethodNamed("equals").isType(boolean.class)); //an typesEqual method
        assertTrue( _c.firstMethodNamed("equals").getParam(0).isType(Object.class)); //accepts an object input

    }
}
