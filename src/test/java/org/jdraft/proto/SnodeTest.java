package org.jdraft.proto;

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
