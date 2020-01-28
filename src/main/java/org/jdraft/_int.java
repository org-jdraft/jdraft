package org.jdraft;

import com.github.javaparser.ast.expr.IntegerLiteralExpr;

import java.util.HashMap;
import java.util.Map;

public class _int implements _expression<IntegerLiteralExpr, _int> {

    public static _int of(){
        return new _int(new IntegerLiteralExpr( ));
    }
    public static _int of(IntegerLiteralExpr il){
        return new _int(il);
    }
    public static _int of( String...code){
        return new _int(Ex.intLiteralEx( code));
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
            return is( Ex.intLiteralEx(stringRep));
        } catch(Exception e){

        }
        return false;
    }

    @Override
    public boolean is(IntegerLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public IntegerLiteralExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LITERAL, this.ile.getValue());
        return comps;
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