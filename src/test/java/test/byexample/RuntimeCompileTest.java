package test.byexample;

import junit.framework.TestCase;
import org.jdraft._class;
import org.jdraft.runtime._classFile;
import org.jdraft.runtime._runtime;
import org.jdraft.runtime._runtimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RuntimeCompileTest extends TestCase {

    /** we can compile source code (as String) directly to return _classFile(s) */
    public void testCompile(){
        List<_classFile> _cfs = _runtime.compile("package aa.bb;", "public class C{}");
        _cfs = _runtime.compile(_class.of("aa.bb.C"));
        assertEquals(1, _cfs.size());
        assertEquals("aa.bb.C", _cfs.get(0).getFullyQualifiedClassName());
        byte[] bytecode = _cfs.get(0).getBytecode(); //we can get the underlying bytecode from the _classFile
    }
    
    /** passing in compiler options */
    public void testCompileWithCompilerOptions(){
        //build a class that uses javba 8+ features (here lambda)
        _class _c = _class.of("aaaa.bbb.C").imports(Consumer.class).main( ()-> {
            Consumer<String> ps = (String s )-> System.out.println( s );
        });
        List<_classFile> _cfs= _runtime.compile(_c);
        
        /* pass in javac compiler Options to accept only Java 1.7 compatibility */ 
        List<String> compilerOptions = new ArrayList<>();
        compilerOptions.add("-source");
        compilerOptions.add("1.7");
        try {
            _cfs = _runtime.compile(compilerOptions, _c);
            fail("Expected Exception compiling a Java 8 feature (lambda) with -source 1.7 compatibility");
        }catch(_runtimeException e){
            //expect an exception
        }
    }
}
