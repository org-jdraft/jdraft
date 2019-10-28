package org.jdraft.runtime;

import org.jdraft.*;
import com.github.javaparser.ast.expr.*;
import junit.framework.TestCase;
import org.jdraft.macro._static;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author Eric
 */
public class _evalTest extends TestCase {
    
    public void testStaticEval(){
        //we can run a static evaluation
        assertEquals(null, _runtime.staticEval("null") );
        //primitives
        assertEquals(true, _runtime.staticEval("true") );

        assertEquals(123, _runtime.staticEval("123") );
        assertEquals(12345678901L, _runtime.staticEval("12345678901L") );
        assertEquals(1.0f, _runtime.staticEval("1.0f") );
        assertEquals(1.0d, _runtime.staticEval("1.0d") );
        assertEquals('c', _runtime.staticEval("'c'") );
        assertEquals("s" instanceof String, _runtime.staticEval("\"s\" instanceof String") );


        //ternary expression
        assertEquals(false, _runtime.staticEval("!true") );

        //method call
        assertTrue( (System.currentTimeMillis() <= (long)_runtime.staticEval("System.currentTimeMillis()") ));

        //instance expression
        System.out.println( Ex.of("new HashMap()").getClass() );
        assertEquals( new HashMap(), _runtime.staticEval("new java.util.HashMap()") );

        //binary expression
        assertEquals(110, _runtime.staticEval("100 + 10") );

        //enclosed expression + binary expression
        assertEquals(3, _runtime.staticEval("(1+5) / 2") );

        int[] arr = (int[])_runtime.staticEval("new int[] {1,2,3,4,5}");
        assertTrue(Arrays.equals( new int[]{1,2,3,4,5}, arr));

        Expression e = Ex.of("3+4");
        assertEquals(7, _runtime.staticEval(e));
    }
    

    public void testEvalClassStaticField(){
        //eval using a new _runtime instance to get the value of a field
        _runtime _r = _runtime.of(_class.of("aaa.bb.C").field("public static int ID= 10234;"));

        assertEquals(10234, _r.eval(Ex.of("aaa.bb.C.ID"))); //get the value of the field
        assertEquals(10234, _r.eval("aaa.bb.C.ID")); //get the value of the field
        assertEquals(10234, _r.eval("ID")); //get the value of the field (be smart enough to look for classes that have a public static field)

        _r = _runtime.of(_class.of("aaa.bb.C").field("public static int ID= 10234;"));
        
        //assertEquals( 102, _eval.type("class B{ public static int doIt(){ return 102; } }") );
    }

    public void testEvalClassStaticMethod(){
        _runtime _r = _runtime.of(_class.of("aaa.bb.C", new Object(){
            public @_static UUID generateUUID(){
                return UUID.randomUUID();
            }
        }));
        UUID uuid = (UUID)_r.eval("aaa.bb.C.generateUUID()");
    }
    /*
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
