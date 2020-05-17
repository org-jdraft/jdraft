package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.type.Type;
import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class $typeArgs<N extends Node & NodeWithTypeArguments>
        implements $bot<N, _typeArgs, $typeArgs>, $methodCallExpr.$part {

    public static $typeArgs of(){
        return new $typeArgs();
    }

    public static $typeArgs of (_typeArgs args){
        return new $typeArgs(args);
    }

    public static $typeArgs of (_typeRef... args){
        return new $typeArgs(args);
    }

    public static $typeArgs of(String...args){
        return of( _typeArgs.of(args));
    }

    public static $typeArgs of($typeRef ...$es){
        return new $typeArgs($es);
    }

    public static $typeArgs.Or or($typeArgs...$as){
        return new $typeArgs.Or($as);
    }

    public List<$typeRef> list = new ArrayList<>();
    public Predicate<_typeArgs> predicate = p->true;

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $typeArgs copy(){
        $typeArgs $s = of().$and( this.predicate.and(t->true) );
        List<$typeRef> l = new ArrayList<>();
        this.list.forEach( e-> l.add(e.copy()));
        $s.list = l;
        return $s;
    }

    @Override
    public $typeArgs<N> $hardcode(Translator translator, Tokens kvs) {
        this.list.forEach(t -> t.$hardcode(translator, kvs));
        return this;
    }

    public $typeArgs(){ }

    public $typeArgs(_typeArgs args){
        for(int i=0;i<args.size(); i++){
            list.add( $typeRef.of(args.getAt(i)));
        }
    }

    public $typeArgs(_typeRef..._exs){
        for(int i=0;i<_exs.length; i++){
            list.add( $typeRef.of(_exs[i]));
        }
    }

    public $typeArgs($typeRef...$exs){
        for(int i=0;i<$exs.length; i++){
            list.add( $exs[i]);
        }
    }

    public $typeArgs setPredicate(Predicate<_typeArgs> predicate){
        this.predicate = predicate;
        return this;
    }

    /**
     * test that all arguments match the predicate
     * @param _ex
     * @return
     */
    public $typeArgs $all(Predicate<_typeRef> _ex ){
        return $and( p-> p.allMatch(_ex) );
    }

    public boolean matches(_typeArgs _args){
        return select(_args) != null;
    }

    public boolean matches(String args){
        return select(args) != null;
    }

    public boolean matches(String...args){
        return select(args) != null;
    }

    public boolean matches(Type...exs){
        return select(exs) != null;
    }

    public boolean matches(_typeRef..._exs){
        return select(_exs) != null;
    }

    public Select<_typeArgs> select(Type...exs){
        return select(_typeArgs.of(exs));
    }

    public Select<_typeArgs> select(_typeRef...exs){
        return select(_typeArgs.of(exs));
    }

    public Select<_typeArgs> select(String args){
        return select(_typeArgs.of(args));
    }

    public Select<_typeArgs> select(String...args){
        return select(_typeArgs.of(args));
    }

    @Override
    public Select<_typeArgs> select(Node n) {
        if( n instanceof NodeWithTypeArguments){
            _typeArgs _as = _typeArgs.of( (NodeWithTypeArguments)n );
            return select(_as);
        }
        return null;
    }

    @Override
    public Select<_typeArgs> selectFirstIn(Node astNode, Predicate<Select<_typeArgs>> predicate) {
        Optional<Node> on = astNode.stream().filter(n ->{
                if( n instanceof NodeWithTypeArguments ){
                    _typeArgs _as = _typeArgs.of( (NodeWithTypeArguments)n);
                    Select<_typeArgs> sel = select(_as);
                    if( sel != null ){
                        return predicate.test(sel);
                    }
                }
                return false;
             }).findFirst();
        if( on.isPresent()){
            return select(_typeArgs.of( (NodeWithTypeArguments)on.get() ));
        }
        return null;
    }

    @Override
    public Predicate<_typeArgs> getPredicate() {
        return predicate;
    }

    @Override
    public Select<_typeArgs> select(_typeArgs candidate) {
        if( isMatchAny()){
            return new Select(candidate, new Tokens());
        }
        if( this.predicate.test(candidate)){
            if( !this.list.isEmpty()) { //they have spelled out individual arguments
                if( this.list.size() == candidate.size() ){
                    Tokens ts = new Tokens();
                    for(int i = 0; i<this.list.size(); i++){
                        Select s = list.get(i).select(candidate.getAt(i));
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
        if( this.list.isEmpty() || (this.list.size() == 1
                && this.list.get(0).isMatchAny())){
            try{
                return this.predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    @Override
    public $typeArgs $and(Predicate<_typeArgs> matchFn) {
        setPredicate( getPredicate().and(matchFn));
        this.predicate = this.predicate.and(matchFn);
        return this;
    }

    public String toString(){
        if( this.isMatchAny() ){
            return "$typeArguments( $ANY$ )";
        }
        if( this.list.isEmpty() ){
            return "$typeArguments() {#"+this.hashCode()+"}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append( "$typeArguments(").append(System.lineSeparator());
        for(int i = 0; i<this.list.size(); i++){
            //if( i > 0 ){
            //    sb.append(System.lineSeparator());
            //}
            sb.append( Text.indent( this.list.get(i).toString()) );
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public _typeArgs draft(Translator translator, Map<String, Object> keyValues) {
        _typeArgs _as = _typeArgs.of();
        for(int i = 0; i<this.list.size(); i++){
            _as.add( (_typeRef)this.list.get(i).draft(translator, keyValues) );
        }
        if( this.predicate.test(_as) ){
            return _as;
        }
        throw new _jdraftException("drafted _args does not match predicate "+_as);
    }

    @Override
    public $typeArgs $(String target, String $Name) {
        this.list.forEach(e -> e.$(target, $Name));
        return this;
    }

    @Override
    public List<String> $list() {
        List<String> sts = new ArrayList<>();
        this.list.forEach(a -> sts.addAll( a.$list()));
        return sts;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> sts = new ArrayList<>();
        this.list.forEach(a -> sts.addAll( a.$listNormalized()));
        return sts.stream().distinct().collect(Collectors.toList());
    }

    /**
     * An Or entity that can match against any of some number of instances
     * NOTE: this can be used as a selector but NOT as a Template
     */
    public static class Or extends $typeArgs {

        public List<$typeArgs> $typeArgsList = new ArrayList<>();

        private Or($typeArgs...nms){
            Arrays.stream(nms).forEach(n-> $typeArgsList.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public boolean matches(NodeWithTypeArguments nwa){
            return select(nwa) != null;
        }

        public $typeArgs.Or copy(){
            Or or = new Or();
            or.predicate = this.predicate.and(t->true);
            this.$typeArgsList.forEach(e-> or.$typeArgsList.add(e.copy()));
            this.list.forEach( e-> or.list.add( (($expr)e).copy()));
            return or;
        }

        public Select<_typeArgs> select(NodeWithTypeArguments nwas){
            return select( _typeArgs.of(nwas) );
        }

        public Select<_typeArgs> select(_typeArgs _candidate){
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $typeArgs $whichBot = whichMatch(_candidate);
            if( $whichBot == null ){
                return null;
            }
            Select whichSelect = $whichBot.select(_candidate);
            if( !commonSelect.tokens.isConsistent(whichSelect.tokens)){
                return null;
            }
            whichSelect.tokens.putAll(commonSelect.tokens);
            return whichSelect;
        }

        /**
         * Unique to OR implementations
         * @param nwa
         * @return
         */
        public $typeArgs whichMatch(NodeWithTypeArguments nwa){
            return whichMatch(_typeArgs.of(nwa));
        }

        /**
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param ae
         * @return
         */
        public $typeArgs whichMatch(_typeArgs ae){
            if( !this.predicate.test(ae ) ){
                return null;
            }
            Optional<$typeArgs> orsel  = this.$typeArgsList.stream().filter($p-> $p.matches(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public Tokens parse(_typeArgs _a){
            $typeArgs $a = whichMatch(_a);
            if( $a != null) {
                Select s = $a.select(_a);
                if( s != null ){
                    return s.tokens;
                }
            }
            return null;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$typeArguments.Or{").append(System.lineSeparator());
            for(int i = 0; i<this.$typeArgsList.size(); i++){
                sb.append( Text.indent( this.$typeArgsList.get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
