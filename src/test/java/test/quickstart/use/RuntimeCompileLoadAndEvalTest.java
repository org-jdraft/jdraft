package test.quickstart.use;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro.*;
import org.jdraft.runtime.*;
import test.byexample.runtime.RuntimeImplementInterfaceTest;

/** _runtime can create, load, and use Classes at Runtime */
public class RuntimeCompileLoadAndEvalTest extends TestCase {

    /** _jdraft can compile & load generated _class models as Classes to use at Runtime*/
    public void testRuntimeLoadAndStaticEval(){
        // compile & load a new Class xx.YY in a new ClassLoader/_runtime
        _runtime _r = _runtime.of( _class.of( "xx.YY", new Object(){
            public @_static int ID = 124;
            public @_static int GET(){ return 1000; }
            public @_static long DIV2( long input) { return input / 2; }
        }));
        // call eval() on the _runtime (_r) to get the value of a static field
        assertEquals( 124, _r.eval("xx.YY.ID"));
        assertEquals( 124, _r.eval("ID"));
        // call eval() on the _runtime (_r) to call a static method
        assertEquals(1000, _r.eval("xx.YY.GET()"));

        //call with a literal parameter
        assertEquals( 50L, _r.eval("xx.YY.DIV2(100)"));

        //call with a parameter (a field to be resolved)
        assertEquals( 62L,_r.eval("xx.YY.DIV2(ID)"));

        //call with a parameter (a method to be resolved & run)
        assertEquals( 500L, _r.eval("xx.YY.DIV2(GET())") );
        assertEquals( 25L, _r.eval("xx.YY.DIV2(DIV2(100))") );

        //create new instances
        assertNotNull( _r.eval( "new YY()") );
        assertNotNull( _r.eval( "new xx.YY()") );

        //call with a parameter (a method to be resolved& run)
        long div2 = (long)_r.eval("xx.YY.DIV2(System.currentTimeMillis())");
        assertTrue( div2 <  System.currentTimeMillis());
    }

    /** _proxy instances simplify creating & using dynamic instances at _runtime */
    public void testRuntime_proxy() {
        @_packageName("graph")
        @_dto class Point {
            double x =0.0d, y = 0.0d;
            public double slope() { return y / x; }
            public double distanceTo( double x2, double y2 ){
                return Math.sqrt((y2 - y) * (y2 - y) + (x2 - x) * (x2 - x));
            }
        }
        _runtime _r = _runtime.of(Point.class);
        /* build an instance of the Point class wrapped in a _proxy */
        _proxy _p = _r.proxy("graph.Point" );

        /* _proxy simplifies calling methods (over Java reflection) */
        assertEquals( 0.0d/0.0d, _p.call("slope"));
        /* _proxy can set fields directly or via set?() methods with set() they can also be "chained"*/
        _p.set("y", 100.0d) //call setter setY() OR directly update field "y"
                .set("x", 50.0d); //call setter setX() OR directly update field "x"
        assertEquals( 100.0d, _p.get("y"));
        assertEquals( 50.0d, _p.get("x"));
        assertEquals( 100.0d/50.0d, _p.call("slope"));
        assertEquals( 1.0d, _p.call("distanceTo", 50.0d, 101.0d));
    }

    public interface Engine{  String goes(); }

    /**
     * jdraft can easily build a dynamic implementation for an interface
     * so we can call method through the interface
     * @see RuntimeImplementInterfaceTest for more examples
     */
    public void testImpl(){
        Engine ee = _runtime.impl( new @_dto Engine(){
            int x,y;
            public String goes() {
                return "Bye Bye";
            }
        });
        assertEquals("Bye Bye", ee.goes());
    }
}