package org.jdraft;

import junit.framework.TestCase;

public class _variablesExprTest extends TestCase {

    public void testVs(){
        _variablesExpr _vs = _variablesExpr.of("@A @B(1) @C(k=2) int x, y=-2, z = 100");
        assertEquals(3, _vs.size());
        assertEquals(3, _vs.list().size());
        assertEquals(2, _vs.list(v-> v.hasInit()).size());
        _vs.isAt(0, "int x");
        _vs.isAt(1, "int y = -2");
        _vs.isAt(2, "int z = 100");

        _vs.has(_variable.of("int x"));
        _vs.has("int x");
        _vs.has("int y = -2");

        assertTrue( _vs.hasAnnoNamed("A"));
        assertTrue( _vs.hasAnnoNamed("B"));
        assertTrue( _vs.hasAnnoNamed("C"));

        assertNotNull(_vs.getAnnoByName("A"));
        assertNotNull(_vs.getAnnoByName("B"));
        assertNotNull(_vs.getAnnoByName("C"));

        assertNotNull(_vs.getAnno(a-> a.hasEntryPair("value", 1)));
        assertNotNull(_vs.getAnno(a-> a.hasEntryPair("k", 2)));

        assertEquals(3, _vs.listAnnos().size());

        assertEquals(1, _vs.list(_v-> _v.isInit(100)).size());
    }

    public void testFull(){
        _variablesExpr _vs = _variablesExpr.of("@A @B(1) @C(k='a', v=2) final aaaa.bbbb.D<T, A> g = aa(0), h");
        assertTrue( _vs.getAnno(0).is("@A"));
        assertTrue( _vs.getAnno(1).is("@B(1)"));
        assertTrue( _vs.getAnno(2).is("@C(k='a', v=2)"));
        assertTrue( _vs.isAt(0, "@A @B(1) @C(k='a', v=2) final aaaa.bbbb.D<T, A> g=aa(0)"));
        assertEquals( _modifiers.of("final" ), _vs.getModifiers() );
        //System.out.println(_vs);
        assertTrue( _vs.getType().is("aaaa.bbbb.D<T, A>") );
        assertTrue( _vs.getElementType().is("aaaa.bbbb.D<T, A>") );
        _variable _vv = _vs.getAt(0);
        assertTrue( _vv.is("@A @B(1) @C(k='a', v=2) final aaaa.bbbb.D<T, A> g=aa(0)"));

        //assertTrue( _vv.is("g = aa(0)"));
        //System.out.println(_vv);
    }

    public void testVariablesAPI(){
        _variablesExpr _vs = _variablesExpr.of("int i");
        assertEquals(1, _vs.size());
        assertEquals( _variable.of("int i"), _vs.getAt(0) );

        assertEquals(1, _vs.listVariables(v-> v.isType(int.class)).size());
        assertEquals(0, _vs.listVariables(v-> v.isType(String.class)).size());

        assertEquals( 0, _vs.indexOf(_variable.of("int i")));
        assertEquals( -1, _vs.indexOf(_variable.of("int j")));

        assertFalse(_vs.isFinal());
        assertEquals( 1, _vs.size());
        assertEquals( _variable.of("int i"), _vs.getAt(0));

        _vs.toEach(v -> System.out.println( v) );
        _vs.toEach(v-> v.isNamed("i"), v-> System.out.println(1));
        try{
            _vs.getAt(2);
            fail("expected exception ");
        }catch(Exception e){
            //expected
        }
        assertTrue(_vs.has(v-> v.isType(int.class)));
    }

    public void testMods(){
        _variablesExpr _vs = _variablesExpr.of( "@A final int i, j");
        assertTrue(_vs.isFinal());
        assertEquals(2, _vs.size());
    }

    public void testAnno(){
        _variablesExpr _vs = _variablesExpr.of( "@A final int i");
        assertTrue( _vs.hasAnnos() );
        assertTrue( _vs.hasAnnoNamed("A"));
        assertTrue( _vs.hasAnno(a->a.isNamed("A")));
        assertEquals( _anno.of("@A"), _vs.getAnno(0) );
        _vs.addAnnos(_anno.of("@B(1)"), _anno.of("@C(k=2)"));

        _vs.remove(_variable.of("int i"));

        assertEquals( 0, _vs.size());
        //_vs.add(_variable.of("int i"));
        System.out.println( _vs);
    }
}
