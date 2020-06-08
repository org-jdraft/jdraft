package org.jdraft;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Assignment of a field or variable to some value, method return, etc.
 * <PRE>
 * a = b
 * </PRE>
 */
public final class _assignExpr implements _expr<AssignExpr, _assignExpr>, _java._node<AssignExpr, _assignExpr> {

    public static final Function<String, _assignExpr> PARSER = s-> _assignExpr.of(s);

    public static _assignExpr of() {
        return new _assignExpr(new AssignExpr());
    }

    public static _assignExpr of(AssignExpr ae) {
        return new _assignExpr(ae);
    }

    public static _assignExpr of(String... code) {
        return new _assignExpr(Expr.assignExpr(code));
    }


    public static <A extends Object> _assignExpr of(Expr.Command c) {
        LambdaExpr le = Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _assignExpr of(Consumer<A> c) {
        LambdaExpr le = Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _assignExpr of(BiConsumer<A, B> command) {
        return from(Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assignExpr of(Expr.TriConsumer<A, B, C> command) {
        return from(Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assignExpr of(Expr.QuadConsumer<A, B, C, D> command) {
        return from(Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _assignExpr of(Function<A, B> command) {
        return from(Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assignExpr of(BiFunction<A, B, C> command) {
        return from(Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assignExpr of(Expr.TriFunction<A, B, C, D> command) {
        return from(Expr.lambdaExpr(Thread.currentThread().getStackTrace()[2]));
    }

    private static _assignExpr from(LambdaExpr le) {
        Optional<AssignExpr> ows = le.getBody().findFirst(AssignExpr.class);
        if (ows.isPresent()) {
            return of(ows.get());
        }
        throw new _jdraftException("No assignment expression found in lambda");
    }

    public static _feature._one<_assignExpr, _expr> TARGET = new _feature._one<>(_assignExpr.class, _expr.class,
            _feature._id.TARGET,
            a -> a.getTarget(),
            (_assignExpr a, _expr _e) -> a.setTarget(_e), PARSER);

    public static _feature._one<_assignExpr, AssignExpr.Operator> OPERATOR = new _feature._one<>(_assignExpr.class, AssignExpr.Operator.class,
            _feature._id.OPERATOR,
            a -> a.getOperator(),
            (_assignExpr a, AssignExpr.Operator o) -> a.setOperator(o), PARSER);

    public static _feature._one<_assignExpr, _expr> VALUE = new _feature._one<>(_assignExpr.class, _expr.class,
            _feature._id.VALUE,
            a -> a.getValue(),
            (_assignExpr a, _expr _e) -> a.setValue(_e), PARSER);

    public static _feature._features<_assignExpr> FEATURES = _feature._features.of(_assignExpr.class,  PARSER, TARGET, OPERATOR, VALUE);

    public AssignExpr ae;

    public _assignExpr(AssignExpr ae) {
        this.ae = ae;
    }

    public _feature._features<_assignExpr> features(){
        return FEATURES;
    }

    @Override
    public _assignExpr copy() {
        return new _assignExpr(this.ae.clone());
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try {
            return is(Expr.assignExpr(stringRep));
        } catch (Exception e) {
        }
        return false;
    }
     */

    @Override
    public boolean is(AssignExpr astNode) {
        return this.ast().equals(astNode);
    }

    public AssignExpr ast() {
        return ae;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.TARGET_EXPR, ae.getTarget());
        comps.put(_java.Feature.ASSIGN_OPERATOR, ae.getOperator());
        comps.put(_java.Feature.VALUE_EXPR, ae.getValue());
        return comps;
    }
     */

    public AssignExpr.Operator getOperator(){
        return this.ae.getOperator();
    }

    public boolean isOperator(AssignExpr.Operator ao) {
        return Objects.equals(this.ast().getOperator(), ao);
    }

    /**
     * "="
     */
    public boolean isAssign() {
        return isOperator(AssignExpr.Operator.ASSIGN);
    }

    /**
     * "+="
     *
     * @return
     */
    public boolean isIncrementAssign() {
        return isOperator(AssignExpr.Operator.PLUS);
    }

    /**
     * "-="
     * @return
     */
    public boolean isDecrementAssign() {
        return isOperator(AssignExpr.Operator.MINUS);
    }
    /**
     *  "*="
     */
    public boolean isMultiplyAssign() {
        return isOperator(AssignExpr.Operator.MULTIPLY);
    }

    /**
     * /=
     * @return
     */
    public boolean isDivideAssign() {
        return isOperator(AssignExpr.Operator.DIVIDE);
    }

    /**
     * &=
     *
     * @return
     */
    public boolean isBinaryAndAssign() {
        return isOperator(AssignExpr.Operator.BINARY_AND);
    }

    /**
     * "|="
     * @return
     */
    public boolean isBinaryOrAssign() {
        return isOperator(AssignExpr.Operator.BINARY_OR);
    }

    /**
     * "^="
     * @return
     */
    public boolean isBinaryXorAssign() {
        return isOperator(AssignExpr.Operator.XOR);
    }

    /**
     * "%="
     * @return
     */
    public boolean isRemainderAssign() {
        return isOperator(AssignExpr.Operator.REMAINDER);
    }

    /**
     * "<<="
     * @return
     */
    public boolean isBinaryLeftShiftAssign() {
        return isOperator(AssignExpr.Operator.LEFT_SHIFT);
    }

    /**
     * ">>="
     * @return
     */
    public boolean isBinarySignedRightShiftAssign() {
        return isOperator(AssignExpr.Operator.SIGNED_RIGHT_SHIFT);
    }

    /**
     * ">>>="
     * @return
     */
    public boolean isBinaryUnsignedRightShiftAssign() {
        return isOperator(AssignExpr.Operator.UNSIGNED_RIGHT_SHIFT);
    }

    public _assignExpr setOperator(String op ){
        setOperator( AssignExpr.Operator.valueOf(op));
        return this;
    }

    public _assignExpr setOperator(AssignExpr.Operator op ){
        this.ae.setOperator(op);
        return this;
    }

    public boolean isValue(Class<? extends _expr>...expressionTypes ){
        _expr v = getValue();
        return Stream.of(expressionTypes).anyMatch(e-> e.isAssignableFrom(v.getClass()) );
    }

    public boolean isValue(String str){
        return Objects.equals( this.ae.getValue(), Expr.of(str));
    }

    public boolean isValue(Expression e){
        return Objects.equals( this.ae.getValue(), e);
    }
    public boolean isValue(_expr e){
        return Objects.equals( this.ae.getValue(), e.ast());
    }

    public boolean isValue(Predicate<_expr> _matchFn){
        return _matchFn.test( getValue() );
    }

    public boolean isTarget(String str){
        return Objects.equals( this.ae.getTarget(), Expr.of(str));
    }
    public boolean isTarget(Expression e){
        return Objects.equals( this.ae.getTarget(), e);
    }
    public boolean isTarget(_expr e){
        return Objects.equals( this.ae.getTarget(), e.ast());
    }

    public boolean isTarget(Predicate<_expr> _matchFn){
        return _matchFn.test( getTarget() );
    }

    public _assignExpr setTarget(String...target){
        this.ae.setTarget(Expr.of(target));
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
        this.ae.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(float value){
        this.ae.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(boolean value){
        this.ae.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(char value){
        this.ae.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(long value){
        this.ae.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(int... value){
        this.ae.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(float... value){
        this.ae.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(boolean... value){
        this.ae.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(char... value){
        this.ae.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(long... value){
        this.ae.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(String...value){
        this.ae.setValue(Expr.of(value));
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
