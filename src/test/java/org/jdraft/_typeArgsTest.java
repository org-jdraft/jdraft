package org.jdraft;

import junit.framework.TestCase;

public class _typeArgsTest extends TestCase {

    public void testEmptyDiamondOperator(){
        _typeArgs _tas = _typeArgs.of();
        assertTrue(_tas.isEmpty());
        assertFalse(_tas.isUsingDiamondOperator());
        try{
            _tas.getAt(0);
            fail("Expected exception n");
        }catch(Exception e){ }
        assertEquals(-1, _tas.indexOf(Types.of(String.class)));
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

        _typeArgs _tas = _typeArgs.of("<>");
        assertTrue( _tas.isUsingDiamondOperator());
        assertTrue( _tas.isEmpty());

        //single
        _tas = _typeArgs.of("<T>");
        assertFalse( _tas.isUsingDiamondOperator());
        assertFalse( _tas.isEmpty());

        _tas = _typeArgs.of("<T, R>");
        assertEquals( _tas, _typeArgs.of("T", "R"));
        _tas = _typeArgs.of();
        _tas.add("T");
        _tas.add("R");
        assertEquals( _tas, _typeArgs.of("T", "R"));
    }

    public void testNew(){
        _newExpr _n = _newExpr.of("A()");
        assertFalse(_n.hasTypeArgs());
        assertFalse(_n.isUsingDiamondOperator());
        _n.setUseDiamondOperator();
        assertTrue(_n.hasTypeArgs());
        assertTrue(_n.isUsingDiamondOperator());
        assertTrue(_n.listTypeArgs(t-> t.is(Integer.class)).isEmpty());

        _n.addTypeArgs("Integer");
        assertTrue(_n.hasTypeArgs());
        assertFalse(_n.isUsingDiamondOperator());
        assertFalse(_n.listTypeArgs(t-> t.is(Integer.class)).isEmpty());

        //_typeArguments _tas = _n.getTypeArguments();
    }

}
