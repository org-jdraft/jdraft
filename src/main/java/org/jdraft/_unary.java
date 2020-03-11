package org.jdraft;

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
public class _unary implements _expression<UnaryExpr, _unary>, _java._multiPart<UnaryExpr, _unary>,
        _java._withExpression<UnaryExpr, _unary> {

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

    // +val
    public boolean isPlusPrefix(){
        return isOperator(UnaryExpr.Operator.PLUS);
    }

    // -val
    public boolean isMinusPrefix() {
        return isOperator(UnaryExpr.Operator.MINUS);
    }

    //++i
    public boolean isPlusPlusPrefix() {
        return isOperator(UnaryExpr.Operator.PREFIX_INCREMENT);
    }
    //--i
    public boolean isMinusMinusPrefix() {
        return isOperator(UnaryExpr.Operator.PREFIX_DECREMENT);
    }
    // !true
    public boolean isNotPrefix() {
        return isOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT);
    }
    // ~bits
    public boolean isBitwiseNegate() {
        return isOperator(UnaryExpr.Operator.BITWISE_COMPLEMENT);
    }
    // i++
    public boolean isPlusPlusPostfix() {
        return isOperator(UnaryExpr.Operator.POSTFIX_INCREMENT);
    }
    // i--
    public boolean isMinusMinusPostfix() {
        return isOperator(UnaryExpr.Operator.POSTFIX_DECREMENT);
    }

    public boolean isOperator(UnaryExpr.Operator operator){
        return Objects.equals( this.unaryEx.getOperator(), operator);
    }

    // +val
    public _unary setPlusPrefix(){
        return setOperator(UnaryExpr.Operator.PLUS);
    }

    // -val
    public _unary setMinusPrefix() {
        return setOperator(UnaryExpr.Operator.MINUS);
    }

    //++i
    public _unary setPlusPlusPrefix() {
        return setOperator(UnaryExpr.Operator.PREFIX_INCREMENT);
    }
    //--i
    public _unary setMinusMinusPrefix() {
        return setOperator(UnaryExpr.Operator.PREFIX_DECREMENT);
    }
    // !true
    public _unary setNotPrefix() {
        return setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT);
    }
    // ~bits
    public _unary setBitwiseNegate() {
        return setOperator(UnaryExpr.Operator.BITWISE_COMPLEMENT);
    }
    // i++
    public _unary setPlusPlusPostfix() {
        return setOperator(UnaryExpr.Operator.POSTFIX_INCREMENT);
    }
    // i--
    public _unary setMinusMinusPostfix() {
        return setOperator(UnaryExpr.Operator.POSTFIX_DECREMENT);
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
