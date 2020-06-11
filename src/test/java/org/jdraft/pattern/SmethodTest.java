package org.jdraft.pattern;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.jdraft.*;
import org.jdraft.macro.*;
import org.jdraft.runtime.*;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmethodTest extends TestCase {

    public void testLabeledStatementList(){
        //$fields: m.put("$name$", $name$);
        $method $toMap = $method.of( new Object() {
            public Map<String,Object> toMap(){
                Map m = new HashMap<>();
                $fields: System.out.println($fields$);
                return m;
            }
            Object $fields$, $name$;
        });

        //OK here this should just NOT print
        _method _m = $toMap.draft();
        assertEquals(2, _m.listAstStatements().size());

        _m = $toMap.draft("fields", false);
        assertEquals(2, _m.listAstStatements().size());



        System.out.println(_m);
        List<_field> _fs = _field.of(Ast.fieldDeclaration("int x,y;"));
    }

    public void testMethodAsMatch(){
        $method $m = $method.as(new Object(){
            void m(){}
        });
        assertTrue( $m.matches("void m(){}"));
        assertFalse($m.matches( "@Ann void m(){}"));
        assertFalse($m.matches( "void ma(){}"));
        assertFalse($m.matches( "int m(){}"));
        assertFalse($m.matches( "void m(int g){}"));
        assertFalse($m.matches( "void m(){ System.out.println(1); }"));

        $m = $method.as(new Object(){
            int m(int...$var$){
                return 1;
            }
        });

        assertTrue( $m.matches("int m(int...is){ return 1; }") );

        assertFalse( $m.matches("int m(int is){ return 1; }") );
        assertFalse( $m.matches("int m(int...is){ return 2; }") );
        assertFalse( $m.matches("public int m(int...is){ return 1; }") );
        assertTrue( $m.matches("int m(int...v){ return 1; }") );

    }


    public void testMethod(){
        //@_$({"X", "Name", "x", "name"})
        $method $m = $method.of( new Object(){
            @_static @Deprecated
            public int getX(){
                return x;
            }
                int x;
        }).$("X", "Name", "x", "name");

        //System.out.println( $m );
        //verify we parameterized the name ("X") to parameter ("Name")
        assertEquals($m.name.nameStencil, $name.of("get$Name$").nameStencil);
        //verify we parameterized the body ("x") to parameter ("name")
        assertEquals( $body.of("{ return $name$; }").bodyStmts.stmtStencil, $m.body.bodyStmts.stmtStencil);

    }

    public void testLikeDOM(){
        class A{
            public int doMethod(){
                return 3;
            }
            public String anotherMethod(){
                return "eric";
            }
        }
        _type mod = $.method().forEachIn(A.class, m-> m.add(0,
                $stmt.of( ()-> System.out.println("Calling: $name$") ).fill(m.getName()).ast() ) );

        System.out.println( mod );
    }

    public void testListIn(){
        class A{  int a; }
        class B{ int b; }
        class C{ int c; }
        List<_field> _fs = $.field().listIn(A.class, B.class, C.class);
        assertEquals( 3, _fs.size());
        List<_class> _cs = new ArrayList<>();
        _cs.add( _class.of(A.class));
        _cs.add( _class.of(B.class));
        _cs.add( _class.of(C.class));

        _fs = $.field().listIn(_cs);
        assertEquals( 3, _fs.size());

    }

    public void testAnonymousObj(){
        $method $m = $.method( new Object(){
            public $type$ get$Name$(){
                return this.$name$;
            }

            class $type${}
            $type$ $name$;
        });

        _method _m = $m.draft("name", "x", "type", int.class);
        //System.out.println( _m );
        assertTrue( $m.matches( _m) );
        class EX{
            String s;
            float f;
            public String getS(){
                return this.s;
            }
            public float getF(){
                return this.f;
            }
        }
        assertEquals( 2, $m.countIn(EX.class) );
    }

    public void testModifiers(){
        String name = "datumTest";
        $method $m = $method.of().$name(name).$modifiers("public").$type("void").$body(b->b.isImplemented());
        class F{
            public void datumTest(){
                System.out.println( "hello");
            }
        }
        assertEquals(1, $m.countIn(F.class));
    }

    private $method $computeRange = $method.of(new Object(){
        public int $name$(){
            int result = 1;
            for(int i=$from$;i<$to$;i++){
                result = result * i;
            }
            return result;
        }        
        int $from$, $to$;
    }).$("*", "op");        
    public void test4Draft(){
        
        //System.out.println( $computeRange.body.bodyStmts );
        //System.out.println( $computeRange.body.list$() );
        //System.out.println( "IMPLD" + $computeRange.body.isImplemented );
        //System.out.println( $computeRange.body.list$Normalized() );
        //System.out.println( "STENCIL " + $computeRange.body.bodyStmts.stmtPattern.list$Normalized() );
        //System.out.println( $computeRange.list$() );
        assertEquals( 4, $computeRange.$list().size());
        //System.out.println( $computeRange.list$Normalized() );
        assertEquals( 4, $computeRange.$listNormalized().size());
        //System.out.println($computeRange.fill("multiply10to20",10,20,"*"));
    }
    
    public void testMethodCtor(){
        $method $m = $method.of( $annoRef.of(Deprecated.class) );
        $m.$annos(t -> t.size() == 1);
    }
    
    /**
     * We need to make sure we can compose when we have a method 
     * that has anyBody() 
     * it will match implemented "m(){}" -OR- not implemented "m();"
     *  
     * 
     * to do so, we have to make a decision when composing the method to 
     * default to notImplemented "m();"
     */
    public void testComposeAnyBody(){
        $method $m = $method.of(new Object(){
           void m(){}
        }).anyBody(); 
       
        //verify that when I 
        assertTrue( 
            $body.of("{}").matches( $m.draft().getBody() ));
    }
    
    public void testAnonymousObjectInit(){
        $method $set = $method.of( new Object(){
            public void set$Name$($type$ $name$){
                this.$name$ = $name$;
            }
            private $type$ $name$;
            class $type${}
        });
        
        class Point{
            int x, y, z;
            
            public void setX(int x){
                this.x = x;
            }
        }
        assertTrue( $set.selectFirstIn(Point.class)
            .is("name", "x", "type", int.class));
        
        _class _c = _class.of(Point.class);
        
        //use the $set method prototype to create/add methods to a _class 
        _c.add($set.draft("name", "y", "type", int.class),
               $set.draft("name", "z", "type", int.class));
        
        //lets create a getter prototype
        $method $get = $method.of( new Object(){
            public $type$ get$Name$(){
                return this.$name$;
            }            
            $type$ $name$;
            class $type${}            
        });
        //lets add getter methods for x,y, and z
        _c.add( $get.draft("name", "x", "type", int.class),
                $get.draft("name", "y", "type", int.class),
                $get.draft("name", "z", "type", int.class) );
        
        //use the prototypes AGAIN to query into the _class
        assertEquals(3, $set.listIn(_c).size()); //verify we have (3) set methods
        assertEquals(3, $get.listIn(_c).size()); //verify we have (3) get methods
        
        //Lets make a working class and use it to verify it works
        //compile and create a new instance of Point
        _proxy _p = _runtime.proxyOf(_c);
        //lets call the set methods        
        _p.set("x", 100).set("y", 200).set("z",300);
        
        //verify the values are set & the getter methods work
        assertEquals( _p.get("x"), 100);
        assertEquals( _p.get("y"), 200);
        assertEquals( _p.get("z"), 300);        
    }
    
    /**
     * Verify that I can find a $method by a body pattern
     */
    public void testSpecificBody(){
        $method $m = $method.of().$body(new Object(){ void m(Object $any$) { 
            System.out.println($any$); 
        }});
        
        class C {
            public void g(){
                System.out.println(1);
            }    
            public void t(){
                // A comment is ignored when matching
                System.out.println( "Some text ");                
            }            
        }        
        assertNotNull($m.firstIn(C.class));
        assertNotNull($m.selectFirstIn(C.class).is("any", 1));
        assertEquals(2, $m.listIn(C.class).size());        
    }
    
    
    public void testSetTest(){
        $method $set = $method.of("public void set$Name$( $type$ $name$ ) {this.$name$ = $name$;}");
        //System.out.println( SET.construct("type",int.class, "name", "x") );
        
        //verify a "round trip" construct a _method by populating $parameters, 
        //then select it and verify we can pull the parameters out...
        assertTrue($set.select($set.draft("type",int.class, "name", "x"))
            .is("type", int.class, "name", "x") );
        
        //make sure I can select from an existing class
        class C{
            public void setX( int x ){
                this.x = x;
            }
            
            /**
             * prototype $set without javadoc or annotations
             * will match a method WITH javadoc and annotations
             * ALSO with an extra (final) modifier
             */
            @Deprecated
            public final void setY(int y){
                this.y = y;
            }
            
            int x, y;
        }
        
        assertTrue($set.selectFirstIn(C.class).is("type",int.class,"name","x"));
        
        assertEquals(2, $set.listSelectedIn(C.class).size());
        
    }
    
    interface I{
        void noBody();
        
        public static int someBody(){
            return 103;
        }
        
        /** Javadoc */ @Deprecated void f();
    }
    public void testAnyPrototype(){
        assertTrue( $method.of().matches("void noBody();") );
        assertTrue( $method.of().matches(" public static int someBody(){ return 103;}") );
        //verify that the any() prototype will list methods with or without body
        System.out.println( $method.of().listIn(I.class) );
        assertEquals( 3, $method.of().listIn(I.class).size() );
    }
    
    public void testSimpleMatch(){
        $method $m = $method.of( new Object(){
            $type$ $name$;
            class $type${} 
            public $type$ get$Name$(){
               return this.$name$;
            }           
        });
        
        //you can create a method based on the properties of a field
        // (i.e. a _field will decompose to 
        // even though the field is private, we DONT traspose the 
        //modifier because it is static
        //assertEquals( $m.draft( _field.of("private int count") ),
        //        $m.draft( "type",int.class, "name", "count" )); //_field.of("int count") ) );
        //_method _m = $m.construct( _field.of("private int count") );                
        //System.out.println( _m );
        
        $m.$annos("@Deprecated");
        _method _m = $m.draft( "type", "int", "name", "count"  );
        System.out.println( _m );
        
        assertTrue($m.matches(_m));
        _m.removeAnnoExprs(Deprecated.class);
        
        /** SHOULD WORK, NEED TO REFACTOR $anno first 
        _m.annotate("@java.lang.Deprecated");
        System.out.println( _m );
        assertTrue($m.matches(_m));
        */
        
    }
    public void testStaticMethodReplaceRemoveList(){
        $method $set = $method.of(
            "public void set$Name$($type$ $name$){",
            "this.$name$ = $name$;",
            "}");
        
        $method $setFleunt = $method.of(
            "public $className$ set$Name$( $type$ $name$ ){",
            "this.$name$ = $name$;",
            "return this;",
            "}");
        
        class Loc{
            int x;
            String y;
            
            public void setX(int x){
                //comment
                this.x = x;
            }
            
            public void setY(String y){
                
                this.y = y;
                
            }
        }
        _class _c = _class.of(Loc.class);
        
        //$method stat = $method.of("public void setX(int x){ this.x = x; }");
        //_method _m = stat.construct();
        //System.out.println("COMPOSED" + _m );
        
        //assertEquals(1, stat.listIn(_c).size());
        //verify we can find (2) set methods
        
        assertEquals(2, $set.listIn(_c).size());
        
        //remove 'em
        $set.removeIn(_c);
        
        //verify they are removed
        assertEquals(0, $set.listIn(_c).size());
        
        //rebuild
        _c = _class.of(Loc.class);
        
        $set.listSelectedIn(_c).get(0).is("type", int.class);
        // call replace with a setFluent prototype
        assertEquals(2, $set.countIn(_c));
        //System.out.println( $setFleunt.hardcode$("className", "Loc").type );
        $set.replaceIn(_c, $setFleunt.$hardcode("className", "Loc") );


        //verify there are no simple sets left 
        assertEquals(0, $set.listIn(_c).size());
        
        System.out.println( _c );
        
        assertTrue( $setFleunt.matches("public Loc setX(int x) {",
            "this.x = x;",
            "return this;",
            "}") );
        
        MethodDeclaration astMd = Ast.methodDeclaration("public Loc setX(int x) {",
            "this.x = x;",
            "return this;",
            "}");
        
        assertTrue($setFleunt.matches(astMd));
        
        _method _m = _method.of(astMd);
        
        assertTrue($setFleunt.matches(_m));
        
        //assertNotNull( $setFleunt.firstIn(Loc.class));
        

        System.out.println( $setFleunt );
        _method _me = $setFleunt.draft("name", "x", "type", int.class);
        System.out.println( _me);
        
        System.out.println( _c );
        String strs = _c.firstMethodNamed("setX").toString();
        System.out.println( strs );
        assertNotNull( $setFleunt.select(strs) );
        
        System.out.println( "METHOD TYPE " + _c.firstMethodNamed("setX").getType() );
        
        assertNotNull( $setFleunt.select(_c.firstMethodNamed("setX") ) );
        assertNotNull( $setFleunt.firstIn(_c));
        
        //verify there are (2) setFluents
        assertEquals(2, $setFleunt.listIn(_c).size());        
    }

    public void testAnonymousBodyWithMacros(){
        $method $m = $method.of(new Object(){
            @_static void a(){ System.out.println(1); }
        });
        _method _m = $m.draft();
        assertTrue( _m.isStatic() );
    }
    
    public void testSimpleGetter(){
        $method $get = $method.of("public $type$ get$Name$(){ return this.$name$; }");
        
        
        BodyDeclaration bd = Ast.bodyDeclaration( "public java.lang.String getEric ()" + System.lineSeparator() +
                "{"+ System.lineSeparator()+
                "    return this.eric;"+ System.lineSeparator() +
                "}");
        System.out.println( bd );
        
        _method _m = $get.draft("name", "eric", "type", String.class);
        System.out.println( _m );
    }
    
    public void testAnonymousBody(){
        $method $m = $method.of( new Object(){
            @_remove class $type${}
            @_remove $type$ $name$;
            
            /**
             * Gets the $name$
             *
             * @return
             */
            @Deprecated
            public $type$ get$Name$(){
                return this.$name$;
            }
        });
        //System.out.println( $m.draft( _field.of("int count;")) );

        //verify that the ANNOTATIONS and JAVADOC are transposed
        //
        assertEquals( _method.of(
                "/**",
                " * Gets the count",
                " *",
                " * @return ",
                " */",
                "@Deprecated",
                "public int getCount(){ return this.count; }"), $m.draft("type", "int", "name","count"));
    }

    public void testGetterSetter(){

        $method $get = $method.of("public $type$ get$Name$(){ return $name$; }");
        $method $getThis = $method.of("public $type$ get$Name$(){ return this.$name$; }");


        $method $set = $method.of("public void set$Name$( $type$ $name$){ this.$name$ = $name$; }");

        _method composed = $get.draft("type", int.class, "name", "x" );
        //System.out.println( composed );
        _method _ma = _method.of(new Object(){
            public int getX(){
                return x;
            }
            int x;
        });
        
        //_method _ma = _method.of("public int getX()", (Integer x)->{
        //    return x;
        //});

        assertTrue( $get.matches( _ma) );

        assertEquals( composed, _ma );

        class F{
            int x,y,z;

            public int getX(){
                return x;
            }

            /**
             * With comment
             * @return
             */
            public int getY(){
                return y;
            }

            public int getZ(){
                return z;
            }

            public void setX( int x){
                this.x = x;
            }

            public void setY( int y){
                this.y = y;
            }
            public void setZ( int z){
                this.z = z;
            }
        }
        _class _c = _class.of( F.class);


        //find all (3) getter METHODS in the TYPE above
        assertEquals( 3, $get.listSelectedIn(_c).size());
        assertEquals($get.select(_c.firstMethodNamed("getX") )._m, _method.of("public int getX(){ return x; }") );
        assertEquals( 3, $set.listSelectedIn(_c).size());

        //print all get METHODS
        $get.forSelectedIn(_c, g -> System.out.println(g._m ));

        //convert all void set METHODS to fluent set METHODS
        $set.forSelectedIn(_c, s-> {
            s._m.ast().setType( _c.getName() );
            s._m.ast().getBody().get().addStatement("return this;");
        });
        _runtime.compile( _c );

    }

}
