/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft.adhoc;

import java.util.Arrays;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class AdhocTest extends TestCase {
    
    static class Vari{
        int val = 0;
        
        public Vari(int...is){
            Arrays.stream(is).forEach( i->val += i);
        }
        
        public Vari(int i){
            val = i;
        }        
    }
    
    public void testVV(){
        Vari t = (Vari)_adhoc.tryAllCtors(Vari.class, 1,2);
        assertEquals(3, t.val);  
    }
    
    public void testA(){
        Vari z = (Vari)_adhoc.tryAllCtors(Vari.class );
        assertEquals(0, z.val);
        
        Vari v = (Vari)_adhoc.tryAllCtors(Vari.class, 1);
        assertEquals(1, v.val);
        
        Vari t = (Vari)_adhoc.tryAllCtors(Vari.class, new int[]{1,2});
        assertEquals(3, t.val);        
    }
    
    public static class Vari2{
        int val = 0;
        String name;
        public Vari2(String name, int...is){
            this.name = name;
            Arrays.stream(is).forEach( i->val += i);
        }
        
        public Vari2(String name, int i){
            this.name = name;
            val = i;
        }        
    }
    
     public void testVV2(){
        
    }
    
    public void testA2(){
        
        //Vari2 v2 = new Vari2("eric");        
        Vari2 z = (Vari2)_adhoc.tryAllCtors(Vari2.class, "eric");
        assertEquals(0, z.val);
        assertEquals("eric", z.name);
        
        
        Vari2 v = (Vari2)_adhoc.tryAllCtors(Vari2.class, "eric", 1);
        assertEquals(1, v.val);
        assertEquals("eric", v.name);
        
        Vari2 t = (Vari2)_adhoc.tryAllCtors(Vari2.class, "eric", new int[]{1,2});
        assertEquals(3, t.val);      
        assertEquals("eric", t.name);
        
        Vari2 t2 = (Vari2)_adhoc.tryAllCtors(Vari2.class, "eric", 1,2);
        assertEquals(3, t2.val);  
        assertEquals("eric", t2.name);  
    }
}
