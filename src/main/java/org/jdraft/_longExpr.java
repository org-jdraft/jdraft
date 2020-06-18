package org.jdraft;

import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;

import java.util.function.Function;

public final class _longExpr implements _expr._literal<LongLiteralExpr, _longExpr> {

    public static final Function<String, _longExpr> PARSER = s-> _longExpr.of(s);

    public static _longExpr of( ){
        return new _longExpr( new LongLiteralExpr());
    }

    //we need to manually set this L because it isnt done
    public static _longExpr of(long l){
        return of( new LongLiteralExpr(l + "L"));
    }

    public static _longExpr of(LongLiteralExpr ll){
        return new _longExpr(ll);
    }
    public static _longExpr of(String...code){
        return new _longExpr(Expr.longLiteralExpr( code));
    }

    public static _feature._one<_longExpr, String> LITERAL_VALUE = new _feature._one<>(_longExpr.class, String.class,
            _feature._id.LITERAL_VALUE,
            a -> a.valueAsString(),
            (_longExpr a, String value) -> a.node().setValue(value), PARSER);


    public static _feature._features<_longExpr> FEATURES = _feature._features.of(_longExpr.class,  PARSER, LITERAL_VALUE);

    public LongLiteralExpr node;

    public _longExpr(LongLiteralExpr node){
        this.node = node;
    }

    public _feature._features<_longExpr> features(){
        return FEATURES;
    }

    @Override
    public _longExpr copy() {
        return new _longExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _longExpr replace(LongLiteralExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean is(long value){
        return this.node.asLong() == value;
    }

    @Override
    public boolean is(LongLiteralExpr astNode) {
        return this.node( ).equals(astNode);
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

    public LongLiteralExpr node(){
        return node;
    }

    public Long getValue(){
        return this.node.asLong();
    }

    public _longExpr setValue(long value){
        this.node.setLong(value);
        return this;
    }

    /**
     * Important that there are multiple representations for the same int value
     *
     * base-10 (normal)   123456L
     * hex                0xDEADL
     * octal              023L
     * binary             0b0101001010L
     * using _ separators (i.e. 1_000_000_000_000L)
     * @return
     */
    public String valueAsString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _longExpr){
            return ((_longExpr)other).node.equals( this.node);
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
