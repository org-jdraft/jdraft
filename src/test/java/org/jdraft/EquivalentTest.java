package org.jdraft;

import java.util.Objects;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class EquivalentTest extends TestCase {
    
    
    public void testS(){        
        assertEquals( m1, m1a);
        assertEquals( m2, m2a);

        /*
        m1.tokenize().forEach((s, o)-> {
            System.out.println( s+" \""+o+"\" : " +Objects.hash( o ) );
        });
        m1a.tokenize().forEach((s, o)-> {
            System.out.println( s+" \""+o+"\" : " +Objects.hash( o ) );
        });
         */
        
        assertEquals( m1.hashCode(), m1a.hashCode());
        assertEquals( m2.hashCode(), m2a.hashCode());
    }
    
    static _method m1 = _method.of( "void a();"); 
    static _method m2 = _method.of( "int b();");
        
    //syntacically different but semantically the same 
    static _method m1a = _method.of( "public abstract void a();" );
    static _method m2a = _method.of( "public int b();" );
        
    /*
    public void testSimple(){
        //test both null
        assertTrue(_method.equivalent(null,null));
        List<_method> ms1 = new ArrayList<>();
        List<_method> ms2 = new ArrayList<>();        
        assertTrue(_method.equivalent(ms1, ms2)); //both empty
        
        ms1.add(m1);
        assertFalse(_method.equivalent(ms1, ms2));//one not the other
        ms2.add(m1);        
        assertTrue(_method.equivalent(ms1, ms2));//both same 1
                
        ms2.add( m2);
        assertFalse(_method.equivalent(ms1, ms2));//2 v 1
        
        ms1.add(m2);
        assertTrue(_method.equivalent(ms1, ms2));//2 v 2 same order        

        
        ms1.clear();
        ms2.clear();
        ms1.add(m1);
        ms1.add(m2);
        
        ms2.add(m2);
        ms2.add(m1);
        assertTrue(_method.equivalent(ms1, ms2));//different order        
    }
    */
    /*
    public void testSemantic(){
        
        List<_method> ms1 = new ArrayList<>();
        List<_method> ms2 = new ArrayList<>();        
        
        ms1.add(m1);        
        ms2.add(m1a);        
        assertTrue(_method.equivalent(ms1, ms2));//both equivalent
                
        ms2.add( m2);
        assertFalse(_method.equivalent(ms1, ms2));//2 v 1
        
        ms1.add(m2a);
        assertTrue(_method.equivalent(ms1, ms2));//2 v 2 same order equivalent        

        ms1.clear();
        ms2.clear();
        ms1.add(m1);
        ms1.add(m2);
        
        ms2.add(m2a);
        ms2.add(m1a);
        assertTrue(_method.equivalent(ms1, ms2));//different order equivalent       
    }
*/
    
}
