/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.pattern;

import org.jdraft.walk.Walk;
import org.jdraft._class;
import java.io.IOException;
import java.net.BindException;

import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class throwsTest extends TestCase {
    
    public void testTh(){
        class C {
            void m() throws IOException, java.net.MalformedURLException {
                
            }
        }
        _class _c = _class.of(C.class);
        Walk.in(_c, n-> System.out.println(n.getClass()+ " "+ n ) );
        
        $typeRef.of(IOException.class).removeIn(_c);
        System.out.println( _c );
        
        $typeUse.of(IOException.class).replaceIn(_c, java.net.BindException.class);
        System.out.println( _c );
        
        $typeUse.of(BindException.class).removeIn(_c);
        System.out.println( _c );

        //$throws.of(MalformedURLException.class).removeIn(_c);
        //System.out.println( _c );
    }
}
