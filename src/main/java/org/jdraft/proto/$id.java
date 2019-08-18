package org.jdraft.proto;

import org.jdraft.*;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Notion of a name or identifier that can be :
 * "simple" i.e. "Map"
 * or "fully qualified" i.e. "java.util.Map"
 * 
 */
public final class $id implements $constructor.$part, $method.$part, $field.$part,
        $parameter.$part, $typeParameter.$part, $var.$part {

    public static $id any(){
        return of();
    }
    
    public static $id of(){
        return $id.of("$id$");
    }
    
    public static $id of(Predicate<String> constraint){
        return of().and(constraint);
    }
    
    public static $id of( String id ){
        return new $id( id );
    }
    
    /** the pattern of the id*/
    public Stencil pattern;

    /**  */
    public Predicate<String> constraint = t -> true;

    /**
     * 
     * @return 
     */
    public boolean isMatchAny(){
        if( this.pattern.isMatchAny()) {
            try{
                return this.constraint.test( null );
            } catch(Exception e){
                return false;
            }
        }
        return false;
    }
    
    /**
     * 
     * @param pattern
     */
    public $id(String pattern) {
        this(pattern, t->true);
    }

    public $id(String pattern, Predicate<String> constraint) {
        this.pattern = Stencil.of(pattern);
        this.constraint = constraint;
    }
    
    /**
     *
     * @param constraint
     * @return
     */
    public $id and(Predicate<String> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }
   
    public $id $(String target, String $Name){
        this.pattern = this.pattern.$(target, $Name);
        return this;
    }

    public $id hardcode$( Translator tr, Tokens tokens){
        this.pattern = this.pattern.hardcode$(tr, tokens);
        return this;
    }

    public $id hardcode$( Translator tr, Object...keyValues){
        this.pattern = this.pattern.hardcode$(tr, keyValues);
        return this;
    }
    
    public List<String> list$() {
        return this.pattern.list$();
    }

    public List<String> list$Normalized() {
        return this.pattern.list$Normalized();
    }

    public String draft(Translator t, Map<String, Object> keyValues) {
        return this.pattern.draft(t, keyValues);
    }

    public boolean matches(String t) {
        return parse(t) != null;
    }

    public Tokens parse(String t) {

        if (t == null) {
            /**
             * Null is allowed IF and ONLY If the Stencil $form isMatchAll
             */
            if (pattern.isMatchAny()) {
                return Tokens.of(pattern.list$().get(0), "");
            }
            return null;
        }
        if( constraint.test( t ) ) {       
            int idx = t.lastIndexOf(".");
            if( idx > 0 ){ //input is fully qualified id
                //if the pattern is NOT fully qualified
                if( !this.pattern.getTextBlanks().getFixedText().contains(".") ){ 
                    String oldPattern = this.pattern.toString();
                    String newPattern = oldPattern.substring(oldPattern.lastIndexOf(".")+1);
                    Tokens ts = Stencil.of(newPattern).parse(normalize(t));
                    return ts;
                }
            }            
            //if neither or both are 
            return pattern.parse( t );
        }
        return null;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString(){
        return this.pattern.toString();
    }
    
    public final String normalize( String str ){
        int idx = str.lastIndexOf(".");
        if( idx > 0 ){
            return str.substring(idx+1);
        }
        return str;
    }
    
    public $proto.$tokens parseTo(String t, $proto.$tokens tokens) {
        if (tokens == null) {
            return null;
        }
        Tokens ts = parse(t);
        if (ts != null) {
            if (tokens.isConsistent(ts)) {
                tokens.putAll(ts);
                return tokens;
            }
        }
        return null;
    }

    public Tokens parseTo(String t, Tokens tokens) {
        if (tokens == null) {
            /* Skip decompose if the tokens already null*/
            return null;
        }

        Tokens ts = parse(t);

        if (ts != null) {
            if (tokens.isConsistent(ts)) {
                tokens.putAll(ts);
                return tokens;
            }
        }
        return null;
    }
}
