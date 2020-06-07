package org.jdraft;

import com.github.javaparser.ast.stmt.EmptyStmt;
import junit.framework.TestCase;


public class _doStmtTest extends TestCase {

    public void testD(){
        _doStmt _ds = _doStmt.of();
        System.out.println( _ds );
        assertTrue( _ds.getBody().isImplemented());
        assertEquals( _ds.getCondition(), _booleanExpr.of(false));
        assertTrue(_ds.isBody(";"));
        _doStmt _expect = _doStmt.of( ()->{
           do ; while(false);
        });
        assertEquals( _ds, _expect);
    }

    public void testRemoveSingleStmt(){
        _doStmt _ss = _doStmt.of( ()->{
            do assert(1==1); while(false);
        });


        System.out.println("BD"+  _ss.getBody() );

        //this works
        //_ss.ast().setBody(new EmptyStmt());
        _ss.removeStatements(_assertStmt.class);
        //_ss.removeStatements( _assertStmt.class );
        System.out.println( _ss);

    }
    public void testSingleStmt(){
        _doStmt _ss = _doStmt.of( ()->{
            do ; while(false);
        });

        //here, when I add, I need to upgrade to a blockStmt
        _ss.add( ()-> System.out.println(1));

        _doStmt _ex = _doStmt.of( ()->{
            do System.out.println(1); while (false);
        });
        assertEquals( _ss, _ex);

        System.out.println( _ss );
    }

    public void testDoAct(){
        _doStmt _ez = _doStmt.of( ()->{
            do return; while(false);
        });

        _ez.add(()->System.out.println(1));

        //_ez.setBody( ()->System.out.println(1) );
        System.out.println( _ez );

        //System.out.println("THE BODY "+ _ez.ast().getBody());
        _ez.getBody().ast();

        _doStmt _ds = _doStmt.of( ()->{
           do {
               System.out.println(1);
               System.out.println(12);
           } while(true);
        });

        System.out.println( _ds );
        //assertTrue(_ds.getBody().is(_blockStmt.class));
    }
}