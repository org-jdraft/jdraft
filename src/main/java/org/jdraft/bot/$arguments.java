package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithArguments;
import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * bot for argument Lists used in:
 * {@link _methodCall}
 * {@link _constant}
 * {@link _constructorCallStmt}
 * {@link _new}
 * NOTE: order matters
 *
 * @param <N>
 */
public class $arguments<N extends Node & NodeWithArguments>
        implements $bot<N, _arguments, $arguments>, $methodCall.$part {

    public static $arguments of(){
        return new $arguments();
    }


    /**
     * Build and return a $arguments bot matching empty arguments lists
     * @return $arguments bot that matches empty arguments lists
     */
    public static $arguments empty(){
        return of( a-> a.isEmpty() );
    }

    /**
     * Build and return a $arguments bot matching arguments lists that are not empty
     * @return $arguments bot that matches non-empty arguments lists
     */
    public static $arguments notEmpty(){
        return of( a-> !a.isEmpty() );
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

    public static $arguments of( $expression...$es){
        return new $arguments($es);
    }

    public static $arguments of (Predicate<_arguments> predicate){
        return new $arguments(predicate);
    }

    public static $arguments.Or or($arguments...$as){
        return new $arguments.Or($as);
    }

    public List<$expression> argumentList = new ArrayList<>();
    public Predicate<_arguments> predicate = p->true;

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $arguments copy(){
        $arguments $args = of( this.predicate.and(t->true) );
        this.argumentList.forEach( a -> $args.argumentList.add( a.copy()));
        return $args;
    }

    @Override
    public $arguments $hardcode(Translator translator, Tokens kvs) {
        this.argumentList.forEach( a -> a.$hardcode(translator, kvs));
        return this;
    }

    public $arguments(){ }

    public $arguments(Predicate<_arguments> predicate){
        this.predicate = predicate;
    }

    public $arguments(_arguments args){
        for(int i=0;i<args.size(); i++){
            argumentList.add( $expression.of(args.getAt(i)));
        }
    }

    public $arguments(_expression..._exs){
        for(int i=0;i<_exs.length; i++){
            argumentList.add( $expression.of(_exs[i]));
        }
    }

    public $arguments($expression...$exs){
        for(int i=0;i<$exs.length; i++){
            argumentList.add( $exs[i]);
        }
    }

    public $arguments setPredicate( Predicate<_arguments> predicate){
        this.predicate = predicate;
        return this;
    }


    public $arguments $any(Class<? extends _expression>...expressionClasses ){
        //Set<Class<? extends _expression>> exSet = new HashSet<>();
        //Arrays.stream
        Predicate<_arguments> ps =
                (args) -> Arrays.stream(expressionClasses).anyMatch( ec-> args.get(a -> ec.isAssignableFrom(a.getClass())) != null );
        return $and( ps );
                //a-> a.anyMatch( ee ->
                //Arrays.stream(expressionClasses).anyMatch(e -> e.isAssignableFrom(a.getClass()) ) ) );
    }


    /**
     * Do ANY of the arguments match this expression predicate
     * @param _exMatchFn
     * @return
     */
    public $arguments $any( Predicate<_expression> _exMatchFn){
        return $and( a-> a.anyMatch(_exMatchFn));
    }

    /**
     * test that all arguments match the predicate
     * @param _ex
     * @return
     */
    public $arguments $all( Predicate<_expression> _ex ){
        return $and( p-> p.allMatch(_ex) );
    }

    /**
     * test that all arguments are of some set of _expression types
     * @param ecs
     * @return
     */
    public $arguments $all( Class<? extends _expression>...ecs){
        return $and( es->es.allMatch(e-> Arrays.stream(ecs).anyMatch( ec-> ec.isAssignableFrom(e.getClass()))));
    }

    public boolean matches(_arguments _args){
        return select(_args) != null;
    }

    public boolean matches(String args){
        return select(args) != null;
    }

    public boolean matches(String...args){
        return select(args) != null;
    }

    public boolean matches(Expression...exs){
        return select(exs) != null;
    }

    public boolean matches(_expression..._exs){
        return select(_exs) != null;
    }

    public Select<_arguments> select(Expression...exs){
        return select(_arguments.of(exs));
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
        setPredicate( getPredicate().and(matchFn));
        this.predicate = this.predicate.and(matchFn);
        return this;
    }

    public String toString(){
        if( this.isMatchAny() ){
            return "$arguments( $ANY$ )";
        }
        if( this.argumentList.isEmpty() ){
            return "$arguments() {#"+this.hashCode()+"}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append( "$arguments(").append(System.lineSeparator());
        for(int i=0;i<this.argumentList.size();i++){
            //if( i > 0 ){
            //    sb.append(System.lineSeparator());
            //}
            sb.append( Text.indent( this.argumentList.get(i).toString()) );
        }
        sb.append(")");
        return sb.toString();
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
    public List<String> $list() {
        List<String> sts = new ArrayList<>();
        this.argumentList.forEach(a -> sts.addAll( a.$list()));
        return sts;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> sts = new ArrayList<>();
        this.argumentList.forEach(a -> sts.addAll( a.$listNormalized()));
        return sts.stream().distinct().collect(Collectors.toList());
    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $arguments { //implements $selector<_arguments, Or>, $methodCall.$part{

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

        public boolean matches(String args){
            return select(args) != null;
        }

        public boolean matches(String... args){
            return select(args) != null;
        }

        public boolean matches(NodeWithArguments nwa){
            return select(nwa) != null;
        }

        public boolean matches(_expression..._exprs){
            return select(_exprs) != null;
        }

        public boolean matches(Expression...exprs){
            return select(exprs) != null;
        }

        public Select<_arguments> select(String args){
            return select( _arguments.of(args) );
        }

        public Select<_arguments> select(String...args){
            return select( _arguments.of(args) );
        }

        public Select<_arguments> select(NodeWithArguments nwas){
            return select( _arguments.of(nwas) );
        }

        public Select<_arguments> select(_expression..._exprs){
            return select( _arguments.of(_exprs) );
        }

        public Select<_arguments> select(Expression...exprs){
            return select( _arguments.of(exprs) );
        }

        public boolean matches(_arguments candidate){
            return select(candidate) != null;
        }

        /**
         * Unique to OR implementations
         * @param nwa
         * @return
         */
        public $arguments whichMatch(NodeWithArguments nwa){
            return whichMatch(_arguments.of(nwa));
        }

        /**
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param ae
         * @return
         */
        public $arguments whichMatch(_arguments ae){
            if( !this.predicate.test(ae ) ){
                return null;
            }
            Optional<$arguments> orsel  = this.$arguments.stream().filter($p-> $p.matches(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public Tokens parse(_arguments _a){
            $arguments $a = whichMatch(_a);
            if( $a != null) {
                Select s = $a.select(_a);
                if( s != null ){
                    return s.tokens;
                }
            }
            return null;
        }

        @Override
        public Select<_arguments> select(_arguments candidate) {
            $arguments $as = whichMatch(candidate);
            if( $as == null ){
                return null;
            }
            return $as.select(candidate);
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$arguments.Or{").append(System.lineSeparator());
            for(int i=0;i<this.$arguments.size();i++){
                sb.append( Text.indent( this.$arguments.get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }

        /*
        @Override
        public $arguments $and(Predicate<_arguments> matchFn) {
            this.predicate = this.predicate.and(matchFn);
            return ($arguments)this;
        }
         */
    }
}
