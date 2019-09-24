package org.jdraft.runtime;

import java.util.*;
import javax.tools.JavaFileObject;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.jdraft.*;

/**
 * Simple API to adapt all of the functionality in the
 * statically, so you don't have to always use:
 * 
 * Compiles and Loads BOTH draft based {@link _code} entities:
 * <UL>
 *    <LI>{@link org.jdraft._type}
 *    <UL>
 *      <LI>{@link org.jdraft._class}
 *      <LI>{@link org.jdraft._interface}
 *      <LI>{@link org.jdraft._enum}
 *      <LI>{@link org.jdraft._annotation}
 *    </UL>
 *    <LI>{@link org.jdraft._packageInfo}
 *    <LI>{@link org.jdraft._moduleInfo}
 * </UL>   
 * 
 * AND JavaParser based {@link com.github.javaparser.ast.CompilationUnit}s
 * 
 * @author Eric
 */
public class _runtime {
    
    /**
     * Javac compiler for compiling java source to bytecode classes
     */
    public static final JavaCompiler JAVAC = ToolProvider.getSystemJavaCompiler();
    
    public static _javaFile file(_code code) {
        return new _javaFile(code);
    }    
        
    /**
     * compile a simple class defined by the fullyQualifiedClassName and code
     * content
     *
     * @param codeLines
     * @return
     */
    public static List<_classFile> compile(String... codeLines) {
        return compile((_type)_java.type(codeLines));
    }

    /**
     *
     * @param compilerOptions
     * @param codeLines
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static List<_classFile> compile(List<String> compilerOptions, String... codeLines) {
        return compile(compilerOptions, (_type)_java.type(codeLines));
    }

    /**
     *
     * @param compilerOptions
     * @param ignoreWarnings
     * @param codeLines
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static List<_classFile> compile(List<String> compilerOptions, boolean ignoreWarnings, String... codeLines) {
        return compile(compilerOptions, ignoreWarnings, (_type)_java.type(codeLines));
    }
    
    /**
     * compiles draft code and returns a Map containing the className and bytecode
     * @param codeArray the draft _code instances (i.e. {@link org.jdraft._class}, {@link org.jdraft._interface})
     * @return map of the className to the bytecode
     */
    public static List<_classFile> compile(_code...codeArray){
        List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(codeArray).forEach( f -> fs.add( _javaFile.of(f)));
        return compile(fs);
    }

    /**
     *
     * @param codeList
     * @param <C>
     * @return
     */
    public static <C extends _code> List<_classFile> compile(List<C> codeList){
        List<JavaFileObject> fs = new ArrayList<>();
        codeList.forEach( f -> fs.add( _javaFile.of(f)));
        return compile(fs);
    }

    /**
     *
     * @param codeFiles
     * @return
     */
    public static List<_classFile> compile(Collection<JavaFileObject> codeFiles) {
        return compile(Collections.EMPTY_LIST, true, codeFiles);
    }

    /**
     * compiles draft code, passes compiler options to the compiler
     * @param compilerOptions
     * @param codeArray
     * @return 
     */
    public static List<_classFile> compile(List<String>compilerOptions, _code...codeArray){
         List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(codeArray).forEach( f -> fs.add( _javaFile.of(f)));
        return compile(compilerOptions, true, fs);
    }
    
    /**
     * Compilers JavaParser {@link com.github.javaparser.ast.CompilationUnit}s
     * and returns the bytecode
     * @param code compilationUnits to be compiled
     * @return Map of bytecode by className
     */
    public static List<_classFile> compile(CompilationUnit...code){
        List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(code).forEach( f -> fs.add( _javaFile.of(f)));
        return compile(fs);
    }
    
    /**
     *
     * @param codeFiles
     * @return
     */
    public static List<_classFile> compile(JavaFileObject... codeFiles) {
        return compile(Collections.EMPTY_LIST, true, Arrays.asList(codeFiles));
    }
    
