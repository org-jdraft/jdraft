package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.ThrowStmt;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _throwStmt implements _stmt._controlFlow._returns<ThrowStmt, _throwStmt>,
        _java._node<ThrowStmt, _throwStmt>, _java._withExpression<ThrowStmt, _throwStmt> {

    public static _throwStmt of(){
        return new _throwStmt( new ThrowStmt( ));
    }
    public static _throwStmt of(ThrowStmt ts){
        return new _throwStmt(ts);
    }
    public static _throwStmt of(String...code){
        return new _throwStmt(Stmts.throwStmt( code));
    }

    public static <A extends Object> _throwStmt of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _throwStmt of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _throwStmt of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _throwStmt of( Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _throwStmt of( Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _throwStmt of( Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _throwStmt of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _throwStmt of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _throwStmt from( LambdaExpr le){
        Optional<ThrowStmt> ows = le.getBody().findFirst(ThrowStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No throw statement found in lambda");
    }

    public static _feature._one<_throwStmt, _expr> EXPRESSION = new _feature._one<>(_throwStmt.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_throwStmt p, _expr _e) -> p.setExpression(_e));

    public static _feature._meta<_throwStmt> META = _feature._meta.of(_throwStmt.class, EXPRESSION );

    private ThrowStmt astStmt;

    public _throwStmt(ThrowStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _throwStmt copy() {
        return new _throwStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmts.throwStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ThrowStmt ast(){
        return astStmt;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _throwStmt ){
            return Objects.equals( ((_throwStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
