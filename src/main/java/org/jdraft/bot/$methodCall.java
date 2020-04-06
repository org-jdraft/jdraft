package org.jdraft.bot;

import org.jdraft.*;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * Bot for inspecting and mutating {@link _methodCall}s / {@link MethodCallExpr}s
 */
public class $methodCall implements $bot.$node<MethodCallExpr, _methodCall, $methodCall>,
        $bot.$multiBot<MethodCallExpr, _methodCall, $methodCall>,
        $selector.$node<_methodCall, $methodCall>,
        $expression<MethodCallExpr, _methodCall, $methodCall> {

    public interface $part{}

    public static $methodCall of( String name ){
        return of( new String[]{name} );
    }

    public static $methodCall of( String name, $part...parts){
        $methodCall $mc = of($name.of(name));
        return addParts($mc, parts);
    }

    public static $methodCall of( $part...parts){
        $methodCall $mc = of();
        return addParts($mc, parts);
    }

    public static $methodCall of() {
        return new $methodCall();
    }

    public static $methodCall of(_methodCall _i) {
        return new $methodCall(_i);
    }

    public static $methodCall of(MethodCallExpr ile) {
        return new $methodCall(_methodCall.of(ile));
    }

    public static $methodCall of(String... code) {
        if( code.length == 1 //singe string
                && code[0].split(" ").length == 1 //single token
                && !code[0].contains("(") ) { //no open parenthesis
                return of().$name(code[0]);
        }
        return of(_methodCall.of(code));
    }

    /* removed to make way for Lambda constructors (this is a "nice to have" anyways)
    public static $methodCall of(Predicate<_methodCall> _matchFn) {
        return new $methodCall().$and(_matchFn);
    }
     */

    private static $methodCall addParts( $methodCall $mc, $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $name){
                $mc.name = ($name)parts[i];
            }
            if( parts[i] instanceof $expression){
                $mc.scope = ($expression)parts[i];
            }
            if( parts[i] instanceof $arguments){
                $mc.arguments = ($arguments)parts[i];
            }
            if( parts[i] instanceof $typeArguments){
                $mc.typeArguments = ($typeArguments)parts[i];
            }
        }
        return $mc;
    }

    public List<$bot> $listBots(){
        List<$bot> bots = new ArrayList();
        bots.add( this.scope );
        bots.add( this.typeArguments );
        bots.add( this.name );
        bots.add( this.arguments );
        return bots;
    }

    public Predicate<_methodCall> predicate = d -> true;

    public $expression scope = $expression.of();
    public $typeArguments typeArguments = $typeArguments.of();
    public $name name = $name.of();
    public $arguments arguments = $arguments.of();

    public $methodCall() { }

    public $methodCall(_methodCall _mc){
        name = $name.of(_mc.getName());
        if( _mc.hasScope()){
            scope = $expression.of(_mc.getScope());
        }
        if( _mc.hasArguments() ){
            arguments = $arguments.of(_mc.getArguments());
        }
        if( _mc.hasTypeArguments() ){
            typeArguments = $typeArguments.of( _mc.getTypeArguments() );
        }
    }

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $methodCall copy(){
        $methodCall $mc = of().$and( this.predicate.and(t->true) );
        $mc.typeArguments = this.typeArguments.copy();
        $mc.arguments = this.arguments.copy();
        $mc.name = this.name.copy();
        $mc.scope = ($expression)this.scope.copy();
        return $mc;
    }

    @Override
    public $methodCall $hardcode(Translator translator, Tokens kvs) {
        typeArguments.$hardcode(translator, kvs);
        arguments.$hardcode(translator, kvs);
        scope.$hardcode(translator, kvs);
        name.$hardcode(translator, kvs);
        return this;
    }

    public Predicate<_methodCall> getPredicate(){
        return this.predicate;
    }

    public $methodCall setPredicate( Predicate<_methodCall> predicate){
        this.predicate = predicate;
        return this;
    }

    public boolean isMatchAny(){
        if( this.name.isMatchAny() && this.scope.isMatchAny() && arguments.isMatchAny() && typeArguments.isMatchAny()){
            try {
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    public Select<_methodCall> select(String... code) {
        try {
            return select(_methodCall.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Select<_methodCall> select(Node n) {
        if (n instanceof MethodCallExpr) {
            return select(_methodCall.of((MethodCallExpr) n));
        }
        return null;
    }

    public Select<_methodCall> select(Expression e) {
        if (e instanceof MethodCallExpr) {
            return select(_methodCall.of((MethodCallExpr) e));
        }
        return null;
    }

    public Select<_methodCall> select(_java._domain _n) {
        if (_n instanceof _methodCall) {
            return select((_methodCall) _n);
        }
        return null;
    }

    public Select<_methodCall> select(_expression<?, ?> _e) {
        if (_e instanceof _methodCall) {
            return select((_methodCall) _e);
        }
        return null;
    }

    public Select<_methodCall> select(_methodCall _mc){
        if( !_mc.hasScope() && !this.scope.isMatchAny()){
            return null;
        }
        if( ! this.predicate.test(_mc)){
            return null;
        }
        Select s = this.scope.select( _mc.getScope() );
        if( s == null ){
            return null;
        }
        Tokens ts = s.tokens;

        s = this.name.select(_mc.getName());
        if( s == null || !ts.isConsistent(s.tokens)){
            return null;
        }
        ts.putAll(s.tokens);

        s = this.arguments.select(_mc.getArguments() );
        if( s == null || !ts.isConsistent(s.tokens)){
            return null;
        }
        ts.putAll(s.tokens);

        s = this.typeArguments.select(_mc.getTypeArguments() );
        if( s == null || !ts.isConsistent(s.tokens)){
            return null;
        }
        ts.putAll(s.tokens);
        return new Select<>(_mc, ts);
    }

    public _methodCall draft(Translator tr, Map<String,Object> keyValues){
        _methodCall _mc = _methodCall.of();
        _mc.setName(this.name.draft(tr, keyValues).name.toString());
        if( !this.scope.isMatchAny() ){
            _mc.setScope( (_expression)this.scope.draft(tr, keyValues));
        }
        _mc.setArguments( this.arguments.draft(tr, keyValues));
        try{
            _mc.setTypeArguments( this.typeArguments.draft(tr, keyValues));
        } catch(Exception e){
            if( !typeArguments.isMatchAny() ){
                throw new _jdraftException("Unable to set type arguments ", e);
            }
        }
        if( !this.predicate.test(_mc) ){
            throw new _jdraftException("Drafted _methodCall failed bot predicate");
        }
        return _mc;
    }

    @Override
    public $methodCall $(String target, String $Name) {
        this.name.$(target, $Name);
        this.scope.$(target, $Name);
        this.arguments.$(target, $Name);
        this.typeArguments.$(target, $Name);
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.scope.$list());
        ps.addAll( this.typeArguments.$list());
        ps.addAll( this.name.$list());
        ps.addAll(this.arguments.$list());
        return ps;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.scope.$listNormalized());
        ps.addAll( this.typeArguments.$listNormalized());
        ps.addAll( this.name.$listNormalized());
        ps.addAll( this.arguments.$listNormalized());
        return ps.stream().distinct().collect(Collectors.toList());
    }

    //$withTypeArguments
    public $typeArguments get$typeArguments(){
        return this.typeArguments;
    }

    public $methodCall $typeArguments(){
        this.typeArguments = $typeArguments.of();
        return this;
    }

    public $methodCall $typeArguments(Predicate<_typeArguments> predicate){
        this.typeArguments.$and(predicate);
        return this;
    }

    public $methodCall $typeArguments($typeArguments $as){
        this.typeArguments = $as;
        return this;
    }

    public $methodCall $typeArguments(String ts){
        this.typeArguments = $typeArguments.of(ts);
        return this;
    }

    public $methodCall $typeArguments(String... ts){
        this.typeArguments = $typeArguments.of(ts);
        return this;
    }

    public $methodCall $typeArguments($typeRef...tas){
        this.typeArguments = $typeArguments.of(tas);
        return this;
    }

    public $methodCall $typeArguments(_typeRef...args){
        this.typeArguments = $typeArguments.of(args);
        return this;
    }

    //$withArguments
    public $arguments get$arguments(){
        return this.arguments;
    }

    public $methodCall $arguments(){
        this.arguments = $arguments.of();
        return this;
    }

    public $methodCall $arguments(Predicate<_arguments> predicate){
        this.arguments.$and(predicate);
        return this;
    }

    public $methodCall $arguments(String args){
        this.arguments = $arguments.of(args);
        return this;
    }

    public $methodCall $arguments(String...args){
        this.arguments = $arguments.of(args);
        return this;
    }

    public $methodCall $arguments($arguments $as){
        this.arguments = $as;
        return this;
    }

    public $methodCall $arguments($expression...args){
        this.arguments = $arguments.of(args);
        return this;
    }

    public $methodCall $arguments(_expression...args){
        this.arguments = $arguments.of(args);
        return this;
    }

    //$withName
    public $name get$name(){
        return this.name;
    }

    public $methodCall $name(){
        this.name = $name.of();
        return this;
    }

    public $methodCall $name(Predicate<String> matchFn){
        this.name.$and(n -> matchFn.test( n.toString()) );
        return this;
    }

    public $methodCall $name(String name){
        this.name = $name.of(name);
        return this;
    }

    public $methodCall $name($name $n){
        this.name = $n;
        return this;
    }

    //$withScope interface
    public $expression get$scope(){
        return this.scope;
    }

    public $methodCall $scope( ){
        this.scope = $expression.of();
        return this;
    }

    public $methodCall $scope( $expression $e ){
        this.scope = $e;
        return this;
    }

    public $methodCall $scope(Predicate<_expression> matchFn){
        this.scope.$and(matchFn);
        return this;
    }

    public $methodCall $scope(Class<? extends _expression>...expressionClasses){
        this.scope = $expression.of(expressionClasses);
        return this;
    }

    public $methodCall $scope(String expression){
        this.scope = $expression.of(expression);
        return this;
    }

    public $methodCall $scope(Expression e){
        this.scope = $expression.of(e);
        return this;
    }

    public $methodCall $scope(_expression _e){
        this.scope = $expression.of(_e);
        return this;
    }

}
