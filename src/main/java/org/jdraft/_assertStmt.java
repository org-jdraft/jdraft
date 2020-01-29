package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.AssertStmt;

import java.util.HashMap;
import java.util.Map;


public class _assertStmt implements _statement<AssertStmt, _assertStmt> {
    public static _assertStmt of(){
        return new _assertStmt( new AssertStmt( ));
    }
    public static _assertStmt of(AssertStmt as){
        return new _assertStmt(as);
    }
    public static _assertStmt of(String...code){
        return new _assertStmt(Stmt.assertStmt( code));
    }

    public static _assertStmt of(Expression check){
        return of( new AssertStmt().setCheck(check));
    }

    public static _assertStmt of(Expression check, String message){
        return of( new AssertStmt().setCheck(check).setMessage(Ex.of(message)));
    }

    public static _assertStmt of(Expression check, Expression message){
        return of( new AssertStmt().setCheck(check).setMessage(message));
    }

    private AssertStmt astStmt;

    public _assertStmt(AssertStmt astStmt){
        this.astStmt = astStmt;
    }

    @Override
    public _assertStmt copy() {
        return new _assertStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.assertStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(AssertStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public AssertStmt ast(){
        return astStmt;
    }

    public _assertStmt setMessage(String message){
        this.astStmt.setMessage(Ex.of(message));
        return this;
    }

    public _assertStmt setMessage(Expression e){
        this.astStmt.setMessage(e);
        return this;
    }

    public _expression getMessage(){
        if( astStmt.getMessage().isPresent()) {
            return _expression.of(astStmt.getMessage().get());
        }
        return null;
    }

    public _assertStmt removeMessage(){
        this.astStmt.removeMessage();
        return this;
    }

    public _assertStmt setCheck(Expression e){
        this.astStmt.setCheck(e);
        return this;
    }

    public _expression getCheck(){
        return _expression.of(astStmt.getCheck());
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.EXPRESSION, astStmt.getCheck() );
        if( astStmt.getMessage().isPresent()){
            comps.put(_java.Component.MESSAGE, astStmt.getMessage().get());
        }
        return comps;
    }
}
