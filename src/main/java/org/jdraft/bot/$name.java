package org.jdraft.bot;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import org.jdraft._jdraftException;
import org.jdraft._name;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;

public class $name implements $bot<Node, _name, $name>,
        $selector<_name, $name>, Template<_name>, $methodCall.$part {

    public static $name of(){
        return new $name();
    }

    public static $name of( Expression e){
        return new $name(e.toString());
    }

    public static $name of( String stencil){
        return new $name(stencil);
    }

    public static $name of( Stencil stencil ){
        return new $name(stencil);
    }

    public static $name of( Predicate<_name> matchFn){
        return new $name().$and(matchFn);
    }

    /** the pattern of the name*/
    public Stencil stencil = null;

    /**  */
    public Predicate<_name> predicate = t -> true;

    public $name(){}

    public $name(String stencil){
        this.stencil = Stencil.of( stencil);
    }

    public $name(Stencil stencil){
        this.stencil = stencil;
    }

    public Predicate<_name> getPredicate(){
        return this.predicate;
    }

    @Override
    public $name setPredicate(Predicate<_name> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public boolean isMatchAny() {
        if( stencil == null ){
            try{
                return predicate.test(null);
            } catch(Exception e){ }
        }
        return false;
    }

    @Override
    public $name $and(Predicate<_name> matchFn) {
        this.predicate = predicate.and(matchFn);
        return this;
    }

    public boolean matches( Node n){
        return select(n) != null;
    }

    public boolean matches(String str ){
        return select(_name.of(str)) != null;
    }

    public Selected select(String s){
        return select( _name.of(s));
    }

    public Selected select( String...str){
        return select( _name.of(str));
    }

    public Selected select( Node n ){

        if( n instanceof Name){
            Name nm = (Name)n;
            return select( _name.of(nm) );
        }
        if( n instanceof SimpleName){
            SimpleName nm = (SimpleName)n;
            return select( _name.of(nm) );
        }
        //if( n instanceof Expression){
        //    return select( _name.of(n) );
        //}
        return null;
    }

    @Override
    public _name firstIn(Class<?> clazz) {
        return null;
    }

    @Override
    public Selected selectFirstIn(Node astNode, Predicate<Select<_name>> predicate) {
        Optional<Node> on = astNode.stream().filter( n ->{
            Select sel = select( n);
            if( sel != null ){
                return predicate.test(sel);
            }
            return false;
        }).findFirst();
        if( !on.isPresent() ){
            return null;
        }
        return select( on.get());
    }

    /*
    @Override
    public Select<String> selectFirstIn(Node astNode, Predicate<Select<String>> predicate) {
        Optional<Node> on = astNode.stream().filter( n ->{
            Select sel = select( n);
            if( sel != null ){
                return predicate.test(sel);
            }
            return false;
        }).findFirst();
        if( !on.isPresent() ){
            return null;
        }
        return select( on.get());
    }
     */

    //public Selected select(String candidate){
    public Selected select(_name candidate){
        if( isMatchAny()){
            return new Selected(candidate, new Tokens());
        }
        if( this.stencil == null ){
            if( this.predicate.test(candidate) ){

                return new Selected(candidate, new Tokens());
            }
        }
        if( this.predicate.test(candidate) ){
            Tokens ts = this.stencil.parse(candidate.toString());
            if( ts != null ) {
                return new Selected(candidate, ts);
            }
        }
        return null;
    }

    @Override
    public _name draft(Translator translator, Map<String, Object> keyValues) {
        if( this.stencil == null){
            Object nm = keyValues.get("$name");
            if( nm == null ){
                throw new _jdraftException( "cannot draft $name with no Template/Stencil ");
            }
            if( nm instanceof Stencil ){
                return _name.of(((Stencil)nm).draft(translator, keyValues));
            } else{
                return _name.of(nm.toString());
            }
        }
        return _name.of(this.stencil.draft(translator, keyValues));
    }

    @Override
    public $name $(String target, String $Name) {
        if( this.stencil != null ){
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> $list() {
        if( this.stencil != null ){
            return this.stencil.$list();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> $listNormalized() {
        if( this.stencil != null ){
            return this.stencil.$listNormalized();
        }
        return Collections.emptyList();
    }

    /**
     * An Or entity that can match against any of the $name instances
     */
    public static class Or extends $name { //implements $selector<String, $name>, $methodCall.$part{

        public Predicate<_name> predicate = p-> true;

        public List<$name> $names = new ArrayList<>();

        private Or($name...nms){
            Arrays.stream(nms).forEach(n-> $names.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Predicate<_name> getPredicate(){
            return this.predicate;
        }

        public $name setPredicate( Predicate<_name> predicate){
            this.predicate = predicate;
            return this;
        }

        @Override
        public Selected select(_name candidate) {
            if( predicate.test(candidate) ) {
                Optional<$name> on = $names.stream().filter(n -> n.matches(candidate)).findFirst();
                if (on.isPresent()) {
                    return on.get().select(candidate);
                }
            }
            return null;
        }

        @Override
        public $name $and(Predicate<_name> matchFn) {
            this.predicate = this.predicate.and(matchFn);
            return null;
        }
    }

    public static class Selected extends Select<_name> {
        public Selected(_name name, Tokens tokens) {
            super(name, tokens);
        }
    }

}
