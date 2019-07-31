package org.jdraft.macro;

import org.jdraft.adhoc._adhoc;
import org.jdraft.macro._importClass;
import org.jdraft.macro._toString;
import org.jdraft.macro._promote;
import org.jdraft._class;
import junit.framework.TestCase;

import java.util.UUID;

public class _toStringTest extends TestCase {

    public void testAutoToString(){
        @_promote("fffff")
        @_toString
        @_importClass(UUID.class)
        class V{
            public int a= 1,b=2,c=3;
            public int[] x= {1,2,3};
            public String[] names = {"A", "B", "C"};
            public UUID blah = UUID.randomUUID();
        }

        _class _c  = _class.of(V.class);

        assertTrue(_c.getMethod("toString").isType(String.class) );

        //make sure it compiles, load and create a new instance then print the toString
        //takes time System.out.println( _proxy.of(  ).toString());
        System.out.println( _adhoc.of(_c).proxy(_c).toString() );
    }
}
