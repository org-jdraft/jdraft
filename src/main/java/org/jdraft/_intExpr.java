package org.jdraft;

import com.github.javaparser.ast.expr.IntegerLiteralExpr;

public final class _intExpr implements _expr._literal<IntegerLiteralExpr, _intExpr> {

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
        return new _intExpr(Exprs.intLiteralEx( code));
    }

    public IntegerLiteralExpr ile;

    public _intExpr(IntegerLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _intExpr copy() {
        return new _intExpr(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.intLiteralEx(stringRep));
        } catch(Exception e){

        }
        return false;
    }

    public boolean is(int value){
        return this.ile.asInt() == value;
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


    public IntegerLiteralExpr ast(){
        return ile;
    }

    public int getValue(){
        return this.ile.asInt();
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
        return this.ile.toString();
    }

    public _intExpr setValue(int value){
        this.ile.setInt(value);
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _intExpr){
            return ((_intExpr)other).ile.equals( this.ile );
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