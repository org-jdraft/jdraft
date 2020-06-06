/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _paramTest extends TestCase {

    public void testBuildFromScratch(){
        _param _p = _param.of();
        System.out.println( _p );
        _p.setType(int.class).setName("x");
        System.out.println( _p );
    }
    public void testFullyQualified(){
        _param _a = _param.of("java.util.List l");
        _param _b = _param.of("List l");

        assertEquals( _a, _b);
        assertEquals( _a.hashCode(), _b.hashCode() );
    }

    public void testOfIsEqualsHashCode(){
        _param _p = _param.of("int a");
        assertEquals( _param.of( "int a" ), _p  );
        assertTrue( _p.is("int a"));
        assertTrue( _p.isType("int") );
        assertTrue( _p.isNamed("a") );
    }
    
}
