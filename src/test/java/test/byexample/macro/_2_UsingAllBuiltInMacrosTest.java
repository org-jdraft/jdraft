package test.byexample.macro;

import junit.framework.TestCase;
import org.jdraft._annotation;
import org.jdraft._class;
import org.jdraft._enum;
import org.jdraft._interface;
import org.jdraft.macro.*;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * Using the existing "Built-in jdraft @annotation macros" in the package {@link org.jdraft.macro}
 */
public class _2_UsingAllBuiltInMacrosTest extends TestCase {

    /** Conventions*/
    public void testUseAllBuiltInMacros(){

        @_abstract class A{
            //setting abstract on a method will REMOVE the body (and set it to "abstract int m();")
            @_abstract int m(){ return 2;}
        }

        _class _a = _class.of(A.class);
        assertTrue( _a.isAbstract());
        assertTrue(_a.getMethod("m").isAbstract());

        // here the _autoConstructor macro will know that the field a needs to be passed
        // in on the constructor (NOTE: the "members" (fields, methods, constructors) will
        // always get processed FIRST before the top level type (_class, _interface, _enum, _annotation)
        @_autoConstructor class B{ @_final int a; int b; }

        //Note: only final fields are required (so only a is in the parameters)
        assertTrue(_class.of(B.class).getConstructor(0).getParameters().is("int a"));

        //@_autoConstructor also works for building an _enum constructor... (but the constructor is private)
        //_enum _ea = _enum.of( "aaaa.E", new @_autoConstructor Object(){
        //     @_final int i;
        //});
        //assertTrue( _ea.getConstructor(0).isPrivate());
        //assertTrue( _ea.getConstructor(0).getParameter(0).isType(int.class));

        _interface _i = _interface.of("aaaa.I", new Object(){
            //default will set the default property (for default methods on interfaces)
            @_default
            int getValue() { return 101; }
        });

        assertTrue( _i.getMethod("getValue").isDefault());

        //dto is a "composite macro" it calls many other macros:
        // @_get, @_set, @_equals, @_hashCode, @_autoConstructor
        @_dto class D{ int x,y; }
        _class _d = _class.of(D.class);

        assertTrue(_d.getMethod("getX").isTypeRef(int.class));
        assertTrue(_d.getMethod("getY").isTypeRef(int.class));
        assertTrue(_d.getMethod("setX").getParameters().is("int x"));
        assertTrue(_d.getMethod("setY").getParameters().is("int y"));
        assertNotNull( _d.getMethod("equals"));
        assertNotNull( _d.getMethod("hashCode"));
        assertNotNull( _d.getMethod("toString"));
        assertNotNull( _d.getConstructor(0));

        //equals will synthesize an equals method based on the fields on the type
        @_equals class E{ String name; char[] initials; }

        //extend works on a class (single extension)
        @_extend(Map.class) class Ex{ }
        assertTrue( _class.of(Ex.class).isExtends(Map.class));

        /*
        //extend works on an interface (multiple Extension)
        _interface _i2 = _interface.of("asd.Inter",
                new @_extend({Serializable.class, Cloneable.class}) Object(){});

        System.out.println( _i2 );
        assertTrue( _i2.isExtends(Serializable.class));
        assertTrue(_i2.isExtends(Cloneable.class));
        assertTrue( _i2.isExtends(Serializable.class) && _i2.isExtends(Cloneable.class));
        */


        //@_final works on types
        @_final class F{
            //@_final works on fields
            @_final int a;
            //@_final works on methods
            @_final int g() { return 5; }
            //@_final works on parameters
            void h( @_final int v){}
        }
        _class _f = macro.to(F.class);
        assertTrue(_f.isFinal() );
        assertTrue( _f.getField("a").isFinal());
        assertTrue( _f.getMethod("g").isFinal());
        System.out.println( _f );
        assertTrue( _f.getMethod("h").getParameter(0).isFinal());

        //get works on classes (creates getter methods getX(), getY())
        @_get class G{ int x, y; }

        _class _g = _class.of(G.class);
        assertTrue( _g.getMethod("getX").isTypeRef(int.class));
        assertTrue( _g.getMethod("getY").isTypeRef(int.class));

        //get works on enums (creates getter methods getX(), getY())
        //_enum _e = _enum.of("GE", new @_get Object(){ int x, y; });

        //assertTrue( _e.getMethod("getX").isType(int.class));
        //assertTrue( _e.getMethod("getY").isType(int.class));

        //hashCode works on classes to build the hashCode method
        @_hashCode class H{ int x, y; }

        _class _h = _class.of(H.class);
        assertNotNull( _h.getMethod("hashCode") );

        //@_init works on fields to set the initial value
        class I{ @_init("100") int val; }
        _class _ic = _class.of( I.class);
        assertTrue( _ic.getField("val").isInit(100));

        //@_implement works to implement a single interface on a class
        @_implement(Serializable.class) class IM { }
        //@_implement works to implement multiple interfaces on a class
        @_implement({Serializable.class, Cloneable.class}) class ICM { }

        //@_implement works to implement a single or multiple interface on an enum
        _enum _ii = _enum.of("GI", new @_implement(Serializable.class) Object(){ int x, y; });

        //@_importClass will add a single import class on any type
        @_addImports(UUID.class) class IC{}
        //@_importClass will add a single import class on any type
        _enum _eic = _enum.of("aaa.EI", new @_addImports(UUID.class) Object(){});
        _interface _iic = _interface.of("aaa.II", new @_addImports(UUID.class) Object(){});
        _annotation _ac = _annotation.of("aaa.AI", new @_addImports(UUID.class) Object(){});

        //name works on type
        @_rename("Rename")class N{
            //name works on field
            @_rename("x") int i = 100;
            @_rename("method") void m(){}
        }
        assertEquals( "Rename", _class.of(N.class).getName());

        // THIS IS ARBITRARY, but it's here to represent how
        // (if you have a static nested (non-local) class
        // and you want it to be non-static you could use @_non_static
        @_static @_non_static class S{
            //@_non
            @_static @_non_static int ii;
            @_static @_non_static int method(){  return 5; }
        }
        assertTrue( !_class.of(S.class).isStatic() );


        //@_public @_protected @_private
        @_public @_protected @_private class PPP{

            @_public @_protected @_private int i;
            @_public @_protected @_private int m(){ return 33;}

            //these work on constructors
            @_public @_protected @_private PPP(){

            }
        }

        class R {
            //@_remove will remove this field
            @_remove int ID = 3;

            //@_remove removes this method
            @_remove void m() {}

            //@_remove removes this constructor
            @_remove R() {}

            //@_remove removes this Inner class
            @_remove class Inner{ }
        }

        //@_set synthesizes setR(int r) methods for appropriate fields on class
        @_set class Se{ int r; }

        //@_setFluent will add set() methods
        @_setFluent class SetF{ int r; }

        //toString will synthesize a toString() method
        @_toString class TS{ int[] vals; }

        //@_toCtor converts a method into a constructor
        class TC{
            int i;

            @_toCtor public void C(int i) {
                this.i = i;
            }
        }

        //@_transient adds the transient keyword to a field
        class TR{
            @_transient int f;
        }

        //@_volatile adds the volatile modifier to a field
        class V{
            @_volatile int v;
        }
    }

}
