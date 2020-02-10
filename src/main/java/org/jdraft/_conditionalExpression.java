package org.jdraft;

import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The ternary conditional expression.
 * In <code>b==0?x:y</code>, b==0 is the condition, x is thenExpr, and y is elseExpr.
 */
public class _conditionalExpression implements _expression<ConditionalExpr, _conditionalExpression>, _java._compound<ConditionalExpr, _conditionalExpression> {

    public static _conditionalExpression of(){
        return new _conditionalExpression( new ConditionalExpr( ));
    }
    public static _conditionalExpression of(ConditionalExpr ce){
        return new _conditionalExpression(ce);
    }
    public static _conditionalExpression of(String...code){
        return new _conditionalExpression(Ex.conditionalEx( code));
    }


    public static <A extends Object> _conditionalExpression of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _conditionalExpression of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _conditionalExpression of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _conditionalExpression of(Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _conditionalExpression of(Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _conditionalExpression of(Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _conditionalExpression of(BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _conditionalExpression of(Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _conditionalExpression from(LambdaExpr le){
        Optional<ConditionalExpr> ows = le.getBody().findFirst(ConditionalExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No ternary expression found in lambda");
    }
    
    public ConditionalExpr ce;

    public _conditionalExpression(ConditionalExpr ce){
        this.ce = ce;
    }

    @Override
    public _conditionalExpression copy() {
        return new _conditionalExpression(this.ce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.conditionalEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ConditionalExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ConditionalExpr ast(){
        return ce;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.CONDITION, ce.getCondition());
        comps.put(_java.Component.THEN, ce.getThenExpr());
        comps.put(_java.Component.ELSE, ce.getElseExpr());
        return comps;
    }

    public _expression getCondition(){
        return _expression.of(this.ce.getCondition());
    }

    public _expression getThen(){
        return _expression.of(this.ce.getThenExpr());
    }

    public _expression getElse(){
        return _expression.of(this.ce.getElseExpr());
    }

    public boolean equals(Object other){
        if( other instanceof _conditionalExpression){
            return ((_conditionalExpression)other).ce.equals( this.ce);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ce.hashCode();
    }
    
    public String toString(){
        return this.ce.toString();
    }
}
