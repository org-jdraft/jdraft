package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.Ast;
import org.jdraft._anno._hasAnnos;
import org.jdraft._class;
import org.jdraft._type;
import org.jdraft.proto.$stmt;
import junit.framework.TestCase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Things that should help with composition
 *
 * One way
 * Dependency injection, Reflection, Runtime Proxies, bytecode generation
 *
 *
 * Another way, generate & compile code
 * More code
 * But less encapsulation
 * Less frameworks
 * smaller binaries
 * easier to debug
 * faster bootstrap
 *
 */
public class _macroTest extends TestCase {

    /*
    public void testManualMac(){

        _class _c = _class.of("C").fields("int x, y, z;");

        //_class _r = $$.to(_c, _autoToString.INSTANCE, _autoConstructor.INSTANCE, _autoEquals.INSTANCE, _autoHashCode.INSTANCE);
        //_c = _class.of("C").FIELDS("int x, y, z;");
        List<macro> lms = new ArrayList<>();
        lms.add(_hashCode.Act)
        _class _s = _hashCode.Macro.to( _equals.Act.to(_autoConstructor.Act.to( _toString.Act.to( _c.ast()) )));

        System.out.println("S" +  _s);

        @_hashCode
        @_equals
        @_autoConstructor
        @_toString
        class C{
            int x,y,z;
        }
        _class _v = _class.of(C.class);
        System.out.println( _v);
        assertEquals( _v, _s);
    }
    */
    /**
     * Code in a tweet:
     *
     * This will generate a new static field ID with a UUID
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface ID{
        class M extends macro<ID, TypeDeclaration> {

            public M( ID id ){
                super(id);
            }

            public void expand(TypeDeclaration t) {
                t.addMember(Ast.field( "public static String ID=\""+ UUID.randomUUID()+"\";"));
            }
        }
    }

    public void testMacro(){
        @ID //Apply the _macro to a class
        class C{}

        _class _c = _class.of(C.class);
        assertTrue( _c.getField("ID").isStatic() );
        System.out.println( _c );
    }

    /**
     * Removes all System.out.println Statements within the code
     * either add to a whole TYPE, a single method or a single constructor
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
    @interface removePrintlns {
        
        $stmt $println = $stmt.of( "System.out.println($any$);" );

        class Macro extends macro<removePrintlns, Node> {
            public Macro( removePrintlns rp ){
                super(rp);
            }
            @Override
            public void expand(Node n) {
                $println.removeIn(n);
            }
        }
    }

    public void testMacroRemovePrintlns(){

        @removePrintlns
        class C{
            void f(){
                int i = 23;
                System.out.println(i);
                System.out.println("Some text "+ 2 + " Some more text");
            }

            void g(){
                System.out.println("method println");
            }
            C(){
                System.out.println( "constructor println");
            }
        }

        _class _c = _class.of( C.class);
        _c.anno(removePrintlns.class);

        //verify that AFTER I run the _macro, there are no matching $printlns left
        //assertEquals(0, removePrintlns.$println.selectAllIn(_c).size());
        // System.out.println( _c );
    }

    //instead of removing System.outs, convert them into comments
    public void testCommentOut() {
        $stmt $from = $stmt.of( "System.out.println( $any$ );" );

        $stmt $to = $stmt.of( "{ /*System.out.println( $any$ );*/ }" );
        class C{
            void f(){
                int i = 23;
                System.out.println(i);
                System.out.println("Some text "+ 2 + " Some more text");
            }

            void g(){
                System.out.println("method println");
            }
            C(){
                System.out.println( "constructor println");
            }
        }
        _class _c = _class.of(C.class);
        $from.replaceIn(_c, $to);
        System.out.println( _c );
    }
}