    /**
     *
     * @param compilerOptions
     * @param codeFiles
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static List<_classFile> compile(List<String> compilerOptions, JavaFileObject... codeFiles) {
        return compile(compilerOptions, true, Arrays.asList(codeFiles));
    }

    /**
     * 
     * @param compilerOptions
     * @param code
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static List<_classFile> compile(List<String>compilerOptions, CompilationUnit...code){
        List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(code).forEach( f -> fs.add( _javaFile.of(f)));
        return compile(compilerOptions, true, fs);
    }
    
    /**
     * 
     * @param compilerOptions
     * @param ignoreWarnings
     * @param code
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static List<_classFile> compile(List<String>compilerOptions, boolean ignoreWarnings, CompilationUnit...code){
        List<JavaFileObject> codeModelFiles = new ArrayList<>();
        Arrays.stream(code).forEach(c -> codeModelFiles.add( new _javaFile(c)));
        return compile(compilerOptions, ignoreWarnings, codeModelFiles);
    }
        
    /**
     * compiles draft code, passes compilerOptions and whether to ignore warnings
     * into the compiler
     * 
     * @param compilerOptions
     * @param ignoreWarnings
     * @param code
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static List<_classFile> compile(List<String>compilerOptions, boolean ignoreWarnings, _code...code){
        List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(code).forEach( f -> fs.add( _javaFile.of(f)));
        return compile(compilerOptions, ignoreWarnings, fs);
    }

    /**
     * Compile and load the _type and return it's Class file
     * (NOTE: will only return the top level class file, but will compile it's
     * nested or companion types)
     * @param _t the _type to compile
     * @return the Compiled and Loaded Class
     */
    public static <_T extends _type> Class<?> Class(_T _t ){
        return Class( new ArrayList<>(), true, _t);
    }

    /**
     * Compile and load the _type and return it's Class file
     * (NOTE: will only return the top level class file, but will compile it's
     * nested or companion types)
     * @param compilerOptions
     * @param _t
     * @return
     */
    public static <_T extends _type> Class<?> Class(List<String>compilerOptions, _T _t ){
        return Class( compilerOptions, true, _t);
    }

    /**
     * Compile and load the _type and return it's Class file
     * (NOTE: will only return the top level class file, but will compile it's
     * nested or companion types)
     *
     * @param compilerOptions
     * @param ignoreWarnings
     * @param _t
     * @return a runtime class compiled and loaded into a new classloader
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static <_T extends _type> Class<?> Class(List<String>compilerOptions, boolean ignoreWarnings, _T _t ){
          _runtime _rt = of(compilerOptions, ignoreWarnings, _t);
          return _rt.getClass(_t);
    }

    /**
     * 
     * @param _c the model of the class
     * @param ctorArgs the constructor args
     * @return 
     */
    public static _proxy proxyOf(_class _c, Object...ctorArgs){
        return _runtime.of(_c).proxy(_c, ctorArgs);
    }
    
    /**
     * 
     * @param compilerOptions the compiler options for creating the source files
     * @param _c the model of the class
     * @param ctorArgs the constructor args
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static _proxy proxyOf(List<String>compilerOptions, _class _c, Object...ctorArgs){
        return _runtime.of(compilerOptions, _c).proxy(_c, ctorArgs);
    }
    
    /**
     * 
     * @param astCu
     * @param ctorArgs
     * @return 
     */
    public static _proxy proxyOf(CompilationUnit astCu, Object...ctorArgs){
        _class _c = _class.of(astCu);
        return _runtime.of(_c).proxy(_c, ctorArgs);
    }

    /**
     * @param compilerOptions
     * @param astCu
     * @param ctorArgs
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static Object instanceOf(List<String> compilerOptions, CompilationUnit astCu, Object...ctorArgs){
        _class _c = _class.of(astCu);
        return _runtime.of(compilerOptions, _c).instance(_c, ctorArgs);
    }    
    
    /**
     * 
     * @param _c the model of the class
     * @param ctorArgs the constructor args
     * @return

     *
     */
    public static Object instanceOf(_class _c, Object...ctorArgs){
        return _runtime.of(_c).instance(_c, ctorArgs);
    }
    
