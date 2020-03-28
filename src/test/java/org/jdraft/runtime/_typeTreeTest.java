package org.jdraft.runtime;

import com.github.javaparser.ast.type.Type;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro._addImports;
import org.jdraft.macro._packageName;

import java.io.Serializable;

public class _typeTreeTest extends TestCase {

    public void testIntF(){
        //assertEquals( 1, 0x1);

        assertEquals( 15, 0xF);
        assertEquals( -268435456, 0xF0000000);
        assertEquals( 15, Integer.parseInt("F", 16));
        assertEquals( 15, Integer.parseUnsignedInt("F", 16));
        assertEquals( -268435456, Integer.parseUnsignedInt("F0000000",16) );
        /*
        System.out.println( Long.parseLong("F0000000",16) );
        long l = Long.parseLong("F0000000", 16);
        System.out.println( Long.toHexString(l) );

        System.out.println( "UnsignedInt" + Integer.parseUnsignedInt("F0000000", 16));

        assertEquals( -268435456, Integer.parseInt("F0000000", 16));
        assertEquals( -268435456, Integer.parseInt("F0000000", 16));
        */
        /*
        assertEquals( 15, Integer.parseInt("F", 16));
        assertEquals( -268435456, 0xF0000000);

        //assertEquals( 16, 0x10);
        assertEquals( -268435456, 0xF0000000);

        assertEquals( -268435456, 0xF0000000);

        assertEquals( 1, Integer.parseInt("1", 16));
        assertEquals( 15, Integer.parseInt("F", 16));
        assertEquals( 16, Integer.parseInt("10", 16));

        int i = 0xF0000000;
        int j = Integer.parseInt("F", 16);

        int k = Integer.parseInt("F0000000", 16);
        int l = Integer.parseInt("0xF0000000", 16);
        Ex.parseNumber("0xF0000000");
        */
        /*
        class C {
            int i = 0xF0000000;
            int j = 0XF0000000;
        }
        _class _c = _class.of(C.class);
        //fail
        _c.hashCode();
         */
    }

    /** COMMENTED OUT BECAUSE SLOW
    public void testSpringTypeTree(){
        //Class c  = org.objenesis.Objenesis.class;

        _archive _ar = _archive.of("C:\\Users\\Eric\\Downloads\\spring-core-5.1.9.RELEASE-sources.jar");
        _code._cache _cc = _code._cache.of(_ar );
        System.out.println( "CODE FILES :" + _cc.list_code().size() );
        System.out.println( "TYPE FILES :" + _cc.list_types().size() );

        //only print out the errors
        Log.setAdapter( new Log.StandardOutStandardErrorAdapter(){
            public void info(Supplier<String> messageSupplier){ }
            public void trace(Supplier<String> messageSupplier){ }
        });
        _typeTree _tt = _typeTree.of( _cc );
        //turn logger back off
        Log.setAdapter( new Log.SilentAdapter());
        System.out.println( _tt );

        System.out.println( "DIRECT CHILDREN" + _tt.listDirectChildren(Serializable.class));
        System.out.println( "DIRECT CHILDREN" + _tt.listDirectChildren(Serializable.class).size());
        System.out.println( "ALL IMPLEMENTERS OF SERIALIZABLE " + _tt.listAllDescendants(Serializable.class) );
        System.out.println( "ALL IMPLEMENTERS OF SERIALIZABLE " + _tt.listAllDescendants(Serializable.class).size() );
    }
    */

    @_packageName("aaaa.bbbb")
    @_addImports(Serializable.class)
    interface I1 extends Serializable{}

    @_packageName("bbbb.aaaa")
    interface I2{ }

    @_packageName("bbbb.aaaa")
    class C1{}

    @_packageName("aaaa.bbbb")
    class C2{}

    public void testDescendants(){
        _interface _i1 = _interface.of(I1.class);
        _interface _i2 = _interface.of(I2.class);
        _class _c1 = _class.of(C1.class);
        _class _c2 = _class.of(C2.class);

        _c1.addImplement(_i1);
        _c2.addImplement(_i2);

        _typeTree _tt = _typeTree.of(_i1, _i2, _c1, _c2);

        //System.out.println( _tt );

        //walks DOWN the
        assertEquals(2, _tt.listAllDescendants(Serializable.class).size());

        _c2.addImplement(Serializable.class);
        _tt = _typeTree.of(_i1, _i2, _c1, _c2);
        //System.out.println( _tt );
        assertEquals(3, _tt.listAllDescendants(Serializable.class).size());

        //this is true because c1 implements _i1 extends Serializable
        assertTrue(_tt.isImplements( _c1, Serializable.class));

        //_c2 directly implements Serializable
        assertTrue(_tt.isImplements( _c2, Serializable.class));

        //_c2
        assertFalse(_tt.isImplements(_c1, _i2) );
    }



    public @interface Blarf {
        @interface Blorf { }
    }

    public void testNestedAnnotationType(){
        _annotation _c = _annotation.of(Blarf.class);
        _typeTree tt = _typeTree.of(_c);

        System.out.println( tt );
    }

    public void testNestedAndLocalClasses(){
        @_addImports(Serializable.class)
        class L{
            class F extends L { //F is nested in L
                public void f(){
                    class LL implements Serializable{ //LL is nested in F

                    }
                }
            }
        }
        _class _l = _class.of(L.class);
        _typeTree _tt = _typeTree.of(_l);
        assertEquals(1, _tt.listAllDescendants(_l).size());
        assertEquals(1, _tt.listAllDescendants(Serializable.class).size());
        //System.out.println( _tt.listAllChildren(Serializable.class));
        //System.out.println( _tt.listAllChildren(Serializable.class).get(0).fullyQualifiedClassName);
    }

