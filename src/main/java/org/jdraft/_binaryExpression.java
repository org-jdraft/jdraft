package org.jdraft;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class _binaryExpression implements _expression<BinaryExpr, _binaryExpression> {

    public static _binaryExpression of(){
        return new _binaryExpression( new BinaryExpr());
    }
    public static _binaryExpression of(BinaryExpr be){
        return new _binaryExpression(be);
    }

    public static _binaryExpression of( String...code){
        return new _binaryExpression(Ex.binaryEx( code));
    }

    public static _binaryExpression or(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.OR));
    }
    public static _binaryExpression and(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.AND));
    }
    public static _binaryExpression bitwiseOr(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.BINARY_OR));
    }
    public static _binaryExpression bitwiseAnd(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.BINARY_AND));
    }
    public static _binaryExpression xor(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.XOR));
    }
    public static _binaryExpression equal(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.EQUALS));
    }
    public static _binaryExpression notEqual(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.NOT_EQUALS));
    }
    public static _binaryExpression less(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.LESS));
    }
    public static _binaryExpression greater(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.GREATER));
    }
    public static _binaryExpression lessOrEqual(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.LESS_EQUALS));
    }
    public static _binaryExpression greaterOrEqual(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.GREATER_EQUALS));
    }
    public static _binaryExpression leftShift(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.LEFT_SHIFT));
    }
    public static _binaryExpression rightShiftSigned(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.SIGNED_RIGHT_SHIFT));
    }
    public static _binaryExpression rightShiftUnsigned(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT));
    }
    public static _binaryExpression plus(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.PLUS));
    }
    public static _binaryExpression minus(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.MINUS));
    }
    public static _binaryExpression multiply(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.MULTIPLY));
    }
    public static _binaryExpression divide(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.DIVIDE));
    }
    public static _binaryExpression remainder(String left, String right){
        return new _binaryExpression( new BinaryExpr(Ex.of(left), Ex.of(right), BinaryExpr.Operator.REMAINDER));
    }

    public static _binaryExpression or(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.OR));
    }
    public static _binaryExpression and(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.AND));
    }
    public static _binaryExpression bitwiseOr(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.BINARY_OR));
    }
    public static _binaryExpression bitwiseAnd(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.BINARY_AND));
    }
    public static _binaryExpression xor(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.XOR));
    }
    public static _binaryExpression equal(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.EQUALS));
    }
    public static _binaryExpression notEqual(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.NOT_EQUALS));
    }
    public static _binaryExpression less(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.LESS));
    }
    public static _binaryExpression greater(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.GREATER));
    }
    public static _binaryExpression lessOrEqual(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.LESS_EQUALS));
    }
    public static _binaryExpression greaterOrEqual(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.GREATER_EQUALS));
    }
    public static _binaryExpression leftShift(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.LEFT_SHIFT));
    }
    public static _binaryExpression rightShiftSigned(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.SIGNED_RIGHT_SHIFT));
    }
    public static _binaryExpression rightShiftUnsigned(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT));
    }
    public static _binaryExpression plus(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.PLUS));
    }
    public static _binaryExpression minus(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.MINUS));
    }
    public static _binaryExpression multiply(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.MULTIPLY));
    }
    public static _binaryExpression divide(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.DIVIDE));
    }
    public static _binaryExpression remainder(Expression left, Expression right){
        return new _binaryExpression( new BinaryExpr(left, right, BinaryExpr.Operator.REMAINDER));
    }

    public BinaryExpr astBe;

    public _binaryExpression(BinaryExpr astBe){
        this.astBe = astBe;
    }

    @Override
    public _binaryExpression copy() {
        return new _binaryExpression(this.astBe.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.binaryEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(BinaryExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public BinaryExpr ast(){
        return astBe;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LEFT, astBe.getLeft());
        comps.put(_java.Component.BINARY_OPERATOR, astBe.getOperator());
        comps.put(_java.Component.RIGHT, astBe.getRight());
        return comps;
    }

    public boolean isLeft(String left){
        try{
            return isLeft(Ex.of(left));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isLeft(Expression e){
        return this.getLeft().equals(e);
    }


    public boolean isRight(String right){
        try{
            return isRight(Ex.of(right));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isRight(Expression e){
        return this.getRight().equals(e);
    }

    public boolean isOperator( BinaryExpr.Operator op){
        return Objects.equals( this.getOperator(), op);
    }

    public boolean isOperator( String operator ){
        return Objects.equals( this.getOperator(), UnaryExpr.Operator.valueOf(operator));
    }

    public _expression getLeft(){
        return _expression.of(this.astBe.getLeft());
    }
    public _expression getRight(){
        return _expression.of(this.astBe.getRight());
    }

    public BinaryExpr.Operator getOperator(){
        return astBe.getOperator();
    }

    public _binaryExpression setOperator(BinaryExpr.Operator bo){
        this.astBe.setOperator(bo);
        return this;
    }

    public _binaryExpression setOperator(String o){
        return setOperator(o);
    }

    public _binaryExpression setLeft(Expression e){
        this.astBe.setLeft(e);
        return this;
    }

    public _binaryExpression setLeft(_expression _e){
        return setLeft( _e.ast());
    }

    public _binaryExpression setLeft(String code){
        return setLeft( Ex.of(code));
    }

    public _binaryExpression setRight(Expression e){
        this.astBe.setRight(e);
        return this;
    }

    public _binaryExpression setRight(_expression _e){
        return setRight( _e.ast());
    }

    public _binaryExpression setRight(String code){
        return setRight( Ex.of(code));
    }

    public boolean equals(Object other){
        if( other instanceof _binaryExpression){
            return ((_binaryExpression)other).astBe.equals( this.astBe);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.astBe.hashCode();
    }
    
    public String toString(){
        return this.astBe.toString();
    }
}
