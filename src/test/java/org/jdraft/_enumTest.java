/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdraft;

import java.io.Serializable;

import com.github.javaparser.ast.type.Type;
import junit.framework.TestCase;
import org.jdraft._enum.*;
import org.jdraft.runtime._runtime;
import test.ComplexEnum;

/**
 *
 * @author Eric
 */
public class _enumTest extends TestCase {
 
    interface MemberI{ }
    
    interface $Member{        
        interface MemberMember{}
    }

    public void testConstantInit(){

            _enum _e =_enum.of("E", new Object(){
                /** a javadoc */
                _constant Y;
                _constant XX, XY, X0;
                _constant WithConstructorArgs = new _constant("arg1", 100);
                _constant Yes = new _constant();
                _constant No = new _constant("a", 100){
                    public int i = 0;
                    public String toString(){
                        return "HELLO";
                    }
                };
            });
            System.out.println( _e );

    }
    public void testImplementMemberClass(){
        _enum _e = _enum.of("E").implement(MemberI.class).implement($Member.class).implement($Member.MemberMember.class);
        System.out.println( _e );
    }
    
    public void testHeader(){
        _enum _e = _enum.of("/* License */",
                "package aaaa.bbbb;",
                "/**",
                " * JavaDoc",
                " */",
                "public enum FFF{  ; }" );
        assertNotNull( _e.getHeaderComment() );
        assertNotNull( _e.getJavadoc() );
        assertEquals( _e.getJavadoc().toString().trim(), 
            Ast.javadocComment(
                "/**", 
                " * JavaDoc", 
                " */").toString().trim() );
        assertEquals( Ast.blockComment("/* License */").toString(), _e.getHeaderComment().toString() );
        System.out.println( _e.getHeaderComment() );
    }

    public void testForMembers(){
        _enum _e = _enum.of("E", new Object(){
            public static final int ID=102; 
            public static final String NAME = "Eric";
        }).constants("A", "B", "C", "D", "E");
           
        _e.forDeclared(_field.class, f-> f.isStatic(), f->System.out.println(f));
        
        _e.forFields( f-> f.isStatic(), f-> System.out.println(f));
    }
    
    public void testConstants(){
        _enum _e = _enum.of("Suit").constants("Hearts","Clubs","Spades","Diamonds");
        assertEquals(4, _e.listConstants().size() );
    }

    
    public void testFullyQualified(){
        _enum _a = _enum.of("E").implement("aaaa.A", "bbbb.B");
        _enum _b = _enum.of("E").implement("B", "A");
        assertEquals( _a, _b);
        assertEquals( _a.hashCode(), _b.hashCode());
    }

    public void testEnum(){
        Ast.constantDecl("A(1)");
        _enum _e = _enum.of("E")
                .constant("A(1)")
                .field("private int i;")
                .constructor("private E(int i){ this.i = i;}");
    }

    public void testType(){
        //todo, ned to
        Type t = Ast.typeRef("@ann aaaa.bbbb.I<T>");
        assertTrue( t.isClassOrInterfaceType() );
        System.out.println( t.toString() );
        assertEquals( "aaaa.bbbb.I", t.toString(Ast.PRINT_NO_ANNOTATIONS_OR_TYPE_PARAMETERS));

        System.out.println( t.getElementType() );
    }

    /**
     * Checking isImports based on different usages
     * (fully qualified or not)
     * fully qualified import
     * wildcardImport
     * Generic
     */
    public void testTypeImport(){

        //explicitly implementing the fully qualified type
        _enum _e = _enum.of("E").implement(java.io.Serializable.class.getCanonicalName());
        System.out.println( _e);
        assertTrue( _e.isImplements(Serializable.class));
        assertTrue( _e.isImplements("Serializable"));
        assertTrue( _e.isImplements(Serializable.class.getCanonicalName()));

        //implementing by simple name, _enum is in the same package as interface
        _interface _i = _interface.of("aaaa.bbbb.I");
        Class iClass = _runtime.Class(_i); //create me the interface class in same package
        _e = _enum.of("aaaa.bbbb.E").implement("I");
        assertTrue( _e.isImplements("I"));
        assertTrue( _e.isImplements("aaaa.bbbb.I"));

        //implementing by simple name, importing by fully qualified name
        _e = _enum.of("E").implement("Serializable").imports(Serializable.class);
        assertTrue( _e.isImplements(Serializable.class));
        assertTrue( _e.isImplements("Serializable"));
        assertTrue( _e.isImplements(Serializable.class.getCanonicalName()));

        //implementing by simple name, importing by wildcard
        _e = _enum.of("E").implement("Serializable").imports("java.io.*");
        assertTrue( _e.isImplements(Serializable.class));
        assertTrue( _e.isImplements("Serializable"));
        assertTrue( _e.isImplements(Serializable.class.getCanonicalName()));

        //test generic implement
        _e = _enum.of("E").implement("aaaa.bbbb.G<I>");
        //should match
        assertTrue( _e.isImplements("aaaa.bbbb.G<I>"));
        assertTrue( _e.isImplements("aaaa.bbbb.G"));
        assertTrue( _e.isImplements("G"));
        assertTrue( _e.isImplements("G<I>"));

        assertFalse(_e.isImplements("xxx.yyy.G"));
        assertFalse(_e.isImplements("xxx.yyy.G<I>"));

        //test generic implement
        _e = _enum.of("E").implement("aaaa.bbbb.G<String>");
        assertTrue( _e.isImplements("aaaa.bbbb.G<String>")); //fully qualified
        assertTrue( _e.isImplements("G<String>")); //not fully qualified

        //yeah this is still a PITA
        //assertTrue( _e.isImplements("aaaa.bbbb.G<java.lang.String>"));
    }

