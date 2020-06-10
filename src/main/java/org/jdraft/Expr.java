package org.jdraft;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.stmt.Statement;

import com.github.javaparser.ast.type.Type;
import org.jdraft.io._io;
import org.jdraft.io._ioException;
import org.jdraft.io._in;
import org.jdraft.text.Text;
import org.jdraft.walk.Walk;

/**
 * Utility for converting free form Strings and Runtime entities (lambdas, Anonymous Objects)
 * into JavaParser AST {@link Expression} implementations.
 * 
 * Simplify the mediation between different representations of the same thing.
 * @author M. Eric DeFazio
 */
public enum Expr {
    ;

    /**
     * A unique JAVAPARSER implementation that DOES NOT use the postProcessor for expressions
     *
     */
    protected static final JavaParser JAVAPARSER =
            new JavaParser(new ParserConfiguration().setLanguageLevel( ParserConfiguration.LanguageLevel.BLEEDING_EDGE));

    /**
     * Functional interface for no input, no return lambda function
     * (Used when we pass in Lambdas to the {@link Expr#lambdaExpr(Command)} operation
     * in order to get the LamdbaExpr AST from the Runtime to a 
     * {@link com.github.javaparser.ast.expr.LambdaExpr}
     */
    @FunctionalInterface
    public interface Command{
        void consume() throws IOException;
    }

    /**
     * Functional interface for (3) input, (1) return lambda function
     * (Used when we pass in Lambdas to the {@link Expr#lambdaExpr(TriFunction)}
     * operation
     * {@link com.github.javaparser.ast.expr.LambdaExpr}
     * @param <A>
     * @param <B>
     * @param <C>
     * @param <D>
     */
    @FunctionalInterface
    public interface TriFunction<A,B,C,D>{
        D apply(A a, B b, C c);
    }

    /**
     * Functional interface for (4) input PARAMETERS, (1) return lambda function
     * (Used when we pass in Lambdas to the {@link Expr#lambdaExpr(QuadFunction)}
     * operation)
     * {@link com.github.javaparser.ast.expr.LambdaExpr}
     * @param <A>
     * @param <B>
     * @param <C>
     * @param <D>
     * @param <E>
     */
    @FunctionalInterface
    public interface QuadFunction<A,B,C,D,E>{
        E apply(A a, B b, C c, D d);
    }

    /**
     * Functional interface for (3) input PARAMETERS, no return lambda function
     * (Used when we pass in Lambdas to the {@link Expr#lambdaExpr(TriConsumer)}
     * operation)
     * {@link com.github.javaparser.ast.expr.LambdaExpr}
     * @param <T>
     * @param <U>
     * @param <V>
     */
    @FunctionalInterface
    public interface TriConsumer<T,U,V>{
        void consume(T t, U u, V v);
    }

    /**
     * Functional interface for (4) input PARAMETERS, no return lambda function
     * (Used when we pass in Lambdas to the {@link Expr#lambdaExpr(QuadConsumer)}
     * operation
     * {@link com.github.javaparser.ast.expr.LambdaExpr}
     * @param <T> any type
     * @param <V> any type
     * @param <U> any type
     * @param <Z> any type
     */
    @FunctionalInterface
    public interface QuadConsumer<T,U,V,Z>{
        void consume(T t, U u, V v, Z z);
    }

    /**
     * Resolves and returns the AST LambdaExpr representing the Runtime Lambda passed in
     * for example:
     * <PRE>
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(Path)
     * @param c the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static LambdaExpr of( Expr.Command c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing the Runtime Lambda passed in
     * for example:
     * <PRE>
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @param <T>
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * @param c the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object> LambdaExpr of( Consumer<T> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing the Runtime Lambda passed in
     * for example:
     * <PRE>
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft     
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * @param <T>
     * @param <U>
     * @param c the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object, U extends Object> LambdaExpr of( BiConsumer<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing the Runtime Lambda passed in
     * for example:
     * <PRE>
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * @param <T>
     * @param <U>
     * @param <V>
     * @param c the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object, U extends Object, V extends Object> LambdaExpr of( TriConsumer<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing the Runtime Lambda passed in
     * for example:
     * <PRE>
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * @param <U>
     * @param <T>
     * @param c the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object, U extends Object> LambdaExpr of( Function<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing the Runtime Lambda passed in
     * for example:
     * <PRE>
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * @param <T>
     * @param <U>
     * @param <V>
     * @param c the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object, U extends Object, V extends Object> LambdaExpr of( BiFunction<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing the Runtime Lambda passed in
     * for example:
     * <PRE>
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <Z>
     * @param c the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object, U extends Object, V extends Object, Z extends Object> LambdaExpr of( TriFunction<T, U, V, Z> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing the Runtime Lambda passed in
     * for example:
     * <PRE>
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * @param <A>
     * @param <B>
     * @param <C>
     * @param <D>
     * @param c the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <A extends Object, B extends Object, C extends Object, D extends Object> LambdaExpr of( QuadConsumer<A,B,C,D> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing lambda expression 
     * that is referenced from this stackTraceElement line
     * for example:
     * <PRE>{@code
     * //call the method f with a lambdaExpression
     * f( (String s) -> System.out.println(s) );
     * //...
     * 
     * //the method f() accepts a Lambda expression, but we WANT the actual
     * // AST (we dont "use" the runtime lambda expression for anything other
     * // than to represent the AST
     * void f( Consumer c ){
     *      StackTraceElement ste = Thread.currentThread().getStackTrace[2];
     *      LambdaExpr le = Expr.lambda( ste );
     *      assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * }
     * }</PRE>
     * NOTE: the source of the calling method StackTraceELement 
     * must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * 
     * @param ste the stack trace Element line containing the code to draw from  
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static LambdaExpr lambdaExpr(StackTraceElement ste ) {
        return lambdaExpr(ste, _io.IN_DEFAULT );
    }

    /**
     * Resolves and returns the AST LambdaExpr representing lambda expression 
     * that is referenced from this stackTraceElement line
     * for example:
     * <PRE>
     * //call the method f with a lambdaExpression
     * f( (String s) -> System.out.println(s) );
     * //...
     * 
     * //the method f() accepts a Lambda expression, but we WANT the actual
     * // AST (we dont "use" the runtime lambda expression for anything other
     * // than to represent the AST
     * void f( Consumer c ){
     *      StackTraceElement ste = Thread.currentThread().getStackTrace[2];
     *      LambdaExpr le = Expr.lambda( ste );
     *      assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * }
     * </PRE>
     * NOTE: the source of the calling method StackTraceELement 
     * must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * 
     * @param ste the stack trace Element line containing the code to draw from  
     * @param resolver the resolver for finding the source referenced in the StackTraceElement line
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static LambdaExpr lambdaExpr(StackTraceElement ste, _in._resolver resolver ){
        _type _t = null;
        try {
            //System.out.println( ste.toString() );
            Class clazz = Class.forName(ste.getClassName());
            _t = _type.of(clazz, resolver);
        } catch (Exception e) {
            throw new _jdraftException("no .java source for Runtime Class \"" + ste.getClassName() + "\" " + System.lineSeparator() +
                    _io.describe(), e); //print out the input config to help
        }
        int lineNumber = ste.getLineNumber();

        //What I need to do is to find the MethodCallExpr that is nested inside another MethodCallExpr
        //check if it begins before the stack trace line and ends aftrer the stack trace line
        List<MethodCallExpr> ln = //Ast.listAll(_walk.BREADTH_FIRST,
            Walk.list(Walk.BREADTH_FIRST,
            _t.ast(),
            Ast.Classes.METHOD_CALL_EXPR,
            mce-> mce.getRange().isPresent() && mce.getRange().get().begin.line <= lineNumber
                && mce.getRange().get().end.line >= lineNumber
                && mce.getArguments().stream().filter(a-> a instanceof LambdaExpr).findFirst().isPresent()                
        );

        // we always want to start with the LAST one, since we could have a nested
        // grouping of statements
        for(int i=ln.size()-1;i>=0;i--){
            Optional<Node> on = ln.get(i).stream().filter(n -> n instanceof LambdaExpr).findFirst();
            if( on.isPresent() ){
                return (LambdaExpr)on.get();
            }
        }

        throw new _ioException("unable to find in lambda at (" + ste.getFileName() + ":" + ste.getLineNumber() + ")"+System.lineSeparator()+ _io.describe());
    }

    public static ObjectCreationExpr newExpr(String ex ){
        return newExpr(new String[]{ex});
    }

    /**
     * Return the AST (ObjectCreationExpr) for the .java SOURCE code 
     * of the Anonymous Runtime Object passed in.
     * 
     * for instance:
     * <PRE>
     * //return the ObjectCreationExpr (the AST for the Runtime Object passed in)
     * ObjectCreationExpr oce = Expr.anonymousObject( new Object(){ int x,y;} );
     * NodeList<BodyExpression<?>> body =  oce.getBody().get();
     * //do something with the AST
     * </PRE>
     * 
     * NOTE: there are important implications here: 
     * 
     * 1) THE Object passed in MUST BE an anonymous Object
     * 2) The .java Source for the code calling this method MUST be locateable by draft 
     * either
     * <UL>
     *  <LI>on the classpath
     *  <LI>in user.dir System property ( this is by default for most IDES like
     *      Eclipse, NetBeans, IntelliJ
     *  <LI>on one of the manually configured in.filePaths
     * {@link org.jdraft.io._io._config#inFilesPath(java.lang.String)}
     * {@link org.jdraft.io._io._config#inProjectsPath(java.lang.String)}
     * </UL>
     * 
     * alternatively, you can use the {@link #newExpr(Object)}
     * method and pass in an _in.resolver to locate the code manually
     * 
     * @see org.jdraft.io._io._config#inFilesPath(java.lang.String)
     * @see org.jdraft.io._io._config#inProjectsPath(java.lang.String)
     * @param anonymousObject an anonymous Object
     * @return the ObjectCreationExpr AST representation of the anonymousObject passed in
     */
    public static ObjectCreationExpr newExpr(Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return newExpr(ste, _io.IN_DEFAULT);
    }

