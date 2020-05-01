package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;

public class macroTest extends TestCase {

    public void testSimpleBuiltInMacroOnAnonymous(){
        _class _c = _class.of("aaaa.B", new Object(){
            int x, y;
        });
        assertTrue(_c.getField("x").isTypeRef(int.class));
        assertTrue(_c.getField("y").isTypeRef(int.class));
    }

    /* Style 1: Roll your own Field Consumer Lambda on the Annotation */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface _annConsumerField {
        /** this is the "macro", its a Field of type consumer on an Annotation
         * the name doesn't matter,
         * by
         */
        Consumer<Node> Macro = (node)-> {
            System.setProperty(_annConsumerField.class.getSimpleName(), "true");
            macro.removeAnnotation(node, _annConsumerField.class);
        };
    }

    /* Test Style 1 */
    public void testUseConsumerFieldMacro(){
        //verify the property is NOT set BEFORE calling the macro
        assertNull(System.getProperty(_annConsumerField.class.getSimpleName()));

        //annotate an example with the macro
        @_annConsumerField
        class C{}

        //call the macro
        _class _c = macro.to(C.class);

        //verify the property was set (in the macro)
        assertEquals("true",System.getProperty(_annConsumerField.class.getSimpleName()));

        //verify the model doesn't still have the macro
        assertFalse(_c.hasAnnoRefs()); //verify we cleaned up the annotation on the class
        assertFalse(_c.hasAnnoRef(_annConsumerField.class));
        //System.out.println(_c );
    }

    /* Style 2: macro instance nested class type (no annotation arguments/parameters)*/

    @Retention(RetentionPolicy.RUNTIME)
    @interface _annInstanceNoArg {

        class Macro extends macro<_annInstanceNoArg, Node> {
            public Macro(_annInstanceNoArg annotation) {
                super(annotation);
            }

            public void expand(Node node){
                System.setProperty(_annInstanceNoArg.class.getSimpleName(), "true");
            }

            public String toString(){ return "macro["+_annInstanceNoArg.class.getSimpleName()+"]"; }
        }
    }

    /* Test Style 2 */
    public void testInstanceNoArgMacro(){

        assertNull( System.getProperty(_annInstanceNoArg.class.getSimpleName()));

        //verify that
        @_annInstanceNoArg
        class D{}

        //apply the macro
        _class _c = macro.to(D.class);
        assertEquals( "true", System.getProperty(_annInstanceNoArg.class.getSimpleName()));
        assertFalse( _c.hasAnnoRef(_annInstanceNoArg.class));
        //List<Consumer<Node>> macros = macro.from(D.class);
        //assertEquals( 1, macros.size());
    }

    /* Style 3 annotation with arguments (being passed as inputs to the macro implementation */
    @Retention(RetentionPolicy.RUNTIME)
    @interface _annInstanceArgs {

        int x() default 0;
        int y() default 0;

        class Macro extends macro<_annInstanceArgs, Node> {

            public Macro(_annInstanceArgs mp){
                super(mp);
            }

            public String toString() { return "macro["+_annInstanceArgs.class.getSimpleName()+"("+this.annotation.x()+","+this.annotation.y()+")]"; }

            @Override
            public void expand(Node node) {
                System.setProperty(_annInstanceArgs.class.getSimpleName(), "true");
            }
        }
    }

    public void testNewInstanceMacro(){
        assertNull( System.getProperty(_annInstanceArgs.class.getSimpleName()));

        //adorn the annotation on an entity
        @_annInstanceArgs(x=1,y=2)
        class E{}

        //now return the model after running the macro on the runtime Class
        _class _c = macro.to(E.class);

        //verify the macro was run
        assertEquals( "true", System.getProperty(_annInstanceArgs.class.getSimpleName()));

        //verify we cleaned up at the end
        assertFalse( _c.hasAnnoRef(_annInstanceArgs.class));
    }

    public void setUp(){
        //here we are turning internal logging (to System out) on to show internals of macros
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        //remove all the properties we set
        //System.clearProperty(_annConsumerField.class.getSimpleName());
        System.clearProperty(_annInstanceNoArg.class.getSimpleName());
        System.clearProperty(_annInstanceArgs.class.getSimpleName());
    }

    public void tearDown(){
        Log.setAdapter(new Log.SilentAdapter());
    }
}
