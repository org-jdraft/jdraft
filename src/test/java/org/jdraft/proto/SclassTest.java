package org.jdraft.proto;

import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import junit.framework.TestCase;
import org.jdraft.*;

import java.io.Serializable;
import java.util.Map;

public class SclassTest extends TestCase {

    public void testMatchAny(){
        $class $c = $class.of();
        assertTrue( $c.isMatchAny());

        //its matches ANY Class
        assertTrue($c.match(Ast.classDecl("public class A{}") ));
        assertTrue($c.match(Ast.of("public class A{}") ));
        assertTrue($c.match(_class.of("A") ));
        assertTrue($c.match(_class.of(SclassTest.class) ));
        $class.Select sel = $c.select(_class.of("aaaa.bbbb.C"));
        assertNotNull(sel);
        assertNotNull(sel.tokens);
        System.out.println( sel.tokens );
        assertTrue(sel.is("className", "C"));
        assertTrue(sel.is("packageName", "aaaa.bbbb"));

        sel = $c.select(_class.of("C"));
        System.out.println( sel.tokens );
        assertTrue(sel.is("className", "C"));


        //assertTrue( sel.tokens.isEmpty());
    }


    //check that individual matches work (match and exclude appropriately based on a property)
    public void testMatchOne(){
        _class _notMatch = _class.of("NotMatch");
        assertTrue( $class.of($package.of("aaaa.bbbb")).match(_class.of("aaaa.bbbb.C")) );
        assertFalse( $class.of($package.of("aaaa.bbbb")).match(_notMatch) );

        assertTrue($class.of($name.of("A")).matches(_class.of("A")));
        assertFalse($class.of($name.of("A")).matches(_notMatch));

        assertTrue( $class.of($import.of(Map.class)).matches(_class.of("A").imports(Map.class)) );
        assertFalse( $class.of($import.of(Map.class)).matches(_notMatch) );

        assertTrue($class.of($.PRIVATE).matches(_class.of("private class PRIVATE{}")));
        assertFalse($class.of($.PRIVATE).matches(_notMatch));

        assertTrue($class.of($.typeParameter("$R$ extends Map")).matches(_class.of("class B <Y extends Map>{}")));
        assertFalse($class.of($.typeParameter("$R$ extends Map")).matches(_class.of("class B{}")));

        //consistency above
        assertTrue( $class.of($.method(m->m.isStatic())).matches(_class.of("C").method("static void m(){}")));
        assertFalse( $class.of($.method(m->m.isStatic())).matches(_class.of("C").method("void m(){}")));

        assertTrue( $class.of($.constructor(c->c.listStatements().size() >0)).matches(
                _class.of("C").constructor("{System.out.println(1);}")) );

        assertFalse( $class.of($.constructor(c->c.listStatements().size() >0)).matches(
                _class.of("C").constructor("{}")) );

        assertTrue($class.of($field.of(f->f.isStatic())).matches(_class.of("C").field("static int i=100;")));
        assertFalse($class.of($field.of(f->f.isStatic())).matches(_class.of("C").field("int i=100;")));

        assertTrue( $class.of( $initBlock.of( (_initBlock i)-> i.isStatic())).matches( _class.of("C").staticBlock("System.out.println(1);") ) );
        assertFalse($class.of( $initBlock.of( (_initBlock i)-> i.isStatic())).matches( _class.of("C").initBlock("System.out.println(1);") ) );

        assertTrue( $class.of().$extend(Map.class).matches(_class.of("AnyClass").extend(Map.class)));
        assertFalse( $class.of().$extend(Map.class).matches(_class.of("AnyClass")));

        assertTrue( $class.of().$implement(Serializable.class).matches(_class.of("AnyClass").implement(Serializable.class)));
        assertFalse( $class.of().$implement(Serializable.class).matches(_class.of("AnyClass")));

        assertTrue( $class.of( $import.of(Map.class)).matches(_class.of("AnyClass").imports(Map.class)));
        assertFalse( $class.of( $import.of(Map.class)).matches(_class.of("AnyClass")));

        _class _c = _class.of("C").javadoc("TODO: fix something");
        $class $c = $class.of($comment.javadocComment(c->c.getContent().contains("TODO")));
        assertTrue($c.matches(_c));
        assertFalse($c.matches(_class.of("C") ));
        assertFalse($c.matches(_class.of("C").javadoc("not compilant")));

        $c = $class.of($.anno(Deprecated.class));
        assertTrue( $c.matches(_class.of("F").anno(Deprecated.class)));
        assertFalse( $c.matches(_class.of("F")));

        $c = $class.of($.name("AC$afterPrefix$"));
        assertTrue( $c.matches(_class.of("AC") )); //exact match
        assertTrue( $c.matches(_class.of("ACExtra") )); //extra
        assertFalse( $c.matches(_class.of("CAF"))); //mismatch

        //hmm $$.expr, $$.stmt, $$.catchClause $$.comment $$.anno $$.throwStmt
        //$.import $.field $.package, $.javadoc $.modifiers $.anno() $.thrown() $.parameter()
    }

    public void testF(){
        $comment $co = $comment.javadocComment(j-> j.getContent().contains("TODO"));
        assertTrue($co.matches(_field.of("/**TODO this thing*/int i=100;") ));

        _class _c = _class.of("C").javadoc("TODO: fix something");
        //System.out.println( _c.ast().getComment().get() );
        assertTrue($comment.javadocComment(c->c.getContent().contains("TODO")).matches(_c) );

        assertTrue( $comment.javadocComment().$and(c->c.getContent().contains("TODO")).matches(_c) );

        $class $c = $class.of().$javadoc(c->c.getContent().contains("TODO"));
        //System.out.println("BEFORE MATCH "+$c.javadoc.commentClasses );

        assertTrue($c.matches(_c) );

        assertFalse($class.of().$javadoc( c->c.getContent().contains("TODO"))
                .matches(_class.of("C").javadoc("no to d: fix something")));
    }


    /**
     * verify I can use a $class to ...
     */
    public void testLocalClass(){
        _class _c = _class.of("aaaa.HHH", new Object(){
                    void m(){
                        class G{}// (a local class)
                    }
            });

        //ensure I can use a $class to match against a local Class (here a class named "G")
        assertEquals(1, $class.of( $.name("G") ).count(_c));

        //find a nested type named "G" that has a parent named "HHH"
        assertEquals(1, $class.of( $.name("G") ).$hasAncestor($class.of($.name("HHH"))).count(_c));
    }

    public void testHasDescendant(){
        _class _c = _class.of("V", new Object(){
           public synchronized int g(){
               return 102;
           }
        });
        //verify I can find the synchronized modifier
        assertEquals(1, $.SYNCHRONIZED.count(_c));

        //verify I can match a class that HAS a Synchronized modifier beneath
        assertTrue( $class.of().$hasDescendant($.SYNCHRONIZED).matches(_c) );

        //verify I can find an instance of a CallableDeclaration that contains a descendant with the synchronized modifier
        assertEquals(1, $.of(CallableDeclaration.class).$hasDescendant($.SYNCHRONIZED).count(_c));

        IntegerLiteralExpr ile = $.of(102).firstIn(_c);
        assertNotNull(ile);

        //verify that I can find a literal expression 102 that has a synchronized ancestor node
        assertEquals(1, $.of(102).$hasAncestor( $.of().$hasDescendant($.SYNCHRONIZED)).count(_c));

        //I mean at this point, I'm just trying to break something
    }
}
