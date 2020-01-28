package org.jdraft;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _variable implements _expression<VariableDeclarationExpr, _variable> {

    public static _variable of(){
        return new _variable(new VariableDeclarationExpr());
    }
    public static _variable of(VariableDeclarationExpr ve){
        return new _variable(ve);
    }
    public static _variable of( String...code){
        return new _variable(Ex.varLocalEx(code));
    }

    public VariableDeclarationExpr ile;

    public _variable(VariableDeclarationExpr ile){
        this.ile = ile;
    }

    @Override
    public _variable copy() {
        return new _variable(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.varLocalEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(VariableDeclarationExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public VariableDeclarationExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put( _java.Component.MODIFIERS, ile.getModifiers());
        comps.put( _java.Component.VARIABLES, ile.getVariables());
        return comps;
    }

    public _modifiers getModifiers(){
        return _modifiers.of(this.ile);
    }

    public List<VariableDeclarator> listVariables(){
        return this.ile.getVariables();
    }

    public boolean equals(Object other){
        if( other instanceof _variable){
            return ((_variable)other).ile.equals( this.ile );
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
