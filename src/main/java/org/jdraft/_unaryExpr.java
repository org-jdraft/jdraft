package org.jdraft;

import com.github.javaparser.ast.expr.UnaryExpr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The unary expression i.e.
 * ("!done") with prefix operator
 * ("x++") with postfix operator
 *
 */
public final class _unaryExpr implements _expr<UnaryExpr, _unaryExpr>, _java._node<UnaryExpr, _unaryExpr>,
        _java._withExpression<UnaryExpr, _unaryExpr> {

    public static final Function<String, _unaryExpr> PARSER = s-> _unaryExpr.of(s);

    public static final UnaryExpr.Operator BITWISE_COMPLEMENT = UnaryExpr.Operator.BITWISE_COMPLEMENT;
    public static final UnaryExpr.Operator LOGICAL_COMPLEMENT = UnaryExpr.Operator.LOGICAL_COMPLEMENT;
    public static final UnaryExpr.Operator MINUS = UnaryExpr.Operator.MINUS;
    public static final UnaryExpr.Operator PLUS = UnaryExpr.Operator.PLUS;
    public static final UnaryExpr.Operator POST_DECREMENT = UnaryExpr.Operator.POSTFIX_DECREMENT;
    public static final UnaryExpr.Operator POST_INCREMENT = UnaryExpr.Operator.POSTFIX_INCREMENT;
    public static final UnaryExpr.Operator PRE_DECREMENT = UnaryExpr.Operator.PREFIX_DECREMENT;
    public static final UnaryExpr.Operator PRE_INCREMENT = UnaryExpr.Operator.PREFIX_INCREMENT;

    public static _unaryExpr of(){
        return new _unaryExpr(new UnaryExpr());
    }
    public static _unaryExpr of(UnaryExpr ue){
        return new _unaryExpr(ue);
    }

    public static _unaryExpr of(UnaryExpr.Operator op, _expr _e){
        UnaryExpr ue = new UnaryExpr();
        ue.setOperator(op);
        ue.setExpression(_e.ast());
        return of(ue);
    }

    public static _unaryExpr of(_expr _e, UnaryExpr.Operator op ){
        UnaryExpr ue = new UnaryExpr();
        ue.setOperator(op);
        ue.setExpression(_e.ast());
        return of(ue);
    }

    public static _unaryExpr of(String...code){
        return new _unaryExpr(Expr.unaryExpr( code));
    }

    public static _feature._one<_unaryExpr, UnaryExpr.Operator> OPERATOR = new _feature._one<>(_unaryExpr.class, UnaryExpr.Operator.class,
            _feature._id.OPERATOR,
            a -> a.getOperator(),
            (_unaryExpr a, UnaryExpr.Operator o) -> a.setOperator(o), PARSER);

    public static _feature._one<_unaryExpr, _expr> EXPRESSION = new _feature._one<>(_unaryExpr.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_unaryExpr a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._features<_unaryExpr> FEATURES = _feature._features.of(_unaryExpr.class, OPERATOR, EXPRESSION)
            .setStrictlyOrdered(false)
            .setFeatureOrder( (_unaryExpr _u, List<_feature<_unaryExpr, ?>> l)->{
                if( _u.isPrefixOperator()){
                    return Stream.of(OPERATOR, EXPRESSION).collect(Collectors.toList());
                }
                return Stream.of(EXPRESSION, OPERATOR).collect(Collectors.toList());
            });

    public UnaryExpr unaryEx;

    public _unaryExpr(UnaryExpr unaryEx){
        this.unaryEx = unaryEx;
    }

    @Override
    public _unaryExpr copy() {
        return new _unaryExpr(this.unaryEx.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.unaryExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public boolean isOperator(String operator ){
        return Objects.equals( getOperator().asString(), operator);
    }

    public boolean isPrefixOperator(String op){
        if( isPrefixOperator() ){
            return Objects.equals( getOperator().asString(), op);
        }
        return false;
    }

    public boolean isPostfixOperator(String op){
        if( isPostfixOperator() ){
            return Objects.equals( getOperator().asString(), op);
        }
        return false;
    }

    public boolean isPrefixOperator(){
        return this.unaryEx.getOperator().isPrefix();
    }

    public boolean isPostfixOperator(){
        return this.unaryEx.getOperator().isPostfix();
    }
    @Override
    public boolean is(UnaryExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public UnaryExpr ast(){
        return unaryEx;
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        comps.put(_java.Feature.UNARY_OPERATOR, unaryEx.getOperator());
        comps.put(_java.Feature.EXPRESSION, unaryEx.getExpression());
        return comps;
    }

    public boolean isOperator(Predicate<UnaryExpr.Operator> uo){
        return uo.test(this.unaryEx.getOperator());
    }

    /*
    public boolean isOperator(String operator){
        try{
            return isOperator( UnaryExpr.Operator.valueOf(operator));
        }
        catch(Exception e){
            return false;
        }
    }
     */

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
    public _unaryExpr setPlusPrefix(){
        return setOperator(UnaryExpr.Operator.PLUS);
    }

    // -val
    public _unaryExpr setMinusPrefix() {
        return setOperator(UnaryExpr.Operator.MINUS);
    }

    //++i
    public _unaryExpr setPlusPlusPrefix() {
        return setOperator(UnaryExpr.Operator.PREFIX_INCREMENT);
    }
    //--i
    public _unaryExpr setMinusMinusPrefix() {
        return setOperator(UnaryExpr.Operator.PREFIX_DECREMENT);
    }
    // !true
    public _unaryExpr setNotPrefix() {
        return setOperator(UnaryExpr.Operator.LOGICAL_COMPLEMENT);
    }
    // ~bits
    public _unaryExpr setBitwiseNegate() {
        return setOperator(UnaryExpr.Operator.BITWISE_COMPLEMENT);
    }
    // i++
    public _unaryExpr setPlusPlusPostfix() {
        return setOperator(UnaryExpr.Operator.POSTFIX_INCREMENT);
    }
    // i--
    public _unaryExpr setMinusMinusPostfix() {
        return setOperator(UnaryExpr.Operator.POSTFIX_DECREMENT);
    }

    //TODO test
    public _unaryExpr setOperator(String operator){
        this.unaryEx.setOperator( UnaryExpr.Operator.valueOf(operator));
        return this;
    }

    public _unaryExpr setOperator(UnaryExpr.Operator operator){
        this.unaryEx.setOperator( operator);
        return this;
    }

    public UnaryExpr.Operator getOperator(){
        return this.unaryEx.getOperator();
    }

    public boolean equals(Object other){
        if( other instanceof _unaryExpr){
            return ((_unaryExpr)other).unaryEx.equals( this.unaryEx);
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
