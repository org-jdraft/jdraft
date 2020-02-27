package org.jdraft;

import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.Objects;

public class _variable implements _java._astNode<VariableDeclarator, _variable>,
        _java._withNameType<VariableDeclarator, _variable> {

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

    public _typeRef getType(){
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
