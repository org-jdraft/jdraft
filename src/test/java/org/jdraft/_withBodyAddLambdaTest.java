package org.jdraft;

import junit.framework.TestCase;
import org.jdraft.walk.Walk;

/**
 *
 * @author Eric
 */
public class _withBodyAddLambdaTest extends TestCase {
    
    public void testSetBodyLambdaTest(){
        String mm = "public void m(){}";
        _method _m = _method.of(mm);
        
        //set the body as a simple statemenet
        _m.setBody(()->System.out.println(1));        
        assertEquals( Stmt.of(()->System.out.println(1)), _m.getAstStatement(0));
        
        //set the body as a block statement
        _m = _method.of(mm);
        _m.setBody( ()-> {System.out.println(1); System.out.println(2);});
        
        //the block statement will set the entire body
        assertEquals( Stmt.of(()->System.out.println(1)), _m.getAstStatement(0));
        assertEquals( Stmt.of(()->System.out.println(2)), _m.getAstStatement(1));
        
        //ensure override existing body
        _m = _method.of(mm);
        _m.add(()->System.out.println("old"));
        
        assertEquals( Stmt.of(()->System.out.println("old")), _m.getAstStatement(0));
        
        //now setBody to override old
        _m.setBody( ()-> {System.out.println(1); System.out.println(2);});
        assertEquals( Stmt.of(()->System.out.println(1)), _m.getAstStatement(0));
        assertEquals( Stmt.of(()->System.out.println(2)), _m.getAstStatement(1));
        
        //testSetBodyWithReturn Statement
        _m = _method.of(mm);
        
        _m.setBody( (a)-> {System.out.println(a); return a;} );
        
        assertEquals( Stmt.of((a)->System.out.println(a)), _m.getAstStatement(0));
        assertEquals( Stmt.of("return a;"), _m.getAstStatement(1));
    }
    
    public void testAddSingleStatementLambdaBody(){
        _method _m = _method.of( new Object(){
            public void m(){
                System.out.println(1);
                assert(true);
                System.out.println(2);
            }
        });
        
        assertEquals(3, _m.getBody().astList().size());
        _m.add( ()->{System.out.println("Last");} );
        assertEquals(4, _m.getBody().astList().size());
        _m.add( (a)->{System.out.println("Last 2"+a);} );
        _m.add( (a,b)->{System.out.println("Last 3"+a+b);} );
        _m.add( (a,b,c)->{System.out.println("Last 4"+a+b+c);} );
        _m.add( (a,b,c,d)->{System.out.println("Last 5"+a+b+c+d);} );
        
        assertEquals(8, _m.getBody().astList().size());
        System.out.println(_m);           
    }
    
    public void testAddSingleStatementAtIndexLambdaBody(){
        _method _m = _method.of( new Object(){
            public void m(){                
            }
        });
        
        assertEquals(0, _m.getBody().astList().size());
        _m.add( 0, ()->System.out.println("A") );        
        _m.add( 0, ()->System.out.println("B") );
        _m.add( 0, ()->System.out.println("C") );
        _m.add( 0, ()->System.out.println("D") );
        _m.add( 0, ()->System.out.println("E") );
        
        
        assertEquals(5, _m.getBody().astList().size());
        assertEquals(Stmt.of(()->System.out.println("A")), _m.getAstStatement(4));
        assertEquals(Stmt.of(()->System.out.println("B")), _m.getAstStatement(3));
        assertEquals(Stmt.of(()->System.out.println("C")), _m.getAstStatement(2));
        assertEquals(Stmt.of(()->System.out.println("D")), _m.getAstStatement(1));
        assertEquals(Stmt.of(()->System.out.println("E")), _m.getAstStatement(0));
        
        assertTrue( _m.getBody().is(()-> {
           System.out.println("E");
           System.out.println("D");
           System.out.println("C");
           System.out.println("B");
           System.out.println("A");           
        }));
        
        System.out.println(_m);           
    }
     
    /**
     * Verify when I add statements via lambda
     */
    public void testAddMultiStatementsLambdaBody(){
        _method _m = _method.of( new Object(){
            public int m(){
                return 0;
            }
        });
        _m.add( ()->System.out.println(1) );
        _m.add( (a)-> {System.out.println(a); return a;});
        _m.add( (a,b)-> {System.out.println(a); System.out.println(b); return b;});
        _m.add( (a,b,c)-> {System.out.println(a); System.out.println(b); System.out.println(c); return c;});
        _m.add( (a,b,c,d)-> {System.out.println(a); System.out.println(b); System.out.println(c); System.out.println(d); return d;});
        
        assertEquals(16, _m.listAstStatements().size());
        
        System.out.println( _m);
    }
    
    public void testAddAtLabelLambda(){
        _method _m = _method.of( new Object(){
           public void m(){
               $label: {}
               System.out.println(1);
               $label2: {}
           } 
        });
        
        _m.addAt("$label", ()->{System.out.println("A");} );
        _m.addAt("$label2", ()->{System.out.println("B");} );

        //asdsad
        //_m.flattenLabel("$label");
        //_m.flattenLabel("$label2");

        Walk.flattenLabel( _m.node() , "$label");
        Walk.flattenLabel( _m.node(), "$label2");
        assertEquals(3, _m.statementCount() );
        
        assertEquals( Stmt.of(()->System.out.println("A")), _m.getAstStatement(0));
        assertEquals( Stmt.of(()->System.out.println(1)), _m.getAstStatement(1));
        assertEquals( Stmt.of(()->System.out.println("B")), _m.getAstStatement(2));
        System.out.println( _m );        
    }
    
}
