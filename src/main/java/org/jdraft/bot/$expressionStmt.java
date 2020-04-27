package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * $bot for inspecting and mutating {@link _expressionStmt}s / {@link com.github.javaparser.ast.stmt.ExpressionStmt}s
 */
public class $expressionStmt implements $bot.$node<ExpressionStmt, _expressionStmt, $expressionStmt>,
        $selector.$node<_expressionStmt, $expressionStmt>,
        $statement<ExpressionStmt, _expressionStmt, $expressionStmt> {

    public interface $part{}

    public static $expressionStmt of(String name ){
        return of( new String[]{name} );
    }

    public static $expressionStmt of() {
        return new $expressionStmt();
    }

    public static $expressionStmt of(_expressionStmt _i) {
        return new $expressionStmt(_i);
    }

    public static $expressionStmt of(ExpressionStmt ile) {
        return new $expressionStmt(_expressionStmt.of(ile));
    }

    public static $expressionStmt of(String... code) {
        return of(_expressionStmt.of(code));
    }

    public static $expressionStmt of( Expressions.Command lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( Consumer<? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( Supplier<? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( BiConsumer<? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( Expressions.TriConsumer<? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( Expressions.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( Function<? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( BiFunction<? extends Object, ? extends Object,? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( Expressions.TriFunction<? extends Object, ? extends Object,? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $expressionStmt of( Expressions.QuadFunction<? extends Object, ? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    private static $expressionStmt from( StackTraceElement ste ){
        return of( _expressionStmt.from( ste) );
    }

    public Predicate<_expressionStmt> predicate = d -> true;

    public $expression expression = $expression.of();

    public $expressionStmt() { }

    @Override
    public $expressionStmt $hardcode(Translator translator, Tokens kvs) {
        this.expression.$hardcode(translator, kvs);
        return this;
    }

    public $expressionStmt(_expressionStmt _r){
        this.expression = $expression.of(_r.getExpression());
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $expressionStmt copy(){
        $expressionStmt $r = of( ).$and(this.predicate.and(t->true) );
        $r.expression = ($expression)this.expression.copy();
        return $r;
    }

    public Predicate<_expressionStmt> getPredicate(){
        return this.predicate;
    }

    public $expressionStmt setPredicate(Predicate<_expressionStmt> predicate){
        this.predicate = predicate;
        return this;
    }

    public boolean isMatchAny(){
        if( this.expression.isMatchAny() ){
            try {
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    public Select<_expressionStmt> select(String... code) {
        try {
            return select(_expressionStmt.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_expressionStmt> select(Node n) {
        if (n instanceof ExpressionStmt) {
            return select(_expressionStmt.of((ExpressionStmt) n));
        }
        return null;
    }

    public Select<_expressionStmt> select(Statement e) {
        if (e instanceof ExpressionStmt) {
            return select(_expressionStmt.of((ExpressionStmt) e));
        }
        return null;
    }

    public Select<_expressionStmt> select(_java._domain _n) {
        if (_n instanceof _expressionStmt) {
            return select((_expressionStmt) _n);
        }
        return null;
    }

    public Select<_expressionStmt> select(_statement<?, ?> _e) {
        if (_e instanceof _expressionStmt) {
            return select((_expressionStmt) _e);
        }
        return null;
    }

    public Select<_expressionStmt> select(_expressionStmt _r){

        if( ! this.predicate.test(_r)){
            return null;
        }
        Select s = this.expression.select( _r.getExpression() );
        if( s == null ){
            return null;
        }
        return new Select<>(_r, s.tokens);
    }

    public _expressionStmt draft(Translator tr, Map<String,Object> keyValues){
        _expressionStmt _es = _expressionStmt.of();
        if( !this.expression.isMatchAny() ){
            _es.setExpression( (_expression)this.expression.draft(tr, keyValues) );
        }
        if( !this.predicate.test(_es) ){
            throw new _jdraftException("Drafted $expressionStmt failed predicate");
        }
        return _es;
    }

    @Override
    public $expressionStmt $(String target, String $Name) {
        this.expression.$(target, $Name);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.expression.$list());
        return ps;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.expression.$listNormalized());
        return ps.stream().distinct().collect(Collectors.toList());
    }

    public $expression get$expression(){
        return this.expression;
    }

    public $expressionStmt $expression( ){
        this.expression = $expression.of();
        return this;
    }

    public $expressionStmt $expression($expression $e ){
        this.expression = $e;
        return this;
    }

    public $expressionStmt $expression(Predicate<_expression> matchFn){
        this.expression.$and(matchFn);
        return this;
    }

    public $expressionStmt $expression(Class<? extends _expression>...expressionClasses){
        this.expression = $expression.of(expressionClasses);
        return this;
    }

    public $expressionStmt $expression(String expression){
        this.expression = $expression.of(expression);
        return this;
    }

    public $expressionStmt $expression(Expression e){
        this.expression = $expression.of(e);
        return this;
    }

    public $expressionStmt $expression(_expression _e) {
        this.expression = $expression.of(_e);
        return this;
    }
}
