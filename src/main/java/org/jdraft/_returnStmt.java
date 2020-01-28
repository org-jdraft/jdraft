package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.HashMap;
import java.util.Map;


public class _returnStmt implements _statement<ReturnStmt, _returnStmt> {
    public static _returnStmt of(){
        return new _returnStmt( new ReturnStmt( ));
    }

    public static _returnStmt of( String...code){
        return new _returnStmt(Stmt.returnStmt( code));
    }

    private ReturnStmt rs;

    public _returnStmt(ReturnStmt rs){
        this.rs = rs;
    }

    @Override
    public _returnStmt copy() {
        return new _returnStmt( this.rs.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.returnStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ReturnStmt astNode) {
        return this.rs.equals( astNode);
    }

    public ReturnStmt ast(){
        return rs;
    }

    public _returnStmt removeExpression(){
        this.rs.removeExpression();
        return this;
    }

    public _returnStmt setExpression(Expression e){
        this.rs.setExpression(e);
        return this;
    }

    public Expression getExpression(){
        if(this.rs.getExpression().isPresent()){
            return rs.getExpression().get();
        }
        return null;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        if( rs.getExpression().isPresent()){
            comps.put(_java.Component.EXPRESSION, rs.getExpression().get());
        }
        return comps;
    }
}
