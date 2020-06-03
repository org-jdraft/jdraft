package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.*;
import java.util.function.*;

public final class _methodCallExpr implements _expr<MethodCallExpr, _methodCallExpr>,
        _java._node<MethodCallExpr, _methodCallExpr>,
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

    public static _feature._features<_methodCallExpr> FEATURES = _feature._features.of(_methodCallExpr.class, SCOPE, TYPE_ARGS, NAME, ARGS );

    public MethodCallExpr mce;

    public _methodCallExpr(MethodCallExpr mce){
        this.mce = mce;
    }

    @Override
    public _methodCallExpr copy() {
        return new _methodCallExpr(this.mce.clone());
    }


    public _methodCallExpr setName(String methodName){
        this.mce.setName(methodName);
        return this;
    }

    public MethodCallExpr ast(){
        return mce;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        if( mce.getScope().isPresent() ) {
            comps.put(_java.Feature.SCOPE_EXPR, mce.getScope().get());
        }
        comps.put(_java.Feature.NAME, mce.getNameAsString());
        if( mce.getTypeArguments().isPresent()) {
            comps.put(_java.Feature.TYPE_ARGS, mce.getTypeArguments().get());
        }
        comps.put(_java.Feature.ARGS_EXPRS, mce.getArguments());
        return comps;
    }
     */

    public SimpleName getNameNode() { return this.mce.getName(); }

    public String getName(){
        return this.mce.getNameAsString();
    }

    public boolean equals(Object other){
        if( other instanceof _methodCallExpr){
            return ((_methodCallExpr)other).mce.equals( this.mce);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.mce.hashCode();
    }
    
    public String toString(){
        return this.mce.toString();
    }
}
