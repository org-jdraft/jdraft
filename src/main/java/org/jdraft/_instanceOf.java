package org.jdraft;

import com.github.javaparser.ast.expr.InstanceOfExpr;

import java.util.HashMap;
import java.util.Map;

public class _instanceOf implements _expression<InstanceOfExpr, _instanceOf> {

    public static _instanceOf of(){
        return new _instanceOf(new InstanceOfExpr( ));
    }
    public static _instanceOf of( InstanceOfExpr ie){
        return new _instanceOf(ie);
    }
    public static _instanceOf of( String...code){
        return new _instanceOf(Ex.instanceOfEx( code));
    }

    public InstanceOfExpr ile;

    public _instanceOf(InstanceOfExpr ile){
        this.ile = ile;
    }

    @Override
    public _instanceOf copy() {
        return new _instanceOf(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.instanceOfEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(InstanceOfExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public InstanceOfExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TYPE, ile.getType());
        comps.put(_java.Component.EXPRESSION, ile.getExpression());
        return comps;
    }

    public _expression getExpression(){
        return _expression.of(this.ile.getExpression());
    }

    public _typeRef getType(){
        return _typeRef.of(this.ile.getType());
    }

    public boolean equals(Object other){
        if( other instanceof _instanceOf){
            return ((_instanceOf)other).ile.equals( this.ile );
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
