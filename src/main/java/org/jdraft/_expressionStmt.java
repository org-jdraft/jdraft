package org.jdraft;

import com.github.javaparser.ast.stmt.ExpressionStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class _expressionStmt implements _statement<ExpressionStmt, _expressionStmt> {

    public static _expressionStmt of(){
        return new _expressionStmt( new ExpressionStmt( ));
    }
    public static _expressionStmt of(ExpressionStmt es){
        return new _expressionStmt( es);
    }
    public static _expressionStmt of(String...code){
        return new _expressionStmt(Stmt.expressionStmt( code));
    }

    private ExpressionStmt astStmt;

    public _expressionStmt(ExpressionStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _expressionStmt copy() {
        return new _expressionStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.expressionStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getExpression(){
        return _expression.of( this.astStmt.getExpression());
    }

    public _expressionStmt setExpression(_expression _e){
        this.astStmt.setExpression(_e.ast());
        return this;
    }
    @Override
    public boolean is(ExpressionStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ExpressionStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put( _java.Component.EXPRESSION, astStmt.getExpression());
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _expressionStmt ){
            return Objects.equals( ((_expressionStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
