package org.jdraft.pattern;

import org.jdraft.*;

import java.io.IOException;
import java.net.URISyntaxException;
import junit.framework.TestCase;
import org.jdraft.macro._toCtor;

public class SconstructorTest extends TestCase {

    public void testConstructWithStmtEx(){

    }

    public void testConstructorMatch() {
        $constructor $ct = $constructor.of(new Object() {
            @_toCtor
            void C(int i) throws IOException {
                System.out.println(i);
            }
        });

        assertTrue($ct.matches("C(int i) throws IOException {",
                "    System.out.println(i);",
                "}")
        );

        assertTrue($ct.matches("C(int i) throws IOException {",
                "    System.out.println(i);",
                "    System.out.println(2);",
                "}")
        );
        assertTrue($ct.matches("public C(@Ann final int i) throws IOException, FileNotFoundException {",
                "    System.out.println(i);",
                "    System.out.println(2);",
                "}")
        );
    }
    public void testConstructorAsMatch(){
        $constructor $ct = $constructor.as( new Object(){
            @_toCtor void C(int i) throws IOException {
                System.out.println(i);
            }
        });

        assertTrue( $ct.matches("C(int i) throws IOException {",
                "    System.out.println(i);",
                "}"));

        assertFalse( $ct.matches("C(int i) throws FileException, IOException {",
                "    System.out.println(i);",
                "}"));

        assertFalse( $ct.matches("public C(int i) throws IOException {",
                "    System.out.println(i);",
                "}"));

        assertFalse( $ct.matches("C(final int i) throws IOException {",
                "    System.out.println(i);",
                "}")
        );

        assertFalse( $ct.matches("@Ann C(int i) throws IOException {",
                "    System.out.println(i);",
                "}")
        );
    }

    public void testConstructorParam(){
        $constructor $ct = $constructor.of(new Object(){
            @Deprecated
            @_toCtor private void TT(int name) throws IOException{}
        });

        System.out.println( $ct.parameters );
    }

    public void testConstructorAnnoParameter(){
        $constructor $ct = $constructor.of( new Object(){
            @_toCtor public void c(int x){
                this.x = x;
            }
            int x;
        }).$("x", "pName");

        System.out.println( $ct.parameters );

        System.out.println( $ct );
    }
    public void testConstructorLambda(){
        $constructor $c = $.constructor(); //c->!c.listStatements().isEmpty());

        assertTrue($c.matches(_constructor.of("C(){System.out.println(1);}")));
    }

    //what if I allowed the passing in of Classes
    //if the class is an annotation add it to annotations
    //if the class is a throwable, add it to ...
    public void testConstructOf(){
        $constructor $ct = $constructor.of($annoRef.of( Deprecated.class ),
            $modifiers.of( _modifier.PRIVATE ),
            $name.of("TT"),
            $throws.of( IOException.class ), 
            $body.of("{}"), 
            $parameters.of("$type$ $name$")
        );
        
        class TT{
            @Deprecated
            private TT(int name) throws IOException {}
            
            public TT(){}
        }
        
        assertEquals( 1, $ct.countIn(TT.class));
        
        $ct = $constructor.of(new Object(){
            @Deprecated
            @_toCtor private void TT(int name) throws IOException{}
        });
        
        assertEquals( 1, $ct.countIn(TT.class));
        
        //here lets build peice by peice
        $ct = $constructor.of();
        assertEquals( 2, $ct.countIn(TT.class));
        $ct.$anno(Deprecated.class);
        assertEquals( 1, $ct.countIn(TT.class));
        
    }
    
    public void testConstructConsistentArg(){
        $constructor $ct = $constructor.of( 
            $parameters.of("int $n$"),
            $body.of("this.$n$ = $n$;") );
                
        class FF{
            int i;
            private FF(int i) throws IOException{ this.i = i; }                        
        }
        assertEquals( 1, $ct.countIn(FF.class));
        
        //verify that if it's inconsistent, doesnt match        
        class GG{
            int i;
            private GG(int notMatch) throws IOException{ this.i = notMatch; }                        
        }
        assertEquals( 0, $ct.countIn(GG.class));
    }
    
    public void testConsistentManyProto(){
        
        //a "consistent" arg $name$ that appears in the $parameters & $body 
        $constructor $ct = $constructor.of(new Object(){            
            @Deprecated
            @_toCtor private void RR(int $name$) throws IOException{ this.$name$ = $name$; }            
            int $name$;
        });
        
        class RR{
            int i;
            @Deprecated
            private RR(int i) throws IOException{ this.i = i; }                        
        }        
        assertEquals( 1, $ct.countIn(RR.class));
    }
    
