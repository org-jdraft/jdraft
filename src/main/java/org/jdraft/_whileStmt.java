package org.jdraft;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

import java.util.HashMap;
import java.util.Map;


public class _whileStmt implements _statement<WhileStmt, _whileStmt> {

    public static _whileStmt of(){
        return new _whileStmt( new WhileStmt( ));
    }
    public static _whileStmt of(WhileStmt ws){
        return new _whileStmt(ws);
    }
    public static _whileStmt of(String...code){
        return new _whileStmt(Stmt.whileStmt( code));
    }

    private WhileStmt astStmt;

    public _whileStmt(WhileStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _whileStmt copy() {
        return new _whileStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.whileStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getCondition(){
        return _expression.of(this.astStmt.getCondition());
    }

    public _body getBody(){
        return _body.of( this.astStmt.getBody() );
    }

    public _whileStmt setCondition(_expression e){
        this.astStmt.setCondition(e.ast());
        return this;
    }

    public _whileStmt setBody(_statement _st){
        this.astStmt.setBody(_st.ast());
        return this;
    }

    public _whileStmt setBody(_body _bd){
        this.astStmt.setBody(_bd.ast());
        return this;
    }

    public _whileStmt clearBody(){
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public boolean is(WhileStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public WhileStmt ast(){
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
