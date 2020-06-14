package org.jdraft;

import junit.framework.TestCase;

public class _switchCaseTest extends TestCase {


    public void testApi(){
        _switchCase _se = _switchCase.of();

        System.out.println( _se.getBodyType());
    }

    public void m(){
        char c = 'c';
        switch(c){
            case 'a': case 'b' :
                System.out.println(1);
        }
    }

    public void testMultiCase(){
        _switchStmt _sw = _switchStmt.of( (Integer key)->{
            switch(key){
                case 1 : case 2: System.out.println(1);
            }
        });
        Print.tree(_sw.ast());

        _sw.getCase(1);

        _sw.getSwitchEntry(s -> s.hasCaseConstant(1) );
        assertEquals( 2, _sw.countSwitchEntries());
        System.out.println( _sw );
    }

    public void testBuildFromScratch(){
        _switchCase _se = _switchCase.of();
        System.out.println( _se );
        assertTrue( _se.isDefault());
        assertTrue( _se.isStatementGroup() );

        _se.setCaseConstant('c');
        //_se.addCase('c');
        _se.setStatements("System.out.println(1);");
        System.out.println( _se );
        _se.setStatements(()-> System.out.println(2));
        assertTrue( _se.listAstStatements().size() == 1);
        assertEquals(Stmt.of( ()->System.out.println(2)),  _se.getStatement(0));
        Print.tree(_se.ast());

        _se.addStatements("System.out.println(2);", "System.out.println(3);");
        System.out.println( _se );

        Print.tree(_se.ast());
    }

    public void testHasCase(){
        //test String default
        _switchCase _se = _switchCase.of("default: System.out.println(1);");
        assertTrue( _se.isDefault() );

        _se = _switchCase.of("case 1: return 'a';");
        assertFalse( _se.isDefault() );

        assertTrue( _se.hasCaseConstant(1) );
        assertFalse( _se.hasCaseConstant(2));

        _se.setCaseConstant(2);
        assertTrue( _se.hasCaseConstant(2));
        assertFalse( _se.hasCaseConstant(1));
        System.out.println( _se );
    }


}
