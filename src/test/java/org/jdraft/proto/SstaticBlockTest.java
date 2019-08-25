package org.jdraft.proto;

import com.github.javaparser.ast.stmt.BlockStmt;
import junit.framework.TestCase;
import org.jdraft.Stmt;
import org.jdraft._staticBlock;

public class SstaticBlockTest extends TestCase {

    public void testMatchAny(){
        $staticBlock $sb = $staticBlock.of();
        assertTrue($sb.isMatchAny());
        _staticBlock _sb = $sb.draft();

        assertTrue($sb.matches(_staticBlock.of("static{}")));
        assertTrue( _sb.hasBody());
    }

    public void testDraftConstant(){
        _staticBlock _sb = $staticBlock.of("System.out.println(1);").draft();
        System.out.println( _sb );

        assertEquals( _staticBlock.of("System.out.println(1);"), _sb);
        assertFalse( _staticBlock.of("System.out.println('c');").equals(  _sb ));
        assertEquals( _staticBlock.of(()->{
            System.out.println(1);
        }), _sb);

        _staticBlock.of( ()-> {
            System.out.println(1);
        });
    }

    public void testSelectParameterized(){
        $staticBlock $sb = $staticBlock.of( ($any$)->{
            System.out.println($any$);
        });

        _staticBlock _d = $sb.draft("any", 1);

        assertTrue($sb.matches( _staticBlock.of( ()-> System.out.println(1))));
        assertEquals( $sb.draft("any", 1), _staticBlock.of( ()-> System.out.println(1)));

        //verify I can
        assertTrue($sb.select( _staticBlock.of( ()-> System.out.println(1))).is("any", 1));
    }
    public void testDraftParameterized(){
        $staticBlock $sb = $staticBlock.of( (Object $p$)->System.out.println($p$) );
        assertEquals( Stmt.of(()->System.out.println('c')), $sb.draft("p", 'c').getStatement(0));
    }
}
