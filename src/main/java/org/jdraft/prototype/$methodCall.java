package org.jdraft.prototype;

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
 *
 */
public class $methodCall implements $prototype.$node<MethodCallExpr, _methodCall, $methodCall>,
        $selector.$node<_methodCall, $methodCall>,
        $expr<MethodCallExpr, _methodCall, $methodCall> {

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

    private static $methodCall addParts( $methodCall $mc, $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $name){
                $mc.name = ($name)parts[i];
            }
            if( parts[i] instanceof $expr){
                $mc.scope = ($expr)parts[i];
            }
            if( parts[i] instanceof $arguments){
                $mc.arguments = ($arguments)parts[i];
            }
            //todo typearguments
        }
        return $mc;
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

    public static $methodCall of(Predicate<_methodCall> _matchFn) {
        return new $methodCall().$and(_matchFn);
    }

    public Predicate<_methodCall> getPredicate(){
        return this.predicate;
    }

    public $methodCall $and(Predicate<_methodCall> _matchFn) {
        this.predicate = this.predicate.and(_matchFn);
        return this;
    }

    public $methodCall $not(Predicate<_methodCall> _matchFn) {
        this.predicate = this.predicate.and(_matchFn.negate());
        return this;
    }

    public Predicate<_methodCall> predicate = d -> true;

    //the parts of the method call
    public $name name = $name.of();
    public $arguments arguments = $arguments.of();
    //todo typeParameters (order doesnt matter)
    public $expr scope = $e.of();

    public $methodCall() {
    }

    public $methodCall(_methodCall _mc){
        name = $name.of(_mc.getName());
        if( _mc.hasScope()){
            scope = $e.of(_mc.getScope());
        }
        if( _mc.hasArguments() ){
            arguments = $arguments.of(_mc.getArguments());
        }
        //todo typeParameters
    }

    public boolean isMatchAny(){
        if( this.name.isMatchAny() && this.scope.isMatchAny() && arguments.isMatchAny()){
            //TODO typeArguments
            try {
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }
    public Selected select(String code) {
        try {
            return select(_methodCall.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(String... code) {
        try {
            return select(_methodCall.of(code));
        } catch (Exception e) {
            return null;
        }
    }

    public Selected select(Node n) {
        if (n instanceof MethodCallExpr) {
            return select(_methodCall.of((MethodCallExpr) n));
        }
        return null;
    }

    public Selected select(Expression e) {
        if (e instanceof MethodCallExpr) {
            return select(_methodCall.of((MethodCallExpr) e));
        }
        return null;
    }

    public Selected select(_java._domain _n) {
        if (_n instanceof _methodCall) {
            return select((_methodCall) _n);
        }
        return null;
    }

    public Selected select(_expression<?, ?> _e) {
        if (_e instanceof _methodCall) {
            return select((_methodCall) _e);
        }
        return null;
    }

    public Selected select(_methodCall _mc){
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
        //todo typeargs
        return new Selected(_mc, ts);
    }

    public _methodCall draft(Translator tr, Map<String,Object> keyValues){
        _methodCall _mc = _methodCall.of();
        _mc.setName(this.name.draft(tr, keyValues));
        if( !this.scope.isMatchAny() ){
            _mc.setScope( (_expression)this.scope.draft(tr, keyValues));
        }
        _mc.setArguments( this.arguments.draft(tr, keyValues));
        //todo typearguments
        return _mc;
    }

    @Override
    public $methodCall $(String target, String $Name) {
        this.name.$(target, $Name);
        this.scope.$(target, $Name);
        this.arguments.$(target, $Name);
        //todo typearguments
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.scope.list$());
        //todo typeArguments
        ps.addAll( this.name.list$());
        ps.addAll(this.arguments.list$());
        return ps;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> ps = new ArrayList<>();
        ps.addAll( this.scope.list$Normalized());
        //todo typeArguments
        ps.addAll( this.name.list$Normalized());
        ps.addAll( this.arguments.list$Normalized());
        return ps.stream().distinct().collect(Collectors.toList());
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

    public $methodCall $arguments($arguments $as){
        this.arguments = $as;
        return this;
    }

    public $methodCall $arguments($expr...args){
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
        this.name.$and(matchFn);
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
    public $expr get$scope(){
        return this.scope;
    }

    public $methodCall $scope( ){
        this.scope = $e.of();
        return this;
    }

    public $methodCall $scope( $expr $e ){
        this.scope = $e;
        return this;
    }

    public $methodCall $scope(Predicate<_expression> matchFn){
        this.scope.$and(matchFn);
        return this;
    }

    public $methodCall $scope(Class<? extends _expression>...expressionClasses){
        this.scope = $e.of(expressionClasses);
        return this;
    }

    public $methodCall $scope(String expression){
        this.scope = $e.of(expression);
        return this;
    }

    public $methodCall $scope(Expression e){
        this.scope = $e.of(e);
        return this;
    }

    public $methodCall $scope(_expression _e){
        this.scope = $e.of(_e);
        return this;
    }

    /**
     * This makes it easier to NOT have to do silly things with generics on the outside
     */
    public static class Selected extends Select<_methodCall> {

        public Selected(_methodCall _node, Tokens tokens) {
            super(_node, tokens);
        }
    }
}
