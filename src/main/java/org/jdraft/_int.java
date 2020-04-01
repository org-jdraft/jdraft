package org.jdraft;

import com.github.javaparser.ast.expr.IntegerLiteralExpr;

public final class _int implements _expression._literal<IntegerLiteralExpr, _int> {

    public static _int of(){
        return new _int(new IntegerLiteralExpr( ));
    }
    public static _int of(int i){
        return of( new IntegerLiteralExpr(i));
    }
    public static _int of(IntegerLiteralExpr il){
        return new _int(il);
    }
    public static _int of( String...code){
        return new _int(Expressions.intLiteralEx( code));
    }

    public IntegerLiteralExpr ile;

    public _int(IntegerLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _int copy() {
        return new _int(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expressions.intLiteralEx(stringRep));
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

    public _int setValue( int value){
        this.ile.setInt(value);
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _int){
            return ((_int)other).ile.equals( this.ile );
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