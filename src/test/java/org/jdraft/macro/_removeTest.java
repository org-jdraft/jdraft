package org.jdraft.macro;

import org.jdraft._class;
import java.io.IOException;
import junit.framework.TestCase;

public class _removeTest extends TestCase {

    public void testRemove(){

        class D{
            @_remove int f;      //field
            @_remove void f(){}  //method
            @_remove D(){}       //constructor
            @_remove class IN{ } //nested TYPE
        }

        _class _c = _class.of(D.class);
        System.out.println( _c );
        assertEquals(0, _c.listMethods().size());
        assertEquals(0, _c.listFields().size());
        assertEquals(0, _c.listConstructors().size());
        assertEquals(0, _c.listInnerTypes().size());
    }

    /** Yeah, annotation macros dont work on types... (like below) sorry */
    public void testRemoveOnThrows(){
        
        class FFF{
            void m() throws @_remove IOException{ }
        }
        
        _class _c = _class.of(FFF.class);
        System.out.println( _c );
    }
}
