package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._method;
import org.jdraft._parameter;
import org.jdraft.macro._abstract;
import org.jdraft.macro._protected;
import org.jdraft.macro._public;
import org.jdraft.macro._remove;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$method;
import org.jdraft.pattern.$node;
import org.jdraft.runtime._runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
            String $name$() {
                return "$name$";
            }
        });

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

    /** https://github.com/square/javapoet#t-for-types */
    public void testAutoImportTypesOnAPI(){
        _class _c = _class.of("com.example.helloworld.HelloWorld", new Object(){
            Date today(){
                return new Date();
            }
        });
        // because Date is a return type on the Anonymous Objects public API,
        // jdraft is smart enough to automatically import it
        assertTrue( _c.hasImport(Date.class) );

        _runtime.compile(_c); //verify it compiles
    }

    /** https://github.com/square/javapoet#t-for-types */
    public void testManualImport(){
        _class _c = _class.of("com.example.helloworld.HelloWorld").setFinal();
        // because we added a method with a String, we can't infer the import (Hoverboard)
        _c.method("Hoverboard tommorrow() { return new Hoverboard(); }");
        // so we can add it manually to the _class _c
        _c.imports("com.mattel.Hoverboard");
    }

    /** https://github.com/square/javapoet#t-for-types */
    public void testManualImport2(){
        _class _c = _class.of("com.example.helloworld.HelloWorld").setFinal();

        // because we added a method with a String, we can't infer the imports (Hoverboard, List)
        _c.method("List<Hoverboard> beyond() { ",
                "    List<Hoverboard> result = new ArrayList<>();",
                "    result.add(new Hoverboard());",
                "    result.add(new Hoverboard());",
                "    result.add(new Hoverboard());",
                "    return result;",
                " }");

        // so we can add it manually to the _class _c
        _c.imports("com.mattel.Hoverboard");
        _c.imports(ArrayList.class, List.class); //we can add imports by Class
    }

    /** https://github.com/square/javapoet#import-static */
    public void testImportStatic(){
        _class _c = _class.of("com.example.helloworld.HelloWorld").setPackagePrivate();
        _c.importStatic("com.mattel.Hoverboard.Boards.*;",
                "com.mattel.Hoverboard.createNimbus;",
                "java.util.Collections.*;")
                .imports("com.mattel.Hoverboard")
                .imports(ArrayList.class, List.class);

        _c.method( new Object(){
            List<Hoverboard> beyond(){
                List<Hoverboard> result = new ArrayList<>();
                result.add( createNimbus(2000));
                result.add( createNimbus("2001"));
                result.add( createNimbus(THUNDERBOLT));
                sort(result);
                return result.isEmpty() ? emptyList() : result;
            }
            //this code exists so the above code compiles as is
            // the @_remove @macro will remove the code (it's not on the _draft model)
            @_remove List emptyList(){ return Collections.emptyList(); }
            @_remove void sort(List l){ Collections.sort(l); }
            @_remove Hoverboard createNimbus(Object var){ return null; }
            @_remove class Hoverboard implements Comparable {
                public int compareTo(Object o) { return 0; }
            }
            @_remove Object THUNDERBOLT = null;
        });
        System.out.println( _c );
    }


    /** https://github.com/square/javapoet#n-for-names */
    public void testNames(){

        // because we do things differently in jdraft
        // it's easier to just build a class containing the real code
        // and interdependencies to methods (instead of defining a $N name)
        class Hex {
            public String byteToHex(int b) {
                char[] result = new char[2];
                result[0] = hexDigit((b >>> 4) & 0xf);
                result[1] = hexDigit(b & 0xf);
                return new String(result);
            }

            public char hexDigit(int i) {
                return (char) (i < 10 ? i + '0' : i - 10 + 'a');
            }
        }
        _class _c = _class.of(Hex.class);

        //in the spirit of things, lets:
        // change the name of the method hexDigit, & update all internal calls
        $node.of("hexDigit").replaceIn(_c, "_0xdigit");
        System.out.println( _c );

        //now lets compile/load and test the modified code
        _runtime _rt = _runtime.of( _c );
        assertEquals( 'a', _rt.eval("new Hex()._0xdigit(10)"));
        assertEquals( 'f', _rt.eval("new Hex()._0xdigit(15)"));
    }

    /** https://github.com/square/javapoet#methods */
    public void testMethods(){
        //we use real code, and apply @macros to achieve the same thing
        @_public @_abstract class HelloWorld {
            @_protected @_abstract void flux(){}
        }
        _class _c = _class.of(HelloWorld.class);
        assertTrue( _c.isAbstract() );
        assertTrue( _c.getMethod("flux").isAbstract() );
        assertFalse( _c.getMethod("flux").isImplemented() );
    }

    /** https://github.com/square/javapoet#constructors */
    public void testConstructors(){
        // Again we are spared writing "factory code" to create the class/constructor
        // just write the code you want (and you can modify it as a _class object
        @_public class HelloWorld {
            private final String greeting;

            public HelloWorld(String greeting) {
                this.greeting = greeting;
            }
        }
        _class _c = _class.of(HelloWorld.class);
    }

    /** https://github.com/square/javapoet#parameters */
    public void testParameters(){
        _parameter._parameters _ps = _parameter._parameters.of("int a");
    }
}
