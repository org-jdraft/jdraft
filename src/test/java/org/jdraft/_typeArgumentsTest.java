package org.jdraft;

import junit.framework.TestCase;

public class _typeArgumentsTest extends TestCase {

    public void testEmptyDiamondOperator(){
        _typeArguments _tas = _typeArguments.of();
        assertTrue(_tas.isEmpty());
        assertFalse(_tas.isUsingDiamondOperator());
        try{
            _tas.getAt(0);
            fail("Expected exception n");
        }catch(Exception e){ }
        assertEquals(-1, _tas.indexOf(Types.typeRef(String.class)));
        assertEquals(-1, _tas.indexOf(_typeRef.of(String.class)));

        _tas.setUseDiamondOperator();
        assertTrue( _tas.isEmpty());
        assertTrue( _tas.isUsingDiamondOperator());
        assertEquals(0, _tas.list().size());
        assertEquals(0, _tas.list(t-> t.isArrayType()).size());
        assertNotNull(_tas.listAstElements());
        _tas.removeDiamondOperator();
        assertTrue( _tas.isEmpty());
        assertNull(_tas.listAstElements());
        assertFalse( _tas.isUsingDiamondOperator());

        assertNull( _tas.list() );
    }

    public void testTypeArgs(){

        _typeArguments _tas = _typeArguments.of("<>");
        assertTrue( _tas.isUsingDiamondOperator());
        assertTrue( _tas.isEmpty());

        //single
        _tas = _typeArguments.of("<T>");
        assertFalse( _tas.isUsingDiamondOperator());
        assertFalse( _tas.isEmpty());

        _tas = _typeArguments.of("<T, R>");
        assertEquals( _tas, _typeArguments.of("T", "R"));
        _tas = _typeArguments.of();
        _tas.add("T");
        _tas.add("R");
        assertEquals( _tas, _typeArguments.of("T", "R"));
    }

    public void testNew(){
        _new _n = _new.of("A()");
        assertFalse(_n.hasTypeArguments());
        assertFalse(_n.isUsingDiamondOperator());
        _n.setUseDiamondOperator();
        assertTrue(_n.hasTypeArguments());
        assertTrue(_n.isUsingDiamondOperator());
        assertTrue(_n.listTypeArguments(t-> t.is(Integer.class)).isEmpty());

        _n.addTypeArguments("Integer");
        assertTrue(_n.hasTypeArguments());
        assertFalse(_n.isUsingDiamondOperator());
        assertFalse(_n.listTypeArguments(t-> t.is(Integer.class)).isEmpty());

        //_typeArguments _tas = _n.getTypeArguments();
    }

}
