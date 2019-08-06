package org.jdraft.proto;

import org.jdraft.Stmt;
import org.jdraft._class;
import org.jdraft._constructor;
import org.jdraft._modifiers;
import java.io.IOException;
import java.net.URISyntaxException;
import junit.framework.TestCase;
import org.jdraft.macro._toCtor;


public class SconstructorTest extends TestCase {

    //what if I allowed the passing in of Classes
    //if the class is an annotation add it to annotations
    //if the class is a throwable, add it to ...
    public void testConstructOf(){
        $constructor $ct = $constructor.of($anno.of( Deprecated.class ), 
            $modifiers.of( _modifiers.PRIVATE ),
            $id.of("TT"),
            $throws.of( IOException.class ), 
            $body.of("{}"), 
            $parameters.of("$type$ $name$")
        );
        
        class TT{
            @Deprecated
            private TT(int name) throws IOException {}
            
            public TT(){}
        }
        
        assertEquals( 1, $ct.count(TT.class));
        
        $ct = $constructor.of(new Object(){
            @Deprecated
            @_toCtor private void TT(int name) throws IOException{}
        });
        
        assertEquals( 1, $ct.count(TT.class));
        
        //here lets build peice by peice
        $ct = $constructor.of();
        assertEquals( 2, $ct.count(TT.class));
        $ct.$anno(Deprecated.class);
        assertEquals( 1, $ct.count(TT.class));
        
    }
    
    public void testConstructConsistentArg(){
        $constructor $ct = $constructor.of( 
            $parameters.of("int $name$"),
            $body.of("this.$name$ = $name$;") );    
                
        class FF{
            int i;
            private FF(int i) throws IOException{ this.i = i; }                        
        }
        assertEquals( 1, $ct.count(FF.class));
        
        //verify that if it's inconsistent, doesnt match        
        class GG{
            int i;
            private GG(int notMatch) throws IOException{ this.i = notMatch; }                        
        }
        assertEquals( 0, $ct.count(GG.class));
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
        assertEquals( 1, $ct.count(RR.class));
    }
    
    public void testConstruct(){
        $constructor $ct = $constructor.of( $body.of("{}") );
        class c{
            c(){}
            c(int i){} //count this one
            c(String s){ assert(true); } //dont count
        }
        assertEquals(2, $ct.count(c.class));
        
        
        $ct = $constructor.of( $anno.of(Deprecated.class) );
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
        assertEquals(2, $ct.count(d.class));
        
        $ct = $constructor.of( $anno.of(Deprecated.class), $body.of("{}") );
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
        assertEquals( 2, $ct.count(e.class));        
        
        $ct = $constructor.of( $throws.of(IOException.class) );
        class F{
            F() throws IOException {} //Yes            
            F(int i) throws URISyntaxException, IOException{} //Yes            
            F( String s) throws URISyntaxException{} //NO            
            void F(){} //NO
        }
        assertEquals( 2, $ct.count(F.class));        
    }
    
    public void testCT(){
        $constructor $c = $constructor.of( 
            $anno.of(Deprecated.class), 
            $body.of("{}"), 
            $id.of(s -> s.startsWith("a")) );
        
        class aaaa{
            @Deprecated
            aaaa(){}
        }
        
        assertEquals(1, $c.count(aaaa.class));
        
    }
    
    public void testAny(){
        class c{
            c(){
                
            }
        }
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

        _constructor _c1 =  $ct.compose();
        _constructor _c2 =  $ct.compose();
        
        assertEquals( _c1, _c2);
        
        //System.out.println( "START");
        assertTrue( _c1.is( _c2.toString() ));
        //System.out.println( "END");
        
        
        assertTrue(
            $ct.compose().is("public ct(int a, String name){",
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
        _constructor _ct = $ct.compose();
        //verify that the ANNOTATIONS and JAVADOC are transposed
        assertTrue( _ct.hasAnno(Deprecated.class));
        assertTrue( _ct.getJavadoc().getContent().contains("Some Javadoc"));
    }

    public void testCtorLabels() {
        $constructor $c = $constructor.of(new Object() {
            public void C() {
                $label:
                System.out.println(12);
            }
        });

        assertTrue($c.compose().getBody().isEmpty());
        assertEquals($c.compose("label", true).getBody().getStatement(0),
                Stmt.of("System.out.println(12);"));
    }
    public void testCtorLabelForAddingCode(){
        $constructor $c = $constructor.of( new Object(){
            public void C(){
                $label:{}
            }
        });

        assertTrue($c.compose().getBody().isEmpty());
        assertEquals( Stmt.of("System.out.println(1);"),
            $c.compose("label", "System.out.println(1);").getBody().getStatement(0));

        //
        assertEquals( Stmt.of("System.out.println(1);"),
            $c.compose("label", Stmt.of("System.out.println(1);")).getBody().getStatement(0));

        //block Statement
        //assertEquals( Stmt.of("{ System.out.println(1); }"),
        assertEquals( Stmt.of("System.out.println(1); "),
            $c.compose("label", Stmt.block("{ System.out.println(1); }")).getBody().getStatement(0));




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

        assertTrue( $noArgNoBody.listSelectedIn(_c).get(0).args.is("name", "Z"));
        //$c.select(_constructor.of("public "))
    }

    public void testAnyParams(){
        $constructor $oneArgInit = $constructor.of("$ctorName$ ($type$ $name$){",
            "this.$name$ = $name$;",
            "}");

        assertTrue( $oneArgInit.matches(_constructor.of("A(String s){ this.s = s;}") ));

        assertTrue( $oneArgInit.select(_constructor.of("A(String s){ this.s = s;}") ).args.is("ctorName", "A") );
        assertTrue( $oneArgInit.select(_constructor.of("A(String s){ this.s = s;}") ).args.is("type", "String") );
        assertTrue( $oneArgInit.select(_constructor.of("A(String s){ this.s = s;}") ).args.is("name", "s") );
    }
}
