package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._blockComment;
import org.jdraft._class;
import org.jdraft._lineComment;
import org.jdraft._statement;

public class $commentTest extends TestCase {
    public void testC(){
        /** class javadoc */
        class C{
            //orphaned line comment 1
            /* orphaned block comment 1*/
            /** orphaned javadoc */
        }

        _class.of(C.class);

        $comment $c = $comment.of();
        assertTrue( $c.isMatchAny() );

        assertEquals(4, $c.countIn(C.class));
        assertEquals(4, $c.listSelectedIn(C.class).size());

        _class _c = $c.removeIn(C.class);
        assertEquals( 0, $c.countIn(_c) );
        //System.out.println( _c );

        assertTrue( 4 == $c.listIn(C.class).size());
        assertTrue($c.matches("/**JDC*/"));
        assertTrue($c.matches("/*BC*/"));
        assertTrue($c.matches("// line comment"));
    }

    public void testF(){
        //lineComment
        class GG{
            /* block comment */
            /** javadoc comment */
            int i=0;
        }
        assertEquals( 1, $comment.of(c-> c instanceof _blockComment).countIn(GG.class));
        assertEquals( 1, $comment.blockComment().countIn(GG.class));

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
