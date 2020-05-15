package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.*;
import java.util.function.*;

public final class _methodCallExpr implements _expr<MethodCallExpr, _methodCallExpr>,
        _java._multiPart<MethodCallExpr, _methodCallExpr>,
        _java._withName<_methodCallExpr>,
        _java._withScope<MethodCallExpr, _methodCallExpr>,
        _arguments._withArguments<MethodCallExpr, _methodCallExpr>,
        _typeArguments._withTypeArguments<MethodCallExpr, _methodCallExpr> {

    public static _methodCallExpr of(){
        return new _methodCallExpr( new MethodCallExpr( ));
    }
    public static _methodCallExpr of(MethodCallExpr mce){
        return new _methodCallExpr(mce);
    }
    public static _methodCallExpr of(String...code){
        return new _methodCallExpr(Exprs.methodCallEx( code));
    }

    public static <A extends Object> _methodCallExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _methodCallExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _methodCallExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _methodCallExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _methodCallExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _methodCallExpr of(Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _methodCallExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _methodCallExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static _methodCallExpr from (StackTraceElement ste ){
        return from(Exprs.lambdaEx( ste ));
    }

    public static _methodCallExpr from(LambdaExpr le){
        Optional<MethodCallExpr> ows = le.getBody().findFirst(MethodCallExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No method call expression found in lambda");
    }

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

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if( mce.getScope().isPresent() ) {
            comps.put(_java.Component.SCOPE, mce.getScope().get());
        }
        comps.put(_java.Component.NAME, mce.getNameAsString());
        if( mce.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, mce.getTypeArguments().get());
        }
        comps.put(_java.Component.ARGUMENTS, mce.getArguments());
        return comps;
    }

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