    /**
     * Return the AST (ObjectCreationExpr) for the .java SOURCE code 
     * of the Runtime Anonymous Object passed in.
     * 
     * for instance:
     * <PRE>
     * //return the ObjectCreationExpr (the AST for the Runtime Object passed in)
     * ObjectCreationExpr oce = Expr.anonymousObject( new Object(){ int x,y;} );
     * NodeList<BodyExpression<?>> body =  oce.getBody().get();
     * //do something with the AST
     * </PRE>
     * 
     * NOTE: there are important implications here: 
     * 
     * 1) THE Object passed in MUST BE an anonymous Object
     * 2) The .java Source for the code calling this method MUST be locateable by draft 
     * either
     * <UL>
     *  <LI>on the classpath
     *  <LI>in user.dir System property ( this is by default for most IDES like
     *      Eclipse, NetBeans, IntelliJ
     *  <LI>on one of the manually configured in.filePaths
     * {@link org.jdraft.io._io._config#inFilesPath(java.lang.String)}
     * {@link org.jdraft.io._io._config#inProjectsPath(java.lang.String)}
     * </UL>
     * 
     * 
     * @see org.jdraft.io._io._config#inFilesPath(java.lang.String)
     * @see org.jdraft.io._io._config#inProjectsPath(java.lang.String)
     * @param anonymousObject an anonymous Object of which we want the AST ObjectCreationExpr
     * representation of the source
     * @param resolver a resolver to use for looking up the source for the calling
     * code
     * @return the ObjectCreationExpr AST representation of the anonymousObject passed in
     */
    public static ObjectCreationExpr newExpr(Object anonymousObject, _in._resolver resolver){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return newExpr(ste, resolver);
    }

    /**
     * Return the AST (ObjectCreationExpr) for the .java SOURCE code 
     * of the Runtime Anonymous Object passed in.
     * 
     * for instance:
     * <PRE>
     * //return the ObjectCreationExpr (the AST for the Runtime Object passed in)
     * ObjectCreationExpr oce = Expr.anonymousObject( new Object(){ int x,y;} );
     * NodeList<BodyExpression<?>> body =  oce.getBody().get();
     * //do something with the AST
     * </PRE>
     * 
     * NOTE: there are important implications here: 
     * 
     * 1) THE Object passed in MUST BE an anonymous Object
     * 2) The .java Source for the code calling this method MUST be locateable by draft 
     * either
     * <UL>
     *  <LI>on the classpath
     *  <LI>in user.dir System property ( this is by default for most IDES like
     *      Eclipse, NetBeans, IntelliJ
     *  <LI>on one of the manually configured in.filePaths
     * {@link org.jdraft.io._io._config#inFilesPath(java.lang.String)}
     * {@link org.jdraft.io._io._config#inProjectsPath(java.lang.String)}
     * </UL>
     * 
     * @see org.jdraft.io._io._config#inFilesPath(java.lang.String)
     * @see org.jdraft.io._io._config#inProjectsPath(java.lang.String)
     * @param ste the stackTraceElement line referring to the lone of code where the
     * 
     * @return the ObjectCreationExpr AST representation of the anonymousObject passed in
     * @throws _ioException if unable to resolve the Source of the anonymousObject
     */
    public static ObjectCreationExpr newExpr(StackTraceElement ste ){
         return newExpr(ste, _io.IN_DEFAULT );
    }

    /**
     * Return the AST (ObjectCreationExpr) for the .java SOURCE code 
     * of the Runtime Anonymous Object passed in.for instance:
     * <PRE>
     * //return the ObjectCreationExpr (the AST for the Runtime Object passed in)
     * ObjectCreationExpr oce = Expr.anonymousObject( new Object(){ int x,y;} );
     * NodeList<BodyExpression<?>> body =  oce.getBody().get();
     * //do something with the AST
     * </PRE>
     * 
     * NOTE: there are important implications here: 
     * 
     * 1) THE Object passed in MUST BE an anonymous Object
     * 2) The .java Source for the code calling this method MUST be locateable by draft 
     * either
     * <UL>
     *  <LI>on the classpath
     *  <LI>in user.dir System property ( this is by default for most IDES like
     *      Eclipse, NetBeans, IntelliJ
     *  <LI>on one of the manually configured in.filePaths
     * {@link org.jdraft.io._io._config#inFilesPath(java.lang.String)}
     * {@link org.jdraft.io._io._config#inProjectsPath(java.lang.String)}
     * </UL>
     * 
     * @param resolver the resolver for the source code
     * @see org.jdraft.io._io._config#inFilesPath(java.lang.String)
     * @see org.jdraft.io._io._config#inProjectsPath(java.lang.String)
     * @param ste the stackTraceElement line referring to the lone of code where the
     * 
     * @return the ObjectCreationExpr AST representation of the anonymousObject 
     * referred to from the StackTraceElement passed in
     * @throws _ioException if unable to resolve the Source of the anonymousObject
     */
    public static ObjectCreationExpr newExpr(StackTraceElement ste, _in._resolver resolver ){
        _type _t = null;
        try {
            Class clazz = Class.forName(ste.getClassName());
            _t = _type.of(clazz, resolver);
        } catch (Exception e) {
            throw new _ioException("no .java source for Runtime Class \"" + ste.getClassName() + "\" " + System.lineSeparator() +
                resolver.describe(), e); //print out the input config to help
        }

        //find all of the potential method calls that could be the call 
        //mentioned in the stack trace based on the line numbers 
        List<MethodCallExpr> mces = Walk.list(Walk.POST_ORDER,
                _t.ast(),
                Ast.Classes.METHOD_CALL_EXPR,
                (MethodCallExpr mce) -> ((MethodCallExpr)mce).getRange().get().begin.line <= ste.getLineNumber() &&
                        ((MethodCallExpr)mce).getRange().get().end.line >= ste.getLineNumber() &&
                        mce.getArguments().stream().filter( e-> e.isObjectCreationExpr() && e.asObjectCreationExpr().getAnonymousClassBody().isPresent() ).findFirst().isPresent()
        );
        for(int i=0; i<mces.size();i++ ){
            //find the particular methodCall containing the anonymous Object being created
            //
            Optional<Expression> on =
                mces.get(i).getArguments().stream().filter( a -> a instanceof ObjectCreationExpr
                    && a.asObjectCreationExpr().getAnonymousClassBody().isPresent()).findFirst();
            if( on.isPresent() ){
                return (ObjectCreationExpr)on.get();
            }
        }
        throw new _ioException("unable to find in anonymous object at (" + ste.getFileName() + ":"
            + ste.getLineNumber() + ")" + System.lineSeparator() + resolver.describe());

    }

