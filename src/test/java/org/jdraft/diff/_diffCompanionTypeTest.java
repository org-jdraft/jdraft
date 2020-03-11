package org.jdraft.diff;

import junit.framework.TestCase;
import org.jdraft.*;

import java.util.List;

public class _diffCompanionTypeTest extends TestCase {

    /*
    public void testForClass(){
        _class _c = _class.of("C");
        _class _cp = _c.copy();
        //make sure the copy is equal
        assertEquals(_c, _cp);
        assertTrue( _c.equals(_cp) );
        assertTrue( _cp.equals(_c) );
        assertEquals(_c.hashCode(), _cp.hashCode());

        //now change the copy (only)
        _cp.addCompanionTypes(_class.of("D"));

        assertTrue(_cp.components().containsKey(_java.Component.COMPANION_TYPES));
        assertTrue( ((List<_type>)_cp.components().get(_java.Component.COMPANION_TYPES)).size() == 1 );


        assertFalse( _c.equals( _cp)); //verify not same
        assertFalse( _c.hashCode() == _cp.hashCode()); //verify not same hashcode

        _diff _d = _diff.of(_c, _cp);
        assertEquals( 1, _d.size()); //verify we have 1 diff

        assertNotNull( _d.firstOn(_java.Component.COMPANION_TYPE)); //there is a companion Type diff
        assertNotNull( _d.rightOnlyOn(_java.Component.CLASS, "D")); //the class is D
        assertNotNull( _d.rightOnlyAt(_java.Component.CLASS, "D")); //the class is D

        System.out.println( _d );
    }

     */

    /*
    public void testForEnum(){
        _enum _c = _enum.of("C");
        _enum _cp = _c.copy();
        //make sure the copy is equal
        assertEquals(_c, _cp);
        assertTrue( _c.equals(_cp) );
        assertTrue( _cp.equals(_c) );
        assertEquals(_c.hashCode(), _cp.hashCode());

        //now change the copy (only)
        _cp.addCompanionTypes(_class.of("D"));
        assertFalse( _c.equals( _cp)); //verify not same
        assertFalse( _c.hashCode() == _cp.hashCode()); //verify not same hashcode

        assertTrue(_cp.components().containsKey(_java.Component.COMPANION_TYPES));
        assertTrue( ((List<_type>)_cp.components().get(_java.Component.COMPANION_TYPES)).size() == 1 );

        _diff _d = _diff.of(_c, _cp);
        assertEquals( 1, _d.size()); //verify we have 1 diff

        assertNotNull( _d.firstOn(_java.Component.COMPANION_TYPE)); //there is a companion Type diff
        assertNotNull( _d.rightOnlyOn(_java.Component.CLASS, "D")); //the class is D
        assertNotNull( _d.rightOnlyAt(_java.Component.CLASS, "D")); //the class is D

        System.out.println( _d );
    }
    */

    /*
    public void testForInterface(){
        _interface _c = _interface.of("C");
        _interface _cp = _c.copy();
        //make sure the copy is equal
        assertEquals(_c, _cp);
        assertTrue( _c.equals(_cp) );
        assertTrue( _cp.equals(_c) );
        assertEquals(_c.hashCode(), _cp.hashCode());

        //now change the copy (only)
        _cp.addCompanionTypes(_class.of("D"));

        assertTrue(_cp.components().containsKey(_java.Component.COMPANION_TYPES));
        assertTrue( ((List<_type>)_cp.components().get(_java.Component.COMPANION_TYPES)).size() == 1 );

        assertFalse( _c.equals( _cp)); //verify not same
        assertFalse( _c.hashCode() == _cp.hashCode()); //verify not same hashcode

        _diff _d = _diff.of(_c, _cp);
        assertEquals( 1, _d.size()); //verify we have 1 diff

        assertNotNull( _d.firstOn(_java.Component.COMPANION_TYPE)); //there is a companion Type diff
        assertNotNull( _d.rightOnlyOn(_java.Component.CLASS, "D")); //the class is D
        assertNotNull( _d.rightOnlyAt(_java.Component.CLASS, "D")); //the class is D

        System.out.println( _d );
    }
    */


    public void testForAnnotation(){
        _annotation _c = _annotation.of("C");
        _annotation _cp = _c.copy();
        //make sure the copy is equal
        assertEquals(_c, _cp);
        assertTrue( _c.equals(_cp) );
        assertTrue( _cp.equals(_c) );
        assertEquals(_c.hashCode(), _cp.hashCode());

        //now change the copy (only)
        _cp.addCompanionTypes(_class.of("D"));

        /** TO ADD */
        assertTrue(_cp.components().containsKey(_java.Component.COMPANION_TYPES));
        assertTrue( ((List<_type>)_cp.components().get(_java.Component.COMPANION_TYPES)).size() == 1 );

        assertFalse( _c.equals( _cp)); //verify not same
        assertFalse( _c.hashCode() == _cp.hashCode()); //verify not same hashcode

        _diff _d = _diff.of(_c, _cp);
        assertEquals( 1, _d.size()); //verify we have 1 diff

        assertNotNull( _d.firstOn(_java.Component.COMPANION_TYPE)); //there is a companion Type diff
        assertNotNull( _d.rightOnlyOn(_java.Component.CLASS, "D")); //the class is D
        assertNotNull( _d.rightOnlyAt(_java.Component.CLASS, "D")); //the class is D

        System.out.println( _d );
    }
}
