package org.jdraft;

import com.github.javaparser.ast.expr.TypeExpr;

import java.util.HashMap;
import java.util.Map;

public class _typeExpression implements _expression<TypeExpr, _typeExpression> {

    public static _typeExpression of(){
        return new _typeExpression( new TypeExpr());
    }

    public static _typeExpression of( String...code){
        return new _typeExpression(Ex.typeEx( code));
    }

    public TypeExpr ile;

    public _typeExpression(TypeExpr ile){
        this.ile = ile;
    }

    @Override
    public _typeExpression copy() {
        return new _typeExpression(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.typeEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(TypeExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public TypeExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TYPE, ile.getType());
        return comps;
    }


    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public _typeRef getType(){
        return _typeRef.of(this.ile.getType());
    }

    public boolean equals(Object other){
        if( other instanceof _typeExpression){
            _typeExpression _te = ((_typeExpression)other);
            return Ast.typesEqual( _te.ast().getType(), this.ile.getType());
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