    /**
     * convert the String code into a single AST Expression nod
     *
     * @param code
     * @return
     */
    public static Expression of(String... code ) {
        String str;
        if( code.length == 1 ){
            str = code[0];
        }
        else{
            str = Text.combine( code ).trim();
        }
        if( str.equals("super") ){
            return new SuperExpr();
        }
        /* TEMP REMOVED
        if( str.startsWith("@")){
            return Ast.annotationExpr(str);
            //return StaticJavaParser.parseAnnotation( str );
        }
        */
        String comment = null;
        int endComment = str.indexOf("*/");
        if( str.startsWith("/*") && endComment > 0 ) {
            //we need to manually "save" the comment
            comment = str.substring(0, endComment + 2);
            str = str.substring(endComment+2);
        }
        //a frequent mistake I make is to end expressions with ";"
        // this will fix it...(no expressions end with ;, those are ExpressionStmt
        if( str.endsWith( ";" ) ) {
            str = str.substring( 0, str.length() - 1 );
        }
        //we need to intercept ArrayInitializers,
        if( str.startsWith("{") && (str.endsWith( "}" ) ) ){

            //it could be an arrayInitialationExpresssion
            Statement st = Stmt.of("Object[] unknown = "+str+";");
            ArrayInitializerExpr aie = (ArrayInitializerExpr)
                st.asExpressionStmt().getExpression().asVariableDeclarationExpr().getVariable(0).getInitializer().get();
            aie.removeForced();
            return aie;
        }

        //try{
            ParseResult<Expression> pe = JAVAPARSER.parseExpression( str );
            Expression e = null;
            if( pe.isSuccessful() ){
                e = pe.getResult().get();
            } else{
                //System.out.println("GOT HERE -- so it's either a VariableDeclarationExpr or an annotation Expr");
                ParseResult<AnnotationExpr> pa = JAVAPARSER.parseAnnotation(str);
                if( pa.isSuccessful() ){
                    e = pa.getResult().get();
                    return e;
                }
                /*
                else {
                    System.out.println("GOT HERE 2");
                    throw new _jdraftException("Unable to parse Expression \"" + str + "\" " + System.lineSeparator() + pe.getProblems());
                }
                */
                ParseResult<VariableDeclarationExpr> pvd = JAVAPARSER.parseVariableDeclarationExpr( str );
                if( pvd.isSuccessful() ){
                    e = pvd.getResult().get();
                } else {
                    //System.out.println("GOT HERE 2");
                    throw new _jdraftException("Unable to parse Expression \"" + str + "\" " + System.lineSeparator() + pe.getProblems());
                }
            }
                    //StaticJavaParser.parseExpression( str );
            if( comment != null ){
                if( comment.startsWith("/**") ){
                    JavadocComment jdc = new JavadocComment( comment.replace("/**", "" ).replace("*/", ""));    
                    e.setComment( jdc);
                } else{
                    BlockComment bc = new BlockComment(comment.replace("/*", "" ).replace("*/", ""));                       
                    e.setComment(bc);
                }                
            }
            if( e instanceof UnaryExpr ){
                return NegativeLiteralNumberPostProcessor.replaceUnaryWithNegativeLiteral( (UnaryExpr)e);
            }
            return e;
        //}
       // catch(ParseProblemException ppe){
       //     try {
                //normal parsing of Variable Declarations fails, we need to call a special parse method
       //         return StaticJavaParser.parseVariableDeclarationExpr(str);
       //     } catch(Exception e ) {
        //        throw new _jdraftException("Unable to parse Expression \"" + str + "\" ", ppe);
        //    }
        //}
    }

    /**
     * i.e. "arr[3]"
     * @param code
     * @return
     */
    public static ArrayAccessExpr arrayAccessExpr(String... code ) {
        String cd = Text.combine(code);
        Expression ee = of(cd);
        //System.out.println("GOT EXPRESSION"+ee+" "+ee.getClass() );
        //ArrayAccessExpr aae = new ArrayAccessExpr(Text.combine(code));
        return ee.asArrayAccessExpr();
    }

    public static ArrayCreationExpr arrayCreationExpr(String... code ) {
        return of( code ).asArrayCreationExpr();
    }

    /**
     * One or more arrayCreationLevels (for multi-dimensional array) i.e. int[][] x = new x[10][20];
     * @param code
     * @return
     */
    public static NodeList<ArrayCreationLevel> arrayCreationLevels(String...code){
        ArrayCreationExpr aae = Expr.arrayCreationExpr("Object "+Text.combine(code)+" unknown ");
        return aae.getLevels();
    }

    /**
     * One or more arrayCreationLevels (for multi-dimensional array) i.e. int[][] x = new x[10][20];
     * @param code
     * @return
     */
    public static ArrayCreationLevel arrayCreationLevel(String...code){
        ArrayCreationExpr aae = Expr.arrayCreationExpr("Object "+Text.combine(code)+" unknown ");
        return aae.getLevels().get(0);
    }

    /**
     * i.e. "{1,2,3,4,5}"
     */
    public static ArrayInitializerExpr arrayInitializerExpr(String... code ) {
        String ai = Text.combine(code);
        ai = "new Object[] "+ai;
        ArrayInitializerExpr aie = of(ai).asArrayCreationExpr().getInitializer().get();
        aie.removeForced();
        return aie;
    }

    /** i.e. "a = 1", "a = 4" */
    public static AssignExpr assignExpr(String... code ) {
        return of( code ).asAssignExpr();
    }

    /** i.e. "a + b"*/
    public static BinaryExpr binaryExpr(String... code ) {
        return of( code ).asBinaryExpr();
    }

    public static BooleanLiteralExpr of( boolean b ){
        return new BooleanLiteralExpr( b );
    }

    /** "true" / "false" */
    public static BooleanLiteralExpr booleanLiteralExpr(boolean b) {
        if( b ){
            return new BooleanLiteralExpr(true);
        }
        return new BooleanLiteralExpr(false);
    }

    /** "true" / "false" */
    public static BooleanLiteralExpr booleanLiteralExpr(String... code ) {
        return of( code ).asBooleanLiteralExpr();
    }

    /** (String)o */
    public static CastExpr castExpr(String... code ) {
        return of( code ).asCastExpr();
    }

    public static CastExpr castExpr(Class castType, Expression expr ){
        return castExpr( Types.of(castType), expr);
    }

    /**
     * Builds a cast from a Type and expression
     * (type)expr
     *
     * @param type the cast type
     * @param expr the expression to be cast
     * @return the CastExpr
     */
    public static CastExpr castExpr(Type type, Expression expr ){
        CastExpr ce = new CastExpr();
        ce.setType(type);
        ce.setExpression(expr);
        return ce;
    }