    /**
     * 
     * @param compilerOptions the compiler options for creating the source files
     * @param _c the model of the class
     * @param ctorArgs the constructor args
     * @return 
     */
    public static Object instanceOf(List<String>compilerOptions, _class _c, Object...ctorArgs){
        return _runtime.of(compilerOptions, _c).instance(_c, ctorArgs);
    }
    
    /**
     * 
     * @param astCu
     * @param ctorArgs
     * @return 
     */
    public static Object instanceOf(CompilationUnit astCu, Object...ctorArgs){
        _class _c = _class.of(astCu);
        return _runtime.of(_c).instance(_c, ctorArgs);
    }

    /**
     *
     * @param expr
     * @return
     */
    public static Object eval(Expression expr){
        _class _c = _class.of("adhoc.ExprEval")
            .method("public static Object eval(){",
                "return "+expr+";",
                "}");
        return proxyOf(_c).call("eval");
    }
    
    /**
     * @param compilerOptions
     * @param astCu
     * @param ctorArgs
     * @return 
     */
    public static _proxy proxyOf(List<String> compilerOptions, CompilationUnit astCu, Object...ctorArgs){
        _class _c = _class.of(astCu);
        return _runtime.of(compilerOptions, _c).proxy(_c, ctorArgs);
    }    
    
    /**
     * builds a new _type from the javaSource code then compiles and loads the 
     * Adhoc and returns it
     * @param javaSourceCode java source code to create a _type from
     * @return the _adhoc with the compiled Classes
     */
    public static _runtime of(String...javaSourceCode ){
        return _runtime.of((_type)_java.type(javaSourceCode));
    }
    
    /**
     * 
     * @param macroAnnotatedClasses one or more Runtime Classes that are annotated
     * @return an _adhoc with compiled classes and resulting _code models after running
     * macros on the Classes
     */
    public static _runtime of(Class...macroAnnotatedClasses){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(macroAnnotatedClasses).forEach(c->sfs.add( _javaFile.of((_type)_java.type(c))));
        return of(Collections.EMPTY_LIST, true, sfs);
    }
    
    /**
     * 
     * @param compilerOptions options to pass to the Javac compiler
     * @param macroAnnotatedClasses classes marked up with macro annotations (i.e. @_autoDto)
     * @return the adhoc (containing the compiled classes and _code source models)
     */
    public static _runtime of(List<String>compilerOptions, Class...macroAnnotatedClasses){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(macroAnnotatedClasses).forEach(c->sfs.add( _javaFile.of( (_type)_java.type(c))));
        return of(compilerOptions, true, sfs);
    }
    
    /**
     * 
     * @param compilerOptions options to pass to the Javac compiler
     * @return the adhoc (containing the compiled classes and _code source models)
     */
    public static _runtime of(List<String>compilerOptions, _javaFile...javaFiles){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(javaFiles).forEach(c->sfs.add( c));
        return of(compilerOptions, true, sfs);
    }
    
    /**
     * 
     * @param compilerOptions options to pass to the Javac compiler
     * @param ignoreWarnings if compiler warnings should be ignored
     * @param macroAnnotatedClasses classes marked up with macro annotations (i.e. @_autoDto)
     * @return the adhoc (containing the compiled classes and _code source models)
     */
    public static _runtime of(List<String>compilerOptions, boolean ignoreWarnings, Class...macroAnnotatedClasses){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(macroAnnotatedClasses).forEach(c->sfs.add( _javaFile.of( (_type)_java.type(c))));
        return of(compilerOptions, ignoreWarnings, sfs);
    }
    
    /**
     * 
     * @param codeArray
     * @return 
     */
    public static _runtime of(_code...codeArray){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(codeArray).forEach(c-> sfs.add( _javaFile.of(c)));
        return _runtime.of(Collections.EMPTY_LIST, true, sfs);
    }

    /**
     *
     * @param codeList
     * @param <C>
     * @return
     */
    public static <C extends _code> _runtime of(List<C> codeList ){
        List<JavaFileObject> sfs = new ArrayList<>();
        codeList.forEach(c-> sfs.add( _javaFile.of(c)));
        return _runtime.of(Collections.EMPTY_LIST, true, sfs);
    }

