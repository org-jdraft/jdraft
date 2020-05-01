package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.*;
import junit.framework.TestCase;
import org.jdraft.runtime._proxy;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

public class macroUseTest extends TestCase {

    public void testAnonymousObjectWithMacroAnnotation(){
        @_packageName("demo") @_dto class Point2D{
            @_final int x, y;
            public UUID uuid;
        }

        _class _c = _class.of(Point2D.class);
        System.out.println( _c );
        _proxy _p1 = _proxy.of(_c, 2, 100);
        _proxy _p2 = _p1.of(2, 100);
        assertEquals( _p1, _p2);
        assertEquals( _p1.instance, _p2.instance); //the underlying objects are ==
        assertEquals( _p1.hashCode(), _p2.hashCode()); //hashcodes are equal

        //change p2
        _p2.set("uuid", UUID.randomUUID());

        assertFalse( _p1.equals( _p2)); //objects are not equal
        assertFalse( _p1.hashCode() == _p2.hashCode()); //hashcodes are not equal
    }

    public void testFN() throws Exception {
        Class c = Class.forName("java.util.function.Function");
        System.out.println( c );
        class L{
            public Function<_type,_type> ft = (_t)-> null;

            public Function<_type,_type> ft2 = (_t)-> null;
        }
        System.out.println( L.class.getField("ft").getAnnotatedType() );
        System.out.println( L.class.getField("ft").getGenericType() );
        Type t = L.class.getField("ft").getGenericType();
        assertTrue( t.equals( L.class.getField("ft2").getGenericType()));
    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface U{
        macro<U, TypeDeclaration> M = new macro<U, TypeDeclaration>(U.class) {
            @Override
            public void expand(TypeDeclaration node) {
                node.tryAddImportToParentCompilationUnit(UUID.class);
            }
        };
    }

    public void testAnnotationFieldAsMacro(){
        @U class F{
        }
        _class _c = _class.of(F.class);
        assertTrue( _c.hasImport(UUID.class));
        assertFalse(_c.hasAnnoRef(U.class));
    }

    public void testT() {
        @_autoConstructor
        class C {
            int i;
        }
        _class _c = _class.of(C.class);
        _c = macro.to(C.class, _c);
        assertTrue(_c.getConstructor(0).getParameters().isEmpty());

        @_dto
        class D {
            @_final
            int i;
            int x, y, z;
        }
        _c = _class.of(D.class);
        assertTrue(_c.getConstructor(0).getParameter(0).isTypeRef(int.class));
        assertNotNull(_c.getMethod("equals"));
        assertNotNull(_c.getMethod("hashCode"));
        assertNotNull(_c.getMethod("toString"));
        assertNotNull(_c.getMethod("getX"));
        assertNotNull(_c.getMethod("getI"));
        assertNotNull(_c.getMethod("setZ").getParameter(0).isTypeRef(int.class));
    }
    public void testAutoEquals(){
        @_equals
        class E{
            int a;
            float f;
            double d;
            String s;
            boolean[] ba;
            UUID[] uuids;
        }
        _class _c = _class.of(E.class);
        assertNotNull( _c.getMethod("equals") );
        System.out.println( "EEEEEEEEEEEEEEEEEEEEEEEEE " + _c.getMethod("equals"));
        assertEquals(1, _equals.$primitive.listSelectedIn(_c).size());  //int a;
        assertEquals(1, _equals.$float.listSelectedIn(_c).size());      //float f
        assertEquals(1, _equals.$double.listSelectedIn(_c).size());     // double d
        assertEquals(1, _equals.$default.listSelectedIn(_c).size());    // String s
        assertEquals(1, _equals.$arrayOfPrimitives.listSelectedIn(_c).size()); //boolean ba
        assertEquals(1, _equals.$arrayOfObject.listSelectedIn(_c).size()); //UUID uuids
    }

    public void testAutoGet(){
        @_get
        class A{
            int x;
            String g;
            static final int r = 100; //no getter
        }
        _class _c = _class.of(A.class);
        assertTrue( _c.getMethod("getX").isTypeRef(int.class));
        assertTrue( _c.getMethod("getG").isTypeRef(String.class));
        assertNull( _c.getMethod("getR") );
    }

    public void testAutoSet(){
        @_set
        class A{
            int x;
            @_final int fin; //no setter
            String g;
            static final int r = 100; //no setter
        }
        _class _c = _class.of(A.class);
        System.out.println( _c );
        assertTrue( _c.getMethod("setX").getParameter(0).isTypeRef(int.class));
        assertTrue( _c.getMethod("setG").getParameter(0).isTypeRef(String.class));
        assertNull( _c.getMethod("setFin") );
        assertNull( _c.getMethod("setR") );
    }

    public void testAutoSetFluent(){
        @_setFluent
        class C{
            int x;
            @_final int fin; //no setter
            String g;
            static final int r = 100; //no setter
        }
        _class _c = _class.of(C.class);
        assertTrue( _c.getMethod("setX").getParameter(0).isTypeRef(int.class));
        assertTrue( _c.getMethod("setG").getParameter(0).isTypeRef(String.class));
        assertTrue( _c.getMethod("setG").isTypeRef(C.class));
        assertTrue( _c.getMethod("setX").isTypeRef(C.class));
        assertNull( _c.getMethod("setFin") );
        assertNull( _c.getMethod("setR") );
    }

    public void testAutoToString(){
        @_toString
        class E{
            String s;
            boolean[] ba;
            UUID[] uuids;
        }
        _class _c = _class.of(E.class);
        assertNotNull( _c.getMethod("toString") );
        System.out.println( _c );
        assertEquals(1, _toString.$simple.listSelectedIn(_c).size());  //String s;
        assertEquals(1, _toString.$arrayOfObjects.listSelectedIn(_c).size());      //uuids f
        assertEquals(1, _toString.$arrayOfPrimitives.listSelectedIn(_c).size());   //boolean[] ba
    }

    interface A{

    }
    interface B{

    }
    interface C{

    }

    @_promote
    @_extend({Serializable.class, Externalizable.class})
    interface D{

    }

    public void testExtend(){
        _interface _i = _interface.of(D.class);
        System.out.println( _i );

        assertTrue( _i.isExtends(Serializable.class));
        assertTrue( _i.isExtends(Externalizable.class));
    }

    public void testFinal(){
        @_final class C{
            @_final int a;
            @_final void m(){}

            @_final class F{

            }
        }
        _class _c = _class.of(C.class);
        assertTrue( _c.isFinal());
        assertTrue( _c.getField("a").isFinal());
        assertTrue( _c.getMethod("m").isFinal());

        System.out.println( _c);
        assertTrue( ((_class)_c.getDeclared("F")).isFinal());
    }

    public void testImplement(){
        @_implement( {Serializable.class, Externalizable.class})
        class C{
        }

        _class _c = _class.of(C.class);
        System.out.println( _c );
        assertTrue( _c.isImplements(Serializable.class));
        assertTrue( _c.isImplements(Externalizable.class));
    }


    public void testImport(){
        @_addImports( Map.class )
        class C{

        }
        _class _c = _class.of(C.class);
        assertTrue( _c.hasImport(Map.class));
    }

    @_non_static
    static class VV{
        @_non_static static int f = 100;
        @_non_static static int getF(){ return 212; }
    }

    public void testNonStatic(){
        _class _c = _class.of(VV.class);
        assertTrue( !_c.isStatic());
        assertTrue( !_c.getField("f").isStatic());
        assertTrue( !_c.getMethod("getF").isStatic());
    }

    public void testPackage(){
        @_packageName("aaaa.bbbb.cccc")
        class C{

        }
        assertEquals( "aaaa.bbbb.cccc", _class.of(C.class).getPackageName());
        assertEquals( _package.of("aaaa.bbbb.cccc"), _class.of(C.class).getPackage());
    }

    public void testPrivate(){
        @_private
        class C{
            @_private int f;
            @_private void m(){}

            @_private class F{

            }
        }
        _class _c = _class.of(C.class);
        assertTrue( _c.isPrivate());
        assertTrue( _c.getField("f").isPrivate());
        assertTrue( _c.getMethod("m").isPrivate());
        assertTrue( _c.getDeclared(_class.class,"F").isPrivate());
    }

    @_promote("aaaa.ffff")
    interface IF{ }

    @_promote("aaaa.ffff")
    class FF{ }

    @_promote("aaaa.ffff")
    enum R{ }

    @_promote("aaaa.ffff")
    @interface GF{ }

    public void testPromote(){
        assertEquals( "aaaa.ffff", _interface.of(IF.class).getPackageName());
        assertEquals( "aaaa.ffff", _class.of(FF.class).getPackageName());
        assertEquals( "aaaa.ffff", _enum.of(R.class).getPackageName());
        assertEquals( "aaaa.ffff", _annotation.of(GF.class).getPackageName());
    }

    public void testProtected(){
        @_protected class O{
            @_protected int g;
            @_protected void m() {}
        }
        _class _c = _class.of(O.class);
        assertTrue( _c.isProtected() );
        assertTrue( _c.getField("g").isProtected() );
        assertTrue( _c.getMethod("m").isProtected() );
    }

    public void testPublic(){
        @_public class T{}
        assertTrue( _class.of(T.class).isPublic() );
    }

    public void testRemove(){
        class T{
            @_remove int f;
            @_remove void m(){}
            @_remove T(){}

            @_remove class G{ }
        }
        _class _c = _class.of(T.class);
        assertEquals( 0, _c.listFields().size() );
        assertEquals( 0, _c.listMethods().size() );
        assertEquals( 0, _c.listConstructors().size() );
        assertEquals( 0, _c.listInnerTypes().size() );
    }

    /*
    public void testReplace(){

        class T{
            @_replace({"int","String"}) int f;
            @_replace({"m", "j"}) void m(){}
            @_replace({"f", "d"}) T(int f){}

            @_replace({"G", "H"}) class G{ }
        }
        //_class _c = _class.of(T.class);
        //_c.ast().walk(ClassOrInterfaceDeclaration.class, c-> System.out.println( "PARENT " + c.getParentNode().isPresent() ) );
        _class _c = _class.of(T.class);

        System.out.println( _c );
        assertTrue( _c.getField("f").isType("String"));
        assertTrue( _c.getField("f").isType(String.class));
        assertTrue( _c.getMethod("j").isVoid());
        assertTrue( _c.getConstructor(0).getParameter(0).isNamed("d"));

        assertNotNull( _c.getNestedClass("H") );
    }
    */

    public void testStatic(){
        @_static class F{
            @_static int f;
            @_static void m(){}
        }
        _class _c = _class.of(F.class);

        assertTrue(_c.isStatic());
        assertTrue(_c.getField("f").isStatic());
        assertTrue(_c.getMethod("m").isStatic());
    }

    public void testTransient(){
        class C{
            @_transient int f;
        }
        _class _c = _class.of(C.class);
        assertTrue( _c.getField("f").isTransient());
    }

    public void testVolatile(){
        class C{
            @_volatile int f;
        }
        _class _c = _class.of(C.class);
        assertTrue( _c.getField("f").isVolatile());
    }

}
