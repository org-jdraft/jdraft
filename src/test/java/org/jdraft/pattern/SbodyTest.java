package org.jdraft.pattern;

import org.jdraft.Ast;
import org.jdraft.Stmts;
import org.jdraft._body;
import org.jdraft._method;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SbodyTest extends TestCase {

    public void testBodyAs(){
        $body $b = $body.of();
        //_body _b = _body.of( ()-> System.out.println(1));
        //System.out.println( _body.of( ()-> System.out.println(1)) );
        //System.out.println( _body.of( ()-> {return 1;}) );
        //System.out.println( _body.of( (Object $name$)-> System.out.println($name$)) );

        assertTrue($b.matches( _body.of(()-> System.out.println(1)) ));
        assertTrue($b.matches( _body.of(()-> {
            System.out.println(1);
            return 3;
        }) ));

        $b = $body.as(); //not implemented body
        assertTrue( $b.matches( Ast.methodDeclaration("void m();")));
        assertFalse( $b.matches( Ast.methodDeclaration("void m(){}"))); //it DOENST match an Empty body

        assertTrue( $body.as("{}").matches( Ast.methodDeclaration("void m(){}"))); //THIS matches an Empty body
        assertFalse( $body.as("{}").matches( Ast.methodDeclaration("void m();"))); //Doesnt match unimplemented

        $b = $body.as( ()-> System.out.println( 1) );
        assertTrue( $b.matches(_body.of(()->System.out.println(1) )));
        assertFalse( $b.matches(_body.of(()->System.out.println(2) )));

        $b = $body.as( (String $name$)-> System.out.println( $name$) );
        assertTrue( $b.matches(_body.of(()->System.out.println(1) )));
        assertTrue( $b.matches(_body.of(()->System.out.println(2) )));
        assertTrue( $b.matches(_body.of(()->System.out.println("eric") )));

        _body _b = _body.of(
                ()->{
                    System.out.println("eric");
                    assert(true);
                } );
        System.out.println( _b );
        assertFalse( $b.constraint.test( _body.of("assert(true);") ) );
        assertFalse( $b.constraint.test( _body.of("assert(true);") ) );

        assertFalse( $b.matches(_body.of(()->{
            System.out.println("eric");
            assert(true); //this extra statement will make it NOT match
        } )));

    }


    public void test$BodyLambda(){

        $body $b = $.body( ()->{
           System.out.println(1);
           System.out.println(2);
        } );
        $b.draft();

        $b = $.body( (a, b,c)->{
           System.out.println(a);
           System.out.println(b);
           System.out.println(c);
        });

        _body _bd = $b.draft();
        /*
        assertEquals( Stmt.of((a)->System.out.println(a)), _bd.getStatement(0));
        assertEquals( Stmt.of((b)->System.out.println(b)), _bd.getStatement(1));
        assertEquals( Stmt.of((c)->System.out.println(c)), _bd.getStatement(2));
        */
    }
    public void testFlattenStmts(){
        $stmt $s = $stmt.of( ()-> {
            System.out.println(1);
            $A:{
                System.out.println(2);
            }
            System.out.println(3);
        });
        System.out.println( "HIDE" + $s.draft("A", false) );
        System.out.println( "SHOW" + $s.draft("A", true) );
        System.out.println( "OVERRIDE WITH ASSERT" + $s.draft("A", "assert(true);") );
        System.out.println( "OVERRIDE WITH MULTIPLE STMTS" + $s.draft("A", Stmts.blockStmt( "assert(true);", "assert(1==1);") ));
    }

    public void testFlatten$Body(){
        $body $b = $body.of( ()-> {
            System.out.println(1);
            $A:{
                System.out.println(2);
            }
            System.out.println(3);
        });

        System.out.println( $b.bodyStmts );

        /*
        System.out.println( "HIDE" + $b.construct("A", false) );
        System.out.println( "SHOW" + $b.construct("A", true) );
        System.out.println( "WITH ASSERT" + $b.construct("A", "assert(true);") );

        _body _b = $b.construct("A", false );

        System.out.println( _b );

        assertTrue( _b.is( ()->{
            System.out.println(1);
            System.out.println(3);
        }));

        _b = $b.construct("A", true );
        assertTrue( _b.is( ()->{
            System.out.println(1);
            System.out.println(2);
            System.out.println(3);
        }));
        System.out.println("THE BODY" + _b );
         */
    }

    static abstract class FFF{
        abstract void m();
        int v(){ return 1; }
        abstract String b(); 
    }
    
    public void testBodyNotImpl(){
        System.out.println( $body.notImplemented().listIn(FFF.class) );

        assertEquals(2, $body.notImplemented().countIn(FFF.class));
    }
    
    public void testMatchEmptyBody(){
        
        assertTrue( $body.of("{}").isImplemented);
        
        assertTrue( $body.of(";").matches(";") );
        assertTrue( $body.of("{}").matches("{}"));
        assertFalse( $stmt.of("{}").matches("{ assert true; }"));        
        
        assertFalse( $body.notImplemented().matches("{ assert(true); }"));
    }
    
    public void testAnyConstruct(){
        _body _b = $body.of().draft();

        //assertFalse( _b.isImplemented() );
        
        _b = $body.of().draft("$body", "{System.out.println(1);}");
        System.out.println("BODY " + _b );
    }
    
    public void testComposeWith$Label(){        
        $body $b = $body.of( ()->{
            $label: System.out.println(2);
        });
        
        //SHOW
        _body _bd = $b.draft( "label", true );
        System.out.println( _bd );
        assertTrue( $stmt.of("System.out.println(2);").matches( _bd.getStatement(0)) );
        
        //HIDE
        _bd = $b.draft();
        assertTrue( _bd.isEmpty());
        
        //OVERRIDE
        _bd = $b.draft( "label", Stmts.of("assert true;") );
        assertTrue( $stmt.of("assert true;").matches( _bd.getStatement(0)) );                
    }
    
    public void testCompose$LabelBlock(){
        $body $b = $body.of(()->{
           $block: {} 
        });
        
        //SHOW
        _body _bd = $b.draft( "block", true );
        //assertTrue( _bd.isEmpty() ) ; //$stmt.of("{}").matches( _bd.getStatement(0)) );
        
        //HIDE
        _bd = $b.draft();
        assertTrue( _bd.isEmpty());
        
        //OVERRIDE (with single statement)
        _bd = $b.draft( "block", Stmts.of("assert true;") );
        assertTrue( $stmt.of("assert true;").matches( _bd.getStatement(0)) );                
        
        //OVERRIDE (with block statement)
        _bd = $b.draft( "block", Stmts.of("{ a(); b(); }") );

        assertTrue( $stmt.of("{ a(); b();}").matches( _bd.getStatement(0)) );
        //assertTrue( $stmt.of("b();").matches( _bd.getStatement(1)) );

        //assertTrue( $stmt.of("a();").matches( _bd.getStatement(0).asBlockStmt().getStatement(0)) );
        //assertTrue( $stmt.of("b();").matches( _bd.getStatement(0).asBlockStmt().getStatement(1)) );
        
    }
    
    public void testBodyLabeledStmt(){
        $body b = $body.of(()->{
           $label: System.out.println( "Hey"); 
        });
        _body _b = b.draft("label", true);
        System.out.println( _b );
    }
    
    public void testNotImplemented(){
        $body $noImpl = $body.of(";"); //an "unimplemented" body        
        assertNotNull( $noImpl.select(_method.of("m();").getBody()) );
        
        $noImpl = $body.notImplemented();
        assertNotNull( $noImpl.select(_method.of("m();").getBody()) );        
    }
    
    public void testEmpty(){
        $body $empty = $body.of("{}"); //an "empty" body        
        assertTrue( $empty.matches("{       }"));
    }
    
    public void testSingleStatement(){
        $body $single = $body.of("{System.out.println(1);}");
        assertTrue( $single.matches("System.out.println(1);") ); 
        assertTrue( $single.matches("{System.out.println(1);}") ); 
        assertTrue( $single.matches("//comment ",
            "System.out.println(1);") ); 
        assertTrue( $single.matches("/* comment */",
            "System.out.println(1);") ); 
        
        assertTrue( $single.matches("/** comment **/",
            "System.out.println(1);") ); 

        //make it a variable prototype
        $single = $body.of("System.out.println($any$);");
        assertTrue( $single.matches("{ System.out.println(1); }") );
        assertTrue( $single.matches("{ System.out.println(i); }") );
        assertTrue( $single.matches("{ System.out.println(a + b); }") );
        assertTrue( $single.matches("System.out.println(a + b);") );     
        assertTrue( $single.matches("/** comment */ System.out.println(a + b);") );  
        
        assertNotNull( $single.select(()-> System.out.println(12)) );
        assertNotNull( $single.select(()-> { System.out.println(12); }) );
        assertNotNull( $single.select((a)-> { System.out.println(a); }) );        
        assertNotNull( $single.select((a, b)-> { System.out.println(a+" "+b); }) );
        assertNotNull( $single.select((a, b, c)-> { System.out.println(a+" "+b+" "+c); }) );
        assertNotNull( $single.select((a, b, c, d)-> { System.out.println(a+" "+b+" "+c+" "+ d); }) );
        assertNotNull( $single.select((a, b)-> { System.out.println(a+" "+b); }) );
        
        $single = $body.of("return $any$;");
        assertNotNull( $single.select((Integer a)-> { return a; })  );        
        assertNotNull( $single.select((Integer a,Integer b)-> { return a+b; })  );
        assertNotNull( $single.select((Integer a,Integer b, Integer c)-> { return a+b+c; })  );
        assertNotNull( $single.select((Integer a,Integer b, Integer c, Integer d)-> { return a+b+c+d; })  );
    }
    
    public void testMultiStatement(){
        $body $multi = $body.of(($any$)->{
            System.out.println($any$);
            System.out.println(12);
        });
        
        //assertTrue( $multi.matches()
    }
    
    public void testAnyMeatSandwich(){
        //so I need $statement("assert true;")
        $body $b = $body.of((a)-> {
            System.out.println(1);
            assert true;
            System.out.println(2);        
        } ).$(Stmts.of("assert true;"), "meat");
        
        System.out.println( $b.bodyStmts.stmtStencil);
        
        //test 1 meat
        assertTrue( $b.matches("System.out.println(1);", "meat();", "System.out.println(2);")); 
        //test 2 meat
        assertTrue( $b.matches("System.out.println(1);", "meat();","meat();","System.out.println(2);")); 
        
        System.out.println( $b.select("System.out.println(1);", "meat();","meat();","System.out.println(2);").tokens);
        //test no meat
        assertTrue( $b.matches("System.out.println(1);", "System.out.println(2);"));                 
    }
    
    
    
    
}
