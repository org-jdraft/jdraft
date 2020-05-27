package org.jdraft.bot;

import com.github.javaparser.ast.expr.*;
import org.jdraft._jdraftException;
import org.jdraft.text.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class $id implements $selector<String, $id>, Template<String>{

    public static $id of() {
        return new $id();
    }

    public static $id of(Expression e) {
        return new $id(e.toString());
    }

    public static $id of(String stencil) {
        return new $id(stencil);
    }

    public static $id startsWith(String text ){
        return of( text+"$after$");
    }

    public static $id endsWith(String text){
        return of( "$before$"+text);
    }

    public static $id contains(String text ){
        return of("$before$"+text+"$after$");
    }

    public static $id of(Stencil stencil ){
        return new $id(stencil);
    }

    /** the pattern of the name*/
    public Stencil stencil = null;

    /**  */
    public Predicate<String> predicate = t -> true;

    public $id(){}

    public $id(String stencil){
        this.stencil = Stencil.of( stencil);
    }

    public $id(Stencil stencil){
        this.stencil = stencil;
    }

    /**
     * Does the stencil explicitly have a . (for a qualified name)
     * @return
     */
    public boolean isQualified(){
        return stencil != null &&
                stencil.getTextForm().getFixedText().indexOf('.') >= 0;
    }

    public $id $not($id... $sels ){
        return $not( t-> Stream.of($sels).anyMatch($s -> (($bot)$s).matches(t) ) );
    }

    /** build another mutable copy of this bot */
    public $id copy(){
        $id copy = of().$and(this.predicate.and(t->true));
        if(this.stencil != null ){
            copy.stencil = Stencil.of( this.stencil );
        }
        return copy;
    }

    @Override
    public Predicate<String> getPredicate(){
        return this.predicate;
    }

    @Override
    public $id setPredicate(Predicate<String> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public boolean isMatchAny() {
        try{
            return stencil == null && predicate.test(null);
        } catch(Exception e){
        //    System.out.println( "Failed predicate test ");
            return false;
        }
    }

    @Override
    public $id $and(Predicate<String> matchFn) {
        this.predicate = predicate.and(matchFn);
        return this;
    }

    public boolean matches(String str ){
        return select(str) != null;
    }

    public Select<String> select(String s){
        if( this.predicate.test(s) ) {
            if (this.stencil == null) {
                return new Select<String>(s, new Tokens());
            }
            Tokens ts = this.stencil.parse(s);
            if (ts != null) {
                return new Select<String>(s, ts);
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
    public $id $(String target, String $Name) {
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

    public $id $hardcode(Translator translator, Map<String,Object> kvs) {
        if( this.stencil != null ){
            this.stencil.$hardcode(translator, kvs);
        }
        return this;
    }

    /**
     * Or bot to inspect/match/select based on a few independent $id $bot instances
     */
    public static class Or extends $id {

        public List<$id> $idBots = new ArrayList<>();

        private Or($id...nms){
            Arrays.stream(nms).forEach(n-> $idBots.add(n));
        }

        public boolean isMatchAny(){
            return false;
        }

        public $id.Or copy(){

            $id.Or or = new $id.Or();
            $idBots.forEach($nb -> or.$idBots.add($nb.copy()));
            or.$and( this.predicate.and(t->true) );
            if( this.stencil != null ) {
                or.stencil = this.stencil.copy();
            }
            return or;
        }

        @Override
        public Select<String> select(String _candidate) {
            Select commonSelect = super.select(_candidate);
            if(  commonSelect == null){
                return null;
            }
            $id $whichBot = whichMatch(_candidate);
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
         * Return the underlying $arguments that matches the _arguments
         * (or null if none of the $arguments match the candidate _arguments)
         * @param candidate
         * @return
         */
        public $id whichMatch(String candidate){
            Optional<$id> orsel  = $idBots.stream().filter($p-> $p.matches( candidate ) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }

        public List<$id> listBots(){
            return this.$idBots;
        }

        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append( "$id.Or{").append(System.lineSeparator());
            for(int i = 0; i< listBots().size(); i++){
                sb.append( Text.indent( this.listBots().get(i).toString()) );
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
