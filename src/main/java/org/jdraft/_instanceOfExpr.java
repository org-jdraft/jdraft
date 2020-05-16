package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.type.ReferenceType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _instanceOfExpr implements _expr<InstanceOfExpr, _instanceOfExpr>,
        _java._multiPart<InstanceOfExpr, _instanceOfExpr>,
        _typeRef._withTypeRef<InstanceOfExpr, _instanceOfExpr>,
        _java._withExpression<InstanceOfExpr, _instanceOfExpr>{

    public static _instanceOfExpr of(){
        return new _instanceOfExpr(new InstanceOfExpr( ));
    }
    public static _instanceOfExpr of(InstanceOfExpr ie){
        return new _instanceOfExpr(ie);
    }
    public static _instanceOfExpr of(String...code){
        return new _instanceOfExpr(Exprs.instanceOfEx( code));
    }


    public static <A extends Object> _instanceOfExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _instanceOfExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _instanceOfExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _instanceOfExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _instanceOfExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _instanceOfExpr of(Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _instanceOfExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _instanceOfExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _instanceOfExpr from(LambdaExpr le){
        Optional<InstanceOfExpr> ows = le.getBody().findFirst(InstanceOfExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No instance of expression found in lambda");
    }

    public InstanceOfExpr ioe;

    public _instanceOfExpr(InstanceOfExpr ioe){
        this.ioe = ioe;
    }

    @Override
    public _instanceOfExpr copy() {
        return new _instanceOfExpr(this.ioe.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.instanceOfEx(stringRep));
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

    public _expr getExpression(){
        return _expr.of(this.ioe.getExpression());
    }

    public _typeRef getTypeRef(){
        return _typeRef.of(this.ioe.getType());
    }

    public _instanceOfExpr setTypeRef(Class clazz){
        this.ioe.setType(clazz);
        return this;
    }

    public _instanceOfExpr setTypeRef(String type){
        this.ioe.setType(type);
        return this;
    }

    public _instanceOfExpr setTypeRef(ReferenceType t){
        this.ioe.setType(t);
        return this;
    }

    public _instanceOfExpr setTypeRef(_typeRef _t){
        this.ioe.setType((ReferenceType)_t.ast());
        return this;
    }

    public _instanceOfExpr setExpression(_expr _e){
        this.ioe.setExpression(_e.ast());
        return this;
    }

    public _instanceOfExpr setExpression(Expression e){
        this.ioe.setExpression(e );
        return this;
    }

    public _instanceOfExpr setExpression(String...expr){
        this.ioe.setExpression(Exprs.of(expr) );
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _instanceOfExpr){
            return ((_instanceOfExpr)other).ioe.equals( this.ioe);
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