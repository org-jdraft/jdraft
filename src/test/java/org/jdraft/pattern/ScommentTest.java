package org.jdraft.pattern;

import org.jdraft.Ast;
import junit.framework.TestCase;
import org.jdraft.Stencil;
import org.jdraft.Tokens;
import org.jdraft._class;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Eric
 */
public class ScommentTest extends TestCase {


    public void testMatchLineComment(){
        assertTrue($comment.of("//TODO").matches("//TODO"));
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
