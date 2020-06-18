package org.jdraft;

import com.github.javaparser.ast.expr.IntegerLiteralExpr;

import java.util.function.Function;

public final class _intExpr implements _expr._literal<IntegerLiteralExpr, _intExpr> {

    public static final Function<String, _intExpr> PARSER = s-> _intExpr.of(s);

    public static _intExpr of(){
        return new _intExpr(new IntegerLiteralExpr( ));
    }
    public static _intExpr of(int i){
        return of( new IntegerLiteralExpr(i));
    }
    public static _intExpr of(IntegerLiteralExpr il){
        return new _intExpr(il);
    }
    public static _intExpr of(String...code){
        return new _intExpr(Expr.integerLiteralExpr( code));
    }

    public static _feature._one<_intExpr, String> LITERAL_VALUE = new _feature._one<>(_intExpr.class, String.class,
            _feature._id.LITERAL_VALUE,
            a -> a.valueAsString(),
            (_intExpr a, String value) -> a.node().setValue(value), PARSER);


    public static _feature._features<_intExpr> FEATURES = _feature._features.of(_intExpr.class, PARSER, LITERAL_VALUE);

    public IntegerLiteralExpr node;

    public _feature._features<_intExpr> features(){
        return FEATURES;
    }

    public _intExpr(IntegerLiteralExpr node){
        this.node = node;
    }

    @Override
    public _intExpr copy() {
        return new _intExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _intExpr replace(IntegerLiteralExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean is(int value){
        return this.node.asInt() == value;
    }

    /**
     * int i = 1_000_000;
     * @return
     */
    public boolean has_Separators(){
        return this.node.getValue().contains("_");
    }

    /**
     * int i = 0b101;
     * int x = 0B101;
     * @return
     */
    public boolean isBinaryFormat(){
        return this.node.getValue().startsWith("0b") || this.node.getValue().startsWith("0B");
    }

    /**
     *
     * int j = 0x101;
     * int k = 0X101;
     * @return
     */
    public boolean isHexFormat(){
        return this.node.getValue().startsWith("0x") || this.node.getValue().startsWith("0X");
    }

    /**
     * int k = 01234567;
     */
    public boolean isOctalFormat(){
        return  this.node.getValue().length() > 0
                && this.node.getValue().startsWith("0")
                && ! isHexFormat()
                && ! isBinaryFormat();
    }


    public IntegerLiteralExpr node(){
        return node;
    }

    public int getValue(){
        return this.node.asInt();
    }

    /**
     * Important that there are multiple representations for the same int value
     *
     * base-10 (normal)   123456
     * hex                0xDEADBEE
     * octal              023
     * binary             0b0101001010
     * using _ separators (i.e. 1_000_000)
     * @return
     */
    public String valueAsString(){
        return this.node.toString();
    }

    public _intExpr setValue(int value){
        this.node.setInt(value);
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _intExpr){
            return ((_intExpr)other).node.equals( this.node);
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