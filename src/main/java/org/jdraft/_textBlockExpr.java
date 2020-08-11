package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

public final class _textBlockExpr implements _expr._literal<TextBlockLiteralExpr, _textBlockExpr>, _java._withText<_textBlockExpr> {

    public static final Function<String, _textBlockExpr> PARSER = s-> _textBlockExpr.of(s);

    public static _textBlockExpr of(){
        return new _textBlockExpr( new TextBlockLiteralExpr());
    }
    public static _textBlockExpr of(TextBlockLiteralExpr tb){
        return new _textBlockExpr(tb);
    }
    public static _textBlockExpr of(String...code){
        return new _textBlockExpr(Expr.textBlockLiteralExpr( code));
    }


    public static <A extends Object> _textBlockExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _textBlockExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _textBlockExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _textBlockExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _textBlockExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _textBlockExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _textBlockExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _textBlockExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _textBlockExpr from(LambdaExpr le){
        Optional<TextBlockLiteralExpr> ows = le.getBody().findFirst(TextBlockLiteralExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No text block literal found in lambda");
    }

    public static _feature._one<_textBlockExpr, String> LITERAL_VALUE = new _feature._one<>(_textBlockExpr.class, String.class,
            _feature._id.LITERAL_VALUE,
            a -> a.valueAsString(),
            (_textBlockExpr a, String value) -> a.node().setValue(value), PARSER);


    public static _feature._features<_textBlockExpr> FEATURES = _feature._features.of(_textBlockExpr.class,  PARSER, LITERAL_VALUE);

    public TextBlockLiteralExpr node;

    public _feature._features<_textBlockExpr> features(){
        return FEATURES;
    }

    public _textBlockExpr(TextBlockLiteralExpr tble){
        this.node = tble;
    }

    @Override
    public _textBlockExpr copy() {
        return new _textBlockExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _textBlockExpr replace(TextBlockLiteralExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(TextBlockLiteralExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public TextBlockLiteralExpr node(){
        return node;
    }

    public String getText(){
        return this.node.getValue();
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _textBlockExpr){
            return Objects.equals( ((_textBlockExpr)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }

    public _textBlockExpr setText(String textContent){
        this.node.setValue(textContent);
        return this;
    }

    @Override
    public String valueAsString() {
        return this.node.getValue();
    }
}
