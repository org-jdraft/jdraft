package org.jdraft;

import com.github.javaparser.ast.comments.Comment;
import junit.framework.TestCase;

import java.util.List;
import java.util.stream.Collectors;

public class _commentTest extends TestCase {

    public void testStencil(){
        /** Attributed Javadoc 1 */
        class C{
            // line comment 2
            int i;

            /* block comment 3*/
            int j;
        }

        _class _c = _class.of(C.class);
        assertTrue(_c.listAllComments().get(0).contains("Attributed"));
        assertTrue(_c.listAllComments().get(1).contains("line"));
        assertTrue(_c.listAllComments().get(2).contains("block"));


        assertEquals( "Javadoc 1", _c.listAllComments().get(0).parseFirst("Attributed $a$").get("a"));
        assertEquals( "Javadoc", _c.listAllComments().get(0).parseFirst("Attributed $a$ 1").get("a"));

        assertEquals( "comment 2", _c.listAllComments().get(1).parseFirst("line $a$").get("a"));
        assertEquals( "comment", _c.listAllComments().get(1).parseFirst("line $a$ 2").get("a"));

        assertEquals( "comment 3", _c.listAllComments().get(2).parseFirst("block $a$").get("a"));
        assertEquals( "comment", _c.listAllComments().get(2).parseFirst("block $a$ 3").get("a"));
    }

    public void testUpdateJavadocComments(){
        /** comment 1*/
        class C{
            /** comment 2*/
            public int i;

            void m(){
                if(true){
                    /** comment here */
                }
            }
        }

        _class _c = _class.of(C.class);
        List<_javadocComment> _jcs = _c.listAllJavadocComments();
        System.out.println( _jcs );
    }

    public void testUpdateBlockComments(){
        /* comment 1*/
        class C{
            /* comment 2*/
            public int i;

            void m(){
                if(true){
                    /* comment here */
                }
            }
        }

        _class _c = _class.of(C.class);
        System.out.println( _c );


        //we might need to do this if we remove a local or member class to be a _class
        //_c = _class.of(Ast.reparse( _c.astCompilationUnit() ));

        System.out.println( _c.listAllBlockComments() );
        _c.listAllBlockComments().get(0).setText("one liner");
        _c.listAllBlockComments().get(1).setText("multi", "line");
        _c.listAllBlockComments().get(2).setText("multi", "line", "comment");
        //System.out.println( _c );

        assertEquals("one liner", _c.listAllBlockComments().get(0).getText()); //we normalize the content before we pass it back (trim etc.)
        assertEquals("multi"+System.lineSeparator()+"line", _c.listAllBlockComments().get(1).getText());
        assertEquals("multi"+System.lineSeparator()+"line"+System.lineSeparator()+"comment", _c.listAllBlockComments().get(2).getText());

        _c.listAllBlockComments().get(0).setText(_blockComment.FIRST_LINE_STYLE,"one liner");
        _c.listAllBlockComments().get(1).setText(_blockComment.FIRST_LINE_STYLE, "multi", "line");
        _c.listAllBlockComments().get(2).setText(_blockComment.FIRST_LINE_STYLE,"multi", "line", "comment");
        //System.out.println( _c );

        assertEquals("one liner", _c.listAllBlockComments().get(0).getText()); //we normalize the content before we pass it back (trim etc.)
        assertEquals("multi"+System.lineSeparator()+"line", _c.listAllBlockComments().get(1).getText());
        assertEquals("multi"+System.lineSeparator()+"line"+System.lineSeparator()+"comment", _c.listAllBlockComments().get(2).getText());

        _c.listAllBlockComments().get(0).setText(_blockComment.OPEN_STYLE,"one liner");
        _c.listAllBlockComments().get(1).setText(_blockComment.OPEN_STYLE, "multi", "line");
        _c.listAllBlockComments().get(2).setText(_blockComment.OPEN_STYLE,"multi", "line", "comment");
        //System.out.println( _c );

        assertEquals("one liner", _c.listAllBlockComments().get(0).getText()); //we normalize the content before we pass it back (trim etc.)
        assertEquals("multi"+System.lineSeparator()+"line", _c.listAllBlockComments().get(1).getText());
        assertEquals("multi"+System.lineSeparator()+"line"+System.lineSeparator()+"comment", _c.listAllBlockComments().get(2).getText());

        _c.listAllBlockComments().get(0).setText(_blockComment.COMPACT_OPEN_STYLE,"one liner");
        _c.listAllBlockComments().get(1).setText(_blockComment.COMPACT_OPEN_STYLE, "multi", "line");
        _c.listAllBlockComments().get(2).setText(_blockComment.COMPACT_OPEN_STYLE,"multi", "line", "comment");
        System.out.println( _c );

        assertEquals("one liner", _c.listAllBlockComments().get(0).getText()); //we normalize the content before we pass it back (trim etc.)
        assertEquals("multi"+System.lineSeparator()+"line", _c.listAllBlockComments().get(1).getText());
        assertEquals("multi"+System.lineSeparator()+"line"+System.lineSeparator()+"comment", _c.listAllBlockComments().get(2).getText());

        //assertEquals(" one liner ", _c.listAllBlockComments().get(0).ast().getContent()); //the ast content
        //assertEquals("one liner", _c.listAllBlockComments().get(0).getContents()); //we normalize the content before we pass it back (trim etc.)

        _c.listAllBlockComments().get(0).setText(_blockComment.ULTRA_COMPACT_STYLE, "one liner");
        _c.listAllBlockComments().get(1).setText(_blockComment.ULTRA_COMPACT_STYLE, "multi", "line");
        _c.listAllBlockComments().get(2).setText(_blockComment.ULTRA_COMPACT_STYLE,"multi", "line", "comment");

        assertEquals("one liner", _c.listAllBlockComments().get(0).getText()); //we normalize the content before we pass it back (trim etc.)
        assertEquals("multi"+System.lineSeparator()+"line", _c.listAllBlockComments().get(1).getText());
        assertEquals("multi"+System.lineSeparator()+"line"+System.lineSeparator()+"comment", _c.listAllBlockComments().get(2).getText());
        //System.out.println( _c );

        //TODO add checks for padding
        assertEquals("one liner", _c.listAllBlockComments().get(0).getText());
    }

