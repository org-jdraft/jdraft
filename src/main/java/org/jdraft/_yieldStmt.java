package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.YieldStmt;
import org.jdraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;


public class _yieldStmt implements _statement<YieldStmt, _yieldStmt> {
    public static _yieldStmt of(){
        return new _yieldStmt( new YieldStmt( ));
    }
    public static _yieldStmt of(YieldStmt ys){
        return new _yieldStmt(ys);
    }

    public static _yieldStmt of(String...code){
        String t = Text.combine(code);
        if( t.startsWith("yield ") ){ //they could pass in the whole yield statement string
            return new _yieldStmt(Stmt.yieldStmt( code));
        }
        //they could just pass in the expression
        return of(Ex.of( code));
    }

    public static _yieldStmt of(Expression e){
        return new _yieldStmt( new YieldStmt(e));
    }

    private YieldStmt yieldStmt;

    public _yieldStmt(YieldStmt yieldStmt){
        this.yieldStmt = yieldStmt;
    }

    @Override
    public _yieldStmt copy() {
        return new _yieldStmt( this.yieldStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.yieldStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public YieldStmt ast(){
        return yieldStmt;
    }

    public boolean isExpression(String...expression){
        try{
            return isExpression(Ex.of(expression));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isExpression(_expression _ex){
        return Objects.equals( this.getExpression(), _ex.ast());
    }

    public boolean isExpression(Expression ex){
        return Objects.equals( this.getExpression(), ex);
    }

    public boolean isExpression(Predicate<_expression> matchFn){
        return matchFn.test(getExpression());
    }

    public _yieldStmt setExpression(String...expression){
        return setExpression(Ex.of(expression));
    }

    public _yieldStmt setExpression(_expression e){
        return setExpression(e.ast());
    }

    public _yieldStmt setExpression(Expression e){
        this.yieldStmt.setExpression(e);
        return this;
    }

    public _expression getExpression(){
        return _expression.of(yieldStmt.getExpression());
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.EXPRESSION, yieldStmt.getExpression());
        return comps;
    }
}
