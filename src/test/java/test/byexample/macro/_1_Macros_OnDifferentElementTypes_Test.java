package test.byexample.macro;

import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro.*;

/**
 * Annotations can be placed on different element types, and therefore Macros
 * can be written and applied to code "in place"
 */
public class _1_Macros_OnDifferentElementTypes_Test extends TestCase {

    public void testOnType(){
        @_dto class C{ int x,y; }

        _class _c = _class.of(C.class); //macro processed here
        assertNotNull( _c.firstMethodNamed("getX"));
    }

    public void testOnTypeUse_AnonymousClass(){
        @_packageName("aaaa") @_dto class C{
            int x,y;
        }
        _class _c = _class.of(C.class);
                //_class.of( "aaaa.C",
                //new @_dto Object(){ int x,y;});
        assertNotNull( _c.firstMethodNamed("getY"));
    }

    public void testOnNestedType(){
        class T{
            @_static class N{}
        }
        assertTrue( _class.of(T.class).getInnerType("N").isStatic());
    }

    public void testOnField(){
        class F{
            @_public @_static @_final @_init("1+2") int i;
            @_transient Integer tr;
            @_volatile Integer vol;
        }
        _class _f = _class.of(F.class);
        assertTrue( _f.getField("i").isStatic());
        assertTrue( _f.getField("tr").isTransient());
        assertTrue( _f.getField("vol").isVolatile());
    }

    public void testOnMethod(){
        @_abstract class M{
            /** WHAAAAA */
            @_abstract int absM(){ return 123; }

            //multiple annotations on each method (processed in order)
            @_public @_static @_final void m(){}
        }

        _class _c = macro.to(M.class);
        assertTrue( _c.firstMethodNamed("absM").isAbstract());
        assertTrue( _c.isAbstract());
        assertTrue( _c.firstMethodNamed("m").is("public static final void m(){}"));
    }

    public void testOnConstructor(){
        class C{
            @_private C(){ }
        }
    }

    public void testOnParameter(){
        class P{
            // on constructor parameters
            // you can have 0,1, or more on EACH parameter
            P(@_final @_rename("n") String s){ }

            // on method parameters
            void m( @_final String name, @_final int y){
            }
        }
        _class _c = macro.to(P.class);
        assertTrue( _c.getConstructor(0).getParam(0).is("final String n"));
        assertTrue( _c.firstMethodNamed("m").getParam(0).is("final String name"));
        assertTrue( _c.firstMethodNamed("m").getParam(1).is("final int y"));
        System.out.println( _c );
    }

    public void setUp(){
        // here we are turning internal logging (to System out) on to show internals of macros
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
    }

    public void tearDown(){
        Log.setAdapter(new Log.SilentAdapter());//turn the logger off AFTER running tests
    }
}
