package test.byexample.ftomassettiexamples;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro._private;

import java.util.Collection;
import java.util.List;

/**
 * https://github.com/ftomassetti/analyze-java-code-examples/blob/master/src/main/java/me/tomassetti/examples/CodeGenerationExample.java
 */
public class CodeGenerationExample extends TestCase {

    public void testGen(){
        _class _c = _class.of("my.example.javaparser.MyClass", new Object(){
            @_private List<String> elements;

            public void newElement(String newElement){
                elements.add(newElement);
            }

            public Collection<String> getElements(){
                return this.elements;
            }
        })
        .addImports("java.util.*"); //would compile/work with or without this*

        System.out.println( _c);

        //we can also compile the source so we know it works
        //_runtime.compile(_c);
    }

}
