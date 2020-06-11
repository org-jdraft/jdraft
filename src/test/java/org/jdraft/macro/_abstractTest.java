package org.jdraft.macro;

import org.jdraft._class;
import junit.framework.TestCase;

public class _abstractTest extends TestCase {

    public void testAbstract(){
         @_abstract class C{
             @_abstract void getR(){}
         }

         _class _c = _class.of(C.class);
        assertTrue( _c.isAbstract());
        assertFalse( _c.hasAnnoExpr(_abstract.class));
        assertTrue( _c.firstMethodNamed("getR").isAbstract());
        assertFalse( _c.firstMethodNamed("getR").hasAnnoExpr(_abstract.class));

         System.out.println( _c );
    }
}
