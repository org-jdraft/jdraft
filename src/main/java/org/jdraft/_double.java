package org.jdraft;

import com.github.javaparser.ast.expr.DoubleLiteralExpr;

import java.util.HashMap;
import java.util.Map;

public class _double implements _expression<DoubleLiteralExpr, _double> {

    public DoubleLiteralExpr ile;

    public _double(DoubleLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _double copy() {
        return new _double(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.doubleLiteralEx(stringRep));
        } catch(Exception e){

        }
        return false;
    }

    @Override
    public boolean is(DoubleLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public DoubleLiteralExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LITERAL, this.ile.getValue());
        return comps;
    }

    public boolean equals(Object other){
        if( other instanceof _double){
            return ((_double)other).ile.equals( this.ile );
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
