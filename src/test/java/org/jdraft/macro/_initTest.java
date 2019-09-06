package org.jdraft.macro;

import org.jdraft.Ex;
import org.jdraft._class;
import org.jdraft._field;
import junit.framework.TestCase;

public class _initTest extends TestCase {

    public void testF(){
        _field _f = _field.of( "int i" );
        _init.Macro.to(_f, Ex.of(100));
        assertEquals( Ex.of(100), _f.getInit() );
    }

    public void testAnno(){
        _class _c = _class.of(new Object(){
            class G {
                @_init("1")
                int f;
            }
        });
        assertEquals( Ex.of(1), _c.getField("f").getInit());
    }
}
