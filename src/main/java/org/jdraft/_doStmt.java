package org.jdraft;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;

import java.util.HashMap;
import java.util.Map;


public class _doStmt implements _statement<DoStmt, _doStmt> {

    public static _doStmt of(){
        return new _doStmt( new DoStmt( ));
    }

    public static _doStmt of(String...code){
        return new _doStmt(Stmt.doStmt( code));
    }

    private DoStmt astStmt;

    public _doStmt(DoStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _doStmt copy() {
        return new _doStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.doStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getCondition(){
        return _expression.of(this.astStmt.getCondition());
    }

    public _body getBody(){
        return _body.of( this.astStmt.getBody() );
    }

    public _doStmt setCondition(_expression e){
        this.astStmt.setCondition(e.ast());
        return this;
    }

    public _doStmt setBody(_statement _st){
        this.astStmt.setBody(_st.ast());
        return this;
    }

    public _doStmt setBody(_body _bd){
        this.astStmt.setBody(_bd.ast());
        return this;
    }

    public _doStmt clearBody(){
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public boolean is(DoStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public DoStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.CONDITION, astStmt.getCondition());
        comps.put(_java.Component.BODY, astStmt.getBody());
        return comps;
    }
}
