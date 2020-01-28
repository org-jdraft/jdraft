package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.YieldStmt;

import java.util.HashMap;
import java.util.Map;


public class _yieldStmt implements _statement<YieldStmt, _yieldStmt> {
    public static _yieldStmt of(){
        return new _yieldStmt( new YieldStmt( ));
    }

    public static _yieldStmt of(String...code){
        return new _yieldStmt(Stmt.yieldStmt( code));
    }

    private YieldStmt rs;

    public _yieldStmt(YieldStmt rs){
        this.rs = rs;
    }

    @Override
    public _yieldStmt copy() {
        return new _yieldStmt( this.rs.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.yieldStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(YieldStmt astNode) {
        return this.rs.equals( astNode);
    }

    public YieldStmt ast(){
        return rs;
    }

    public _yieldStmt setExpression(Expression e){
        this.rs.setExpression(e);
        return this;
    }

    public _expression getExpression(){
        return _expression.of(rs.getExpression());
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.EXPRESSION, rs.getExpression());
        return comps;
    }
}
