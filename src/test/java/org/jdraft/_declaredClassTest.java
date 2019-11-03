package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.utils.Log;
import org.jdraft.diff._diff;
import org.jdraft.macro._static;
import org.jdraft.macro._volatile;
import org.jdraft.macro._imports;
import org.jdraft.macro._non_static;
import org.jdraft.macro._protected;
import org.jdraft.macro._final;
import org.jdraft.macro._package;
import org.jdraft.macro._public;
import junit.framework.TestCase;

import java.util.*;

public class _declaredClassTest extends TestCase {

    public void testJ(){
        CompilationUnit cu = StaticJavaParser.parse(
                "/**\n"+
                "*/\n"+
                 "public class SomeClass {\n"+
                 "    List something = new ArrayList();\n"+
                 "}");
    }

    @_package("som.pkg")
    @_imports({Map.class, List.class, Set.class, HashSet.class})
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
        _class _d = _class.of("som.pkg.Baseline",
                new @_public @_final @_imports({Map.class, List.class, Set.class, HashSet.class}) Object(){
                    volatile public int a;

                    protected @_static final int g() { return 102; }

                }).staticBlock( ()->System.out.println("replaced"));

        assertTrue( _diff.of(_c, _d).isEmpty());

        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
        assertEquals(_c, _d);

        System.out.println(_c);
    }
}
