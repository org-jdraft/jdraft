package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._annos;
import org.jdraft.text.*;

/**
 * prototype / template model of a _typeParameter
 *
 * @author Eric
 */
public class $typeParameter
    implements Template<_typeParam>, //$pattern<_typeParameter, $typeParameter>,
        $pattern.$java<_typeParam,$typeParameter>,
        $method.$part, $constructor.$part, $class.$part, $interface.$part, $type.$part {

    public Class<_typeParam> _modelType(){
        return _typeParam.class;
    }

    /** */
    public interface $part { }

    /**
     * prototype to match ANY typeParameter
     * @return 
     */
    public static $typeParameter of(){
        return new $typeParameter();
    }
    
    /**
     * 
     * @param parts
     * @return 
     */
    public static $typeParameter of( $typeParameter.$part...parts ){
        return new $typeParameter( parts );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $typeParameter of( Predicate<_typeParam> constraint ){
        return of().$and(constraint);
    }
    
    /**
     * 
     * @param typeParameter
     * @return 
     */
    public static $typeParameter of( String typeParameter){
        return new $typeParameter( _typeParam.of(typeParameter) );
    }
    
    /**
     * 
     * @param _tp
     * @return 
     */
    public static $typeParameter of( _typeParam _tp){
        return new $typeParameter(_tp);
    }
    
    /**
     * 
     * @param astTp
     * @return 
     */
    public static $typeParameter of( TypeParameter astTp){
        return new $typeParameter(_typeParam.of(astTp));
    }

    public static $typeParameter.Or or( _typeParam... _protos ){
        $typeParameter[] arr = new $typeParameter[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $typeParameter.of( _protos[i]);
        }
        return or(arr);
    }

    public static $typeParameter.Or or( $typeParameter...$tps ){
        return new $typeParameter.Or($tps);
    }

    public static $typeParameter as( String typeParameter){
        return as( _typeParam.of(typeParameter));
    }

    public static $typeParameter as( _typeParam _tp ){
        $annoRefs $as = $annoRefs.none();
        if( _tp.hasAnnos() ){
            $as = $annoRefs.as(_tp);
        }
        $name $nm = $name.as( _tp.getName() );

        List<$typeRef> $tbs = new ArrayList<>();
        _tp.getAstExtendsTypeBound().forEach(t -> $tbs.add($typeRef.as(t)));
        $typeParameter $tp = new $typeParameter( $as, $nm);
        $tp.$typeBound.addAll($tbs);


        return $tp.$and( tp-> tp.getAstExtendsTypeBound().size() == _tp.getAstExtendsTypeBound().size());
    }

    public static $typeParameter not( $part...parts){
        $typeParameter $tp = of();
        $tp.$not(parts);
        return $tp;
    }

    public Predicate<_typeParam> constraint = t-> true;
    
    public $annoRefs anns = $annoRefs.of();
    
    public $name name = $name.of("$typeParameter$");
    
    /**
     * The (optional) lower bound on a type parameter (i.e. extends)
     * for example (Serializable) in : "A extends Serializable" 
     * NOTE: it can be more than one class or interface separated by the &:
     * "A extends Serializable & Clonable"
     */
    public List<$typeRef> $typeBound = new ArrayList<>();
    
    /**
     * 
     * @param anns
     * @param name
     * @param typeBounds 
     */
    private $typeParameter($annoRefs anns, $name name, $typeRef...typeBounds){
        this.anns = anns;
        this.name = name;
        Arrays.stream(typeBounds).forEach( t -> this.$typeBound.add(t));
    }

    private $typeParameter($typeParameter.$part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $name ){
                this.name = ($name)parts[i];
            }
            else if( parts[i] instanceof $annoRefs){
                this.anns = ($annoRefs)parts[i];
            }
            else if( parts[i] instanceof $annoRef){
                this.anns.$annosList.add( ($annoRef)parts[i] );
            }
            else if( parts[i] instanceof $typeRef ){
                this.$typeBound.add(($typeRef)parts[i]);
            }
        }
    }

    /**
     * Adds a NOT constraint to the {@link #constraint} based on one or more $method.$part
     * @param parts
     * @return
     */
    public $typeParameter $not(final $part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $annoRef){
                final $annoRef $fa = (($annoRef)parts[i]);
                Predicate<_typeParam> pf = f-> $fa.countIn(f) > 0;
                $and( pf.negate() );
            }
            else if( parts[i] instanceof $name){
                final $name $fn = (($name)parts[i]);
                Predicate<_typeParam> pf = f-> $fn.matches(f.getName());
                $and( pf.negate() );
            }
        }
        return this;
    }

    private $typeParameter(){
        //no arg... match defaults (can be created through of(), and any() static methods)
    }
    
    public $typeParameter(_typeParam _tp){
        anns = $annoRefs.of(_tp);
        name = name.of(_tp.getName() );
        _tp.getAstExtendsTypeBound().forEach(tb -> this.$typeBound.add($typeRef.of(tb)));
    }
    
    public $typeParameter $anno(){
        this.anns = $annoRefs.of();
        return this;
    }
    
    public $typeParameter $anno( $annoRef $ann ){
        this.anns.$annosList.add($ann);
        return this;
    }
    
    public $typeParameter $annos( $annoRefs $anns ){
        this.anns = $anns;
        return this;
    }
    
    public $typeParameter $annos( Predicate<_annos> constraint ){
        this.anns.$and(constraint);
        return this;
    }
    
    public $typeParameter $name( String $name){
        this.name.nameStencil = Stencil.of($name);
        return this;
    }
    
    public $typeParameter $name(Predicate<String> constraint){
        this.name.and(constraint);
        return this;
    }
    
    public $typeParameter $name( $name $name){
        this.name = $name;
        return this;
    }
    
    public $typeParameter $typeBound( $typeRef... $tb ){
        Arrays.stream($tb).forEach(t -> this.$typeBound.add(t));        
        return this;
    }
    
    public $typeParameter $and(Predicate<_typeParam> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public _typeParam draft(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$typeParameter") != null ){
            Object tp = keyValues.get("$typeParameter");            
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$typeParameter");
            if( tp instanceof $typeParameter ){
                return (($typeParameter)tp).draft(translator, kvs);
            }
            if( tp instanceof _typeParam){
                return $typeParameter.of( (_typeParam)tp).draft(translator, kvs);
            }
            if( tp instanceof TypeParameter){
                return $typeParameter.of( (TypeParameter)tp).draft(translator, kvs);
            }
            return $typeParameter.of( tp.toString() ).draft(translator, kvs);
        }
        String anos = this.anns.draftToString(translator, keyValues);
        String nm = this.name.draft(translator, keyValues);
        if( nm.length() == 0 && keyValues.containsKey("name")){ //this handles $any$
            nm = keyValues.get("name").toString();
        }
        String tb = "";
        if( !this.$typeBound.isEmpty() ){
            StringBuilder b = new StringBuilder();
            for(int i=0;i<this.$typeBound.size(); i++){
                if( i > 0){
                    b.append(" & ");
                }
                b.append($typeBound.get(i).draft(translator, keyValues));
            }
            tb = " extends "+ b.toString();
        }
        return _typeParam.of(anos+nm+tb);
    }

    @Override
    public $typeParameter $(String target, String $paramName) {
        this.anns.$(target, $paramName);
        this.name.$(target, $paramName);
        this.$typeBound.forEach(tb -> tb.$(target, $paramName));
        return this;
    }

    @Override
    public $typeParameter $hardcode(Translator translator, Map<String, Object> keyValues) {
        return this.$hardcode(translator, Tokens.of(keyValues));
    }

    @Override
    public List<String> $list() {
        List<String> found = new ArrayList<>();
        found.addAll(this.anns.$list());
        found.addAll(this.name.list$());
        this.$typeBound.forEach( tb -> found.addAll(tb.$list()));
        return found;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> found = new ArrayList<>();
        found.addAll(this.anns.$listNormalized());
        found.addAll(this.name.list$Normalized());
        this.$typeBound.forEach( tb -> found.addAll(tb.$listNormalized()));
        return found.stream().distinct().collect(Collectors.toList());
    }
    
    public $typeParameter $hardcode(Translator trans, Tokens hardcodedKeyValues ) {
        this.anns.$hardcode(trans, hardcodedKeyValues);
        this.name.hardcode$(trans, hardcodedKeyValues);
        this.$typeBound.forEach(tb -> tb.$hardcode(trans, hardcodedKeyValues));
        return this;
    }

    public boolean isMatchAny(){
        try{
            return this.constraint.test(null ) && this.name.isMatchAny() && this.anns.isMatchAny() && this.$typeBound.isEmpty();
        } catch(Exception e){
            return false;
        }
    }

    public boolean match( Node n){
        if( n instanceof TypeParameter ){
            return matches( (TypeParameter) n);
        }
        return false;
    }

    @Override
    public _typeParam firstIn(Node astStartNode, Predicate<_typeParam> _typeParamMatchFn) {
        Optional<TypeParameter> otp = 
            astStartNode.findFirst(TypeParameter.class, tp->{
                Select sel = select(tp);
                return sel != null && _typeParamMatchFn.test(sel._tp);
            });
        if( otp.isPresent()){
            return _typeParam.of(otp.get());
        }
        return null;
    }
    
    @Override
    public Select selectFirstIn(Node astNode) {
        Optional<TypeParameter> otp = 
            astNode.findFirst(TypeParameter.class, tp-> matches(tp) );
        if( otp.isPresent()){
            return select(otp.get());
        }
        return null;
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Node astNode, Predicate<Select> selectConstraint) {
        Optional<TypeParameter> otp = 
            astNode.findFirst(TypeParameter.class, tp-> {
                Select sel = select(tp); 
                return sel != null && selectConstraint.test(sel);
            });
        if( otp.isPresent()){
            return select(otp.get());
        }
        return null;
    }
    
    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> selected = new ArrayList<>();
        forSelectedIn(astNode, s -> selected.add( s ) );
        return selected;
    }

    /**
     * 
     * @param astRootNode
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn(Node astRootNode, Predicate<Select> selectConstraint) {
        List<Select> selected = new ArrayList<>();
        forSelectedIn( astRootNode, selectConstraint, s -> selected.add( s ) );
        return selected;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        astRootNode.walk(TypeParameter.class, tp-> {
            Select sel = select(tp);
            if( sel != null && selectConstraint.test(sel)){
                selectActionFn.accept(sel);
            }
        });
        return astRootNode;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
        astRootNode.walk(TypeParameter.class, tp-> {
            Select sel = select(tp);
            if( sel != null ){
                selectActionFn.accept(sel);
            }
        });
        return astRootNode;
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_typeParam> _typeParamMatchFn, Consumer<_typeParam> _typeParameterActionFn) {
        astNode.walk(TypeParameter.class, tp-> {
            Select sel = select(tp);
            if( sel != null && _typeParamMatchFn.test(sel._tp)){
                _typeParameterActionFn.accept(sel._node());
            }
        });
        return astNode;
    }

    @Override
    public <N extends Node> N removeIn(N astNode) {
        forEachIn(astNode, tp -> tp.node().remove() );
        return astNode;
    }
    
    @Override
    public String toString(){
        if( isMatchAny() ){
            return "$typeParameter{ $ANY$ }";
        }
        String ans = "";
        if( !anns.isMatchAny() ){
            ans = anns.toString()+" ";
        }
        if( $typeBound.isEmpty() ){
            return ans + name.toString();
        }
        StringBuilder tb = new StringBuilder();
        tb.append(" extends ");
        for(int i=0;i<this.$typeBound.size(); i++){
            if( i > 0){
                tb.append( "&" );
            } 
            tb.append(this.$typeBound.get(i) );
        }
        return "$typeParameter{ " + ans + name.toString()+tb +" }";
    }
    
    public boolean matches(String typeParameter){
        return select(typeParameter)!= null;
    }
    
    public boolean matches(TypeParameter astTp){
        return select(astTp) != null;
    }
    
    public boolean matches(_typeParam _tp){
        return select(_tp) != null;
    }
    
    public Select select(String typeParameter){
        return select( _typeParam.of(typeParameter));
    }
    
    public Select select(_typeParam _tp){
        if( !this.constraint.test(_tp)){
            System.out.println( "Failed constraint");
            return null;
        }

        $annoRefs.Select asel = this.anns.select(_tp);
        if( asel == null ){
            System.out.println("Failed Annotations Select");
        }

        if( asel != null ){
            Tokens ts = asel.tokens().asTokens();
            ts = this.name.parseTo(_tp.getName(), ts);
            if( ts == null ){
                return null;
            }
            List<ClassOrInterfaceType> availableTypeBounds = new ArrayList<>();
            availableTypeBounds.addAll(_tp.getAstExtendsTypeBound());
            //System.out.println( "Available type bound s"+ availableTypeBounds);
            //System.out.println( "this.$typeBound"+ this.$typeBound);
            /*
            if( availableTypeBounds.size() > this.$typeBound.size() ){
                Log.info("Not correct number of type bounds %s ONLY HAVE %s", ()->availableTypeBounds.size(),()-> this.$typeBound.size() );
                Log.info("Not correct number of type bounds %s WITH %s", ()->availableTypeBounds,()-> this.$typeBound );
                if( this.isMatchAny() ){
                    return new Select(_tp, ts);
                }
                return null;
            }
             */
            for(int i=0;i<this.$typeBound.size(); i++){
                $typeRef $tb = this.$typeBound.get(i);
                Optional<ClassOrInterfaceType> coit = 
                    availableTypeBounds.stream().filter( t -> $tb.matches(t) ).findFirst();
                if( !coit.isPresent()){
                    return null;
                }
                ts = $tb.parseTo(_typeRef.of(coit.get()), ts);
                if( ts == null ){
                    return null;
                }
            }
            return new Select(_tp, ts);
        }
        return null;
    }
    
    public Select select(TypeParameter astTp){
        return select( _typeParam.of(astTp));
    }


    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $typeParameter {

        final List<$typeParameter>ors = new ArrayList<>();

        public Or($typeParameter...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $typeParameter.Or $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$typeParameter.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param n
         * @return
         */
        public $typeParameter.Select select(_typeParam n){
            $typeParameter $a = whichMatch(n);
            if( $a != null ){
                return $a.select(n);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $method that matches the Method or null if none of the match
         * @param tp
         * @return
         */
        public $typeParameter whichMatch(_typeParam tp){
            if( !this.constraint.test(tp ) ){
                return null;
            }
            Optional<$typeParameter> orsel  = this.ors.stream().filter( $p-> $p.matches(tp) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    public static class Select 
        implements $pattern.selected,
            selectAst<TypeParameter>,
            select_java<_typeParam> {

        public Select(_typeParam _tp, Tokens ts){
            this._tp = _tp;
            this.$tokens = $tokens.of(ts);
        }
        
        public $tokens $tokens;
        public _typeParam _tp;
        
        @Override
        public $tokens tokens() {
            return $tokens;
        }

        @Override
        public TypeParameter ast() {
            return _tp.node();
        }

        
        @Override
        public _typeParam _node() {
            return _tp;
        }        
        
        public boolean is( String typeParam ){
            return _tp.is(typeParam);
        }
        
        public NodeList<ClassOrInterfaceType> typeBound(){
            return _tp.getAstExtendsTypeBound();
        }
        
        public String name(){
            return _tp.getName();
        }
        
        public _annos annos(){
            return _tp.getAnnos();
        }

        @Override
        public String toString(){
            return "$throws.Select {"+ System.lineSeparator()+
                    Text.indent(_tp.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + $tokens) + System.lineSeparator()+
                    "}";
        }
    }    
}