    /**
     *
     * @param jfs
     * @return
     */
    public static _runtime of(_javaFile...jfs ){
        return _runtime.of(Collections.EMPTY_LIST, true, Arrays.asList( jfs) );
    }
    
    /**
     * 
     * @param compilerOptions
     * @param code
     * @return 
     */
    public static _runtime of(List<String>compilerOptions, _code...code){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(code).forEach(c-> sfs.add( _javaFile.of(c)));  
        return _runtime.of(compilerOptions, true, sfs);
    }
    
    /**
     * 
     * @param compilerOptions
     * @param ignoreWarnings
     * @param code
     * @return 
     */
    public static _runtime of(List<String>compilerOptions, boolean ignoreWarnings, _code...code){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(code).forEach(c-> sfs.add( _javaFile.of(c)));  
        return _runtime.of(compilerOptions, ignoreWarnings, sfs);
    }
    
    /**
     * 
     * @param code
     * @return 
     */
    public static _runtime of(CompilationUnit...code){
        List<JavaFileObject> cfs = new ArrayList<>();
        Arrays.stream(code).forEach(c -> cfs.add( _javaFile.of(c)));
        return _runtime.of(Collections.EMPTY_LIST, true, cfs);
    }
    
    /**
     * 
     * @param compilerOptions
     * @param code
     * @return 
     */
    public static _runtime of(List<String>compilerOptions, CompilationUnit...code){
        List<JavaFileObject> codeModelFiles = new ArrayList<>();
        Arrays.stream(code).forEach(c -> codeModelFiles.add( new _javaFile(c)));
        return _runtime.of(compilerOptions, true, codeModelFiles);
    }
    
    /**
     * 
     * @param compilerOptions
     * @param ignoreWarnings
     * @param code
     * @return 
     */
    public static _runtime of(List<String>compilerOptions, boolean ignoreWarnings, CompilationUnit...code){
        List<JavaFileObject> codeModelFiles = new ArrayList<>();
        Arrays.stream(code).forEach(c -> codeModelFiles.add( new _javaFile(c)));
        return _runtime.of(compilerOptions, ignoreWarnings, codeModelFiles);
    }
    
    /**
     * 
     * @param compilerOptions
     * @param ignoreWarnings
     * @param codeModelFiles
     * @return 
     */
    public static _runtime of(List<String> compilerOptions, boolean ignoreWarnings, Collection<JavaFileObject> codeModelFiles){
        
        _classLoader classLoader = new _classLoader( ClassLoader.getSystemClassLoader() );
         
        _fileManager fileManager = 
            new _fileManager(JAVAC.getStandardFileManager(null, null, null), classLoader);
                
        callCompiler(fileManager, compilerOptions, ignoreWarnings, codeModelFiles);
        
        //now load all of the classes into a classLoader
        Map<String, Class<?>> classes = new HashMap<>();
        for( String className: classLoader.classNameTo_classFile.keySet()){
            try{
                classes.put(className, classLoader.loadClass(className));
            }catch(ClassNotFoundException cnfe){
                throw new _runtimeException("Unable to find class "+className, cnfe);
            }
        }
        Map<String,_javaFile> classToCodeModel = new TreeMap<>();
        
        codeModelFiles.forEach(
            c-> classToCodeModel.put(
                c.getName().substring(1, c.getName().length() - ".java".length()).replace("/","."), 
                    (_javaFile)c) );
        fileManager.classLoader.classNameTo_javaFile = classToCodeModel;
        return new _runtime( fileManager );
    }
    
    /**
     * The fileManager, storing the _classLoader & containing the _code and 
     * compiled bytecode
     */
    _fileManager fileManager;
    
    private _runtime(_fileManager fileManager){
        this.fileManager = fileManager;
    }

    /**
     *
     * @param _m
     * @param args
     * @return
     */
    public Object call(_method _m, Object...args){
        if( _m.isStatic() ){
            Optional<TypeDeclaration> otd = _m.ast().findAncestor(TypeDeclaration.class);
            if( otd.isPresent() ){
                TypeDeclaration td = otd.get();
                String fullTypeName = (String)td.getFullyQualifiedName().get();
                return call( fullTypeName, _m.getName(), args);
            }
        } 
        throw new _runtimeException("Unable to call method "+_m);
    }

