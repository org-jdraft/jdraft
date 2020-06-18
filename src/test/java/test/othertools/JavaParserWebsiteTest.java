package test.othertools;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._project;
import org.jdraft.macro._static;
import org.jdraft.pattern.$;
import org.jdraft.pattern.$class;
import org.jdraft.pattern.$field;
import org.jdraft.runtime._runtime;

/**
 * Build and emulate the examples from javaparser.org website
 *
 */
public class JavaParserWebsiteTest extends TestCase {

    /**
     * from JavaParser.org
     * <PRE><CODE>
     * CompilationUnit compilationUnit = StaticJavaParser.parse("class A{}");
     * Optional<ClassOrInterfaceDeclaration> classA = compilationUnit.getClassByName("A");
     * </CODE></PRE>
     */
    public void testParse(){
        _class _c = _class.of("public class A{}"); //this does the parsing
        assertEquals( _c ,_class.of("A")); //there are "shortcuts" to creating classes
    }

    public void testAnalyze(){

        //here we create a simple class for testing
        class C{
            public int i;
            private String name;
            public @_static int blarg;
            public int cc;
        }
        //look for all public fields that are NOT static
        $field.of($.PUBLIC, $.NOT_STATIC).forEachIn(C.class,
                f-> System.out.println("Check field at line "+f.node().getRange().map(r-> r.begin.line).orElse(-1)));
    }

    enum E{ A;}
    interface I{}

    public void testTransform(){
        abstract class G{ }
        abstract class H{ }
        abstract class AbstractI{ }

        //here read and cache ASTs for the source code for these classes
        _project _s = _project.of(E.class, G.class, H.class, AbstractI.class, I.class);

        //ensure all abstract classes names to start with "Abstract"
        $class.of( $.ABSTRACT ).$not( c-> c.getSimpleName().startsWith("Abstract") )
                .forEachIn( _s, s-> s.setName( "Abstract"+ s.getSimpleName()) );

        //verify there are no abstract classes that have names without the "Abstract" prefix
        assertEquals(0, $class.of( $.ABSTRACT ).$not( c-> c.getSimpleName().startsWith("Abstract") ).countIn(_s));
    }

    public void testGenerate(){
        _class _c = _class.of("MyClass", new Object(){
            public @_static int A_CONSTANT;
            private String name;
        });
        String code = _c.toString();
        System.out.println(code);

        //we can also compile the class
        _runtime.compile( _c );
    }
}
