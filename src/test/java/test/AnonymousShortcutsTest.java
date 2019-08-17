package test;

import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.Statement;

import org.jdraft.*;
import org.jdraft.macro.*;

import org.jdraft.proto.*;

import org.jdraft.runtime.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import junit.framework.TestCase;
import org.jdraft.macro._final;
import org.jdraft.macro._static;

/**
 *
 * @author Eric
 */
public class AnonymousShortcutsTest extends TestCase {
    
    /**
     * 
     */
    public void testMethod(){
        
        //create a method via an anonymous class
        _method _m = _method.of( new Object(){
            /** Javadoc*/
            @Deprecated
            public void m(){
                System.out.println( "Hello");
            }
        });
        
        System.out.println( _m );
    }
     
    public void test$Method(){
        //create a method prototype via Anonymous class
        $method $m = $method.of( new Object(){
            /** Javadoc */
            @Deprecated
            public void m(){
                System.out.println("Hello");
            }
        });
        //todo create a toString for $method
        System.out.println( $m );
        System.out.println( $m.draft() );
    }
    
    public void testField(){
        _field _f = _field.of( new Object(){
            /** Javadoc */
            @Deprecated
            public static final int a = 100 + 20;
        });
        //ensure the field has the anno, the javadoc and the init expression
        assertEquals( _f, _field.of("/** Javadoc */ @Deprecated public static final int a = 100 + 20;" ) );
    }
    
    public void test$Field(){
        $field $f = $field.of( new Object(){
            /** Javadoc */
            @Deprecated
            public static final int a = 100 + 20;
        }); 
        
        assertEquals( $f.draft(), _field.of("/** Javadoc */ @Deprecated public static final int a = 100 + 20;" ) );
    }
    
    public void testConstructor(){
        _constructor _c = _constructor.of( new Object(){
            private int a, b;
            
            /**
             * Some Javadoc 
             * @param a
             * @param b
             */
            @Deprecated
            public void C(int a, final int b){
                this.a = a;
                this.b = b;
            }
        });        
        System.out.println( _c );
    }
    
    public void test$constructor(){
        
        $constructor $c = $constructor.of( new Object(){
            private int a, b;
            
            /**
             * Some Javadoc 
             * @param a
             * @param b
             */
            @Deprecated
            public void C(int a, final int b){
                this.a = a;
                this.b = b;
            }
        });        
        System.out.println( $c.draft() );
        
    }
    
    /**
     * Instead of "one at a time" you can create the body and members
     * of a _class as 
     */
    public void testAnonymousClassBodyMembers(){
        _class _c = _class.of("Anon", new Object(){
            private int a;
            private String name = "Default";
            
            public @_static @_final Map someStatic;
            
            /** a constructor */
            @Deprecated
            @_toCtor public void m( int a, String name) throws IOException{
                this.a = a;
                this.name = name;
            }
            
            /** a static method */
            @Deprecated
            public @_static void g() throws FileNotFoundException {
                System.out.println("hello");
            }            
        });
        System.out.println( _c.getField("someStatic"));
        assertTrue( _c.getField("someStatic").is("public static final Map someStatic;") );
        assertTrue( _c.hasImport(Map.class)); //ohh... also, we read the public API and "auto import" the appropriate classes
        assertTrue( _c.getMethod("g").isStatic());
        assertTrue( _c.getMethod("g").hasThrow(FileNotFoundException.class));
        
        System.out.println(_c);
    }
    
    public interface ToImplement{
        public boolean implementedThisMethod();
    }
    
    public void testImplementer(){
        _class _c = _class.of("AnonymousImplementer", new ToImplement(){
            public static final boolean implemented = true;
            
            @_remove private int someValueIDontWant;
                    
            /** The implementation method will be transposed */
            @Override
            public boolean implementedThisMethod() {
                return this.implemented;
            }            
            
            public @_static void anotherIncludedMethod(){
                System.out.println( "It's transposed");
            }
        });
        
        assertTrue( _c.isImplements(ToImplement.class));
        assertTrue( _c.getField("implemented").is("public static final boolean implemented = true;") );
        assertTrue( _c.getMethod("implementedThisMethod").hasAnno(Override.class) );
        assertTrue( _c.getMethod("anotherIncludedMethod").isStatic());
        assertNull( _c.getField("someValueIDontWant")); 
    }
    
    public void testEnum(){
        _enum _e = _enum.of("E", new Object(){});        
        System.out.println( _e );
        _e = _enum.of("E", new Object(){
            public @_static final Map m = null; 
            public int a;
            @_toCtor void m(int v){
                this.a = v;
            }
        }).constants("A(1)", "B(2)");    
        
        assertTrue( _e.hasImport(Map.class));
        //System.out.println( _e );
        //_javac.of(_e );
    }
    
    public void testInterface(){
        _interface _i = _interface.of("I", new Object(){
            public int aField = 100;
            
            public @_static void aStaticMethod(){
                System.out.println("Static Interface method");
            }
            
            public @_default int aDefaultMethod(){
                return 103;
            }
            
            /**
             * Since this method is NEITHER static
             * @param a
             * @param b
             * @return 
             */
            public Map aMethodWhosBodyWillBeRemoved(int a, int b){
                return null;
            }            
        });
        assertTrue( _i.hasImport(Map.class));//since map is on the API of, we auto import it  
        assertTrue( _i.getMethod("aStaticMethod").isStatic() );
        assertTrue( _i.getMethod("aDefaultMethod").isDefault() );
        assertFalse( _i.getMethod("aMethodWhosBodyWillBeRemoved").hasBody());
        _runtime.compile(_i);
        System.out.println( _i );
    }
    
    public void testBody(){
        _body _b = _body.of( new Object(){
            void b(){
                System.out.println(1);
                assert(1==1);                
            }
        });
        
        assertEquals( 2, _b.getStatements().size() );
        assertEquals( _b.getStatement(0), Stmt.of("System.out.println(1);") );
        assertEquals( _b.getStatement(1), Stmt.of("assert(1==1);") );
    }
    
    public void testStmt(){
        Statement st = Stmt.of( ()->System.out.println("Hello") );
    }
    
    public void testLambda(){
        LambdaExpr l = Expr.of(()-> System.out.println("hey") );
    }
    
    public void testExpressions(){
        IntegerLiteralExpr ile = Expr.of(1);
        DoubleLiteralExpr dle = Expr.of(3.14f);        
    }
    
    
}
