package org.jdraft.macro;

import org.jdraft._class;
import junit.framework.TestCase;

public class _setFluentTest extends TestCase {


    public void testSetNone(){
        @_setFluent
        class C{

        }
        _class _c = _class.of(C.class);
        assertTrue( _c.listAnnos(_setFluent.class).isEmpty());
        assertTrue(_c.listMethods().isEmpty());

        @_setFluent
        class D{
            final int f = 100;
        }
        _c = _class.of(D.class);
        assertTrue( _c.listAnnos(_setFluent.class).isEmpty());
        assertTrue(_c.listMethods().isEmpty());
    }

    public void testSetOne(){

        @_setFluent
        class G{
            int a;
        }
        _class _c = _class.of(G.class);
        System.out.println( _c );
        assertTrue( _c.listAnnos(_setFluent.class).isEmpty());
        assertTrue( _c.getMethod("setA").isTypeRef(G.class));
        assertTrue( _c.getMethod("setA").getParameter(0).isTypeRef(int.class));
    }

    public void testSetMulti(){

        @_setFluent
        class G{
            int a,b,c;
            final String name = "Blah";
        }

        _class _c = _class.of(G.class);
        assertTrue( _c.listAnnos(_setFluent.class).isEmpty());
        assertTrue( _c.getMethod("setA").isTypeRef(G.class));
        assertTrue( _c.getMethod("setA").getParameter(0).isTypeRef(int.class));
        assertTrue( _c.getMethod("setB").isTypeRef(G.class));
        assertTrue( _c.getMethod("setB").getParameter(0).isTypeRef(int.class));
        assertTrue( _c.getMethod("setC").isTypeRef(G.class));
        assertTrue( _c.getMethod("setC").getParameter(0).isTypeRef(int.class));
        assertNull( _c.getMethod("setName")); //make sure there is no setName method (final field)
    }

}
