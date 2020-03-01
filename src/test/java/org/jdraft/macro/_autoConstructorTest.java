package org.jdraft.macro;

import org.jdraft._class;
import junit.framework.TestCase;

public class _autoConstructorTest extends TestCase {

    public void testAutoCtorNoArg(){
        @_autoConstructor
        class F{

        }
        _class _c =  _class.of(F.class);
        //verify I created a constructor AND it has no PARAMETERS
        assertTrue( _c.getConstructor(0).getParameters().isEmpty() );

        @_autoConstructor
        class G{
            int a,b,c;
            String s;
        }
        _c = _class.of(G.class);
        //verify I created a constructor AND it has no PARAMETERS (no final FIELDS)
        assertTrue( _c.getConstructor(0).getParameters().isEmpty() );
    }

    @_autoConstructor
    private static class S{
        @_final int val;
    }

    public void testCtorSingleArg(){
        _class _c = _class.of(S.class);
        assertTrue( _c.getConstructor(0).getParameter(0).isTypeRef(int.class));
    }

    @_autoConstructor
    private static class S2{
        @_final int val = 1;
    }

    public void testCtorSingleArgInit(){
        _class _c = _class.of(S2.class);
        assertFalse( _c.getConstructor(0).hasParameters());
    }
}
