/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.proto;

import org.jdraft.proto.$id;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SidTest extends TestCase {
    
    
    public void testMatchId(){
        $id $i = $id.of("Eric");
        assertTrue( $i.matches("Eric"));//will match exact
        assertFalse( $i.matches("AlEric"));//wont match different /endswith
        assertTrue( $i.matches("org.Eric"));//WILL match ends with (after .)
        assertFalse( $i.matches("org.SEric")); //Wont match ends with (after .)
    }
    
}
