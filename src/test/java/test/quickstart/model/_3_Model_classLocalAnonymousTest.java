package test.quickstart.model;

import com.github.javaparser.ast.stmt.ExpressionStmt;

import junit.framework.TestCase;
import java.io.FileNotFoundException;
import java.util.UUID;

import org.jdraft.*;
import org.jdraft.runtime._runtime;

/**
 * Alternative ways of building jdraft _class (using nested classes, local classes, anonymous classes)
 * verify the _class is valid Class by calling _runtime.compile()
 */
public class _3_Model_classLocalAnonymousTest extends TestCase {

    public static class ANestedClass{
        int a;
        public ANestedClass( int a ){
            this.a = a;
        }
        @Deprecated public int m(){ return 100; }
    }

    /**
     * in addition to creating a _model representation from a String (for a _method or other model),
     * jdraft can build a model (_class) from a Runtime Reference to a Nested class (*)
     * (*)assuming the source code for the Class is on the classpath
     */
    public void testBuildModelFromNestedClass() {
        // we can build a _class from the reference to a Nested Class
        _class _c = _class.of(ANestedClass.class);

        // just as with _method, the _class (_c) we can access properties from the AST
        assertEquals(ANestedClass.class.getSimpleName(), _c.getName());
        assertEquals(ANestedClass.class.getCanonicalName(), _c.getFullName());
    }

    public void testBuildModelFromAnonymousObject() {
        class LocalClass {
            int i=0;
            public void setI(int i){
                this.i = i;
            }
        }
        // here we can build a _class from a Local class defined within the method
        _class _c = _class.of( LocalClass.class);

        // when we ask the _class (_c) to getMethod(), it will create a wrapper _method (_m)
        // to the MethodDeclaration "on demand", you can use the _method (_m), and it'll get
        // garbage-collected when it goes at of scope (but it's stateless, so that's fine)
        _method _m = _c.getMethod("setI");

        // we can get the statements in the method
        // NOTE: Statements are "unwrapped" JavaParser Statement types, (no corresponding jdraft _models)
        // Also, Comments DO NOT count as Statements
        assertTrue( _m.getAstStatement(0 ) instanceof ExpressionStmt); // the first Statement is:
        // _class _c = _class.of( ANestedClass.class) ;

        // ...an "ExpressionStmt" is a JavaParser Statement implementation... later we will discuss how to
        // manipulate and analyze JavaParser Statements, but for now understand Statements are on the AST
        // and the _method (_m) can access the Statements from the MethodDeclaration directly
    }

    public void testFromAnonymousClass(){
        // Here, we construct _class with the Canonical name ("aaaa.bbbb.C") for a _class & an Anonymous
        // Object with a Field (_field _f)and Method (_method _m)
        // of a _method we pass in an Anonymous Object containing a method...)
        _class _c = _class.of("aaaa.bbbb.C", new Object(){
            public int val = 123;

            @Deprecated UUID id() throws FileNotFoundException {
                 return UUID.randomUUID();
            }
        });
        // NOTE: upon the construction of the _class model, the _class.of() factory method will
        // inspect the runtime API of the anonymous Object, and determine that we need to manually import
        // java.util.UUID & java.io.FileNotFoundException.  This import happens automatically.

        assertEquals( "aaaa.bbbb.C", _c.getFullName() );

        // each time we get or list elements (like _field, _method, _constructor,...)
        _field _f = _c.getField("val");
        assertTrue(_f.isInit(123)); //the init value of the field is 123

        _method _m = _c.getMethod("id" );
        assertTrue( _m.isTypeRef( UUID.class ) );
        assertTrue( _m.hasAnnoExpr( Deprecated.class ) );
        _m.setStatic().setFinal().setPublic();

        // the _class has methods to add elements (like _field/_method/_constructor/_initBlock) using Strings
        _c.addConstructor("public C(final int val){ this.val = val; }");
        _c.addStaticBlock("System.out.println(\"A Static Block\");"); //this is the body of the static block

        // to verify _class (_c) represents a valid Java Class, we call the javac compiler
        _runtime.compile(_c); // if not this line will fail and give you a stacktrace from javac

        System.out.println( _c ); //print the .java source code
    }
}