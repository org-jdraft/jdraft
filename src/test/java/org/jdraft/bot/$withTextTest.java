package org.jdraft.bot;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._field;

public class $withTextTest extends TestCase {


    public void testFromJir(){
        class C {
            void method1() {
                getDrawCard(1).clickLink("Apply Serial Approval 2 for  abcUser(1) for Period 1.");
                getDrawCard(2).clickLink("Apply Serial Approval 2 for abcUser(2) for Period 2.");
            }
            void method2() {
                getDrawCard(1).clickLink("Apply Serial Approval 2 for xyzUser(1) for Period 1.");
                getDrawCard(2).clickLink("Apply Serial Approval 2 for xyzUser(2) for Period 2.");
            }
            class CL{
                public void clickLink(String s){
                }
            }
            CL getDrawCard(int i){
                return new CL();
            }
        }
        _class _c = $withText.of(" Period ").replaceIn(C.class, " Draw ");
        System.out.println( _c );
    }

    public void test$String(){
        class C{
            String s = "this is a String containing text";
        }
        assertEquals(0, $stringExpr.of("containing").countIn(C.class));
        assertEquals(1, $stringExpr.contains("containing").countIn(C.class));

        _class _c = $withText.of("containing").replaceIn(C.class, "replaced");

        System.out.println( _c );

        //$string.contains("containing");

        //this unifies, comment strings, literal strings and textblocks
        //$strings.contai()

        //_class _c =
        //        $string.contains("containing").matchReplaceIn(C.class, "contains", "replaced");
        //.forEachIn(C.class, _s -> _s.replace("containing", "replaced"));

        //_c = $string.of().$and(s-> s.contains( "containing"))
        //        .forEachIn(C.class, _s -> _s.replace("containing", "replaced"));

        //System.out.println(_c);
    }

    public void testAll(){
        /**
         * Target String
         */
        class C{
            // Target String
            String s = "Target String";

            /* Target String */
            public void m(){
                //Target String

                /* Target String */

                /** Target String */
            }
        }

        _class _c = _class.of(C.class);
        _c.addAfterLast(_field.class, _field.of("String textBlock= \"\"\"\n"
                + //"Text block\n"+
                "Target String \n"+
                "\"\"\""));

        assertEquals(6, _c.listAllComments().size());
        assertEquals(8, $withText.of("Target String").countIn(_c));

        _c = $withText.of("Target String").replaceIn(_c, "Replacement");
        assertEquals( 0, $withText.of("Target String").countIn(_c)); //make sure we cant find
        assertEquals( 8, $withText.of("Replacement").countIn(_c));

        System.out.println( _c );
    }

    public void testAllIntermediate(){
        /**
         * Before Target String After
         * and more lines
         */
        class C{
            // Brefore This Target String at the end
            String s = "A long Target String";

            /* Target String */
            public void m(){
                //Target String

                /* Target String */

                /** Target String */
            }
        }

        _class _c = _class.of(C.class);
        assertEquals(6, _c.listAllComments().size());
        assertEquals(7, $withText.of("Target String").countIn(C.class));

        _c = $withText.of("Target String").replaceIn(C.class, "Replacement");
        assertEquals( 0, $withText.of("Target String").countIn(_c)); //make sure we cant find
        assertEquals( 7, $withText.of("Replacement").countIn(_c));

        System.out.println( _c );
    }
}
