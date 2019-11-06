package test.othertools;

import junit.framework.TestCase;
import org.jdraft._method;
import org.jdraft.pattern.$method;

/**
 * A Rewrite taken from the presentation at Voxxed Days Ticino 2019 by Luca Molteni
 * https://youtu.be/1pSj6OQsq9k?t=1867
 */
public class JavaParserByLucaTest extends TestCase {

    private static final $method $GREET = $method.of(new Object(){
        public String greetings(String name){
            return $message$;
        }
        private String $message$;
    });

    public static _method generateGreetings(boolean parameter ){
        if( parameter ){
            return $GREET.draft("message", "\"Hello \" + name + System.lineSeparator()");
        }
        return $GREET.draft("message", "\"Good morning\"");
    }

    public void testGenerateMethod(){
        _method _m = generateGreetings(true);
        assertTrue( $GREET.matches(_m));
        _m = generateGreetings(false);
        assertTrue( $GREET.matches(_m));
    }
}
