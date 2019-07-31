package org.jdraft.adhoc;

import java.util.*;
import junit.framework.TestCase;

/**
 * Explain what 
 * @author Eric
 */
public class ExamplesTest extends TestCase {
    
    //compile & return bytecode
    
    //Compiling
    //Loading & using
    //Integrating with Popular Tools (JavaPoet, JavaParser)
    //at it's most basic, adhoc helps you compile (in memory) java code
    public void testCompile(){
        //at it's most basic, the Adhoc will compile Java code at runtime
        _adhoc.compile("public class A{}");
        
        //when compiling, you get back a Map of className to bytecode  
        List<_bytecodeFile> bytecode = _adhoc.compile("public class A{}");
        
        //you can pass compiler options to Adhoc 
        //beyond compiling Adhoc lets you also load and use dynamic classes 
        
    }
    
    //but the bigger picture is that it integrates with existing Code generation
    //tools (like JavaParser & JavaPoet)
    
    
    //compile and load classes
    public void testSimple(){
        _adhoc adhoc = _adhoc.of(  "public class A{}");
    }
    
    //compile with compile options
    public void testCompileWithCompilerOptionsAndWarningsEnabled(){
        List<String> compilerOptions = new ArrayList<>();
        compilerOptions.add("-parameters");
        boolean ignoreWarnings = false;
        
        List<_bytecodeFile> bytecodeMap = 
            _adhoc.compile(compilerOptions, ignoreWarnings,  "public class A{}");        
    }
    
   
}
