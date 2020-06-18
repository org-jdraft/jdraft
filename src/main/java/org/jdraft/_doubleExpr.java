package org.jdraft;

import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;

import java.util.function.Function;

public final class _doubleExpr implements _expr._literal<DoubleLiteralExpr, _doubleExpr> {

    public static final Function<String, _doubleExpr> PARSER = s-> _doubleExpr.of(s);

    public static _doubleExpr of(){
        return new _doubleExpr( new DoubleLiteralExpr());
    }
    public static _doubleExpr of(DoubleLiteralExpr dl){
        return new _doubleExpr(dl);
    }
    public static _doubleExpr of(String...code){
        return new _doubleExpr(Expr.doubleLiteralExpr( code));
    }

    public static _doubleExpr of(double d){
        return new _doubleExpr(Expr.doubleLiteralExpr( d));
    }

    public static _feature._one<_doubleExpr, String> LITERAL_VALUE = new _feature._one<>(_doubleExpr.class, String.class,
            _feature._id.LITERAL_VALUE,
            a -> a.valueAsString(),
            (_doubleExpr a, String value) -> a.node().setValue(value), PARSER);


    public static _feature._features<_doubleExpr> FEATURES = _feature._features.of(_doubleExpr.class,  PARSER, LITERAL_VALUE);

    public DoubleLiteralExpr node;

    public _doubleExpr(DoubleLiteralExpr node){
        this.node = node;
    }

    public _feature._features<_doubleExpr> features(){
        return FEATURES;
    }

    @Override
    public _doubleExpr copy() {
        return new _doubleExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _doubleExpr replace(DoubleLiteralExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(DoubleLiteralExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public boolean is(double d){
        return Expr.equal(node, d);
    }

    public Double getValue(){
        return this.node.asDouble();
    }

    public _doubleExpr set(String value){
        this.node.setValue(value);
        return this;
    }

    public _doubleExpr set(Double value){
        this.node.setDouble(value);
        return this;
    }

    public _doubleExpr set(Float value){
        this.node.setValue(value+"F");
        return this;
    }

    /**
     * Important that there are multiple representations for the same float value
     * 1.23f
     * 1.23d
     *
     * @return
     */
    public String valueAsString(){
        return this.node.toString();
    }

    public DoubleLiteralExpr node(){
        return node;
    }

    public boolean equals(Object other){
        if( other instanceof _doubleExpr){
            return ((_doubleExpr)other).node.equals( this.node);
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
