package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft.Print;
import org.jdraft._charExpr;
import org.jdraft._intExpr;

import java.util.stream.Collectors;

/**
 * Use the $pattern API and streamline queries and actions
 *
 */
public class SPatternApiTest extends TestCase {

    //query
    //  isIn
    //  countIn
    //  firstIn
    //  listIn
    //  streamIn

    // removeIn
    // replaceIn
    // replaceSelectedIn

    //refining the query
    //  $
    //  $hardcode

    //  $and
    //  $not

    // $isInRange
    //

    // at

    // descendant ancestor child parent
    // has
    // hasNo

    //select
    //  selectFirstIn
    //  selectIn
    //

    // query & do
    //  forEachIn
    //  forEachSelectedIn
    //  streamIn

    public void testC(){
        class F {
            int aa = 100;
            char a = '\t';
            char b = 'a';
            char c = 'g';
            char[] cs = {'a', 'b', 'c'};
            int i,j = 1;
            String s = "1";
        }

        //select FIRST LITERAL FROM F.class
        $.literal().firstIn(F.class);
        System.out.println( $.literal("'a'").firstIn(F.class) );
        assertEquals(3, $.literal("'a'", "1").countIn(F.class) );
        assertTrue($.literal().isIn(F.class));

        //$.textBlock("");
        $.doubleLiteral(1.0d, 2.0d, 3.0d );
        //$.stringLiteral("a", "b", "c");
        $.charLiteral('a', 'e', 'i', 'o', 'u');

        $.literal().streamIn(F.class).forEach( l-> System.out.println(l) ); //map((LiteralExpr l) -> l.asLiteralExpr())

        _intExpr _i = $.intLiteral(1).firstIn(F.class);
        System.out.println(_i);
        assertEquals(1, $.intLiteral(1).countIn(F.class));
        assertEquals(2, $.intLiteral(1, 100).countIn(F.class));
        _charExpr _c = $.charLiteral('g').firstIn(F.class);

        System.out.println( $.charLiteral().streamIn(F.class).map(c-> c.getValue()).collect(Collectors.toList()) );

        //_int _j = $int.of(1,2,3,4).firstIn(F.class).map(i-> i.getValue()); //stream().skip(2).forEach( i -> System.out.println( i ) );
        //$char.of('a', 'e', 'i', 'o', 'u').firstIn(F.class);
        //$char.of().stream().skip(3).

        //_field _c = _class.of(F.class).getField("c");
        Print.tree(_c);

    }
}
