package test.byexample.macro.custom;

import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro.macro;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <PRE>
 * This illustrates Custom Macros and passing arguments (in the form of Annotation Type Elements)
 * to the macro.
 *
 * The JLS (Java Language Specification) defines the types of arguments that can be used as
 * Annotation Type Elements (and therefore passed to macro implementations).
 *
 * from <A HREF="https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.6.1">JLS Annotation Types Elements</A>
 *
 * In summary:
 *     a primitive ( boolean, float, double, int, char, ...)
 *     a String
 *     a Class
 *     a Invocation of a Class (i.e. MyClass<String>)
 *     an Enum Type
 *     an Annotation Type
 *     a (single dimension) array of (primitives, Strings, Class, Invocation of Class, Enum Types, AnnotationTypes)
 * </PRE>
 * @see macro
 */
public class _3_Annotation_With_Macro_Instance_Args_Test extends TestCase {

    /* Style 3 annotation with arguments (being passed as inputs to the macro implementation */
    @Retention(RetentionPolicy.RUNTIME)
    @interface _annInstanceArgs {
        /* Define arguments to be passed into a macro (as annotation members) */
        int x() default 0;
        int y() default 0;

        class Macro extends macro<_annInstanceArgs, Node> {

            public Macro(_annInstanceArgs mp){
                super(mp);
            }

            @Override
            public void expand(Node node) {
                System.setProperty("x", this.annotation.x()+"");
                System.setProperty("y", this.annotation.y()+"");
                System.setProperty(_annInstanceArgs.class.getSimpleName(), "true");
            }

            public String toString() {
                return "macro["+ _annInstanceArgs.class.getSimpleName()+"("+this.annotation.x()+","+this.annotation.y()+")]";
            }
        }
    }

    public void testInstanceMacro() {
        assertNull(System.getProperty(_annInstanceArgs.class.getSimpleName()));

        //adorn the annotation on an entity (here we rely on the default values 0, 0 for x,y
        @_annInstanceArgs()
        class E {
        }

        //now return the model after running the macro on the runtime Class
        _class _c = macro.to(E.class);

        //verify the macro was run
        assertEquals("true", System.getProperty(_annInstanceArgs.class.getSimpleName()));
        assertEquals("0", System.getProperty("x"));
        assertEquals("0", System.getProperty("y"));

        //verify we cleaned up at the end
        assertFalse(_c.hasAnnoRef(_annInstanceArgs.class));
    }

    public void testPassAnnoArgs(){
        //run it again this time use explicit (rather than default) args
        @_annInstanceArgs(x=100,y=200) class F{}

        //now return the model after running the macro on the runtime Class
        _class _c = macro.to(F.class);

        //verify the macro was run
        assertEquals( "true", System.getProperty(_annInstanceArgs.class.getSimpleName()));
        assertEquals( "100", System.getProperty("x")); //we can use these args in the macro
        assertEquals( "200", System.getProperty("y"));

        //verify we cleaned up at the end
        assertFalse( _c.hasAnnoRef(_annInstanceArgs.class));
    }

    public void setUp(){
        //here we are turning internal logging (to System out) on to show internals of macros
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
    }

    public void tearDown(){
        //remove all the properties we set
        System.clearProperty(_annInstanceArgs.class.getSimpleName());
        System.clearProperty("x");
        System.clearProperty("y");
        Log.setAdapter(new Log.SilentAdapter());
    }
}
