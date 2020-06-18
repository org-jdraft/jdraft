package org.jdraft;

import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.Optional;
import java.util.function.*;

/**
 *
 * <PRE><CODE>
 * (String)getCharSeq() //"String" as type; "getCharSeq();" is expression
 * (List<String>)a      //List<String> as type; "a" as expression
 * </CODE></PRE>
 *
 */
public final class _castExpr implements _expr<CastExpr, _castExpr>, _tree._node<CastExpr, _castExpr>,
        _typeRef._withType<CastExpr, _castExpr>, _java._withExpression<CastExpr, _castExpr> {

    public static final Function<String, _castExpr> PARSER = s-> _castExpr.of(s);

    public static _castExpr of(){
        return new _castExpr( new CastExpr());
    }
    public static _castExpr of(CastExpr ce){
        return new _castExpr(ce);
    }
    public static _castExpr of(String...code){
        return new _castExpr(Expr.castExpr( code));
    }

    public static <A extends Object> _castExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _castExpr of(Supplier<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _castExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _castExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _castExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _castExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _castExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _castExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _castExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _castExpr from(LambdaExpr le){
        Optional<CastExpr> ows = le.getBody().findFirst(CastExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No cast found in lambda");
    }

    public static _feature._one<_castExpr, _typeRef> TYPE = new _feature._one<>(_castExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_castExpr a, _typeRef o) -> a.setType(o), PARSER);

    public static _feature._one<_castExpr, _expr> EXPRESSION = new _feature._one<>(_castExpr.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_castExpr a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._features<_castExpr> FEATURES = _feature._features.of(_castExpr.class,  PARSER, TYPE, EXPRESSION);

    public _feature._features<_castExpr> features(){
        return FEATURES;
    }

    public CastExpr node;

    public _castExpr(CastExpr node){
        this.node = node;
    }

    @Override
    public _castExpr copy() {
        return new _castExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _castExpr replace(CastExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(CastExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public CastExpr node(){
        return node;
    }

    public boolean equals(Object other){
        if( other instanceof _castExpr){
            return ((_castExpr)other).node.equals( this.node);
        }
        return false;
    }

    public _castExpr setExpression(String ex){
        return setExpression(_expr.of(ex));
    }

    public _castExpr setExpression(Expression e){
        this.node.setExpression(e);
        return this;
    }

    public _castExpr setExpression(_expr _e){
        this.node.setExpression(_e.node());
        return this;
    }

    public boolean isExpression(String expression ){
        return isExpression(_expr.of(expression));
    }

    public boolean isExpression( Expression e){
        return Expr.equal(e, this.node.getExpression());
    }

    public boolean isExpression( _expr _e){
        return Expr.equal(_e.node(), this.node.getExpression());
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }
    
    public String toString(){
        return this.node.toString();
    }
}
