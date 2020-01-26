package org.jdraft;

import com.github.javaparser.ast.expr.ArrayAccessExpr;

import java.util.HashMap;
import java.util.Map;

public class _arrayAccess implements _expression<ArrayAccessExpr, _arrayAccess> {

    public ArrayAccessExpr ile;

    public _arrayAccess(ArrayAccessExpr ile){
        this.ile = ile;
    }

    @Override
    public _arrayAccess copy() {
        return new _arrayAccess(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.arrayAccessEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ArrayAccessExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ArrayAccessExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.INDEX, ile.getIndex());
        comps.put(_java.Component.NAME, ile.getName().toString());
        return comps;
    }

    public _expression getName(){
        return _expression.of(this.ile.getName());
    }

    public _expression getIndex(){
        return _expression.of(this.ile.getIndex());
    }

    public boolean equals(Object other){
        if( other instanceof _arrayAccess){
            return ((_arrayAccess)other).ile.equals( this.ile );
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
