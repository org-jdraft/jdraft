package org.jdraft.runtime;

import java.util.ArrayList;
import java.util.List;
import org.jdraft._class;
import org.jdraft.macro.*;
import junit.framework.TestCase;

public class _javacTest extends TestCase {

    public void testJ(){
        _class _c = _toString.Act.to( _class.of("aaaa.bbb.C").addField("int x,y,z;"));
        List<_classFile> _cfs = _runtime.compile(_c);

        //use
        //_javac.of( _c);
        List<String>javacOptions = new ArrayList<>();
        javacOptions.add("-parameters");
        _runtime.compile( javacOptions, _c);
    }


    //compile & return bytecode

    //Compiling
    //Loading & using
    //Integrating with Popular Tools (JavaPoet, JavaParser)
    //at it's most basic, adhoc helps you compile (in memory) java code
    public void testCompile(){
        //at it's most basic, the Adhoc will compile Java code at runtime
        _runtime.compile("public class A{}");

        //when compiling, you get back a Map of className to bytecode
        List<_classFile> bytecode = _runtime.compile("public class A{}");

        //you can pass compiler options to Adhoc
        //beyond compiling Adhoc lets you also load and use dynamic classes

    }

    //but the bigger picture is that it integrates with existing Code generation
    //tools (like JavaParser & JavaPoet)


    //compile and load classes
    public void testSimple(){
        _runtime adhoc = _runtime.of(  "public class A{}");
    }

    //compile with compile options
    public void testCompileWithCompilerOptionsAndWarningsEnabled(){
        List<String> compilerOptions = new ArrayList<>();
        compilerOptions.add("-parameters");
        boolean ignoreWarnings = false;

        List<_classFile> bytecodeMap =
                _runtime.compile(compilerOptions, ignoreWarnings,  "public class A{}");
    }


    /** TODO Maybe figure this out later 
    public void testParentChildClassLoader(){
        _class _a = _class.of("aaaa.bbbb.A", new Object(){ int x,y,z; }, _dto.$);
        _class _b = _class.of("aaaa.bbbb.B", new Object(){ float a, b,c;}, _dto.$).extend(_a);
        //System.out.println( _a );

        //I HAVE to call _new b/c I have to realize/ lo
        AdhocClassLoader _parentClassLoader = (AdhocClassLoader)_new.of(_a).getClass().getClassLoader();

        //compile b using the base class Loader (_clA) containing _a, it's base class
        //Adhoc.compile(_parentClassLoader, _b );
        //_project.of(_parentClassLoader, _b );

    }
    */ 
}
