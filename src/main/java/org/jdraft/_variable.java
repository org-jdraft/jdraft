package org.jdraft;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.util.Objects;
import java.util.function.Function;

public final class _variable implements _tree._node<VariableDeclarator, _variable>,
        _java._withNameType<VariableDeclarator, _variable> {

    public static final Function<String, _variable> PARSER = s-> _variable.of(s);

    public static _variable of(){
        return of( new VariableDeclarator());
    }

    public static _variable of( String...var){
        if( var.length == 0){
            return of( );
        }
        return new _variable( Ast.variableDeclarator(var) );
    }

    public static _variable of( VariableDeclarator vd ){
        return new _variable( vd );
    }

    public static _feature._one<_variable, _typeRef> TYPE = new _feature._one<>(_variable.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_variable a, _typeRef _tr) -> a.setType(_tr), PARSER);

    public static _feature._one<_variable, String> NAME = new _feature._one<>(_variable.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_variable a, String name) -> a.setName(name), PARSER);

    public static _feature._one<_variable, _expr> INIT = new _feature._one<>(_variable.class, _expr.class,
            _feature._id.INIT,
            a -> a.getInit(),
            (_variable a, _expr _e) -> a.setInit(_e), PARSER);

    public static _feature._features<_variable> FEATURES = _feature._features.of(_variable.class,  PARSER, TYPE, NAME, INIT);

    public VariableDeclarator node;

    public _variable(VariableDeclarator node){
        this.node = node;
    }

    public _feature._features<_variable> features(){
        return FEATURES;
    }

    public _typeRef getType(){
        return _typeRef.of(this.node.getType());
    }

    @Override
    public _variable copy() {
        return of( node.clone());
    }

    @Override
    public VariableDeclarator node() {
        return node;
    }

    public boolean equals(Object o){
        if( o instanceof _variable){
            _variable _v = (_variable)o;
            return Objects.equals( this.node, _v.node());
        }
        return false;
    }

    /**
     * Is the variable defined as part of a member field (i.e. on a type)
     * @return true if this variable is a child of a Field
     */
    public boolean isField(){
        if( this.node.getParentNode().isPresent()){
            if( this.node.getParentNode().get() instanceof VariableDeclarationExpr ){
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isLocal(){
        if( this.node.getParentNode().isPresent()){
            if( this.node.getParentNode().get() instanceof VariableDeclarationExpr ){
                return true;
            }
            return false;
        }
        return false; //no parent so we dont know if it's a field or a local var
    }

    public boolean isFinal(){
        if( this.node.getParentNode().isPresent() &&  this.node.getParentNode().get() instanceof VariableDeclarationExpr){
            //check if the parent node describing this is
            VariableDeclarationExpr vde = (VariableDeclarationExpr) this.node.getParentNode().get();
            return vde.isFinal();
        }
        return false;
    }

    public _expr getInit(){
        if( this.node.getInitializer().isPresent()){
            return _expr.of( node.getInitializer().get());
        }
        return null;
    }

    public _variable setInit(String...e){
        this.node.setInitializer(Expr.of(e));
        return this;
    }

    public _variable setInit(Expression e){
        this.node.setInitializer(e);
        return this;
    }

    public _variable setInit(_expr _e){
        this.node.setInitializer(_e.node());
        return this;
    }

    public _variable removeInit(){
        this.node.removeInitializer();
        return this;
    }

    public _variable setInit(boolean b){
        return setInit(Expr.of(b));
    }

    public _variable setInit(int i){
        return setInit(Expr.of(i));
    }

    public _variable setInit(char c){
        return setInit(Expr.of(c));
    }

    public _variable setInit(float f){
        return setInit(Expr.of(f));
    }

    public _variable setInit(long l){
        return setInit(Expr.of(l));
    }

    public _variable setInit(double d){
        return setInit(Expr.of(d));
    }

    public boolean hasInit(){
        return this.node.getInitializer().isPresent();
    }

    public boolean isInit(boolean b){
        return isInit(Expr.of(b));
    }

    public boolean isInit(int i){
        return isInit(Expr.of(i));
    }

    public boolean isInit(char c){
        return isInit(Expr.of(c));
    }

    public boolean isInit(float f){
        return isInit(Expr.of(f));
    }

    public boolean isInit(long l){
        return isInit(Expr.of(l));
    }

    public boolean isInit(double d){
        return isInit(Expr.of(d));
    }

    public boolean isInit(String...ex){
        try{
            return isInit(Expr.of(ex));
        }catch(Exception e){
            return false;
        }
    }
    public boolean isInit(_expr _e){
        return isInit(_e.node());
    }

    public boolean isInit(Expression e){
        return this.node.getInitializer().isPresent() &&
                Expr.equal( this.node.getInitializer().get(), e);
    }

    public int hashCode( ){
        return this.node.hashCode() * 31;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _variable replace(VariableDeclarator replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public _variable setName(String name) {
        this.node.setName(name);
        return this;
    }

    public String toString(){
        return this.node.toString();
    }

    public SimpleName getNameNode(){ return this.node.getName(); }

    @Override
    public String getName() {
        return this.node.getNameAsString();
    }
}
