package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._method;
import org.jdraft.pattern.$method;
import org.jdraft.runtime._runtime;

public class JavaPoet_Tutorial_Test extends TestCase {

    /** https://github.com/square/javapoet#example */
    public void testHello(){
        _class _c = _class.of("com.example.helloworld.Hello")
                .main( "System.out.println(\"Hello, JavaPoet!\");");
        System.out.println( _c );
    }

    /** https://github.com/square/javapoet#code--control-flow */
    public void testMethod(){
        // Here we pass in an anonymous Object with a "real" method declaration and
        // jdraft will use the source of the method to build a model
        _method _m = _method.of(new Object(){
            void main() {
                int total = 0;
                for (int i = 0; i < 10; i++) {
                    total += i;
                }
            }
        });

        System.out.println( _m );
    }

    /** https://github.com/square/javapoet#l-for-literals */
    public void testComputeRange(){
        // jdraft builds and populates dynamic methods differently from JavaPoet
        // jdraft says...
        // 1) just build the code as you normally would
        // 2) create a _draft of this code (_m) by passing the method in an anonymous Object
        _method _m = _method.of( new Object(){
            int multiply10to20() {
                int result = 1;
                for (int i = 10; i < 20; i++) {
                    result = result * i;
                }
                return result;
            }
        });
        // 3) build a $method pattern ($m) from the _draft(_m)
        $method $m = $method.of(_m);

        // "parameterize" the values in the code, so (10,20,*) are now variables ("min", "max", "op")
        $m.$("10", "min", "20", "max", "*", "op");

        // to create a new _method, pass in the key/value pairs to draft

        // re-drafting the _draft (set the variables with their initial values in _m)
        _method _mult = $m.draft("min", 10, "max", 20, "op", "*");
        assertEquals( _m, _mult); //verify that we get the same _method(_mult) when we re-draft with same vars

        // change the range from (10,20) to (5,15)
        _method _mulr = $m.draft("min", 5, "max", 15, "op", "*");

        assertTrue( _mulr.getName().equals("multiply5to15") );
        _method _plus = $m.draft("min", 10, "max", 20, "op", "+");

        System.out.println( _plus );
    }

    /**
     * jdraft can get the source code from any anonymous Object(*)
     * this makes it easy to build code with complicated control flow inside your IDE of choice
     *
     * (*) assuming the code is in the classpath, and for (Netbeans, IntelliJ, Eclipse) it is
     */
    public void testC(){
        _method _m = _method.of( new Object() {
            void main() {
                long now = System.currentTimeMillis();
                if (System.currentTimeMillis() < now) {
                    System.out.println("Time travelling, woo hoo!");
                } else if (System.currentTimeMillis() == now) {
                    System.out.println("Time stood still!");
                } else {
                    System.out.println("Ok, time still moving forward");
                }
            }
        });
    }

    /** https://github.com/square/javapoet#l-for-literals */
    public void testForLiterals(){
        $method $m = $method.of( new Object(){
            String slimShady() {
                return "slimShady";
            }
        }).$("slimShady", "name");

        _class _c = _class.of("HelloWorld").setFinal();

        _c.method($m.draft("name", "slimShady"));
        _c.method($m.draft("name", "eminem"));
        _c.method($m.draft("name", "marshallMathers"));

        _c.forMethods(m-> m.setPublic()); //lets make all methods public so we can test them

        //ok lets compile/load and test that it works
        _runtime _r = _runtime.of(_c );

        assertEquals("slimShady", _r.eval("new HelloWorld().slimShady()"));
        assertNotNull( "eminem", _r.eval("new HelloWorld().eminem()"));
        assertNotNull( "marshallMathers", _r.eval("new HelloWorld().marshallMathers()"));

        _c.forMethods(m->m.setPackagePrivate()); //set all methods back to packagePrivate
    }
}
