package org.jdraft.proto;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.proto.$stmt;
import org.jdraft.proto.$body;
import org.jdraft.Stmt;
import org.jdraft._body;
import org.jdraft._method;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SbodyTest extends TestCase {

    public void testFlattenStmts(){
        $stmt $s = $stmt.of( ()-> {
            System.out.println(1);
            $A:{
                System.out.println(2);
            }
            System.out.println(3);
        });
        System.out.println( "HIDE" + $s.construct("A", false) );
        System.out.println( "SHOW" + $s.construct("A", true) );
        System.out.println( "OVERRIDE WITH ASSERT" + $s.construct("A", "assert(true);") );
        System.out.println( "OVERRIDE WITH MULTIPLE STMTS" + $s.construct("A", Stmt.block( "assert(true);", "assert(1==1);") ));
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
        assertEquals(2, $body.notImplemented().count(FFF.class));
    }
    
    public void testMatchEmptyBody(){
        
        assertTrue( $body.of("{}").isImplemented);
        
        assertTrue( $body.of(";").matches(";") );
        assertTrue( $body.of("{}").matches("{}"));
        assertFalse( $stmt.of("{}").matches("{ assert true; }"));        
        
        assertFalse( $body.of("{}").matches("{ assert(true); }"));
    }
    
    public void testAnyConstruct(){
        _body _b = $body.any().construct();
        System.out.println("BODY " + _b );
        assertFalse( _b.isImplemented() );
        
        _b = $body.of().construct("$body", "{System.out.println(1);}");
    }
    
    public void testComposeWith$Label(){        
        $body $b = $body.of( ()->{
            $label: System.out.println(2);
        });
        
        //SHOW
        _body _bd = $b.construct( "label", true );
        System.out.println( _bd );
        assertTrue( $stmt.of("System.out.println(2);").matches( _bd.getStatement(0)) );
        
        //HIDE
        _bd = $b.construct();
        assertTrue( _bd.isEmpty());
        
        //OVERRIDE
        _bd = $b.construct( "label", Stmt.of("assert true;") );
        assertTrue( $stmt.of("assert true;").matches( _bd.getStatement(0)) );                
    }
    
    public void testCompose$LabelBlock(){
        $body $b = $body.of(()->{
           $block: {} 
        });
        
        //SHOW
        _body _bd = $b.construct( "block", true );
        assertTrue( _bd.isEmpty() ) ; //$stmt.of("{}").matches( _bd.getStatement(0)) );
        
        //HIDE
        _bd = $b.construct();
        assertTrue( _bd.isEmpty());
        
        //OVERRIDE (with single statement)
        _bd = $b.construct( "block", Stmt.of("assert true;") );
        assertTrue( $stmt.of("assert true;").matches( _bd.getStatement(0)) );                
        
        //OVERRIDE (with block statement)
        _bd = $b.construct( "block", Stmt.of("{ a(); b(); }") );

        assertTrue( $stmt.of("a();").matches( _bd.getStatement(0)) );
        assertTrue( $stmt.of("b();").matches( _bd.getStatement(1)) );

        //assertTrue( $stmt.of("a();").matches( _bd.getStatement(0).asBlockStmt().getStatement(0)) );
        //assertTrue( $stmt.of("b();").matches( _bd.getStatement(0).asBlockStmt().getStatement(1)) );
        
    }
    
    public void testBodyLabeledStmt(){
        $body b = $body.of(()->{
           $label: System.out.println( "Hey"); 
        });
        _body _b = b.construct("label", true);
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
        } ).$(Stmt.of("assert true;"), "meat");
        
        System.out.println( $b.bodyStmts.stmtPattern );
        
        //test 1 meat
        assertTrue( $b.matches("System.out.println(1);", "meat();", "System.out.println(2);")); 
        //test 2 meat
        assertTrue( $b.matches("System.out.println(1);", "meat();","meat();","System.out.println(2);")); 
        
        System.out.println( $b.select("System.out.println(1);", "meat();","meat();","System.out.println(2);").args);
        //test no meat
        assertTrue( $b.matches("System.out.println(1);", "System.out.println(2);"));                 
    }
    
    
    
    
}
