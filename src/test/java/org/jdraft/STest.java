/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft;

import junit.framework.TestCase;

import org.jdraft.pattern.$;
import org.jdraft.pattern.$field;

/**
 *
 * @author Eric
 */
public class STest extends TestCase {

    public void testGetParentMember(){
        class C{
            void m(){
                class I{
                    int i;
                }
            }
        }
        _class _c = _class.of(C.class);
        _field _f = $field.of().firstIn(_c);
        assertTrue( _f.getParentMember() instanceof _class);//class I
        assertTrue( _f.getParentMember().getParentMember() instanceof _method);
        assertTrue( _f.getParentMember().getParentMember().getParentMember() instanceof _class);
        assertNull( _f.getParentMember().getParentMember().getParentMember().getParentMember() );
    }

    public void testQueryCount(){
        class MyClass{
            String[] s = new String[0];
            
            void m(){
                int i = s.length;
                assert(true);
                System.out.println( "HELLO" );
                System.out.println( 314 );
                System.out.println( 3.14f );
            }
        }
        assertEquals(1, $.of(true).countIn(MyClass.class));
        assertEquals(1, $.of(0).countIn(MyClass.class));
        assertEquals(1, $.of("HELLO").countIn(MyClass.class));
        assertEquals(1, $.assertStmt().countIn(MyClass.class));
        assertEquals(1, $.doubleLiteral().countIn(MyClass.class));
        
        assertEquals(1, $.doubleLiteral("3.14f").countIn(MyClass.class));
        assertEquals(1, $.doubleLiteral("3.14").countIn(MyClass.class));
        
        
        assertEquals(1, $.field().countIn(MyClass.class));
        //assertEquals(1, $.fieldAccessExpr().count(MyClass.class));
        assertEquals(1, $.varLocal().countIn(MyClass.class));
        assertEquals(1, $.arrayCreation().countIn(MyClass.class));
        
        assertEquals(1, $.assertStmt().countIn(MyClass.class));
        
    }
}
