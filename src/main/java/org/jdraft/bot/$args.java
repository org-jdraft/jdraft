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
 * {@link _methodCallExpr}
 * {@link _constant}
 * {@link _constructorCallStmt}
 * {@link _newExpr}
 * NOTE: order matters
 *
 * @param <N>
 */
public class $args<N extends Node & NodeWithArguments>
        implements $bot<N, _args, $args>, $methodCallExpr.$part {

    public static $args of(){
        return new $args();
    }

    /**
     * Build and return a $arguments bot matching empty arguments lists
     * @return $arguments bot that matches empty arguments lists
     */
    public static $args empty(){
        return of().$and( a -> ((_args)a).isEmpty() );
    }

    /**
     * Build and return a $arguments bot matching arguments lists that are not empty
     * @return $arguments bot that matches non-empty arguments lists
     */
    public static $args notEmpty(){
        return of().$and( a-> ! ((_args)a).isEmpty() );
    }

    public static $args of (_args args){
        return new $args(args);
    }

    public static $args of (_expr... args){
        return new $args(args);
    }

    public static $args of(String...args){
        return of( _args.of(args));
    }

    public static $args of($expr...$es){
        return new $args($es);
    }

    public static $args.Or or($args...$as){
        return new $args.Or($as);
    }

    public List<$expr> argumentList = new ArrayList<>();
    public Predicate<_args> predicate = p->true;

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $args copy(){
        $args $args = of( ).$and(this.predicate.and(t->true) );
        this.argumentList.forEach( a -> $args.argumentList.add( a.copy()));
        return $args;
    }

    @Override
    public $args $hardcode(Translator translator, Tokens kvs) {
        this.argumentList.forEach( a -> a.$hardcode(translator, kvs));
        return this;
    }

    public $args(){ }

    public $args(_args args){
        for(int i=0;i<args.size(); i++){
            argumentList.add( $expr.of(args.getAt(i)));
        }
    }

    public $args(_expr..._exs){
        for(int i=0;i<_exs.length; i++){
            argumentList.add( $expr.of(_exs[i]));
        }
    }

    public $args($expr...$exs){
        for(int i=0;i<$exs.length; i++){
            argumentList.add( $exs[i]);
        }
    }

    public $args setPredicate(Predicate<_args> predicate){
        this.predicate = predicate;
        return this;
    }

    public $args $any(Class<? extends _expr>...expressionClasses ){
        Predicate<_args> ps =
                (args) -> Arrays.stream(expressionClasses).anyMatch( ec-> args.get(a -> ec.isAssignableFrom(a.getClass())) != null );
        return $and( ps );
    }


    /**
     * Do ANY of the arguments match this expression predicate
     * @param _exMatchFn
     * @return
     */
    public $args $any(Predicate<_expr> _exMatchFn){
        return $and( a-> a.anyMatch(_exMatchFn));
    }

    /**
     * test that all arguments match the predicate
     * @param _ex
     * @return
     */
    public $args $all(Predicate<_expr> _ex ){
        return $and( p-> p.allMatch(_ex) );
    }

    /**
     * test that all arguments are of some set of _expression types
     * @param ecs
     * @return
     */
    public $args $all(Class<? extends _expr>...ecs){
        return $and( es->es.allMatch(e-> Arrays.stream(ecs).anyMatch( ec-> ec.isAssignableFrom(e.getClass()))));
    }

    public boolean matches(_args _args){
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

    public boolean matches(_expr..._exs){
        return select(_exs) != null;
    }

    public Select<_args> select(Expression...exs){
        return select(_args.of(exs));
    }

    public Select<_args> select(_expr...exs){
        return select(_args.of(exs));
    }

    public Select<_args> select(String args){
        return select(_args.of(args));
    }

    public Select<_args> select(String...args){
        return select(_args.of(args));
    }

    @Override
    public Select<_args> select(Node n) {
        if( n instanceof NodeWithArguments){
            _args _as = _args.of( (NodeWithArguments)n );
            return select(_as);
        }
        return null;
    }

    @Override
    public Select<_args> selectFirstIn(Node astNode, Predicate<Select<_args>> predicate) {
        Optional<Node> on = astNode.stream().filter(n ->{
                if( n instanceof NodeWithArguments ){
                    _args _as = _args.of( (NodeWithArguments)n);
                    Select<_args> sel = select(_as);
                    if( sel != null ){
                        return predicate.test(sel);
                    }
                }
                return false;
             }).findFirst();
        if( on.isPresent()){
            return select(_args.of( (NodeWithArguments)on.get() ));
        }
        return null;
    }

    @Override
    public Predicate<_args> getPredicate() {
        return predicate;
    }

    @Override
    public Select<_args> select(_args candidate) {
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
                //if the only argument is a "matchAny $e" argument,(this means a MATCH ANY EXPRESSION),
                // (not just a matchAny SPECIFIC EXPRESSION like "$int $matchAnyInt = $int.of();" )
                // that means we can match the entire argument list as a token
                else if( this.argumentList.size() == 1 && this.argumentList.get(0).isMatchAny() && this.argumentList.get(0) instanceof $e ){
                    if( this.argumentList.get(0).$list().size() == 0 ){
                        return new Select<>(candidate, new Tokens());
                    }
                    //we are doing this because we want to be able to match
                    // a single $any$ $arguments list, i.e.
                    // $arguments $any = $arguments.of("$any$");

                    // with either a single argument (i.e. one of {(1),(call()),("String")}
                    // or with multiple arguments (args list) ( {(1,2,3), ('c',1), ("str", new Date())}

                    // because the cardinality intent is unclear when we specify "$any$"
                    // (does that mean ANY arguments list, or ANY one-parameter argument?)
                    // we interpret it as the former

                    //to specify ANY one-parameter argument list:
                    // $arguments $oneParam = $arguments.of("$any$").$and(a-> a.size() == 1)

                    //we need to make the entire parameter list
                    this.argumentList.get(0).$list().get(0);
                    Tokens ts = new Tokens();
                    ts.put(this.argumentList.get(0).$list().get(0).toString(), candidate.toString());
                    return new Select<>(candidate, ts);
                } else {
                    return null;
                }
            }
            //if the argument list is empty, means anything passing predicate will do
            return new Select(candidate, new Tokens());
        }
        return null; //didnt match predicate
    }

    @Override
    public boolean isMatchAny() {
        if( this.argumentList.isEmpty() ){
                //|| (this.argumentList.size() == 1
                //&& this.argumentList.get(0) instanceof $e
                //&& this.argumentList.get(0).isMatchAny())){
            try{
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    @Override
    public $args $and(Predicate<_args> matchFn) {
        setPredicate( getPredicate().and(matchFn));
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
    public _args draft(Translator translator, Map<String, Object> keyValues) {
        _args _as = _args.of();
        for(int i=0;i<this.argumentList.size(); i++){
            _as.add( (_expr)this.argumentList.get(i).draft(translator, keyValues) );
        }
        if( this.predicate.test(_as) ){
            return _as;
        }
        throw new _jdraftException("drafted _args does not match predicate "+_as);
    }

    @Override
    public $args $(String target, String $Name) {
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
    public static class Or extends $args {

        public List<$args> $argsBots = new ArrayList<>();

        private Or($args...nms){
            Arrays.stream(nms).forEach(n-> $argsBots.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Select<_args> select(NodeWithArguments nwas){
            return select( _args.of(nwas) );
        }

        public boolean matches(_args candidate){
            return select(candidate) != null;
        }

        /**
         * Unique to OR implementations
         * @param nwa
         * @return
         */
        public $args whichMatch(NodeWithArguments nwa){
            return whichMatch(_args.of(nwa));
        }

        public List<$args> $listOrSelectors() {
            return this.$argsBots;
        }

        /**
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param
         * @return
         */
        public $args whichMatch(_args ae){
            if( !this.predicate.test(ae ) ){
                return null;
            }
            Optional<$args> orsel  = this.$argsBots.stream().filter($p-> $p.matches(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public $args.Or copy(){
            $args.Or $copy = $args.or();
            $copy.$and(this.predicate);
            this.argumentList.forEach( ($a)-> $copy.argumentList.add( (($expr)$a).copy()));
            this.$argsBots.forEach( ($a) -> $copy.$argsBots.add($a.copy()));
            return $copy;
        }

        /**
         *
         * @param _a
         * @return
         */
        public Select<_args> select(_args _a){
            Select commonSelect = super.select(_a);
            if(  commonSelect == null){
                return null;
            }
            $args $whichBot = whichMatch(_a);
            if( $whichBot == null ){
                return null;
            }
            Select whichSelect = $whichBot.select(_a);
            if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$arguments.Or{").append(System.lineSeparator());
            for(int i = 0; i<this.$argsBots.size(); i++){
                sb.append( Text.indent( this.$argsBots.get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }/* Or */
}