    public void testCompanionTypes(){
        class Y{ }
        class Comp implements Serializable{ }

        _class _c = _class.of(Y.class)
                .addCompanionTypes(_class.of(Comp.class),
                        _enum.of("E").addImplements(Serializable.class),
                        _interface.of("I").addExtend(Serializable.class)).addImports(Serializable.class);
        _c = _class.of(_c.toString());
        //System.out.println( _c );
        //System.out.println( "COMP TYPES "+ _c.listCompanionTypes() );

        _type c1 = _c.listCompanionTypes().get(0);
        assertEquals(2, c1.listCompanionTypes().size());
        _type c2 = _c.listCompanionTypes().get(1);
        assertEquals(2, c2.listCompanionTypes().size());
        _type c3 = _c.listCompanionTypes().get(2);
        assertEquals(2, c3.listCompanionTypes().size());

        //assertEquals( 0, _c.listCompanionTypes().get(0).listCompanionTypes().size()) ;
        //_typeTree _tt = _typeTree.of(_c);

        //assertEquals(3, _tt.listDirectChildren(Serializable.class).size());
    }

    public void testTypes(){
        _typeTree _tt = _typeTree.of(
                _interface.of("I").addExtend(Serializable.class),
                _enum.of("E").addImplements(Serializable.class),
                _class.of("C").addImplement("I"));
        assertEquals(2, _tt.listDirectChildren(Serializable.class).size());
        assertEquals(3, _tt.listAllDescendants(Serializable.class).size());

        assertEquals( 2, _tt.listAllAncestors("C").size()); //I, "Serializable"

        assertTrue( _tt.listAllAncestors("C").contains( _tt.node("I") ));
        assertTrue( _tt.listAllAncestors("C").contains( _tt.node("Serializable") ));
    }
    //test type parameters
    public void testClassNameNormal(){
        Type tt = Types.typeRef("AA<String>");
        assertEquals("AA", tt.asClassOrInterfaceType().getName().toString());

        _typeTree _tt = _typeTree.of( _class.of("AA<String>"), _class.of("BB<F>").addExtend("AA"));
        assertEquals( 1, _tt.listDirectChildren("AA").size());
        assertEquals( 1, _tt.listDirectChildren("AA<String>").size());
    }

    public void testClassWithPackage(){
        _class _c = _class.of("aaaa.bbbb.CC");
        _typeTree _tt = _typeTree.of(_c );
        assertEquals( 0, _tt.listDirectChildren("aaaa.bbbb.CC").size());
        assertEquals( 0, _tt.listDirectChildren("CC").size());
        assertEquals( 0, _tt.listDirectChildren(_c).size());

        _tt = _typeTree.of( _c, _class.of("ffff.gggg.HH").addImplement("CC<String>").addImports(_c));

        assertEquals( 1, _tt.listDirectChildren("aaaa.bbbb.CC").size());
        assertEquals( 1, _tt.listDirectChildren("CC").size());
        assertEquals( 1, _tt.listDirectChildren(_c).size());
    }

    public void testImplements(){
        _typeTree _tt = _typeTree.of(_class.of("A").addImplement(Serializable.class));
        //System.out.println( _tt );
        assertEquals( 1, _tt.listDirectChildren(Serializable.class).size());

        _tt = _typeTree.of( _class.of("A").addImplement(Serializable.class), _class.of("B").addImplement(Serializable.class));
        assertEquals( 2, _tt.listDirectChildren(Serializable.class).size());
    }

    public void testMultiImpl(){
        _typeTree _tt = _typeTree.of(_interface.of("A"), _class.of("B").addImplement(Serializable.class).addImplement("A"));
        assertEquals( 1, _tt.listDirectChildren(Serializable.class).size());
        assertEquals( 1, _tt.listDirectChildren("A").size());
    }

    public void testImplExt(){
        _typeTree _tt = _typeTree.of(_class.of("A").addExtend(TestCase.class).addImplement(Serializable.class));
        assertEquals( 1, _tt.listDirectChildren(Serializable.class).size());
        assertEquals( 1, _tt.listDirectChildren(TestCase.class).size());
        assertEquals( 0, _tt.listDirectChildren("A").size());
    }

    public void testDirectChildren(){
        _typeTree _tt = _typeTree.of(_class.of("A"));
        assertTrue(_tt.listDirectChildren("A").isEmpty());

        try{
            _tt.listDirectChildren("B");
            fail("expected exception");
        }catch(Exception e){
            //expected
        }
    }

    public void testABDirectChildren(){
        _typeTree _tt = _typeTree.of(_class.of("A"), _class.of("B").addExtend("A"));
        assertEquals(1, _tt.listDirectChildren("A").size() );
        assertEquals(0, _tt.listDirectChildren("B").size() );
    }

    public void testATestCaseDirectChildren(){
        _typeTree _tt = _typeTree.of(_class.of("A").addExtend(TestCase.class));
        assertEquals(1, _tt.listDirectChildren("TestCase").size() );
        assertEquals(0, _tt.listDirectChildren("A").size() );

        assertEquals(1, _tt.listAllDescendants("TestCase").size() );
        assertEquals(0, _tt.listAllDescendants("A").size() );

    }

    public void testMultiplLayers(){
        _typeTree _tt = _typeTree.of(_class.of("A").addExtend(TestCase.class),
                _class.of("B").addExtend("A"), _class.of("C").addExtend("B"));

        assertEquals(1, _tt.listDirectChildren("TestCase").size() );
        assertEquals(1, _tt.listDirectChildren("A").size() );
        assertEquals(1, _tt.listDirectChildren("B").size() );
        assertEquals(0, _tt.listDirectChildren("C").size() );

        assertEquals( 3, _tt.listAllDescendants("TestCase").size() );
    }
}

