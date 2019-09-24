package test.byexample;

import com.github.javaparser.ast.body.TypeDeclaration;
import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft._type;
import org.jdraft.macro._macro;
import org.jdraft.macro.macro;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;

/**
 * Build your own "annotation/macro" That can be applied to code
 */
public class AnnotationMacroTest extends TestCase {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface _annoMac{

        //public static Act A = new Act();

        class Macro extends macro<_annoMac, TypeDeclaration> {

            public Macro(_annoMac _a){
                super(_a);
            }

            @Override
            public void expand(TypeDeclaration type) {
                System.out.println( "HERE "+type);
            }
        }
        /*
        class Act implements Consumer<TypeDeclaration> {
            @Override
            public void accept(TypeDeclaration typeDeclaration) {
                System.out.println( "Macro Called");
            }
        }
         */
    }

    public void testAM(){
        @_annoMac class C{

        }
        _class _c = _class.of(C.class);
    }
}
