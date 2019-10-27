package test.byexample.macro.custom;

import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro.*;

public class _4_OnDifferentElementTypes_Test extends TestCase {

    public void setUp(){
        //here we are turning internal logging (to System out) on to show internals of macros
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
    }

    public void tearDown(){
        //turn the logger off AFTER running tests
        Log.setAdapter(new Log.SilentAdapter());
    }

    /*
    public @_non_static static class SC{
        static void m(){ }
    }
    */
    public void testOnType(){
        //_class _c = _class.of(SC.class);
        //assertTrue( !_c.isStatic());

        @_dto class C{ int x,y; }
    }

    public void testOnTypeUse_AnonymousClass(){
        //                    name
        _class _c = _class.of( "aaaa.C",
                new @_dto Object(){ int x,y;});
    }

    public void testOnNestedType(){
        class T{
            @_static class N{}
        }
    }

    public void testOnField(){
        class F{
            @_public @_static @_final @_init("1+2") int i;
            @_transient Integer tr;
            @_volatile Integer vol;
        }
    }

    public void testOnMethod(){
        @_abstract class M{

            /** WHAAAAA */
            @_abstract int absM(){ return 123; }

            //multiple annotations on each method (processed in order)
            @_public @_static @_final void m(){}
        }

        _class _c = macro.to(M.class);

        assertTrue( _c.getMethod("absM").isAbstract());

        System.out.println( _c );
        assertTrue( _c.isAbstract());
        assertTrue( _c.getMethod("m").is("public static final void m(){}"));

    }

    public void testOnConstructor(){
        class C{
            @_private C(){ }
        }
    }

    public void testOnParameter(){
        class P{
            //on constructor parameters
            //you can have 0,1, or more on EACH parameter
            P(@_final @_name("n") String s){ }

            //on method parameters
            void m( @_final String name, @_final int y){
            }
        }
        _class _c = macro.to(P.class);
        assertTrue( _c.getConstructor(0).getParameter(0).is("final String n"));
        assertTrue( _c.getMethod("m").getParameter(0).is("final String name"));
        assertTrue( _c.getMethod("m").getParameter(1).is("final int y"));
        System.out.println( _c );
        //_c.getMethod("m")
    }
}
