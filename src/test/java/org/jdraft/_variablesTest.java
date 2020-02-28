package org.jdraft;

import junit.framework.TestCase;

public class _variablesTest extends TestCase {

    public void testVs(){
        _variables _vs = _variables.of("@A @B(1) @C(k=2) int x, y=-2, z = 100");
        assertEquals(3, _vs.size());
        assertEquals(3, _vs.list().size());
        assertEquals(2, _vs.list(v-> v.hasInit()).size());
        assertTrue( _vs.hasAnno("A"));
        assertTrue( _vs.hasAnno("B"));
        assertTrue( _vs.hasAnno("C"));


        assertNotNull(_vs.getAnno("A"));
        assertNotNull(_vs.getAnno("B"));
        assertNotNull(_vs.getAnno("C"));

        assertNotNull(_vs.getAnno(a-> a.hasMemberValue("value", 1)));
        assertNotNull(_vs.getAnno(a-> a.hasMemberValue("k", 2)));

        assertEquals(3, _vs.listAnnos().size());

        assertEquals(1, _vs.list(_v-> _v.isInit(100)).size());
    }

    public void testVariablesAPI(){
        _variables _vs = _variables.of("int i");
        assertEquals(1, _vs.size());
        assertEquals( _variable.of("int i"), _vs.get(0) );

        assertEquals(1, _vs.listVariables(v-> v.isType(int.class)).size());
        assertEquals(0, _vs.listVariables(v-> v.isType(String.class)).size());

        assertEquals( 0, _vs.indexOf(_variable.of("int i")));
        assertEquals( -1, _vs.indexOf(_variable.of("int j")));

        assertFalse(_vs.isFinal());
        assertEquals( 1, _vs.size());
        assertEquals( _variable.of("int i"), _vs.get(0));

        _vs.forEach(v -> System.out.println( v) );
        _vs.forEach(v-> v.isNamed("i"), v-> System.out.println(1));
        try{
            _vs.get(2);
            fail("expected exception ");
        }catch(Exception e){
            //expected
        }
        assertTrue(_vs.has(v-> v.isType(int.class)));
    }

    public void testMods(){
        _variables _vs = _variables.of( "@A final int i, j");
        assertTrue(_vs.isFinal());
        assertEquals(2, _vs.size());
    }

    public void testAnno(){
        _variables _vs = _variables.of( "@A final int i");
        assertTrue( _vs.hasAnnos() );
        assertTrue( _vs.hasAnno("A"));
        assertTrue( _vs.hasAnno(a->a.isNamed("A")));
        assertEquals( _anno.of("@A"), _vs.getAnno(0) );
        _vs.addAnnos(_anno.of("@B(1)"), _anno.of("@C(k=2)"));

        _vs.remove(_variable.of("int i"));

        assertEquals( 0, _vs.size());
        //_vs.add(_variable.of("int i"));
        System.out.println( _vs);
    }
}
