package org.jdraft;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.IfStmt;

import java.util.HashMap;
import java.util.Map;


public class _ifStmt implements _statement<IfStmt, _ifStmt> {

    public static _ifStmt of(){
        return new _ifStmt( new IfStmt( ));
    }

    public static _ifStmt of(String...code){
        return new _ifStmt(Stmt.ifStmt( code));
    }

    private IfStmt astStmt;

    public _ifStmt(IfStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _ifStmt copy() {
        return new _ifStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.ifStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getCondition(){
        return _expression.of(this.astStmt.getCondition());
    }

    public _statement getThen(){
        return _statement.of( this.astStmt.getThenStmt() );
    }

    public _statement getElse(){
        if( this.astStmt.getElseStmt().isPresent()) {
            return _statement.of(this.astStmt.getElseStmt().get());
        }
        return null;
    }

    public _ifStmt setCondition(_expression e){
        this.astStmt.setCondition(e.ast());
        return this;
    }

    public _ifStmt setThen(_statement _st){
        this.astStmt.setThenStmt(_st.ast());
        return this;
    }

    public _ifStmt setThen(_body _bd){
        this.astStmt.setThenStmt(_bd.ast());
        return this;
    }

    public _ifStmt setElse(_statement _st){
        this.astStmt.setElseStmt(_st.ast());
        return this;
    }

    public _ifStmt setElse(_body _bd){
        this.astStmt.setElseStmt(_bd.ast());
        return this;
    }

    @Override
    public boolean is(IfStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public IfStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.CONDITION, astStmt.getCondition());
        comps.put(_java.Component.THEN, astStmt.getThenStmt());
        if( this.astStmt.getElseStmt().isPresent()) {
            comps.put(_java.Component.ELSE, astStmt.getElseStmt().get());
        }
        return comps;
    }
}
