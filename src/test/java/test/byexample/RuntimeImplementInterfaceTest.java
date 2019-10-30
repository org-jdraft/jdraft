package test.byexample;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro._final;
import org.jdraft.macro._static;
import org.jdraft.macro._toCtor;
import org.jdraft.runtime._runtime;

/**
 * Constructor Args
 * Compiler Options
 */
public class RuntimeImplementInterfaceTest extends TestCase {

    public interface ToImpl{
        String whoImplemented();
    }

    /**
     * The most natural and convenient way to create a new dynamic instance (If you have a resident Interface)
     * is
     */
    public void testRuntimeInstanceOfViaInterface(){

        ToImpl e = (ToImpl) _runtime.instanceOf(_class.of( "EE", new ToImpl(){
            @Override
            public String whoImplemented() {
                return "_runtime.instanceOf(_class)";
            }
        }));
        assertEquals("_runtime.instanceOf(_class)", e.whoImplemented());
    }

    public void testRuntimeImplAnonymousClass(){
        ToImpl ti = _runtime.impl(new ToImpl(){
           public String whoImplemented(){
               return "_runtime.impl(anon)";
           }
        });
        assertEquals("_runtime.impl(anon)", ti.whoImplemented());
    }

    /* create a runtime implementation of the ToImpl interface with the source from an anonymous class */
    public void testRuntimeImplAnnoMacros() throws NoSuchFieldException, IllegalAccessException {

        ToImpl ti = _runtime.impl("aa.bb.C", new ToImpl(){

            //we process this annotation to make the field static
            public @_static final int ID = 12345;

            public @_final int i; // this will be final, meaning we need to set it on construction

            @_toCtor public void C(int i){ //this "method" will be converted into a constructor
                this.i = i;
            }

            @Override
            public String whoImplemented() {
                return "_runtime.impl()";
            }
        },
        123); //argument passed in to the constructor of the class "aa.bb.C"
        assertEquals("_runtime.impl()", ti.whoImplemented());
        //reflective get the static field
        assertEquals(12345, ti.getClass().getField("ID").getInt(null));
    }





    /*
    //TODO look into @dto issues
    public void testRuntimeLoadInstanceCall() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        @_package("graph")
        @_set class Point{
            double x, y = 0.0d;

            public double slope(){
                return y/x;
            }
        }
        _class _c = _class.of(Point.class);
        //Log.setAdapter( new Log.SilentAdapter());
        _runtime _r = _runtime.of( _c);
        //build an instance
        Object instance = _r.instance("graph.Point");
        Class loadedClass = instance.getClass();
        assertEquals( "graph.Point", loadedClass.getCanonicalName() );
        assertEquals(0.0d/0.0d, loadedClass.getMethod("slope").invoke(instance) );
        /** This nonsense is WHY I wrote _proxy
        //using simple reflection, call the slope method on the point

        Arrays.stream(loadedClass.getMethods()).forEach(m -> System.out.println( m ));

        instance.getClass().getMethod("setY", long.class).invoke(instance, 100.0d);
        instance.getClass().getMethod("setX", long.class).invoke(instance, 50.0d);

        assertEquals( 100.0d/50.0d, instance.getClass().getMethod("slope").invoke(instance) );

    }
    */
}
