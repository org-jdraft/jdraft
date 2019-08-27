package org.jdraft.proto;

import junit.framework.TestCase;
import org.jdraft.Stmt;
import org.jdraft._initBlock;

public class SstaticBlockTest extends TestCase {

    public void testMatchAny(){
        $staticBlock $sb = $staticBlock.of();
        assertTrue($sb.isMatchAny());
        _initBlock _sb = $sb.draft();

        assertTrue($sb.matches(_initBlock.of("static{}")));
        assertTrue( _sb.hasBody());
    }

    public void testDraftConstant(){
        _initBlock _sb = $staticBlock.of("System.out.println(1);").draft();
        System.out.println( _sb );

        assertEquals( _initBlock.of("System.out.println(1);"), _sb);
        assertFalse( _initBlock.of("System.out.println('c');").equals(  _sb ));
        assertEquals( _initBlock.of(()->{
            System.out.println(1);
        }), _sb);

        _initBlock.of( ()-> {
            System.out.println(1);
        });
    }

    public void testSelectParameterized(){
        $staticBlock $sb = $staticBlock.of( ($any$)->{
            System.out.println($any$);
        });

        _initBlock _d = $sb.draft("any", 1);

        assertTrue($sb.matches( _initBlock.of( ()-> System.out.println(1))));
        assertEquals( $sb.draft("any", 1), _initBlock.of( ()-> System.out.println(1)));

        //verify I can
        assertTrue($sb.select( _initBlock.of( ()-> System.out.println(1))).is("any", 1));
    }
    public void testDraftParameterized(){
        $staticBlock $sb = $staticBlock.of( (Object $p$)->System.out.println($p$) );
        assertEquals( Stmt.of(()->System.out.println('c')), $sb.draft("p", 'c').getStatement(0));
    }
}
