package org.jdraft.diff;

import org.jdraft._interface;
import junit.framework.TestCase;
import static org.jdraft.diff.Feature.*;

/**
 *
 * @author Eric
 */
public class _extendsDiffTest extends TestCase {
    
    public void testOutOfOrder(){
        _interface _a1 = _interface.of("A").addExtend("B").addExtend("C");
        _interface _a2 = _interface.of("A").addExtend("C").addExtend("B");
        
        assertTrue( _extendsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualified(){
        _interface _a1 = _interface.of("A").addExtend("aaaa.bbbb.B");
        _interface _a2 = _interface.of("A").addExtend("aaaa.bbbb.B");
        
        assertTrue( _extendsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualifiedvNot(){
        _interface _a1 = _interface.of("A").addExtend("aaaa.bbbb.B");
        _interface _a2 = _interface.of("A").addExtend("B");
        
        assertTrue( _extendsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualifiedvNotAndOutOfOrder(){
        _interface _a1 = _interface.of("A").addExtend("aaaa.bbbb.C").addExtend("aaaa.bbbb.B");
        _interface _a2 = _interface.of("A").addExtend("B").addExtend( "C");
        
        assertTrue( _extendsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testDiff(){
        _interface _a1 = _interface.of("A").addExtend("aaaa.bbbb.C").addExtend("aaaa.bbbb.B");
        _interface _a2 = _interface.of("A");
        
        
        //_diff.typeRefsOf(_a1, _a2);
        //System.out.println( _extendsDiff.INSTANCE.diff(_a1, _a2) );
        assertTrue( _extendsDiff.INSTANCE.diff(_a1, _a2).hasLeftOnlyAt(EXTENDS_TYPES));
        assertEquals(2, _extendsDiff.INSTANCE.diff(_a1, _a2).listAt(EXTENDS_TYPES).size());
        
        assertTrue( _extendsDiff.INSTANCE.diff(_a2, _a1).hasRightOnlyAt(EXTENDS_TYPES));
        assertEquals(2, _extendsDiff.INSTANCE.diff(_a1, _a2).listAt(EXTENDS_TYPES).size());
        
    }
    
}
