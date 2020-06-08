package org.jdraft;

import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The ternary conditional expression.
 * In <code>b==0?x:y</code>, b==0 is the condition, x is thenExpr, and y is elseExpr.
 */
public final class _ternaryExpr implements _expr<ConditionalExpr, _ternaryExpr>,
        _java._withCondition<ConditionalExpr, _ternaryExpr>,
        _tree._node<ConditionalExpr, _ternaryExpr> {

    public static final Function<String, _ternaryExpr> PARSER = s-> _ternaryExpr.of(s);

    public static _ternaryExpr of(){
        return new _ternaryExpr( new ConditionalExpr( ));
    }
    public static _ternaryExpr of(ConditionalExpr ce){
        return new _ternaryExpr(ce);
    }
    public static _ternaryExpr of(String...code){
        return new _ternaryExpr(Expr.ternaryExpr( code));
    }

    public static <A extends Object> _ternaryExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _ternaryExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _ternaryExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _ternaryExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _ternaryExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _ternaryExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _ternaryExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _ternaryExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _ternaryExpr from(LambdaExpr le){
        Optional<ConditionalExpr> ows = le.getBody().findFirst(ConditionalExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No ternary expression found in lambda");
    }

    public static _feature._one<_ternaryExpr, _expr> CONDITION = new _feature._one<>(_ternaryExpr.class, _expr.class,
            _feature._id.CONDITION,
            a -> a.getCondition(),
            (_ternaryExpr p, _expr _es) -> p.setCondition(_es), PARSER);

    public static _feature._one<_ternaryExpr, _expr> THEN = new _feature._one<>(_ternaryExpr.class, _expr.class,
            _feature._id.THEN,
            a -> a.getThen(),
            (_ternaryExpr p, _expr _es) -> p.setThen(_es), PARSER);

    public static _feature._one<_ternaryExpr, _expr> ELSE = new _feature._one<>(_ternaryExpr.class, _expr.class,
            _feature._id.ELSE,
            a -> a.getElse(),
            (_ternaryExpr p, _expr _es) -> p.setElse(_es), PARSER);

    public static _feature._features<_ternaryExpr> FEATURES = _feature._features.of(_ternaryExpr.class, PARSER,  CONDITION, THEN, ELSE );

    public _feature._features<_ternaryExpr> features(){
        return FEATURES;
    }

    public ConditionalExpr ce;

    public _ternaryExpr(ConditionalExpr ce){
        this.ce = ce;
    }

    @Override
    public _ternaryExpr copy() {
        return new _ternaryExpr(this.ce.clone());
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.ternaryExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }
     */

    @Override
    public boolean is(ConditionalExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ConditionalExpr ast(){
        return ce;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        comps.put(_java.Feature.CONDITION_EXPR, ce.getCondition());
        comps.put(_java.Feature.THEN_EXPR, ce.getThenExpr());
        comps.put(_java.Feature.ELSE_EXPR, ce.getElseExpr());
        return comps;
    }
     */

    public boolean isThen( String...expr){
        return isThen( Expr.of(expr));
    }

    public boolean isThen( _expr _e){
        return isThen(_e.ast());
    }

    public boolean isThen( Expression e){
        return Objects.equals( this.ce.getCondition(), e);
    }

    public _ternaryExpr setThen(String...expr){
        this.ce.setThenExpr(Expr.of(expr));
        return this;
    }

    public _ternaryExpr setThen(_expr _e){
        this.ce.setThenExpr(_e.ast());
        return this;
    }

    public _ternaryExpr setThen(Expression e){
        this.ce.setThenExpr( e );
        return this;
    }

    public _ternaryExpr setElse(_expr _e){
        this.ce.setElseExpr(_e.ast());
        return this;
    }

    public boolean isElse( String...expr){
        return isElse( Expr.of(expr));
    }

    public boolean isElse( _expr _e){
        return isElse(_e.ast());
    }

    public boolean isElse( Expression e){
        return Objects.equals( this.ce.getCondition(), e);
    }

    public _ternaryExpr setElse(Expression e){
        this.ce.setElseExpr( e );
        return this;
    }

    public _ternaryExpr setElse(String...expr){
        this.ce.setElseExpr(Expr.of(expr));
        return this;
    }

    public _expr getThen(){
        return _expr.of(this.ce.getThenExpr());
    }

    public _expr getElse(){
        return _expr.of(this.ce.getElseExpr());
    }

    public boolean equals(Object other){
        if( other instanceof _ternaryExpr){
            return ((_ternaryExpr)other).ce.equals( this.ce);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ce.hashCode();
    }
    
    public String toString(){
        return this.ce.toString();
    }
}
