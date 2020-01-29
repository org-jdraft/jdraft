package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _switchStmtTest extends TestCase {

    public enum Suit{
        HEARTS,CLUBS,DIAMONDS,SPADES;
    }

    public void testEE(){
        Expression ee = Ex.of("com.github.javaparser.ast.expr.LongLiteralExpr");
        System.out.println( ee.getClass() );
        System.out.println( ee );
    }
    /* this is what is created when we do enums
        switch(a) {
            case SPADES:
                return 1;
            case HEARTS:
                return 2;
            case DIAMONDS:
                return 3;
            case CLUBS:
                return 4;
            default: return -1;
        }
    */

    public Suit expect(){
        int a = 2;
        switch(a) {
            case 1:
                return org.jdraft._switchStmtTest.Suit.SPADES;
            case 2:
                return org.jdraft._switchStmtTest.Suit.HEARTS;
            case 3:
                return org.jdraft._switchStmtTest.Suit.DIAMONDS;
            case 4:
                return org.jdraft._switchStmtTest.Suit.CLUBS;
            default:
                return Suit.CLUBS;
        }
    }
    public void testEnumValues(){
        _switchStmt _s = _switchStmt.of("a");
        _s.map(1, Suit.SPADES);
        _s.map(2, Suit.HEARTS);
        _s.map(3, Suit.DIAMONDS);
        _s.map(4, Suit.CLUBS);
        _s.setDefault((Object r)-> Suit.CLUBS);

        System.out.println( _s);
        assertEquals( 5, _s.listCaseGroups().size());
        assertEquals( 5, _s.listSwitchEntries().size());
    }
    public void testEnumCases(){
        _switchStmt _s = _switchStmt.of("a");
        _s.map(Suit.SPADES, 1);
        _s.map(Suit.HEARTS, 2);
        _s.map(Suit.DIAMONDS, 3);
        _s.map(Suit.CLUBS, 4);
        _s.setDefault((Object r)-> -1);
        assertEquals( 5, _s.listCaseGroups().size());
        assertEquals( 5, _s.listSwitchEntries().size());

        System.out.println( _s );
    }

    public void testEnumCasesMulti(){
        _switchStmt _s = _switchStmt.of("a");
        _s.map(Suit.SPADES, 1);
        _s.map(Suit.HEARTS, 2);
        _s.map(Suit.DIAMONDS, 2);
        _s.map(Suit.CLUBS, 1);
        _s.setDefault("return -1;");

        assertEquals( 3, _s.listCaseGroups().size());
        assertEquals( 5, _s.listSwitchEntries().size());

        //System.out.println( _s );
    }

    public void testF(){
        /*
        int i=0;
        switch (i){
            case 3+4: System.out.println( "Value");
        }
         */
        LongLiteralExpr lle = new LongLiteralExpr(1);

        System.out.println( lle.toString() );
        _long l = _long.of(1);
        System.out.println( l );
    }

    public void testMapToStatements(){
        float f = 1.23f;
        long l = 123L;
        String s = "ereer";

        //char, byte, short, int, String, enum
        //boolean b = true;
        //switch(b){
        //    case "ee" : System.out.println(1);
        //}
    }

    //mapping direct mapping from literals to other literals
    public void testMapTypesKeysValues(){
        _switchStmt _ss = _switchStmt.of("a");
        //String
        _ss.map("A",1);
        _ss.map("B", 'c');
        _ss.map("C", Long.MAX_VALUE);
        _ss.map("D", 12.03d);
        _ss.map("E", 12.03f);
        _ss.map(  "F", "G");

        //int
        _ss.map(1,1);
        _ss.map(1, 'c');
        _ss.map(1, Long.MAX_VALUE);
        _ss.map(1, 12.03d);
        _ss.map(1, 12.03f);
        _ss.map(1, "G");

        //char
        _ss.map('a',1);
        _ss.map('b', 'c');
        _ss.map('c', Long.MAX_VALUE);
        _ss.map('d', 12.03d);
        _ss.map('e', 12.03f);
        _ss.map('f', "G");

        _ss.map( _int.of("0xDEAD").ast(), 1 );
        _ss.map( _int.of("0xBEEF").ast(), 2 );
        System.out.println( _ss);
    }


    public void testMapVars(){
        _switchStmt _ss = _switchStmt.of("a");
        _ss.map(1, "A");
        _ss.map(2, "A");
        _ss.map(3, "B");

        _switchStmt _ss2 = _switchStmt.of("a");
        _ss2.map(1, _returnStmt.ofString("A"));
        _ss2.map(2, _returnStmt.ofString("A"));
        _ss2.map(3, _returnStmt.ofString("B"));

        assertEquals( _ss, _ss2);
        assertEquals(_ss.hashCode(), _ss2.hashCode());
        //System.out.println( _ss );
    }

    //TODO make the caseGroups use labels as apposed to SwitchEntries
    //
    public void testBuildViaMap(){
        Map<String, Integer> keyToValue = new HashMap<>();
        keyToValue.put("A", 1);
        keyToValue.put("B", 1);
        keyToValue.put("C", 2);

        _switchStmt _ss = _switchStmt.of("a");
        keyToValue.forEach( (k,v)->{
                _ss.map(k, v);
        } );

        //verify I can set the default
        _ss.setDefault((String a)->{
           throw new RuntimeException("Bad key "+a);
        });

        assertEquals( _ss.getDefault().getStatement(0), Stmt.of( (String a)->{ throw new RuntimeException("Bad key "+ a);}) );
        System.out.println( _ss );
    }

    public void testBuildEmptyCaseGroupsAndMutate(){
        //empty switch, no caseGroups
        assertTrue( _switchStmt.of().listCaseGroups().isEmpty() );

        //build and empty switch
        _switchStmt _s = _switchStmt.of();
        //build an empty caseGroup
        _caseGroup _cg = _caseGroup.of();
        _s.addCaseGroups( _cg);
        assertTrue( _s.listCaseGroups().isEmpty() ); //adding empty caseGroup to empty switch

        _cg.addCaseConstant(1);
        _s.addCaseGroups( _cg);
        //System.out.println( _s);
        _cg.setStatements("System.out.println(1);");
        //System.out.println( _s);
        _cg.setStatements( ()-> System.out.println(3));
        //System.out.println( _s);
        _cg.setStatements( ()-> {
            System.out.println(4);
            System.out.println(5);
        });
        //System.out.println( _s);
        _cg.addStatements( ()-> System.out.println(6));
        //System.out.println( _s);
        _cg.addStatements( ()-> {
            System.out.println(7);
            System.out.println(8);
        });

        //System.out.println( _s);

        assertEquals( Stmt.of( ()-> System.out.println(4)), _cg.getStatement(0));
        assertEquals( Stmt.of( ()-> System.out.println(5)), _cg.getStatement(1));
        assertEquals( Stmt.of( ()-> System.out.println(6)), _cg.getStatement(2));
        assertEquals( Stmt.of( ()-> System.out.println(7)), _cg.getStatement(3));
        assertEquals( Stmt.of( ()-> System.out.println(8)), _cg.getStatement(4));

        assertTrue( _cg.hasCaseConstant(1));
        assertFalse( _cg.hasCaseConstant(2));

        //ok, lets add some case constants
        _cg.addCaseConstant(2);
        _cg.addCaseConstant(3);
        _cg.addCaseConstant(4);
        _cg.addCaseConstant(5);
        _cg.addCaseConstant(6);

        //verify they have 'em
        assertTrue( _cg.hasCaseConstant(2));
        assertTrue( _cg.hasCaseConstant(3));
        assertTrue( _cg.hasCaseConstant(4));
        assertTrue( _cg.hasCaseConstant(5));
        assertTrue( _cg.hasCaseConstant(6));

    }

    //todo I should be able to create and ADD caseGroup(s) to the _switch
    public void testCaseGroups(){
        //NOTE there was an issue when I had comments on the entities
        _switchStmt cg = _switchStmt.of( (Integer dayOfWeek)-> {
            switch(dayOfWeek){
               case 1: case 2: case 3: case 4:
                   System.out.println("Week Day !");
               break;
               case 6: case 7:
                   System.out.println("Week End !");
               break;
               default:
                   throw new RuntimeException("bad day");
          }
        });

        List<_caseGroup> _cgs = cg.listCaseGroups();
        assertEquals( 3, _cgs.size());

        _cgs.get(0).addCaseConstant(5);

        assertTrue( _cgs.get(0).listCaseConstants().size()== 5);

        assertTrue( _cgs.get(0).hasCaseConstant(1));
        assertTrue( _cgs.get(0).hasCaseConstant(2));
        assertTrue( _cgs.get(0).hasCaseConstant(3));
        assertTrue( _cgs.get(0).hasCaseConstant(4));
        assertTrue( _cgs.get(0).hasCaseConstant(5)); //had an issue with comments
        //assertTrue(  _cgs.get(0).isPassthru() );

        assertTrue( _cgs.get(1).hasCaseConstant(6));
        assertTrue( _cgs.get(1).hasCaseConstant(7));

        //assertTrue( _cgs.get(2).hasCaseConstant(new IntegerLiteralExpr(9)));

        assertTrue( _cgs.get(2).isDefault());
        assertTrue( _cgs.get(2).listCaseConstants().size() == 0);

        System.out.println( _cgs.get(0) );
    }

    public void testLambdaAndDefault(){
        _switchStmt _s = _switchStmt.of( (Integer key)-> {
           switch(key){
               default: return 1;
           }
        });


        assertTrue( _s.getSwitchSelector().is("key"));
        assertTrue( _s.getSwitchEntry(0).is("default: return 1;"));
        assertTrue( _s.listSwitchEntries().get(0).is("default: return 1;"));
        System.out.println( _s );

        //verify I can get the default for the switch
        _switchEntry def = _s.getDefault();
        assertNotNull(def);

        //sets the default for the switch
        _s.setDefault("System.out.println(1);");
        System.out.println(_s);
        _s.setDefault("System.out.println(1); System.out.println(2);");
        System.out.println(_s);
        assertEquals (1, _s.listSwitchEntries(_se -> _se.isDefault() ).size());

        assertFalse( _s.getSwitchEntry(0).isCaseConstant(0));
    }

}
