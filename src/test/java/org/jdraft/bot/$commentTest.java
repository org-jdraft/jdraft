package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;

import java.util.List;

public class $commentTest extends TestCase {

    public void testWithText(){
        assertTrue($comment.of("with text").matches("// with text"));
        //assertTrue($comment.of("with text").matches("// before with text and extra"));
        _blockComment _bc = _blockComment.of(" with some text and other ");
        assertTrue(_bc.contains("some text"));

    }
    //convert <code>$contents$</code> to {@code $contents}
    public void testCodeConvert() {
        Stencil codeTags = Stencil.of("<code>$contents$</code>");
        Stencil codeTagRep = Stencil.of("{@code $contents$}");

        $comment $ct = $comment.of().$and(c -> c.contains(codeTags));
        assertTrue($ct.matches("// content <code>1</code> content "));
        assertTrue($ct.matches("/* some content <code>1</code> content*/"));
        assertTrue($ct.matches("/** some content <code>1</code> content **/"));

        //todo caps
        class CC {
            // <code>1</code>
            int i;

            /* <code>2</code> */
            int j;

            /**
             * <code>3</code>
             */
            int k;
        }
        $ct.printIn(CC.class);

        assertEquals(3, $ct.countIn(CC.class));

        _class _c = $ct.forSelectedIn(CC.class, s -> {
            String content = s.select.getText();
            Tokens ts = codeTags.parseFirst(content);
            while (ts != null) {
                //realize the old version
                String match = codeTags.draft(ts);
                String replacement = codeTagRep.draft(ts);
                content = Text.replace(content, match, replacement);
                ts = codeTags.parseFirst(content);
            }
            s.select.setText(content);
        });

        System.out.println(_c);

        //now verify we can find exactly (3) changed {@code } comments
        assertEquals(3, $comment.of().$and(c -> c.contains(codeTagRep)).countIn(_c));
    }

    public void testMulti(){
        Stencil codeTags = Stencil.of("<code>$contents$</code>");
        Stencil codeTagRep = Stencil.of("{@code $contents$}");
        $comment $ct = $comment.of().$and(c -> c.contains(codeTags));
        class Multi{
            //<code>1</code>
            int i;
            /* <code>
               some contents
               on multiple lines
               </code>
             */
            int j;
            /**
             * <code>a comment with multiple code tags</code>
             * <code>
             *     Here, we use a
             *     multiple line tag
             * </code>
             * another string
             */
            int k;
        }
        //look through all matching comments and
        List<_comment> _cs =
                $ct.matchReplaceIn(Ast.of(Multi.class), "<code>$contents$</code>", "{@code $contents$}");
        System.out.println( _cs);
    }



    public void testRR(){
        /** class javadoc */
        class C{
            //comment 1
            int i;
            /* block comment 1*/
            int j;
            /** javadoc */
            int k;
        }
        //System.out.println( Ast.of(C.class) );
        _class _c = _class.of(C.class);
        assertEquals(4, _c.listAllComments().size() );

        assertEquals( 1, _c.listAllBlockComments().size());
        assertEquals( 1, _c.listAllLineComments().size());
        assertEquals( 2, _c.listAllJavadocComments().size());
    }

    public void testAttributed(){
        _class _c = _class.of("/** class */ public class A{}");
        assertNotNull(_c.getJavadoc());
    }

    public void testC(){
        /** class javadoc */
        class C{
            //line comment 1
            int i;
            /* block comment 1*/
            int j;
            /** javadoc */
            int k;
        }
        //System.out.println( Ast.of(C.class) );
        //System.out.println( _class.of(C.class) );

        assertEquals(1, $comment.line().countIn(C.class));
        assertEquals(1, $comment.block().countIn(C.class));
        assertEquals(2, $comment.javadoc().countIn(C.class));

        $comment $c = $comment.of();
        assertTrue( $c.isMatchAny() );
        assertEquals( 4, $c.countIn(C.class));



        $comment.of().printIn(C.class);

        assertEquals(4, $comment.of().countIn(C.class));
        assertEquals(4, $c.listSelectedIn(C.class).size());

        //test remove
        _class _c = $c.removeIn(C.class);
        assertEquals( 0, $c.countIn(_c) );
        //System.out.println( _c );

        assertTrue( 4 == $c.listIn(C.class).size());
        assertTrue($c.matches("/**JDC*/"));
        assertTrue($c.matches("/*BC*/"));
        assertTrue($c.matches("// line comment"));

        $c = $comment.of().$and(c-> c.isAttributed());
        assertEquals( 4, $c.countIn(C.class));

        //test lambdas
        assertEquals( 1, $comment.line(c-> c.isAttributed()).countIn(C.class));
        assertEquals( 1, $comment.block(c-> c.isAttributed()).countIn(C.class));
        assertEquals( 2, $comment.javadoc(c-> c.isAttributed()).countIn(C.class));

        assertTrue( $comment.of("line comment 1").stencil.matches("line comment 1"));
        _lineComment _lc = _lineComment.of("line comment 1");
        assertEquals( "line comment 1", _lc.getText());
        assertEquals(1, $comment.of("line comment 1").countIn(C.class));
        assertEquals(1, $comment.of("block comment 1").countIn(C.class));
        assertEquals(1, $comment.of("class javadoc").countIn(C.class));
        assertEquals(1, $comment.of("javadoc").countIn(C.class));

        assertEquals(1, $comment.line("line comment 1").countIn(C.class));
        assertEquals(1, $comment.block("block comment 1").countIn(C.class));
        assertEquals(1, $comment.javadoc("class javadoc").countIn(C.class));
        assertEquals(1, $comment.javadoc("javadoc").countIn(C.class));
    }

    public void testF(){
        //lineComment
        class GG{
            /* block comment */
            /** javadoc comment */
            int i=0;
        }
        assertEquals( 1, $comment.of().$and(c-> c instanceof _blockComment).countIn(GG.class));
        assertEquals( 1, $comment.block().countIn(GG.class));

        assertEquals( 2, $comment.of().$isAttributed().countIn(GG.class)); //lineComment
        assertEquals( 1, $comment.of().$isAttributed(false).countIn(GG.class)); /*blockComment*/

        assertEquals( 2, $comment.of().$and(c-> c.isAttributed()).countIn(GG.class)); //lineComment
        assertEquals( 1, $comment.of().$and(c-> !c.isAttributed()).countIn(GG.class)); /*block comment*/
    }

    public void testStencil(){
        class C{
            void m() {
                //TODO
                /*TODO*/
                /**TODO*/

                //TODO do something
                /* TODO do something else */
                /** TODO do some other thing */
            }
        }

        //verify we can match the
        assertEquals(6, $comment.of().$and(c-> c.getText().startsWith("TODO")).countIn(C.class));

        System.out.println( _stmt.of( () -> System.out.println(1)) );

        class replaceInBody{
            void v(){
                /**TODO*/

            }
        }

        _class _c = _class.of(replaceInBody.class);


        /*
        System.out.println(
                $comment.of("TODO").replaceIn(replaceInBody.class, _statement.of(() -> System.out.println(1)).ast() ) );
        */

    }
}