    interface F{

    }
    class R{

    }
    interface II<U extends String>{

    }
    class D implements II<String>{

    }

    public void testImport(){
        _enum _e = _enum.of( ComplexEnum.class);
        System.out.println( _e );
        assertEquals("ComplexEnum", _e.getName());
        assertTrue(_e.getModifiers().is("public"));
        assertTrue(_e.isImplements( Serializable.class ) );
        assertTrue(_e.isImplements( "MarkerInterface<String>" ) );
        assertNotNull( _e.getConstant( "A"));
        assertNotNull( _e.getConstant( "A").getJavadoc());
        
        _constant _c = _e.getConstant("B");
        
        assertTrue(_c.hasAnnos());
        assertTrue(_c.hasJavadoc());
        assertTrue(_c.hasFields());
        assertTrue(_c.hasArguments());
        assertEquals( 2, _c.listArguments().size());
        assertEquals( Ex.intLiteralEx( "1"), _c.getArgument(0));
        assertEquals( Ex.stringLiteralEx("String"), _c.getArgument(1));
        assertTrue( _c.getAnnos().is("@ann", "@ann2(k='o')"));
        _field _f = _c.getField( "num");
        assertNotNull( _f);
        assertTrue( _f.getModifiers().is( "public static final"));
        assertEquals( Ex.of(12233), _f.getInit() );
        
        _method _m = _c.getMethod("getNum");
        assertTrue( _m.hasJavadoc() );
        _m.getModifiers().is( "public final");
        assertTrue( _m.isType( int.class));
        assertTrue( _m.getBody().is("return 12345;") );
        
        _constant _cc = _e.getConstant("C");
        assertEquals(2, _cc.listArguments().size() );
        assertEquals(Ex.of(2), _cc.getArgument(0));
        assertEquals(Ex.stringLiteralEx("Blah"), _cc.getArgument(1));
        
        assertTrue( _e.hasInitBlocks() );
        assertTrue( _e.getInitBlock(0).is("System.out.println(12231);"));
        
        assertTrue( _e.hasConstructors());
        _constructor _ct = _e.getConstructor( 0 );
        
        assertFalse(_ct.hasParameters());
        System.out.println( "CONSTRUCTOR "+ _ct );
        assertTrue(_ct.isPrivate());
        
        assertTrue( _ct.getModifiers().is( "private"));
        assertTrue(_ct.getBody().isImplemented());
        assertTrue(_ct.getBody().isEmpty() );
        
        _ct = _e.getConstructor( 1 );
        assertTrue( _ct.hasAnnos() );
        System.out.println( _ct.getAnnos() );
        assertTrue( _ct.getAnnos().is("@ann","@ann2(k='y')"));
        assertTrue( _ct.isPrivate());
        assertTrue( _ct.getParameter( 0 ).is( "@ann @ann2(k='l',v=6) int i"));
        assertTrue( _ct.getParameter( 1 ).is( "String...s"));
        
        
        assertTrue( _e.hasMethods());
        _m = _e.getMethod( "AMethod");
        assertTrue( _m.getModifiers().is("public static"));
        assertTrue( _m.isVoid() );
        assertTrue( _m.isVarArg() );
        assertTrue( _m.getParameter( 0 ).is("String...vals"));
        assertTrue( _m.getBody().is( "System.out.println(23123);"));
    }
    
     public void testConstructor(){
        _enum _e = _enum.of("E").field("int i;");

        _e.constructor(new Object(){
            int i;

            public void changeThisToEnumName(int i){
                this.i = i;
            }
        });
        //verify the enum
        //assertTrue(_e.getConstructor(0)  );
        assertEquals("E", _e.getConstructor(0).getName() );
        assertTrue(_e.getConstructor(0).is("E(int i){ this.i = i; }"));
        //_javac.of( _e);
    }
     
}
