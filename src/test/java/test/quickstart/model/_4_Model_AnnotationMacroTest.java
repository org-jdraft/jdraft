package test.quickstart.model;

import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro.*;

/**
 *
 * @see test.byexample.macro for examples of using macros
 * @see test.byexample.macro.custom for examples of building and using custom macros
 * @see test.byexample.macro._2_UsingAllBuiltInMacrosTest for examples using the built in macros
 */
public class _4_Model_AnnotationMacroTest extends TestCase {

    public void testApplyMacroAnnotation(){
        //use the @_get annotation to create get() methods for the appropriate fields (x,y)
        @_get class UsesMacro{
            int x,y;
        }
        //when we load the _class from the runtimeClass, the annotations are processed
        _class _c = _class.of( UsesMacro.class );
        // the corresponding _class (_c) will remove the @_get annotation after processing
        assertFalse( _c.hasAnnos() );

        assertNotNull(_c.firstMethodNamed("getX")); //verify getX() is created
        assertNotNull(_c.firstMethodNamed("getY")); //verify getY() is created
    }

    public void testApplyMacrosOnMultipleMembers(){
        //apply these annotations to
        //@_set @_get @_toString @_equals @_hashCode

        Log.info("Ok %s",()->"hi" );
        _class _c = _class.of( "aa.vv.M", new Object() {

            public @_static @_final int ID = 1003;

            @_init("100") int x;

            @_toCtor public void M(){
                System.out.println("In constructor");
            }

            public @_static final int method(){
                return ID;
            }
        } );

        assertTrue( _c.fieldNamed("ID").is("public static final int ID=1003;"));
        assertTrue( _c.fieldNamed("x").is("int x = 100;"));
        assertTrue( _c.getConstructor(0).is("public M(){ System.out.println(\"In constructor\"); }"));
        assertTrue( _c.firstMethodNamed("method").is("public static final int method(){ return ID; }"));

    }

    public void setUp(){
        Log.setAdapter( new Log.StandardOutStandardErrorAdapter());
    }

    public void tearDown(){
        Log.setAdapter( new Log.SilentAdapter());
    }
}
