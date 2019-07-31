package org.jdraft.macro;

import org.jdraft._class;
import junit.framework.TestCase;

public class _setTest extends TestCase {

    public void testSetNone(){
        @_set
        class C{

        }
        _class _c = _class.of(C.class);
        assertTrue( _c.listAnnos(_set.class).isEmpty());
        assertTrue(_c.listMethods().isEmpty());

        @_set
        class D{
            final int f = 100;
        }
        _c = _class.of(D.class);
        assertTrue( _c.listAnnos(_set.class).isEmpty());
        assertTrue(_c.listMethods().isEmpty());
    }

    public void testSetOne(){

        @_set
        class G{
            int a;
        }
        _class _c = _class.of(G.class);
        assertTrue( _c.listAnnos(_set.class).isEmpty());
        assertTrue( _c.getMethod("setA").isVoid());
        assertTrue( _c.getMethod("setA").getParameter(0).isType(int.class));
    }

    public void testSetMulti(){

        @_set
        class G{
            int a,b,c;
            final String name = "Blah";
        }

        _class _c = _class.of(G.class);
        assertTrue( _c.listAnnos(_set.class).isEmpty());
        assertTrue( _c.getMethod("setA").isVoid());
        assertTrue( _c.getMethod("setA").getParameter(0).isType(int.class));
        assertTrue( _c.getMethod("setB").isVoid());
        assertTrue( _c.getMethod("setB").getParameter(0).isType(int.class));
        assertTrue( _c.getMethod("setC").isVoid());
        assertTrue( _c.getMethod("setC").getParameter(0).isType(int.class));
        assertNull( _c.getMethod("setName")); //make sure there is no setName method (final field)
    }

}
