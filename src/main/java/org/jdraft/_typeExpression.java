package org.jdraft;

import com.github.javaparser.ast.expr.TypeExpr;

import java.util.HashMap;
import java.util.Map;

public class _typeExpression implements _expression<TypeExpr, _typeExpression>, _java._simple<TypeExpr, _typeExpression> {

    public static _typeExpression of(){
        return new _typeExpression( new TypeExpr());
    }
    public static _typeExpression of(TypeExpr te){
        return new _typeExpression(te);
    }
    public static _typeExpression of( String...code){
        return new _typeExpression(Ex.typeEx( code));
    }

    public TypeExpr te;

    public _typeExpression(TypeExpr te){
        this.te = te;
    }

    @Override
    public _typeExpression copy() {
        return new _typeExpression(this.te.clone());
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
        return te;
    }

    /*
    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TYPE, te.getType());
        return comps;
    }
     */


    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public _typeRef getType(){
        return _typeRef.of(this.te.getType());
    }

    public boolean equals(Object other){
        if( other instanceof _typeExpression){
            _typeExpression _te = ((_typeExpression)other);
            return Ast.typesEqual( _te.ast().getType(), this.te.getType());
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.te.hashCode();
    }
    
    public String toString(){
        return this.te.toString();
    }
}
