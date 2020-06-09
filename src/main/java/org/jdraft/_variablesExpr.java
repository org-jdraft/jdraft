package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.util.*;
import java.util.function.Function;
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
        _tree._node<VariableDeclarationExpr, _variablesExpr>,
        _tree._group<VariableDeclarator, _variable, _variablesExpr>,
        _annoExprs._withAnnoExprs<_variablesExpr>,
        _modifiers._withFinal<_variablesExpr>{

    public static final Function<String, _variablesExpr> PARSER = s-> _variablesExpr.of(s);

    public static _variablesExpr of(){
        return new _variablesExpr(new VariableDeclarationExpr());
    }

    public static _variablesExpr of(VariableDeclarationExpr ve){
        return new _variablesExpr(ve);
    }

    public static _variablesExpr of(String...code){
        return new _variablesExpr(Expr.variablesExpr(code));
    }

    public static _feature._one<_variablesExpr, _annoExprs> ANNOS = new _feature._one<>(_variablesExpr.class, _annoExprs.class,
            _feature._id.ANNOS,
            a -> a.getAnnoExprs(),
            (_variablesExpr p, _annoExprs _ccs) -> p.setAnnoExprs(_ccs), PARSER);

    public static _feature._one<_variablesExpr, _modifiers> MODIFIERS = new _feature._one<>(_variablesExpr.class, _modifiers.class,
            _feature._id.MODIFIERS,
            a -> a.getModifiers(),
            (_variablesExpr a, _modifiers _m) -> a.setModifiers(_m), PARSER);

    public static _feature._many<_variablesExpr, _variable> VARIABLES = new _feature._many<>(_variablesExpr.class, _variable.class,
            _feature._id.VARIABLES,
            _feature._id.VARIABLE,
            a -> a.list(),
            (_variablesExpr p, List<_variable> _ccs) -> p.set(_ccs), PARSER, s-> _variable.of(s))
            .setOrdered(false);

    public static _feature._features<_variablesExpr> FEATURES = _feature._features.of(_variablesExpr.class,  PARSER, ANNOS, MODIFIERS, VARIABLES);

    public _feature._features<_variablesExpr> features(){
        return FEATURES;
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

    public _modifiers getModifiers(){
        return _modifiers.of( this.ast() );
    }

    public _variablesExpr setModifiers( _modifiers _mods){
        this.ast().getModifiers().clear();
        this.ast().setModifiers( _mods.ast() );
        return this;
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.variablesExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }
     */

    /**
     * Is the variable at the index equal to the _v
     * @param index the index of the variable (0-based)
     * @param _v the target variable
     * @return true if they are equal, false otherwise
     */
    public boolean isAt(int index, _variable _v){
        if( index < size() ){
            return getAt(index).equals(_v);
        }
        return false;
    }

    /**
     * Is the variable at the index (0-based) equal to the variable
     * @param index
     * @param var
     * @return
     */
    public boolean isAt(int index, String var){
        if( index < size() ){
            return getAt(index).is(var);
        }
        return false;
    }

    /**
     * Is the variable at the index equal to the lambda
     * @param index
     * @param _pv
     * @return
     */
    public boolean isAt( int index, Predicate<_variable> _pv){
        if( index < size() ){
            return _pv.test( getAt(index) );
        }
        return false;
    }

    public boolean has( String varCode){
        try{
            return has( _variable.of(varCode));
        } catch(Exception e){
            return false;
        }
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
        try {
            return equals(_variablesExpr.of(astNode));
        }catch(Exception e){
            return false;
        }
    }

    public VariableDeclarationExpr ast(){
        return varDeclEx;
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
            _variablesExpr _o = ((_variablesExpr)other);
            //return ((_variablesExpr)other).varDeclEx.equals( this.varDeclEx);
            if(ast().getVariables().size() != _o.ast().getVariables().size() ){
                return false;
            }
            //they have the same number of variables
            if( !Objects.equals( this.getAnnoExprs(), _o.getAnnoExprs() ) ){
                return false;
            }
            if( !Objects.equals( this.getModifiers(), _o.getModifiers() ) ){
                return false;
            }
            Set<VariableDeclarator> vds = new HashSet<>();
            vds.addAll( this.ast().getVariables() );

            Set<VariableDeclarator> ovds = new HashSet<>();
            ovds.addAll( _o.ast().getVariables() );
            return Objects.equals(ovds, vds );
        }
        return false;
    }

    public boolean is(String code){
        return is(new String[]{code});
    }
    public int hashCode(){
        return 31 * this.varDeclEx.hashCode();
    }

    public String toString() {
        return toString( new PrettyPrinterConfiguration());
    }

    public String toString(PrettyPrinterConfiguration ppc){
        if( varDeclEx.getVariables().size() == 0){
            return "";
        }
        return this.varDeclEx.toString(ppc);
    }

}
