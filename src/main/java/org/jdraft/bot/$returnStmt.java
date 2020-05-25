package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * $bot for selecting, inspecting, drafting & mutating {@link _returnStmt}s / {@link ReturnStmt}s
 */
public class $returnStmt implements $bot.$node<ReturnStmt, _returnStmt, $returnStmt>,
        $selector.$node<_returnStmt, $returnStmt>,
        $stmt<ReturnStmt, _returnStmt, $returnStmt> {

    public interface $part{}

    public static $returnStmt of(String name ){
        return of( new String[]{name} );
    }

    public static $returnStmt of() {
        return new $returnStmt();
    }

    public static $returnStmt of(_returnStmt _i) {
        return new $returnStmt(_i);
    }

    public static $returnStmt of(ReturnStmt ile) {
        return new $returnStmt(_returnStmt.of(ile));
    }

    public static $returnStmt of(String... code) {
        return of(_returnStmt.of(code));
    }

    public Predicate<_returnStmt> predicate = d -> true;

    /** optional expression to return (NOTE could be null)*/
    public $expr expression = $expr.of();

    public $returnStmt() { }

    @Override
    public $returnStmt $hardcode(Translator translator, Tokens kvs) {
        this.expression.$hardcode(translator, kvs);
        return this;
    }

    public $returnStmt(_returnStmt _r){
        if( _r.hasExpression() ){
            this.expression = $expr.of(_r.getExpression());
        }
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $returnStmt copy(){
        $returnStmt $r = of( ).$and(this.predicate.and(t->true) );
        $r.expression = ($expr)this.expression.copy();
        return $r;
    }

    public Predicate<_returnStmt> getPredicate(){
        return this.predicate;
    }

    public $returnStmt setPredicate(Predicate<_returnStmt> predicate){
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

    public Select<_returnStmt> select(String... code) {
        try {
            return select(_returnStmt.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_returnStmt> select(Node n) {
        if (n instanceof ReturnStmt) {
            return select(_returnStmt.of((ReturnStmt) n));
        }
        return null;
    }

    public Select<_returnStmt> select(Statement e) {
        if (e instanceof ReturnStmt) {
            return select(_returnStmt.of((ReturnStmt) e));
        }
        return null;
    }

    public Select<_returnStmt> select(_java._domain _n) {
        if (_n instanceof _returnStmt) {
            return select((_returnStmt) _n);
        }
        return null;
    }

    public Select<_returnStmt> select(_stmt<?, ?> _e) {
        if (_e instanceof _returnStmt) {
            return select((_returnStmt) _e);
        }
        return null;
    }

    public Select<_returnStmt> select(_returnStmt _r){
        if( !_r.hasExpression() && !this.expression.isMatchAny()){
            return null;
        }
        if( ! this.predicate.test(_r)){
            return null;
        }
        Select s = this.expression.select( _r.getExpression() );
        if( s == null ){
            return null;
        }
        return new Select<>(_r, s.tokens);
    }

    public _returnStmt draft(Translator tr, Map<String,Object> keyValues){
        _returnStmt _rs = _returnStmt.of();
        if( !this.expression.isMatchAny() ){
            _rs.setExpression( (_expr)this.expression.draft(tr, keyValues) );
        }
        if( !this.predicate.test(_rs) ){
            throw new _jdraftException("Drafted _returnStmt failed bot predicate");
        }
        return _rs;
    }

    @Override
    public $returnStmt $(String target, String $Name) {
        this.expression.$(target, $Name);
        return this;
    }

    public $returnStmt $not( $returnStmt... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
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

    //$withExpression interface
    public $expr get$expression(){
        return this.expression;
    }

    public $returnStmt $expression( ){
        this.expression = $expr.of();
        return this;
    }

    public $returnStmt $expression($expr $e ){
        this.expression = $e;
        return this;
    }

    public $returnStmt $expression(Predicate<_expr> matchFn){
        this.expression.$and(matchFn);
        return this;
    }

    public $returnStmt $expression(Class<? extends _expr>...expressionClasses){
        this.expression = $expr.of(expressionClasses);
        return this;
    }

    public $returnStmt $expression(String expression){
        this.expression = $expr.of(expression);
        return this;
    }

    public $returnStmt $expression(Expression e){
        this.expression = $expr.of(e);
        return this;
    }

    public $returnStmt $expression(_expr _e) {
        this.expression = $expr.of(_e);
        return this;
    }
}
