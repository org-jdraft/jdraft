package test.byexample.macro.custom;

import com.github.javaparser.ast.Node;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.macro.macro;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;

/**
 * Custom macros in jdraft have (2) main parts:
 * <OL>
 * <LI>an @Annotation declaration that has a Name and (optional) arguments
 * <LI>a (nested) Processor containing code to accept the Runtime Annotation and the AST Node being annotated
 * </OL>
 * <PRE>
 * "How" the macros work is:
 * 1) we pass a java.lang.Class (ClassRef) to "macro"
 * 2) we resolve the .java Source code (Src) for the Class (should be on the ClassPath)
 * 3) we build an AST (TypeAst) from the .java source code (Src)
 * 4) we use Reflection to iterate over all of the members (RuntimeMember) ({@link java.lang.reflect.Field}, {@link java.lang.reflect.Method}...)
 *     4a) for each member, get all RUNTIME Annotation instances (RuntimeAnnoInstance) on the member
 *         -- for each RUNTIME Annotation instances (RuntimeAnnoInstace) get the Class (RuntimeAnnoClass)
 *             ...if the Annotation Class (RuntimeAnnoClass) declares a field of type {@link Consumer}<Node>
 *                or has a nested Class that extends {@link macro}
 *                ... get the value of the (MacroInstance) Field (or create an instance of the nested {@link macro} Class)
 *                ... resolve the AST member node (MemberAst) within the (TypeAst) based on the Runtime member (RuntimeMember)
 *                ... pass the (MemberAst) node and (RuntimeAnnoInstance) to the (MacroInstance)
 *                   (*** the Macro instance can access the parameters stored in the RuntimeAnnoInstance ***)
 * 5) we "process" all Annotation/Macros that are on the Class (ClassRef) last (in the same manner as 4a...)
 * </PRE>
 * @see macro
 * @see org.jdraft.macro package of the existing built in macros (examples to look through)
 */
public class _0_CustomAnnotation_MacroTest extends TestCase {

    /* 1) "Macro Annotation" (@_nodeSrc), MUST have RetentionPolicy.RUNTIME & MUST be public to be processed*/
    @Retention(RetentionPolicy.RUNTIME)
    public @interface _nodeSrc {
        /* 2) this Consumer<Node> field "executes" the macro (the name DOES NOT MATTER, only the Type),
              it sets a System property "node" to be the source of the node it annotated */
        Consumer<Node> NodeSrcAsSystemProperty = (n) -> System.setProperty("node",n.toString() );
    }

    /**
     * When we adorn some Runtime Element with the Annotation, it is attached to its corresponding AST
     * when we resolve the source and build the AST model...
     */
    public void testSimple() {
        /* ...by annotating a class */
        @_nodeSrc class C { }

        _class _c = macro.to(C.class); /* when we call macro.to(C.class) ALL annotation macros are run */

        // verify the macro worked, the System property "node" has the .java source of _c
        assertEquals( _c.toString().trim(), System.getProperty("node"));

        // NOTE: the Consumer<Node> implementation for @A, does not automatically "clean up"
        // the Annotation @_nodeSrc after processing a macro implementation WILL
    }

    /**
     * the "preferred" but more involved way of writing a custom macro is writing a nested
     * class (Impl) under the Annotation Declaration (@Count) that extends {@link macro}
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface _count {
        class Impl extends macro<_count, Node> {
            public Impl(_count b){ super(b); }

            @Override
            public void expand(Node node) {
                System.setProperty("count",  //increment the count each time this is called
                        (Integer.parseInt(System.getProperty("count", "0") ) + 1)+"");
            }
        }
    }

    public void testMacroImplementation() {
        assertNull( System.getProperty("count"));
        @_count class D{
            @_count int i;
            @_count D(){}
            @_count void m(){}
        }
        _class _c = _class.of(D.class); /* NOTE: _class.of(Class) calls macro.to(Class) internally */
        assertFalse( _c.hasAnnoExpr(_count.class)); // macro (_count.Impl) removed the @_count annotation
        assertFalse( _c.getField("i").hasAnnoExpr(_count.class)); // macro (_count.Impl) removed the @_count annotation
        assertFalse( _c.getConstructor(0).hasAnnoExpr(_count.class)); // macro (_count.Impl) removed the @_count annotation
        assertFalse( _c.firstMethodNamed("m").hasAnnoExpr(_count.class)); // macro (_count.Impl) removed the @_count annotation
        assertEquals( "4", System.getProperty("count")); //make sure the macro was called (4) times
    }

    public void tearDown(){
        System.clearProperty("node");
        System.clearProperty("count");
    }
}