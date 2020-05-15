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
public class $arguments<N extends Node & NodeWithArguments>
        implements $bot<N, _arguments, $arguments>, $methodCallExpr.$part {

    public static $arguments of(){
        return new $arguments();
    }

    /**
     * Build and return a $arguments bot matching empty arguments lists
     * @return $arguments bot that matches empty arguments lists
     */
    public static $arguments empty(){
        return of().$and( a -> ((_arguments)a).isEmpty() );
    }

    /**
     * Build and return a $arguments bot matching arguments lists that are not empty
     * @return $arguments bot that matches non-empty arguments lists
     */
    public static $arguments notEmpty(){
        return of().$and( a-> ! ((_arguments)a).isEmpty() );
    }

    public static $arguments of (_arguments args){
        return new $arguments(args);
    }

    public static $arguments of (_expr... args){
        return new $arguments(args);
    }

    public static $arguments of( String...args){
        return of( _arguments.of(args));
    }

    public static $arguments of( $expr...$es){
        return new $arguments($es);
    }

    public static $arguments.Or or($arguments...$as){
        return new $arguments.Or($as);
    }

    public List<$expr> argumentList = new ArrayList<>();
    public Predicate<_arguments> predicate = p->true;

    /**
     * Build and return a new mutable copy of this bot
     * @return
     */
    public $arguments copy(){
        $arguments $args = of( ).$and(this.predicate.and(t->true) );
        this.argumentList.forEach( a -> $args.argumentList.add( a.copy()));
        return $args;
    }

    @Override
    public $arguments $hardcode(Translator translator, Tokens kvs) {
        this.argumentList.forEach( a -> a.$hardcode(translator, kvs));
        return this;
    }

    public $arguments(){ }

    public $arguments(_arguments args){
        for(int i=0;i<args.size(); i++){
            argumentList.add( $expr.of(args.getAt(i)));
        }
    }

    public $arguments(_expr..._exs){
        for(int i=0;i<_exs.length; i++){
            argumentList.add( $expr.of(_exs[i]));
        }
    }

    public $arguments($expr...$exs){
        for(int i=0;i<$exs.length; i++){
            argumentList.add( $exs[i]);
        }
    }

    public $arguments setPredicate( Predicate<_arguments> predicate){
        this.predicate = predicate;
        return this;
    }

    public $arguments $any(Class<? extends _expr>...expressionClasses ){
        Predicate<_arguments> ps =
                (args) -> Arrays.stream(expressionClasses).anyMatch( ec-> args.get(a -> ec.isAssignableFrom(a.getClass())) != null );
        return $and( ps );
    }


    /**
     * Do ANY of the arguments match this expression predicate
     * @param _exMatchFn
     * @return
     */
    public $arguments $any( Predicate<_expr> _exMatchFn){
        return $and( a-> a.anyMatch(_exMatchFn));
    }

    /**
     * test that all arguments match the predicate
     * @param _ex
     * @return
     */
    public $arguments $all( Predicate<_expr> _ex ){
        return $and( p-> p.allMatch(_ex) );
    }

    /**
     * test that all arguments are of some set of _expression types
     * @param ecs
     * @return
     */
    public $arguments $all( Class<? extends _expr>...ecs){
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

    public boolean matches(_expr..._exs){
        return select(_exs) != null;
    }

    public Select<_arguments> select(Expression...exs){
        return select(_arguments.of(exs));
    }

    public Select<_arguments> select(_expr...exs){
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
    public $arguments $and(Predicate<_arguments> matchFn) {
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
    public _arguments draft(Translator translator, Map<String, Object> keyValues) {
        _arguments _as = _arguments.of();
        for(int i=0;i<this.argumentList.size(); i++){
            _as.add( (_expr)this.argumentList.get(i).draft(translator, keyValues) );
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
    public static class Or extends $arguments {

        public List<$arguments> $argumentsBots = new ArrayList<>();

        private Or($arguments...nms){
            Arrays.stream(nms).forEach(n-> $argumentsBots.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Select<_arguments> select(NodeWithArguments nwas){
            return select( _arguments.of(nwas) );
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

        public List<$arguments> $listOrSelectors() {
            return this.$argumentsBots;
        }

        /**
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param
         * @return
         */
        public $arguments whichMatch(_arguments ae){
            if( !this.predicate.test(ae ) ){
                return null;
            }
            Optional<$arguments> orsel  = this.$argumentsBots.stream().filter($p-> $p.matches(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public $arguments.Or copy(){
            $arguments.Or $copy = $arguments.or();
            $copy.$and(this.predicate);
            this.argumentList.forEach( ($a)-> $copy.argumentList.add( (($expr)$a).copy()));
            this.$argumentsBots.forEach( ($a) -> $copy.$argumentsBots.add($a.copy()));
            return $copy;
        }

        /**
         *
         * @param _a
         * @return
         */
        public Select<_arguments> select(_arguments _a){
            Select commonSelect = super.select(_a);
            if(  commonSelect == null){
                return null;
            }
            $arguments $whichBot = whichMatch(_a);
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
            for(int i = 0; i<this.$argumentsBots.size(); i++){
                sb.append( Text.indent( this.$argumentsBots.get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }/* Or */
}
