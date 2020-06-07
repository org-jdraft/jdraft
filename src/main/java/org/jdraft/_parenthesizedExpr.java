package org.jdraft;

import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *  An expression between ( ).
 *  <br/><code>(1+1)</code>
 */
public final class _parenthesizedExpr implements _expr<EnclosedExpr, _parenthesizedExpr>,
        _java._node<EnclosedExpr, _parenthesizedExpr>,
        _java._withExpression<EnclosedExpr, _parenthesizedExpr>{

    public static final Function<String, _parenthesizedExpr> PARSER = s-> _parenthesizedExpr.of(s);

    public static _parenthesizedExpr of( ){
        return new _parenthesizedExpr(new EnclosedExpr( ));
    }
    public static _parenthesizedExpr of(EnclosedExpr ee ){
        return new _parenthesizedExpr(ee);
    }
    public static _parenthesizedExpr of(String...code){
        return new _parenthesizedExpr(Expr.parenthesizedExpr( code));
    }

    public static <A extends Object> _parenthesizedExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _parenthesizedExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _parenthesizedExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _parenthesizedExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _parenthesizedExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _parenthesizedExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _parenthesizedExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _parenthesizedExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _parenthesizedExpr from(LambdaExpr le){
        Optional<EnclosedExpr> ows = le.getBody().findFirst(EnclosedExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No enclosed expression found in lambda");
    }

    public static _feature._one<_parenthesizedExpr, _expr> EXPRESSION = new _feature._one<>(_parenthesizedExpr.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_parenthesizedExpr p, _expr _e) -> p.setExpression(_e), PARSER);

    public static _feature._features<_parenthesizedExpr> FEATURES = _feature._features.of(_parenthesizedExpr.class, EXPRESSION);

    public EnclosedExpr ee;

    public _parenthesizedExpr(EnclosedExpr ee){
        this.ee = ee;
    }

    public _feature._features<_parenthesizedExpr> features(){
        return FEATURES;
    }

    @Override
    public _parenthesizedExpr copy() {
        return new _parenthesizedExpr(this.ee.clone());
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.parenthesizedExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }
     */

    @Override
    public boolean is(EnclosedExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    //I had to override these because its called "inner" not expression
    public _parenthesizedExpr setExpression(String...ex){
        this.ee.setInner(Expr.of(ex));
        return this;
    }

    public _parenthesizedExpr setExpression(_expr _e){
        this.ee.setInner(_e.ast());
        return this;
    }

    public _parenthesizedExpr setExpression(Expression e){
        this.ee.setInner(e);
        return this;
    }

    public _expr getExpression(){
        return _expr.of(this.ee.getInner());
    }

    public EnclosedExpr ast(){
        return ee;
    }

    public _expr getInner(){
        return _expr.of(this.ee.getInner());
    }

    public boolean equals(Object other){
        if( other instanceof _parenthesizedExpr){
            return ((_parenthesizedExpr)other).ee.equals( this.ee);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ee.hashCode();
    }
    
    public String toString(){
        return this.ee.toString();
    }
}
