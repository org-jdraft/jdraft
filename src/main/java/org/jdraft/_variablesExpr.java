package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The declaration of one or more variables
 * int i;
 * int i=0;
 * int i, j;
 * int i=0, j=1;
 * int i, j[], k;
 */
public final class _variablesExpr implements _expr<VariableDeclarationExpr, _variablesExpr>,
        _java._multiPart<VariableDeclarationExpr, _variablesExpr>,
        _java._set<VariableDeclarator, _variable, _variablesExpr>,
        _annoExprs._withAnnoExprs<_variablesExpr>,
        _modifiers._withFinal<_variablesExpr>{

    public static _variablesExpr of(){
        return new _variablesExpr(new VariableDeclarationExpr());
    }

    public static _variablesExpr of(VariableDeclarationExpr ve){
        return new _variablesExpr(ve);
    }

    public static _variablesExpr of(String...code){
        return new _variablesExpr(Exprs.variablesExpr(code));
    }

    public VariableDeclarationExpr varDeclEx;

    public _variablesExpr(VariableDeclarationExpr varDeclEx){
        this.varDeclEx = varDeclEx;
    }

    @Override
    public _variablesExpr copy() {
        return new _variablesExpr(this.varDeclEx.clone());
    }

    @Override
    public List<_variable> list() {
        return listAstElements().stream().map(v-> _variable.of(v)).collect(Collectors.toList());
    }

    @Override
    public NodeList<VariableDeclarator> listAstElements() {
        return this.varDeclEx.getVariables();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.variablesExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public boolean isFinal(){
        return this.varDeclEx.isFinal();
    }

    public _variablesExpr setFinal(boolean fin){
        this.varDeclEx.setFinal(fin);
        return this;
    }

    public _variablesExpr setFinal(){
        this.varDeclEx.setFinal(true);
        return this;
    }
    @Override
    public boolean is(VariableDeclarationExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public VariableDeclarationExpr ast(){
        return varDeclEx;
    }

    @Override
    public Map<_java.Feature, Object> components() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        comps.put( _java.Feature.IS_FINAL, varDeclEx.isFinal() );
        comps.put( _java.Feature.ANNO_EXPRS, _annoExprs.of(varDeclEx));
        comps.put( _java.Feature.VARIABLES, list() );
        return comps;
    }

    //does the same as list
    public List<_variable> listVariables(){

        return this.varDeclEx.getVariables().stream().map(v-> _variable.of(v)).collect(Collectors.toList());
    }

    //does the same as list(predicate)
    public List<_variable> listVariables( Predicate<_variable> matchFn){
        return listVariables().stream().filter(matchFn).collect(Collectors.toList());
    }

    public _variable getAt(int index){
        return _variable.of(this.varDeclEx.getVariable(index));
    }

    public _variable get(String name){
        List<_variable> lv = list(v-> v.isNamed(name));
        if( lv.isEmpty() ){
            return null;
        }
        return lv.get(0);
    }

    @Override
    public _annoExprs getAnnoExprs() {
        return _annoExprs.of(this.varDeclEx);
    }

    public boolean equals(Object other){
        if( other instanceof _variablesExpr){
            return ((_variablesExpr)other).varDeclEx.equals( this.varDeclEx);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.varDeclEx.hashCode();
    }
    
    public String toString(){
        if( varDeclEx.getVariables().size() == 0){
            return "";
        }
        return this.varDeclEx.toString();
    }

}
