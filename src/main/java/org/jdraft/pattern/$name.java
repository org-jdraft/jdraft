package org.jdraft.pattern;

import java.util.*;
import java.util.function.Predicate;

import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * A name within the AST that can be :
 *
 * {@link com.github.javaparser.ast.expr.NameExpr}
 * {@link com.github.javaparser.ast.expr.SimpleName}
 *
 * @see $id a potentially qualified (i.e. java.util.Map) identifier/name
 * @see $ex#nameEx() a name expression
 */
public class $name implements $constructor.$part, $method.$part, $field.$part,
        $parameter.$part, $typeParameter.$part, $var.$part, $class.$part, $interface.$part, $enum.$part, $annotation.$part,
        $enumConstant.$part,  $annotationEntry.$part, $type.$part {

    public static $name of(){
        return $name.of("$name$");
    }

    public static $name of(Predicate<String> constraint){
        return of().and(constraint);
    }

    public static $name of(String name ){
        return new $name( name );
    }

    public static $name.Or or( String... _protos ){
        $name[] arr = new $name[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $name.of( _protos[i]);
        }
        return or(arr);
    }

    public static $name.Or or( $name...$tps ){
        return new $name.Or($tps);
    }

    public static $name as(String name) { return of(name); }

    public static $name not( Stencil name ){
        return of().$not( n-> name.parseFirst(n)!= null );
    }


    /** the pattern of the name*/
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

    public $name $and(Predicate<String> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public $name $not(Predicate<String> notConstraint ){
        this.constraint = this.constraint.and( notConstraint.negate() );
        return this;
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

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $name {

        final List<$name>ors = new ArrayList<>();

        public Or($name...$as){
            super("$name$");
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $name hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$name.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        public Tokens parse(String name){
            $name $n = whichMatch(name);
            if( $n != null ){
                return $n.parse(name);
            }
            return null;
        }

        public Tokens parseTo(String name, Tokens tokens) {
            $name $n = whichMatch(name);
            if( $n != null ){
                return $n.parseTo(name, tokens);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $method that matches the Method or null if none of the match
         * @param name
         * @return
         */
        public $name whichMatch(String name){
            if( !this.constraint.test(name ) ){
                return null;
            }
            Optional<$name> orsel  = this.ors.stream().filter($p-> $p.matches(name) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
}
