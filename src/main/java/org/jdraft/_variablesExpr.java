package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.CharLiteralExpr;
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
        _annos._withAnnos<_variablesExpr>,
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

    public static _feature._one<_variablesExpr, _annos> ANNOS = new _feature._one<>(_variablesExpr.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_variablesExpr p, _annos _ccs) -> p.setAnnos(_ccs), PARSER);

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

    public VariableDeclarationExpr node;

    public _variablesExpr(VariableDeclarationExpr node){
        this.node = node;
    }

    @Override
    public _variablesExpr copy() {
        return new _variablesExpr(this.node.clone());
    }

    @Override
    public List<_variable> list() {
        return astList().stream().map(v-> _variable.of(v)).collect(Collectors.toList());
    }

    @Override
    public NodeList<VariableDeclarator> astList() {
        return this.node.getVariables();
    }

    public _modifiers getModifiers(){
        return _modifiers.of( this.node() );
    }

    public _variablesExpr setModifiers( _modifiers _mods){
        this.node().getModifiers().clear();
        this.node().setModifiers( _mods.ast() );
        return this;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _variablesExpr replace(VariableDeclarationExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

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
        return this.node.isFinal();
    }

    public _variablesExpr setFinal(boolean fin){
        this.node.setFinal(fin);
        return this;
    }

    public _variablesExpr setFinal(){
        this.node.setFinal(true);
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

    public VariableDeclarationExpr node(){
        return node;
    }

    //does the same as list
    public List<_variable> listVariables(){
        return this.node.getVariables().stream().map(v-> _variable.of(v)).collect(Collectors.toList());
    }

    //does the same as list(predicate)
    public List<_variable> listVariables( Predicate<_variable> matchFn){
        return listVariables().stream().filter(matchFn).collect(Collectors.toList());
    }

    public _variable getAt(int index){
        return _variable.of(this.node.getVariable(index));
    }

    public _variable get(String name){
        List<_variable> lv = list(v-> v.isNamed(name));
        if( lv.isEmpty() ){
            return null;
        }
        return lv.get(0);
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(this.node);
    }

    public boolean equals(Object other){
        if( other instanceof _variablesExpr){
            _variablesExpr _o = ((_variablesExpr)other);
            //return ((_variablesExpr)other).varDeclEx.equals( this.varDeclEx);
            if(this.node().getVariables().size() != _o.node().getVariables().size() ){
                return false;
            }
            //they have the same number of variables
            if( !Objects.equals( this.getAnnos(), _o.getAnnos() ) ){
                return false;
            }
            if( !Objects.equals( this.getModifiers(), _o.getModifiers() ) ){
                return false;
            }
            Set<VariableDeclarator> vds = new HashSet<>();
            vds.addAll( this.node().getVariables() );

            Set<VariableDeclarator> ovds = new HashSet<>();
            ovds.addAll( _o.node().getVariables() );
            return Objects.equals(ovds, vds );
        }
        return false;
    }

    public boolean is(String code){
        return is(new String[]{code});
    }
    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public String toString() {
        return toString( new PrettyPrinterConfiguration());
    }

    public String toString(PrettyPrinterConfiguration ppc){
        if( node.getVariables().size() == 0){
            return "";
        }
        return this.node.toString(ppc);
    }
}
