package org.jdraft.proto;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import junit.framework.TestCase;
import org.jdraft.*;

import java.io.Serializable;
import java.util.Map;

public class SenumTest extends TestCase {

    public void testMatchAny(){
        $enum $e = $enum.of();
        assertTrue( $e.isMatchAny());

        //its matches ANY Class
        assertTrue($e.match(Ast.enumDecl("public enum A{}") ));
        assertTrue($e.match(Ast.of("public enum A{}") ));
        assertTrue($e.match(_enum.of("A") ));

        $enum.Select sel = $e.select(_enum.of("aaaa.bbbb.C"));
        assertNotNull(sel);
        assertNotNull(sel.tokens);
        System.out.println( sel.tokens );
        assertTrue(sel.is("enumName", "C"));
        assertTrue(sel.is("packageName", "aaaa.bbbb"));

        sel = $e.select(_enum.of("C"));
        System.out.println( sel.tokens );
        assertTrue(sel.is("enumName", "C"));
        //assertTrue( sel.tokens.isEmpty());
    }


    //check that individual matches work (match and exclude appropriately based on a property)
    public void testMatchOne(){
        _enum _notMatch = _enum.of("NotMatch");
        assertTrue( $enum.of($package.of("aaaa.bbbb")).match(_enum.of("aaaa.bbbb.C")) );
        assertFalse( $enum.of($package.of("aaaa.bbbb")).match(_notMatch) );

        assertTrue($enum.of($name.of("A")).matches(_enum.of("A")));
        assertFalse($enum.of($name.of("A")).matches(_notMatch));

        assertTrue( $enum.of($import.of(Map.class)).matches(_enum.of("A").imports(Map.class)) );
        assertFalse( $enum.of($import.of(Map.class)).matches(_notMatch) );

        assertTrue($enum.of($.PRIVATE).matches(_enum.of("private enum PRIVATE{}")));
        assertFalse($enum.of($.PRIVATE).matches(_notMatch));

        //consistency above
        assertTrue( $enum.of($.method(m->m.isStatic())).matches(_enum.of("C").method("static void m(){}")));
        assertFalse( $enum.of($.method(m->m.isStatic())).matches(_enum.of("C").method("void m(){}")));

        assertTrue( $enum.of($.constructor(c->c.listStatements().size() >0)).matches(
                _enum.of("C").constructor("{System.out.println(1);}")) );

        assertFalse( $enum.of($.constructor(c->c.listStatements().size() >0)).matches(
                _enum.of("C").constructor("{}")) );

        assertTrue($enum.of($field.of(f->f.isStatic())).matches(_enum.of("C").field("static int i=100;")));
        assertFalse($enum.of($field.of(f->f.isStatic())).matches(_enum.of("C").field("int i=100;")));

        assertTrue( $enum.of( $initBlock.of( (_initBlock i)-> i.isStatic())).matches( _enum.of("C").staticBlock("System.out.println(1);") ) );
        assertFalse($enum.of( $initBlock.of( (_initBlock i)-> i.isStatic())).matches( _enum.of("C").initBlock("System.out.println(1);") ) );

        assertTrue( $enum.of().$implement(Serializable.class).matches(_enum.of("AnyClass").implement(Serializable.class)));
        assertFalse( $enum.of().$implement(Serializable.class).matches(_enum.of("AnyClass")));

        assertTrue( $enum.of( $import.of(Map.class)).matches(_enum.of("AnyClass").imports(Map.class)));
        assertFalse( $enum.of( $import.of(Map.class)).matches(_enum.of("AnyClass")));

        _enum _c = _enum.of("C").javadoc("TODO: fix something");
        $enum $c = $enum.of($comment.javadocComment(c->c.getContent().contains("TODO")));
        assertTrue($c.matches(_c));
        assertFalse($c.matches(_enum.of("C") ));
        assertFalse($c.matches(_enum.of("C").javadoc("not compilant")));

        $c = $enum.of($.anno(Deprecated.class));
        assertTrue( $c.matches(_enum.of("F").anno(Deprecated.class)));
        assertFalse( $c.matches(_enum.of("F")));

        $c = $enum.of($.name("AC$afterPrefix$"));
        assertTrue( $c.matches(_enum.of("AC") )); //exact match
        assertTrue( $c.matches(_enum.of("ACExtra") )); //extra
        assertFalse( $c.matches(_enum.of("CAF"))); //mismatch

        //hmm $$.expr, $$.stmt, $$.catchClause $$.comment $$.anno $$.throwStmt
        //$.import $.field $.package, $.javadoc $.modifiers $.anno() $.thrown() $.parameter()
    }

    public void testF(){
        $comment $co = $comment.javadocComment(j-> j.getContent().contains("TODO"));
        assertTrue($co.matches(_field.of("/**TODO this thing*/int i=100;") ));

        _enum _c = _enum.of("C").javadoc("TODO: fix something");
        //System.out.println( _c.ast().getComment().get() );
        assertTrue($comment.javadocComment(c->c.getContent().contains("TODO")).matches(_c) );

        assertTrue( $comment.javadocComment().$and(c->c.getContent().contains("TODO")).matches(_c) );

        $enum $c = $enum.of().$javadoc(c->c.getContent().contains("TODO"));
        //System.out.println("BEFORE MATCH "+$c.javadoc.commentClasses );

        assertTrue($c.matches(_c) );

        assertFalse($enum.of().$javadoc( c->c.getContent().contains("TODO"))
                .matches(_class.of("C").javadoc("no to d: fix something")));
    }



    public void testHasDescendant(){
        _enum _c = _enum.of("V", new Object(){
           public synchronized int g(){
               return 102;
           }
        });
        //verify I can find the synchronized modifier
        assertEquals(1, $.SYNCHRONIZED.count(_c));

        //verify I can match a class that HAS a Synchronized modifier beneath
        assertTrue( $enum.of().$hasDescendant($.SYNCHRONIZED).matches(_c) );

        //verify I can find an instance of a CallableDeclaration that contains a descendant with the synchronized modifier
        assertEquals(1, $.of(CallableDeclaration.class).$hasDescendant($.SYNCHRONIZED).count(_c));

        IntegerLiteralExpr ile = $.of(102).firstIn(_c);
        assertNotNull(ile);

        //verify that I can find a literal expression 102 that has a synchronized ancestor node
        assertEquals(1, $.of(102).$hasAncestor( $.of().$hasDescendant($.SYNCHRONIZED)).count(_c));

        //I mean at this point, I'm just trying to break something
    }
}
