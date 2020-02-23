package org.jdraft;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _variable implements _expression<VariableDeclarationExpr, _variable>,
        _java._multiPart<VariableDeclarationExpr, _variable>,
        _modifiers._withModifiers<_variable> {

    public static _variable of(){
        return new _variable(new VariableDeclarationExpr());
    }
    public static _variable of(VariableDeclarationExpr ve){
        return new _variable(ve);
    }
    public static _variable of( String...code){
        return new _variable(Ex.varLocalEx(code));
    }

    public VariableDeclarationExpr varDeclEx;

    public _variable(VariableDeclarationExpr varDeclEx){
        this.varDeclEx = varDeclEx;
    }

    @Override
    public _variable copy() {
        return new _variable(this.varDeclEx.clone());
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
        return varDeclEx;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put( _java.Component.MODIFIERS, varDeclEx.getModifiers());
        comps.put( _java.Component.VARIABLES, varDeclEx.getVariables());
        return comps;
    }

    public _modifiers getModifiers(){
        return _modifiers.of(this.varDeclEx);
    }

    public List<VariableDeclarator> listVariables(){
        return this.varDeclEx.getVariables();
    }

    public boolean equals(Object other){
        if( other instanceof _variable){
            return ((_variable)other).varDeclEx.equals( this.varDeclEx);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.varDeclEx.hashCode();
    }
    
    public String toString(){
        return this.varDeclEx.toString();
    }
}
