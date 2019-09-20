package test.byexample.macro.custom;

import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro.macro;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;

/**
 * Shows how to develop and test a simple Macro
 *
 * Conventions
 *
 * Logging (whats the macro system doing?
 * Macros on different elements
 * Macros order of operations
 *
 */
public class _1_Consumer_Field_Test extends TestCase {

    /* Style 1: Roll your own Field Consumer Lambda on the Annotation */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface _annConsumerField {
        /**
         * this is the "macro", its a Field of type consumer on an Annotation
         * the name doesn't matter,
         * by
         */
        Consumer<Node> Macro = (node)-> {
            System.setProperty(_annConsumerField.class.getSimpleName(), "true");
            macro.removeAnnotation(node, _annConsumerField.class);
        };
    }

    /* Use/Test Style 1 */
    public void testUseConsumerFieldMacro(){
        //verify the property is NOT set BEFORE calling the macro
        assertNull(System.getProperty(_annConsumerField.class.getSimpleName()));

        //annotate an example with the macro
        @_annConsumerField class C{}

        //call the macro
        _class _c = macro.to(C.class);

        //verify the property was set (in the macro)
        assertEquals("true",System.getProperty(_annConsumerField.class.getSimpleName()));

        //verify the model doesn't still have the macro
        assertFalse(_c.hasAnnos()); //verify we cleaned up the annotation on the class
        assertFalse(_c.hasAnno(_annConsumerField.class));
        //System.out.println(_c );
    }

    public void setUp(){
        //here we are turning internal logging (to System out) on to show internals of macros
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        //remove all the properties we set
        System.clearProperty(_annConsumerField.class.getSimpleName());
    }
}
