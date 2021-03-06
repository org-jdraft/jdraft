package org.jdraft.macro;

import junit.framework.TestCase;
import org.jdraft._class;

public class _getTest extends TestCase {
    public void testM(){

    }

    public void testLocalField(){
        class Local{
            @_get int i;
        }
        _class _c = _class.of(Local.class);
        //System.out.println( _c );
        assertTrue(_c.getMethod("getI").isPublic());
    }

    public void testGetNothing(){
        @_get
        class D{
        }
        _class _c = _class.of( D.class);
        assertFalse( _c.hasAnno(_get.class)); //verify we removed the annotation
        assertTrue( _c.listMethods().isEmpty());
    }
    public void testGetSingle(){
        @_get
        class DD{
            int x;
        }
        _class _c = _class.of( DD.class);
        assertFalse( _c.hasAnno(_get.class)); //verify we removed the annotation
        assertTrue( _c.firstMethodNamed("getX").isType(int.class));
    }
}
