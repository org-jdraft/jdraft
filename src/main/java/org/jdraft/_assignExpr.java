package org.jdraft;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Assignment of a field or variable to some value, method return, etc.
 * <PRE>
 * a = b
 * </PRE>
 *
 * Left hand side (Target)
 * Right hand side (Value)
 */
public final class _assignExpr implements _expr<AssignExpr, _assignExpr>, _tree._node<AssignExpr, _assignExpr> {

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

    public AssignExpr node;

    public _assignExpr(AssignExpr node) {
        this.node = node;
    }

    public _feature._features<_assignExpr> features(){
        return FEATURES;
    }

    @Override
    public _assignExpr copy() {
        return new _assignExpr(this.node.clone());
    }

    public _assignExpr replace(AssignExpr ae){
        this.node.replace(ae);
        this.node = ae;
        return this;
    }

    @Override
    public boolean is(AssignExpr astNode) {
        return this.node().equals(astNode);
    }

    public AssignExpr node() {
        return node;
    }

    public AssignExpr.Operator getOperator(){
        return this.node.getOperator();
    }

    public boolean isOperator(AssignExpr.Operator ao) {
        return Objects.equals(this.node().getOperator(), ao);
    }

    /** "="*/
    public boolean isAssign() {
        return isOperator(AssignExpr.Operator.ASSIGN);
    }

    /** "+=" */
    public boolean isIncrementAssign() {
        return isOperator(AssignExpr.Operator.PLUS);
    }

    /** "-=" */
    public boolean isDecrementAssign() {
        return isOperator(AssignExpr.Operator.MINUS);
    }

    /** "*="*/
    public boolean isMultiplyAssign() {
        return isOperator(AssignExpr.Operator.MULTIPLY);
    }

    /** /= */
    public boolean isDivideAssign() {
        return isOperator(AssignExpr.Operator.DIVIDE);
    }

    /** &= */
    public boolean isBinaryAndAssign() {
        return isOperator(AssignExpr.Operator.BINARY_AND);
    }

    /** "|="*/
    public boolean isBinaryOrAssign() {
        return isOperator(AssignExpr.Operator.BINARY_OR);
    }

    /** "^=" */
    public boolean isBinaryXorAssign() {
        return isOperator(AssignExpr.Operator.XOR);
    }

    /** "%="*/
    public boolean isRemainderAssign() {
        return isOperator(AssignExpr.Operator.REMAINDER);
    }

    /** "<<="*/
    public boolean isBinaryLeftShiftAssign() {
        return isOperator(AssignExpr.Operator.LEFT_SHIFT);
    }

    /** ">>=" */
    public boolean isBinarySignedRightShiftAssign() {
        return isOperator(AssignExpr.Operator.SIGNED_RIGHT_SHIFT);
    }

    /** ">>>="*/
    public boolean isBinaryUnsignedRightShiftAssign() {
        return isOperator(AssignExpr.Operator.UNSIGNED_RIGHT_SHIFT);
    }

    public _assignExpr setOperator(String op ){
        setOperator( AssignExpr.Operator.valueOf(op));
        return this;
    }

    public _assignExpr setOperator(AssignExpr.Operator op ){
        this.node.setOperator(op);
        return this;
    }

    public boolean isValue(Class<? extends _expr>...expressionTypes ){
        _expr v = getValue();
        return Stream.of(expressionTypes).anyMatch(e-> e.isAssignableFrom(v.getClass()) );
    }

    public boolean isValue(String str){
        return Objects.equals( this.node.getValue(), Expr.of(str));
    }

    public boolean isValue(Expression e){
        return Objects.equals( this.node.getValue(), e);
    }
    public boolean isValue(_expr e){
        return Objects.equals( this.node.getValue(), e.node());
    }

    public boolean isValue(Predicate<_expr> _matchFn){
        return _matchFn.test( getValue() );
    }

    public boolean isTarget(String str){
        return Objects.equals( this.node.getTarget(), Expr.of(str));
    }
    public boolean isTarget(Expression e){
        return Objects.equals( this.node.getTarget(), e);
    }
    public boolean isTarget(_expr e){
        return Objects.equals( this.node.getTarget(), e.node());
    }

    public boolean isTarget(Predicate<_expr> _matchFn){
        return _matchFn.test( getTarget() );
    }

    public _assignExpr setTarget(String...target){
        this.node.setTarget(Expr.of(target));
        return this;
    }

    public _assignExpr setTarget(_expr _e){
        this.node.setTarget(_e.node());
        return this;
    }

    public _assignExpr setTarget(Expression e){
        this.node.setTarget(e);
        return this;
    }

    public _assignExpr setValue(int value){
        this.node.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(float value){
        this.node.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(boolean value){
        this.node.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(char value){
        this.node.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(long value){
        this.node.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(int... value){
        this.node.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(float... value){
        this.node.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(boolean... value){
        this.node.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(char... value){
        this.node.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(long... value){
        this.node.setValue(Expr.arrayInitializerExpr(value));
        return this;
    }

    public _assignExpr setValue(String...value){
        this.node.setValue(Expr.of(value));
        return this;
    }

    public _assignExpr setValue(_expr _e){
        this.node.setValue(_e.node());
        return this;
    }

    public _assignExpr setValue(Expression e){
        this.node.setValue(e);
        return this;
    }

    public _expr getValue(){
        return _expr.of(this.node.getValue());
    }

    public _expr getTarget(){
        return _expr.of(this.node.getTarget());
    }

    public boolean equals(Object other){
        if( other instanceof _assignExpr){
            return ((_assignExpr)other).node.equals( this.node);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public String toString(){
        return this.node.toString();
    }
}
