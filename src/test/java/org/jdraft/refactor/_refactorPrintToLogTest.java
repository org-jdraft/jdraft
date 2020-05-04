package org.jdraft.refactor;

import com.github.javaparser.ast.ImportDeclaration;
import junit.framework.TestCase;
import org.jdraft.Ast;
import org.jdraft._class;
import org.jdraft._project;
import org.jdraft._field;
import org.jdraft.macro._static;
import org.jdraft.runtime._runtime;
import org.jdraft.runtime._classFile;
import org.jdraft.io._path;
import org.jdraft.pattern.$field;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class _refactorPrintToLogTest extends TestCase {

    static _refactorPrintToLog LOGWARN = new _refactorPrintToLog(
            (_field f)->f.isStatic() && f.isTypeRef(Logger.class),
            new ImportDeclaration[] { Ast.importDeclaration( Logger.class ),Ast.importDeclaration( Level.class )},
            $field.of("public static final Logger LOG = Logger.getLogger($className$.class.getCanonicalName());"),
            "$name$.warning($any$ + \"\");" );

    public static void main(String[] args){
        //here's where all the test cases are
        _project _b = _path.of(Paths.get("C:\\jdraft\\project\\jdraft\\src\\test")).load();
        List<_class> testClasses = new ArrayList<>();
        _b.forEach(_class.class, c-> c.isExtends(TestCase.class), c-> testClasses.add(c));

        //convert System outs to LOG WARN
        testClasses.stream().forEach( t-> {
            System.out.println( t.getName() );
            LOGWARN.expand(t);
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

        _refactorPrintToLog lr = new _refactorPrintToLog(
                (_field f)->f.isStatic() && f.isTypeRef(Logger.class),
                new ImportDeclaration[] { Ast.importDeclaration( Logger.class ),Ast.importDeclaration( Level.class )},
                $field.of("public static final Logger LOG = Logger.getLogger($className$.class.getCanonicalName());"),
                "$name$.warning($any$ + \"\");" );

        //apply the macro

        lr.expand(_c);
        _runtime.of(_c).call(_c, "m");
    }

    public static final Logger LOG = Logger.getLogger(_refactorPrintToLogTest.class.getCanonicalName());

    public void testLL(){
        LOG.severe("A MESSAGE");
    }
}
