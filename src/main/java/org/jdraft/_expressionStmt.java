package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


public final class _expressionStmt implements _statement<ExpressionStmt, _expressionStmt>,
        _java._uniPart<ExpressionStmt, _expressionStmt>, _java._withExpression<ExpressionStmt, _expressionStmt> {

    public static _expressionStmt of(){
        return new _expressionStmt( new ExpressionStmt( ));
    }

    public static _expressionStmt of( _expression _e){
        return of( new ExpressionStmt(_e.ast()) );
    }
    public static _expressionStmt of(ExpressionStmt es){
        return new _expressionStmt( es);
    }
    public static _expressionStmt of(String...code){
        return new _expressionStmt(Statements.expressionStmt( code));
    }


    public static <A extends Object> _expressionStmt of(Expressions.Command c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _expressionStmt of(Consumer<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _expressionStmt of(BiConsumer<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _expressionStmt of( Expressions.TriConsumer<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _expressionStmt of( Expressions.QuadConsumer<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _expressionStmt of( Function<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _expressionStmt of( BiFunction<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _expressionStmt of( Expressions.TriFunction<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public  static _expressionStmt from( StackTraceElement ste ){
        return from( _lambda.from(ste).ast());
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
            return is( Statements.expressionStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ExpressionStmt ast(){
        return astStmt;
    }

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
