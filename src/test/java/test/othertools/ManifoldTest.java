package test.othertools;

import java.net.URL;
import javax.swing.ImageIcon;

//import com.sun.javadoc.SourcePosition;
import org.jdraft._class;
import org.jdraft.macro._packageName;
import org.jdraft.macro._public;
import org.jdraft.macro._static;

//import com.sun.tools.javac.util.BasicDiagnosticFormatter.BasicConfiguration.SourcePosition;

import junit.framework.TestCase;

/**
 * Example of Source Creation in jdraft that emulates the
 *
 */
public class ManifoldTest extends TestCase {

    /**
     * https://github.com/manifold-systems/manifold/tree/master/manifold-core-parent/manifold
     *
     return new SrcClass(_fqn, SrcClass.Kind.Class)
     .imports(URL.class, SourcePosition.class)
     .superClass(new SrcType(ImageIcon.class))
     .addField(new SrcField("INSTANCE", simpleName).modifiers(Modifier.STATIC))
     .addConstructor(new SrcConstructor()
     .addParam(new SrcParameter("url")
     .type(URL.class))
     .modifiers(Modifier.PRIVATE)
     .body(new SrcStatementBlock()
     .addStatement(new SrcRawStatement()
     .rawText("super(url);"))
     .addStatement(new SrcRawStatement()
     .rawText("INSTANCE = this;"))))
     .addMethod(new SrcMethod().modifiers(Modifier.STATIC)
     .name("get")
     .returns(simpleName)
     .body(new SrcStatementBlock()
     .addStatement(
     new SrcRawStatement()
     .rawText("try {")
     .rawText("  return INSTANCE != null ? INSTANCE : new " + simpleName +
     "(new URL("\\" + ManEscapeUtil.escapeForJavaStringLiteral(_url) + "\\"));")
     .rawText("} catch(Exception e) {")
     .rawText("  throw new RuntimeException(e);")
     .rawText("}"))));
     */
    public void testImageCodeGen() {
        @_packageName("manifold.imagecodegen")
        @_public class Sample extends ImageIcon{

            @_static Sample INSTANCE;

            private Sample(URL url) {
                super(url);
            }

            public @_static Sample get() {
                try {
                    return INSTANCE != null ? INSTANCE
                            //"\\" + ManEscapeUtil.escapeForJavaStringLiteral(_url) + "\\"
                            : new Sample( new URL( "\\http://google.com\\" ));
                }catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        _class _c = _class.of(Sample.class);
                //.imports(SourcePosition.class);

        System.out.println( _c);
    }
}