    /**
     *
     * @param _c
     * @param methodName
     * @param args
     * @return
     */
    public Object call(_class _c, String methodName, Object...args){
        return invokeStatic(getClass(_c), methodName, args);
    }

    /**
     *
     * @param fullyQualifiedClassName
     * @param methodName
     * @param args
     * @return
     */
    public Object call(String fullyQualifiedClassName, String methodName, Object... args) {
        return invokeStatic(getClass(fullyQualifiedClassName), methodName, args);
    }

    /**
     * gets the underlying
     * @param _t
     * @return
     */
    public <_T extends _type> Class<?> getClass(_T _t){
        try{
            return this.fileManager.classLoader.loadClass(_t.getFullName());
        }catch(ClassNotFoundException cnfe){
            throw new _runtimeException("Unable to find class "+ _t.getFullName());
        }
    }

    /**
     * gets the class by the name
     * @param fullyQualifiedClassName
     * @return
     */
    public Class<?> getClass(String fullyQualifiedClassName) {
        try{
            return this.fileManager.classLoader.loadClass(fullyQualifiedClassName);
        }catch(ClassNotFoundException cnfe){
            throw new _runtimeException("Unable to find class "+ fullyQualifiedClassName);
        }
    }
    
    /**
     * Gets the code
     * @param fullyQualifiedClassName
     * @return 
     */
    public _code get_code( String fullyQualifiedClassName ){
        return this.fileManager.classLoader.get_code(fullyQualifiedClassName);
    }
    
    /**
     * Gets the coded for the class
     * @param clazz
     * @return 
     */
    public _code get_code( Class clazz ){
        return this.fileManager.classLoader.get_code(clazz);
    }

    /**
     *
     * @return
     */
    public _classLoader getClassLoader() {
        return this.fileManager.classLoader;
    }

    /**
     *
     * @param fullyQualifiedClassName
     * @param fieldName
     * @return
     */
    public Object getFieldValue(String fullyQualifiedClassName, String fieldName) {
        return getFieldValue(getClass(fullyQualifiedClassName), fieldName);
    }

    /**
     *
     * @return
     */
    public _fileManager getFileManager() {
        return this.fileManager;
    }

    /**
     * Look through the loaded types to find the first main() method
     * and call it (with no arguments)
     */
    public void main(){
        Optional<_code> oc = 
            this.fileManager.classLoader.list_code().stream()
                .filter( _c-> _c instanceof _type && ((_type)_c).getDeclared(_method.class, m-> ((_method)m).isMain()) !=null )
                    .findFirst();
        if( !oc.isPresent() ){
            throw new _runtimeException("cannot find type with a public static void main(String[] args) method");
        }
        _type _t = (_type)oc.get();
        
        invokeStatic(getClass(_t.getFullName()), "main", (Object)new String[0]);
    }

    /**
     *
     * @param _c
     * @param ctorArgs
     * @return
     */
    public Object instance(_class _c, Object...ctorArgs){
        return instance( this.getClass(_c.getFullName()), ctorArgs);
    }

    /**
     *
     * @param fullyQualifiedClassName
     * @param ctorArgs
     * @return
     */
    public Object instance(String fullyQualifiedClassName, Object... ctorArgs) {
        return instance( this.getClass(fullyQualifiedClassName), ctorArgs);
    }

    /**
     *
     * @param clazz
     * @param ctorArgs
     * @return
     */
    public Object instance(Class clazz, Object... ctorArgs) {
        return tryAllCtors( clazz.getConstructors(), ctorArgs);
    }

    /**
     *
     * @param fullyQualifiedClassName
     * @param args
     */
    public void main(String fullyQualifiedClassName, String... args) {
        if( args.length == 0 ){
            this.call( fullyQualifiedClassName, "main", (Object)new String[0] );
        }
        this.call(fullyQualifiedClassName, "main", (Object)args);
    }

