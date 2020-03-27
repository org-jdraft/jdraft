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
public class _enclosedExpression implements _expression<EnclosedExpr, _enclosedExpression>,
        _java._uniPart<EnclosedExpr, _enclosedExpression>,
        _java._withExpression<EnclosedExpr, _enclosedExpression>{

    public static _enclosedExpression of( ){
        return new _enclosedExpression(new EnclosedExpr( ));
    }
    public static _enclosedExpression of(EnclosedExpr ee ){
        return new _enclosedExpression(ee);
    }
    public static _enclosedExpression of( String...code){
        return new _enclosedExpression(Expressions.enclosedEx( code));
    }


    public static <A extends Object> _enclosedExpression of(Expressions.Command c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _enclosedExpression of(Consumer<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _enclosedExpression of(BiConsumer<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _enclosedExpression of( Expressions.TriConsumer<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _enclosedExpression of( Expressions.QuadConsumer<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _enclosedExpression of( Function<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _enclosedExpression of( BiFunction<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _enclosedExpression of( Expressions.TriFunction<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _enclosedExpression from( LambdaExpr le){
        Optional<EnclosedExpr> ows = le.getBody().findFirst(EnclosedExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No enclosed expression found in lambda");
    }

    public EnclosedExpr ee;

    public _enclosedExpression(EnclosedExpr ee){
        this.ee = ee;
    }

    @Override
    public _enclosedExpression copy() {
        return new _enclosedExpression(this.ee.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expressions.enclosedEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(EnclosedExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    //I had to override these because its called "inner" not expression
    public _enclosedExpression setExpression(String...ex){
        this.ee.setInner(Expressions.of(ex));
        return this;
    }

    public _enclosedExpression setExpression( _expression _e){
        this.ee.setInner(_e.ast());
        return this;
    }

    public _enclosedExpression setExpression( Expression e){
        this.ee.setInner(e);
        return this;
    }

    public _expression getExpression(){
        return _expression.of(this.ee.getInner());
    }

    public EnclosedExpr ast(){
        return ee;
    }

    public _expression getInner(){
        return _expression.of(this.ee.getInner());
    }

    public boolean equals(Object other){
        if( other instanceof _enclosedExpression){
            return ((_enclosedExpression)other).ee.equals( this.ee);
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
