package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.pattern.$;

public class _variableTest extends TestCase {

    public void testFieldOrLocal(){
        _variable _v = _variable.of("int i");
        assertFalse(_v.isLocal() );
        assertFalse(_v.isField() );

        _variablesExpr _lvs = _variablesExpr.of("int i, j");
        assertTrue( _lvs.getAt(0).isLocal());
        assertTrue( _lvs.getAt(1).isLocal());

        assertFalse( _lvs.getAt(0).isField());
        assertFalse( _lvs.getAt(1).isField());

        _v = _variable.of(_field.of( "int j" ).node());
        assertTrue( _v.isField() );
        assertFalse( _v.isLocal());
    }

    //ensure variables
    public void testF(){
        class C{
            public final int a=12;
            public String h, j;
            public void method(int k){
                int j,i;
            }
        }

        //$.variables();
        assertEquals( 1, $.parameter().countIn(C.class));
        assertEquals( 3, $.field().countIn(C.class));
        assertEquals( 1, $.localVariables().countIn(C.class));
        assertEquals( 5, $.variable().countIn(C.class));

        assertEquals( 3, $.variable(v-> v.getType().isPrimitiveType()).countIn(C.class));
    }

    public void testAPI(){
        _variable _v = _variable.of("int i");
        assertTrue(_v.isNamed("i"));
        assertTrue(_v.isType(int.class));
        assertFalse( _v.hasInit());
        assertFalse( _v.isFinal());

        assertFalse( _v.isField());
        assertFalse( _v.isLocal());

        //now init
        _v.setInit(1);
        assertTrue( _v.hasInit());
        assertTrue( _v.isInit(1));
        //remove init
        _v.removeInit();
        assertFalse( _v.hasInit());
        assertFalse( _v.isInit(1));

        _v.setName("newName");
        assertEquals("newName", _v.getName());
    }

}
