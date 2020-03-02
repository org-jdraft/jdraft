package org.jdraft;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import java.util.Objects;

public class _variable implements _java._node<VariableDeclarator, _variable>,
        _java._withNameTypeRef<VariableDeclarator, _variable> {

    public static _variable of( String...var){
        return new _variable( Ast.varDecl(var) );
    }

    public static _variable of( VariableDeclarator vd ){
        return new _variable( vd );
    }

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

    public _expression getInit(){
        if( this.vd.getInitializer().isPresent()){
            return _expression.of( vd.getInitializer().get());
        }
        return null;
    }


    public _variable setInit(String...e){
        this.vd.setInitializer(Ex.of(e));
        return this;
    }

    public _variable setInit(Expression e){
        this.vd.setInitializer(e);
        return this;
    }

    public _variable setInit(_expression _e){
        this.vd.setInitializer(_e.ast());
        return this;
    }

    public _variable removeInit(){
        this.vd.removeInitializer();
        return this;
    }

    public _variable setInit(boolean b){
        return setInit(Ex.of(b));
    }

    public _variable setInit(int i){
        return setInit(Ex.of(i));
    }

    public _variable setInit(char c){
        return setInit(Ex.of(c));
    }

    public _variable setInit(float f){
        return setInit(Ex.of(f));
    }

    public _variable setInit(long l){
        return setInit(Ex.of(l));
    }

    public _variable setInit(double d){
        return setInit(Ex.of(d));
    }


    public boolean hasInit(){
        return this.vd.getInitializer().isPresent();
    }

    public boolean isInit(boolean b){
        return isInit(Ex.of(b));
    }

    public boolean isInit(int i){
        return isInit(Ex.of(i));
    }

    public boolean isInit(char c){
        return isInit(Ex.of(c));
    }

    public boolean isInit(float f){
        return isInit(Ex.of(f));
    }

    public boolean isInit(long l){
        return isInit(Ex.of(l));
    }

    public boolean isInit(double d){
        return isInit(Ex.of(d));
    }

    public boolean isInit(String...ex){
        try{
            return isInit(Ex.of(ex));
        }catch(Exception e){
            return false;
        }
    }
    public boolean isInit(_expression _e){
        return isInit(_e.ast());
    }

    public boolean isInit(Expression e){
        return this.vd.getInitializer().isPresent() &&
                Ex.equivalent( this.vd.getInitializer().get(), e);
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

    @Override
    public String getName() {
        return this.vd.getNameAsString();
    }
}
