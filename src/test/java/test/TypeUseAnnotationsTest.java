package test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.jdraft._anno;
import org.jdraft._class;
import org.jdraft._field;
import org.jdraft._typeParameter;
import org.jdraft.pattern.$ex;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;

import junit.framework.TestCase;

/**
 * This class houses use cases where people try funny things with annotations
 * (annotations on Type Use and TypeParamtete
 * @author Eric
 */
public class TypeUseAnnotationsTest 
    extends TestCase {
    
    /** 
     * These "type Annotations" aer normally used by the Checker Framework:
     * https://checkerframework.org/
     * for compile time checking (integrated wwith the Javac compiler)
     */
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @interface TypeAnno{ }
    
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    @interface TA2{}
    
    public static class Nested{
        public static void saySomething(){ System.out.println("hi"); }
    }
    public class NN{
        public class F{
            
        }
    }
    class TT {
	//Generics constructor
	public <T> TT(T item){ }
    }
    
    static class GT<T>{
        public static<R> void size(){}
    }
    
    interface Sayable{  
        void say();  
    }
    
    public void testTypeAnnotations(){
        
        class TC <@TypeAnno @TA2 F> /*In Type Parameters*/
            extends @TypeAnno @TA2 NN    
            implements @TypeAnno @TA2 Serializable /*in inheritance*/ {
            
            /* on a type Declaration */             
            @TypeAnno @TA2 Object o = "STR";
            
            /* nested type declaration */
            TypeUseAnnotationsTest.@TypeAnno @TA2 Nested n; 
            
            void m(){
                /*In Constructors with type annotations:*/
                NN n = new @TypeAnno @TA2 NN(); 
                /*In (non static) class constructors: */
                n.new @TypeAnno @TA2 F(); 
            }
            
            /* in cast */
            String s = (@TypeAnno @TA2 String)o; 
            
            /* in Type Generic */
            List<@TA2 @TypeAnno Integer> l = null;  
            
            /* in lambda? */
            //Consumer<String> cff = 
            //    (@TypeAnno @TA2 String s)->System.out.println( "3" );
            
            /* In intersection types ( x & y) */
            public <E extends @TypeAnno @TA2 Serializable & @TypeAnno @TA2 Cloneable> void foo(){
            
            }
            /* In wildcard bounds */
            List<? extends @TypeAnno @TA2 Comparable<String>> w = null;
            List<? super @TypeAnno @TA2 File> c = null;
            List<@TypeAnno @TA2 ? extends Comparable<String>> u = null;
            
            /* throwing exceptions */
            void mm() throws @TypeAnno @TA2 FileNotFoundException, 
                @TypeAnno @TA2 RuntimeException {}
             
            /* in MethodReferences/ClassReferences */
            boolean i = o instanceof @TypeAnno @TA2 String;
            Sayable z = @TypeAnno @TA2 Nested::saySomething;              
            Sayable y = GT::<@TypeAnno @TA2 String>size;       
            
            /* FAILS HERE : In generic constructor */
            //TT tt = new <String> @TypeAnno @TA2 TT( "S" );
        }        
        
        _class _c = _class.of( TC.class );
        System.out.println( _c );
        
        $ex.objectCreationEx().firstIn(_c);
        
        _typeParameter _tp = _c.getTypeParameters().get(0);
        assertEquals( _anno.of(TypeAnno.class), _tp.getAnno(0) );
        assertEquals( _anno.of(TA2.class), _tp.getAnno(1) );
        
        ClassOrInterfaceType coit = _c.getExtends();
        assertEquals(_anno.of(TypeAnno.class).ast(), coit.getAnnotation(0));
        assertEquals(_anno.of(TA2.class).ast(), coit.getAnnotation(1));
        
        List<ClassOrInterfaceType> impls = _c.listImplements();
        coit = impls.get(0);
        assertEquals(_anno.of(TypeAnno.class).ast(), coit.getAnnotation(0));
        assertEquals(_anno.of(TA2.class).ast(), coit.getAnnotation(1));
        
        _field _f = _c.getField("o");
        assertTrue( _f.hasAnno(TypeAnno.class));
        assertTrue( _f.hasAnno(TA2.class));
        
        _f = _c.getField("n");
        //System.out.println(_f);
        //VariableDeclarator vd = _f.ast();
        FieldDeclaration astFd = _f.getFieldDeclaration();
        
        //System.out.println( astFd.getVariable(0).getType().getAnnotations() );
        
        assertEquals( _anno.of(TypeAnno.class).ast(), 
            astFd.getVariable(0).getType().getAnnotations().get(0) );
        
        assertEquals( _anno.of(TA2.class).ast(), 
            astFd.getVariable(0).getType().getAnnotations().get(1) );
        
        //assertTrue( _f.hasAnno(TypeAnno.class));
        //assertTrue( _f.hasAnno(TA2.class));        
        //System.out.println(  );
        //System.out.println(_c);        
    }    
    
    public void testJavaParserPart(){
        ObjectCreationExpr oce = 
            StaticJavaParser.parseExpression("new @TypeAnno TT(1)");
        
        System.out.println( oce );
        oce = StaticJavaParser.parseExpression("new <String> TT(1)");
        
        System.out.println( oce );
        //oce = StaticJavaParser.parseExpression("new <String> @TypeAnno TT(1)");        
    }
    
}
