package org.jdraft.diff;

import org.jdraft.diff._extendsDiff;
import org.jdraft._interface;
import junit.framework.TestCase;
import static org.jdraft._java.Component.*;

/**
 *
 * @author Eric
 */
public class _extendsDiffTest extends TestCase {
    
    public void testOutOfOrder(){
        _interface _a1 = _interface.of("A").extend("B").extend("C");
        _interface _a2 = _interface.of("A").extend("C").extend("B");
        
        assertTrue( _extendsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualified(){
        _interface _a1 = _interface.of("A").extend("aaaa.bbbb.B");
        _interface _a2 = _interface.of("A").extend("aaaa.bbbb.B");
        
        assertTrue( _extendsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualifiedvNot(){
        _interface _a1 = _interface.of("A").extend("aaaa.bbbb.B");
        _interface _a2 = _interface.of("A").extend("B");
        
        assertTrue( _extendsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testFullyQualifiedvNotAndOutOfOrder(){
        _interface _a1 = _interface.of("A").extend("aaaa.bbbb.C").extend("aaaa.bbbb.B");
        _interface _a2 = _interface.of("A").extend("B").extend( "C");
        
        assertTrue( _extendsDiff.INSTANCE.diff( _a1, _a2).isEmpty() );
    }
    
    public void testDiff(){
        _interface _a1 = _interface.of("A").extend("aaaa.bbbb.C").extend("aaaa.bbbb.B");
        _interface _a2 = _interface.of("A");
        
        
        //_diff.typeRefsOf(_a1, _a2);
        //System.out.println( _extendsDiff.INSTANCE.diff(_a1, _a2) );
        assertTrue( _extendsDiff.INSTANCE.diff(_a1, _a2).hasLeftOnlyAt(EXTENDS));
        assertEquals(2, _extendsDiff.INSTANCE.diff(_a1, _a2).listAt(EXTENDS).size());
        
        assertTrue( _extendsDiff.INSTANCE.diff(_a2, _a1).hasRightOnlyAt(EXTENDS));
        assertEquals(2, _extendsDiff.INSTANCE.diff(_a1, _a2).listAt(EXTENDS).size());
        
    }
    
}
