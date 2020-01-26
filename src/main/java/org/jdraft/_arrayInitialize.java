package org.jdraft;

import com.github.javaparser.ast.expr.ArrayInitializerExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _arrayInitialize implements _expression<ArrayInitializerExpr, _arrayInitialize> {

    public ArrayInitializerExpr ile;

    public _arrayInitialize(ArrayInitializerExpr ile){
        this.ile = ile;
    }

    @Override
    public _arrayInitialize copy() {
        return new _arrayInitialize(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.arrayInitializerEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ArrayInitializerExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ArrayInitializerExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.VALUES, ile.getValues());
        return comps;
    }

    public List<_expression> getValues(){
        List<_expression> vs = new ArrayList<>();
        this.ile.getValues().forEach(v-> vs.add( _expression.of(v)));
        return vs;
    }

    public boolean equals(Object other){
        if( other instanceof _arrayInitialize){
            return ((_arrayInitialize)other).ile.equals( this.ile );
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
