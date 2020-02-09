package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


public class _expressionStmt implements _statement<ExpressionStmt, _expressionStmt>,
        _java._simple<ExpressionStmt, _expressionStmt> {

    public static _expressionStmt of(){
        return new _expressionStmt( new ExpressionStmt( ));
    }
    public static _expressionStmt of(ExpressionStmt es){
        return new _expressionStmt( es);
    }
    public static _expressionStmt of(String...code){
        return new _expressionStmt(Stmt.expressionStmt( code));
    }


    public static <A extends Object> _expressionStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _expressionStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _expressionStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _expressionStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _expressionStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _expressionStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _expressionStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _expressionStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _expressionStmt from( LambdaExpr le){
        Optional<ExpressionStmt> ows = le.getBody().findFirst(ExpressionStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No expression statement found in lambda");
    }


    private ExpressionStmt astStmt;



    public _expressionStmt(ExpressionStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _expressionStmt copy() {
        return new _expressionStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.expressionStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getExpression(){
        return _expression.of( this.astStmt.getExpression());
    }

    public _expressionStmt setExpression(_expression _e){
        this.astStmt.setExpression(_e.ast());
        return this;
    }
    @Override
    public boolean is(ExpressionStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ExpressionStmt ast(){
        return astStmt;
    }

    /*
    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put( _java.Component.EXPRESSION, astStmt.getExpression());
        return comps;
    }
     */

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _expressionStmt ){
            return Objects.equals( ((_expressionStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
