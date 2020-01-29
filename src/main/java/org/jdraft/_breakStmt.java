package org.jdraft;

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BreakStmt;

import java.util.HashMap;
import java.util.Map;


public class _breakStmt implements _statement<BreakStmt, _breakStmt> {

    public static _breakStmt of(){
        return new _breakStmt( new BreakStmt( ).removeLabel());
    }
    public static _breakStmt of(BreakStmt bs){
        return new _breakStmt(bs);
    }
    public static _breakStmt of(String...code){
        return new _breakStmt(Stmt.breakStmt( code));
    }

    private BreakStmt astStmt;

    public _breakStmt(BreakStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _breakStmt copy() {
        return new _breakStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.breakStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public String getLabel(){
        if(this.astStmt.getLabel().isPresent()){
            return this.astStmt.getLabel().get().asString();
        }
        return null;
    }

    public _breakStmt setLabel(String label){
        this.astStmt.setLabel( new SimpleName(label));
        return this;
    }

    public _breakStmt removeLabel(){
        this.astStmt.removeLabel();
        return this;
    }

    @Override
    public boolean is(BreakStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public BreakStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        if( astStmt.getLabel().isPresent()){
            comps.put(_java.Component.LABEL, astStmt.getLabel().get().asString());
        }
        return comps;
    }
}