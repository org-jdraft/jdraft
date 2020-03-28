package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.Log;
import org.jdraft.diff._diff;
import org.jdraft.macro._static;
import org.jdraft.macro._volatile;
import org.jdraft.macro._addImports;
import org.jdraft.macro._non_static;
import org.jdraft.macro._protected;
import org.jdraft.macro._final;
import org.jdraft.macro._packageName;
import org.jdraft.macro._public;
import junit.framework.TestCase;

import java.util.*;

public class _declaredBodyPartClassTest extends TestCase {

    public void testJ(){
        CompilationUnit cu = StaticJavaParser.parse(
                "/**\n"+
                "*/\n"+
                 "public class SomeClass {\n"+
                 "    List something = new ArrayList();\n"+
                 "}");
    }

    @_packageName("som.pkg")
    @_addImports({Map.class, List.class, Set.class, HashSet.class})
    @_non_static @_public @_final
    private static class Baseline{

        static{
            System.out.println("replaced");
        }

        @_volatile @_public
        int a;

        @_protected @_static @_final
        public int g(){
            return 102;
        }
    }

    public void testL(){

        _class _c = _class.of(Baseline.class);

        @_public @_packageName("som.pkg") @_final @_addImports({Map.class, List.class, Set.class, HashSet.class})
        class Baseline{
            volatile public int a;
            protected @_static final int g() { return 102; }

        }
        _class _d = _class.of(Baseline.class).staticBlock( ()->System.out.println("replaced"));
        /*
        _class _d = _class.of("som.pkg.Baseline",
                new @_public @_final @_imports({Map.class, List.class, Set.class, HashSet.class}) Object(){
                    volatile public int a;

                    protected @_static final int g() { return 102; }

                }).staticBlock( ()->System.out.println("replaced"));
        */
        assertTrue( _diff.of(_c, _d).isEmpty());

        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        assertEquals(_c, _d);

        System.out.println(_c);
    }
}
