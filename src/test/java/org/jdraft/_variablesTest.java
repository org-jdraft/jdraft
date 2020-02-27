package org.jdraft;

import junit.framework.TestCase;

import java.lang.reflect.Modifier;

public class _variablesTest extends TestCase {

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
