package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Statement wrapper over and Expression (to make an Expression a Statement)
 *
 */
public final class _exprStmt implements _stmt<ExpressionStmt, _exprStmt>,
        _java._node<ExpressionStmt, _exprStmt>, _java._withExpression<ExpressionStmt, _exprStmt> {

    public static _exprStmt of(){
        return new _exprStmt( new ExpressionStmt( ));
    }

    public static _exprStmt of(_expr _e){
        return of( new ExpressionStmt(_e.ast()) );
    }
    public static _exprStmt of(ExpressionStmt es){
        return new _exprStmt( es);
    }
    public static _exprStmt of(String...code){
        return new _exprStmt(Stmts.exprStmt( code));
    }

    public static <A extends Object> _exprStmt of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _exprStmt of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _exprStmt of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _exprStmt of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _exprStmt of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _exprStmt of(Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _exprStmt of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _exprStmt of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public  static _exprStmt from(StackTraceElement ste ){
        return from( _lambdaExpr.from(ste).ast());
    }

    private static _exprStmt from(LambdaExpr le){
        Optional<ExpressionStmt> ows = le.getBody().findFirst(ExpressionStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No expression statement found in lambda");
    }

    public static _feature._one<_exprStmt, _expr> EXPRESSION = new _feature._one<>(_exprStmt.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_exprStmt a, _expr _e) -> a.setExpression(_e));

    public static _feature._meta<_exprStmt> META = _feature._meta.of(_exprStmt.class, EXPRESSION);

    private ExpressionStmt astStmt;

    public _exprStmt(ExpressionStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _exprStmt copy() {
        return new _exprStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmts.exprStmt(stringRep));
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
        if( other instanceof _exprStmt){
            return Objects.equals( ((_exprStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
