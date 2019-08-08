package org.jdraft.runtime;

import com.github.javaparser.ast.type.Type;
import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro._importClass;

import java.io.Serializable;

public class _typeTreeTest extends TestCase {

    public void testNestedAndLocalClasses(){
        @_importClass(Serializable.class)
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
        assertEquals(1, _tt.listAllChildren(_l).size());
        assertEquals(1, _tt.listAllChildren(Serializable.class).size());
        //System.out.println( _tt.listAllChildren(Serializable.class));
        //System.out.println( _tt.listAllChildren(Serializable.class).get(0).fullyQualifiedClassName);
    }

    public void testCompanionTypes(){
        class Y{ }
        class Comp implements Serializable{ }

        _class _c = _class.of(Y.class)
                .addCompanionTypes(_class.of(Comp.class),
                        _enum.of("E").implement(Serializable.class),
                        _interface.of("I").extend(Serializable.class)).imports(Serializable.class);
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
                _interface.of("I").extend(Serializable.class),
                _enum.of("E").implement(Serializable.class),
                _class.of("C").implement("I"));
        assertEquals(2, _tt.listDirectChildren(Serializable.class).size());
        assertEquals(3, _tt.listAllChildren(Serializable.class).size());

        assertEquals( 2, _tt.listAllAncestors("C").size()); //I, "Serializable"

        assertTrue( _tt.listAllAncestors("C").contains( _tt.node("I") ));
        assertTrue( _tt.listAllAncestors("C").contains( _tt.node("Serializable") ));
    }
    //test type parameters
    public void testClassNameNormal(){
        Type tt = Ast.typeRef("AA<String>");
        assertEquals("AA", tt.asClassOrInterfaceType().getName().toString());

        _typeTree _tt = _typeTree.of( _class.of("AA<String>"), _class.of("BB<F>").extend("AA"));
        assertEquals( 1, _tt.listDirectChildren("AA").size());
        assertEquals( 1, _tt.listDirectChildren("AA<String>").size());
    }

    public void testClassWithPackage(){
        _class _c = _class.of("aaaa.bbbb.CC");
        _typeTree _tt = _typeTree.of(_c );
        assertEquals( 0, _tt.listDirectChildren("aaaa.bbbb.CC").size());
        assertEquals( 0, _tt.listDirectChildren("CC").size());
        assertEquals( 0, _tt.listDirectChildren(_c).size());

        _tt = _typeTree.of( _c, _class.of("ffff.gggg.HH").implement("CC<String>").imports(_c));

        assertEquals( 1, _tt.listDirectChildren("aaaa.bbbb.CC").size());
        assertEquals( 1, _tt.listDirectChildren("CC").size());
        assertEquals( 1, _tt.listDirectChildren(_c).size());
    }

    public void testImplements(){
        _typeTree _tt = _typeTree.of(_class.of("A").implement(Serializable.class));
        //System.out.println( _tt );
        assertEquals( 1, _tt.listDirectChildren(Serializable.class).size());

        _tt = _typeTree.of( _class.of("A").implement(Serializable.class), _class.of("B").implement(Serializable.class));
        assertEquals( 2, _tt.listDirectChildren(Serializable.class).size());
    }

    public void testMultiImpl(){
        _typeTree _tt = _typeTree.of(_interface.of("A"), _class.of("B").implement(Serializable.class).implement("A"));
        assertEquals( 1, _tt.listDirectChildren(Serializable.class).size());
        assertEquals( 1, _tt.listDirectChildren("A").size());
    }

    public void testImplExt(){
        _typeTree _tt = _typeTree.of(_class.of("A").extend(TestCase.class).implement(Serializable.class));
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
        _typeTree _tt = _typeTree.of(_class.of("A"), _class.of("B").extend("A"));
        assertEquals(1, _tt.listDirectChildren("A").size() );
        assertEquals(0, _tt.listDirectChildren("B").size() );
    }

    public void testATestCaseDirectChildren(){
        _typeTree _tt = _typeTree.of(_class.of("A").extend(TestCase.class));
        assertEquals(1, _tt.listDirectChildren("TestCase").size() );
        assertEquals(0, _tt.listDirectChildren("A").size() );

        assertEquals(1, _tt.listAllChildren("TestCase").size() );
        assertEquals(0, _tt.listAllChildren("A").size() );

    }

    public void testMultiplLayers(){
        _typeTree _tt = _typeTree.of(_class.of("A").extend(TestCase.class),
                _class.of("B").extend("A"), _class.of("C").extend("B"));

        assertEquals(1, _tt.listDirectChildren("TestCase").size() );
        assertEquals(1, _tt.listDirectChildren("A").size() );
        assertEquals(1, _tt.listDirectChildren("B").size() );
        assertEquals(0, _tt.listDirectChildren("C").size() );

        assertEquals( 3, _tt.listAllChildren("TestCase").size() );
    }
}

