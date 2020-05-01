package org.jdraft.pattern;

import com.github.javaparser.ast.body.TypeDeclaration;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._annotation;
import org.jdraft._field;
import org.jdraft._int;

import java.util.Map;

public class SannotationTest extends TestCase {

    public void testNotAnno(){
        $annotation $a = $annotation.of( new Object(){
            @_$not String name;
            int value;
        });
        assertTrue( $a.annotationElements.size() == 1 );
        assertTrue( $a.annotationElements.get(0).matches("int value();") );
        System.out.println( $a );

    }
    public void testAnonConstructor(){
        $annotation $a = $annotation.of("A", new Object(){
           int value = 100;
        });
        assertTrue( $a.name.matches("A"));
        assertEquals(1, $a.annotationElements.size());
        //System.out.println( $a );

        $a = $annotation.of( new Object(){
           String name;
           int a = 1;
           int[] vals = new int[]{1,2,3};
        });

        assertTrue( $a.name.matches("A"));
        assertEquals(3, $a.annotationElements.size());
        //System.out.println( $a );
    }
    public void testMatchAny(){
        $annotation $c = $annotation.of();
        assertTrue( $c.isMatchAny());

        //its matches ANY Class
        assertTrue($c.match(Ast.typeDecl( "public @interface A{}") ));
        assertTrue($c.match(Ast.of("public @interface A{}") ));
        assertTrue($c.match(_annotation.of("A") ));
        $annotation.Select sel = $c.select(_annotation.of("aaaa.bbbb.C"));
        assertNotNull(sel);
        assertNotNull(sel.tokens);
        System.out.println( sel.tokens );
        assertTrue(sel.is("annotationName", "C"));
        assertTrue(sel.is("packageName", "aaaa.bbbb"));

        sel = $c.select(_annotation.of("C"));
        System.out.println( sel.tokens );
        assertTrue(sel.is("annotationName", "C"));

        //assertTrue( sel.tokens.isEmpty());
    }


    //check that individual matches work (match and exclude appropriately based on a property)
    public void testMatchOne(){
        _annotation _notMatch = _annotation.of("NotMatch");
        assertTrue( $annotation.of($package.of("aaaa.bbbb")).match(_annotation.of("aaaa.bbbb.C")) );
        assertFalse( $annotation.of($package.of("aaaa.bbbb")).match(_notMatch) );

        assertTrue($annotation.of($name.of("A")).matches(_annotation.of("A")));
        assertFalse($annotation.of($name.of("A")).matches(_notMatch));

        assertTrue( $annotation.of($import.of(Map.class)).matches(_annotation.of("A").addImports(Map.class)) );
        assertFalse( $annotation.of($import.of(Map.class)).matches(_notMatch) );

        assertTrue($annotation.of($.PRIVATE).matches(_annotation.of("private @interface PRIVATE{}")));
        assertFalse($annotation.of($.PRIVATE).matches(_notMatch));


        assertTrue($annotation.of($field.of(f->f.hasInit())).matches(_annotation.of("C").addField("static int i=100;")));
        assertFalse($annotation.of($field.of(f->f.isInit())).matches(_annotation.of("C").addField("static int i;")));

        assertTrue( $annotation.of( $import.of(Map.class)).matches(_annotation.of("AnyClass").addImports(Map.class)));
        assertFalse( $annotation.of( $import.of(Map.class)).matches(_annotation.of("AnyClass")));

        _annotation _c = _annotation.of("C").setJavadoc("TODO: fix something");
        $annotation $c = $annotation.of($comment.javadocComment(c->c.getContent().contains("TODO")));
        assertTrue($c.matches(_c));
        assertFalse($c.matches(_annotation.of("C") ));
        assertFalse($c.matches(_annotation.of("C").setJavadoc("not compilant")));

        $c = $annotation.of($.anno(Deprecated.class));
        assertTrue( $c.matches(_annotation.of("F").addAnnoRefs(Deprecated.class)));
        assertFalse( $c.matches(_annotation.of("F")));

        $c = $annotation.of($.name("AC$afterPrefix$"));
        assertTrue( $c.matches(_annotation.of("AC") )); //exact match
        assertTrue( $c.matches(_annotation.of("ACExtra") )); //extra
        assertFalse( $c.matches(_annotation.of("CAF"))); //mismatch

        //hmm $$.expr, $$.stmt, $$.catchClause $$.comment $$.anno $$.throwStmt
        //$.import $.field $.package, $.javadoc $.modifiers $.anno() $.thrown() $.parameter()
    }

    public void testF(){
        $comment $co = $comment.javadocComment(j-> j.getContent().contains("TODO"));
        assertTrue($co.matches(_field.of("/**TODO this thing*/int i=100;") ));

        _annotation _c = _annotation.of("C").setJavadoc("TODO: fix something");
        //System.out.println( _c.ast().getComment().get() );
        assertTrue($comment.javadocComment(c->c.getContent().contains("TODO")).matches(_c) );

        assertTrue( $comment.javadocComment().$and(c->c.getContent().contains("TODO")).matches(_c) );

        $annotation $c = $annotation.of().$javadoc(c->c.getContent().contains("TODO"));
        //System.out.println("BEFORE MATCH "+$c.javadoc.commentClasses );

        assertTrue($c.matches(_c) );

        assertFalse($annotation.of().$javadoc( c->c.getContent().contains("TODO"))
                .matches(_annotation.of("C").setJavadoc("no to d: fix something")));
    }


    @interface A{

        /**
         * Javadoc
         * @return
         */
        @Deprecated
        int a();
    }

    public void testAnno(){
        _annotation _a = _annotation.of(A.class);
        System.out.println( _a);
    }

    public void testHasDescendant(){
        _annotation _c = _annotation.of("V", new Object(){
            /**
             * I should ensure the javadoc ports over
             * this will be converted into int value() default 100;
             */
            @Deprecated
            public int value = 100;

            //this will be saved as a field
            public static final int ID = 200;
        });
        System.out.println( _c );


        //verify I can find the static modifier (value has a static IMPLIED MODIFIER)
        assertEquals(2, $.STATIC.countIn(_c));

        //verify I can match a class that HAS a Synchronized modifier beneath
        assertTrue( $annotation.of().$hasDescendant($.field($.STATIC)).matches(_c) );

        //verify I can find an instance of a CallableDeclaration that contains a descendant with the synchronized modifier
        assertEquals(1, $.of(TypeDeclaration.class).countIn(_c));
        assertEquals(1, $.of(TypeDeclaration.class).$hasDescendant($.field($.STATIC)).countIn(_c));

        _int ile = $.of(200).firstIn(_c);
        assertNotNull(ile);

        //verify that I can find a literal expression 102 that has a synchronized ancestor node
        assertEquals(1, $.of(200).$hasAncestor( $.of().$hasDescendant($.STATIC)).countIn(_c));

        //I mean at this point, I'm just trying to break something
    }
}
