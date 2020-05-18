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

/**
 * Assignment of a field or variable to some value, method return, etc.
 * <PRE>
 * a = b
 * </PRE>
 */
public final class _assignExpr implements _expr<AssignExpr, _assignExpr>, _java._multiPart<AssignExpr, _assignExpr> {

    public static _assignExpr of() {
        return new _assignExpr(new AssignExpr());
    }

    public static _assignExpr of(AssignExpr ae) {
        return new _assignExpr(ae);
    }

    public static _assignExpr of(String... code) {
        return new _assignExpr(Exprs.assignExpr(code));
    }


    public static <A extends Object> _assignExpr of(Exprs.Command c) {
        LambdaExpr le = Exprs.lambdaExpr(Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _assignExpr of(Consumer<A> c) {
        LambdaExpr le = Exprs.lambdaExpr(Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _assignExpr of(BiConsumer<A, B> command) {
        return from(Exprs.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assignExpr of(Exprs.TriConsumer<A, B, C> command) {
        return from(Exprs.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assignExpr of(Exprs.QuadConsumer<A, B, C, D> command) {
        return from(Exprs.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _assignExpr of(Function<A, B> command) {
        return from(Exprs.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assignExpr of(BiFunction<A, B, C> command) {
        return from(Exprs.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assignExpr of(Exprs.TriFunction<A, B, C, D> command) {
        return from(Exprs.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    private static _assignExpr from(LambdaExpr le) {
        Optional<AssignExpr> ows = le.getBody().findFirst(AssignExpr.class);
        if (ows.isPresent()) {
            return of(ows.get());
        }
        throw new _jdraftException("No assignment expression found in lambda");
    }

    public AssignExpr ae;

    public _assignExpr(AssignExpr ae) {
        this.ae = ae;
    }

    @Override
    public _assignExpr copy() {
        return new _assignExpr(this.ae.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try {
            return is(Exprs.assignExpr(stringRep));
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
    public _assignExpr setAssignOperation() {
        return setOperator(AssignExpr.Operator.ASSIGN);
    }

    /**
     * "+="
     *
     * @return
     */
    public _assignExpr setIncrementAssignOperation() {
        return setOperator(AssignExpr.Operator.PLUS);
    }

    /**
     * "-="
     * @return
     */
    public _assignExpr setDecrementAssignOperation() {
        return setOperator(AssignExpr.Operator.MINUS);
    }
    /**
     *  "*="
     */
    public _assignExpr setMultiplyAssignOperation() {
        return setOperator(AssignExpr.Operator.MULTIPLY);
    }

    /**
     * /=
     * @return
     */
    public _assignExpr setDivideAssignOperation() {
        return setOperator(AssignExpr.Operator.DIVIDE);
    }

    /**
     * &=
     *
     * @return
     */
    public _assignExpr setBinaryAndAssignOperation() {
        return setOperator(AssignExpr.Operator.BINARY_AND);
    }

    /**
     * "|="
     * @return
     */
    public _assignExpr setBinaryOrAssignOperation() {
        return setOperator(AssignExpr.Operator.BINARY_OR);
    }

    /**
     * "^="
     * @return
     */
    public _assignExpr setBinaryXorAssignOperation() {
        return setOperator(AssignExpr.Operator.XOR);
    }

    /**
     * "%="
     * @return
     */
    public _assignExpr setRemainderAssignOperation() {
        return setOperator(AssignExpr.Operator.REMAINDER);
    }

    /**
     * "<<="
     * @return
     */
    public _assignExpr setBinaryLeftShiftAssignOperation() {
        return setOperator(AssignExpr.Operator.LEFT_SHIFT);
    }

    /**
     * ">>="
     * @return
     */
    public _assignExpr setBinarySignedRightShiftAssignOperation() {
        return setOperator(AssignExpr.Operator.SIGNED_RIGHT_SHIFT);
    }

    /**
     * ">>>="
     * @return
     */
    public _assignExpr setBinaryUnsignedRightShiftAssignOperation() {
        return setOperator(AssignExpr.Operator.UNSIGNED_RIGHT_SHIFT);
    }





    public _assignExpr setOperator(String op ){
        setOperator( AssignExpr.Operator.valueOf(op));
        return this;
    }

    public _assignExpr setOperator(AssignExpr.Operator op ){
        this.ae.setOperator(op);
        return this;
    }

    public boolean isValue(String str){
        return Objects.equals( this.ae.getValue(), Exprs.of(str));
    }
    public boolean isValue(Expression e){
        return Objects.equals( this.ae.getValue(), e);
    }
    public boolean isValue(_expr e){
        return Objects.equals( this.ae.getValue(), e.ast());
    }

    public boolean isTarget(String str){
        return Objects.equals( this.ae.getTarget(), Exprs.of(str));
    }
    public boolean isTarget(Expression e){
        return Objects.equals( this.ae.getTarget(), e);
    }
    public boolean isTarget(_expr e){
        return Objects.equals( this.ae.getTarget(), e.ast());
    }

    public _assignExpr setTarget(String...target){
        this.ae.setTarget(Exprs.of(target));
        return this;
    }

    public _assignExpr setTarget(_expr _e){
        this.ae.setTarget(_e.ast());
        return this;
    }

    public _assignExpr setTarget(Expression e){
        this.ae.setTarget(e);
        return this;
    }

    public _assignExpr setValue(int value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(float value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(boolean value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(char value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(long value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(int... value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(float... value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(boolean... value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(char... value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(long... value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(String...value){
        this.ae.setValue(Exprs.of(value));
        return this;
    }

    public _assignExpr setValue(_expr _e){
        this.ae.setValue(_e.ast());
        return this;
    }

    public _assignExpr setValue(Expression e){
        this.ae.setValue(e);
        return this;
    }

    public _expr getValue(){
        return _expr.of(this.ae.getValue());
    }

    public _expr getTarget(){
        return _expr.of(this.ae.getTarget());
    }

    public boolean equals(Object other){
        if( other instanceof _assignExpr){
            return ((_assignExpr)other).ae.equals( this.ae);
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
