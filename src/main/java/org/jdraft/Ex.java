package org.jdraft;

import java.math.BigInteger;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.*;

import com.github.javaparser.*;
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

/**
 * Utility for converting free form Strings and Runtime entities (lambdas, Anonymous Objects)
 * into JavaParser AST {@link Expression} implementations.
 * 
 * Simplify the mediation between different representations of the same thing.
 * @author M. Eric DeFazio
 */
public enum Ex {
    ;

    /**
     * Functional interface for no input, no return lambda function
     * (Used when we pass in Lambdas to the {@link Ex#lambdaEx(Command)} operation
     * in order to get the LamdbaExpr AST from the Runtime to a 
     * {@link com.github.javaparser.ast.expr.LambdaExpr}
     */
    @FunctionalInterface
    public interface Command{
        void consume();
    }

    /**
     * Functional interface for (3) input, (1) return lambda function
     * (Used when we pass in Lambdas to the {@link Ex#lambdaEx(TriFunction)}
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
     * (Used when we pass in Lambdas to the {@link Ex#lambdaEx(QuadFunction)}
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
     * (Used when we pass in Lambdas to the {@link Ex#lambdaEx(TriConsumer)}
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
     * (Used when we pass in Lambdas to the {@link Ex#lambdaEx(QuadConsumer)}
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
    public static LambdaExpr of( Ex.Command c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
        return lambdaEx( ste );
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
        return lambdaEx( ste );
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
        return lambdaEx( ste );
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
        return lambdaEx( ste );
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
        return lambdaEx( ste );
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
        return lambdaEx( ste );
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
        return lambdaEx( ste );
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
    public static LambdaExpr lambdaEx(StackTraceElement ste ) {
        return lambdaEx(ste, _io.IN_DEFAULT );
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
    public static LambdaExpr lambdaEx(StackTraceElement ste, _in._resolver resolver ){
        _type _t = null;
        try {
            //System.out.println( ste.toString() );
            Class clazz = Class.forName(ste.getClassName());
            _t = _java.type(clazz, resolver);
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
            Ast.METHOD_CALL_EXPR,
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
     * alternatively, you can use the {@link #newEx(Object)}
     * method and pass in an _in.resolver to locate the code manually
     * 
     * @see org.jdraft.io._io._config#inFilesPath(java.lang.String)
     * @see org.jdraft.io._io._config#inProjectsPath(java.lang.String)
     * @param anonymousObject an anonymous Object
     * @return the ObjectCreationExpr AST representation of the anonymousObject passed in
     */
    public static ObjectCreationExpr newEx(Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return newEx(ste, _io.IN_DEFAULT);
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
    public static ObjectCreationExpr newEx(Object anonymousObject, _in._resolver resolver){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return newEx(ste, resolver);
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
    public static ObjectCreationExpr newEx(StackTraceElement ste ){
         return newEx(ste, _io.IN_DEFAULT );
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
    public static ObjectCreationExpr newEx(StackTraceElement ste, _in._resolver resolver ){
        _type _t = null;
        try {
            Class clazz = Class.forName(ste.getClassName());
            _t = _java.type(clazz, resolver);
        } catch (Exception e) {
            throw new _ioException("no .java source for Runtime Class \"" + ste.getClassName() + "\" " + System.lineSeparator() +
                resolver.describe(), e); //print out the input config to help
        }

        //find all of the potential method calls that could be the call 
        //mentioned in the stack trace based on the line numbers 
        List<MethodCallExpr> mces = Walk.list(Walk.POST_ORDER,
                _t.ast(),
                Ast.METHOD_CALL_EXPR,
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
            Statement st = Stmt.of("Object[] arr = "+str+";");
            ArrayInitializerExpr aie = (ArrayInitializerExpr)
                st.asExpressionStmt().getExpression().asVariableDeclarationExpr().getVariable(0).getInitializer().get();
            aie.removeForced();
            return aie;
        }
        try{
            Expression e = StaticJavaParser.parseExpression( str );
            if( comment != null ){
                if( comment.startsWith("/**") ){
                    JavadocComment jdc = new JavadocComment( comment.replace("/**", "" ).replace("*/", ""));    
                    e.setComment( jdc);
                } else{
                    BlockComment bc = new BlockComment(comment.replace("/*", "" ).replace("*/", ""));                       
                    e.setComment(bc);
                }                
            }
            return e;
        }
        catch(ParseProblemException ppe){
            try {
                //normal parsing of Variable Declarations fails, we need to call a special parse method
                return StaticJavaParser.parseVariableDeclarationExpr(str);
            } catch(Exception e ) {
                throw new _jdraftException("Unable to parse Expression \"" + str + "\" ", ppe);
            }
        }
    }

    public static final Class<Expression> EXPRESSION = Expression.class;

    /**
     * i.e. "arr[3]"
     */
    public static final Class<ArrayAccessExpr> ARRAY_ACCESS = ArrayAccessExpr.class;

    /**
     * i.e. "arr[3]"
     * @param code
     * @return
     */
    public static ArrayAccessExpr arrayAccessEx(String... code ) {
        return of( code ).asArrayAccessExpr();
    }

    /**
     * i.e. "int[][] matrix "
     */
    public static final Class<ArrayCreationExpr> ARRAY_CREATION = ArrayCreationExpr.class;

    public static ArrayCreationExpr arrayCreationEx(String... code ) {
        return of( code ).asArrayCreationExpr();
    }

    /**
     * i.e. "{1,2,3,4,5}"
     */
    public static final Class<ArrayInitializerExpr> ARRAY_INITIALIZER = ArrayInitializerExpr.class;

    /**
     * i.e. "{1,2,3,4,5}"
     */
    public static ArrayInitializerExpr arrayInitializerEx(String... code ) {
        String ai = Text.combine(code);
        ai = "new Object[] "+ai;
        ArrayInitializerExpr aie = of(ai).asArrayCreationExpr().getInitializer().get();
        aie.removeForced();
        return aie;
    }

    /** i.e. "a = 1", "a = 4" */
    public static final Class<AssignExpr> ASSIGN = AssignExpr.class;

    /** i.e. "a = 1", "a = 4" */
    public static AssignExpr assignEx(String... code ) {
        return of( code ).asAssignExpr();
    }

    /** i.e. "a || b"*/
    public static final Class<BinaryExpr> BINARY = BinaryExpr.class;

    /** i.e. "a + b"*/
    public static BinaryExpr binaryEx(String... code ) {
        return of( code ).asBinaryExpr();
    }

    public static final Class<BooleanLiteralExpr> BOOLEAN_LITERAL = BooleanLiteralExpr.class;

    public static BooleanLiteralExpr of( boolean b ){
        return new BooleanLiteralExpr( b );
    }

    /** "true" / "false" */
    public static BooleanLiteralExpr booleanLiteralEx(boolean b) {
        if( b ){
            return new BooleanLiteralExpr(true);
        }
        return new BooleanLiteralExpr(false);
    }

    /** "true" / "false" */
    public static BooleanLiteralExpr booleanLiteralEx(String... code ) {
        return of( code ).asBooleanLiteralExpr();
    }

    public static final Class<CastExpr> CAST = CastExpr.class;

    /** (String)o */
    public static CastExpr castEx(String... code ) {
        return of( code ).asCastExpr();
    }

    public static CastExpr castEx(Class castType, Expression expr ){
        return castEx( Ast.typeRef(castType), expr);
    }

    /**
     * Builds a cast from a Type and expression
     * (type)expr
     *
     * @param type the cast type
     * @param expr the expression to be cast
     * @return the CastExpr
     */
    public static CastExpr castEx(Type type, Expression expr ){
        CastExpr ce = new CastExpr();
        ce.setType(type);
        ce.setExpression(expr);
        return ce;
    }

    /** 'c' */
    public static final Class<CharLiteralExpr> CHAR_LITERAL = CharLiteralExpr.class;

    public static CharLiteralExpr of( char c ){
        return new CharLiteralExpr( c );
    }

    /** 'c' */
    public static CharLiteralExpr charLiteralEx(char c ) {
        return new CharLiteralExpr(c);
    }

    /** 'c' */
    public static CharLiteralExpr charLiteralEx(String... code ) {
        return of( code ).asCharLiteralExpr();
    }

    /** String.class */
    public static final Class<ClassExpr> CLASS = ClassExpr.class;

    /** i.e. "String.class" */
    public static ClassExpr classEx(String... code ) {
        return of( code ).asClassExpr();
    }

    /**
     * saved ? return true;
     */
    public static final Class<ConditionalExpr> CONDITIONAL = ConditionalExpr.class;

    /** saved ? return true; */
    public static ConditionalExpr conditionalEx(String... code ) {
        return of( code ).asConditionalExpr();
    }

    /** 3.14d */
    public static final Class<DoubleLiteralExpr> DOUBLE_LITERAL = DoubleLiteralExpr.class;

    public static DoubleLiteralExpr of( double d ){
        return new DoubleLiteralExpr( d );
    }

    public static DoubleLiteralExpr doubleLiteralEx(double d ) {
        return new DoubleLiteralExpr( d );
    }

    /** i.e. "3.14d" */
    public static DoubleLiteralExpr doubleLiteralEx(String... code ) {
        return of( code ).asDoubleLiteralExpr();
    }

    public static DoubleLiteralExpr of( float d ){
        return new DoubleLiteralExpr( d );
    }
    
    public static ArrayInitializerExpr arrayInitializerEx(int[] intArray ){
        return of( intArray);
    }
    
    public static ArrayInitializerExpr of( int[] intArray ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<intArray.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( intArray[i] );
        }
        sb.append(" }");
        return arrayInitializerEx( sb.toString() );
    }
    
    public static ArrayInitializerExpr arrayInitializerEx(float[] floatArray ){
        return of( floatArray);
    }
    
    public static ArrayInitializerExpr of( float[] array ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<array.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( array[i] ).append("f");
        }
        sb.append(" }");
        return arrayInitializerEx( sb.toString() );
    }
    
    public static ArrayInitializerExpr arrayInitializerEx(double[] array ){
        return of( array);
    }
    
    public static ArrayInitializerExpr of( double[] array ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<array.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( array[i] ).append("d");
        }
        sb.append(" }");
        return arrayInitializerEx( sb.toString() );
    }

