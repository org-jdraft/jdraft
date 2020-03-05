package org.jdraft.prototype;

import org.jdraft._jdraftException;
import org.jdraft.text.Stencil;
import org.jdraft.text.Template;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.Predicate;

public class $name implements $selector<String, $name>, Template<String>, $methodCall.$part {

    public static $name of(){
        return new $name();
    }

    public static $name of( String stencil){
        return new $name(stencil);
    }

    public static $name of( Stencil stencil ){
        return new $name(stencil);
    }

    public static $name of( Predicate<String> matchFn){
        return new $name().$and(matchFn);
    }

    /** the pattern of the name*/
    public Stencil stencil = null;

    /**  */
    public Predicate<String> predicate = t -> true;

    public $name(){}

    public $name(String stencil){
        this.stencil = Stencil.of( stencil);
    }

    public $name(Stencil stencil){
        this.stencil = stencil;
    }

    public Predicate<String> getPredicate(){
        return this.predicate;
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
    public $name $and(Predicate<String> matchFn) {
        this.predicate = predicate.and(matchFn);
        return this;
    }

    public Selected select(String candidate){
        if( isMatchAny()){
            return new Selected(candidate, new Tokens());
        }
        if( this.stencil == null ){
            if( this.predicate.test(candidate) ){
                return new Selected(candidate, new Tokens());
            }
        }
        if( this.predicate.test(candidate) ){
            Tokens ts = this.stencil.parse(candidate);
            if( ts != null ) {
                return new Selected(candidate, ts);
            }
        }
        return null;
    }

    @Override
    public String draft(Translator translator, Map<String, Object> keyValues) {
        if( this.stencil == null){
            Object nm = keyValues.get("$name");
            if( nm == null ){
                throw new _jdraftException( "cannot draft $name with no Template/Stencil ");
            }
            if( nm instanceof Stencil ){
                return ((Stencil)nm).draft(translator, keyValues);
            } else{
                return nm.toString();
            }
        }
        return this.stencil.draft(translator, keyValues);
    }

    @Override
    public $name $(String target, String $Name) {
        if( this.stencil != null ){
            this.stencil = this.stencil.$(target, $Name);
        }
        return this;
    }

    @Override
    public List<String> list$() {
        if( this.stencil != null ){
            return this.stencil.list$();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> list$Normalized() {
        if( this.stencil != null ){
            return this.stencil.list$Normalized();
        }
        return Collections.emptyList();
    }


    /**
     * An Or entity that can match against any of the $name instances
     */
    public static class Or implements $selector<String, $name>, $methodCall.$part{

        public Predicate<String> predicate = p-> true;

        public List<$name> $names = new ArrayList<>();

        private Or($name...nms){
            Arrays.stream(nms).forEach(n-> $names.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public Predicate<String> getPredicate(){
            return this.predicate;
        }

        @Override
        public Selected select(String candidate) {
            if( predicate.test(candidate) ) {
                Optional<$name> on = $names.stream().filter(n -> n.matches(candidate)).findFirst();
                if (on.isPresent()) {
                    return on.get().select(candidate);
                }
            }
            return null;
        }

        @Override
        public $name $and(Predicate<String> matchFn) {
            this.predicate = this.predicate.and(matchFn);
            return null;
        }
    }

    public static class Selected extends Select<String> {
        public Selected(String name, Tokens tokens) {
            super(name, tokens);
        }
    }

}
