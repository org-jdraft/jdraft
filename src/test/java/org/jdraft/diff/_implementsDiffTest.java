/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.diff;

import org.jdraft._class;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _implementsDiffTest extends TestCase {
    
    public void testOutOfOrder(){
        _class _a1 = _class.of("A").addImplements("B", "C");
        _class _a2 = _class.of("A").addImplements("C", "B");
        
        assertTrue( _implementsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualified(){
        _class _a1 = _class.of("A").addImplement("aaaa.bbbb.B");
        _class _a2 = _class.of("A").addImplement("aaaa.bbbb.B");
        
        assertTrue( _implementsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualifiedvNot(){
        _class _a1 = _class.of("A").addImplement("aaaa.bbbb.B");
        _class _a2 = _class.of("A").addImplement("B");
        
        assertTrue( _implementsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualifiedvNotAndOutOfOrder(){
        _class _a1 = _class.of("A").addImplements("aaaa.bbbb.C", "aaaa.bbbb.B");
        _class _a2 = _class.of("A").addImplements("B", "C");
        
        assertTrue( _implementsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
}
