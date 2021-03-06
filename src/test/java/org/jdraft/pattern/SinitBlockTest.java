package org.jdraft.pattern;

import junit.framework.TestCase;
import org.jdraft.Stmt;
import org.jdraft._initBlock;

public class SinitBlockTest extends TestCase {

    public void testMatchStmt(){
        $initBlock $ib = $initBlock.of(()-> System.out.println( 12 ) );

        //matches static exact
        assertTrue( $ib.matches("static { System.out.println( 12 ); } "));

        //matches instance exact
        assertTrue( $ib.matches("{ System.out.println( 12 ); } "));

        //matches with other statement
        assertTrue( $ib.matches("{ System.out.println( 12 ); assert(true); } "));

        $ib = $initBlock.as(()-> System.out.println( 12 ) );
        assertFalse( $ib.matches("{ System.out.println( 12 ); assert(true); } "));
    }


    public void testMatchAny(){
        $initBlock $ib = $initBlock.of();
        assertTrue($ib.isMatchAny());
        _initBlock _sb = $ib.draft();

        assertTrue($ib.matches(_initBlock.of("static{}")));
        assertTrue( _sb.hasBody());
    }

    public void testDraftConstant(){
        _initBlock _sb = $initBlock.of("System.out.println(1);").draft();
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
        $initBlock $sb = $initBlock.of( ($any$)->{
            System.out.println($any$);
        });

        _initBlock _d = $sb.draft("any", 1);

        assertTrue($sb.matches( _initBlock.of( ()-> System.out.println(1))));
        assertEquals( $sb.draft("any", 1), _initBlock.of( ()-> System.out.println(1)));

        //verify I can
        assertTrue($sb.select( _initBlock.of( ()-> System.out.println(1))).is("any", 1));
    }
    public void testDraftParameterized(){
        $initBlock $sb = $initBlock.of( (Object $p$)->System.out.println($p$) );
        assertEquals( Stmt.of(()->System.out.println('c')), $sb.draft("p", 'c').getAstStatement(0));
    }
}
