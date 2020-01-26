package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._method;
import org.jdraft.pattern.$method;
import org.jdraft.runtime._proxy;

/**
 * A Rewrite taken from the presentation at Voxxed Days Ticino 2019 by Luca Molteni
 * https://youtu.be/1pSj6OQsq9k?t=1867
 */
public class JavaParserLucaReplace2Test extends TestCase {

    /**
     * Build a method "pattern" ($method) and specialize the pattern
     * with either
     * @param parameter
     * @return
     */
    public _method generateGreetings(boolean parameter){

        $method $m = $method.of(new Object(){
            public String greetings( String name){
                $code : return "Hello "+ name + System.lineSeparator();
            }
        });

        if( parameter ) {
            //setting code to true means KEEP the code at the label $code: & remove the $code label
            return $m.draft("code", true);
        }
        //specifying a non-boolean value for "code" will replace the code at $code: & remove the $code label
        return $m.draft("code", "return \"Good Morning\";" );
    }

    public void testGenerateMethod(){
        System.out.println( generateGreetings(true) );
        System.out.println( generateGreetings(false) );
    }

    /**
     * Create a new _class "C"
     * Generate the _method "greetings"
     * Add _method "greetings" to _class "C",
     * Compile the _class "C" to a Class "C" (with javac)   | proxy.of(...)
     * Load the Class "C" in a new ClassLoader              | proxy.of(...)
     * Create a new instance of the Class "C"               | proxy.of(...)
     * Call the "greetings" method on the instance          | proxy.call(...)
     * Verify the "greetings" returns the expected String from the generated method
     */
    public void testGenerate_RunMethod_VerifyResult(){
        assertEquals("Hello Eric"+System.lineSeparator(),
                _proxy.of( _class.of("C").method(generateGreetings(true)))
                        .call("greetings", "Eric"));

        assertEquals("Good Morning",
                _proxy.of( _class.of("C").method(generateGreetings(false)))
                        .call("greetings", "Eric"));
    }
}
