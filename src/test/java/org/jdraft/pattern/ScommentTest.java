package org.jdraft.pattern;

import org.jdraft.Ast;
import junit.framework.TestCase;
import org.jdraft._class;

/**
 *
 * @author Eric
 */
public class ScommentTest extends TestCase {

    public void testFindAndReplace(){
        /**
         * javadoc aeiou
         */
        class Local{
            /* aeiou */
            public int f;
            public void m(){
                //line comment aeiou

                //line comment aeiou attached
                assert(1==1);
            }
        }

        _class _c = $comment.of().findAndReplace(Local.class,"aeiou", "i before e accept after c");

        //verify we made (4) changes above
        assertEquals(4, $comment.of("i before e accept after c").count(_c) );
    }

    public void testStmtComment(){

        $comment STATEMENT_COMMENT = $comment.as("<code>$statement$</code>");
        //exact matches
        assertTrue(STATEMENT_COMMENT.matches("//<code>assert(1==1)</code>")); //line comment
        assertTrue(STATEMENT_COMMENT.matches("/*<code>assert(1==1)</code>*/")); /*block comment */
        assertTrue(STATEMENT_COMMENT.matches("/**<code>assert(1==1)</code>*/")); /** javadoc comment */

        assertTrue(STATEMENT_COMMENT.matches("//<code>/*comment*/ assert(1==1)</code>")); //comment with internal comment

        assertTrue(STATEMENT_COMMENT.matches("//  <code>/*comment*/ assert(1==1)</code>  ")); //comment with extra (leading and trailing spaces)

        //comment across many lines
        assertTrue(STATEMENT_COMMENT.matches("/*  <code>",
                "if(i==1){",
                "    System.out.println(1);",
                "}",
                "</code>  */")); //comment with extra (leading and trailing spaces)

        //javadoc comment with multiple leading "*"s across many lines
        assertTrue(STATEMENT_COMMENT.matches("/**  <code>",
                " * if(i==1){",
                " *     System.out.println(1);",
                " * }",
                " * </code>  */")); //comment with extra (leading and trailing spaces)
    }

    public void testCommentAnd(){
        $comment $todo = $comment.or( $comment.of("TODO"), $comment.of("FIXME") );
        class F{
            /** Javadoc TODO */
            int i = 100;

            public void m(){
                /* block FIXME */
            }
        }
        assertEquals( 2, $todo.count(F.class));

        $comment $c = $comment.of("TODO");
        assertTrue($c.matches( "// TODO FIXME"));

        $c.$and("FIXME");
        assertFalse($c.matches( "// TODO")); //needs FIXME
        assertFalse($c.matches( "// FIXME")); //needs TODO
        assertTrue($c.matches( "// TODO FIXME")); //has BOTH

        $c = $comment.of("TODO");
        $c.$not("FIXME");
        assertTrue($c.matches( "// TODO"));
        assertFalse($c.matches( "// FIXME")); //needs TODO
        assertFalse($c.matches( "// TODO FIXME")); //has FIXME


    }

    public void testMatchOfComments(){

        //without providing a comment type... this will match
        assertTrue($comment.of("TODO").matches("//TODO"));
        assertTrue($comment.of("TODO").matches("/*TODO*/"));
        assertTrue($comment.of("TODO").matches("/**TODO*/"));

        assertTrue($comment.of("TODO").matches("//before TODO after "));
        assertTrue($comment.of("TODO").matches("/*before TODO after */"));
        assertTrue($comment.of("TODO").matches("/**before TODO after */"));

        assertTrue($comment.of("//TODO").matches("//TODO"));
        assertTrue($comment.of("//TODO").matches("//stuff before TODO stuff after"));
        assertTrue($comment.of("/*TODO*/").matches("/*TODO*/"));
        assertTrue($comment.of("/*TODO*/").matches("/*stuffBeofre TODOStuffAfter*/"));
        assertTrue($comment.of("/**TODO*/").matches("/**TODO*/"));
        assertTrue($comment.of("/**TODO*/").matches("/**Stuff before TODO --- stuff after*/"));
    }

