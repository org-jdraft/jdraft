package org.jdraft.pattern;

import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.Tokens;
import org.jdraft.Ast;
import org.jdraft.Stmt;
import org.jdraft._body;
import org.jdraft._class;
import org.jdraft._method;
import org.jdraft.pattern.$stmts.Select;
import junit.framework.TestCase;

import java.util.*;

@Ast.cache
public class SsnipTest extends TestCase {

    public void testSnipAnyMatchesEmptyOrLongBlocks(){
        //assertTrue( $snip.any().matches( _method.of("void m();").getBody().ast() ));
        assertTrue( $stmts.of().matches( _method.of("void m(){}").getBody().ast() ));
        assertTrue( $stmts.of().matches(Stmt.assertStmt("assert(1==1)")));
        assertTrue( $stmts.of().matches(Stmt.breakStmt("break;")));
        assertTrue( $stmts.of().matches(Stmt.assertStmt("assert(1==1)")));
        
        assertTrue( $stmts.of().matches( _body.of(new Object(){
            void m(){
                int i=0;
                int j=1;
                assertTrue(1==1);
                label: assertTrue(2==2);
                System.out.println("Hello");
            }
        }).ast() ) );
        
    }

    public void testIfSnipTest(){

        String str = "if($name$.isLoggable(Level.FINER)){ $name$.fine($any$ + \"\"); }";
        $stmt $st = $stmt.of( str);
        System.out.println ( $st.draft("NAME", "LOG", "any", "\"message\"") );

        $stmts $s = $stmts.of( str );
        $s.draft("NAME", "LOG", "any", "\"message\"");

    }

    public void test$snip2$label(){
        $stmts $s = $stmts.of(new Object(){
           void m( String $any$ ){
               System.out.println( $any$ );
               $label: System.out.println( 111 );
           }
        });

        assertEquals(1, $s.draft("any", 1).size());
        assertEquals(2, $s.draft("any", 1, "$label", true).size());

        $s = $stmts.of( ()->{
           label: System.out.println(1);
        });

        System.out.println( $s.draft( "label", false));
        assertEquals( 1, $s.draft("label", true).size());
        assertEquals( 0, $s.draft("label", false).size());
    }

    public void testFillLabelWithSingleStatement(){
        $stmts $s = $stmts.of(() -> {
            $body:{ /** Dynamic code filled in here */ }
            System.out.println("Hello");
        });

        List<Statement> sts = $s.draft("$body", Stmt.of(()->System.out.println(1) ));
        assertEquals(2, sts.size());
        assertEquals( Stmt.of( ()->System.out.println(1) ), sts.get(0));
        assertEquals( Stmt.of( ()->System.out.println("Hello") ), sts.get(1));
    }

    public void testFillLabelWithMultipleStatements(){
        $stmts $s = $stmts.of(() -> {
            $body:{}
            System.out.println("Hello");
        });

        List<Statement> sts = $s.draft("$body", Stmt.of(()->{
            System.out.println(1);
            System.out.println(2);
        } ));
        assertEquals(3, sts.size());
        assertEquals( Stmt.of(()->System.out.println(1)), sts.get(0));
        assertEquals( Stmt.of(()->System.out.println(2)), sts.get(1));
        assertEquals( Stmt.of(()->System.out.println("Hello")), sts.get(2));
    }

    public void testDynamicLabel() {
        //class C{
        //    void f(){
        //        $doThis: System.out.println("Hello");
        //    }
        //}
        //$snip $s = $snip.of( _class.of(C.class).getMethod("f").getBody() );

        $stmts $s = $stmts.of(() -> {
            $doThis:
            System.out.println("Hello");
        });
        List<Statement> sts = $s.draft(new Tokens());
        //System.out.println(sts);
        assertEquals(0, sts.size());

        sts = $s.draft("$doThis", true);
        assertEquals(1, sts.size());
        assertEquals(Stmt.of(() -> System.out.println("Hello")), sts.get(0));
    }

