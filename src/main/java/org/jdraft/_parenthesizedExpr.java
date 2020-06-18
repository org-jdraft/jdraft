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
public final class _parenthesizedExpr implements _expr<EnclosedExpr, _parenthesizedExpr>,
        _tree._node<EnclosedExpr, _parenthesizedExpr>,
        _java._withExpression<EnclosedExpr, _parenthesizedExpr>{

    public static final Function<String, _parenthesizedExpr> PARSER = s-> _parenthesizedExpr.of(s);

    public static _parenthesizedExpr of( ){
        return new _parenthesizedExpr(new EnclosedExpr( ));
    }
    public static _parenthesizedExpr of(EnclosedExpr ee ){
        return new _parenthesizedExpr(ee);
    }
    public static _parenthesizedExpr of(String...code){
        return new _parenthesizedExpr(Expr.parenthesizedExpr( code));
    }

    public static <A extends Object> _parenthesizedExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _parenthesizedExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _parenthesizedExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _parenthesizedExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _parenthesizedExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _parenthesizedExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _parenthesizedExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _parenthesizedExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _parenthesizedExpr from(LambdaExpr le){
        Optional<EnclosedExpr> ows = le.getBody().findFirst(EnclosedExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No enclosed expression found in lambda");
    }

    public static _feature._one<_parenthesizedExpr, _expr> EXPRESSION = new _feature._one<>(_parenthesizedExpr.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_parenthesizedExpr p, _expr _e) -> p.setExpression(_e), PARSER);

    public static _feature._features<_parenthesizedExpr> FEATURES = _feature._features.of(_parenthesizedExpr.class, PARSER,  EXPRESSION);

    public EnclosedExpr node;

    public _parenthesizedExpr(EnclosedExpr node){
        this.node = node;
    }

    public _feature._features<_parenthesizedExpr> features(){
        return FEATURES;
    }

    @Override
    public _parenthesizedExpr copy() {
        return new _parenthesizedExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _parenthesizedExpr replace(EnclosedExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(EnclosedExpr astNode) {
        return this.node( ).equals(astNode);
    }

    //I had to override these because its called "inner" not expression
    public _parenthesizedExpr setExpression(String...ex){
        this.node.setInner(Expr.of(ex));
        return this;
    }

    public _parenthesizedExpr setExpression(_expr _e){
        this.node.setInner(_e.node());
        return this;
    }

    public _parenthesizedExpr setExpression(Expression e){
        this.node.setInner(e);
        return this;
    }

    public _expr getExpression(){
        return _expr.of(this.node.getInner());
    }

    public EnclosedExpr node(){
        return node;
    }

    public _expr getInner(){
        return _expr.of(this.node.getInner());
    }

    public boolean equals(Object other){
        if( other instanceof _parenthesizedExpr){
            return ((_parenthesizedExpr)other).node.equals( this.node);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }
    
    public String toString(){
        return this.node.toString();
    }
}
