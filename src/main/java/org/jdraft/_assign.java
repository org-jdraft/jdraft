package org.jdraft;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _assign implements _expression<AssignExpr, _assign>, _java._multiPart<AssignExpr, _assign> {

    public static _assign of() {
        return new _assign(new AssignExpr());
    }

    public static _assign of(AssignExpr ae) {
        return new _assign(ae);
    }

    public static _assign of(String... code) {
        return new _assign(Expressions.assignEx(code));
    }


    public static <A extends Object> _assign of(Expressions.Command c) {
        LambdaExpr le = Expressions.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _assign of(Consumer<A> c) {
        LambdaExpr le = Expressions.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _assign of(BiConsumer<A, B> command) {
        return from(Expressions.lambdaEx(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assign of(Expressions.TriConsumer<A, B, C> command) {
        return from(Expressions.lambdaEx(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assign of(Expressions.QuadConsumer<A, B, C, D> command) {
        return from(Expressions.lambdaEx(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _assign of(Function<A, B> command) {
        return from(Expressions.lambdaEx(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assign of(BiFunction<A, B, C> command) {
        return from(Expressions.lambdaEx(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assign of(Expressions.TriFunction<A, B, C, D> command) {
        return from(Expressions.lambdaEx(Thread.currentThread().getStackTrace()[2]));
    }

    private static _assign from(LambdaExpr le) {
        Optional<AssignExpr> ows = le.getBody().findFirst(AssignExpr.class);
        if (ows.isPresent()) {
            return of(ows.get());
        }
        throw new _jdraftException("No assignment expression found in lambda");
    }

    public AssignExpr ae;

    public _assign(AssignExpr ae) {
        this.ae = ae;
    }

    @Override
    public _assign copy() {
        return new _assign(this.ae.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try {
            return is(Expressions.assignEx(stringRep));
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean is(AssignExpr astNode) {
        return this.ast().equals(astNode);
    }

    public AssignExpr ast() {
        return ae;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TARGET, ae.getTarget());
        comps.put(_java.Component.ASSIGN_OPERATOR, ae.getOperator());
        comps.put(_java.Component.VALUE, ae.getValue());
        return comps;
    }
    public boolean isOperator(AssignExpr.Operator ao) {
        return Objects.equals(this.ast().getOperator(), ao);
    }

    /**
     * "="
     */
    public boolean isAssignOperation() {
        return isOperator(AssignExpr.Operator.ASSIGN);
    }

    /**
     * "+="
     *
     * @return
     */
    public boolean isIncrementAssignOperation() {
        return isOperator(AssignExpr.Operator.PLUS);
    }

    /**
     * "-="
     * @return
     */
    public boolean isDecrementAssignOperation() {
        return isOperator(AssignExpr.Operator.MINUS);
    }
    /**
     *  "*="
     */
    public boolean isMultiplyAssignOperation() {
        return isOperator(AssignExpr.Operator.MULTIPLY);
    }

    /**
     * /=
     * @return
     */
    public boolean isDivideAssignOperation() {
        return isOperator(AssignExpr.Operator.DIVIDE);
    }

    /**
     * &=
     *
     * @return
     */
    public boolean isBinaryAndAssignOperation() {
        return isOperator(AssignExpr.Operator.BINARY_AND);
    }

    /**
     * "|="
     * @return
     */
    public boolean isBinaryOrAssignOperation() {
        return isOperator(AssignExpr.Operator.BINARY_OR);
    }

    /**
     * "^="
     * @return
     */
    public boolean isBinaryXorAssignOperation() {
        return isOperator(AssignExpr.Operator.XOR);
    }

    /**
     * "%="
     * @return
     */
    public boolean isRemainderAssignOperation() {
        return isOperator(AssignExpr.Operator.REMAINDER);
    }

    /**
     * "<<="
     * @return
     */
    public boolean isBinaryLeftShiftAssignOperation() {
        return isOperator(AssignExpr.Operator.LEFT_SHIFT);
    }

    /**
     * ">>="
     * @return
     */
    public boolean isBinarySignedRightShiftAssignOperation() {
        return isOperator(AssignExpr.Operator.SIGNED_RIGHT_SHIFT);
    }

    /**
     * ">>>="
     * @return
     */
    public boolean isBinaryUnsignedRightShiftAssignOperation() {
        return isOperator(AssignExpr.Operator.UNSIGNED_RIGHT_SHIFT);
    }

    /**
     * "="
     */
    public _assign setAssignOperation() {
        return setOperator(AssignExpr.Operator.ASSIGN);
    }

    /**
     * "+="
     *
     * @return
     */
    public _assign setIncrementAssignOperation() {
        return setOperator(AssignExpr.Operator.PLUS);
    }

    /**
     * "-="
     * @return
     */
    public _assign setDecrementAssignOperation() {
        return setOperator(AssignExpr.Operator.MINUS);
    }
    /**
     *  "*="
     */
    public _assign setMultiplyAssignOperation() {
        return setOperator(AssignExpr.Operator.MULTIPLY);
    }

    /**
     * /=
     * @return
     */
    public _assign setDivideAssignOperation() {
        return setOperator(AssignExpr.Operator.DIVIDE);
    }

    /**
     * &=
     *
     * @return
     */
    public _assign setBinaryAndAssignOperation() {
        return setOperator(AssignExpr.Operator.BINARY_AND);
    }

    /**
     * "|="
     * @return
     */
    public _assign setBinaryOrAssignOperation() {
        return setOperator(AssignExpr.Operator.BINARY_OR);
    }

    /**
     * "^="
     * @return
     */
    public _assign setBinaryXorAssignOperation() {
        return setOperator(AssignExpr.Operator.XOR);
    }

    /**
     * "%="
     * @return
     */
    public _assign setRemainderAssignOperation() {
        return setOperator(AssignExpr.Operator.REMAINDER);
    }

    /**
     * "<<="
     * @return
     */
    public _assign setBinaryLeftShiftAssignOperation() {
        return setOperator(AssignExpr.Operator.LEFT_SHIFT);
    }

    /**
     * ">>="
     * @return
     */
    public _assign setBinarySignedRightShiftAssignOperation() {
        return setOperator(AssignExpr.Operator.SIGNED_RIGHT_SHIFT);
    }

    /**
     * ">>>="
     * @return
     */
    public _assign setBinaryUnsignedRightShiftAssignOperation() {
        return setOperator(AssignExpr.Operator.UNSIGNED_RIGHT_SHIFT);
    }





    public _assign setOperator( String op ){
        setOperator( AssignExpr.Operator.valueOf(op));
        return this;
    }

    public _assign setOperator( AssignExpr.Operator op ){
        this.ae.setOperator(op);
        return this;
    }

    public boolean isValue(String str){
        return Objects.equals( this.ae.getValue(), Expressions.of(str));
    }
    public boolean isValue(Expression e){
        return Objects.equals( this.ae.getValue(), e);
    }
    public boolean isValue(_expression e){
        return Objects.equals( this.ae.getValue(), e.ast());
    }

    public boolean isTarget(String str){
        return Objects.equals( this.ae.getTarget(), Expressions.of(str));
    }
    public boolean isTarget(Expression e){
        return Objects.equals( this.ae.getTarget(), e);
    }
    public boolean isTarget(_expression e){
        return Objects.equals( this.ae.getTarget(), e.ast());
    }

    public _assign setTarget(String...target){
        this.ae.setTarget(Expressions.of(target));
        return this;
    }

    public _assign setTarget(_expression _e){
        this.ae.setTarget(_e.ast());
        return this;
    }

    public _assign setTarget(Expression e){
        this.ae.setTarget(e);
        return this;
    }

    public _assign setValue(String...value){
        this.ae.setValue(Expressions.of(value));
        return this;
    }

    public _assign setValue(_expression _e){
        this.ae.setValue(_e.ast());
        return this;
    }

    public _assign setValue(Expression e){
        this.ae.setValue(e);
        return this;
    }

    public _expression getValue(){
        return _expression.of(this.ae.getValue());
    }

    public _expression getTarget(){
        return _expression.of(this.ae.getTarget());
    }

    public boolean equals(Object other){
        if( other instanceof _assign){
            return ((_assign)other).ae.equals( this.ae);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ae.hashCode();
    }

    public String toString(){
        return this.ae.toString();
    }
}
