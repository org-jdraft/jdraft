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
public class _1_Annotation_With_Consumer_Field_Test extends TestCase {

    /* Custom Annotation/Macro as an "Annotation With Consumer Field" -type
    *  meaning the Annotation signifying the macro */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface _annWithField {
        /**
         * THIS IS THE SIMPLEST TYPE OF ANNOTATION MACRO called a Consumer Field Macro
         *
         * this is the "macro" code, its a Field of type consumer on an Annotation
         * the name doesn't matter,
         * by
         */
        Consumer<Node> Macro = (node)-> {
            /* set a System Property to verify how many times it was called */
            System.setProperty(_annWithField.class.getSimpleName(),
                    ( Integer.parseInt(System.getProperty(_annWithField.class.getSimpleName(), "0") )+ 1) +"" );
            /* manually remove the annotation on the model */
            macro.removeAnnotation(node, _annWithField.class);
        };
    }

    /* Test "Annotation With Consumer Field" Style 1 */
    public void testUseConsumerFieldMacro(){
        // 1) verify the property is NOT set BEFORE calling the macro
        assertNull(System.getProperty(_annWithField.class.getSimpleName()));

        // 2) annotate an example with the macro
        @_annWithField
        class C{}

        // 3a) call the macro (here manually)
        _class _c = macro.to(C.class);

        // 3b) _class.of(...) will ALSO internally process macros
        _class _c2 = _class.of(C.class);

        assertEquals( _c, _c2);

        // verify the System property was set (in the macro)
        assertEquals("2",System.getProperty(_annWithField.class.getSimpleName()));

        // verify the model doesn't still have the macro
        assertFalse(_c.hasAnnoRefs()); //verify we cleaned up the annotation on the class
        assertFalse(_c.hasAnnoRef(_annWithField.class));
        System.out.println(_c );
    }

    public void setUp(){
        //here we are turning internal logging (to System out) on to show internals of macros
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        //remove all the properties we set
        System.clearProperty(_annWithField.class.getSimpleName());
    }

    public void tearDown(){
        Log.setAdapter(new Log.SilentAdapter());
    }
}