    /**
     *
     * @param code
     * @param ctorArgs
     * @return
     */
    public _proxy proxy(_code code, Object... ctorArgs) {
        if( code instanceof _class){
            Class clazz = this.getClass(((_class) code).getFullName());
            return new _proxy( instance( clazz, ctorArgs ));
        }
        throw new _runtimeException("code "+code+" is not a _class");
    }

    /**
     *
     * @param fullyQualifiedClassName
     * @param ctorArgs
     * @return
     */
    public _proxy proxy(String fullyQualifiedClassName, Object... ctorArgs) {
        Class clazz = this.getClass(fullyQualifiedClassName);
        return new _proxy( instance( clazz, ctorArgs ));        
    }

    /**
     *
     * @param clazz
     * @param ctorArgs
     * @return
     */
    public _proxy proxy(Class clazz, Object... ctorArgs) {
        return new _proxy( instance( clazz, ctorArgs ));        
    }
    
    /**
     * compile based on the input and the bytecode in a return a Map (based on
     * the name of the cmopiled class)
     *
     * @param compilerOptions
     * @param ignoreWarnings
     * @param codeFiles
     * @return a Map with the fullyQualifiedClassName to the AdhocByteCodeFile
     * generated by the compiler
     */
    public static List<_classFile> compile(
        List<String> compilerOptions, boolean ignoreWarnings, Collection<JavaFileObject> codeFiles) {

        if (codeFiles.isEmpty()) {
            throw new _runtimeException("No source code to compile");
        }

        _classLoader classLoader = new _classLoader(ClassLoader.getSystemClassLoader());

        _fileManager fileManager
                = new _fileManager(JAVAC.getStandardFileManager(null, null, null), classLoader);

        callCompiler(fileManager, compilerOptions, ignoreWarnings, codeFiles);

        return (List<_classFile>) fileManager.classLoader.classNameTo_classFile.values().stream().collect(Collectors.toList());
    }
    
     /**
     *
     * @param fileManager
     * @param compilerOptions
     * @param ignoreWarnings
     * @param codeFiles
     */
    static void callCompiler(
            _fileManager fileManager, List<String> compilerOptions, boolean ignoreWarnings, Collection<? extends JavaFileObject> codeFiles) {

        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();

        JavaCompiler.CompilationTask task
                = JAVAC.getTask(null, fileManager, collector, compilerOptions, null, codeFiles);

        boolean result = task.call();

        if (!result || collector.getDiagnostics().size() > 0) {
            StringBuilder exceptionMsg = new StringBuilder();
            exceptionMsg.append("Unable to compile the source");
            boolean hasWarnings = false;
            boolean hasErrors = false;
            for (Diagnostic<? extends JavaFileObject> d : collector.getDiagnostics()) {
                switch (d.getKind()) {
                    case NOTE:
                    case MANDATORY_WARNING:
                    case WARNING:
                        hasWarnings = true;
                        break;
                    case OTHER:
                    case ERROR:
                    default:
                        hasErrors = true;
                        break;
                }
                exceptionMsg.append("\n").append("[kind=").append(d.getKind());
                exceptionMsg.append(", ").append("line=").append(d.getLineNumber());
                exceptionMsg.append(", ").append("message=").append(d.getMessage(Locale.US)).append("]");
            }
            if (hasWarnings && !ignoreWarnings || hasErrors) {
                throw new _runtimeException(exceptionMsg.toString());
            }
        }
    }
    
    //** REFLECTION HELPER METHODS */
      /**
     *
     * @param clazz
     * @param methodName
     * @param args
     * @return
     */
    public static Object invokeStatic(Class clazz, String methodName, Object... args) {
        int indexOfP = methodName.indexOf('(');
        if (indexOfP > 0) {
            methodName = methodName.substring(0, indexOfP);
        }
        Method[] ms = clazz.getMethods();
        if (ms.length == 0) {
            throw new _runtimeException("No method with NAME \"" + methodName + "\" on " + clazz);
        }
        if (ms.length == 1) {
            if (!ms[0].getName().equals(methodName)) {
                throw new _runtimeException("No method with NAME \"" + methodName + "\" on " + clazz);
            }
            return invokeStatic(clazz, ms[0], args);
        }
        //more than one...
        _runtimeException exception = null;
        for (int i = 0; i < ms.length; i++) {
            Method m = ms[i];

            if (m.getName().equals(methodName)
                    && (m.getParameterCount() == args.length
                    || (args.length > m.getParameterCount() && m.isVarArgs()))) {
                try {
                    return invokeStatic(clazz, m, args);
                } catch (_runtimeException de) {
                    exception = de;
                }
            }
        }
        if (exception == null) {
            throw new _runtimeException("could not find method \"" + methodName + "\"");
        }
        throw exception;
    }

