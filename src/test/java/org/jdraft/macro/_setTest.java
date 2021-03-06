package org.jdraft.macro;

import org.jdraft._class;
import junit.framework.TestCase;

public class _setTest extends TestCase {


    public void testLocalField(){
        class Local{
            @_set int i;
        }
        _class _c = _class.of(Local.class);
        System.out.println( _c );
        assertTrue(_c.getMethod("setI").isPublic());
    }

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
        assertTrue( _c.firstMethodNamed("setA").isVoid());
        assertTrue( _c.firstMethodNamed("setA").getParam(0).isType(int.class));
    }

    public void testSetMulti(){

        @_set
        class G{
            int a,b,c;
            final String name = "Blah";
        }

        _class _c = _class.of(G.class);
        assertTrue( _c.listAnnos(_set.class).isEmpty());
        assertTrue( _c.firstMethodNamed("setA").isVoid());
        assertTrue( _c.firstMethodNamed("setA").getParam(0).isType(int.class));
        assertTrue( _c.firstMethodNamed("setB").isVoid());
        assertTrue( _c.firstMethodNamed("setB").getParam(0).isType(int.class));
        assertTrue( _c.firstMethodNamed("setC").isVoid());
        assertTrue( _c.firstMethodNamed("setC").getParam(0).isType(int.class));
        assertNull( _c.firstMethodNamed("setName")); //make sure there is no setName method (final field)
    }

}
