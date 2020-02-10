package org.jdraft;

import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.ContinueStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public final class _continueStmt implements _statement._controlFlow._signal<ContinueStmt, _continueStmt>,
        _java._simple<ContinueStmt, _continueStmt> {

    public static _continueStmt of(){
        return new _continueStmt( new ContinueStmt( ));
    }
    public static _continueStmt of(ContinueStmt cs){
        return new _continueStmt( cs);
    }
    public static _continueStmt of(String...code){
        return new _continueStmt(Stmt.continueStmt( code));
    }

    public static _continueStmt to(String label){
        return new _continueStmt(new ContinueStmt(label));
    }

    private ContinueStmt astStmt;

    public _continueStmt(ContinueStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _continueStmt copy() {
        return new _continueStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.continueStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public String getLabel(){
        if(this.astStmt.getLabel().isPresent()){
            return this.astStmt.getLabel().get().asString();
        }
        return null;
    }

    public _continueStmt setLabel(String label){
        this.astStmt.setLabel( new SimpleName(label));
        return this;
    }

    public _continueStmt removeLabel(){
        this.astStmt.removeLabel();
        return this;
    }

    @Override
    public boolean is(ContinueStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ContinueStmt ast(){
        return astStmt;
    }

    /*
    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        if( astStmt.getLabel().isPresent()){
            comps.put(_java.Component.LABEL, astStmt.getLabel().get().asString());
        }
        return comps;
    }
     */

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _continueStmt ){
            return Objects.equals( ((_continueStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
