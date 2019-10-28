package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft.macro._static;

public class $typeTest extends TestCase {

    public void testTypeWithField(){
        class C {
            public @_static final int ID = 12345;
        }
        $field $f = $field.of($.PUBLIC, $.STATIC).$name("ID");
        assertTrue($type.of( $.PUBLIC, $f ).isIn(C.class));

    }
}
