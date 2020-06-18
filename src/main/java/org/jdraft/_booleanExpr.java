package org.jdraft;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;

import java.util.function.Function;

public final class _booleanExpr implements _expr._literal<BooleanLiteralExpr, _booleanExpr> {

    public static final Function<String, _booleanExpr> PARSER = s-> _booleanExpr.of(s);

    public static _booleanExpr of( ){
        return new _booleanExpr(new BooleanLiteralExpr());
    }

    public static _booleanExpr of(boolean b){
        return new _booleanExpr(new BooleanLiteralExpr(b));
    }
    public static _booleanExpr of(BooleanLiteralExpr bl){
        return new _booleanExpr(bl);
    }
    public static _booleanExpr of(String...code){
        return new _booleanExpr(Expr.booleanLiteralExpr( code));
    }

    public static _feature._one<_booleanExpr, Boolean> LITERAL_VALUE = new _feature._one<>(_booleanExpr.class, Boolean.class,
            _feature._id.LITERAL_VALUE,
            a -> a.getValue(),
            (_booleanExpr a, Boolean b) -> a.set(b), PARSER);

    public static _feature._features<_booleanExpr> FEATURES = _feature._features.of(_booleanExpr.class,  PARSER, LITERAL_VALUE);

    public BooleanLiteralExpr node;

    public _feature._features<_booleanExpr> features(){
        return FEATURES;
    }

    public _booleanExpr(BooleanLiteralExpr node){
        this.node = node;
    }

    @Override
    public _booleanExpr copy() {
        return new _booleanExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _booleanExpr replace(BooleanLiteralExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean is(boolean b){
        return this.node().getValue() == b;
    }

    @Override
    public boolean is(BooleanLiteralExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public boolean isTrue(){
        return this.node.getValue();
    }

    public boolean isFalse(){
        return !this.node.getValue();
    }

    public BooleanLiteralExpr node(){
        return node;
    }

    public boolean equals(Object other){
        if( other instanceof _booleanExpr){
            return ((_booleanExpr)other).node.equals( this.node);
        }
        return false;
    }

    public _booleanExpr set(String...value){
        this.set(Expr.booleanLiteralExpr(value));
        return this;
    }

    public _booleanExpr set(BooleanLiteralExpr  b){
        this.node = b;
        return this;
    }

    public _booleanExpr set(boolean b){
        this.node.setValue(b);
        return this;
    }

    public boolean getValue(){
        return this.node.getValue();
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public String toString(){
        return this.node.toString();
    }

    @Override
    public String valueAsString() {
        return node.getValue()+"";
    }
}
