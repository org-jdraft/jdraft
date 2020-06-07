package org.jdraft;

import junit.framework.TestCase;

public class _ifStmtTest extends TestCase {

    public void testIfFromScratch(){
        _ifStmt _is = _ifStmt.of();

        assertFalse(_is.isThenBlock());
        assertFalse(_is.isElseBlock());

        //System.out.println(_is);
        _is.setCondition("true");
        //System.out.println(_is);
        assertTrue( _is.isCondition("true"));
        _is.isCondition(_booleanExpr.class);
        assertTrue( _is.isCondition(_booleanExpr.class, b-> b.isTrue() ));
        _is.setThen("System.out.println(1);");
        //System.out.println("BDY"+ _is.getThen() );
        assertTrue(_is.isThen("System.out.println(1);"));
        //System.out.println(_is);
        _is.setThen(_stmt.of("System.out.println(1);"));
        assertFalse(_is.isThenBlock());
        assertFalse(_is.isElseBlock());
        //System.out.println(_is);
        _is.setElse("System.out.println(2);");
        //System.out.println(_is);
        assertFalse(_is.isThenBlock());
        assertFalse(_is.isElseBlock());
    }

    public void testBlockIfElse(){
        _ifStmt _is = _ifStmt.of("if(true);");

        assertTrue( _is.isThen(_emptyStmt.of()));

        assertFalse( _is.isThenBlock());
        assertFalse( _is.isElseBlock());

        _is.setThen( ()->{
            System.out.println(1);
            System.out.println(2);
        });
        System.out.println( _is );
        assertTrue( _is.isThenBlock());
        assertFalse( _is.isElseBlock());

        assertFalse( _is.hasElse());
        _is.setElse( ()->{
           System.out.println("else");
           System.out.println("block");
        });
        assertTrue( _is.isThenBlock());
        assertTrue( _is.isElseBlock());

        assertTrue(_is.isThen(_blockStmt.class));
        assertTrue(_is.isThen(_blockStmt.class, _bs-> !_bs.isEmpty() ));

        assertTrue(_is.isElse(_blockStmt.class));
        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.size() == 2));

        _is.removeElse();
        assertTrue( _is.isThenBlock());
        assertFalse( _is.isElseBlock());

        _is.clearThen();
        assertFalse( _is.isThenBlock());
        assertFalse( _is.isElseBlock());

        assertFalse(_is.hasElse());
    }

}