    public void testMultiStatementLabel(){
        $stmts $s = $stmts.of( (String $name$, Integer $i$)-> {
            System.out.println($i$);
            $label : {
                System.out.println( 1 );
                System.out.println( $name$ );
            }
            System.out.println($name$);
        });

        List<Statement> sts = $s.draft( "NAME", "e", "i", 3, "$label", true);
        assertEquals( 4, sts.size());

        //I need to make $label for fill
        $s.fill(1, "eric" );
    }

    public void test$snipAvoidInfLoop(){
        $stmts $s = $stmts.of( (Object $i$)-> System.out.println($i$) );
        $stmts $r = $stmts.of( ($i$)-> { System.out.println($i$); System.out.println($i$);} );

        class D{
            void g(){
                System.out.println( "hey");
                System.out.println( "this" );
            }
        }
        _class _c = _class.of(D.class);
        $s.replaceIn(_c, $r);
        assertTrue( _c.getMethod("g").getBody().is( 
            "System.out.println(\"hey\");", 
            "System.out.println(\"hey\");", 
            "System.out.println(\"this\");",
            "System.out.println(\"this\");") );
        //System.out.println( _c );
        
        _c = _class.of(D.class);
        $stmts.of("System.out.println($i$);").replaceIn( _c, "{System.out.println($i$); System.out.println($i$);}" );
        //System.out.println( _c );

        assertTrue( _c.getMethod("g").getBody().is( 
            "System.out.println(\"hey\");", 
            "System.out.println(\"hey\");", 
            "System.out.println(\"this\");",
            "System.out.println(\"this\");") );
    }

    public void test$snipRep(){
        $stmts $s = $stmts.of( (Integer $i$)-> {
            assert $i$ > 0;
            System.out.println( $i$ );
        } );
        $stmts $dbl = $stmts.of( (Integer $i$)->{
            System.out.println( $i$ + $i$ );
            assert $i$ + $i$ > 0;
        } );

        class L{
            void m(){
                int i=3;
                assert i > 0;
                System.out.println( i );

                int j = 4;
                assert j > 0;
                System.out.println( j );
            }
        }
        _class _c = _class.of(L.class);

        $s.replaceIn( _c, $dbl );

        System.out.println( _c );
        /*
        $s.forSelectedIn(_c, s-> {
            List<Statement> sts = replacement.compose( s.tokens );

        });
        */
    }

    public void test$snipReplace(){
        $stmts $s = $stmts.of( (Integer $i$)-> {
            assert $i$ > 0;
            System.out.println( $i$ );
        } );
        class L{
            void m(){
                int i=3;
                assert i > 0;
                System.out.println( i );
            }
        }
        _class _c = _class.of(L.class);
        $s.forSelectedIn( _c, ($stmts.Select s) -> {
            //rearrage the order of the statements, first the println then the assert
            s.statements.get(0).replace( $s.$sts.get(1).draft(s.tokens) );
            s.statements.get(1).replace( $s.$sts.get(0).draft(s.tokens) );
        });
        assertTrue( _c.getMethod("m").getBody().getStatement(1) instanceof ExpressionStmt );
        assertTrue( _c.getMethod("m").getBody().getStatement(2) instanceof AssertStmt);
    }

    //create a snip of many statements, and verify it matches
    // a block of code with statements that have comments
    //a snip w/o comments will match Statements With comments
    public void test$snipMatchBlockComments(){
        $stmts $s = $stmts.of( (Integer $i$)-> {
            assert $i$ > 0;
            System.out.println( $i$ );
        } );

        System.out.println( $s );

        BlockStmt bs = (BlockStmt) Stmt.of( (Integer i)-> {
            /** comment 1 */
            assert 1 > 0;
            // comment 2
            System.out.println( 1 ); /* comment 3 */
        });
        $stmts.Select ss = $s.select( bs.getStatement(0 ) );

        assertNotNull( ss );
        assertTrue( ss.tokens.is("i", "1"));
        assertEquals( Stmt.of( ()-> {/** comment 1 */ assert 1 > 0;} ), ss.statements.get(0));
        assertEquals( Stmt.of( () -> {
            // comment 2
            System.out.println( 1 ); /* comment 3 */
        }),  ss.statements.get(1));
    }

