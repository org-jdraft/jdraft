package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
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

/**
 * Bot for inspecting and mutating {@link _methodCall}s / {@link MethodCallExpr}s
 */
public class $returnStmt implements $bot.$node<ReturnStmt, _returnStmt, $returnStmt>,
        //$bot.$uniBot<ReturnStmt, _returnStmt, $returnStmt>,
        $selector.$node<_returnStmt, $returnStmt>,
        $statement<ReturnStmt, _returnStmt, $returnStmt> {

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

    public static $returnStmt of(Predicate<_returnStmt> _matchFn) {
        return new $returnStmt().$and(_matchFn);
    }

    public Predicate<_returnStmt> predicate = d -> true;

    /** optional expression to return */
    public $expression expression = $expression.of();

    public $returnStmt() { }

    @Override
    public $returnStmt $hardcode(Translator translator, Tokens kvs) {
        this.expression.$hardcode(translator, kvs);
        return this;
    }

    public $returnStmt(_returnStmt _r){
        if( _r.hasExpression() ){
            this.expression = $expression.of(_r.getExpression());
        }
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $returnStmt copy(){
        $returnStmt $r = of( this.predicate.and(t->true) );
        $r.expression = ($expression)this.expression.copy();
        return $r;
    }

    public Predicate<_returnStmt> getPredicate(){
        return this.predicate;
    }

    public $returnStmt setPredicate(Predicate<_returnStmt> predicate){
        this.predicate = predicate;
        return this;
    }

    /*
    public $methodCall $and(Predicate<_methodCall> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $methodCall $not(Predicate<_methodCall> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }
     */

    public boolean isMatchAny(){
        if( this.expression.isMatchAny() ){
            try {
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    /*
    public Selected select(String code) {
        try {
            return select(_methodCall.of(code));
        } catch (Exception e) {
            return null;
        }
    }
     */

    public Selected select(String... code) {
        try {
            return select(_returnStmt.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof ReturnStmt) {
            return select(_returnStmt.of((ReturnStmt) n));
        }
        return null;
    }

    public Selected select(Statement e) {
        if (e instanceof ReturnStmt) {
            return select(_returnStmt.of((ReturnStmt) e));
        }
        return null;
    }

    public Selected select(_java._domain _n) {
        if (_n instanceof _returnStmt) {
            return select((_returnStmt) _n);
        }
        return null;
    }

    public Selected select(_statement<?, ?> _e) {
        if (_e instanceof _returnStmt) {
            return select((_returnStmt) _e);
        }
        return null;
    }

    public Selected select(_returnStmt _r){
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
        return new Selected(_r, s.tokens);
    }

    public _returnStmt draft(Translator tr, Map<String,Object> keyValues){
        _returnStmt _rs = _returnStmt.of();
        if( !this.expression.isMatchAny() ){
            _rs.setExpression( (_expression)this.expression.draft(tr, keyValues) );
        }
        if( !this.predicate.test(_rs) ){
            throw new _jdraftException("Drafted _methodCall failed bot predicate");
        }
        return _rs;
    }

    @Override
    public $returnStmt $(String target, String $Name) {
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

    //$withExpression interface
    public $expression get$expression(){
        return this.expression;
    }

    public $returnStmt $expression( ){
        this.expression = $expression.of();
        return this;
    }

    public $returnStmt $expression($expression $e ){
        this.expression = $e;
        return this;
    }

    public $returnStmt $expression(Predicate<_expression> matchFn){
        this.expression.$and(matchFn);
        return this;
    }

    public $returnStmt $expression(Class<? extends _expression>...expressionClasses){
        this.expression = $expression.of(expressionClasses);
        return this;
    }

    public $returnStmt $expression(String expression){
        this.expression = $expression.of(expression);
        return this;
    }

    public $returnStmt $expression(Expression e){
        this.expression = $expression.of(e);
        return this;
    }

    public $returnStmt $expression(_expression _e) {
        this.expression = $expression.of(_e);
        return this;
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_returnStmt> {

        public Selected(_returnStmt _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}
