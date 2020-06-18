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
 * i.e.
 * <PRE>
 * "System.out.println(1);" is an {@link _exprStmt} for the
 * "System.out.println(1)" {@link _methodCallExpr}
 * </PRE>
 */
public final class _exprStmt implements _stmt<ExpressionStmt, _exprStmt>,
        _java._withExpression<ExpressionStmt, _exprStmt> {

    public static final Function<String, _exprStmt> PARSER = s-> _exprStmt.of(s);

    public static _exprStmt of(){
        return new _exprStmt( new ExpressionStmt( ));
    }

    public static _exprStmt of(_expr _e){
        return of( new ExpressionStmt(_e.node()) );
    }
    public static _exprStmt of(ExpressionStmt es){
        return new _exprStmt( es);
    }
    public static _exprStmt of(String...code){
        return new _exprStmt(Stmt.expressionStmt( code));
    }

    public static <A extends Object> _exprStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _exprStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _exprStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _exprStmt of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _exprStmt of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _exprStmt of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _exprStmt of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _exprStmt of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public  static _exprStmt from(StackTraceElement ste ){
        return from( _lambdaExpr.from(ste).node());
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
            (_exprStmt a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._features<_exprStmt> FEATURES = _feature._features.of(_exprStmt.class,  PARSER, EXPRESSION);

    private ExpressionStmt node;

    public _exprStmt(ExpressionStmt rs){
        this.node = rs;
    }

    public _feature._features<_exprStmt> features(){
        return FEATURES;
    }

    @Override
    public _exprStmt copy() {
        return new _exprStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _exprStmt replace(ExpressionStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public ExpressionStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _exprStmt){
            _exprStmt _o = (_exprStmt)other;
            return Objects.equals( _o.getExpression(), getExpression() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
