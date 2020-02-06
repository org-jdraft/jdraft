package org.jdraft;

import com.github.javaparser.ast.expr.LongLiteralExpr;

import java.util.HashMap;
import java.util.Map;

public class _long implements _expression._literal<LongLiteralExpr, _long> {

    public static _long of( ){
        return new _long( new LongLiteralExpr());
    }

    //we need to manually set this L because it isnt done
    public static _long of( long l){
        return of( new LongLiteralExpr(l + "L"));
    }

    public static _long of(LongLiteralExpr ll){
        return new _long(ll);
    }
    public static _long of( String...code){
        return new _long(Ex.longLiteralEx( code));
    }

    public LongLiteralExpr ile;

    public _long(LongLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _long copy() {
        return new _long(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.longLiteralEx(stringRep));
        } catch(Exception e){}
        return false;
    }

    @Override
    public boolean is(LongLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public LongLiteralExpr ast(){
        return ile;
    }

    public Long getValue(){
        return this.ile.asLong();
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

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LITERAL, this.ile.getValue());
        return comps;
    }

    public boolean equals(Object other){
        if( other instanceof _long){
            return ((_long)other).ile.equals( this.ile );
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
