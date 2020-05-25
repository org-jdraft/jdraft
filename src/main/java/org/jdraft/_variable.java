package org.jdraft;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.util.Objects;

public final class _variable implements _java._node<VariableDeclarator, _variable>,
        _java._withNameTypeRef<VariableDeclarator, _variable> {

    public static _variable of(){
        return of( new VariableDeclarator());
    }

    public static _variable of( String...var){
        if( var.length == 0){
            return of( );
        }
        return new _variable( Ast.varDecl(var) );
    }

    public static _variable of( VariableDeclarator vd ){
        return new _variable( vd );
    }

    public static _feature._one<_variable, _typeRef> TYPE = new _feature._one<>(_variable.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getTypeRef(),
            (_variable a, _typeRef _tr) -> a.setTypeRef(_tr));

    public static _feature._one<_variable, String> NAME = new _feature._one<>(_variable.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_variable a, String name) -> a.setName(name));

    public static _feature._one<_variable, _expr> INIT = new _feature._one<>(_variable.class, _expr.class,
            _feature._id.INIT,
            a -> a.getInit(),
            (_variable a, _expr _e) -> a.setInit(_e));

    public static _feature._meta<_variable> META = _feature._meta.of(_variable.class, TYPE, NAME, INIT);

    public VariableDeclarator vd;

    public _variable(VariableDeclarator vd){
        this.vd = vd;
    }

    public _typeRef getTypeRef(){
        return _typeRef.of(this.vd.getType());
    }

    @Override
    public _variable copy() {
        return of( vd.clone());
    }

    @Override
    public VariableDeclarator ast() {
        return vd;
    }

    public boolean equals(Object o){
        if( o instanceof _variable){
            _variable _v = (_variable)o;
            return Objects.equals( this.vd, _v.ast());
        }
        return false;
    }

    /**
     * Is the variable defined as part of a member field (i.e. on a type)
     * @return true if this variable is a child of a Field
     */
    public boolean isField(){
        if( this.vd.getParentNode().isPresent()){
            if( this.vd.getParentNode().get() instanceof VariableDeclarationExpr ){
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isLocal(){
        if( this.vd.getParentNode().isPresent()){
            if( this.vd.getParentNode().get() instanceof VariableDeclarationExpr ){
                return true;
            }
            return false;
        }
        return false; //no parent so we dont know if it's a field or a local var
    }

    public boolean isFinal(){
        if( this.vd.getParentNode().isPresent() &&  this.vd.getParentNode().get() instanceof VariableDeclarationExpr){
            //check if the parent node describing this is
            VariableDeclarationExpr vde = (VariableDeclarationExpr) this.vd.getParentNode().get();
            return vde.isFinal();
        }
        return false;
    }

    public _expr getInit(){
        if( this.vd.getInitializer().isPresent()){
            return _expr.of( vd.getInitializer().get());
        }
        return null;
    }


    public _variable setInit(String...e){
        this.vd.setInitializer(Exprs.of(e));
        return this;
    }

    public _variable setInit(Expression e){
        this.vd.setInitializer(e);
        return this;
    }

    public _variable setInit(_expr _e){
        this.vd.setInitializer(_e.ast());
        return this;
    }

    public _variable removeInit(){
        this.vd.removeInitializer();
        return this;
    }

    public _variable setInit(boolean b){
        return setInit(Exprs.of(b));
    }

    public _variable setInit(int i){
        return setInit(Exprs.of(i));
    }

    public _variable setInit(char c){
        return setInit(Exprs.of(c));
    }

    public _variable setInit(float f){
        return setInit(Exprs.of(f));
    }

    public _variable setInit(long l){
        return setInit(Exprs.of(l));
    }

    public _variable setInit(double d){
        return setInit(Exprs.of(d));
    }


    public boolean hasInit(){
        return this.vd.getInitializer().isPresent();
    }

    public boolean isInit(boolean b){
        return isInit(Exprs.of(b));
    }

    public boolean isInit(int i){
        return isInit(Exprs.of(i));
    }

    public boolean isInit(char c){
        return isInit(Exprs.of(c));
    }

    public boolean isInit(float f){
        return isInit(Exprs.of(f));
    }

    public boolean isInit(long l){
        return isInit(Exprs.of(l));
    }

    public boolean isInit(double d){
        return isInit(Exprs.of(d));
    }

    public boolean isInit(String...ex){
        try{
            return isInit(Exprs.of(ex));
        }catch(Exception e){
            return false;
        }
    }
    public boolean isInit(_expr _e){
        return isInit(_e.ast());
    }

    public boolean isInit(Expression e){
        return this.vd.getInitializer().isPresent() &&
                Exprs.equal( this.vd.getInitializer().get(), e);
    }

    public int hashCode( ){
        return this.vd.hashCode() * 31;
    }

    @Override
    public boolean is(String... stringRep) {
        return of( Ast.varDecl(stringRep)).equals(this);
    }

    @Override
    public _variable setName(String name) {
        this.vd.setName(name);
        return this;
    }

    public String toString(){
        return this.vd.toString();
    }

    public SimpleName getNameNode(){ return this.vd.getName(); }

    @Override
    public String getName() {
        return this.vd.getNameAsString();
    }
}