    public static ArrayInitializerExpr arrayInitializerEx(boolean[] array ){
        return of( array);
    }
    
    public static ArrayInitializerExpr of( boolean[] array ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<array.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( array[i] );
        }
        sb.append(" }");
        return arrayInitializerEx( sb.toString() );
    }
    
    public static ArrayInitializerExpr arrayInitializerEx(char[] array ){
        return of( array );
    }
    
    public static ArrayInitializerExpr of( char[] array ){
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(int i=0;i<array.length;i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append("'").append( array[i] ).append("'");
        }
        sb.append(" }");
        return arrayInitializerEx( sb.toString() );
    }
    

    public static DoubleLiteralExpr doubleLiteralEx(float d ) {
        return new DoubleLiteralExpr( d );
    }

    /** i.e. "3.14f" */
    public static DoubleLiteralExpr floatLiteralEx(String... code ) {
        return of( code ).asDoubleLiteralExpr();
    }

    public static final Class<EnclosedExpr> ENCLOSED = EnclosedExpr.class;

    /** i.e. ( 4 + 5 ) */
    public static EnclosedExpr enclosedEx(String... code ) {
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
    public static EnclosedExpr enclosedEx(Function<? extends Object,? extends Object> lambdaWithFieldAccessInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return astLambda.getBody().findFirst(EnclosedExpr.class).get();
    }

    /**
     * person.NAME
     */
    public static final Class<FieldAccessExpr> FIELD_ACCESS = FieldAccessExpr.class;

    /**
     * i.e. System.out
     *
     * @param code
     * @return
     */
    public static FieldAccessExpr fieldAccessEx(String... code ) {
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
    public static FieldAccessExpr fieldAccessEx(Function<? extends Object,? extends Object> lambdaWithFieldAccessInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return astLambda.getBody().findFirst(FieldAccessExpr.class).get();
    }
    
    /**
     * <PRE>"v instanceof Serializable"</PRE>
     */
    public static final Class<InstanceOfExpr> INSTANCEOF = InstanceOfExpr.class;

    /**
     * i.e. v instanceof Serializable
     * @param code
     * @return
     */
    public static InstanceOfExpr instanceOfEx(String... code ) {
        return of( code ).asInstanceOfExpr();
    }

    /**
     * v instanceof Serializable
     * @param fun
     * @return
     */
    public static InstanceOfExpr instanceOfEx(Function<? extends Object, ? extends Object> fun ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr le = lambdaEx(ste);
        return le.getBody().findFirst(InstanceOfExpr.class).get();
    }

    public static final Class<IntegerLiteralExpr> INT_LITERAL = IntegerLiteralExpr.class;

    public static IntegerLiteralExpr of(int i) {
        return new IntegerLiteralExpr(i);
    }

    public static IntegerLiteralExpr intLiteralEx(int i) {
        return new IntegerLiteralExpr(i);
    }

    public static IntegerLiteralExpr intLiteralEx(String... code ) {
        return of( code ).asIntegerLiteralExpr();
    }

    public static final Class<LambdaExpr> LAMBDA = LambdaExpr.class;

    /**
     * parses and returns a lambda expression from the code
     * @param code
     * @return
     */
    public static LambdaExpr lambdaEx(String... code ) {
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
    public static LambdaExpr lambdaEx(Ex.Command c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
    public static <T extends Object,U extends Object>  LambdaExpr lambdaEx(Function<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
    public static <T extends Object,U extends Object, V extends Object> LambdaExpr lambdaEx(BiFunction<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
    public static <T extends Object,U extends Object, V extends Object, W extends Object> LambdaExpr lambdaEx(TriFunction<T, U, V, W> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
    public static <T extends Object,U extends Object, V extends Object, W extends Object, X extends Object> LambdaExpr lambdaEx(QuadFunction<T, U, V, W, X> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
    public static <T extends Object>  LambdaExpr lambdaEx(Consumer<T> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
    public static <T extends Object, U extends Object>  LambdaExpr lambdaEx(BiConsumer<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
    public static <T extends Object, U extends Object, V extends Object> LambdaExpr lambdaEx(Ex.TriConsumer<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
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
    public static <A extends Object, B extends Object, C extends Object, D extends Object> LambdaExpr lambdaEx(Ex.QuadConsumer<A,B,C,D> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return lambdaEx( ste );
    }
    
    public static final Class<LongLiteralExpr> LONG_LITERAL = LongLiteralExpr.class;

    public static LongLiteralExpr of(long l) {
        return new LongLiteralExpr(l);
    }

    public static LongLiteralExpr longLiteralEx(long l ) {
        return new LongLiteralExpr(l);
    }

    public static LongLiteralExpr longLiteralEx(String... code ) {
        return new LongLiteralExpr(Text.combine(code));
    }

    public static final Class<MethodCallExpr> METHOD_CALL = MethodCallExpr.class;

    /**
     * i.e. out.println(1)
     *
     * @param code
     * @return
     */
    public static MethodCallExpr methodCallEx(String... code ) {
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
    public static MethodCallExpr methodCallEx(Ex.Command lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
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
    public static MethodCallExpr methodCallEx(Consumer<? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
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
    public static MethodCallExpr methodCallEx(BiConsumer<? extends Object,? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
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
    public static MethodCallExpr methodCallEx(TriConsumer<? extends Object,? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
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
    public static MethodCallExpr methodCallEx(QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return astLambda.getBody().findFirst(MethodCallExpr.class).get();
    }
    
    /** i.e. "String:toString" */
    public static final Class<MethodReferenceExpr> METHOD_REFERENCE = MethodReferenceExpr.class;

    /** 
     * i.e."String:toString" 
     * 
     * @param code
     * @return 
     */
    public static MethodReferenceExpr methodReferenceEx(String... code ) {
        String r = Text.combine(code);
        r = "o -> "+ r;
        //System.out.println( r );
        LambdaExpr rs = lambdaEx(r);
        MethodReferenceExpr mre = rs.getExpressionBody().get().asMethodReferenceExpr();
        mre.removeForced(); //DISCONNECT
        return mre;
        //return AST.expr( code ).asMethodReferenceExpr();
    }

    public static final Class<NameExpr> NAME = NameExpr.class;

    public static NameExpr nameEx(String... code ) {
        return of( code ).asNameExpr();
    }

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

    public static AnnotationExpr annotation( String... code ) {
        return of( code ).asAnnotationExpr();
    }

    public static final Class<NullLiteralExpr> NULL = NullLiteralExpr.class;

    /** i.e. null */
    public static NullLiteralExpr nullEx(){
        return new NullLiteralExpr();
    }

    public static final Class<ObjectCreationExpr> OBJECT_CREATION = ObjectCreationExpr.class;

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
     * @param anonymousClassImplementation
     * @return the ObjectCreationExpr model instance of the runtime instance
     */
    public static ObjectCreationExpr anonymousClassEx(Object anonymousClassImplementation ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return newEx(ste, _io.IN_DEFAULT);
    }

    public static ObjectCreationExpr objectCreationEx(String... code ) {
        return of( code ).asObjectCreationExpr();
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
    public static ObjectCreationExpr objectCreationEx(Supplier<? extends Object> lambdaThatCreatesObject ) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr le = lambdaEx(ste);
        return le.getBody().findFirst(ObjectCreationExpr.class).get();        
    }

    public static final Class<StringLiteralExpr> STRING_LITERAL = StringLiteralExpr.class;

    public static StringLiteralExpr stringLiteralEx(String code ){
        String str = code;
        if(! str.startsWith("\"") ){
            str = "\""+str;
        }
        if(! str.endsWith("\"") ){
            str = str+"\"";
        }
        return of( str ).asStringLiteralExpr();
    }
    
    public static StringLiteralExpr stringLiteralEx(String... code ) {
        String str = Text.combine( code );
        if(! str.startsWith("\"") ){
            str = "\""+str;
        }
        if(! str.endsWith("\"") ){
            str = str+"\"";
        }
        return of( str ).asStringLiteralExpr();
    }
        
    /** "super" */
    public static final Class<SuperExpr> SUPER = SuperExpr.class;

    /** 
     * "super" 
     * 
     * @return a super expression
     */
    public static SuperExpr superEx(  ) {
        return new SuperExpr();
    }
    
    public static ThisExpr superEx(String... expr ){
        return (ThisExpr)StaticJavaParser.parseExpression(Text.combine(expr));
    }
    
    public static final Class<ThisExpr> THIS = ThisExpr.class;

    public static ThisExpr thisEx(  ) {
        return new ThisExpr();
    }
    
    public static ThisExpr thisEx(String... expr ){
        return (ThisExpr)StaticJavaParser.parseExpression(Text.combine(expr));
    }

    public static final Class<TypeExpr> TYPE = TypeExpr.class;

    public static TypeExpr typeEx(String... code ) {
        //World::greet
        String str = Text.combine(code);
        str = str + "::method";
        TypeExpr te =  of( str ).asMethodReferenceExpr().getScope().asTypeExpr();
        te.removeForced(); //DISCONNECT
        return te;
    }

    public static final Class<UnaryExpr> UNARY = UnaryExpr.class;

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
    public static UnaryExpr unaryEx(Supplier<? extends Object> lambdaThatCreatesObject ) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr le = lambdaEx(ste);
        return le.getBody().findFirst(UnaryExpr.class).get();        
    }
    
    /**
     * i.e. "!equals(t)" 
     * 
     * @param code the unary operator expression
     * @return 
     */
    public static UnaryExpr unaryEx(String... code ) {
        return of( code ).asUnaryExpr();
    }
    
    /**
     * int i = 1
     */
    public static final Class<VariableDeclarationExpr> VARIABLE_LOCAL_EXPR = VariableDeclarationExpr.class;

    /**
     * i.e. "int i=1"
     * Variable declaration expression
     * (this is a "container" of a {@link com.github.javaparser.ast.body.VariableDeclarator} specifically used
     * for a local variable)
     * @param code
     * @return 
     */
    public static VariableDeclarationExpr varLocalEx(String... code ) {
        return StaticJavaParser.parseVariableDeclarationExpr( Text.combine( code ));
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
    
    public static boolean equivalent( LiteralStringValueExpr left, LiteralStringValueExpr right ){
        //System.out.println( " checking eq "+left+ " "+right); 
        if( left instanceof CharLiteralExpr ){
            return (right instanceof CharLiteralExpr) && Objects.equals(left, right);
        }
        if( left instanceof StringLiteralExpr){
            return (right instanceof StringLiteralExpr) && Objects.equals(left, right);
        }
        return equivalent( parseNumber(left.getValue()), parseNumber( right.getValue() ) );        
    }
    
    /**
     * 
     * @param left
     * @param right
     * @return 
     */
    public static boolean equivalent( Number left, Number right ){
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
    public static boolean equivalent( Expression left, Expression right ){
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
            return equivalent( (LiteralStringValueExpr)left , (LiteralStringValueExpr)right);
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
                if( ! equivalent( lvs.get(i), rvs.get(i))){
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
    public static boolean equivalentAnnos( NodeWithAnnotations left, NodeWithAnnotations right ){
       NodeList<AnnotationExpr> las = left.getAnnotations();
       NodeList<AnnotationExpr> ras = right.getAnnotations();
       
       if( las.size() != ras.size() ){
           return false;
       }
       for(int i=0;i<las.size(); i++){
           AnnotationExpr la = las.get(i);
           if( !ras.stream().filter(a -> equivalent(a, la) ).findFirst().isPresent() ){
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
    public static boolean equivalent( AnnotationExpr left, AnnotationExpr right){
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
                return equivalent( ((SingleMemberAnnotationExpr) left).getMemberValue(), 
                    ((SingleMemberAnnotationExpr) right).getMemberValue());
            }
            NormalAnnotationExpr ra = ((NormalAnnotationExpr)right);            
            if( ra.getPairs().size() == 1 && ra.getPairs().get(0).getNameAsString().equals("value")){
                return equivalent( ((SingleMemberAnnotationExpr) left).getMemberValue(),
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
                return equivalent( ((SingleMemberAnnotationExpr) right).getMemberValue(),
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
            if( !ra.getPairs().stream().filter( p -> p.getNameAsString().equals(name) && equivalent( p.getValue(), ex) ).findFirst().isPresent()){
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
    public static boolean equivalent (Expression exp, Object o) {
        if( exp == null){
            return o == null;
        }
        if( o instanceof Expression ){
            return equivalent( exp, (Expression)exp );
        }
        if( o == null || o instanceof NullLiteralExpr || o.equals("null") ) {
            return exp.equals( new NullLiteralExpr() );
        }
        if( exp instanceof LiteralStringValueExpr && o instanceof LiteralStringValueExpr ){
            return equivalent( (LiteralStringValueExpr) exp, (LiteralStringValueExpr)o);
        }
        else if( o instanceof String ){
            try{
                Expression e = Ex.of( (String)o);
                return equivalent(exp , e );
            }catch(Exception e){
                if( exp instanceof StringLiteralExpr ){
                    return equivalent( exp, Ex.stringLiteralEx(o.toString()) );
                }
            }
        }
        //handle All Wrapper types
        else if( o instanceof Number ||  o instanceof Boolean ){ //Int Float, etc.
            return equivalent( Ex.of(o.toString()), exp );
        }
        else if(o instanceof Character ){
            return Objects.equals( Ex.charLiteralEx( (Character)o), exp );
        }
        //arrays?
        else if( o.getClass().isArray() ){
            if( o.getClass().getComponentType().isPrimitive() ){
                Class ct = o.getClass().getComponentType();
                if( ct == int.class ){
                    return equivalent(exp, Ex.of( (int[])o) );
                }
                if( ct == float.class ){
                    return equivalent(exp, Ex.of( (float[])o) );
                }
                if( ct == double.class ){
                    return equivalent(exp, Ex.of( (double[])o) );
                }
                if( ct == boolean.class ){
                    return equivalent(exp, Ex.of( (boolean[])o) );
                }
                if( ct == char.class ){
                    return equivalent(exp, Ex.of( (char[])o) );
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
    
    
    public static int memberValueHash(MemberValuePair mvp) {
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
            pairs.forEach(p -> memberValueHashes.add(memberValueHash(p)));
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
            memberValueHashes.add( memberValueHash(new MemberValuePair("value", memberValue) ) );
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
    
    public static boolean equivalent(LiteralStringValueExpr ie, Expression e){
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
            return NF.parse(str.replace("_", "").replace("F", "f").replace("D", "d"));
        }catch( Exception e){
            throw new RuntimeException(""+e);
        }
    }
}
