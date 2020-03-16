package org.jdraft.pattern;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SnodeTest extends TestCase {

    public void testMethodOfNodeCaseCatchVar(){
        _method _m = _method.of(new Object(){
            public void m(int i){
                int j = i + 100;
                switch( i ){

                    case 1 : System.out.println( 1  );
                    default : System.out.println( "the default ");
                }

                try{
                    UUID uuid = UUID.fromString("12231233123123");
                }catch(IllegalArgumentException fnfe){
                    System.out.println( fnfe );
                }
            }
        });

        assertTrue( $method.of( $catch.of() ).matches(_m) );
        assertTrue( $method.of( $catch.of(IllegalArgumentException.class) ).matches(_m) );
        assertTrue( $method.of( $catch.of( $stmt.of( ($any$)->System.out.println($any$) )) ).matches(_m) );


        assertFalse( $method.not( $catch.of(IllegalArgumentException.class) ).matches(_m) );
        assertFalse( $method.not( $catch.of( $stmt.of( ($any$)->System.out.println($any$) )) ).matches(_m) );


        assertTrue( $method.of( $switchEntry.of() ).matches(_m) ); //a method with a switch case
        assertTrue( $method.of( $switchEntry.of( $ex.of(1) ) ).matches(_m) ); //a method with a switch "case 1: ..."
        assertTrue( $method.of( $switchEntry.of( $ex.of(1), $stmt.of(()->System.out.println(1)) ) ).matches(_m) ); //a method with a switch "case 1: System.out.println(1);"
        assertTrue( $method.of( $switchEntry.of( $stmt.of(()->System.out.println(1)) ) ).matches(_m) ); //a method with ANY case w/ body containing: "System.out.println(1);"

        assertFalse( $method.not( $switchEntry.of( $ex.of(1) ) ).matches(_m) ); //a method with a switch "case 1: ..."
        assertFalse( $method.not( $switchEntry.of( $ex.of(1), $stmt.of(()->System.out.println(1)) ) ).matches(_m) ); //a method with a switch "case 1: System.out.println(1);"
        assertFalse( $method.not( $switchEntry.of( $stmt.of(()->System.out.println(1)) ) ).matches(_m) ); //a method with ANY case w/ body containing: "System.out.println(1);"

        //assertTrue( $method.of( $case.of(IllegalArgumentException.class) ).matches(_m) );

        assertTrue( $method.of( $var.of() ).matches(_m) );
        assertTrue( $method.of( $var.of($name.of("j")) ).matches(_m) ); //a method with a var
        assertTrue( $method.of( $var.of(int.class) ).matches(_m) );

        assertFalse( $method.not( $var.of($name.of("j")) ).matches(_m) ); //a method with a var
        assertFalse( $method.not( $var.of(int.class) ).matches(_m) );

        //_java.describe(_m );
        $node $n = $node.of( new SimpleName("out") );
        assertTrue( $method.of( $n ).matches(_m) );
        assertFalse( $method.not( $n ).matches(_m) );
    }


    public void testConstructorOfNodeCaseCatchVar(){
        _constructor _c = _constructor.of(new Object(){
            public void m(int i){
                int j = i + 100;
                switch( i ){
                    case 1 : System.out.println( 1  );
                    default : System.out.println( "the default ");
                }
                try{
                    UUID uuid = UUID.fromString("12231233123123");
                }catch(IllegalArgumentException fnfe){
                    System.out.println( fnfe );
                }
            }
        });

        assertTrue( $constructor.of( $catch.of() ).matches(_c) );
        assertTrue( $constructor.of( $catch.of(IllegalArgumentException.class) ).matches(_c) );
        assertTrue( $constructor.of( $catch.of( $stmt.of( ($any$)->System.out.println($any$) )) ).matches(_c) );


        assertFalse( $constructor.not( $catch.of(IllegalArgumentException.class) ).matches(_c) );
        assertFalse( $constructor.not( $catch.of( $stmt.of( ($any$)->System.out.println($any$) )) ).matches(_c) );


        assertTrue( $constructor.of( $switchEntry.of() ).matches(_c) ); //a ctor with a switch case
        assertTrue( $constructor.of( $switchEntry.of( $ex.of(1) ) ).matches(_c) ); //a ctor with a switch "case 1: ..."
        assertTrue( $constructor.of( $switchEntry.of( $ex.of(1), $stmt.of(()->System.out.println(1)) ) ).matches(_c) ); //a ctor with a switch "case 1: System.out.println(1);"
        assertTrue( $constructor.of( $switchEntry.of( $stmt.of(()->System.out.println(1)) ) ).matches(_c) ); //a ctor with ANY case w/ body containing: "System.out.println(1);"

        assertFalse( $constructor.not( $switchEntry.of( $ex.of(1) ) ).matches(_c) ); //a ctor with a switch "case 1: ..."
        assertFalse( $constructor.not( $switchEntry.of( $ex.of(1), $stmt.of(()->System.out.println(1)) ) ).matches(_c) ); //a ctor with a switch "case 1: System.out.println(1);"
        assertFalse( $constructor.not( $switchEntry.of( $stmt.of(()->System.out.println(1)) ) ).matches(_c) ); //a ctor with ANY case w/ body containing: "System.out.println(1);"

        //assertTrue( $method.of( $case.of(IllegalArgumentException.class) ).matches(_m) );

        assertTrue( $constructor.of( $var.of() ).matches(_c) );
        assertTrue( $constructor.of( $var.of($name.of("j")) ).matches(_c) ); //a ctor with a var
        assertTrue( $constructor.of( $var.of(int.class) ).matches(_c) );

        assertFalse( $constructor.not( $var.of($name.of("j")) ).matches(_c) ); //a ctor with a var
        assertFalse( $constructor.not( $var.of(int.class) ).matches(_c) );

        //_java.describe(_m );
        $node $n = $node.of( new SimpleName("out") );
        assertTrue( $constructor.of( $n ).matches(_c) );
        assertFalse( $constructor.not( $n ).matches(_c) );
    }

    public void testAfter(){
        //verify the position of a thing
        class Position{
            void f(){
                int a = 1;
                int b = 2;
            }
        }

        CompilationUnit cu = StaticJavaParser.parse("class F{ void g(){ System.out.println(1); System.out.println(2); } }");

        Statement st1 = cu.getType(0).getMethodsByName("g").get(0).getBody().get().getStatement(0);
        Statement st2 = cu.getType(0).getMethodsByName("g").get(0).getBody().get().getStatement(1);

        assertTrue( st1.getRange().get().isBefore(st2.getRange().get().begin) );
        assertTrue( st2.getRange().get().isAfter(st1.getRange().get().begin) );
    }

    public void testHardcode$(){
        class C{
            void f(){
                int b = 4;
                int a = b;
            }
        }
        //assertEquals( 3, $node.of("$name$", SimpleName.class).count(C.class));
        assertEquals( 5, $node.of("$name$", SimpleName.class).listIn(C.class).size()); //C, f, b, a, b
        assertEquals( 1, $node.of("$name$", SimpleName.class).$hardcode("name", "a").listIn(C.class).size());
    }



    public void testStaticFieldAccessMethodCall(){
        class D{
            public void tt(){
                Collections.sort(null, null);
            }
            List l(){
                return Collections.EMPTY_LIST;
            }
            void useMethodReference(){
                Map<String, List> sl = new HashMap();

                sl.values().forEach(Collections::sort);
            }
        }
        //here we find Collections::sort
        assertEquals(1, $typeRef.of(Collections.class).countIn(D.class)); //there are no instances of the Collections class appearing above

        //...but we ARE using Collections, to access a specific variable (Collections.EMPTY_LIST)
        // and to call a static method Collections.sort(...)
        assertEquals( 1, $.of("Collections", NameExpr.class).$isParent( $.methodCall("Collections.$call$($any$)") ).countIn(D.class));
        assertEquals( 1, $.of("Collections", NameExpr.class).$isParent( $.fieldAccessExpr("Collections.$field$") ).countIn(D.class));
        //assertEquals(1, $typeRef.of(Collections.class).count(D.class));
    }
    public void testMethodCall(){
        class C{
            void m(){
                System.out.println(1); //here I am using the Class "System", but System in this context is not a Type

            }
            System s(){
                return null;
            }

        }
        //assertEquals( 1, $.typeRef(System.class).count(C.class));
        //$.methodCall().forEachIn(C.class, Ast::describe);
        assertEquals(1, $.typeRef("System").countIn(C.class)); //there is (1) reference to System (return type)
        assertEquals( 1, $.of("System", SimpleName.class).$hasAncestor( $.fieldAccessExpr() ).countIn(C.class));
        //assertEquals( 1, $.of("System", SimpleName.class).$hasAncestor( $.methodReference() ).count(C.class));
    }

    interface B{

    }
    class A{

    }
    public void testAdvTypeUse(){

        class C extends A implements B {
            System s = null; //as a field

            //as return type
            public System getSystem(){
                return s;
            }

            //as a Parameter
            public void setSystem( System s){
                this.s = s;
            }

            public void asAMethodReference(){
                List l = new ArrayList<>();
                //in a Method reference
                l.forEach( System.out::println);
            }

            public void asLocalVariable() {
                System sy = null;
            }

            public List<System> asTypeArg(){ return null; }

            public void inFullyQualifiedMethod(){
                System.out.println(1);
            }
        }



        //add it manually as an import
        _class _c = _class.of(C.class);
        _c.astCompilationUnit().addImport(new ImportDeclaration("java.lang.System", false, false));

        Expression scope = Ex.fieldAccessEx("System.out" ).asFieldAccessExpr().getScope();
        Ast.isParent(scope, FieldAccessExpr.class);

        Ast.describe( ((_java._node)$.stmt("System.out.println(1);").firstIn(_c)).ast() );

        assertEquals(6, $typeRef.of(System.class).countIn(_c));
        $typeRef.of(System.class).forEachIn( _c, s-> System.out.println(s+" parent -> "+ s.ast().getParentNode().get()));

        assertEquals( 1, $.of("System", SimpleName.class).$hasAncestor( $.methodReference() ).countIn(_c));

        $.of(SimpleName.class).$hasAncestor( $.fieldAccessExpr() );
        //screw it, we should be able to manually look for things
        //assertEquals(1, $.of(  ).count(_c)); //OK
        System.out.println( _c );
        assertEquals( 2, $var.of(System.class).countIn(_c) );
        //assertEquals( 1, $import.of(System.class).count(_c));

        //assertTrue( Ast.typesEqual(Ast.typeRef("System"), Ast.typeRef(System.class)) );
        //assertEquals( 1, $var.member().count(_c) );



        //assertTrue( $typeRef.of(System.class).matches("java.lang.System"));
        //assertTrue( $typeRef.of(System.class).matches("System"));

        //assertEquals( 1, $var.member($typeRef.of("System")).count(_c) );
        //assertEquals( 1, $var.member($typeRef.of(System.class)).count(_c) );
        //assertEquals( 1, $.of($var.member(System.class)).count(_c) );

        //only found (2) of (6) ... im just verifying it's broke
        $typeUse tu = $typeUse.of(System.class);
        assertEquals(2, tu.listIn(C.class).size() );

        System.out.println( _c );
        //$typeRef $tr = $typeRef.of(System.class);
        $typeRef $tr = $typeRef.of("System");
        System.out.println( $tr.countIn(_c) );
    }

    public void testTypeUse(){
        class C{
            int i, j;
            String t;
            List<String> l = new ArrayList<>();

            public void g(){
                if( true ) {
                    synchronized (t) {
                        l.forEach( System.out::println );
                    }
                }
            }
            void gg(){ }
        }


        _class _c = _class.of( C.class);
        System.out.println( $node.of().forEachIn(_c, e -> System.out.println(e.getClass()+" "+e)) );//

        //System.out.println( $node.of().forEachIn(C.class, e -> System.out.println(e)) );//e.getClass()+" "+

        //$node.of().forEachIn(C.class, e-> System.out.println(  e.getClass() ));


        $node.of(Ast.SIMPLE_NAME).$isParent( $method.of(m-> m.isPackagePrivate())).forEachIn(C.class, e ->System.out.println("found " + e));

        //find all synchronized statements that are ancestors (within) a public class in C
        $node.of(Ast.SYNCHRONIZED_STMT).$hasAncestor( $method.of(m->m.isPublic()) ).forEachIn( C.class, s -> System.out.println( s) );

        //find and return all types that have synchronized statements
        $node.of(Ast.TYPE_DECLARATION).$hasDescendant( $.synchronizedStmt() ).forEachIn( C.class, s-> System.out.println(s));

        //$node.of(Ast.NODE_WITH_CONSTRUCTORS).forEachIn( C.class, e-> System.out.println( e.getClass()));
        //$node.of(Ast.NODE_WITH_ANNOTATIONS).forEachIn( C.class, e-> System.out.println( e.getClass()));
        //$node.of(Type.class).forEachIn(C.class, e-> System.out.println(  e.getClass() ));



        //System.out.println( "Types & SimpleName" + $node.of(Type.class, SimpleName.class).listIn(C.class));
        //System.out.println( "Types" + $node.of(Type.class).listIn(C.class));
        //System.out.println( "Types" + $node.of(SimpleName.class).listIn(C.class));

        //System.out.println( "Types" + $node.of(n-> n instanceof Type).listIn(C.class));

        //System.out.println( "SIMPLE NAMES" + $node.of(n-> n instanceof SimpleName).listIn(C.class));

        //$or( $typeRef.of(), $node.of(n-> n instanceof SimpleName) );
        //$node.or( $proto... );

        //$node.of("$any$").addConstraint(n -> n instanceof com.github.javaparser.ast.type.Type);
        //System.out.println( $node.of("$any$").listIn(C.class) );

        //System.out.println( $typeUse.of().listIn(C.class) );
    }

    public void testClasses(){
        class D{
            int f = 1;
             void m(){}
        }


        System.out.println( $node.of(SimpleName.class).listIn(D.class) );
        System.out.println( $node.of(SimpleName.class, FieldDeclaration.class).listIn(D.class) );

        System.out.println( $node.of(SimpleName.class).$isParent(ClassOrInterfaceDeclaration.class).listIn(D.class));
        System.out.println( $node.of(SimpleName.class).$isParent(MethodDeclaration.class).listIn(D.class));

        //list the names of all public methods in D class
        System.out.println( $node.of(SimpleName.class).$isParent($.method(m->m.isPublic())).listIn(D.class));
        System.out.println( $node.of(SimpleName.class).$isParent($.method(m->m.isPublic())).listIn(D.class));

    }
    public interface Inter{
        public static final int VAL = 1;
        public static void tell(){            
        }
        public static Integer blah(){ return 1;}
        public static Integer mr(Object o){ return 1;}
    }
    
    public static class Base{
        public static final int VAL = 1;
        public static void tell(){            
        }
        
        
        public static Integer blah(){ return 1;}
        public static Integer mr(Object o){ return 1;}
    }
    
    public @interface Ann{
        int value() default 1;
        int a() default 2;
    }
    
    
    /** */
    public void testAstParseAnnotationOnMethod(){
        AnnotationExpr as = Ast.anno("@$dollar");
        as = Ast.anno("@$dollar.bills");
        
    }
    
    public void test$Node(){
        AnnotationExpr ae = StaticJavaParser.parseAnnotation("@$Ann");
        //System.out.println( "ANNOTATIONEPR " + ae );
        ae = StaticJavaParser.parseAnnotation("@$nodeTest.Ann");
        //System.out.println( "ANNOTATIONEPR " + ae );
        CompilationUnit cu = StaticJavaParser.parse(
            "class C{" + System.lineSeparator()+
            "    @draft.java.proto.$nodeTest.Ann" + System.lineSeparator() +
            "    public int i;" + System.lineSeparator() +        
                    
            "    @$nodeTest.Ann\n" +
            "    public void setInter( Inter ifld ){\n" +
            "        this.ifield = ifld;\n" +
            "    }" + System.lineSeparator() +                
            "}" );
        System.out.println( cu );
        
    }
    
    /***
     * There is a problem whenever I have a $ in the annotation name
     *  
     */
    
    public void testN(){
        _class _c = _class.of("C",new Object(){
            @Ann
            Inter ifield = null;
            
            @SnodeTest.Ann
            Base bField = null;
            
            @org.jdraft.pattern.SnodeTest.Ann
            Map<Inter,Base> m = new HashMap<>();
            
            @org.jdraft.pattern.SnodeTest.Ann
            Inter getInter(){
                Inter.tell();
                System.out.println( Inter.VAL );
                Class II = Inter.class; //verify classExpr                
                Consumer<Object> C = Inter::mr; //verify method references
                
                BiConsumer<Inter, Base> f = ( Inter i, Base b) -> System.out.println();
                
                Inter[] arr = new Inter[0];
                if( arr[0] instanceof Inter ){
                    
                }
                @SnodeTest.Ann
                class INN implements Inter{ //check inner nest
                    
                }
                
                return ifield;
            }
            
            Base getBase(){
                Base.tell();
                System.out.println( Base.VAL );
                System.out.println( (Base)bField ); //verify case
                Class CC = Base.class;
                //verify method references
                Consumer<Object> C = Base::mr;
                Base[] arr = new Base[0];
                if( arr[0] instanceof Base ){
                    
                }
                class INN extends Base{ //check inner nest
                    
                }
                return bField;
            }
            
            @SnodeTest.Ann
            public void setInter( Inter ifld ){
                this.ifield = ifld;
            }
            //@draft.java.proto.$nodeTest.Ann
            public <I extends Inter> void g( I in ) {}
            
            //@$nodeTest.Ann
            public <B extends Base> void gg( B in ) {}                        
        }).addImplement(Inter.class)
                .addExtend(Base.class);
         
        //the first thing is to replace fully qualified references
        $node $n = new $node(Inter.class.getCanonicalName());
        $n.replaceIn(_c, Inter.class.getCanonicalName().replace("Inter", "Other") );
        $n = new $node("Inter");
        $n.replaceIn(_c, "Other");
        
        
        $n = new $node(Ann.class.getCanonicalName());
        $n.replaceIn(_c, Ann.class.getCanonicalName().replace("Ann", "Stan") );
        
        $n = new $node("$nodeTest.Ann");
        $n.replaceIn(_c, "$nodeTest.Stan");
        
        $n = new $node("Ann");
        $n.replaceIn(_c, "Stan");
        
        
        $n = new $node(Base.class.getCanonicalName());
        $n.replaceIn(_c, Base.class.getCanonicalName().replace("Base", "Replace") );
        $n = new $node("Base");
        $n.replaceIn(_c, "Replace");
        
        System.out.println( _c );        
    }    
}
