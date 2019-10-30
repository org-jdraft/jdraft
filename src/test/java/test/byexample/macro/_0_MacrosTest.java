package test.byexample.macro;

import com.github.javaparser.utils.Log;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro.*;

/**
 * <P>Macros are programs, associated with {@link java.lang.reflect.AnnotatedElement} via Runtime Annotations
 * that facilitate to change the underlying/modified model (_field, _method, _type, _constructor)</P>
 *
 * <P>BY CONVENTION: Annotations that are associated with macros start with an "_" (underscore). This is consistent
 * with the jdraft API, makes it easier to spot and understand where macros are being used in code,
 * </P>
 *
 * Macros can be simple modifications (i.e. adding and/or removing a modifiers from a
 * {@link org.jdraft._type}, {@link org.jdraft._field}, {@link org.jdraft._method}, {@link org.jdraft._constructor}):
 * <UL>
 *     <LI>{@link _public}</LI>
 *     <LI>{@link _private}</LI>
 *     <LI>{@link _protected}</LI>
 *     <LI>{@link _abstract}</LI>
 *     <LI>{@link _static}</LI>
 *     <LI>{@link _non_static}</LI>
 *     <LI>{@link _final}</LI>
 *     <LI>{@link _transient}</LI>
 *     <LI>{@link _volatile}</LI>
 *     <LI>{@link _native}</LI>
 *     <LI>{@link _default}</LI>
 * </UL>
 *
 * -or- Macros can also be used to synthesize boilerplate code:
 * <UL>
 *     <LI>{@link _get}</LI>
 *     <LI>{@link _set}</LI>
 *     <LI>{@link _setFluent}</LI>
 *     <LI>{@link _equals}</LI>
 *     <LI>{@link _hashCode}</LI>
 *     <LI>{@link _toString}</LI>
 *     <LI>{@link _dto}</LI>
 *     <LI>{@link _autoConstructor}</LI>
 * </UL>
 *
 * <P>By convention Annotation/Macro Classes start with the "_"
 * this is so we can easily scan annotation uses to find ones that are macros
 * and because often what we want to accomplish is already a java keyword
 * {abstract, final, static, public, private, protected, volatile, synchronized}
 * so it's easy to read and understand the intent of the macro
 * also it helps with autocomplete in the editor, when you type in "@_"
 * The macro system
 *
 *
 * order
 * on different types
 * intermingled with other annotations
 * logging the internals
 */
public class _0_MacrosTest extends TestCase {

    public void setUp(){
        //(Optional) setup the internals of the macro system to log information
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
    }

    public void tearDown(){
        Log.setAdapter(new Log.SilentAdapter());
    }

    public void testAnno(){
        // macros are initiated by annotations, here we attach a _static annotation to a class
        @_static class C{ }

        // when we construct a _class (model) processing annotation/macros is done automatically
        _class _c = _class.of(C.class);

        // verify the class created (LOGICALLY) equals this (who cares about the format)
        // NOTE: after the macro is processed, the annotation (@_static) is removed*
        assertTrue(_c.is("public static class C{}"));
    }

    /** macros are applied FIRST to members (Methods, Fields, Constructors) in the order they are declared in code*/
    public void testMultipleMacroAnnotations(){
        @_static @Deprecated class D {
            @_static @_init("1 + 2") int i;
            @_static void m(){}
        }

        // all (4) macros will be processed, @Deprecated IS NOT an annotation/macro, so it is retained
        _class _c = macro.to(D.class);

        // after we process, we expect the class to look like this:
        // NOTE: the @Deprecated annotation IS NOT a macro annotation, so it is retained
        assertTrue( _c.is("@Deprecated public static class D{ static int i = 1 + 2; static void m(){} }" ));
    }


    public void testMacrosOnAllElementsAndNests(){
        @_static class C{
            //Note: the Macros are run in the order of the annotations,
            //here we are illustrating:
            // @_public gets called (and makes i public)
            // and THEN @_private gets called (and makes i private)
            @_public @_private int a;

            // Here we have a macro annotation @_init
            @_init("100") @Deprecated int b;
        }
    }

    public void testMultipleAnnosOnAnonymousConstructor(){
        //here we construct a new _class model, and pass in the @_get annotation on the
        //anonymous class (containing the code body)... this will ensure the getX() and getY()
        // methods are synthesized (as it appears in _expected)
        _class _c = _class.of("aaaa.bbbb.C", new @_get Object(){ int x,y; });
        _class _expected = _class.of( "aaaa.bbbb.C", new Object(){
            int x,y;
            public int getX(){
                return x;
            }
            public int getY(){
                return y;
            }
        });
        //verify they are equal (gets created)
        assertEquals( _c, _expected);
    }
}
