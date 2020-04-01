package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.*;
import java.util.function.*;

public final class _methodCall implements _expression<MethodCallExpr, _methodCall>,
        _java._multiPart<MethodCallExpr, _methodCall>,
        _java._withName<_methodCall>,
        _java._withScope<MethodCallExpr, _methodCall>,
        _arguments._withArguments<MethodCallExpr, _methodCall>,
        _typeArguments._withTypeArguments<MethodCallExpr, _methodCall> {

    public static _methodCall of(){
        return new _methodCall( new MethodCallExpr( ));
    }
    public static _methodCall of(MethodCallExpr mce){
        return new _methodCall(mce);
    }
    public static _methodCall of( String...code){
        return new _methodCall(Expressions.methodCallEx( code));
    }

    public static <A extends Object> _methodCall of(Expressions.Command c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _methodCall of(Consumer<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _methodCall of(BiConsumer<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _methodCall of( Expressions.TriConsumer<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _methodCall of( Expressions.QuadConsumer<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _methodCall of( Function<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _methodCall of( BiFunction<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _methodCall of( Expressions.TriFunction<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _methodCall from( LambdaExpr le){
        Optional<MethodCallExpr> ows = le.getBody().findFirst(MethodCallExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No method call expression found in lambda");
    }

    public MethodCallExpr mce;

    public _methodCall(MethodCallExpr mce){
        this.mce = mce;
    }

    @Override
    public _methodCall copy() {
        return new _methodCall(this.mce.clone());
    }


    public _methodCall setName(String methodName){
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
        if( other instanceof _methodCall){
            return ((_methodCall)other).mce.equals( this.mce);
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