    public void test$snipSelectStaticStatement() {
        //partsMap & match a static statement
        $stmts s = $stmts.of(() -> System.out.println(1));
        assertNotNull(s.select(Stmt.of("System.out.println(1);")));
        assertTrue(s.matches(Stmt.of("System.out.println(1);")));
    }

    public void test$snipSelectVarStatement() {
        //partsMap a variable statement
        $stmts s = $stmts.of( (Object $any$) -> System.out.println($any$));
        assertNotNull(s.select(Stmt.of("System.out.println(1);")));
        assertNotNull(s.select(Stmt.of("System.out.println(1);")));
        $stmts.Select tks = s.select(Stmt.of("System.out.println(1);"));
        assertTrue(tks.is("any", "1"));
    }

    public void test$snipSingleStmtSelect(){
        class F{
            public void m1(){
                System.out.println(1);
            }
            public void m2(){
                System.out.println(2);
            }
        }
        List<$stmts.Select> ss = $stmts.of( (Object $any$)-> System.out.println($any$) ).listSelectedIn( _class.of(F.class) );
        assertEquals(2, ss.size());
        assertEquals( ss.get(0).statements.get(0), Stmt.of( ()-> System.out.println(1)));
        assertEquals( ss.get(1).statements.get(0), Stmt.of( ()-> System.out.println(2)));
        assertTrue( ss.get(0).tokens.is("any", "1"));
        assertTrue( ss.get(1).tokens.is("any", "2"));

        //verify we can match Statements EVEN WITH COMMENTS
        class G{
            public void m1(){
                // comment
                System.out.println(1);
            }
            public void m2(){
                /* comment */
                System.out.println(2);
            }
            public void m3(){
                /** comment */
                System.out.println(3);
            }
        }
        ss = $stmts.of( (Object $any$)-> System.out.println($any$) ).listSelectedIn( _class.of(G.class) );

        assertEquals(3, ss.size());
        assertTrue( $stmt.of( Stmt.of(()-> System.out.println(1) )).matches( ss.get(0).statements.get(0) ));
        assertTrue( $stmt.of( Stmt.of(()-> System.out.println(2) )).matches( ss.get(1).statements.get(0)));
        assertTrue( $stmt.of( Stmt.of(()-> System.out.println(3) )).matches( ss.get(2).statements.get(0)));

        assertTrue( $stmt.of( ()-> System.out.println(1) ).matches( ss.get(0).statements.get(0)));
        assertTrue( $stmt.of( ()-> System.out.println(2) ).matches( ss.get(1).statements.get(0)));
        assertTrue( $stmt.of( ()-> System.out.println(3) ).matches( ss.get(2).statements.get(0)));
        
        assertTrue( ss.get(0).tokens.is("any", "1"));
        assertTrue( ss.get(1).tokens.is("any", "2"));
        assertTrue( ss.get(2).tokens.is("any", "3"));

    }

    public void test$snipMultiVarMultiStatement(){
        $stmts s = $stmts.of( ($a$, $b$, $c$, $d$)-> {
            System.out.println(" $a$ "+$a$);
            System.out.println(" $b$ "+$b$);
            System.out.println(" $c$ "+$c$);
            System.out.println(" $d$ "+$d$);
        });

        List<Statement> sts = s.fill(1,2,3,4);
        System.out.println( sts );

        assertEquals( sts.get(0), Stmt.of(()-> System.out.println(" 1 "+1)));
        assertEquals( sts.get(1), Stmt.of(()-> System.out.println(" 2 "+2)));
        assertEquals( sts.get(2), Stmt.of(()-> System.out.println(" 3 "+3)));
        assertEquals( sts.get(3), Stmt.of(()-> System.out.println(" 4 "+4)));

        BlockStmt bs = new BlockStmt();
        sts.forEach(st -> bs.addStatement(st));

        //verify I can partsMap the List of statements and get the variables out
        //across
        Select ts = s.select( bs.getStatement(0) );
        assertTrue( ts.is("a", "1"));
        assertTrue( ts.is("b", "2"));
        assertTrue( ts.is("c", "3"));
        assertTrue( ts.is("d", "4"));
    }

