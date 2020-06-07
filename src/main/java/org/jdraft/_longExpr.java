package org.jdraft;

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
            (_longExpr a, String value) -> a.ast().setValue(value), PARSER);


    public static _feature._features<_longExpr> FEATURES = _feature._features.of(_longExpr.class, LITERAL_VALUE);

    public LongLiteralExpr ile;

    public _longExpr(LongLiteralExpr ile){
        this.ile = ile;
    }

    public _feature._features<_longExpr> features(){
        return FEATURES;
    }

    @Override
    public _longExpr copy() {
        return new _longExpr(this.ile.clone());
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.longLiteralExpr(stringRep));
        } catch(Exception e){}
        return false;
    }
     */

    public boolean is(long value){
        return this.ile.asLong() == value;
    }

    @Override
    public boolean is(LongLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    /**
     * int i = 1_000_000;
     * @return
     */
    public boolean has_Separators(){
        return this.ile.getValue().contains("_");
    }

    /**
     * int i = 0b101;
     * int x = 0B101;
     * @return
     */
    public boolean isBinaryFormat(){
        return this.ile.getValue().startsWith("0b") || this.ile.getValue().startsWith("0B");
    }

    /**
     *
     * int j = 0x101;
     * int k = 0X101;
     * @return
     */
    public boolean isHexFormat(){
        return this.ile.getValue().startsWith("0x") || this.ile.getValue().startsWith("0X");
    }

    /**
     * int k = 01234567;
     */
    public boolean isOctalFormat(){
        return  this.ile.getValue().length() > 0
                && this.ile.getValue().startsWith("0")
                && ! isHexFormat()
                && ! isBinaryFormat();
    }

    public LongLiteralExpr ast(){
        return ile;
    }

    public Long getValue(){
        return this.ile.asLong();
    }

    public _longExpr setValue(long value){
        this.ile.setLong(value);
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
        return this.ile.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _longExpr){
            return ((_longExpr)other).ile.equals( this.ile );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ile.hashCode();
    }

    public String toString(){
        return this.ile.toString();
    }
}
