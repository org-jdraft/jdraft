package org.jdraft.adhoc;

import org.jdraft._class;
import org.jdraft.macro._dto;
import junit.framework.TestCase;

public class _newTest extends TestCase {

    public void testF(){
        Object o = _adhoc.instanceOf(
            _dto.Macro.to(
                _class.of("aaaa.VV").fields("int x, y, z;")
            )
        );
        assertEquals( "VV", o.getClass().getSimpleName() );
        assertEquals( "aaaa.VV", o.getClass().getCanonicalName() );
    }


}
