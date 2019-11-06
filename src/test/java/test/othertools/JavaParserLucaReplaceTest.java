package test.othertools;

import junit.framework.TestCase;
import org.jdraft._method;
import org.jdraft.pattern.$;

/**
 * A Rewrite taken from the presentation at Voxxed Days Ticino 2019 by Luca Molteni
 * https://youtu.be/1pSj6OQsq9k?t=1867
 */
public class JavaParserLucaReplaceTest extends TestCase {

    public _method generateGreetings(boolean parameter ){
        class C{
            public String greetings( String name){
                return "Hello "+ name + System.lineSeparator();
            }
        }
        _method _m = _method.of(C.class, "greetings", String.class);
        if( !parameter ){
            return $.binaryExpr("\"Hello \" + name + System.lineSeparator()")
                    .replaceIn(_m, "Good Morning");
        }
        return _m;
    }

    public void testGenerateMethod(){
        System.out.println( generateGreetings(true) );
        System.out.println( generateGreetings(false) );
    }
}
