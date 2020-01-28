package org.jdraft;

import com.github.javaparser.ast.stmt.EmptyStmt;

import java.util.HashMap;
import java.util.Map;

/**
 * just ";" (i.e. "for(int i=0;;;)")
 */
public class _emptyStmt implements _statement<EmptyStmt, _emptyStmt> {

    public static _emptyStmt of(){
        return new _emptyStmt( new EmptyStmt( ));
    }
    public static _emptyStmt of(EmptyStmt es){
        return new _emptyStmt( es);
    }
    public static _emptyStmt of(String...code){
        return new _emptyStmt(Stmt.emptyStmt( code));
    }

    private EmptyStmt astStmt;

    public _emptyStmt(EmptyStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _emptyStmt copy() {
        return new _emptyStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.emptyStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(EmptyStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public EmptyStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        return comps;
    }
}
