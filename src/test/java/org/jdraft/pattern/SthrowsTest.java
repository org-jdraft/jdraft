package org.jdraft.pattern;

import org.jdraft._throws;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SthrowsTest extends TestCase {

    public void testMatchAs(){
        assertTrue( $throws.as().matches("") );
        assertTrue( $throws.as(IOException.class).matches(_throws.of(IOException.class)));
        assertFalse($throws.as().matches(_throws.of(IOException.class)));
        assertTrue( $throws.as(IOException.class).matches(_throws.of(IOException.class)));

        assertFalse( $throws.as(IOException.class).matches(_throws.of(IOException.class, FileNotFoundException.class)));
    }
    public void testThrowsNone(){
        
        class TTTT{
            void a(){}
            void b(){}
            void m() throws IOException{}       //Yes     
            void r() throws java.io.IOException, URISyntaxException{} //YES
            void f() throws URISyntaxException{} //YES
            void g() throws RuntimeException{} //YES            
        }
        assertEquals(6, $throws.of().count(TTTT.class));
        assertEquals(2, $throws.none().count(TTTT.class));
        
    }
    
    public void testThrowCompose(){
        _throws _th = $throws.of().draft();
        assertTrue( _th.isEmpty());
        
        
        //parameter override
        _th = $throws.of().draft("$throws$", _throws.of(IOException.class) );
        assertEquals( _throws.of(IOException.class), _th);
        System.out.println(_th ); 
    }
    
    public void testThroMatch(){
        $throws $ts = $throws.of();
        class B{
            void m() throws IOException{}       //Yes     
            void r() throws java.io.IOException, URISyntaxException{} //YES
            void f() throws URISyntaxException{} //YES
            void g() throws RuntimeException{} //YES            
        }
        assertEquals( 4, $ts.count(B.class));
        assertEquals( 2, $throws.of(IOException.class).count(B.class) );
        assertEquals( 2, $throws.of("IOException").count(B.class) );
        
        $ts = $throws.of(IOException.class);
        
        class C{
            void m() throws IOException{}       //Yes     
            void r() throws java.io.IOException, URISyntaxException{} //tyes
            void f() throws URISyntaxException{} //no
            void g() throws RuntimeException{} //no
            
        }        
        assertEquals(2, $ts.count(C.class));
        
        $ts = $throws.of( URISyntaxException.class, IOException.class);
        class D{
            void m() throws IOException{}         //NO          
            void r() throws java.io.IOException{} //NO
            void g() throws URISyntaxException, IOException{} //YES
            void y() throws java.io.IOException, java.net.URISyntaxException{} //YES
        }
        assertEquals(2, $ts.count( D.class));
        
        //with ONLY constraint
        $ts = $throws.of( ts-> ts.size() == 0 );
        class E{
            void m() throws IOException{}         //NO          
            void r() throws java.io.IOException{} //NO
            void g(){} //YES
            void y(){} //YES
        }
        assertEquals( 2, $ts.count(E.class));        
    }
    
}
