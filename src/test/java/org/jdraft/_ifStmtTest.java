package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.text.Stencil;

public class _ifStmtTest extends TestCase {


    public void testBare(){
        //these are all forms
        if(true); //no body
        if(true) {} //empty body
        if(true) System.out.println(1); //braceless body


        if(System.currentTimeMillis() % 2 == 0 ) //ifCondition
            System.out.println(1);


        _ifStmt _is = _ifStmt.of( "if(true);" );
        _is = _ifStmt.of( "if(true){}" );
        _is = _ifStmt.of( "if(System.currentTimeMillis() % 2 == 0 ) //ifCondition\n" +
                          "    System.out.println(1);");
        assertFalse(_is.hasElse());
    }

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
        //make sure the else stmt contains (2) _exprStmts
        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count() == 2));
        //make sure the else block contains (2) methodCallExpr expressionStmts
        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count(_e->_e.isExpression(_methodCallExpr.class)) == 2));
        //make sure the else block contains (2) methodCallExpr expressionStmts that are System.out.println s
        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count(
                _e->_e.isExpression(_methodCallExpr.class, _mc-> _mc.isScope("System.out") && _mc.isNamed("println"))) == 2));

        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count(
                _e->_e.isExpression(_methodCallExpr.class, _mc-> _mc.isScopedName("System.out.println"))) == 2));

        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count(
                _e->_e.isExpression(_methodCallExpr.class, _mc-> _mc.is("System.out.println($any$)"))) == 2));

        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count(
                _e-> _e.is("System.out.println($any$);")) ==2));

        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count(
                Stencil.of("System.out.println($any$);")) ==2));

        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count(Stencil.of(
                "System.out.println($any$);")) ==2));

        //verify we accept stylistic differences ( here space padded arg lists )
        assertTrue(_is.isElse(_blockStmt.class, _bs-> _bs.walk(_exprStmt.class).count(Stencil.of(
                "System.out.println($any$);")) ==2));

        assertTrue(_is.walk(_exprStmt.EXPRESSION).has());
        assertEquals(_is.walk(_exprStmt.EXPRESSION).first(es-> es instanceof _methodCallExpr), _methodCallExpr.of("System.out.println(1)") );

        _is.removeElse();
        assertTrue( _is.isThenBlock());
        assertFalse( _is.isElseBlock());

        _is.clearThen();
        assertFalse( _is.isThenBlock());
        assertFalse( _is.isElseBlock());

        assertFalse(_is.hasElse());
    }

    public void testWalkFeature(){
        _method _m = _method.of( new Object(){
           public int val(int x,int y){
               int z = x + y;
               return 100 + 200 + z;
           }
        });

        assertTrue(_m.walk(_returnStmt.class).first().is("return 100+200+z;"));
        assertTrue(_m.walk(_returnStmt.class).first().is("return 100+$any$+z;"));
        assertEquals(_nameExpr.of("x"), _m.walk(_binaryExpr.LEFT).first());
        assertEquals(_nameExpr.of("y"), _m.walk(_binaryExpr.RIGHT).first());

        assertEquals("x", _m.walk(_binaryExpr.LEFT).first().toString());
        assertEquals("y", _m.walk(_binaryExpr.RIGHT).first().toString());


        assertEquals("y", _m.walk(_binaryExpr.LEFT, _binaryExpr.RIGHT).first(_e-> _e.is("y")).toString());

        //_m.walk and find one of these features
        _expr _l = _m.walk(_binaryExpr.LEFT, _binaryExpr.RIGHT).first(_expr._literal.class);

        assertEquals( _expr.of(100), _m.walk(_binaryExpr.LEFT, _binaryExpr.RIGHT).first(_expr._literal.class));
        //String name = _m.walk(_feature._id.NAME).first();

    }
}