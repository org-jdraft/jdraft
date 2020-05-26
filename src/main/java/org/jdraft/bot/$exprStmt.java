package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * $bot for inspecting and mutating {@link _exprStmt}s / {@link com.github.javaparser.ast.stmt.ExpressionStmt}s
 */
public class $exprStmt implements $bot.$node<ExpressionStmt, _exprStmt, $exprStmt>,
        $selector.$node<_exprStmt, $exprStmt>,
        $stmt<ExpressionStmt, _exprStmt, $exprStmt>, $bot.$withComment<$exprStmt> {

    public interface $part{}

    public static $exprStmt of(String name ){
        return of( new String[]{name} );
    }

    public static $exprStmt of() {
        return new $exprStmt();
    }

    public static $exprStmt of(_exprStmt _i) {
        return new $exprStmt(_i);
    }

    public static $exprStmt of(ExpressionStmt ile) {
        return new $exprStmt(_exprStmt.of(ile));
    }

    public static $exprStmt of(String... code) {
        return of(_exprStmt.of(code));
    }

    public static $exprStmt of(Exprs.Command lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(Consumer<? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(Supplier<? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(BiConsumer<? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(Exprs.TriConsumer<? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(Exprs.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(Function<? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(BiFunction<? extends Object, ? extends Object,? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(Exprs.TriFunction<? extends Object, ? extends Object,? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    public static $exprStmt of(Exprs.QuadFunction<? extends Object, ? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCall ){
        return from( Thread.currentThread().getStackTrace()[2]);
    }

    private static $exprStmt from(StackTraceElement ste ){
        return of( _exprStmt.from( ste) );
    }

    public Predicate<_exprStmt> predicate = d -> true;

    public $expr expression = $expr.of();

    public $comment comment = null;

    public $exprStmt() { }

    @Override
    public $comment get$Comment() {
        return comment;
    }

    public $exprStmt $not( $exprStmt... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    @Override
    public $exprStmt $hasComment($comment $c) {
        this.comment = $c;
        return this;
    }

    @Override
    public $exprStmt $hardcode(Translator translator, Tokens kvs) {
        this.expression.$hardcode(translator, kvs);
        if( this.comment != null ){
            this.comment.$hardcode(translator, kvs);
        }
        return this;
    }

    public $exprStmt(_exprStmt _r){
        this.expression = $expr.of(_r.getExpression());
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $exprStmt copy(){
        $exprStmt $r = of( ).$and(this.predicate.and(t->true) );
        $r.expression = ($expr)this.expression.copy();
        return $r;
    }

    public Predicate<_exprStmt> getPredicate(){
        return this.predicate;
    }

    public $exprStmt setPredicate(Predicate<_exprStmt> predicate){
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

    public Select<_exprStmt> select(String... code) {
        try {
            return select(_exprStmt.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_exprStmt> select(Node n) {
        if (n instanceof ExpressionStmt) {
            return select(_exprStmt.of((ExpressionStmt) n));
        }
        return null;
    }

    public Select<_exprStmt> select(Statement e) {
        if (e instanceof ExpressionStmt) {
            return select(_exprStmt.of((ExpressionStmt) e));
        }
        return null;
    }

    public Select<_exprStmt> select(_java._domain _n) {
        if (_n instanceof _exprStmt) {
            return select((_exprStmt) _n);
        }
        return null;
    }

    public Select<_exprStmt> select(_stmt<?, ?> _e) {
        if (_e instanceof _exprStmt) {
            return select((_exprStmt) _e);
        }
        return null;
    }

    public Select<_exprStmt> select(_exprStmt _r){

        if( ! this.predicate.test(_r)){
            return null;
        }

        Select s = this.expression.select( _r.getExpression() );
        if( s == null ){
            return null;
        }
        if( this.comment != null ){
            Select c = null;
            try {
                c = this.comment.select((_comment) _r.getComment());
            }catch(Exception e){
                return null;
            }
            if( c == null || !c.tokens.isConsistent(s.tokens)){
                return null;
            }
            s.tokens.putAll(c.tokens);
        }
        return new Select<>(_r, s.tokens);
    }

    public _exprStmt draft(Translator tr, Map<String,Object> keyValues){

        _exprStmt _es = _exprStmt.of();
        if( this.comment != null ){
            _es.setComment(this.comment.draft(tr, keyValues));
        }
        if( !this.expression.isMatchAny() ){
            _es.setExpression( (_expr)this.expression.draft(tr, keyValues) );
        }
        if( !this.predicate.test(_es) ){
            throw new _jdraftException("Drafted $expressionStmt failed predicate");
        }
        return _es;
    }

    @Override
    public $exprStmt $(String target, String $Name) {
        this.expression.$(target, $Name);
        if( this.comment != null ){
            this.comment.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> ps = new ArrayList<>();
        if( this.comment != null ){
            ps.addAll( this.comment.$list() );
        }
        ps.addAll( this.expression.$list());

        return ps;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> ps = new ArrayList<>();
        if( this.comment != null ){
            ps.addAll( this.comment.$listNormalized() );
        }
        ps.addAll( this.expression.$listNormalized());
        return ps.stream().distinct().collect(Collectors.toList());
    }

    public $exprStmt $expression( ){
        this.expression = $expr.of();
        return this;
    }

    public $exprStmt $expression($expr $e ){
        this.expression = $e;
        return this;
    }

    public $exprStmt $expression(Predicate<_expr> matchFn){
        this.expression.$and(matchFn);
        return this;
    }

    public $exprStmt $expression(Class<? extends _expr>...expressionClasses){
        this.expression = $expr.of(expressionClasses);
        return this;
    }

    public $exprStmt $expression(String expression){
        this.expression = $expr.of(expression);
        return this;
    }

    public $exprStmt $expression(Expression e){
        this.expression = $expr.of(e);
        return this;
    }

    public $exprStmt $expression(_expr _e) {
        this.expression = $expr.of(_e);
        return this;
    }
}
