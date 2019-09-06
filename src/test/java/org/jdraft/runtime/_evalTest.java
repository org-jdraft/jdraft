package org.jdraft.runtime;

import org.jdraft.*;
import com.github.javaparser.ast.expr.*;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class _evalTest extends TestCase {
    
    public void testEval(){
        Expression e = Ex.of("3+4");
        assertEquals(7, _runtime.eval(e));
    }
    
    /*
    public void testEvalClassStaticMethod(){
        assertEquals(102, 
            _eval.of( _class.of("A", new Object(){
                public @_static int doIt(){
                    return 102;
                }
            })));
        
        assertEquals( 102, _eval.type("class B{ public static int doIt(){ return 102; } }") );
    }
    
    public void testEvalMethod(){
        assertEquals( 500, 
            _eval.method("int m(int x){ return x * 100; }", 5));
        
        //evaluate a method with no return value
        _eval.method("void m(){ System.setProperty(\"a\", \"100\"); }" );
        assertEquals("100", System.getProperty("a") );
    }
    
    public void testEvalExpression(){        
        assertEquals( 7,  _eval.of( ()-> 3+4 ));
        assertEquals( 7,  _eval.expr("3+4"));
        //assertEquals( 7,  _eval.of("int i=1+1;", "i++;", "i++;", "i++;", "i++;", "i++;", "return i;"));
        //assertEquals( 1,  _eval.of( ()-> Map.of("X", 1).size() ) );
        
        //assertEquals( 1,  _eval.of( 
        //        (Integer res, String r) -> Map.of("X",res).size() ) 
        //);
    }
*/
    
}
