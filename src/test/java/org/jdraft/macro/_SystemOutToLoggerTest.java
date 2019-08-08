package org.jdraft.macro;

import com.github.javaparser.ast.ImportDeclaration;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._class;
import org.jdraft._field;
import org.jdraft.runtime._runtime;
import org.jdraft.runtime._classFile;
import org.jdraft.io._batch;
import org.jdraft.proto.$field;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class _SystemOutToLoggerTest extends TestCase {
    //org.jdraft.macro._replaceSystemOutWithLog rep = new _replaceSystemOutWithLog

    static _replaceSystemOutWithLog LOGWARN = new _replaceSystemOutWithLog(
            (_field f)->f.isStatic() && f.isType(Logger.class),
            new ImportDeclaration[] { Ast.importDeclaration( Logger.class ),Ast.importDeclaration( Level.class )},
            $field.of("public static final Logger LOG = Logger.getLogger($className$.class.getCanonicalName());"),
            "$name$.warning($any$ + \"\");" );

    public static void main(String[] args){
        //here's where all the test cases are
        _batch _b = _batch.of(Paths.get("C:\\jdraft\\project\\jdraft\\src\\test"));
        List<_class> testClasses = new ArrayList<>();
        _b.for_types(_class.class, c-> c.isExtends(TestCase.class), c-> testClasses.add(c));

        //convert System outs to LOG WARN
        testClasses.stream().forEach( t-> {
            System.out.println( t.getName() );
            LOGWARN.apply(t);
        } );

        //now compile all tet classes
        List<_classFile> _bcfs = _runtime.compile(testClasses);
    }

    public void testSingle() {
        class C{
            public @_static void m() {
                //System.out.println("Hello");
                String rel1 = "1";
                String rel2 = "2";
                System.out.println( rel1);
                System.out.println( rel2);
            }
        }
        _class _c = _class.of( C.class );

        _replaceSystemOutWithLog lr = new _replaceSystemOutWithLog(
                (_field f)->f.isStatic() && f.isType(Logger.class),
                new ImportDeclaration[] { Ast.importDeclaration( Logger.class ),Ast.importDeclaration( Level.class )},
                $field.of("public static final Logger LOG = Logger.getLogger($className$.class.getCanonicalName());"),
                "$name$.warning($any$ + \"\");" );

        //apply the macro

        _c = (_class)lr.apply(_c);
        _runtime.of(_c).call(_c, "m");
    }

    public static final Logger LOG = Logger.getLogger(_SystemOutToLoggerTest.class.getCanonicalName());

    public void testLL(){
        LOG.severe("A MESSAGE");
    }
}
