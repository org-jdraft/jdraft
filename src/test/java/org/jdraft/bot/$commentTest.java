package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft.*;

public class $commentTest extends TestCase {

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

        $c = $comment.of(c-> c.isAttributed());
        assertEquals( 4, $c.countIn(C.class));

        //test lambdas
        assertEquals( 1, $comment.line(c-> c.isAttributed()).countIn(C.class));
        assertEquals( 1, $comment.block(c-> c.isAttributed()).countIn(C.class));
        assertEquals( 2, $comment.javadoc(c-> c.isAttributed()).countIn(C.class));

        assertTrue( $comment.of("line comment 1").stencil.matches("line comment 1"));
        _lineComment _lc = _lineComment.of("line comment 1");
        assertEquals( "line comment 1", _lc.getContents());
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
        assertEquals( 1, $comment.of(c-> c instanceof _blockComment).countIn(GG.class));
        assertEquals( 1, $comment.block().countIn(GG.class));

        assertEquals( 2, $comment.of().$isAttributed().countIn(GG.class)); //lineComment
        assertEquals( 1, $comment.of().$isAttributed(false).countIn(GG.class)); /*blockComment*/

        assertEquals( 2, $comment.of(c-> c.isAttributed()).countIn(GG.class)); //lineComment
        assertEquals( 1, $comment.of(c-> !c.isAttributed()).countIn(GG.class)); /*block comment*/
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
        assertEquals(6, $comment.of(c-> c.getContents().startsWith("TODO")).countIn(C.class));

        System.out.println( _statement.of( () -> System.out.println(1)) );

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
