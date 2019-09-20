package test.byexample.macro.custom;

import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro.macro;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class _2_Instance_Test extends TestCase {

    /* Style 2: macro instance nested class type (no annotation arguments/parameters)*/
    @Retention(RetentionPolicy.RUNTIME)
    public @interface _annInstanceNoArg {

        class Macro extends macro<_annInstanceNoArg, Node> {
            public Macro(_annInstanceNoArg annotation) {
                super(annotation);
            }

            public void expand(Node node){
                System.setProperty(_annInstanceNoArg.class.getSimpleName(), "true");
            }

            public String toString(){ return "macro["+ _annInstanceNoArg.class.getSimpleName()+"]"; }
        }
    }

    /* Test Style 2 */
    public void testInstanceNoArgMacro(){

        assertNull( System.getProperty(_annInstanceNoArg.class.getSimpleName()));

        //verify that
        @_annInstanceNoArg class D{}

        //apply the macro to the model
        _class _c = macro.to(D.class);
        assertEquals( "true", System.getProperty(_annInstanceNoArg.class.getSimpleName()));
        assertFalse( _c.hasAnno(_annInstanceNoArg.class));
    }


    public void setUp(){
        //here we are turning internal logging (to System out) on to show internals of macros
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        //remove all the properties we set
        System.clearProperty(_annInstanceNoArg.class.getSimpleName());
    }
}
