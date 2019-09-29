package org.jdraft;

import junit.framework.TestCase;

public class _javaMemberAtTest extends TestCase {

    public void testMemberAt(){
        _class _c = _class.of("public class C{",        //line 1
                "    int f = 100;",                     //line 2
                "    public C(int f){ this.f = f; }",   //line 3
                "    public int getF(){ return f;}",    //line 4
                "    ",                                 //line 5
                "}");                                   //line 6

        _class _cc = _java.memberAt(_c, 1);
        assertEquals( _c, _cc);
        _field _f = _java.memberAt(_c, 2);
        assertTrue(_f.is("int f = 100;"));
        _constructor _ct = _java.memberAt(_c, 3);
        assertTrue(_ct.is("public C(int f){ this.f = f; }"));
        _method _m = _java.memberAt(_c, 4);
        assertTrue(_m.is("public int getF(){ return f; }"));

        _cc = _java.memberAt(_c, 5);
        assertEquals( _c, _cc);

        _cc = _java.memberAt(_c, 6);
        assertEquals( _c, _cc);

        assertNull( _java.memberAt(_c, 8));
    }
}