    public static CharLiteralExpr of( char c ){
        return new CharLiteralExpr( c );
    }

    /** 'c' */
    public static CharLiteralExpr charLiteralExpr(char c ) {
        return new CharLiteralExpr(c);
    }

    public static CharLiteralExpr charLiteralExpr(String s) {
        return new CharLiteralExpr(s);
    }

    public static ClassExpr classExpr(Class clazz){
        return new ClassExpr(_typeRef.of(clazz).ast());
    }

    /** i.e. "String.class" */
    public static ClassExpr classExpr(String... code ) {
        return of( code ).asClassExpr();
    }

    /** saved ? return true; */
    public static ConditionalExpr conditionalExpr(String... code ) {
        return of( code ).asConditionalExpr();
    }

    /** saved ? return true; */
    public static ConditionalExpr ternaryExpr(String... code ) {
        return of( code ).asConditionalExpr();
    }

    public static DoubleLiteralExpr of( double d ){
        return new DoubleLiteralExpr( d );
    }

    public static DoubleLiteralExpr doubleLiteralExpr(double d ) {
        return new DoubleLiteralExpr( d );
    }

    /** i.e. "3.14d" */
    public static DoubleLiteralExpr doubleLiteralExpr(String... code ) {
        return of( code ).asDoubleLiteralExpr();
    }

    public static DoubleLiteralExpr of( float d ){
        return new DoubleLiteralExpr( d );
    }
    
    //public static ArrayInitializerExpr arrayInitExpr(int[] intArray ){
    //    return of( intArray);
    //}

