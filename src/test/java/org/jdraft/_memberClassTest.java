package org.jdraft;


import com.github.javaparser.utils.Log;
import org.jdraft.diff._diff;
import org.jdraft.macro._static;
import org.jdraft.macro._volatile;
import org.jdraft.macro._importClass;
import org.jdraft.macro._non_static;
import org.jdraft.macro._protected;
import org.jdraft.macro._final;
import org.jdraft.macro._package;
import org.jdraft.macro._public;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class _memberClassTest extends TestCase {

    @_package("som.pkg")
    @_importClass({Map.class, List.class, Set.class, HashSet.class})
    @_non_static @_public @_final
    private static class Baseline{

        static{
            System.out.println("replaced");
        }

        @_volatile @_public
        int a;

        @_protected @_static @_final
        public int g(){
            return 102;
        }
    }

    public void testL(){

        _class _c = _class.of(Baseline.class);
        _class _d = _class.of("som.pkg.Baseline",
                new @_public @_final @_importClass({Map.class, List.class, Set.class, HashSet.class}) Object(){
                    volatile public int a;

                    protected @_static final int g() { return 102; }

                }).staticBlock( ()->System.out.println("replaced"));

        assertTrue( _diff.of(_c, _d).isEmpty());

        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        assertEquals(_c, _d);

        System.out.println(_c);
    }

    /*
    public void testLocalM(){
        //we can USE an actual BaseLine template(note: it's private, so other classes dont have access it)
        Baseline bl = new Baseline();
        assertEquals(102, bl.g());

        //_type.of( clazz )
        _class _baseLine = Java._class( Baseline.class );
        assertEquals("som.pkg",_baseLine.getPackage() );
        assertTrue( _baseLine.listImports().contains(Map.class, List.class, Set.class, HashSet.class));
        assertEquals( _modifiers.of("public final"), _baseLine.getModifiers() );
        assertEquals( Ast.stmt("System.out.println(\"replaced\");"), _baseLine.getStaticBlock().getStmt(0));

        assertEquals( _field.of("public volatile int a"), _baseLine.getField("a"));

        assertEquals( _method.of("protected static final int g(){", "return 102;", "}"), _baseLine.getMethod("g"));
    }

    public void testClassAnnotations() {

        @_final
        class L {

            @_replace({"101", "23"})
            @_public private @_static int a = 101;


            @_public
            @_replace({"Hello", "GoodBye"})
            L(String a){
                System.out.println( "Hello there" + a );
            }

            @_public @_static @_final
            @_replace({"true", "false"})
            String c(){
                assert(true);
                return "this was true";
            }
        }
        _class _a = _class.of( L.class );
        System.out.println( _a.getConstructor(0) );

        _class _c = Java._class(L.class);
        assertTrue(_c.isPublic());
        assertTrue(_c.isFinal());
        assertTrue(!_c.isStatic());
    }

    @_static
    public class Inner{

    }

    public void testClassLocal(){
        _class _c = Java._class( Inner.class );
        assertTrue( _c.isStatic());
    }

    @_non_static
    public static class StaticInner{

    }


    public void testNonStatic() {
        _class _c = Java._class(StaticInner.class);
        assertTrue( !_c.isStatic());
    }

    public void testClassImport(){
        @_import({Map.class, Set.class})
        class F{

        }
        _class _c = Java._class(F.class);
        assertTrue(_c.listImports().contains(Map.class));
        assertTrue(_c.listImports().contains(Set.class));
    }

    public void testPack(){
        @_package("new.pkg")
        class P{

        }
        _class _c = Java._class(P.class);
        assertEquals("new.pkg", _c.getPackage());
    }

    /*
    public void testF(){
        class F{
            public F(){
            }
        }

        assertEquals( 1, F.class.getDeclaredConstructors()[0].getParameterCount());
        System.out.println( F.class.getDeclaredConstructors()[0].getAnnotatedParameterTypes()[0] );
        System.out.println( F.class.getDeclaredConstructors()[0].getAnnotatedParameterTypes()[0].getType() );

        _class _f = _class.of(F.class);
        assertTrue(_ctor.isNestedMatch(F.class.getDeclaredConstructors()[0], _f.getCtor(0)));

        //test a single argument
        class R{
            public R(int r){}
        }
        _class _c = _class.of(R.class);
        assertTrue(_ctor.isNestedMatch(R.class.getDeclaredConstructors()[0], _c.getCtor(0)));

        class T{
            public T(String...str){}
        }
        _c = _class.of(T.class);
        assertTrue(_ctor.isNestedMatch(T.class.getDeclaredConstructors()[0], _c.getCtor(0)));


    }
*/
}
