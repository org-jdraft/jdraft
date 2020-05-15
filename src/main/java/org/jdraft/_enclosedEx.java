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
public final class _enclosedEx implements _expr<EnclosedExpr, _enclosedEx>,
        _java._uniPart<EnclosedExpr, _enclosedEx>,
        _java._withExpression<EnclosedExpr, _enclosedEx>{

    public static _enclosedEx of( ){
        return new _enclosedEx(new EnclosedExpr( ));
    }
    public static _enclosedEx of(EnclosedExpr ee ){
        return new _enclosedEx(ee);
    }
    public static _enclosedEx of(String...code){
        return new _enclosedEx(Exprs.enclosedEx( code));
    }


    public static <A extends Object> _enclosedEx of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _enclosedEx of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _enclosedEx of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _enclosedEx of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _enclosedEx of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _enclosedEx of(Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _enclosedEx of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _enclosedEx of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _enclosedEx from(LambdaExpr le){
        Optional<EnclosedExpr> ows = le.getBody().findFirst(EnclosedExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No enclosed expression found in lambda");
    }

    public EnclosedExpr ee;

    public _enclosedEx(EnclosedExpr ee){
        this.ee = ee;
    }

    @Override
    public _enclosedEx copy() {
        return new _enclosedEx(this.ee.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.enclosedEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(EnclosedExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    //I had to override these because its called "inner" not expression
    public _enclosedEx setExpression(String...ex){
        this.ee.setInner(Exprs.of(ex));
        return this;
    }

    public _enclosedEx setExpression(_expr _e){
        this.ee.setInner(_e.ast());
        return this;
    }

    public _enclosedEx setExpression(Expression e){
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
        if( other instanceof _enclosedEx){
            return ((_enclosedEx)other).ee.equals( this.ee);
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
