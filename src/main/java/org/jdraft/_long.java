package org.jdraft;

import com.github.javaparser.ast.expr.LongLiteralExpr;

import java.util.HashMap;
import java.util.Map;

public class _long implements _expression<LongLiteralExpr, _long> {

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
