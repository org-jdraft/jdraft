package org.jdraft.macro;

import org.jdraft.Expressions;
import org.jdraft._class;
import org.jdraft._field;
import junit.framework.TestCase;

public class _initTest extends TestCase {

    public void testF(){
        _field _f = _field.of( "int i" );
        _init.Act.to(_f.ast(), Expressions.of(100));
        assertEquals( Expressions.of(100), _f.getInit() );
    }

    public void testAnno(){
        _class _c = _class.of("AAAA", new Object(){
            //class G {
                @_init("1") int f;
            //}
        });
        assertEquals( Expressions.of(1), _c.getField("f").getInit());
    }
}
