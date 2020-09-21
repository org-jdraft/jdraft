package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.ThrowStmt;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _throwStmt implements
        _stmt._returns<ThrowStmt, _throwStmt>,
        _java._withExpression<ThrowStmt, _throwStmt> {

    public static final Function<String, _throwStmt> PARSER = s-> _throwStmt.of(s);

    public static _throwStmt of(){
        return new _throwStmt( new ThrowStmt( ));
    }
    public static _throwStmt of(ThrowStmt ts){
        return new _throwStmt(ts);
    }
    public static _throwStmt of(String...code){
        return new _throwStmt(Stmt.throwStmt( code));
    }

    public static <A extends Object> _throwStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _throwStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _throwStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _throwStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _throwStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _throwStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _throwStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _throwStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
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
            (_throwStmt p, _expr _e) -> p.setExpression(_e), PARSER);

    public static _feature._features<_throwStmt> FEATURES = _feature._features.of(_throwStmt.class,  PARSER, EXPRESSION );

    private ThrowStmt node;

    public _throwStmt(ThrowStmt rs){
        this.node = rs;
    }

    public _feature._features<_throwStmt> features(){
        return FEATURES;
    }

    @Override
    public _throwStmt copy() {
        return new _throwStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _throwStmt replace(ThrowStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public ThrowStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _throwStmt ){
            return Objects.equals( ((_throwStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
