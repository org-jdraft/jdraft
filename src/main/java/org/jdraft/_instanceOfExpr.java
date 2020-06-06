package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.type.ReferenceType;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _instanceOfExpr implements _expr<InstanceOfExpr, _instanceOfExpr>,
        _java._node<InstanceOfExpr, _instanceOfExpr>,
        _typeRef._withType<InstanceOfExpr, _instanceOfExpr>,
        _java._withExpression<InstanceOfExpr, _instanceOfExpr>{

    public static final Function<String, _instanceOfExpr> PARSER = s-> _instanceOfExpr.of(s);

    public static _instanceOfExpr of(){
        return new _instanceOfExpr(new InstanceOfExpr( ));
    }
    public static _instanceOfExpr of(InstanceOfExpr ie){
        return new _instanceOfExpr(ie);
    }
    public static _instanceOfExpr of(String...code){
        return new _instanceOfExpr(Expr.instanceOfExpr( code));
    }


    public static <A extends Object> _instanceOfExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _instanceOfExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _instanceOfExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _instanceOfExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _instanceOfExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _instanceOfExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _instanceOfExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _instanceOfExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _instanceOfExpr from(LambdaExpr le){
        Optional<InstanceOfExpr> ows = le.getBody().findFirst(InstanceOfExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No instance of expression found in lambda");
    }

    public static _feature._one<_instanceOfExpr, _typeRef> TYPE = new _feature._one<>(_instanceOfExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_instanceOfExpr a, _typeRef _t) -> a.setType(_t), PARSER);

    public static _feature._one<_instanceOfExpr, _expr> EXPRESSION = new _feature._one<>(_instanceOfExpr.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_instanceOfExpr a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._features<_instanceOfExpr> FEATURES = _feature._features.of(_instanceOfExpr.class, TYPE, EXPRESSION );

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
            return is( Expr.instanceOfExpr(stringRep));
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

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.TYPE, ioe.getType());
        comps.put(_java.Feature.EXPRESSION, ioe.getExpression());
        return comps;
    }
     */

    public _expr getExpression(){
        return _expr.of(this.ioe.getExpression());
    }

    public _typeRef getType(){
        return _typeRef.of(this.ioe.getType());
    }

    public _instanceOfExpr setType(Class clazz){
        this.ioe.setType(clazz);
        return this;
    }

    public _instanceOfExpr setType(String type){
        this.ioe.setType(type);
        return this;
    }

    public _instanceOfExpr setTypeRef(ReferenceType t){
        this.ioe.setType(t);
        return this;
    }

    public _instanceOfExpr setType(_typeRef _t){
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
        this.ioe.setExpression(Expr.of(expr) );
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
