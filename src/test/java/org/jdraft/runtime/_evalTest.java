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

        //instance of
        assertEquals("s" instanceof String, _runtime.staticEval("\"s\" instanceof String") );

        //ternary expression
        assertEquals(false, _runtime.staticEval("!true") );

        //method call
        assertTrue( (System.currentTimeMillis() <= (long)_runtime.staticEval("System.currentTimeMillis()") ));

        //instance expression
        //System.out.println( Ex.of("new HashMap()").getClass() );
        assertEquals( new HashMap(), _runtime.staticEval("new java.util.HashMap()") );

        //binary expression
        assertEquals(110, _runtime.staticEval("100 + 10") );

        //enclosed expression + binary expression
        assertEquals(3, _runtime.staticEval("(1+5) / 2") );

        int[] arr = (int[])_runtime.staticEval("new int[] {1,2,3,4,5}");
        assertTrue(Arrays.equals( new int[]{1,2,3,4,5}, arr));

        long[] larr = (long[])_runtime.staticEval("new long[]{System.currentTimeMillis()}");
        assertTrue( System.currentTimeMillis() >= larr[0]);
        Expression e = Expr.of("3+4");
        assertEquals(7, _runtime.staticEval(e));
    }

    public void testRuntimeEval(){
        //eval using a new _runtime instance to get the value of a field
        _runtime _r = _runtime.of(_class.of("aaa.bb.C")
                .addField("public static int ID= 10234;")
        );

        assertEquals(10234, _r.eval(Expr.of("aaa.bb.C.ID"))); //get the value of the field
        assertEquals(10234, _r.eval("aaa.bb.C.ID")); //get the value of the field
        assertEquals(10234, _r.eval("ID")); //get the value of the field (be smart enough to look for classes that have a public static field)

        assertEquals(10234, _r.eval("ID"));
        assertEquals(10234, _r.eval("aaa.bb.C.ID"));

        //instantate new instances
        assertNotNull( _r.eval( "new aaa.bb.C()"));
        assertNotNull( _r.eval( "new C()"));

        //assertEquals( 102, _eval.type("class B{ public static int doIt(){ return 102; } }") );

        //verify all the things I can static eval I can _runtime.eval
        //we can run a static evaluation
        assertEquals(null, _r.eval("null") );
        //primitives
        assertEquals(true, _r.eval("true") );

        assertEquals(123, _r.eval("123") );
        assertEquals(12345678901L, _r.eval("12345678901L") );
        assertEquals(1.0f, _r.eval("1.0f") );
        assertEquals(1.0d, _r.eval("1.0d") );
        assertEquals('c', _r.eval("'c'") );

        //instance of
        assertEquals("s" instanceof String, _r.eval("\"s\" instanceof String") );

        //ternary expression
        assertEquals(false, _r.eval("!true") );

        //method call
        assertTrue( (System.currentTimeMillis() <= (long)_r.eval("System.currentTimeMillis()") ));

        //instance expression
        //System.out.println( Ex.of("new HashMap()").getClass() );
        assertEquals( new HashMap(), _r.eval("new java.util.HashMap()") );

        //binary expression
        assertEquals(110, _r.eval("100 + 10") );

        //enclosed expression + binary expression
        assertEquals(3, _r.eval("(1+5) / 2") );

        int[] arr = (int[])_r.eval("new int[] {1,2,3,4,5}");
        assertTrue(Arrays.equals( new int[]{1,2,3,4,5}, arr));

        long[] larr = (long[])_r.eval("new long[]{System.currentTimeMillis()}");
        assertTrue( System.currentTimeMillis() >= larr[0]);
        Expression e = Expr.of("3+4");
        assertEquals(7, _r.eval(e));
    }

    public void testEvalClassStaticMethod(){
        _runtime _r = _runtime.of(_class.of("aaa.bb.C", new Object(){
            public @_static UUID generateUUID(){
                return UUID.randomUUID();
            }
        }));
        UUID uuid = (UUID)_r.eval("aaa.bb.C.generateUUID()");
        assertNotNull(uuid);
        uuid = (UUID)_r.eval("generateUUID()");
        assertNotNull(uuid);
    }
}
