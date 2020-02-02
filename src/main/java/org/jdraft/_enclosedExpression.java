package org.jdraft;

import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *  An expression between ( ).
 *  <br/><code>(1+1)</code>
 */
public class _enclosedExpression implements _expression<EnclosedExpr, _enclosedExpression> {

    public static _enclosedExpression of( ){
        return new _enclosedExpression(new EnclosedExpr( ));
    }
    public static _enclosedExpression of(EnclosedExpr ee ){
        return new _enclosedExpression(ee);
    }
    public static _enclosedExpression of( String...code){
        return new _enclosedExpression(Ex.enclosedEx( code));
    }


    public static <A extends Object> _enclosedExpression of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _enclosedExpression of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _enclosedExpression of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _enclosedExpression of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _enclosedExpression of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _enclosedExpression of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _enclosedExpression of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _enclosedExpression of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _enclosedExpression from( LambdaExpr le){
        Optional<EnclosedExpr> ows = le.getBody().findFirst(EnclosedExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No enclosed expression found in lambda");
    }

    public EnclosedExpr ile;

    public _enclosedExpression(EnclosedExpr ile){
        this.ile = ile;
    }

    @Override
    public _enclosedExpression copy() {
        return new _enclosedExpression(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.enclosedEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(EnclosedExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public EnclosedExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.INNER, ile.getInner());
        return comps;
    }

    public _expression getInner(){
        return _expression.of(this.ile.getInner());
    }

    public boolean equals(Object other){
        if( other instanceof _enclosedExpression){
            return ((_enclosedExpression)other).ile.equals( this.ile );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ile.hashCode();
    }
    
    public String toString(){
        return this.ile.toString();
    }
}