    public void testMatchAsComments(){

        //without providing a comment type... this will match
        assertTrue($comment.as("TODO").matches("//TODO"));
        assertTrue($comment.as("TODO").matches("/*TODO*/"));
        assertTrue($comment.as("TODO").matches("/**TODO*/"));

        assertFalse($comment.as("TODO").matches("//before TODO after "));
        assertFalse($comment.as("TODO").matches("/*before TODO after */"));
        assertFalse($comment.as("TODO").matches("/**before TODO after */"));

        assertTrue($comment.as("//TODO").matches("//TODO"));
        assertFalse($comment.as("//TODO").matches("//stuff before TODO stuff after"));
        assertTrue($comment.as("/*TODO*/").matches("/*TODO*/"));
        assertFalse($comment.as("/*TODO*/").matches("/*stuffBeofre TODOStuffAfter*/"));
        assertTrue($comment.as("/**TODO*/").matches("/**TODO*/"));
        assertFalse($comment.as("/**TODO*/").matches("/**Stuff before TODO --- stuff after*/"));
    }

    public void testMatchOf(){
        /** TODO something */
        class C{
            /*TODO something else */
            public int i=0;

            void m(){
                // TODO fix this
            }
        }
        _class _c = _class.of(C.class);
        System.out.println( _c );

        assertEquals(3, $comment.of("TODO").count(C.class));
        assertEquals(1, $comment.of("/** TODO */").count(C.class));
        assertEquals(1, $comment.of("/* TODO */").count(C.class));
        assertEquals(1, $comment.of("// TODO").count(C.class));
    }
    public void testCompose(){
        assertEquals( Ast.lineComment("//Hello ").getContent().trim(), 
                $comment.of("//Hello").draft().getContent().trim());
        //assertEquals( Ast.blockComment("/* Hello */"), $comment.of("/* Hello */").construct());
    }
    
    public void testConstruct(){
        assertNull($comment.javadocComment().draft());
        assertNotNull($comment.javadocComment().draft("javadoc", "Hi"));
        System.out.println($comment.javadocComment().draft("javadoc", "Hi"));
        System.out.println($comment.javadocComment().draft("javadoc", "Hi"+System.lineSeparator()+"There"));
        
        assertNull($comment.lineComment().draft());
        assertNotNull($comment.lineComment().draft("comment", "Hi"));
        
        System.out.println($comment.lineComment().draft("comment", "Hi"));
        System.out.println($comment.lineComment().draft("comment", "Hi"+System.lineSeparator()+"There"));
    }
    
    public void testAnyMatch(){
        assertTrue( $comment.of().matches("//Hello") );
        assertTrue( $comment.of().matches("/*Hello*/") );
        assertTrue( $comment.of().matches("/** Hello*/") );
        
        assertTrue( $comment.lineComment().matches("//Hello") );
        assertFalse( $comment.lineComment().matches("/*Hello*/") );
        assertFalse( $comment.lineComment().matches("/** Hello*/") );
        
        assertFalse( $comment.blockComment().matches("//Hello") );
        assertTrue( $comment.blockComment().matches("/*Hello*/") );
        assertFalse( $comment.blockComment().matches("/** Hello*/") );
        
        assertFalse( $comment.javadocComment().matches("//Hello") );
        assertFalse( $comment.javadocComment().matches("/*Hello*/") );
        assertTrue( $comment.javadocComment().matches("/** Hello*/") );        
    }
    
    public void testFindFirst(){
        /** Javadoc */
        class C{
            /* block comment */
            int i=1200;
            
            void m(){
               // line comment (orphan)
               
               // line comment (assigned)
               assert(1==1);
            }            
        }
        assertEquals(4, $comment.of().count(C.class));
        
        
        //as expected the orphaned comment comes first...
        System.out.println( $comment.of().firstIn(C.class) );
        assertTrue( $comment.of().firstIn(C.class).isJavadocComment());
        
        assertEquals( Ast.lineComment("// line comment (orphan)"), 
            $comment.lineComment().firstIn(C.class));
        
        assertEquals( Ast.blockComment("/* block comment */"), 
            $comment.blockComment().firstIn(C.class));
        
        assertEquals( Ast.lineComment("// line comment (orphan)"), 
            $comment.lineComment(c -> c.isOrphan()).firstIn(C.class)); //first orphaned line comment
        
        assertEquals( Ast.lineComment("// line comment (assigned)"), 
            $comment.lineComment(c -> !c.isOrphan()).firstIn(C.class)); //first un orphaed line comment
        
        assertEquals( Ast.lineComment("// line comment (orphan)"), 
            $comment.of().$and( (c) -> c.isOrphan() )
                    .firstIn(C.class)); //first orphaned comment
    }       
}
