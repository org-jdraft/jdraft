package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.text.Stencil;

public class _stringExprTest extends TestCase {

    public void testStr(){
        _stringExpr st = new _stringExpr("Eric Is Great");
        st.isText("Eric Is Great");
        st.isText( s-> s.startsWith("Eric"));
        st.isText(Stencil.of("Eric $After$"));

        assertTrue( st.parseText(Stencil.of("Eric $After$")).is("After", "Is Great"));
        System.out.println( st );

    }


}
