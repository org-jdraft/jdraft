package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * The unary expression i.e.
 * ("!done") with prefix operator
 * ("x++") with postfix operator
 *
 */
public class _unary implements _expression<UnaryExpr, _unary> {

    public static _unary of(){
        return new _unary(new UnaryExpr());
    }
    public static _unary of(UnaryExpr ue){
        return new _unary(ue);
    }
    public static _unary of( String...code){
        return new _unary(Ex.unaryEx( code));
    }

    public UnaryExpr unaryEx;

    public _unary(UnaryExpr unaryEx){
        this.unaryEx = unaryEx;
    }

    @Override
    public _unary copy() {
        return new _unary(this.unaryEx.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.unaryEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(UnaryExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public UnaryExpr ast(){
        return unaryEx;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.UNARY_OPERATOR, unaryEx.getOperator());
        comps.put(_java.Component.EXPRESSION, unaryEx.getExpression());
        return comps;
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

    public _unary setExpression(String...expression){
        return setExpression(Ex.of(expression));
    }

    public _unary setExpression(_expression e){
        return setExpression(e.ast());
    }

    public _unary setExpression(Expression e){
        this.unaryEx.setExpression(e);
        return this;
    }

    public _expression getExpression(){
        return _expression.of(this.unaryEx.getExpression());
    }

    public boolean isOperator(Predicate<UnaryExpr.Operator> uo){
        return uo.test(this.unaryEx.getOperator());
    }

    public boolean isOperator(String operator){
        try{
            return isOperator( UnaryExpr.Operator.valueOf(operator));
        }
        catch(Exception e){
            return false;
        }
    }
    public boolean isOperator(UnaryExpr.Operator operator){
        return Objects.equals( this.unaryEx.getOperator(), operator);
    }

    //TODO test
    public _unary setOperator(String operator){
        this.unaryEx.setOperator( UnaryExpr.Operator.valueOf(operator));
        return this;
    }

    public _unary setOperator(UnaryExpr.Operator operator){
        this.unaryEx.setOperator( operator);
        return this;
    }

    public UnaryExpr.Operator getOperator(){
        return this.unaryEx.getOperator();
    }

    public boolean equals(Object other){
        if( other instanceof _unary){
            return ((_unary)other).unaryEx.equals( this.unaryEx);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.unaryEx.hashCode();
    }
    
    public String toString(){
        return this.unaryEx.toString();
    }
}
