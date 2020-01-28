package org.jdraft;

import com.github.javaparser.ast.stmt.ThrowStmt;

import java.util.HashMap;
import java.util.Map;


public class _throwStmt implements _statement<ThrowStmt, _throwStmt> {

    public static _throwStmt of(){
        return new _throwStmt( new ThrowStmt( ));
    }
    public static _throwStmt of(ThrowStmt ts){
        return new _throwStmt(ts);
    }
    public static _throwStmt of(String...code){
        return new _throwStmt(Stmt.throwStmt( code));
    }

    private ThrowStmt astStmt;

    public _throwStmt(ThrowStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _throwStmt copy() {
        return new _throwStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.throwStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getExpression(){
        return _expression.of(astStmt.getExpression());
    }

    public _throwStmt setExpression(_expression _e){
        this.astStmt.setExpression(_e.ast());
        return this;
    }

    @Override
    public boolean is(ThrowStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ThrowStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.EXPRESSION, astStmt.getExpression());
        return comps;
    }
}
