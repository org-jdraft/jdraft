package org.jdraft;

import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;

import java.util.HashMap;
import java.util.Map;


public class _localClassStmt implements _statement<LocalClassDeclarationStmt, _localClassStmt> {

    public static _localClassStmt of(){
        return new _localClassStmt( new LocalClassDeclarationStmt( ));
    }

    public static _localClassStmt of(String...code){
        return new _localClassStmt(Stmt.localClassStmt( code));
    }

    private LocalClassDeclarationStmt astStmt;

    public _localClassStmt(LocalClassDeclarationStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _localClassStmt copy() {
        return new _localClassStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.localClassStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _class get_class(){
        return _class.of( this.astStmt.getClassDeclaration());
    }

    public _localClassStmt set_class(_class _c){
        this.astStmt.setClassDeclaration(_c.ast());
        return this;
    }

    @Override
    public boolean is(LocalClassDeclarationStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public LocalClassDeclarationStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        astStmt.getClassDeclaration();

        comps.put( _java.Component.CLASS, get_class());
        return comps;
    }
}
