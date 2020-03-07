package org.jdraft.prototype;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import org.jdraft._arguments;
import org.jdraft._expression;
import org.jdraft._jdraftException;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class $arguments<N extends Node & NodeWithArguments>
        implements $prototype<N, _arguments, $arguments>, $methodCall.$part {

    public static $arguments of(){
        return new $arguments();
    }

    public static $arguments of (_arguments args){
        return new $arguments(args);
    }

    public static $arguments of (_expression... args){
        return new $arguments(args);
    }

    public static $arguments of( String...args){
        return of( _arguments.of(args));
    }

    public static $arguments of( $expr ...$es){
        return new $arguments($es);
    }

    public static $arguments of (Predicate<_arguments> predicate){
        return new $arguments(predicate);
    }

    public static $arguments.Or or($arguments...$as){
        return new $arguments.Or($as);
    }

    public List<$expr> argumentList = new ArrayList<>();
    public Predicate<_arguments> predicate = p->true;

    public $arguments(){ }

    public $arguments(Predicate<_arguments> predicate){
        this.predicate = predicate;
    }

    public $arguments(_arguments args){
        for(int i=0;i<args.size(); i++){
            argumentList.add( $e.of(args.getAt(i)));
        }
    }

    public $arguments(_expression..._exs){
        for(int i=0;i<_exs.length; i++){
            argumentList.add( $e.of(_exs[i]));
        }
    }

    public $arguments($expr...$exs){
        for(int i=0;i<$exs.length; i++){
            argumentList.add( $exs[i]);
        }
    }

    public boolean matches(String args){
        return select(args) != null;
    }

    public boolean matches(String...args){
        return select(args) != null;
    }

    public boolean matches(_expression..._exs){
        return select(_exs) != null;
    }

    public Select<_arguments> select(_expression...exs){
        return select(_arguments.of(exs));
    }

    public Select<_arguments> select(String args){
        return select(_arguments.of(args));
    }

    public Select<_arguments> select(String...args){
        return select(_arguments.of(args));
    }

    @Override
    public Select<_arguments> select(Node n) {
        if( n instanceof NodeWithArguments){
            _arguments _as = _arguments.of( (NodeWithArguments)n );
            return select(_as);
        }
        return null;
    }

    @Override
    public Select<_arguments> selectFirstIn(Node astNode, Predicate<Select<_arguments>> predicate) {
        Optional<Node> on = astNode.stream().filter(n ->{
                if( n instanceof NodeWithArguments ){
                    _arguments _as = _arguments.of( (NodeWithArguments)n);
                    Select<_arguments> sel = select(_as);
                    if( sel != null ){
                        return predicate.test(sel);
                    }
                }
                return false;
             }).findFirst();
        if( on.isPresent()){
            return select(_arguments.of( (NodeWithArguments)on.get() ));
        }
        return null;
    }

    @Override
    public Predicate<_arguments> getPredicate() {
        return predicate;
    }

    @Override
    public Select<_arguments> select(_arguments candidate) {
        if( isMatchAny()){

            return new Select(candidate, new Tokens());
        }
        if( this.predicate.test(candidate)){
            if( !this.argumentList.isEmpty()) { //they have spelled out individual arguments
                if( this.argumentList.size() == candidate.size() ){
                    Tokens ts = new Tokens();
                    for( int i=0;i<this.argumentList.size(); i++){
                        Select s = argumentList.get(i).select(candidate.getAt(i));
                        if( s == null || !ts.isConsistent(s.tokens)){
                            return null;
                        }
                        ts.putAll(s.tokens);
                    }
                    return new Select(candidate, ts);
                }
                return null;
            }
            return new Select(candidate, new Tokens());
        }
        return null;
    }

    @Override
    public boolean isMatchAny() {
        if( this.argumentList.isEmpty() || (this.argumentList.size() == 1
                && this.argumentList.get(0) instanceof $e
                && this.argumentList.get(0).isMatchAny())){
            try{
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    @Override
    public $arguments $and(Predicate<_arguments> matchFn) {
        this.predicate = this.predicate.and(matchFn);
        return this;
    }

    @Override
    public _arguments draft(Translator translator, Map<String, Object> keyValues) {
        _arguments _as = _arguments.of();
        for(int i=0;i<this.argumentList.size(); i++){
            _as.add( (_expression)this.argumentList.get(i).draft(translator, keyValues) );
        }
        if( this.predicate.test(_as) ){
            return _as;
        }
        throw new _jdraftException("drafted _args does not match predicate "+_as);
    }

    @Override
    public $arguments $(String target, String $Name) {
        this.argumentList.forEach(e -> e.$(target, $Name));
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> sts = new ArrayList<>();
        this.argumentList.forEach(a -> sts.addAll( a.list$()));
        return sts;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> sts = new ArrayList<>();
        this.argumentList.forEach(a -> sts.addAll( a.list$Normalized()));
        return sts.stream().distinct().collect(Collectors.toList());
    }

    /**
     * An Or entity that can match against any of some number of instances
     */
    public static class Or implements $selector<_arguments, Or>, $methodCall.$part{

        public Predicate<_arguments> predicate = p-> true;

        public List<$arguments> $arguments = new ArrayList<>();

        private Or($arguments...nms){
            Arrays.stream(nms).forEach(n-> $arguments.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Predicate<_arguments> getPredicate(){
            return this.predicate;
        }

        @Override
        public Select<_arguments> select(_arguments candidate) {
            if( predicate.test(candidate) ) {
                Optional<$arguments> on = $arguments.stream().filter(n -> n.matches(candidate)).findFirst();
                if (on.isPresent()) {
                    return on.get().select(candidate);
                }
            }
            return null;
        }

        @Override
        public Or $and(Predicate<_arguments> matchFn) {
            this.predicate = this.predicate.and(matchFn);
            return null;
        }
    }
}
