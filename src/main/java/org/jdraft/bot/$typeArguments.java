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

public class $typeArguments<N extends Node & NodeWithTypeArguments>
        implements $bot<N, _typeArguments, $typeArguments>, $methodCall.$part {

    public static $typeArguments of(){
        return new $typeArguments();
    }

    public static $typeArguments of (_typeArguments args){
        return new $typeArguments(args);
    }

    public static $typeArguments of (_typeRef... args){
        return new $typeArguments(args);
    }

    public static $typeArguments of(String...args){
        return of( _typeArguments.of(args));
    }

    public static $typeArguments of($typeRef ...$es){
        return new $typeArguments($es);
    }

    public static $typeArguments of (Predicate<_typeArguments> predicate){
        return new $typeArguments(predicate);
    }

    public static $typeArguments.Or or($typeArguments...$as){
        return new $typeArguments.Or($as);
    }

    public List<$typeRef> list = new ArrayList<>();
    public Predicate<_typeArguments> predicate = p->true;

    /**
     * Build and return a new independent mutable copy of this bot
     * @return
     */
    public $typeArguments copy(){
        $typeArguments $s = of( this.predicate.and(t->true) );
        List<$typeRef> l = new ArrayList<>();
        this.list.forEach( e-> l.add(e.copy()));
        $s.list = l;
        return $s;
    }

    public $typeArguments(){ }

    public $typeArguments(Predicate<_typeArguments> predicate){
        this.predicate = predicate;
    }

    public $typeArguments(_typeArguments args){
        for(int i=0;i<args.size(); i++){
            list.add( $typeRef.of(args.getAt(i)));
        }
    }

    public $typeArguments(_typeRef..._exs){
        for(int i=0;i<_exs.length; i++){
            list.add( $typeRef.of(_exs[i]));
        }
    }

    public $typeArguments($typeRef...$exs){
        for(int i=0;i<$exs.length; i++){
            list.add( $exs[i]);
        }
    }

    public $typeArguments setPredicate(Predicate<_typeArguments> predicate){
        this.predicate = predicate;
        return this;
    }

    /**
     * test that all arguments match the predicate
     * @param _ex
     * @return
     */
    public $typeArguments $all(Predicate<_typeRef> _ex ){
        return $and( p-> p.allMatch(_ex) );
    }

    public boolean matches(_typeArguments _args){
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

    public Select<_typeArguments> select(Type...exs){
        return select(_typeArguments.of(exs));
    }

    public Select<_typeArguments> select(_typeRef...exs){
        return select(_typeArguments.of(exs));
    }

    public Select<_typeArguments> select(String args){
        return select(_typeArguments.of(args));
    }

    public Select<_typeArguments> select(String...args){
        return select(_typeArguments.of(args));
    }

    @Override
    public Select<_typeArguments> select(Node n) {
        if( n instanceof NodeWithTypeArguments){
            _typeArguments _as = _typeArguments.of( (NodeWithTypeArguments)n );
            return select(_as);
        }
        return null;
    }

    @Override
    public Select<_typeArguments> selectFirstIn(Node astNode, Predicate<Select<_typeArguments>> predicate) {
        Optional<Node> on = astNode.stream().filter(n ->{
                if( n instanceof NodeWithTypeArguments ){
                    _typeArguments _as = _typeArguments.of( (NodeWithTypeArguments)n);
                    Select<_typeArguments> sel = select(_as);
                    if( sel != null ){
                        return predicate.test(sel);
                    }
                }
                return false;
             }).findFirst();
        if( on.isPresent()){
            return select(_typeArguments.of( (NodeWithTypeArguments)on.get() ));
        }
        return null;
    }

    @Override
    public Predicate<_typeArguments> getPredicate() {
        return predicate;
    }

    @Override
    public Select<_typeArguments> select(_typeArguments candidate) {
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
    public $typeArguments $and(Predicate<_typeArguments> matchFn) {
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
    public _typeArguments draft(Translator translator, Map<String, Object> keyValues) {
        _typeArguments _as = _typeArguments.of();
        for(int i = 0; i<this.list.size(); i++){
            _as.add( (_typeRef)this.list.get(i).draft(translator, keyValues) );
        }
        if( this.predicate.test(_as) ){
            return _as;
        }
        throw new _jdraftException("drafted _args does not match predicate "+_as);
    }

    @Override
    public $typeArguments $(String target, String $Name) {
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
    public static class Or extends $typeArguments { //implements $selector<_arguments, Or>, $methodCall.$part{

        public Predicate<_typeArguments> predicate = p-> true;

        public List<$typeArguments> $arguments = new ArrayList<>();

        private Or($typeArguments...nms){
            Arrays.stream(nms).forEach(n-> $arguments.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Predicate<_typeArguments> getPredicate(){
            return this.predicate;
        }

        public boolean matches(String args){
            return select(args) != null;
        }

        public boolean matches(String... args){
            return select(args) != null;
        }

        public boolean matches(NodeWithTypeArguments nwa){
            return select(nwa) != null;
        }

        public boolean matches(_typeRef..._exprs){
            return select(_exprs) != null;
        }

        public boolean matches(Type...exprs){
            return select(exprs) != null;
        }

        public boolean matches(_typeArguments candidate){
            return select(candidate) != null;
        }

        public Select<_arguments> select(String args){
            return select( _typeArguments.of(args) );
        }

        public Select<_arguments> select(String...args){
            return select( _typeArguments.of(args) );
        }

        public Select<_arguments> select(NodeWithTypeArguments nwas){
            return select( _typeArguments.of(nwas) );
        }

        public Select<_arguments> select(_typeRef..._exprs){
            return select( _typeArguments.of(_exprs) );
        }

        public Select<_arguments> select(Type...exprs){
            return select( _typeArguments.of(exprs) );
        }

        /**
         * Unique to OR implementations
         * @param nwa
         * @return
         */
        public $typeArguments whichMatch(NodeWithTypeArguments nwa){
            return whichMatch(_typeArguments.of(nwa));
        }

        /**
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param ae
         * @return
         */
        public $typeArguments whichMatch(_typeArguments ae){
            if( !this.predicate.test(ae ) ){
                return null;
            }
            Optional<$typeArguments> orsel  = this.$arguments.stream().filter($p-> $p.matches(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public Tokens parse(_typeArguments _a){
            $typeArguments $a = whichMatch(_a);
            if( $a != null) {
                Select s = $a.select(_a);
                if( s != null ){
                    return s.tokens;
                }
            }
            return null;
        }

        @Override
        public Select<_arguments> select(_typeArguments candidate) {
            $typeArguments $as = whichMatch(candidate);
            if( $as == null ){
                return null;
            }
            return $as.select(candidate);
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$typeArguments.Or{").append(System.lineSeparator());
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
