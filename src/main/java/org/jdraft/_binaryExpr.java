package org.jdraft;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.UnaryExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

/**
 * Binary Expresssions
 */
public final class _binaryExpr implements _expr<BinaryExpr, _binaryExpr>,
        _java._multiPart<BinaryExpr, _binaryExpr> {

    /** ALL OPERATORS */

    public static final BinaryExpr.Operator AND = BinaryExpr.Operator.AND;
    public static final BinaryExpr.Operator BINARY_AND = BinaryExpr.Operator.BINARY_AND;
    public static final BinaryExpr.Operator BINARY_OR = BinaryExpr.Operator.BINARY_OR;
    public static final BinaryExpr.Operator DIVIDE = BinaryExpr.Operator.DIVIDE;
    public static final BinaryExpr.Operator EQUALS = BinaryExpr.Operator.EQUALS;
    public static final BinaryExpr.Operator GREATER = BinaryExpr.Operator.GREATER;
    public static final BinaryExpr.Operator GREATER_EQUALS = BinaryExpr.Operator.GREATER_EQUALS;
    public static final BinaryExpr.Operator LEFT_SHIFT = BinaryExpr.Operator.LEFT_SHIFT;
    public static final BinaryExpr.Operator LESS = BinaryExpr.Operator.LESS;
    public static final BinaryExpr.Operator LESS_EQUALS = BinaryExpr.Operator.LESS_EQUALS;
    public static final BinaryExpr.Operator MINUS = BinaryExpr.Operator.MINUS;
    public static final BinaryExpr.Operator MULTIPLY = BinaryExpr.Operator.MULTIPLY;
    public static final BinaryExpr.Operator NOT_EQUALS = BinaryExpr.Operator.NOT_EQUALS;
    public static final BinaryExpr.Operator OR = BinaryExpr.Operator.OR;
    public static final BinaryExpr.Operator PLUS = BinaryExpr.Operator.PLUS;
    public static final BinaryExpr.Operator REMAINDER = BinaryExpr.Operator.REMAINDER;
    public static final BinaryExpr.Operator SIGNED_RIGHT_SHIFT = BinaryExpr.Operator.SIGNED_RIGHT_SHIFT;
    public static final BinaryExpr.Operator UNSIGNED_RIGHT_SHIFT = BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT;
    public static final BinaryExpr.Operator XOR = BinaryExpr.Operator.XOR;

    public static _binaryExpr of(){
        return new _binaryExpr( new BinaryExpr());
    }
    public static _binaryExpr of(BinaryExpr be){
        return new _binaryExpr(be);
    }

    public static _binaryExpr of(String...code){
        return new _binaryExpr(Exprs.binaryExpr( code));
    }

    public static _binaryExpr of(Expression left, BinaryExpr.Operator operator, Expression right ){
        return new _binaryExpr( new BinaryExpr( left, right, operator) );
    }

    public static _binaryExpr of(_expr _left, BinaryExpr.Operator operator, _expr _right ){
        return new _binaryExpr( new BinaryExpr( _left.ast(), _right.ast(), operator) );
    }

    public static <A extends Object> _binaryExpr of(Supplier s){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _binaryExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _binaryExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _binaryExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _binaryExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _binaryExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _binaryExpr of(Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _binaryExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _binaryExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _binaryExpr from(LambdaExpr le){
        Optional<BinaryExpr> ows = le.getBody().findFirst(BinaryExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No binary expression found in lambda");
    }

    public static _binaryExpr or(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.OR));
    }
    public static _binaryExpr and(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.AND));
    }
    public static _binaryExpr bitwiseOr(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.BINARY_OR));
    }
    public static _binaryExpr bitwiseAnd(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.BINARY_AND));
    }
    public static _binaryExpr xor(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.XOR));
    }
    public static _binaryExpr equal(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.EQUALS));
    }
    public static _binaryExpr notEqual(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.NOT_EQUALS));
    }
    public static _binaryExpr less(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.LESS));
    }
    public static _binaryExpr greater(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.GREATER));
    }
    public static _binaryExpr lessOrEqual(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.LESS_EQUALS));
    }
    public static _binaryExpr greaterOrEqual(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.GREATER_EQUALS));
    }
    public static _binaryExpr leftShift(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.LEFT_SHIFT));
    }
    public static _binaryExpr rightShiftSigned(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.SIGNED_RIGHT_SHIFT));
    }
    public static _binaryExpr rightShiftUnsigned(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT));
    }
    public static _binaryExpr plus(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.PLUS));
    }
    public static _binaryExpr minus(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.MINUS));
    }
    public static _binaryExpr multiply(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.MULTIPLY));
    }
    public static _binaryExpr divide(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.DIVIDE));
    }
    public static _binaryExpr remainder(String left, String right){
        return new _binaryExpr( new BinaryExpr(Exprs.of(left), Exprs.of(right), BinaryExpr.Operator.REMAINDER));
    }

    public static _binaryExpr or(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.OR));
    }
    public static _binaryExpr and(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.AND));
    }
    public static _binaryExpr bitwiseOr(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.BINARY_OR));
    }
    public static _binaryExpr bitwiseAnd(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.BINARY_AND));
    }
    public static _binaryExpr xor(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.XOR));
    }
    public static _binaryExpr equal(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.EQUALS));
    }
    public static _binaryExpr notEqual(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.NOT_EQUALS));
    }
    public static _binaryExpr less(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.LESS));
    }
    public static _binaryExpr greater(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.GREATER));
    }
    public static _binaryExpr lessOrEqual(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.LESS_EQUALS));
    }
    public static _binaryExpr greaterOrEqual(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.GREATER_EQUALS));
    }
    public static _binaryExpr leftShift(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.LEFT_SHIFT));
    }
    public static _binaryExpr rightShiftSigned(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.SIGNED_RIGHT_SHIFT));
    }
    public static _binaryExpr rightShiftUnsigned(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT));
    }
    public static _binaryExpr plus(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.PLUS));
    }
    public static _binaryExpr minus(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.MINUS));
    }
    public static _binaryExpr multiply(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.MULTIPLY));
    }
    public static _binaryExpr divide(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.DIVIDE));
    }
    public static _binaryExpr remainder(Expression left, Expression right){
        return new _binaryExpr( new BinaryExpr(left, right, BinaryExpr.Operator.REMAINDER));
    }

    public BinaryExpr astBe;

    public _binaryExpr(BinaryExpr astBe){
        this.astBe = astBe;
    }

    @Override
    public _binaryExpr copy() {
        return new _binaryExpr(this.astBe.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.binaryExpr(stringRep));
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
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.LEFT_EXPR, astBe.getLeft());
        comps.put(_java.Feature.BINARY_OPERATOR, astBe.getOperator());
        comps.put(_java.Feature.RIGHT_EXPR, astBe.getRight());
        return comps;
    }


    public boolean isLeft( Predicate<_expr> matchFn){
        return matchFn.test(getLeft());
    }

    public boolean isLeft(String... left){
        try{
            return isLeft(Exprs.of(left));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isLeft(Expression e){
        return this.getLeft().equals(e);
    }

    public <_E extends _expr> boolean isLeft(Class<_E> _e){
          return _e.isAssignableFrom( getLeft().getClass());
    }

    public boolean isLeft(_expr e){
        return this.getLeft().equals(e.ast());
    }

    public boolean isRight( Predicate<_expr> matchFn){
        return matchFn.test(getRight());
    }

    public boolean isRight(String... right){
        try{
            return isRight(Exprs.of(right));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isRight(Expression e){
        return this.getRight().equals(e);
    }

    public boolean isRight(_expr e){
        return this.getRight().equals(e.ast());
    }

    public <_E extends _expr> boolean isRight(Class<_E> _e){
        return _e.isAssignableFrom( getRight().getClass());
    }

    public boolean isOperator( BinaryExpr.Operator op){
        return Objects.equals( this.getOperator(), op);
    }

    public boolean isOperator( String operator ){
        return Objects.equals( this.getOperator(), UnaryExpr.Operator.valueOf(operator));
    }

    public boolean  isOr(){
        return isOperator(BinaryExpr.Operator.OR);
    }
    public boolean isAnd(){
        return isOperator(BinaryExpr.Operator.AND);
    }
    public boolean isBitwiseOr(){
        return isOperator(BinaryExpr.Operator.BINARY_OR);
    }
    public boolean isBitwiseAnd(){
        return isOperator(BinaryExpr.Operator.BINARY_AND);
    }
    public boolean isXor(){
        return isOperator(BinaryExpr.Operator.XOR);
    }
    public boolean isEqual(){
        return isOperator(BinaryExpr.Operator.EQUALS);
    }
    public boolean isNotEqual(){
        return isOperator(BinaryExpr.Operator.NOT_EQUALS);
    }
    public boolean isLessThan(){
        return isOperator(BinaryExpr.Operator.LESS);
    }
    public boolean isGreaterThan(){
        return isOperator(BinaryExpr.Operator.GREATER);
    }
    public boolean isLessThanOrEqualTo(){
        return isOperator(BinaryExpr.Operator.LESS_EQUALS);
    }
    public boolean isGreaterThanOrEqualTo(){
        return isOperator( BinaryExpr.Operator.GREATER_EQUALS);
    }
    public boolean isLeftShift(){
        return isOperator( BinaryExpr.Operator.LEFT_SHIFT);
    }
    public boolean isRightShiftSigned(){
        return isOperator( BinaryExpr.Operator.SIGNED_RIGHT_SHIFT);
    }
    public boolean isRightShiftUnsigned(){
        return isOperator( BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT);
    }
    public boolean isPlus(){
        return isOperator(BinaryExpr.Operator.PLUS);
    }
    public boolean isMinus(){
        return isOperator(BinaryExpr.Operator.MINUS);
    }
    public boolean isMultiply(){
        return isOperator(BinaryExpr.Operator.MULTIPLY);
    }
    public boolean isDivide(){
        return isOperator(BinaryExpr.Operator.DIVIDE);
    }
    public boolean isRemainder(){
        return isOperator(BinaryExpr.Operator.REMAINDER);
    }

    public _expr getLeft(){
        return _expr.of(this.astBe.getLeft());
    }
    public _expr getRight(){
        return _expr.of(this.astBe.getRight());
    }

    public BinaryExpr.Operator getOperator(){
        return astBe.getOperator();
    }

    public _binaryExpr setOperator(BinaryExpr.Operator bo){
        this.astBe.setOperator(bo);
        return this;
    }

    public _binaryExpr setOperator(String o){
        return setOperator(o);
    }

    public _binaryExpr setOr(){
        return setOperator(BinaryExpr.Operator.OR);
    }
    public _binaryExpr setAnd(){
        return setOperator(BinaryExpr.Operator.AND);
    }
    public _binaryExpr setBitwiseOr(){
        return setOperator(BinaryExpr.Operator.BINARY_OR);
    }
    public _binaryExpr setBitwiseAnd(){
        return setOperator(BinaryExpr.Operator.BINARY_AND);
    }
    public _binaryExpr setXor(){
        return setOperator(BinaryExpr.Operator.XOR);
    }
    public _binaryExpr setEqual(){
        return setOperator(BinaryExpr.Operator.EQUALS);
    }
    public _binaryExpr setNotEqual(){
        return setOperator(BinaryExpr.Operator.NOT_EQUALS);
    }
    public _binaryExpr setLessThan(){
        return setOperator(BinaryExpr.Operator.LESS);
    }
    public _binaryExpr setGreaterThan(){
        return setOperator(BinaryExpr.Operator.GREATER);
    }
    public _binaryExpr setLessThanOrEqualTo(){
        return setOperator(BinaryExpr.Operator.LESS_EQUALS);
    }
    public _binaryExpr setGreaterThanOrEqualTo(){
        return setOperator( BinaryExpr.Operator.GREATER_EQUALS);
    }
    public _binaryExpr setLeftShift(){
        return setOperator( BinaryExpr.Operator.LEFT_SHIFT);
    }
    public _binaryExpr setRightShiftSigned(){
        return setOperator( BinaryExpr.Operator.SIGNED_RIGHT_SHIFT);
    }
    public _binaryExpr setRightShiftUnsigned(){
        return setOperator( BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT);
    }
    public _binaryExpr setPlus(){
        return setOperator(BinaryExpr.Operator.PLUS);
    }
    public _binaryExpr setMinus(){
        return setOperator(BinaryExpr.Operator.MINUS);
    }
    public _binaryExpr setMultiply(){
        return setOperator(BinaryExpr.Operator.MULTIPLY);
    }
    public _binaryExpr setDivide(){
        return setOperator(BinaryExpr.Operator.DIVIDE);
    }
    public _binaryExpr setRemainder(){
        return setOperator(BinaryExpr.Operator.REMAINDER);
    }

    public _binaryExpr setLeft(Expression e){
        this.astBe.setLeft(e);
        return this;
    }

    public _binaryExpr setLeft(_expr _e){
        return setLeft( _e.ast());
    }

    public _binaryExpr setLeft(String... code){
        return setLeft( Exprs.of(code));
    }

    public _binaryExpr setRight(Expression e){
        this.astBe.setRight(e);
        return this;
    }

    public _binaryExpr setRight(_expr _e){
        return setRight( _e.ast());
    }

    public _binaryExpr setRight(String... code){
        return setRight( Exprs.of(code));
    }

    public boolean equals(Object other){
        if( other instanceof _binaryExpr){
            return ((_binaryExpr)other).astBe.equals( this.astBe);
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