    /**
     *
     * @param clazz
     * @param method
     * @param args
     * @return
     */
    public static Object invokeStatic(Class clazz, Method method, Object... args) {
        try {
            return method.invoke(null, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new _runtimeException("Error invoking \"" + method.getName() + "\" on " + clazz, ex);
        }
    }

    /**
     * Gets the value of a public static field
     *
     * @param clazz the class to get value from
     * @param fieldName the name of the (public/static) field on the class
     * @return the value of the field
     */
    public static Object getFieldValue(Class clazz, String fieldName) {
        try {
            Field f = clazz.getField(fieldName);
            return f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new _runtimeException("Could not access field \"" + fieldName + "\" on class \"" + clazz + "\"");
        }
    }

    /**
     *
     * @param clazz
     * @param ctorArgs
     * @return
     */
    public static Object tryAllCtors(Class clazz, Object... ctorArgs) {
        return tryAllCtors(clazz.getConstructors(), ctorArgs);
    }

    /**
     * Brute force, just try of ctors that accept the number compile PARAMETERS
     * (silently fail and return on success)
     *
     * @param ctors
     * @param ctorArgs
     * @return
     */
    public static Object tryAllCtors(Constructor[] ctors, Object... ctorArgs) {
        //if ctorArgs is 0, then I check FIRST for a 
        if (ctorArgs.length == 0) {
            //check if there is a public no-arg ctor
            Optional<Constructor> oc
                    = Arrays.stream(ctors).filter(c -> Modifier.isPublic(c.getModifiers()) && c.getParameterCount() == 0).findFirst();
            if (oc.isPresent()) {
                try {
                    return oc.get().newInstance();
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    throw new _runtimeException("Unable to call no arg public Constructor \""
                            + ctors[0].getName() + "()", ex);
                }
            }
            //check if there is a single vararg constructor
            oc = Arrays.stream(ctors).filter(c -> Modifier.isPublic(c.getModifiers()) && c.getParameterCount() == 1 && c.isVarArgs()).findFirst();
            if (oc.isPresent()) {
                try {
                    //System.out.println("Found constructor "+ oc.get());
                    //arrrggg I MIGHT need to create a 
                    Class cc = oc.get().getParameters()[0].getType().getComponentType();
                    return oc.get().newInstance(Array.newInstance(cc, 0));
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    throw new _runtimeException("Unable to call no arg public Constructor \""
                            + ctors[0].getName() + "()", ex);
                }
            }
        }
        //organize the CONSTRUCTORS by largest number compile parameterrs first
        Comparator<Constructor> pc = (Constructor o1, Constructor o2) -> {
            Constructor c1 = (Constructor) o1;
            Constructor c2 = (Constructor) o2;
            return c2.getParameterCount() - c1.getParameterCount();
        };

        //first I want to find a constructor that has EXACTLY thje same parameters
        PriorityQueue<Constructor> exactCtors = new PriorityQueue<>(pc);
        //if I can't find an exact constructor, look for a vararg constructor
        PriorityQueue<Constructor> varCtors = new PriorityQueue<>(pc);

        //accept of the CONSTRUCTORS export the priorityQueue ordering them
        for (Constructor ctor : ctors) {
            if (ctor.isVarArgs()) {
                varCtors.add(ctor);
            } else {
                exactCtors.add(ctor);
            }
        }
        //now that the ctors are organized...
        ctors = exactCtors.toArray(new Constructor[0]);

        //test exact constructors
        for (Constructor ctor : ctors) {
            if (ctor.getParameterCount() == ctorArgs.length || (ctor.isVarArgs() && ctor.getParameterCount() <= ctorArgs.length)) {
                try {
                    return ctor.newInstance(ctorArgs);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    //dont catch
                }
            }
        }
        //If I make it here, I havent found an exact constructor that works
        //based on the arguments I've passed in
        ctors = varCtors.toArray(new Constructor[0]);

        for (Constructor ctor : ctors) {
            //if (ctor.getParameterCount() <= ctorArgs.length) {
            try {
                //I need to "condense" args
                
                //IF I found a vararg constructor BUT the actual args count >
                // args count (i.e. the last few args are actually a single argument 
                // i.e.
                // public C(int... a){}
                // C c = C( 1,2,3,4,5);
                //
                // I need to "condense" that last N parameters as an array on N
                // parameters
                // from above, I need to condense (1,2,3,4,5) int a single 
                // array argument {1,2,3,4,5}
                if (ctorArgs.length > ctor.getParameterCount()) {
                    int condenseCount = (ctorArgs.length - ctor.getParameterCount()) + 1;
                    Parameter p = ctor.getParameters()[ctor.getParameterCount() - 1];

                    try {
                        Object[] firstArgs = Arrays.copyOfRange(ctorArgs, 0, ctorArgs.length - condenseCount);
                        Object vararg = Array.newInstance(p.getType().getComponentType(), condenseCount);
                        int idx = 0;
                        
                        //condense the last (trailing) args into an array before passing them into
                        // the constructor
                        for (int i = ctor.getParameterCount() - 1; i < (ctor.getParameterCount() + condenseCount) - 1; i++) {
                            Array.set(vararg, idx, ctorArgs[i]);
                            idx++;
                        }
                        if (firstArgs.length > 0) { //if there are actual args BEFORE the Vararg parameter
                            //i.e. public C( String name, int...vals){} where name is before varargs parameter vals
                            //I need to build an allargs array, and add the varargs as the last element
                            Object[] allArgs = new Object[ctor.getParameterCount()];
                            System.arraycopy(ctorArgs, 0, allArgs, 0, ctorArgs.length - condenseCount);
                            Array.set(allArgs, ctor.getParameterCount() - 1, vararg);
                            return ctor.newInstance(allArgs);
                        } else {
                            //there are no args before varargs (i.e. public C(int...args){})
                            //it's a single argument varag constructor
                            return ctor.newInstance(vararg);
                        }
                    } catch (ArrayIndexOutOfBoundsException | IllegalAccessException | IllegalArgumentException | InstantiationException | NegativeArraySizeException | InvocationTargetException e) {
                        
                    }
                } else if (ctorArgs.length == ctor.getParameterCount() - 1) {
                    //Arg underflow, the args passed in is one less... (but the last param is vararg)
                    // i.e. public C(int...is){} //I have a constructor with varargs is
                    // C c = new C();//but I pass in (0) parameters...
                    // so I need to build an empty array of the parameter type 
                    Parameter p = ctor.getParameters()[ctor.getParameterCount() - 1];
                    //build an empty array of 0 length to represent the last argument
                    Object[] allArgs = new Object[ctor.getParameterCount()];
                    System.arraycopy(ctorArgs, 0, allArgs, 0, ctorArgs.length);
                    Array.set(allArgs, allArgs.length - 1, Array.newInstance(p.getType().getComponentType(), 0));
                    return ctor.newInstance(allArgs);
                }
                //the EXACT correct amount of arguments
                return ctor.newInstance(ctorArgs);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                //dont catch
            }
        }

        /** BELOW HERE IS CONSTRUCTING SOME ERROR HANDLING */
        StringBuilder args = new StringBuilder();
        args.append("(");
        for (int i = 0; i < ctorArgs.length; i++) {
            if (i > 0) {
                args.append(",");
            }
            args.append(ctorArgs[i]);
        }
        args.append(")");
        throw new _runtimeException("Unable to find a public Constructor to accept \""
                + ctors[0].getName() + "\"\n"
                + args.toString());
    }
}
