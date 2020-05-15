package org.jdraft;

import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The ternary conditional expression.
 * In <code>b==0?x:y</code>, b==0 is the condition, x is thenExpr, and y is elseExpr.
 */
public final class _conditionalExpr implements _expr<ConditionalExpr,
        _conditionalExpr>, _java._multiPart<ConditionalExpr, _conditionalExpr> {

    public static _conditionalExpr of(){
        return new _conditionalExpr( new ConditionalExpr( ));
    }
    public static _conditionalExpr of(ConditionalExpr ce){
        return new _conditionalExpr(ce);
    }
    public static _conditionalExpr of(String...code){
        return new _conditionalExpr(Exprs.conditionalEx( code));
    }

    public static <A extends Object> _conditionalExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _conditionalExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _conditionalExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _conditionalExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _conditionalExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _conditionalExpr of(Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _conditionalExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _conditionalExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _conditionalExpr from(LambdaExpr le){
        Optional<ConditionalExpr> ows = le.getBody().findFirst(ConditionalExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No ternary expression found in lambda");
    }
    
    public ConditionalExpr ce;

    public _conditionalExpr(ConditionalExpr ce){
        this.ce = ce;
    }

    @Override
    public _conditionalExpr copy() {
        return new _conditionalExpr(this.ce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.conditionalEx(stringRep));
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

    public boolean isCondition( String...expr){
        return isCondition( Exprs.of(expr));
    }

    public boolean isCondition( _expr _e){
        return isCondition(_e.ast());
    }

    public boolean isCondition( Expression e){
        return Objects.equals( this.ce.getCondition(), e);
    }

    public _conditionalExpr setCondition(String...expr){
        this.ce.setCondition(Exprs.of(expr));
        return this;
    }

    public _conditionalExpr setCondition(_expr _e){
        this.ce.setCondition(_e.ast());
        return this;
    }

    public _conditionalExpr setCondition(Expression e){
        this.ce.setCondition( e );
        return this;
    }

    public boolean isThen( String...expr){
        return isThen( Exprs.of(expr));
    }

    public boolean isThen( _expr _e){
        return isThen(_e.ast());
    }

    public boolean isThen( Expression e){
        return Objects.equals( this.ce.getCondition(), e);
    }

    public _conditionalExpr setThen(String...expr){
        this.ce.setThenExpr(Exprs.of(expr));
        return this;
    }

    public _conditionalExpr setThen(_expr _e){
        this.ce.setThenExpr(_e.ast());
        return this;
    }

    public _conditionalExpr setThen(Expression e){
        this.ce.setThenExpr( e );
        return this;
    }

    public _conditionalExpr setElse(_expr _e){
        this.ce.setElseExpr(_e.ast());
        return this;
    }

    public boolean isElse( String...expr){
        return isElse( Exprs.of(expr));
    }

    public boolean isElse( _expr _e){
        return isElse(_e.ast());
    }

    public boolean isElse( Expression e){
        return Objects.equals( this.ce.getCondition(), e);
    }

    public _conditionalExpr setElse(Expression e){
        this.ce.setElseExpr( e );
        return this;
    }

    public _conditionalExpr setElse(String...expr){
        this.ce.setElseExpr(Exprs.of(expr));
        return this;
    }

    public _expr getCondition(){
        return _expr.of(this.ce.getCondition());
    }

    public _expr getThen(){
        return _expr.of(this.ce.getThenExpr());
    }

    public _expr getElse(){
        return _expr.of(this.ce.getElseExpr());
    }

    public boolean equals(Object other){
        if( other instanceof _conditionalExpr){
            return ((_conditionalExpr)other).ce.equals( this.ce);
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
