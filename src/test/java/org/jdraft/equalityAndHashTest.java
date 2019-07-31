/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft;

import org.jdraft._class;
import org.jdraft._field;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class equalityAndHashTest extends TestCase {
    
    public void testFieldInit(){                
        _field _f = _field.of( "int val = 1;" );
        _field _f2 = _field.of( "int val = 0x01;" );
        assertEquals(_f.hashCode(), _f2.hashCode());
        assertEquals(_f, _f2);
    }
    
    public void testFieldsArrayInit(){                
        _field _f = _field.of( "int[] vals = {0x01, 0b1,1};" );
        _field _f2 = _field.of( "int[] vals = {1, 0x01, 0b1};" );
        assertEquals(_f.hashCode(), _f2.hashCode());
        assertEquals(_f, _f2);
    }
    
    public void testFieldsAnno(){
        _field _f = _field.of( "@R(1) int val;" );
        _field _f2 = _field.of( "@R(0x01) int val;" );
        assertEquals(_f.hashCode(), _f2.hashCode());
        assertEquals( _f, _f2);
    }
    
    @interface R{
        int value();
    }
    
    public void testNumbers(){
        class A{
            @R(0x01)
            int i = 1;
            int x;
            
            int[] vals = {1, 0x01, 0b1};
        }
        
        class B{
            @R(1)
            int x;
            int i=0x01;            
            int[] vals = {0x01, 0b1,1};
        }
        
        
        _class _c = _class.of( A.class );
        _class _b = _class.of( B.class ).name("A");
        
        System.out.println( _c );
        System.out.println( _b );
        assertEquals( _c.hashCode(), _b.hashCode());
    }
    
}
