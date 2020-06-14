package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _switchStmtTest extends TestCase {

    public enum Color{
        BLACK, RED;
    }
    public enum Suit{
        HEARTS(Color.RED),CLUBS(Color.BLACK),DIAMONDS(Color.RED),SPADES(Color.BLACK);
        public final Color color;
        Suit(Color col){
            this.color = col;
        }
    }

    public enum Rank{
        ACE, TWO, THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE, TEN, JACK, QUEEN,KING;
    }

    public void testMapToLogic(){
        _switchStmt _ss = _switchStmt.of("a").autoBrake(true);
        _ss.mapCode(1, ()->{ System.out.println(1); });
        _ss.mapCode(2, ()->System.out.println(2));
        _ss.mapCode(3, ()->System.out.println(3));
        _ss.setDefault((String a)->System.out.println(a));
        System.out.println(_ss);

        /*
        with "autoBreak", when I add a statement or group of statements
        ...I need to check if the last statement is not a break, return, or throw
        ...if so, add a break at the end
        int a = 100;
        switch(a) {
            case 1:
                System.out.println(1);
                break;
            case 2:
                System.out.println(2);
                break;
            case 3:
                System.out.println(3);
                break;
            default:
                System.out.println(a);
                break;
                }
         */
    }

    public void testEnumToEnum(){
        Suit s = Suit.CLUBS;
        _switchStmt _ss = _switchStmt.of("rtos");
        _ss.mapReturn(Rank.ACE, Suit.SPADES);

        System.out.println( _ss );
    }

    public void testEE(){
        Expression ee = Expr.of("com.github.javaparser.ast.expr.LongLiteralExpr");
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

    /*
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
    */
    public void testEnumValues(){
        _switchStmt _s = _switchStmt.of("a");
        _s.mapReturn(1, Suit.SPADES);
        _s.mapReturn(2, Suit.HEARTS);
        _s.mapReturn(3, Suit.DIAMONDS);
        _s.mapReturn(4, Suit.CLUBS);
        _s.setDefault((Object r)-> Suit.CLUBS);

        System.out.println( _s);
        assertEquals( 5, _s.listCaseGroups().size());
        assertEquals( 5, _s.listSwitchEntries().size());
    }
    public void testEnumCases(){
        _switchStmt _s = _switchStmt.of("a");
        _s.mapReturn(Suit.SPADES, 1);
        _s.mapReturn(Suit.HEARTS, 2);
        _s.mapReturn(Suit.DIAMONDS, 3);
        _s.mapReturn(Suit.CLUBS, 4);
        _s.setDefault((Object r)-> -1);
        assertEquals( 5, _s.listCaseGroups().size());
        assertEquals( 5, _s.listSwitchEntries().size());

        System.out.println( _s );
    }

    public void testEnumCasesMulti(){
        _switchStmt _s = _switchStmt.of("a");
        _s.mapReturn(Suit.SPADES, 1);
        _s.mapReturn(Suit.HEARTS, 2);
        _s.mapReturn(Suit.DIAMONDS, 2);
        _s.mapReturn(Suit.CLUBS, 1);
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
        _longExpr l = _longExpr.of(1);
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
        _ss.mapReturn("A",1);
        _ss.mapReturn("B", 'c');
        _ss.mapReturn("C", Long.MAX_VALUE);
        _ss.mapReturn("D", 12.03d);
        _ss.mapReturn("E", 12.03f);
        _ss.mapReturn(  "F", "G");

        //int
        _ss.mapReturn(1,1);
        _ss.mapReturn(1, 'c');
        _ss.mapReturn(1, Long.MAX_VALUE);
        _ss.mapReturn(1, 12.03d);
        _ss.mapReturn(1, 12.03f);
        _ss.mapReturn(1, "G");

        //char
        _ss.mapReturn('a',1);
        _ss.mapReturn('b', 'c');
        _ss.mapReturn('c', Long.MAX_VALUE);
        _ss.mapReturn('d', 12.03d);
        _ss.mapReturn('e', 12.03f);
        _ss.mapReturn('f', "G");

        _ss.mapReturn( _intExpr.of("0xDEAD").ast(), 1 );
        _ss.mapReturn( _intExpr.of("0xBEEF").ast(), 2 );
        System.out.println( _ss);
    }


    public void testMapVars(){
        _switchStmt _ss = _switchStmt.of("a");
        _ss.mapReturn(1, "A");
        _ss.mapReturn(2, "A");
        _ss.mapReturn(3, "B");

        _switchStmt _ss2 = _switchStmt.of("a");
        _ss2.mapCode(1, _returnStmt.ofString("A"));
        _ss2.mapCode(2, _returnStmt.ofString("A"));
        _ss2.mapCode(3, _returnStmt.ofString("B"));

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
                _ss.mapReturn(k, v);
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
        _switchCases _cg = _switchCases.of();
        _s.addCaseGroups( _cg);
        assertTrue( _s.listCaseGroups().isEmpty() ); //adding empty caseGroup to empty switch

        _cg.addCase(1);
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

        assertEquals( _stmt.of( ()-> System.out.println(4)), _cg.getStatement(0));
        assertEquals( _stmt.of( ()-> System.out.println(5)), _cg.getStatement(1));
        assertEquals( _stmt.of( ()-> System.out.println(6)), _cg.getStatement(2));
        assertEquals( _stmt.of( ()-> System.out.println(7)), _cg.getStatement(3));
        assertEquals( _stmt.of( ()-> System.out.println(8)), _cg.getStatement(4));

        assertTrue( _cg.hasCase(1));
        assertFalse( _cg.hasCase(2));

        //ok, lets add some case constants
        _cg.addCase(2);
        _cg.addCase(3);
        _cg.addCase(4);
        _cg.addCase(5);
        _cg.addCase(6);

        //verify they have 'em
        assertTrue( _cg.hasCase(2));
        assertTrue( _cg.hasCase(3));
        assertTrue( _cg.hasCase(4));
        assertTrue( _cg.hasCase(5));
        assertTrue( _cg.hasCase(6));

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

        List<_switchCases> _cgs = cg.listCaseGroups();
        assertEquals( 3, _cgs.size());

        _cgs.get(0).addCase(5);

        assertTrue( _cgs.get(0).listCases().size()== 5);

        assertTrue( _cgs.get(0).hasCase(1));
        assertTrue( _cgs.get(0).hasCase(2));
        assertTrue( _cgs.get(0).hasCase(3));
        assertTrue( _cgs.get(0).hasCase(4));
        assertTrue( _cgs.get(0).hasCase(5)); //had an issue with comments
        //assertTrue(  _cgs.get(0).isPassthru() );

        assertTrue( _cgs.get(1).hasCase(6));
        assertTrue( _cgs.get(1).hasCase(7));

        //assertTrue( _cgs.get(2).hasCaseConstant(new IntegerLiteralExpr(9)));

        assertTrue( _cgs.get(2).isDefault());
        assertTrue( _cgs.get(2).listCases().size() == 0);

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
        _switchCase def = _s.getDefault();
        assertNotNull(def);

        //sets the default for the switch
        _s.setDefault("System.out.println(1);");
        System.out.println(_s);
        _s.setDefault("System.out.println(1); System.out.println(2);");
        System.out.println(_s);
        assertEquals (1, _s.listSwitchEntries(_se -> _se.isDefault() ).size());

        assertFalse( _s.getSwitchEntry(0).hasCaseConstant(0));
    }

}