    public void testConstruct(){
        $constructor $ct = $constructor.of( $body.empty() );
        class c{
            c(){}
            c(int i){} //count this one
            c(String s){ assert(true); } //dont count
        }
        assertEquals(2, $ct.countIn(c.class));
        
        
        $ct = $constructor.of( $annoRef.of(Deprecated.class) );
        class d{
            @Deprecated
            d(){}
            
            @Deprecated
            d(int i){ }
            
            @Deprecated
            void m(){
                //this shouldnt count, because it's a method not a constructor
            }
        }
        assertEquals(2, $ct.countIn(d.class));
        
        $ct = $constructor.of( $annoRef.of(Deprecated.class), $body.of("{}") );
        class e{
            @Deprecated
            e(){}
            
            @Deprecated
            e(int i){ }
            
            @Deprecated
            void e(){
                //this shouldnt count, because it's a method not a constructor
            }
        }
        assertEquals( 2, $ct.countIn(e.class));
        
        $ct = $constructor.of( $throws.of(IOException.class) );
        class F{
            F() throws IOException {} //Yes            
            F(int i) throws URISyntaxException, IOException{} //Yes            
            F( String s) throws URISyntaxException{} //NO            
            void F(){} //NO
        }
        assertEquals( 2, $ct.countIn(F.class));
    }
    
    public void testCT(){
        $constructor $c = $constructor.of( 
            $annoRef.of(Deprecated.class),
            $body.of("{}"), 
            $name.of(s -> s.startsWith("a")) );
        
        class aaaa{
            @Deprecated
            aaaa(){}
        }
        
        assertEquals(1, $c.countIn(aaaa.class));
        
    }
    
    public void testAny(){
        class c{
            c(){
                
            }
        }
        assertTrue( $.constructor().matches(_constructor.of("C(){}")));
        assertTrue( $.constructor().matches(_constructor.of("private C(){}")));
        _constructor _ct = _constructor.of("private C(){System.out.println(1);}");

        System.out.println( _ct );

        assertTrue($.constructor().isMatchAny());
        assertTrue($constructor.of().isMatchAny());
        assertTrue( $.constructor().matches(_ct));

        assertEquals(1, $constructor.of().listIn(c.class).size());
        assertTrue(! $constructor.of().selectFirstIn(c.class).hasParameters());
        assertTrue($constructor.of().selectFirstIn(c.class).isParameters("()"));
        assertTrue($constructor.of().selectFirstIn(c.class).isNamed("c"));
    }
    
    public class A extends RuntimeException {}
    public class B extends RuntimeException {}
    
    public void testThrowsMulti(){
        $constructor $ct = $constructor.of( new Object(){
            void m(int a) throws A, B{
                this.a = a;
            }
            int a;
        });
        
        assertTrue( $ct.matches(_constructor.of(new Object(){
              void m(int a) throws B, SconstructorTest.A {
                  this.a = a;
              }
              int a;
        })) );        
    }
    
    public void testNoArgConstructor(){
        $constructor $c = $constructor.of( "public $name$(){ assert(1==1); }" )
                .$(Stmt.of("assert(1==1);").toString(), "body");
        
        //TODO body doesnt match no body
        //assertTrue($c.matches(_constructor.of("public C(){}")));
        
        
    }
    
    /**
     * Verify
     */
    public void testCompareWithJavadocAndAnnotationsAndComments(){
        $constructor $c = $constructor.of( "public $name$(int x){ this.x = x; }" );

        assertTrue($c.matches(_constructor.of("public C(int x){ this.x = x;}")));
        assertTrue($c.matches(_constructor.of("/** Javadoc */ public C(int x){ this.x = x;}")));
        assertTrue($c.matches(_constructor.of("/** Javadoc */ @Deprecated public C(int x){ this.x = x;}")));
        assertTrue($c.matches(_constructor.of("public C(int x){ /* block comment*/ this.x = x;}")));
        assertTrue($c.matches(_constructor.of("/** Javadoc */ @Deprecated public C(int x){ /** jd comment */ this.x = x;}")));

        $c = $constructor.of( "/** JAVADOC $name$ */ public $name$(int x){ this.x = x; }" );
        assertTrue($c.matches(_constructor.of("/** JAVADOC C */ public C(int x){ this.x = x;}")));
        assertTrue($c.matches(_constructor.of("/** JAVADOC C */ public C(int x){ this.x = x;}")));
        assertTrue($c.matches(_constructor.of("/** JAVADOC C */ @Deprecated public C(int x){ this.x = x;}")));
        assertTrue($c.matches(_constructor.of("/** JAVADOC C */ public C(int x){ /* block comment*/ this.x = x;}")));
        assertTrue($c.matches(_constructor.of("/** JAVADOC C */ @Deprecated public C(int x){ /** jd comment */ this.x = x;}")));
    }

