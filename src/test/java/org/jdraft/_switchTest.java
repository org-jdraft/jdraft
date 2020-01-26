package org.jdraft;

import junit.framework.TestCase;

import java.util.List;

public class _switchTest extends TestCase {

    public void testBuildEmptyCaseGroupsAndMutate(){
        //empty switch, no caseGroups
        assertTrue( _switch.of().listCaseGroups().isEmpty() );

        //build and empty switch
        _switch _s = _switch.of();
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
        _switch cg = _switch.of( (Integer dayOfWeek)-> {
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
        _switch _s = _switch.of( (Integer key)-> {
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
