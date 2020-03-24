package org.jdraft;

import com.github.javaparser.ast.comments.Comment;
import junit.framework.TestCase;

import java.util.List;
import java.util.stream.Collectors;

public class _commentTest extends TestCase {


    public void testGetSetContents(){
        class G{
        }
    }

    public void testListComments(){

        /** Top level comment */
        class G{
            //orphaned

            /** Field */
            int i=0;
        }

        _class _c = _class.of( G.class);
        List<_comment> _cs = _c.listComments();
        assertEquals( 3, _cs.size());

        assertEquals( 0, _c.listComments(c-> c instanceof _blockComment).size());
        assertEquals( 0, _c.listBlockComments().size());

        assertEquals( 1, _c.listComments(c-> c instanceof _lineComment).size());

        _cs = _c.listComments(c-> c instanceof _javadocComment);
        assertEquals( 2, _cs.size());

        _c.forComments(c -> System.out.println( c ));
        _c.forComments(c-> c.isAttributed(), c-> System.out.println("ATTRIBUTED"+c.getCommentedNode()));

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
                _c.listJavadocComments().stream().map( jd-> jd.getNormalizedContents()).collect(Collectors.toList());

        assertEquals( "javadoc contents"+System.lineSeparator()+"with stuff" + System.lineSeparator()+"starting on first line", ls.get(0));
        assertEquals( "single javadoc with nothing on first line", ls.get(1));
        assertEquals( "multi line javadoc with"+System.lineSeparator()+"nothing on the first line", ls.get(2));
        //_c.listJavadocComments().forEach( c-> System.out.println("NORMAL>"+c.getNormalizedContents()) );

        //System.out.println( _c );

        _blockComment _bc = _c.listBlockComments().get(0);
        assertEquals( "BlockComment", _c.listBlockComments().get(0).getNormalizedContents() );
        assertEquals( "block comment" +System.lineSeparator()+ "multi line" + System.lineSeparator() +"with content",
                _c.listBlockComments().get(1).getNormalizedContents() );
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
