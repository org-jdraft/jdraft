package test.byexample;

import junit.framework.TestCase;
import org.jdraft.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class OfClassTest extends TestCase {

    class C{ int i; }
    enum E{ A; }
    interface I{ void m(); }
    @interface A{ int value(); }

    public void testBuildOfClass(){
        class LocalClass{
            int val;
        }
        _java._domain[] ms = {
                // (build a specific _type) from the source of the class
                _type.of( C.class),

                _class.of(C.class),
                _enum.of(E.class),
                _interface.of(I.class),
                _annotation.of(A.class),

                //note the class can be a top level class
                _class.of(LocalClass.class), //Local (to this method) classes

                _annoExpr.of(Deprecated.class), //the @Deprecated annotation
                _throws.of(RuntimeException.class),
                _import.of(Map.class),
                _imports.of(File.class, IOException.class),
                _typeRef.of(Integer.class),

                _catch.of(IOException.class, URISyntaxException.class)
                        .setBody(()-> {System.out.println("Ohh man, ohh geez");}),
                _newExpr.of( URL.class ),
                _parameter.of(String.class, "name"),
        };
    }
}