    public void test$snipDecomposeStaticMultiStatements(){
        //static
        $stmts s = $stmts.of( ()-> {System.out.println(1); assert(true);});
        BlockStmt bs = Stmt.blockStmt("{System.out.println(1); assert(true);}");
        assertNotNull(s.select(bs.getStatement(0)) );
    }

    public void test$snipDecomposeStaticVarMultiStatements(){
        $stmts s = $stmts.of( (Object $any$, Boolean $expr$)-> {System.out.println($any$); assert($expr$);});
        BlockStmt bs = Stmt.blockStmt("{System.out.println(1); assert(true);}");
        Select tks = s.select(bs.getStatement(0));
        assertNotNull(tks);
        assertTrue(tks.is("any", "1") && tks.is("expr", "true") );
    }

    public void test$snip(){
        //single statement
        $stmts $s = $stmts.of("System.out.println($any$);");
        List<Statement> ls =  $s.fill(1);
        assertEquals(Stmt.of("System.out.println(1);"), ls.get(0));

        $s = $stmts.of( (Object $any$) -> System.out.println($any$) );
        ls =  $s.fill(1);
        assertEquals(Stmt.of("System.out.println(1);"), ls.get(0));
    }

    public void test$snipPostParameterize(){
        $stmts $s = $stmts.of( ()->System.out.println( 4 + 5 ));
        $s.$("4 + 5", "BODY"); //here we parameterize 4 + 5
        assertEquals( Stmt.of("System.out.println(1);"), $s.fill(1).get(0));
    }

    public void test$snippetLocal(){
        assertEquals(Stmt.of("System.out.println(\"Eric\");"),
                $stmts.of((Object $any$)->System.out.println($any$)).fill("\"Eric\"").get(0));
        assertEquals( Stmt.of("assert(1==1);"), $stmts.of((Boolean $any$)-> {assert($any$);}).fill("1==1").get(0) );
    }

    //simple snippets that are represented as lambdas
    public static final $stmts $s = $stmts.of( (Object $any$)-> System.out.println($any$));
    public static final $stmts $s2 = $stmts.of((Object $x$, Object $y$)-> System.out.println( "$x$ is "+$x$+ " $y$ is "+$y$));
    public static final $stmts $s3 = $stmts.of(($x$, $y$, $z$)-> System.out.println( "$x$ is "+$x$+ " $y$ is "+$y$+" $z$ is "+$z$));
    public static final $stmts $s4 = $stmts.of(($a$, $b$, $c$, $d$)-> System.out.println( "$a$ is "+$a$+ " $b$ is "+$b$+" $c$ is "+$c$+ " $d$ is "+$d$));

    //the PARAMETERS can have types
    public static final $stmts $s5 = $stmts.of((Integer $a$, String $b$, Map $c$, UUID $d$)-> System.out.println( "$a$ is "+$a$+ " $b$ is "+$b$+" $c$ is "+$c$+ " $d$ is "+$d$));


    public void test$snippetLambda(){
        System.out.println( $stmts.of( (Object $any$)->System.out.println($any$) ).fill(1).get(0));
        System.out.println( $stmts.of( (Boolean $any$)->{ assert($any$); } ).fill(true).get(0));

        System.out.println( $s.fill("1==1").get(0));
        System.out.println( $s2.fill(100,200).get(0));
        System.out.println( $s3.fill(100,200,300).get(0));
        System.out.println( $s4.fill(100,200,300,400).get(0));
        System.out.println( $s5.fill(100,200,300,400).get(0));
    }

}
