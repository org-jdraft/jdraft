package org.jdraft.runtime;

import java.util.*;
import javax.tools.JavaFileObject;

import com.github.javaparser.utils.Log;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.*;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.jdraft.*;
import org.jdraft.io._batch;
import org.jdraft.text.Stencil;
//import org.jdraft.pattern.*;

/**
 * Simple API to adapt all of the functionality in the
 * statically, so you don't have to always use:
 * 
 * Compiles and Loads BOTH draft based {@link _codeUnit} or {@link _project} entities:
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
public final class _runtime {
    
    /**
     * Javac compiler for compiling java source to bytecode classes
     */
    public static final JavaCompiler JAVAC = ToolProvider.getSystemJavaCompiler();

    /**
     * compile a simple class defined by the fullyQualifiedClassName and code
     * content
     *
     * @param codeLines
     * @return
     */
    public static List<_classFile> compile(String... codeLines) {
        return compile((_type) _type.of(codeLines));
    }

    /**
     *
     * @param compilerOptions
     * @param codeLines
     * @return
     * @see <A HREF="https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javac.html#options">javac options</A>
     */
    public static List<_classFile> compile(List<String> compilerOptions, String... codeLines) {
        return compile(compilerOptions, (_type) _type.of(codeLines));
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
        return compile(compilerOptions, ignoreWarnings, (_type) _type.of(codeLines));
    }
    
    /**
     * compiles draft code and returns a Map containing the className and bytecode
     * @param codeArray the draft _code instances (i.e. {@link org.jdraft._class}, {@link org.jdraft._interface})
     * @return map of the className to the bytecode
     */
    public static List<_classFile> compile(_codeUnit...codeArray){
        List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(codeArray).forEach( f -> fs.add( _javaFile.of(f)));
        return compile(fs);
    }

    /**
     *
     * @param codeList
     * @return
     */
    public static <_C extends _codeUnit> List<_classFile> compile(List<_C> codeList){
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
    public static List<_classFile> compile(List<String>compilerOptions, _codeUnit...codeArray){
         List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(codeArray).forEach( f -> fs.add( _javaFile.of(f)));
        return compile(compilerOptions, true, fs);
    }

    /**
     * Compile all the {@link _codeUnit} files in all the {@link _project}
     * @param _cuss array of _codeUnits
     * @return list of _classFiles (in memory bytecode files that HAVE NOT been loaded by a ClassLoader)
     */
    public static List<_classFile> compile(_project... _cuss){
        return compile(Collections.EMPTY_LIST, true, _cuss);
    }

    /**
     *
     * @param compilerOptions
     * @param _cuss
     * @return
     */
    public static List<_classFile> compile(List<String>compilerOptions, _project... _cuss){
        return compile(compilerOptions, true, _cuss);
    }

    /**
     * compiles draft code, passes compiler options to the compiler
     * @param compilerOptions
     * @param _cuss the array of codeUnits
     * @return
     */
    public static List<_classFile> compile(List<String>compilerOptions, boolean ignoreWarnings, _project... _cuss){
        List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(_cuss).forEach( _cus -> _cus.forEach(_cu-> fs.add( _javaFile.of(_cu)) ) );
        return compile(compilerOptions, ignoreWarnings, fs);
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
    public static List<_classFile> compile(List<String>compilerOptions, boolean ignoreWarnings, _codeUnit...code){
        List<JavaFileObject> fs = new ArrayList<>();
        Arrays.stream(code).forEach( f -> fs.add( _javaFile.of(f)));
        return compile(compilerOptions, ignoreWarnings, fs);
    }

    /**
     * Compile and load the {@link _type} {@link _class} {@link _enum}, {@link _annotation} {@link _interface}
     * and return it's Class file
     * (NOTE: will only return the top level class file, but will compile it's
     * nested or companion types)
     * @param _t the _type to compile {@link _class} {@link _enum}, {@link _annotation} {@link _interface}
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
     * @param _t the {@link _class} {@link _enum}, {@link _annotation} or {@link _interface}
     * @return a loaded Class
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
     * @param ignoreWarnings dont stop on compiler warnings
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
     * @param ctorArgs the constructor arguments
     * @return a _proxy instance referring to a new Object instance
     */
    public static _proxy proxyOf(_class _c, Object...ctorArgs){
        return _runtime.of(_c).proxy(_c, ctorArgs);
    }

    /**
     * compile and return a (no-arg) _proxy instance wraopping a new Object instance of a new class defined by
     * the classSourceCode at runtime.
     *
     * @param classSourceCode the source code of a Class
     * @return a _proxy instance to an instance of the class loaded
     */
    public static _proxy proxyOf(String classSourceCode){
        return proxyOf(_class.of(classSourceCode), new Object[0]);
    }

    /**
     *
     * @param classSourceCode
     * @param ctorArgs
     * @return
     */
    public static _proxy proxyOf(String classSourceCode, Object...ctorArgs){
        return proxyOf(_class.of(classSourceCode), ctorArgs);
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
     * @param fullyQualifiedClassName
     * @param anonymousImplementation
     * @param ctorArgs
     * @param <I>
     * @return
     */
    public static <I extends Object> I impl(String fullyQualifiedClassName, I anonymousImplementation, Object...ctorArgs){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        //ObjectCreationExpr oce = Expressions.newEx(ste);
        //generate an ENTIRELY NEW class
        _class _c = _class.of(fullyQualifiedClassName, anonymousImplementation, ste);
        return (I)instanceOf(_c, ctorArgs);
    }

    /**
     * Creates a new Class that implements the interface (or extends the base class)
     * <PRE>
     *     Object impl = _runtime.impl( new @_dto Serializeable(){
     *         int x,y;
     *     });
     * </PRE>
     *
     * @param anonymousImplementation
     * @param <I>
     * @return
     */
    public static <I extends Object> I impl(I anonymousImplementation, Object...ctorArgs) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expressions.newEx(ste);
        _class _c = _class.of(oce.getType().getNameAsString() + "Impl", anonymousImplementation, ste);
        return (I) instanceOf(_c, ctorArgs);
    }

    public static Object instanceOf(Class clazz){
        return instanceOf(_class.of(clazz), new Object[0]);
    }

    public static Object instanceOf(Class clazz, Object...ctorArgs){
        return instanceOf(_class.of(clazz), ctorArgs);
    }

    public static Object instanceOf(String classSourceCode){
        return instanceOf(_class.of(classSourceCode), new Object[0]);
    }
    public static Object instanceOf(String classSourceCode, Object...ctorArgs){
        return instanceOf(_class.of(classSourceCode), ctorArgs);
    }

    public static Object instanceOf(_class _c){
        return instanceOf(_c, new Object[0]);
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
     * Statically evaluate the code defined by the expression
     * @param expressionCode the c
     * @return
     */
    public static Object staticEval( String... expressionCode ){
        return staticEval( Expressions.of(expressionCode));
    }

    private static Stencil $evalExpr = Stencil.of(
            "public static Object eval(){",
            "    return $expr$;",
            "}");
    /*
    private static $method $evalExpr = $method.of(
            "public static Object eval(){",
            "    return $expr$;",
            "}");
     */

    /* This requires the source of this class be on the classpath... not good
    public static $method $evalExpr = $method.of(
            new Object() {
                public @_static Object eval(){
                    return $expr$;
                }
                @_remove Object $expr$;
        } );
    */

    /**
     * Statically evaluates the expr
     * @param expr
     * @return
     */
    public static Object staticEval( Expression expr ){
        if( expr.isLiteralExpr() ){ //we can shortcut these kinds of expressions and just return the value
            LiteralExpr le = (LiteralExpr)expr;
            if( le instanceof NullLiteralExpr){
                return null;
            }
            if( le instanceof  StringLiteralExpr ){
                return ((StringLiteralExpr)le).asString();
            }
            if( le instanceof CharLiteralExpr ){
                return ((CharLiteralExpr)le).asChar();
            }
            if( le instanceof DoubleLiteralExpr ){
                return Expressions.parseFloatOrDouble( le.asDoubleLiteralExpr() );
            }
            if( le instanceof IntegerLiteralExpr){
                return Expressions.parseInt(le.asIntegerLiteralExpr());
            }
            if( le instanceof BooleanLiteralExpr){
                return ((BooleanLiteralExpr) le).getValue();
            }
            if( le instanceof LongLiteralExpr){
                return Expressions.parseLong(le.asLongLiteralExpr());
            }
        }
        if( expr instanceof ThisExpr ){
            throw new _runtimeException("cannot run this expression "+ expr);
        }
        if( expr instanceof SuperExpr ){
            throw new _runtimeException("cannot run super expression "+ expr);
        }
        if( expr.isAnnotationExpr() ){
            throw new _runtimeException("cannot run annotation expression "+ expr);
        }
        //assignExpression??
        if( expr.isAssignExpr() ){
            //expr.asAssignExpr().
            throw new _runtimeException("cannot run assign expression "+ expr);
        }
        if( expr.isCastExpr() ){
            throw new _runtimeException("cannot run cast expression "+ expr);
        }
        if( expr.isLambdaExpr() ){
            throw new _runtimeException("cannot run lambda expression "+ expr);
        }
        if( expr.isMethodReferenceExpr() ){
            throw new _runtimeException("cannot run method reference expression "+ expr);
        }
        if( expr.isSwitchExpr()){
            throw new _runtimeException("cannot run switch expression "+ expr);
        }
        if( expr.isTypeExpr() ){
            /*
            //i.e. "world::greet"
            //convert it to a methodCallExpr
            TypeExpr te = expr.asTypeExpr();
            te.
            $ex.Select $es = $ex.typeEx("$type$::$methodName$").select(expr.asTypeExpr());
            staticEval( $ex.methodCallEx("$type$.")
             */
            throw new _runtimeException("cannot run type expression "+ expr);
        }
        //if( expr.isMethodReferenceExpr() ){
        //    MethodReferenceExpr mre = expr.asMethodReferenceExpr();
        //    _class _c = _class.of("adhoc.ExprEval").method( $evalExpr.draft("expr", expr) );
        //}
        _class _c = _class.of("adhoc.ExprEval").addMethod( $evalExpr.draft("expr", expr) );
        return proxyOf(_c).call("eval");
    }

    /**
     * A simple way of evaluating an expression using the loaded classes
     * @param expressionCode
     * @return
     */
    public Object eval( String expressionCode ){
         return eval( Expressions.of(expressionCode) );
    }

    /**
     *
     * @param expr
     * @return
     */
    public Object eval(Expression expr){
        if( expr.isFieldAccessExpr() ){
            FieldAccessExpr fae = expr.asFieldAccessExpr();
            Expression scope = fae.getScope();
            if( scope == null || scope.toString().equals("")){
                //System.out.println("Empty expression ");
            }
            //hmm if I dont have scope, I could Look for all code containing the field name as a static variable
            // and return the value...
            //this.fileManager.classLoader.list_code(_c -> _c.getField(fae.getNameAsString()))
            return getFieldValue(fae.getScope().toString(), fae.getNameAsString());
        }
        if( expr.isNameExpr() ){ //they might want to know the value of a field
            //System.out.println("Its an name "+ expr );
            List<_type> _ts = this.fileManager.classLoader.list_types(_c ->{
                //System.out.println( "checking "+_c.getFullName() );
                if(_c instanceof _field._withFields){

                    return ((_field._withFields)_c).getField( (f)-> ((_field)f).getName().equals(expr.toString())
                            && ((_field)f).isPublic() && ((_field)f).isStatic() ) != null;
                }
                return false;
             });
            if( _ts.size() == 0 ){
                throw new _runtimeException("Unable to find a field named "+ expr);
            }
            if( _ts.size() ==1){
                return getFieldValue( _ts.get(0).getFullName(), expr.toString() );
            }
            throw new _runtimeException("field named "+ expr+" found in multiple classes");
        }
        /*
        if( expr.isMethodReferenceExpr() ){
            MethodReferenceExpr mre = expr.asMethodReferenceExpr();
            mre.get
            _class _c = _class.of("adhoc.ExprEval").method( $evalExpr.draft("expr", expr) );
        }
         */
        if( expr.isMethodCallExpr() ){
            MethodCallExpr mce = expr.asMethodCallExpr();
            if( mce.getScope().isPresent() ){
                //if( mce.getArguments().isEmpty() ) {
                //    return call(mce.getScope().get().toString(), mce.getNameAsString());
                //} else{
                Expression[] args = mce.getArguments().toArray(new Expression[0]);
                Object[] params = new Object[args.length];
                for(int i=0;i<args.length; i++){
                     params[i] = eval(args[i]);
                     final int a = i;
                     Log.trace("set [ %s ] to %s", ()-> a, ()->params[a] );
                     //System.out.println( "set ["+i+"] to "+params[i]);
                 }
                 Expression es = mce.getScope().get();
                 if( es instanceof ObjectCreationExpr ){
                     Object instance = eval(es);
                     return new _proxy(instance).call(mce.getNameAsString(), params);
                 }
                 return call(mce.getScope().get().toString(), mce.getNameAsString(), params);
            }

            /* MED REMOVED
            //System.out.println( "NAME "+ mce.getNameAsString() );
            //$type $publicTypeNamedWithConstructor = $type.of( $.PUBLIC, $method.of($.PUBLIC, $.STATIC, $name.as(mce.getNameAsString()) ));

            //List<_type> _ts = $publicTypeNamedWithConstructor.listIn(this.fileManager.classLoader.list_types());
            */

            List<_type> _ts = this.fileManager.classLoader.list_types(_t-> _t.isPublic() &&
                    _t.getMember(_method.class, (_m)-> ((_method) _m).isPublic() && ((_method) _m).isStatic() && ((_method) _m).isNamed(mce.getNameAsString())) != null);

            if( _ts.size() == 0 ){
                throw new _runtimeException("could not find static method to evaluate \n"+ mce);
            }
            if( _ts.size() == 1) {
                Expression[] args = mce.getArguments().toArray(new Expression[0]);
                Object[] params = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    params[i] = eval(args[i]);
                    final int a = i;
                    Log.trace("set [ %s ] to %s", ()-> a, ()->params[a] );
                }
                return call(_ts.get(0).getFullName(), mce.getNameAsString(), params);
            }
            throw new _runtimeException("ambiguous method call for "+ mce);
        } else if ( expr instanceof ObjectCreationExpr ){
            ObjectCreationExpr oce = (ObjectCreationExpr) expr;
            if( oce.getScope().isPresent() ){
                Expression[] args = oce.getArguments().toArray(new Expression[0]);
                Object[] params = new Object[args.length];
                for(int i=0;i<args.length; i++){
                    params[i] = eval(args[i]);
                    final int a = i;
                    Log.trace("set [ %s ] to %s", ()-> a, ()->params[a] );
                    //System.out.println( "set ["+i+"] to "+params[i]);
                }
                String name = oce.getType().getScope().get()+"."+ oce.getTypeAsString();
                return instance(name, params);
                //return call(mce.getScope().get().toString(), mce.getNameAsString(), params);
            }
            String typeString = oce.getTypeAsString();
            //$class $publicClassWithName = $class.of($name.as(typeString), $.PUBLIC);
            //if( typeString.contains(".")){
            //    String packageName = typeString.substring(0, typeString.lastIndexOf('.'));
            //    String className = typeString.substring(typeString.lastIndexOf('.')+1);
            //    $publicClassWithName = $class.of($.PUBLIC, $name.as(className), $package.of(packageName) );
            //}
            //System.out.println( $publicClassWithName );
            //List<_class> _ts = $publicClassWithName.listIn(this.fileManager.classLoader.list_types());
            List<_class> _ts = null;
            if( typeString.contains(".")){
                String packageName = typeString.substring(0, typeString.lastIndexOf('.'));
                String className = typeString.substring(typeString.lastIndexOf('.')+1);
                //$publicClassWithName = $class.of($.PUBLIC, $name.as(className), $package.of(packageName) );
                _ts = this.fileManager.classLoader.list_types(_class.class, _c-> _c.isPublic() && _c.isNamed(className) && _c.isInPackage(packageName));
            } else {
                _ts = this.fileManager.classLoader.list_types(_class.class, _c -> _c.isPublic() && _c.isNamed(typeString));
            }
            if( _ts.size() == 0 ){
                //they could be calling System.currentTimeMillis() or some other Class thats not in the _runtime Classes
                return staticEval(expr);
            }
            if( _ts.size() == 1) {
                Expression[] args = oce.getArguments().toArray(new Expression[0]);
                Object[] params = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    params[i] = eval(args[i]);
                    final int a = i;
                    Log.trace("set [ %s ] to %s", ()-> a, ()->params[a] );
                }
                return instance(_ts.get(0).getFullName(), params);
            }
            throw new _runtimeException("ambiguous type for creating "+ oce);
        }
        return staticEval(expr);
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
        return _runtime.of((_type) _type.of(javaSourceCode));
    }
    
    /**
     * 
     * @param macroAnnotatedClasses one or more Runtime Classes that are annotated
     * @return an _adhoc with compiled classes and resulting _code models after running
     * macros on the Classes
     */
    public static _runtime of(Class...macroAnnotatedClasses){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(macroAnnotatedClasses).forEach(c->sfs.add( _javaFile.of((_type) _type.of(c))));
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
        Arrays.stream(macroAnnotatedClasses).forEach(c->sfs.add( _javaFile.of( (_type) _type.of(c))));
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
     * @return the _runtime (containing the compiled classes and _code source models)
     */
    public static _runtime of(List<String>compilerOptions, boolean ignoreWarnings, Class...macroAnnotatedClasses){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(macroAnnotatedClasses).forEach(c->sfs.add( _javaFile.of( (_type) _type.of(c))));
        return of(compilerOptions, ignoreWarnings, sfs);
    }

    public static _runtime of(_batch..._batches ){
        return of( Collections.EMPTY_LIST, true, _batches);
    }

    public static _runtime of(boolean ignoreWarnings, _batch..._batches ){
        return of( Collections.EMPTY_LIST, ignoreWarnings, _batches);
    }

    public static _runtime of(List<String>compilerOptions, boolean ignoreWarnings, _batch..._batches ){
         _project _cus = new _project();
         Arrays.stream(_batches).forEach(_b-> _cus.add(_b.load()) );
         return of( compilerOptions, ignoreWarnings, _cus);
    }

    /**
     *
     * @param _cuss
     * @return
     */
    public static _runtime of(_project..._cuss ){
        return of(Collections.EMPTY_LIST, true, _cuss);
    }

    /**
     *
     * @param compilerOptions
     * @param _cuss
     * @return
     */
    public static _runtime of(List<String>compilerOptions, _project..._cuss ){
        return of(compilerOptions, true, _cuss);
    }

    /**
     * Given multiple _codeUnits containing code build a runtime by compiling all the code to classes and
     * creating a new _classLoader and loading all of the compiled classes
     * @param compilerOptions javac compiler options
     * @param ignoreWarnings (dont stop the compiler if warnings are encountered)
     * @param _cuss an array of _codeUnits containing one or more _codeUnit (s)
     * @return a runtime with the classes loaded
     */
    public static _runtime of(List<String>compilerOptions, boolean ignoreWarnings, _project..._cuss ){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(_cuss).forEach(_cus-> _cus.forEach(_cu -> sfs.add( _javaFile.of( _cu) ) ) );
        return of(compilerOptions, ignoreWarnings, sfs);
    }

    /**
     * 
     * @param codeArray
     * @return 
     */
    public static _runtime of(_codeUnit...codeArray){
        List<JavaFileObject> sfs = new ArrayList<>();
        Arrays.stream(codeArray).forEach(c-> sfs.add( _javaFile.of(c)));
        return _runtime.of(Collections.EMPTY_LIST, true, sfs);
    }

    public static <_C extends _codeUnit> _runtime of(_C code ){
        return of( Stream.of(code).collect(Collectors.toList()));
    }

    /**
     *
     * @param codeList
     * @param <_C>
     * @return
     */
    public static <_C extends _codeUnit> _runtime of(List<_C> codeList ){
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
    public static _runtime of(List<String>compilerOptions, _codeUnit...code){
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
    public static _runtime of(List<String>compilerOptions, boolean ignoreWarnings, _codeUnit...code){
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

    private Class<?> getJavaLangClass( String name ){
        if(!name.contains(".") ){
            try {
                return Class.forName("java.lang." + name);
            } catch(Exception e){
                return null;
            }
        }
        if( name.startsWith("java.lang") ){
            try {
                return Class.forName(name);
            }catch(Exception e){
                return null;
            }
        }
        return null;
    }

    /**
     * gets the class by the name
     * @param fullyQualifiedClassName
     * @return
     */
    public Class<?> getClass(String fullyQualifiedClassName) {
        try{
            Class cl = getJavaLangClass(fullyQualifiedClassName);
            if( cl != null ){
                return cl;
            }
            //if( fullyQualifiedClassName.equals("System") ){
            //    return java.lang.System.class;
            //}
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
    public _codeUnit get_code(String fullyQualifiedClassName ){
        return this.fileManager.classLoader.get_code(fullyQualifiedClassName);
    }
    
    /**
     * Gets the coded for the class
     * @param clazz
     * @return 
     */
    public _codeUnit get_code(Class clazz ){
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
        Optional<_codeUnit> oc =
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
        if( clazz.getConstructors().length == 1){
            try {
                if( ctorArgs.length ==0 ){
                    //System.out.println( "NO ARGS ");
                    return clazz.getConstructors()[0].newInstance();
                }
                return clazz.getConstructors()[0].newInstance(ctorArgs);
            }catch(Exception e){
                throw new _runtimeException("unable to call lone constructor on "+ clazz+" ", e);
            }
        }
        if( clazz.getConstructors().length > 1 ){
            return tryAllCtors( clazz.getConstructors(), ctorArgs);
        }
        if( clazz.getDeclaredConstructors().length > 0 ){
            return tryAllCtors( clazz.getDeclaredConstructors(), ctorArgs);
        }
        throw new _runtimeException("cannot find constructors for "+clazz);
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
    public _proxy proxy(_codeUnit code, Object... ctorArgs) {
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
    public static Object tryAllCtors(final Constructor[] ctors, Object... ctorArgs) {
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
        //organize the CONSTRUCTORS by largest number compile parameters first
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

        Constructor[] xactCtors = exactCtors.toArray(new Constructor[0]);

        //test exact constructors
        for (Constructor ctor : xactCtors) {
            if (ctor.getParameterCount() == ctorArgs.length || (ctor.isVarArgs() && ctor.getParameterCount() <= ctorArgs.length)) {
                try {
                    return ctor.newInstance(ctorArgs);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    //dont catch
                }
            }
        }
        //If I make it here, I haven't found an exact constructor that works
        //based on the arguments I've passed in
        Constructor[] vctors = varCtors.toArray(new Constructor[0]);

        for (Constructor ctor : vctors) {
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
