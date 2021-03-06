package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro._static;

import java.util.Map;

public class SinterfaceTest extends TestCase {

    /*
    public void test$Not(){
        $interface $i = $interface.of( new Object(){
            @_$not @_static int i = 102;
        });
        $field $f = $field.of("static int i=102;");
        System.out.println( $f );
        assertTrue( $f.matches("static int i=102;"));

        System.out.println( $i );
        assertFalse( $i.matches("public interface I{ static int i = 102; }"));

    }
    */

    interface I{
        @_static int i=0;
    }
    public void testAnony(){
        $interface $i = $interface.of("interface I { static int i=0; }"); //I.class); //$interface.of(new Object(){ });
        assertEquals( 1, $i.fields.size());

        /*
        //this should now EXTEND serializable
        $i = $interface.of("aaaa.bbbb.I<String>", new Serializable(){
            public @_static int m(){
                return 56;
            }
        });
        System.out.println( $i );
        //make sure the macros are run
        assertTrue($i.methods.get(0).modifiers.matches("public static"));
        assertTrue($i.packageDecl.matches("aaaa.bbbb"));
        assertTrue($i.name.matches("I"));
        assertTrue($i.typeParameters.matches("<String>"));
*/
        //use a pre Anonymous macro
        $i = $interface.of("import java.uti.UUID;",
        "public interface AAA{}");

        assertEquals( 1, $i.imports.size() );
    }

    public void testMatchAny(){
        $interface $i = $interface.of();
        assertTrue( $i.isMatchAny());

        //its matches ANY Class
        assertTrue($i.match(Ast.typeDeclaration("public interface A{}") ));
        assertTrue($i.match(Ast.of("public interface A{}") ));
        assertTrue($i.match(_interface.of("A") ));

        $interface.Select sel = $i.select(_interface.of("aaaa.bbbb.C"));
        assertNotNull(sel);
        assertNotNull(sel.tokens);
        System.out.println( sel.tokens );
        assertTrue(sel.is("interfaceName", "C"));
        assertTrue(sel.is("packageName", "aaaa.bbbb"));

        sel = $i.select(_interface.of("C"));
        System.out.println( sel.tokens );
        assertTrue(sel.is("interfaceName", "C"));


        //assertTrue( sel.tokens.isEmpty());
    }


    //check that individual matches work (match and exclude appropriately based on a property)
    public void testMatchOne(){
        _interface _notMatch = _interface.of("NotMatch");
        assertTrue( $interface.of($package.of("aaaa.bbbb")).match(_interface.of("aaaa.bbbb.C")) );
        assertFalse( $interface.of($package.of("aaaa.bbbb")).match(_notMatch) );

        assertTrue($interface.of($name.of("A")).matches(_interface.of("A")));
        assertFalse($interface.of($name.of("A")).matches(_notMatch));

        assertTrue( $interface.of($import.of(Map.class)).matches(_interface.of("A").addImports(Map.class)) );
        assertFalse( $interface.of($import.of(Map.class)).matches(_notMatch) );

        assertTrue($interface.of($.PRIVATE).matches(_interface.of("private interface PRIVATE{}")));
        assertFalse($interface.of($.PRIVATE).matches(_notMatch));

        assertTrue($interface.of($.typeParameter("$R$ extends Map")).matches(_interface.of("class B <Y extends Map>{}")));
        assertFalse($interface.of($.typeParameter("$R$ extends Map")).matches(_interface.of("class B{}")));

        //consistency above
        assertTrue( $interface.of($.method(m->m.isStatic())).matches(_interface.of("C").addMethod("static void m(){}")));
        assertFalse( $interface.of($.method(m->m.isStatic())).matches(_interface.of("C").addMethod("void m(){}")));


        assertTrue($interface.of($field.of(f->f.isType(int.class))).matches(_interface.of("C").addField("int i=100;")));
        assertFalse($interface.of($field.of(f->f.isType(int.class))).matches(_interface.of("C").addField("String s;")));

        assertTrue( $interface.of().$extend(Map.class).matches(_interface.of("AnyClass").addExtend(Map.class)));
        assertFalse( $interface.of().$extend(Map.class).matches(_interface.of("AnyClass")));

        assertTrue( $interface.of( $import.of(Map.class)).matches(_interface.of("AnyClass").addImports(Map.class)));
        assertFalse( $interface.of( $import.of(Map.class)).matches(_interface.of("AnyClass")));

        _interface _i = _interface.of("C").setJavadoc("TODO: fix something");
        $interface $i = $interface.of($comment.javadocComment(c->c.getContent().contains("TODO")));
        assertTrue($i.matches(_i));
        assertFalse($i.matches(_interface.of("C") ));
        assertFalse($i.matches(_interface.of("C").setJavadoc("not compilant")));

        $i = $interface.of($.anno(Deprecated.class));
        assertTrue( $i.matches(_interface.of("F").addAnnos(Deprecated.class)));
        assertFalse( $i.matches(_interface.of("F")));

        $i = $interface.of($.name("AC$afterPrefix$"));
        assertTrue( $i.matches(_interface.of("AC") )); //exact match
        assertTrue( $i.matches(_interface.of("ACExtra") )); //extra
        assertFalse( $i.matches(_interface.of("CAF"))); //mismatch

        //hmm $$.expr, $$.stmt, $$.catchClause $$.comment $$.anno $$.throwStmt
        //$.import $.field $.package, $.javadoc $.modifiers $.anno() $.thrown() $.parameter()
    }

    public void testF(){
        $comment $co = $comment.javadocComment(j-> j.getContent().contains("TODO"));
        assertTrue($co.matches(_field.of("/**TODO this thing*/int i=100;") ));

        _interface _i = _interface.of("C").setJavadoc("TODO: fix something");
        //System.out.println( _c.ast().getComment().get() );
        assertTrue($comment.javadocComment(c->c.getContent().contains("TODO")).matches(_i) );

        assertTrue( $comment.javadocComment().$and(c->c.getContent().contains("TODO")).matches(_i) );

        $interface $c = $interface.of().$javadoc(c->c.getContent().contains("TODO"));
        //System.out.println("BEFORE MATCH "+$c.javadoc.commentClasses );

        assertTrue($c.matches(_i) );

        assertFalse($interface.of().$javadoc( c->c.getContent().contains("TODO"))
                .matches(_class.of("C").setJavadoc("no to d: fix something")));
    }

}