    public void testStandardStyleBlockComment(){
        assertEquals( " single line ", _comment.formatContents(_blockComment.STANDARD_STYLE, "single line"));
        assertEquals( " single line ", _comment.formatContents(_blockComment.STANDARD_STYLE, "/*single line*/"));


        //we dont indent the first line
        assertEquals( " single line ", _comment.formatContents(_blockComment.STANDARD_STYLE, "    ", "single line"));
        assertEquals( " single line ", _comment.formatContents(_blockComment.STANDARD_STYLE, "    ", "/*single line*/"));

        String comm = _comment.formatContents(_blockComment.STANDARD_STYLE, "first line"+System.lineSeparator()+"second line");

        //System.out.println( new BlockComment( comm) );
        assertEquals(" "+ System.lineSeparator()+" * first line"+System.lineSeparator()+" * second line"+System.lineSeparator()+" ",
                comm);

        //the IDEA is that we want to be able to add a multi-line block comment that tries to abide by the format
    }

    public void testGetSetContents(){
        class G{

        }

        _class _c = _class.of(G.class);
        _c.ast().setComment(_blockComment.of("blockus"+System.lineSeparator()+"multis"+System.lineSeparator()+"commentus").ast() );
        System.out.println( _c );
    }

    public void testListComments(){

        /** Top level comment */
        class G{
            //orphaned

            /** Field */
            int i=0;
        }

        _class _c = _class.of( G.class);
        List<_comment> _cs = _c.listAllComments();
        assertEquals( 3, _cs.size());

        assertEquals( 0, _c.listAllComments(c-> c instanceof _blockComment).size());
        assertEquals( 0, _c.listAllBlockComments().size());

        assertEquals( 1, _c.listAllComments(c-> c instanceof _lineComment).size());

        _cs = _c.listAllComments(c-> c instanceof _javadocComment);
        assertEquals( 2, _cs.size());

        _c.forAllComments(c -> System.out.println( c ));
        _c.forAllComments(c-> c.isAttributed(), c-> System.out.println("ATTRIBUTED"+c.getAttributedNode()));

    }

