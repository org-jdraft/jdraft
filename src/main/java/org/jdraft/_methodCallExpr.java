package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.*;
import java.util.function.*;

public final class _methodCallExpr implements _expr<MethodCallExpr, _methodCallExpr>,
        _tree._node<MethodCallExpr, _methodCallExpr>,
        _java._withName<_methodCallExpr>,
        _java._withScope<MethodCallExpr, _methodCallExpr>,
        _args._withArgs<MethodCallExpr, _methodCallExpr>,
        _typeArgs._withTypeArgs<MethodCallExpr, _methodCallExpr> {

    public static final Function<String, _methodCallExpr> PARSER = s-> _methodCallExpr.of(s);

    public static _methodCallExpr of(){
        return new _methodCallExpr( new MethodCallExpr( ));
    }
    public static _methodCallExpr of(MethodCallExpr mce){
        return new _methodCallExpr(mce);
    }
    public static _methodCallExpr of(String...code){
        return new _methodCallExpr(Expr.methodCallExpr( code));
    }

    public static <A extends Object> _methodCallExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _methodCallExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _methodCallExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _methodCallExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _methodCallExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _methodCallExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _methodCallExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _methodCallExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static _methodCallExpr from (StackTraceElement ste ){
        return from(Expr.lambdaExpr( ste ));
    }

    public static _methodCallExpr from(LambdaExpr le){
        Optional<MethodCallExpr> ows = le.getBody().findFirst(MethodCallExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No method call expression found in lambda");
    }

    public static _feature._one<_methodCallExpr, _expr> SCOPE = new _feature._one<>(_methodCallExpr.class, _expr.class,
            _feature._id.SCOPE,
            a -> a.getScope(),
            (_methodCallExpr a, _expr _e) -> a.setScope(_e), PARSER);

    public static _feature._one<_methodCallExpr, _typeArgs> TYPE_ARGS = new _feature._one<>(_methodCallExpr.class, _typeArgs.class,
            _feature._id.TYPE_ARGS,
            a -> a.getTypeArgs(),
            (_methodCallExpr a, _typeArgs _ta) -> a.setTypeArgs(_ta), PARSER);

    public static _feature._one<_methodCallExpr, String> NAME = new _feature._one<>(_methodCallExpr.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_methodCallExpr a, String s) -> a.setName(s), PARSER);

    public static _feature._one<_methodCallExpr, _args> ARGS = new _feature._one<>(_methodCallExpr.class, _args.class,
            _feature._id.ARGS,
            a -> a.getArgs(),
            (_methodCallExpr a, _args _a) -> a.setArgs(_a), PARSER);

    public static _feature._features<_methodCallExpr> FEATURES = _feature._features.of(_methodCallExpr.class, PARSER, SCOPE, TYPE_ARGS, NAME, ARGS );

    public MethodCallExpr node;

    public _methodCallExpr(MethodCallExpr node){
        this.node = node;
    }

    public _feature._features<_methodCallExpr> features(){
        return FEATURES;
    }

    @Override
    public _methodCallExpr copy() {
        return new _methodCallExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _methodCallExpr replace(MethodCallExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public _methodCallExpr setName(String methodName){
        this.node.setName(methodName);
        return this;
    }

    /**
     * checks the name and scope matches the string.
     * <PRE>
     *     _methodCallExpr _mce = _methodCallExpr.of("System.out.println(1)");
     *     assertTrue(_mce.isScopedName("System.out.println")); //the scope "System.out" and name "println" are combined
     * </PRE>
     * <PRE>
     *     _methodCallExpr _mce = _methodCallExpr.of("myObject.call(1)");
     *     assertTrue(_mce.isScopedName("myObject.call")); //the scope "myObject" and name "call" are combined
     * </PRE>
     * @param scopedName
     * @return
     */
    public boolean isScopedName( String scopedName){
        try {
            _methodCallExpr _mce = of(scopedName + "()");
            return Objects.equals( _mce.getScope(), getScope()) && Objects.equals( _mce.getName(), getName() );
        }catch(Exception e){
            return false;
        }
    }

    public MethodCallExpr node(){
        return node;
    }

    public SimpleName getNameNode() { return this.node.getName(); }

    public String getName(){
        return this.node.getNameAsString();
    }

    public boolean equals(Object other){
        if( other instanceof _methodCallExpr){
            return ((_methodCallExpr)other).node.equals( this.node);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }
    
    public String toString(){
        return this.node.toString();
    }
}