    /** I should partsMap to create stencils for each component
    public void testCompareAnySignature(){
        //almost anything
        $constructor $c =  $constructor.of("private $name$(int x){ assert(true); }")
                .$("private", "mods")
                .$("int x", "params")
                .$( Stmt.of("assert(true);"), "BODY");
        //$BODY(); //this means ANY BODY will do
        //$PARAMETERS();

        System.out.println( $c );


        assertTrue( $c.matches(_constructor.of("private Name(int x){ assert(true); }")));
        assertTrue( $c.matches(_constructor.of("public Name(int x){ assert(true); }")));
        assertTrue( $c.matches(_constructor.of("public Name(String x){ assert(true); }")));
        assertTrue( $c.matches(_constructor.of("public Name(String x){ System.out.println(1); }")));
    }
    */

    public void testBuildViaAnonymousClass(){
        $constructor $ct = $constructor.of(new Object() {
            int a; String name;

            @_toCtor public void ct(int a, String name ){
                this.a = a;
                this.name = name;
            }
        });

        //System.out.println( $ct.signatureStencil );
        //System.out.println( $ct.construct() );
        //System.out.println( $ct.construct() );

        _constructor _c1 =  $ct.draft();
        _constructor _c2 =  $ct.draft();
        
        assertEquals( _c1, _c2);
        
        //System.out.println( "START");
        assertTrue( _c1.is( _c2.toString() ));
        //System.out.println( "END");
        
        
        assertTrue(
            $ct.draft().is("public ct(int a, String name){",
            "this.a = a;",
            "this.name = name;",
            "}"));

        $ct = $constructor.of( new Object(){
            String s;
            /**
             * Some Javadoc
             */
            @Deprecated
            public void Ctor( ){
                this.s = "Some S";
            }
        });
        _constructor _ct = $ct.draft();
        //verify that the ANNOTATIONS and JAVADOC are transposed
        assertTrue( _ct.hasAnnoExpr(Deprecated.class));
        assertTrue( _ct.getJavadoc().getText().contains("Some Javadoc"));
    }

    public void testCtorLabels() {
        $constructor $c = $constructor.of(new Object() {
            public void C() {
                $label:
                System.out.println(12);
            }
        });

        assertTrue($c.draft().getBody().isEmpty());
        assertEquals($c.draft("label", true).getBody().getStatement(0),
                Stmt.of("System.out.println(12);"));
    }
    public void testCtorLabelForAddingCode(){
        $constructor $c = $constructor.of( new Object(){
            public void C(){
                $label:{}
            }
        });

        assertTrue($c.draft().getBody().isEmpty());
        assertEquals( Stmt.of("System.out.println(1);"),
            $c.draft("label", "System.out.println(1);").getBody().getStatement(0));

        //
        assertEquals( Stmt.of("System.out.println(1);"),
            $c.draft("label", Stmt.of("System.out.println(1);")).getBody().getStatement(0));

        //block Statement
        //assertEquals( Stmt.of("{ System.out.println(1); }"),
        assertEquals( Stmt.of("System.out.println(1); "),
                $c.draft("label", Stmt.of("System.out.println(1);")).getBody().getStatement(0));
            //$c.draft("label", Stmt.blockStmt("{ System.out.println(1); }")).getBody().getStatement(0));




        /*
        //Comment
        assertEquals( ,
                $c.compose("label", Ast.comment("/* hello "))
                        .getBody().getStatement(0));
                        */
    }



    public void testC(){
        //match any no arg CONSTRUCTORS
        $constructor $noArgNoBody = $constructor.of("$name$(){}");

        assertTrue( $noArgNoBody.matches(_constructor.of("name(){}") ));

        class Y{
            Y(){ }
        }
        _class _c = _class.of(Y.class);
        assertNotNull( $noArgNoBody.listSelectedIn(_c).get(0) );

        class Z{
            Z(){
            }
        }
        _c = _class.of(Z.class);
        assertTrue( $noArgNoBody.listSelectedIn(_c).size() == 1 );

        System.out.println( $noArgNoBody.listSelectedIn(_c) );

        assertTrue( $noArgNoBody.listSelectedIn(_c).get(0).tokens.is("name", "Z"));
        //$c.select(_constructor.of("public "))
    }

    public void testAnyParams(){
        $constructor $oneArgInit = $constructor.of("$ctorName$ ($type$ $name$){",
            "this.$name$ = $name$;",
            "}");

        assertTrue( $oneArgInit.matches(_constructor.of("A(String s){ this.s = s;}") ));

        assertTrue( $oneArgInit.select(_constructor.of("A(String s){ this.s = s;}") ).tokens.is("ctorName", "A") );
        assertTrue( $oneArgInit.select(_constructor.of("A(String s){ this.s = s;}") ).tokens.is("type", "String") );
        assertTrue( $oneArgInit.select(_constructor.of("A(String s){ this.s = s;}") ).tokens.is("name", "s") );
    }
}
