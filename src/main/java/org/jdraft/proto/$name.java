package org.jdraft.proto;

import org.jdraft.Stencil;
import org.jdraft.Tokens;
import org.jdraft.Translator;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * A name within the AST that can be :
 *
 * {@link com.github.javaparser.ast.expr.NameExpr}
 * {@link com.github.javaparser.ast.expr.SimpleName}
 *
 * @see $id a potentially qualified (i.e. java.util.Map) identifier/name
 * @see $ex#nameEx() a name expression
 */
public final class $name implements $constructor.$part, $method.$part, $field.$part,
        $parameter.$part, $typeParameter.$part, $var.$part, $class.$part, $interface.$part, $enum.$part, $annotation.$part,
        $enumConstant.$part,  $annotationElement.$part{

    public static $name of(){
        return $name.of("$name$");
    }

    public static $name of(Predicate<String> constraint){
        return of().and(constraint);
    }

    public static $name of(String name ){
        return new $name( name );
    }

    /** the pattern of the id*/
    public Stencil nameStencil;

    /**  */
    public Predicate<String> constraint = t -> true;

    /**
     *
     * @return
     */
    public boolean isMatchAny(){
        if( this.nameStencil.isMatchAny()) {
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
     * @param name
     */
    public $name(String name) {
        this(name, t->true);
    }

    public $name(String name, Predicate<String> constraint) {
        this.nameStencil = Stencil.of(name);
        this.constraint = constraint;
    }
    
    /**
     *
     * @param constraint
     * @return
     */
    public $name and(Predicate<String> constraint) {
        this.constraint = this.constraint.and(constraint);
        return this;
    }
   
    public $name $(String target, String $Name){
        this.nameStencil = this.nameStencil.$(target, $Name);
        return this;
    }

    public $name hardcode$(Translator tr, Tokens tokens){
        this.nameStencil = this.nameStencil.hardcode$(tr, tokens);
        return this;
    }

    public $name hardcode$(Translator tr, Object...keyValues){
        this.nameStencil = this.nameStencil.hardcode$(tr, keyValues);
        return this;
    }
    
    public List<String> list$() {
        return this.nameStencil.list$();
    }

    public List<String> list$Normalized() {
        return this.nameStencil.list$Normalized();
    }

    public String draft(Translator t, Map<String, Object> keyValues) {
        return this.nameStencil.draft(t, keyValues);
    }

    public boolean matches(String name) {
        return parse(name) != null;
    }

    public Tokens parse(String name) {
        if (name == null) {
            /** Null is allowed IF and ONLY If the Stencil $form isMatchAny */
            if (nameStencil.isMatchAny()) {
                return Tokens.of(nameStencil.list$().get(0), "");
            }
            return null;
        }
        if( constraint.test( name ) ) {
            Tokens ts = this.nameStencil.parse(name);
            if (ts != null) {
                return ts;
            }
        }
        return null;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString(){
        return "$name{ "+this.nameStencil.toString()+" }";
    }

    /*
    public final String normalize( String name ){
        int idx = name.lastIndexOf(".");
        if( idx > 0 ){
            return name.substring(idx+1);
        }
        return name;
    }
     */
    
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