    public void testNormalizeContents(){
        /* BlockComment */
        class V{
            void m() {
                /* block comment
                 * multi line
                 * with content
                 */
            }
            void g(){
                /** javadoc contents
                 *  with stuff
                 *  starting on first line
                 */

                /**
                 * single javadoc with nothing on first line
                 */

                /**
                 * multi line javadoc with
                 * nothing on the first line
                 */
            }
        }
        //System.out.println( Ast.of(V.class) );
        _class _c = _class.of(V.class);
        List<String> ls =
                _c.listAllJavadocComments().stream().map(jd-> jd.getText()).collect(Collectors.toList());

        assertEquals( "javadoc contents"+System.lineSeparator()+"with stuff" + System.lineSeparator()+"starting on first line", ls.get(0));
        assertEquals( "single javadoc with nothing on first line", ls.get(1));
        assertEquals( "multi line javadoc with"+System.lineSeparator()+"nothing on the first line", ls.get(2));
        //_c.listJavadocComments().forEach( c-> System.out.println("NORMAL>"+c.getNormalizedContents()) );

        //System.out.println( _c );

        _blockComment _bc = _c.listAllBlockComments().get(0);
        assertEquals( "BlockComment", _c.listAllBlockComments().get(0).getText() );
        assertEquals( "block comment" +System.lineSeparator()+ "multi line" + System.lineSeparator() +"with content",
                _c.listAllBlockComments().get(1).getText() );
        //String contents = _bc.getContents();
        //System.out.println( "CONTENTS >"+ _c.listBlockComments().get(1).getContents()+"<");
        //System.out.println( "NORMAL CONTENTS >"+ _c.listBlockComments().get(1).getNormalizedContents()+"<");
        //System.out.println( ">"+_c.listBlockComments().get(1).getContents()+"<");


        //_c.listBlockComments().get(1).ast().setContent("A"); //first line\nsecond line\n third line\n");

        //System.out.println( _c );


    }

    public void testLineComment(){
        // line comment 1
        class LineComment{
            //line comment 2
            int i=0;

            // line comment 3
            LineComment(){ }

            // line comment 4
            void v(){}
        }
        _class _c = _class.of(LineComment.class);
        List<Comment> cs = _c.astCompilationUnit().getComments();
        //attribution
        for(int i=0;i<cs.size();i++){
            assertTrue(cs.get(i).getCommentedNode().isPresent() );
        }
        //System.out.println( cs );

        class MultiLineComment{
            //multi
            //line
            //comment
            int i=0;
        }

        _c = _class.of(MultiLineComment.class);
         //_c.listAllComments(); //listAllContainedComments()
        //_c.listAllComments( _comment.class, matchFn);
        //_c.listAllComments( matchFn );

        //_c.getJavadoc(); //returns the Javadoc comment or null if no javadoc comment
        //_c.getComment();
        //_c.hasComment();

        //_c.listContainedComments();
        cs = _c.astCompilationUnit().getComments();
        assertFalse( cs.get(0).getCommentedNode().isPresent() );
        assertFalse( cs.get(1).getCommentedNode().isPresent() );
        assertTrue( cs.get(2).getCommentedNode().isPresent() );


        _java._node _n = (_java._node)_java.of( cs.get(2).getCommentedNode().get());
        System.out.println("N "+ _n );
        System.out.println("C "+ cs.get(2).toString() );
        System.out.println("N .ast() "+ _n.ast() );
        //assertNotNull(_n.getComment());
    }


    public void testBlockComment(){

        /* block comment 1 */
        class BlockComment{
            /*
               block
               comment
               2
             */
            int i=0;

            /*
             * block
             * comment
             * 3
             */
            BlockComment(){ }

            /* block comment 4
             */
            void v(){}
        }

        _class _c = _class.of(BlockComment.class);
        List<Comment> cs = _c.astCompilationUnit().getComments();
        //attribution
        for(int i=0;i<cs.size();i++){
            assertTrue(cs.get(i).getCommentedNode().isPresent() );
        }
        System.out.println( cs );
    }

    public void testJavadocComment(){
        /** jd comment 1 */
        class JDComment{
            /**
             * jd
             * comment
             * 2
             */
            int i=0;

            /**
             * jd
             * comment
             * 3
             */
            JDComment(){ }

            /** jd comment 4
             */
            void v(){}
        }

        _class _c = _class.of(JDComment.class);
        List<Comment> cs = _c.astCompilationUnit().getComments();
        //attribution
        for(int i=0;i<cs.size();i++){
            assertTrue(cs.get(i).getCommentedNode().isPresent() );
        }
        cs.forEach(c-> System.out.println( c ) );
        //System.out.println( cs );
    }
}
