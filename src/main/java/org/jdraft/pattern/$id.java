package org.jdraft.pattern;

import org.jdraft.*;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Notion of a name or identifier that can be :
 * "simple" i.e. "Map"
 * or "fully qualified" i.e. "java.util.Map"
 *
 * @see $name (a "simple" name cannot be fully qualified
 */
public final class $id {

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
    public Stencil idStencil;

    /**  */
    public Predicate<String> constraint = t -> true;

    /**
     * 
     * @return 
     */
    public boolean isMatchAny(){
        if( this.idStencil.isMatchAny()) {
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
     * @param idStencil
     */
    public $id(String idStencil) {
        this(idStencil, t->true);
    }

    public $id(String idStencil, Predicate<String> constraint) {
        this.idStencil = Stencil.of(idStencil);
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
        this.idStencil = this.idStencil.$(target, $Name);
        return this;
    }

    public $id hardcode$( Translator tr, Tokens tokens){
        this.idStencil = this.idStencil.hardcode$(tr, tokens);
        return this;
    }

    public $id hardcode$( Translator tr, Object...keyValues){
        this.idStencil = this.idStencil.hardcode$(tr, keyValues);
        return this;
    }
    
    public List<String> list$() {
        return this.idStencil.list$();
    }

    public List<String> list$Normalized() {
        return this.idStencil.list$Normalized();
    }

    public String draft(Translator t, Map<String, Object> keyValues) {
        return this.idStencil.draft(t, keyValues);
    }

    public boolean matches(String id) {
        return parse(id) != null;
    }


    public Tokens parse(String id) {

        if (id == null) {
            /** Null is allowed IF and ONLY If the Stencil $form isMatchAll */
            if (idStencil.isMatchAny()) {
                return Tokens.of(idStencil.list$().get(0), "");
            }
            return null;
        }
        if( constraint.test( id ) ) {
            int idx = id.lastIndexOf(".");
            if( idx > 0 ){ //input is fully qualified id
                //if the pattern is NOT fully qualified
                if( !this.idStencil.getTextForm().getFixedText().contains(".") ){
                    String oldPattern = this.idStencil.toString();
                    String newPattern = oldPattern.substring(oldPattern.lastIndexOf(".")+1);
                    Tokens ts = Stencil.of(newPattern).parse(normalize(id));
                    return ts;
                }
            }            
            //if neither or both are 
            return idStencil.parse( id );
        }
        return null;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString(){
        return this.idStencil.toString();
    }
    
    public final String normalize( String str ){
        int idx = str.lastIndexOf(".");
        if( idx > 0 ){
            return str.substring(idx+1);
        }
        return str;
    }
    
    public $pattern.$tokens parseTo(String t, $pattern.$tokens tokens) {
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
            /* Skip parse if the tokens already null*/
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