    public static ArrayInitializerExpr arrayInitializerExpr(long...longArray) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<longArray.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( longArray[i]+"L" );
        }
        sb.append(" }");
        return arrayInitializerExpr( sb.toString() );
    }

    public static ArrayInitializerExpr arrayInitializerExpr(int... intArray ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<intArray.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( intArray[i] );
        }
        sb.append(" }");
        return arrayInitializerExpr( sb.toString() );
    }
    
    //public static ArrayInitializerExpr arrayInitExpr(float[] floatArray ){
    //    return of( floatArray);
    //}
    
    public static ArrayInitializerExpr arrayInitializerExpr(float... array ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<array.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( array[i] ).append("f");
        }
        sb.append(" }");
        return arrayInitializerExpr( sb.toString() );
    }
    
    //public static ArrayInitializerExpr arrayInitExpr(double[] array ){
     //   return of( array);
    //}
    
    public static ArrayInitializerExpr arrayInitializerExpr(double... array ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<array.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( array[i] ).append("d");
        }
        sb.append(" }");
        return arrayInitializerExpr( sb.toString() );
    }

    //public static ArrayInitializerExpr arrayInitExpr(boolean[] array ){
    //    return of( array);
   // }
    
    public static ArrayInitializerExpr arrayInitializerExpr(boolean... array ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<array.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( array[i] );
        }
        sb.append(" }");
        return arrayInitializerExpr( sb.toString() );
    }

    /*
    //public static ArrayInitializerExpr arrayInitializerExpr(char...chars ){
        return of( chars);
    }
    //public static ArrayInitializerExpr arrayInitializerExpr( boolean...bools){
        return of(bools);
    }
    //public static ArrayInitializerExpr arrayInitializerExpr( float...floats){
        return of(floats);
    }
    //public static ArrayInitializerExpr arrayInitializerExpr( double...doubles){
        return of(doubles);
    }
    //public static ArrayInitializerExpr arrayInitializerExpr( int...ints){
        return of(ints);
    }
     */

    /**
     * Creates and returns a String lister Array ArrayInitailizerExpr
     * i.e.<PRE>
     * {"A", "B", "C", "D"}
     * </PRE>
     * @param strs
     * @return
     */
    public static ArrayInitializerExpr stringArrayInitExpr(String...strs ){
        ArrayInitializerExpr ae = new ArrayInitializerExpr();
        Arrays.stream(strs).forEach( s -> ae.getValues().add( Expr.stringLiteralExpr(s)));
        return ae;
    }


    //public static ArrayInitializerExpr arrayInitializerExpr(char... array ){
    //    return arrayInitializerExpr( array );
   // }
    
    public static ArrayInitializerExpr arrayInitializerExpr(char... array ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<array.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append("'").append( array[i] ).append("'");
        }
        sb.append(" }");
        return arrayInitializerExpr( sb.toString() );
    }

    public static DoubleLiteralExpr doubleLiteralExpr(float d ) {
        return new DoubleLiteralExpr( d );
    }

    /** i.e. "3.14f"
    public static DoubleLiteralExpr doubleLiteralExpr(String... code ) {
        return of( code ).asDoubleLiteralExpr();
    }
    */


    /** i.e. ( 4 + 5 ) */
    public static EnclosedExpr parenthesizedExpr(String... code ) {
        return of( code ).asEnclosedExpr();
    }
    
    /**
     * i.e. ( 4 + val(100) )
     * Builds & returns a EnclosedExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithFieldAccessInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a EnclosedExpr
     * Ast node
     * @return the EnclosedExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static EnclosedExpr parenthesizedExpr(Function<? extends Object,? extends Object> lambdaWithFieldAccessInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambdaExpr(ste);
        return astLambda.getBody().findFirst(EnclosedExpr.class).get();
    }

    /**
     * i.e. System.out
     *
     * @param code
     * @return
     */
    public static FieldAccessExpr fieldAccessExpr(String... code ) {
        return of( code ).asFieldAccessExpr();
    }

    /**
     * i.e. System.out
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithFieldAccessInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the FieldAccessExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static FieldAccessExpr fieldAccessExpr(Function<? extends Object,? extends Object> lambdaWithFieldAccessInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambdaExpr(ste);
        return astLambda.getBody().findFirst(FieldAccessExpr.class).get();
    }

    /**
     * i.e. v instanceof Serializable
     * @param code
     * @return
     */
    public static InstanceOfExpr instanceOfExpr(String... code ) {
        return of( code ).asInstanceOfExpr();
    }

    /**
     * v instanceof Serializable
     * @param fun
     * @return
     */
    public static InstanceOfExpr instanceOfExpr(Function<? extends Object, ? extends Object> fun ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr le = lambdaExpr(ste);
        return le.getBody().findFirst(InstanceOfExpr.class).get();
    }



    public static IntegerLiteralExpr of(int i) {
        //return intExpr( ""+i);
        IntegerLiteralExpr ile = new IntegerLiteralExpr(i);
        return ile;
    }

    public static IntegerLiteralExpr integerLiteralExpr(int i) {
        //return intExpr(""+i);
        IntegerLiteralExpr ile = new IntegerLiteralExpr(i);
        return ile;
    }


    public static IntegerLiteralExpr integerLiteralExpr(String... code ) {
        Expression e = of(code);
        if( e instanceof IntegerLiteralExpr ){
            return (IntegerLiteralExpr)e;
        }
        if( e instanceof UnaryExpr && ((UnaryExpr) e).getExpression().isIntegerLiteralExpr()){
            UnaryExpr par = (UnaryExpr) e;
            //condense the UnaryExpr down to a IntLiteralExpr
            if( par.getOperator() == UnaryExpr.Operator.PLUS){
                return par.getExpression().asIntegerLiteralExpr();
            }
            if( par.getOperator() == UnaryExpr.Operator.MINUS){
                IntegerLiteralExpr ile = par.getExpression().asIntegerLiteralExpr();
                //
                String val = ile.getValue();
                ile.setValue("-"+ile.getValue());
                if( ile.getRange().isPresent()){
                    ile.setRange(par.getRange().get());
                }
                return ile;
            }
        }
        return of( code ).asIntegerLiteralExpr();
    }

    /**
     * parses and returns a lambda expression from the code
     * @param code
     * @return
     */
    public static LambdaExpr lambdaExpr(String... code ) {
        return of( code ).asLambdaExpr();
    }

    /**
     * Builds a lambda expression from the *CODE* passed in... i,.e.
     * <PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param c a "command" or no arg, no return lambda implementation
     * @return the LambdaExpr instance
     */
    public static LambdaExpr lambdaExpr(Expr.Command c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object,U extends Object>  LambdaExpr lambdaExpr(Function<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param <V>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object,U extends Object, V extends Object> LambdaExpr lambdaExpr(BiFunction<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <W>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object,U extends Object, V extends Object, W extends Object> LambdaExpr lambdaExpr(TriFunction<T, U, V, W> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <W>
     * @param <X>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object,U extends Object, V extends Object, W extends Object, X extends Object> LambdaExpr lambdaExpr(QuadFunction<T, U, V, W, X> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object>  LambdaExpr lambdaExpr(Consumer<T> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object, U extends Object>  LambdaExpr lambdaExpr(BiConsumer<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param <V>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object, U extends Object, V extends Object> LambdaExpr lambdaExpr(Expr.TriConsumer<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <A>
     * @param <B>
     * @param <C>
     * @param <D>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <A extends Object, B extends Object, C extends Object, D extends Object> LambdaExpr lambdaExpr(Expr.QuadConsumer<A,B,C,D> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaExpr( ste );
    }

    public static LongLiteralExpr of(long l) {
        return new LongLiteralExpr(l);
    }

    public static LongLiteralExpr longLiteralExpr(long l ) {
        return new LongLiteralExpr(l);
    }

    public static LongLiteralExpr longLiteralExpr(String... code ) {
        return new LongLiteralExpr(Text.combine(code));
    }


    /**
     * i.e. out.println(1)
     *
     * @param code
     * @return
     */
    public static MethodCallExpr methodCallExpr(String... code ) {
        String str = Text.combine(code);
        if( str.startsWith("<")){ //
            String s = "Object UNKNOWN = "+ str +";";
            //Box <String> box = new Box <String> ("Jack");
            //System.out.println( s );
            VariableDeclarationExpr vde =  Expr.variablesExpr(s);
            return (MethodCallExpr) vde.getVariable(0).getInitializer().get();
        }
        return of( code ).asMethodCallExpr();
    }

    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static MethodCallExpr methodCallExpr(Expr.Command lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambdaExpr(ste);
        return astLambda.getBody().findFirst(MethodCallExpr.class).get();
    }

    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static MethodCallExpr methodCallExpr(Consumer<? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambdaExpr(ste);
        return astLambda.getBody().findFirst(MethodCallExpr.class).get();
    }    
    
    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static MethodCallExpr methodCallExpr(BiConsumer<? extends Object,? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambdaExpr(ste);
        return astLambda.getBody().findFirst(MethodCallExpr.class).get();
    }  
    
    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static MethodCallExpr methodCallExpr(TriConsumer<? extends Object,? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambdaExpr(ste);
        return astLambda.getBody().findFirst(MethodCallExpr.class).get();
    }
    
    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static MethodCallExpr methodCallExpr(QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambdaExpr(ste);
        return astLambda.getBody().findFirst(MethodCallExpr.class).get();
    }

    /** 
     * i.e."String:toString" 
     * 
     * @param code
     * @return 
     */
    public static MethodReferenceExpr methodReferenceExpr(String... code ) {
        String r = Text.combine(code);
        r = "UNKNOWN -> "+ r;
        //System.out.println( r );
        LambdaExpr rs = lambdaExpr(r);
        MethodReferenceExpr mre = rs.getExpressionBody().get().asMethodReferenceExpr();
        mre.removeForced(); //DISCONNECT
        return mre;
        //return AST.expr( code ).asMethodReferenceExpr();
    }


    public static NameExpr nameExpr(String... code ) {
        return of( code ).asNameExpr();
    }


    public static AnnotationExpr annotationExpr(String... code ) {
        return of( code ).asAnnotationExpr();
    }

    /** i.e. null */
    public static NullLiteralExpr nullExpr(){
        return new NullLiteralExpr();
    }

    public static ObjectCreationExpr objectCreationExpr(Object anonymousClassImplementation ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return newExpr(ste, _io.IN_DEFAULT);
    }

    public static ObjectCreationExpr anonymousClassExpr(Object anonymousClassImplementation ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return newExpr(ste, _io.IN_DEFAULT);
    }


    /**
     * @return the ObjectCreationExpr model instance of the runtime instance
     */
    public static ObjectCreationExpr newExpr(Class clazz) {
        return Expr.newExpr("new "+clazz.getCanonicalName()+"()");
    }

    /**
     * Builds a JavaParser AST model of an Anonymous Class (an {@link ObjectCreationExpr} based on the
     * real code passed in:
     * i.e.<PRE>
     *     ObjectCreationExpr oce = Ast.anonymousClass( new Object(){
     *        public int a;
     *        public void SomeMethod(){
     *            System.out.println(1);
     *        }
     *     });
     * </PRE>
     * @param code
     * @return the ObjectCreationExpr model instance of the runtime instance
     */
    public static ObjectCreationExpr newExpr(String... code ) {
        String str = Text.combine(code);
        if( !str.startsWith("new") ){
            str = "new "+str;
        }
        if(! ( str.endsWith(")") || str.endsWith("}")) ){
            str = str+"()";
        }
        return of( str ).asObjectCreationExpr();
    }
    
    /**
     * accepts a lambda expression and (DOES NOT RUN IT) but rather
     * reads the body code and returns 
     * 
     * i.e.
     * <PRE>
     * ObjectCreationExpression oce = Ast.objectCreation( ()-> new HashMap());
     * </PRE>
     * @param lambdaThatCreatesObject a supplier lambda expression that must contain a "new"
     * @return the AST ObjectCreation Expression representing the new
     */
    public static ObjectCreationExpr newExpr(Supplier<? extends Object> lambdaThatCreatesObject ) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr le = lambdaExpr(ste);
        return le.getBody().findFirst(ObjectCreationExpr.class).get();        
    }

    public static StringLiteralExpr stringLiteralExpr(String code ){
        if( code.startsWith("\"")){
            code = code.substring(1);
            if( code.endsWith("\"")){
                code = code.substring(0, code.length() -1);
            }
        }
        return new StringLiteralExpr(code);
        /*
        String str = code;
        if(! str.startsWith("\"") ){
            str = "\""+str;
        }
        if(! str.endsWith("\"") ){
            str = str+"\"";
        }
        return new StringLiteralExpr(str);
        //return of( str ).asStringLiteralExpr();
         */
    }
    
    public static StringLiteralExpr stringLiteralExpr(String... code ) {
        String str = Text.combine( code );
        if(! str.startsWith("\"") ){
            str = "\""+str;
        }
        if(! str.endsWith("\"") ){
            str = str+"\"";
        }
        return of( str ).asStringLiteralExpr();
    }

    public static TextBlockLiteralExpr textBlockLiteralExpr(String code ){
        return new TextBlockLiteralExpr(code);
    }

    public static TextBlockLiteralExpr textBlockLiteralExpr(String... code ) {
        String str = Text.combine( code );
        return new TextBlockLiteralExpr(str);
    }

    /** 
     * "super" 
     * 
     * @return a super expression
     */
    public static SuperExpr superExpr(  ) {
        return new SuperExpr();
    }
    
    public static SuperExpr superExpr(String... expr ){
        return (SuperExpr) Expr.of( expr);
        //return (SuperExpr)StaticJavaParser.parseExpression(Text.combine(expr));
    }


    /**
     * "int res = switch(s){ case 'a': yield 2; default: yield 3; }"
     *
     * @return a switch expression
     */
    public static SwitchExpr switchExpr(  ) {
        return new SwitchExpr();
    }

    /**
     * "int res = switch(s){ case 'a': yield 2; default: yield 3; }"
     *
     * @return a switch expression
     */
    public static SwitchExpr switchExpr(String... expr ){
        return (SwitchExpr)Ast.PARSER.parseExpression(Text.combine(expr)).getResult().get();
    }


    public static ThisExpr thisExpr(  ) {
        return new ThisExpr();
    }
    
    public static ThisExpr thisExpr(String... expr ){
        return (ThisExpr) Expr.of(expr);
        //return (ThisExpr)StaticJavaParser.parseExpression(Text.combine(expr));
    }

    public static TypeExpr typeExpr(String... code ) {
        //World::greet
        String str = Text.combine(code);
        str = str + "::UNKNOWNmethod";
        TypeExpr te =  of( str ).asMethodReferenceExpr().getScope().asTypeExpr();
        te.removeForced(); //DISCONNECT
        return te;
    }


    /**
     * i.e. !isTrue()
     *
     * accepts a lambda expression and (DOES NOT RUN IT) but rather
     * reads the body code and returns 
     * 
     * i.e.
     * <PRE>
     * UnaryExpr oce = Ast.objectCreation( ()-> !true);
     * </PRE>
     * @param lambdaThatCreatesObject a supplier lambda expression that must contain a "new"
     * @return the AST ObjectCreation Expression representing the new
     */
    public static UnaryExpr unaryExpr(Supplier<? extends Object> lambdaThatCreatesObject ) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr le = lambdaExpr(ste);
        return le.getBody().findFirst(UnaryExpr.class).get();        
    }
    
    /**
     * i.e. "!equals(t)" 
     * 
     * @param code the unary operator expression
     * @return 
     */
    public static UnaryExpr unaryExpr(String... code ) {
        return of( code ).asUnaryExpr();
    }


    /**
     * i.e. "int i=1"
     * Variable declaration expression
     * (this is a "container" of a {@link com.github.javaparser.ast.body.VariableDeclarator} specifically used
     * for a local variable)
     * @param code
     * @return 
     */
    public static VariableDeclarationExpr variablesExpr(String... code ) {
        return (VariableDeclarationExpr) Expr.of(code);
        //return StaticJavaParser.parseVariableDeclarationExpr( Text.combine( code ));
    }    
    
    /**
     * We need this because syntactically there are many ways to represent the 
     * same number as an IntLiteralExpr/LongLiteralExpr.
     * <PRE>
     * i.e.
     * simple "1"
     * binary "0b01"
     * hex    "0x01"
     * 
     * </PRE>
     * @param left
     * @param right
     * @return 
     */
    
    public static boolean equal(LiteralStringValueExpr left, LiteralStringValueExpr right ){
        //System.out.println( " checking eq "+left+ " "+right); 
        if( left instanceof CharLiteralExpr ){
            return (right instanceof CharLiteralExpr) && Objects.equals(left, right);
        }
        if( left instanceof StringLiteralExpr){
            return (right instanceof StringLiteralExpr) && Objects.equals(left, right);
        }
        return equal( parseNumber(left.getValue()), parseNumber( right.getValue() ) );
    }
    
    /**
     * 
     * @param left
     * @param right
     * @return 
     */
    public static boolean equal(Number left, Number right ){
        if( left.getClass() == right.getClass() ){
            return left.equals(right);
        }
        if( (left.getClass() == Integer.class || left.getClass() == Long.class) 
          && (right.getClass() == Integer.class || right.getClass() == Long.class ) ){
            return left.longValue() == right.longValue();
        }
        return false;
    }
    
    /**
     * 
     * @param left
     * @param right
     * @return 
     */
    public static boolean equal(Expression left, Expression right ){
        if( left == null ){
            return right == null;
        }
        if( right == null ){
            return false;
        }
        if( left.getClass() != right.getClass()){
            return false;
        }
        if( left instanceof LiteralStringValueExpr ){
            return equal( (LiteralStringValueExpr)left , (LiteralStringValueExpr)right);
        }
        if( left instanceof ArrayInitializerExpr ){
            ArrayInitializerExpr ll = (ArrayInitializerExpr)left;
            ArrayInitializerExpr rr = (ArrayInitializerExpr)right;
            NodeList<Expression> lvs = ll.getValues();
            NodeList<Expression> rvs = rr.getValues();
            if( lvs.size() != lvs.size() ){
                return false;
            }
            for(int i=0;i<lvs.size();i++){
                if( ! equal( lvs.get(i), rvs.get(i))){
                    return false;
                }
            }
            return true;
        }
        //todo more work to be done
        return left.equals(right);
    }
     
    /**
     * 
     * @param left
     * @param right
     * @return 
     */
    public static boolean equalAnnos(NodeWithAnnotations left, NodeWithAnnotations right ){
       NodeList<AnnotationExpr> las = left.getAnnotations();
       NodeList<AnnotationExpr> ras = right.getAnnotations();
       
       if( las.size() != ras.size() ){
           return false;
       }
       for(int i=0;i<las.size(); i++){
           AnnotationExpr la = las.get(i);
           if( !ras.stream().filter(a -> equal(a, la) ).findFirst().isPresent() ){
               return false;
           }
       }
       return true;       
    }
    
    /**
     * 
     * @param left
     * @param right
     * @return 
     */
    public static boolean equal(AnnotationExpr left, AnnotationExpr right){
        if( left == null ){
            return right == null;
        }
        if( right == null ){
            return false;
        }
        if( !normalizeName( left.getNameAsString()).equals( normalizeName(right.getNameAsString()) ) ){
            return false;
        }
        if( left instanceof MarkerAnnotationExpr){
            if( right instanceof MarkerAnnotationExpr){
                return true;
            }
            if( right instanceof SingleMemberAnnotationExpr ){
                return false;
            }
            return ((NormalAnnotationExpr)right).getPairs().isEmpty();            
        }
        if( left instanceof SingleMemberAnnotationExpr){
            if( right instanceof MarkerAnnotationExpr){
                return false;
            }
            if( right instanceof SingleMemberAnnotationExpr ){
                return equal( ((SingleMemberAnnotationExpr) left).getMemberValue(),
                    ((SingleMemberAnnotationExpr) right).getMemberValue());
            }
            NormalAnnotationExpr ra = ((NormalAnnotationExpr)right);            
            if( ra.getPairs().size() == 1 && ra.getPairs().get(0).getNameAsString().equals("value")){
                return equal( ((SingleMemberAnnotationExpr) left).getMemberValue(),
                    ra.getPairs().get(0).getValue() );
            }
            return false;
        }
        //left Must be a NormalAnnotationExpr
        NormalAnnotationExpr la = (NormalAnnotationExpr)left;
        if( right instanceof MarkerAnnotationExpr){
            return la.getPairs().isEmpty();
        }
        if( right instanceof SingleMemberAnnotationExpr){
            if( la.getPairs().size() == 1 && la.getPairs().get(0).getNameAsString().equals("value") ){
                return equal( ((SingleMemberAnnotationExpr) right).getMemberValue(),
                    la.getPairs().get(0).getValue() );
            }
            return false;
        }
        NormalAnnotationExpr ra = (NormalAnnotationExpr)right;
        
        if( la.getPairs().size() != ra.getPairs().size()){
            return false;
        }
        for(int i=0;i<la.getPairs().size(); i++){
            String name = la.getPairs().get(i).getNameAsString();
            Expression ex = la.getPairs().get(i).getValue();
            if( !ra.getPairs().stream().filter( p -> p.getNameAsString().equals(name) && equal( p.getValue(), ex) ).findFirst().isPresent()){
                return false;
            }
        }
        return true;        
    }
    
    /**
     * 
     * @param exp
     * @param o could be another expression, a String, or a value (integer, Float, array, etc.)
     * @return 
     */
    public static boolean equal(Expression exp, Object o) {
        if( exp == null){
            return o == null;
        }
        if( o instanceof Expression ){
            return equal( exp, (Expression)exp );
        }
        if( o == null || o instanceof NullLiteralExpr || o.equals("null") ) {
            return exp.equals( new NullLiteralExpr() );
        }
        if( exp instanceof LiteralStringValueExpr && o instanceof LiteralStringValueExpr ){
            return equal( (LiteralStringValueExpr) exp, (LiteralStringValueExpr)o);
        }
        else if( o instanceof String ){
            try{
                Expression e = Expr.of( (String)o);
                return equal(exp , e );
            }catch(Exception e){
                if( exp instanceof StringLiteralExpr ){
                    return equal( exp, Expr.stringLiteralExpr(o.toString()) );
                }
            }
        }
        //handle All Wrapper types
        else if( o instanceof Number ||  o instanceof Boolean ){ //Int Float, etc.
            return equal( Expr.of(o.toString()), exp );
        }
        else if(o instanceof Character ){
            return Objects.equals( Expr.charLiteralExpr( (Character)o), exp );
        }
        //arrays?
        else if( o.getClass().isArray() ){
            if( o.getClass().getComponentType().isPrimitive() ){
                Class ct = o.getClass().getComponentType();
                if( ct == int.class ){
                    return equal(exp, Expr.arrayInitializerExpr( (int[])o) );
                }
                if( ct == float.class ){
                    return equal(exp, Expr.arrayInitializerExpr( (float[])o) );
                }
                if( ct == double.class ){
                    return equal(exp, Expr.arrayInitializerExpr( (double[])o) );
                }
                if( ct == boolean.class ){
                    return equal(exp, Expr.arrayInitializerExpr( (boolean[])o) );
                }
                if( ct == char.class ){
                    return equal(exp, Expr.arrayInitializerExpr( (char[])o) );
                }
                throw new _jdraftException("Only simple primitive types supported");
            } 
        }
        return false;
    }    
    
    /**
     * We need to do this for Expressions because there are many ways to 
     * syntactically represent the same (semantic) thing
     * i.e. 
     * 
     * @param e
     * @return 
     */
    public static int hash( Expression e ){
        if( e == null ){
            return 0;
        }
        //here the expressions store their (numberic) values as Strings
        // so there are a few ways to represent the same number
        // 0b01110
        // 0xDEADBEEF
        // ...so we need to convert from the String representation to the 
        // number representation and use that for the hashcode
        if( e instanceof LiteralExpr ){
            if( e instanceof LiteralStringValueExpr){
                if( e instanceof StringLiteralExpr ){
                    return e.hashCode();
                } else if( e instanceof IntegerLiteralExpr ){
                    return Objects.hash( IntegerLiteralExpr.class, parseInt( (IntegerLiteralExpr)e ) );
                } else if( e instanceof LongLiteralExpr ){
                    return Objects.hash( LongLiteralExpr.class, parseLong( (LongLiteralExpr)e ) );
                } else if( e instanceof DoubleLiteralExpr ){
                    return Objects.hash( DoubleLiteralExpr.class, parseNumber( ((LiteralStringValueExpr)e).getValue() ) );
                } else if( e instanceof BooleanLiteralExpr ){
                    return e.hashCode();
                } else if( e instanceof CharLiteralExpr ){
                    return e.hashCode();
                } 
                return Objects.hash( e );            
            } 
            return e.hashCode(); //NullLiteral
        }
        if( e instanceof ArrayInitializerExpr){
            //an array CAN have literals in them, so I have to do a visit/walk/hash 
            //for each element
            ArrayInitializerExpr aee = (ArrayInitializerExpr)e;
            NodeList<Expression> es = aee.getValues();
            List<Integer> hashy = new ArrayList<>();
            for(int i=0;i<es.size();i++){
                hashy.add( hash(es.get(i)) );
            }
            return hashy.hashCode();
        }
        if( e instanceof AnnotationExpr){
            return hash( (AnnotationExpr)e);
        }
        //TODO I need to walk other entities (especially for complex things like
        // 
        return e.hashCode();
    }
    
    
    public static int memberValuePairHash(MemberValuePair mvp) {
        if (mvp.getValue() instanceof AnnotationExpr) {
            return Objects.hash(mvp.getNameAsString(), hash((AnnotationExpr) mvp.getValue()));
        }
        return Objects.hash(mvp.getNameAsString(), hash(mvp.getValue()));
    }
    
    public static String normalizeName(String name) {
        int idx = name.lastIndexOf('.');
        if (idx < 0) {
            return name;
        }
        return name.substring(idx + 1);
    }
    
    public static int hashAnnos( NodeWithAnnotations nwa ){
        Set<Integer> annoHashes = new HashSet<>();
        nwa.getAnnotations().forEach(a -> annoHashes.add(hash( (AnnotationExpr)a) ) );
        return annoHashes.hashCode();
    }
    
    /**
     * Return a Hash of an Annotation Expression we need to do this because
     * <PRE>
     * 1) the name can be simple or fully qualified
     * 2) key-value pairs can appear in any order
     *
     * we want this annotation:
     * @annotation(a=1,b='e')
     * ...to be "equal" to this annotation:
     * @fully.qualified.annotation(b='e',a=1)
     *
     * </PRE>
     *
     * @param ae
     * @return
     */
    public static int hash(AnnotationExpr ae) {
        if (ae instanceof NormalAnnotationExpr) {
            NormalAnnotationExpr nae = (NormalAnnotationExpr) ae;
            String normalizedName = normalizeName(nae.getNameAsString());

            NodeList<MemberValuePair> pairs = nae.getPairs();
            //Annotations can contain OTHER ANNOTATIONS
            Set<Integer> memberValueHashes = new HashSet<>(); //Hash them all
            pairs.forEach(p -> memberValueHashes.add(memberValuePairHash(p)));
            return Objects.hash(normalizedName, memberValueHashes);
        }
        if (ae instanceof SingleMemberAnnotationExpr) {
            SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr) ae;
            String normalizedName = normalizeName(sa.getNameAsString());
            Expression memberValue = sa.getMemberValue();
            if (memberValue instanceof AnnotationExpr) {
                //we ALSO have t
                return Objects.hash(normalizedName, hash((AnnotationExpr) memberValue));
            }
            //Set<MemberValuePair> mvps = new HashSet<>();
            Set<Integer> memberValueHashes = new HashSet<>(); //Hash them all
            memberValueHashes.add( memberValuePairHash(new MemberValuePair("value", memberValue) ) );
            //we want this: 
            // @a(1)
            //to be the same as:
            // @a(value=1)
            return Objects.hash(normalizedName, memberValueHashes);
        }
        //it's a marker annotation
        return Objects.hash(normalizeName(ae.getNameAsString()));
    }
    
     /**
     * A local number format we can use to compare number literals...
     * we need this because number can have different syntax
     * 
     */
    private static final NumberFormat NF = NumberFormat.getInstance();
    
    public static boolean equal(LiteralStringValueExpr ie, Expression e){
        if( e instanceof LiteralStringValueExpr ){
            return parseLong(ie).equals( parseLong( e.asLiteralStringValueExpr().getValue() ) );
        }
        return false;
    }
    
    public static Long parseLong( LiteralStringValueExpr les ){
        return parseNumber(les.getValue()).longValue();
    }
    
    public static Long parseLong( IntegerLiteralExpr ile){
        Number n = parseNumber(ile.getValue());
        return n.longValue();        
    }
    
    public static Long parseLong( LongLiteralExpr lle){
        Number n = parseNumber(lle.getValue());
        return n.longValue();        
    }
    
    public static Integer parseInt( IntegerLiteralExpr ile ){
        return parseInt(ile.getValue());
    }
    
    public static Long parseLong( String s ){
        Number n = parseNumber(s);
        return n.longValue();        
    }
    
    /**
     * 
     * @param s
     * @return 
     */
    public static Integer parseInt( String s){
        Number n = parseNumber(s);
        return n.intValue();        
    }

    /**
     * This String can represent a Float or a Double, since we ONLY have
     * DoubleLiteralExpr to model both Float and Double Literals
     *
     * @param floatOrDbl
     * @return
     */
    public static Number parseFloatOrDouble( String floatOrDbl){
        floatOrDbl = floatOrDbl.trim().replace("_", "");
        if( floatOrDbl.endsWith("f")|| floatOrDbl.endsWith("F")){
            return Float.parseFloat(floatOrDbl.substring(0, floatOrDbl.length() -1));
        }
        if( floatOrDbl.endsWith("d")|| floatOrDbl.endsWith("D") ){
            return Double.parseDouble(floatOrDbl.substring(0, floatOrDbl.length() -1));
        }
        return Double.parseDouble(floatOrDbl);
    }

    /**
     * This String can represent a Float or a Double, since we ONLY have
     * DoubleLiteralExpr to model both Float and Double Literals
     *
     * @param dblExpr
     * @return
     */
    public static Number parseFloatOrDouble( DoubleLiteralExpr dblExpr ){
        return parseFloatOrDouble(dblExpr.toString());
    }

    /**
     * Parses and returns the number from the String
     * 
     * NOTE this is public for testing, but only really used internally
     * @param s
     * @return 
     */
    public static Number parseNumber( String s ){
        //Long l = 0xAAA_BBBL; this is valid
        String str = s.trim();
        if(str.startsWith("0x") || str.startsWith("0X") ){
            if( str.endsWith("L") || str.endsWith("l")){
                //Im a simple man... and If I can represent some long as a string
                //in some form in Java, JAVA should provide a parser that (given this string)
                //returns me the appropriate long value... but NOOOOOOOO
                // they have to just screw it all up and waste my time with this 
                // bullshit
                if( str.equals("0x0L") || str.equals("0x0l") || str.equals("0X0L") ){
                    return 0L;
                }                
                return new BigInteger( str.replace("L", "").replace("l", "")
                    .replace("0x", "").replace("0X", "").replace("_", ""), 16).longValue();                
            }
            return Integer.parseUnsignedInt(str.substring(2).replace("_", ""), 16);
        }
        if( str.startsWith("0b")|| str.startsWith("0B")){
            if( str.endsWith("L") || str.endsWith("l") ){
                String subSt = str.substring(2, str.length() -1);                
                return Long.parseUnsignedLong(subSt.replace("_", ""), 2);
            }
            return Integer.parseInt(str.substring(2).replace("_", ""), 2);
        }        
        try{
            return NumberFormat.getInstance().parse(
                    str.replace("_", "")
                            .replace("F", "f")
                            .replace("D", "d"));
        }catch( Exception e){
            throw new RuntimeException(""+e);
        }
    }

    public static class Classes {

        public static final Class<Expression> EXPRESSION = Expression.class;

        /**
         * i.e. "arr[3]"
         * "arr[n]"
         * "arr[size - 1]"
         */
        public static final Class<ArrayAccessExpr> ARRAY_ACCESS = ArrayAccessExpr.class;

        /**
         * i.e. "int[][] matrix "
         */
        public static final Class<ArrayCreationExpr> ARRAY_CREATION = ArrayCreationExpr.class;

        /**
         *
         */
        public static final Class<ArrayCreationLevel> ARRAY_CREATION_LEVEL = ArrayCreationLevel.class;

        /**
         * i.e. "{1,2,3,4,5}"
         */
        public static final Class<ArrayInitializerExpr> ARRAY_INITIALIZER = ArrayInitializerExpr.class;

        /** i.e. "a = 1", "a = 4" */
        public static final Class<AssignExpr> ASSIGN = AssignExpr.class;

        /** i.e. "a || b"*/
        public static final Class<BinaryExpr> BINARY = BinaryExpr.class;

        public static final Class<BooleanLiteralExpr> BOOLEAN_LITERAL = BooleanLiteralExpr.class;

        public static final Class<CastExpr> CAST = CastExpr.class;


        /** 'c' */
        public static final Class<CharLiteralExpr> CHAR_LITERAL = CharLiteralExpr.class;

        /** String.class */
        public static final Class<ClassExpr> CLASS = ClassExpr.class;


        /**
         * saved ? return true;
         */
        public static final Class<ConditionalExpr> TERNARY = ConditionalExpr.class;


        /** 3.14d */
        public static final Class<DoubleLiteralExpr> DOUBLE_LITERAL = DoubleLiteralExpr.class;


        public static final Class<EnclosedExpr> PARENTHESIZED = EnclosedExpr.class;
        /**
         * person.NAME
         */
        public static final Class<FieldAccessExpr> FIELD_ACCESS = FieldAccessExpr.class;



        public static final Class<NullLiteralExpr> NULL = NullLiteralExpr.class;

        public static final Class<ObjectCreationExpr> OBJECT_CREATION = ObjectCreationExpr.class;
        /**
         * <PRE>"v instanceof Serializable"</PRE>
         */
        public static final Class<InstanceOfExpr> INSTANCEOF = InstanceOfExpr.class;

        public static final Class<IntegerLiteralExpr> INT_LITERAL = IntegerLiteralExpr.class;

        public static final Class<LambdaExpr> LAMBDA = LambdaExpr.class;


        public static final Class<LongLiteralExpr> LONG_LITERAL = LongLiteralExpr.class;


        public static final Class<MethodCallExpr> METHOD_CALL = MethodCallExpr.class;


        /** i.e. "String:toString" */
        public static final Class<MethodReferenceExpr> METHOD_REFERENCE = MethodReferenceExpr.class;


        public static final Class<NameExpr> NAME = NameExpr.class;

        /**
         * Any implementation {@link AnnotationExpr}
         * ({@link MarkerAnnotationExpr}, {@link SingleMemberAnnotationExpr}, {@link NormalAnnotationExpr})
         */
        public static final Class<AnnotationExpr> ANNOTATION_ANY = AnnotationExpr.class;

        /**
         * i.e. {@code @Deprecated }
         */
        public static final Class<MarkerAnnotationExpr> ANNOTATION_MARKER = MarkerAnnotationExpr.class;

        /**
         * @Generated("2/14/1985")
         */
        public static final Class<SingleMemberAnnotationExpr> ANNOTATION_SINGLE_MEMBER = SingleMemberAnnotationExpr.class;

        /**
         * @KeyValue(key1="string",key2=12)
         */
        public static final Class<NormalAnnotationExpr> ANNOTATION_NORMAL = NormalAnnotationExpr.class;


        public static final Class<StringLiteralExpr> STRING_LITERAL = StringLiteralExpr.class;


        /**
         * """
         * textBlock
         *    on
         *    multiple
         *    lines
         * """
         */
        public static final Class<TextBlockLiteralExpr> TEXT_BLOCK = TextBlockLiteralExpr.class;


        /** "super" */
        public static final Class<SuperExpr> SUPER = SuperExpr.class;


        /** "int res = switch(s){ case 'a': yield 2; default: yield 3; }" */
        public static final Class<SwitchExpr> SWITCH = SwitchExpr.class;

        public static final Class<ThisExpr> THIS = ThisExpr.class;
        public static final Class<TypeExpr> TYPE = TypeExpr.class;

        public static final Class<UnaryExpr> UNARY = UnaryExpr.class;

        /**
         * int i = 1
         */
        public static final Class<VariableDeclarationExpr> VARIABLE_LOCAL_EXPR = VariableDeclarationExpr.class;

    }
}
