package lut;

import junit.framework.TestCase;
import org.jdraft.Exprs;
import org.jdraft.Stmts;
import org.jdraft._returnStmt;
import org.jdraft._switchStmt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SwitchBuilderTest extends TestCase {

    public void testBuildSwitchThrow(){
        Map<String,Integer> keyToValue = new HashMap<>();
        keyToValue.put("A", 1);

        _switchStmt _ss = _switchStmt.of("key");
        _ss.setDefault( (String key) -> {throw new RuntimeException("Invalid Key "+ key);} );
        _ss.mapCode("B", ()->{throw new IOException("IOE");});

        assertTrue(_ss.getCaseGroup("B").isThrow(IOException.class));
    }

    public void testBuildSwitchLut(){
        //here we just create ourselves a sample mapping
        Map<String,Integer> keyToValue = new HashMap<>();
        keyToValue.put("A", 1);
        keyToValue.put("B", 2);
        keyToValue.put("C", 3);
        keyToValue.put("D", 2); /* Note both "B" and "D" return 2 */

        //_switchStmt _ss = _switchStmt.of().setSwitchSelector("key");
        _switchStmt _ss = _switchStmt.of("key");
        keyToValue.forEach( (String s, Integer i)-> _ss.mapReturn(s, i));
        _ss.setDefault( (String key) -> {throw new RuntimeException("Invalid Key "+ key);} );


        assertEquals( _returnStmt.of(1), _ss.getCaseGroup("A").getStatement(0) );
        assertEquals( _returnStmt.of(2), _ss.getCaseGroup("B").getStatement(0) );
        assertEquals( _returnStmt.of(3), _ss.getCaseGroup("C").getStatement(0) );
        assertEquals( _returnStmt.of(2), _ss.getCaseGroup("D").getStatement(0) );

        assertTrue( _ss.getCaseGroup("A").getStatement(0).is( Stmts.of(()->{return 1;} )) );
        assertTrue( _ss.getCaseGroup("B").getStatement(0).is( Stmts.of(()->{return 2;} )) );
        assertTrue( _ss.getCaseGroup("C").getStatement(0).is( Stmts.of(()->{return 3;} )) );
        assertTrue( _ss.getCaseGroup("D").getStatement(0).is( Stmts.of(()->{return 2;} )) );

        assertTrue( _ss.getCaseGroup("A").isReturn(Exprs.of(1)));
        assertTrue( _ss.getCaseGroup("B").isReturn(Exprs.of(2)));
        assertTrue( _ss.getCaseGroup("C").isReturn(Exprs.of(3)));
        assertTrue( _ss.getCaseGroup("D").isReturn(Exprs.of(2)));

        //heres the simplified version
        assertTrue( _ss.getCaseGroup("A").isReturn(1));
        assertTrue( _ss.getCaseGroup("B").isReturn(2));
        assertTrue( _ss.getCaseGroup("C").isReturn(3));
        assertTrue( _ss.getCaseGroup("D").isReturn(2));

        assertTrue( _ss.getDefault().isThrow() );
        assertTrue( _ss.getDefault().isThrow(RuntimeException.class) );


        System.out.println( _ss );

    }
}
