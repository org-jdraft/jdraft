package org.jdraft;

import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _instanceOf implements _expression<InstanceOfExpr, _instanceOf>, _java._compoundNode<InstanceOfExpr, _instanceOf> {

    public static _instanceOf of(){
        return new _instanceOf(new InstanceOfExpr( ));
    }
    public static _instanceOf of( InstanceOfExpr ie){
        return new _instanceOf(ie);
    }
    public static _instanceOf of( String...code){
        return new _instanceOf(Ex.instanceOfEx( code));
    }


    public static <A extends Object> _instanceOf of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _instanceOf of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _instanceOf of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _instanceOf of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _instanceOf of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _instanceOf of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _instanceOf of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _instanceOf of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _instanceOf from( LambdaExpr le){
        Optional<InstanceOfExpr> ows = le.getBody().findFirst(InstanceOfExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No instance of expression found in lambda");
    }

    public InstanceOfExpr ioe;

    public _instanceOf(InstanceOfExpr ioe){
        this.ioe = ioe;
    }

    @Override
    public _instanceOf copy() {
        return new _instanceOf(this.ioe.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.instanceOfEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(InstanceOfExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public InstanceOfExpr ast(){
        return ioe;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TYPE, ioe.getType());
        comps.put(_java.Component.EXPRESSION, ioe.getExpression());
        return comps;
    }

    public _expression getExpression(){
        return _expression.of(this.ioe.getExpression());
    }

    public _typeRef getType(){
        return _typeRef.of(this.ioe.getType());
    }

    public boolean equals(Object other){
        if( other instanceof _instanceOf){
            return ((_instanceOf)other).ioe.equals( this.ioe);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ioe.hashCode();
    }
    
    public String toString(){
        return this.ioe.toString();
    }
}
