package org.jdraft.proto;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import org.jdraft.proto.$node;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.jdraft.Ast;
import org.jdraft._class;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import junit.framework.TestCase;

/**
 *
 * @author Eric
 */
public class SnodeTest extends TestCase {


    public void testTypeUse(){
        class C{
            int i, j;
            String t;

            public void g(){
                if( true ) {
                    synchronized (t) {

                    }
                }
            }
            void gg(){ }
        }

        //System.out.println( $node.of().listIn(C.class) );

        //$node.of().forEachIn(C.class, e-> System.out.println(  e.getClass() ));


        $node.of(Ast.SIMPLE_NAME).$hasParent( $method.of( m-> m.isPackagePrivate())).forEachIn(C.class, e ->System.out.println("found " + e));

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

        System.out.println( $node.of(SimpleName.class).$hasParent(ClassOrInterfaceDeclaration.class).listIn(D.class));
        System.out.println( $node.of(SimpleName.class).$hasParent(MethodDeclaration.class).listIn(D.class));

        //list the names of all public methods in D class
        System.out.println( $node.of(SimpleName.class).$hasParent($.method(m->m.isPublic())).listIn(D.class));
        System.out.println( $node.of(SimpleName.class).$hasParent($.method(m->m.isPublic())).listIn(D.class));

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
            
            @org.jdraft.proto.SnodeTest.Ann
            Map<Inter,Base> m = new HashMap<>();
            
            @org.jdraft.proto.SnodeTest.Ann
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
        }).implement(Inter.class)
                .extend(Base.class);
         
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
