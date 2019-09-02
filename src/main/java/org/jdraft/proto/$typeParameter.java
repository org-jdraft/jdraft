package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft.*;
import org.jdraft._anno._annos;
import org.jdraft._typeParameter;
import org.jdraft._typeRef;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * prototype / template model of a _typeParameter
 *
 * @author Eric
 */
public final class $typeParameter
    implements Template<_typeParameter>, $proto<_typeParameter, $typeParameter>, $proto.$java<_typeParameter,$typeParameter>,
        $method.$part, $constructor.$part {


    public Class<_typeParameter> javaType(){
        return _typeParameter.class;
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
    public static $typeParameter of( Predicate<_typeParameter> constraint ){
        return of().$and(constraint);
    }
    
    /**
     * 
     * @param typeParameter
     * @return 
     */
    public static $typeParameter of( String typeParameter){
        return new $typeParameter( _typeParameter.of(typeParameter) );
    }
    
    /**
     * 
     * @param _tp
     * @return 
     */
    public static $typeParameter of( _typeParameter _tp){
        return new $typeParameter(_tp);
    }
    
    /**
     * 
     * @param astTp
     * @return 
     */
    public static $typeParameter of( TypeParameter astTp){
        return new $typeParameter(_typeParameter.of(astTp));
    }

    public Predicate<_typeParameter> constraint = t-> true;
    
    public $annos $anns = $annos.of();
    
    public $id $name = $id.of("$name$");
    
    /**
     * The (optional) lower bound on a type parameter (i.e. extends)
     * for example (Serializable) in : "A extends Serializable" 
     * NOTE: it can be more than one class or interface separated by the &:
     * "A extends Serializable & Clonable"
     */
    public List<$typeRef> $typeBound = new ArrayList<>();
    
    /**
     * 
     * @param $anns
     * @param name
     * @param typeBounds 
     */
    private $typeParameter( $annos $anns, $id name, $typeRef...typeBounds){
        this.$anns = $anns;
        this.$name = name;
        Arrays.stream(typeBounds).forEach( t -> this.$typeBound.add(t));
    }
    
    private $typeParameter($typeParameter.$part...parts ){
        for(int i=0;i<parts.length;i++){
            if( parts[i] instanceof $id ){
                this.$name = ($id)parts[i];
            }
            else if( parts[i] instanceof $annos ){
                this.$anns = ($annos)parts[i];
            }
            else if( parts[i] instanceof $anno ){
                this.$anns.$annosList.add( ($anno)parts[i] );
            }
            else if( parts[i] instanceof $typeRef ){
                this.$typeBound.add(($typeRef)parts[i]);
            }
        }
    }
    
    private $typeParameter(){
        //no arg... match defaults (can be created through of(), and any() static methods)
    }
    
    public $typeParameter(_typeParameter _tp){
        $anns = $annos.of(_tp);
        $name = $id.of(_tp.getName() );
        _tp.getTypeBound().forEach(tb -> this.$typeBound.add($typeRef.of(tb)));                   
    }
    
    public $typeParameter $anno(){
        this.$anns = $annos.of();
        return this;
    }
    
    public $typeParameter $anno( $anno $ann ){
        this.$anns.$annosList.add($ann);
        return this;
    }
    
    public $typeParameter $annos( $annos $anns ){
        this.$anns = $anns;
        return this;
    }
    
    public $typeParameter $annos( Predicate<_annos> constraint ){
        this.$anns.$and(constraint);
        return this;
    }
    
    public $typeParameter $name( String $name){
        this.$name.idStencil = Stencil.of($name);
        return this;
    }
    
    public $typeParameter $name(Predicate<String> constraint){
        this.$name.and(constraint);
        return this;
    }
    
    public $typeParameter $name( $id $name){
        this.$name = $name;
        return this;
    }
    
    public $typeParameter $typeBound( $typeRef... $tb ){
        Arrays.stream($tb).forEach(t -> this.$typeBound.add(t));        
        return this;
    }
    
    public $typeParameter $and(Predicate<_typeParameter> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public _typeParameter draft(Translator translator, Map<String, Object> keyValues) {
        if( keyValues.get("$typeParameter") != null ){
            Object tp = keyValues.get("$typeParameter");            
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$typeParameter");
            if( tp instanceof $typeParameter ){
                return (($typeParameter)tp).draft(translator, kvs);
            }
            if( tp instanceof _typeParameter){
                return $typeParameter.of( (_typeParameter)tp).draft(translator, kvs);
            }
            if( tp instanceof TypeParameter){
                return $typeParameter.of( (TypeParameter)tp).draft(translator, kvs);
            }
            return $typeParameter.of( tp.toString() ).draft(translator, kvs);
        }
        String anos = this.$anns.draftToString(translator, keyValues);
        String nm = this.$name.draft(translator, keyValues);
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
        return _typeParameter.of(anos+nm+tb);        
    }

    @Override
    public $typeParameter $(String target, String $Name) {
        this.$anns.$(target, $Name);
        this.$name.$(target, $Name);
        this.$typeBound.forEach(tb -> tb.$(target, $Name));
        return this;
    }

    @Override
    public List<String> list$() {
        List<String> found = new ArrayList<>();
        found.addAll(this.$anns.list$());
        found.addAll(this.$name.list$());
        this.$typeBound.forEach( tb -> found.addAll(tb.list$()));
        return found;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> found = new ArrayList<>();
        found.addAll(this.$anns.list$Normalized());
        found.addAll(this.$name.list$Normalized());
        this.$typeBound.forEach( tb -> found.addAll(tb.list$Normalized()));
        return found.stream().distinct().collect(Collectors.toList());
    }
    
    public $typeParameter hardcode$( Translator trans, Tokens hardcodedKeyValues ) {
        this.$anns.hardcode$(trans, hardcodedKeyValues);
        this.$name.hardcode$(trans, hardcodedKeyValues);
        this.$typeBound.forEach(tb -> tb.hardcode$(trans, hardcodedKeyValues));
        return this;
    }

    public boolean match( Node n){
        if( n instanceof TypeParameter ){
            return matches( (TypeParameter) n);
        }
        return false;
    }

    @Override
    public _typeParameter firstIn(Node astStartNode, Predicate<_typeParameter> _typeParamMatchFn) {
        Optional<TypeParameter> otp = 
            astStartNode.findFirst(TypeParameter.class, tp->{
                Select sel = select(tp);
                return sel != null && _typeParamMatchFn.test(sel._tp);
            });
        if( otp.isPresent()){
            return _typeParameter.of(otp.get());
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_typeParameter> _typeParamMatchFn, Consumer<_typeParameter> _typeParameterActionFn) {
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
        forEachIn(astNode, tp -> tp.ast().remove() );
        return astNode;
    }
    
    @Override
    public String toString(){
        String ans = "";
        if( !$anns.isMatchAny() ){
            ans = $anns.toString()+" ";            
        }
        if( $typeBound.isEmpty() ){
            return ans + $name.toString();
        }
        StringBuilder tb = new StringBuilder();
        tb.append(" extends ");
        for(int i=0;i<this.$typeBound.size(); i++){
            if( i > 0){
                tb.append( "&" );
            } 
            tb.append(this.$typeBound.get(i) );
        }
        return "($typeParmeter):" + ans + $name.toString()+tb;
    }
    
    public boolean matches(String typeParameter){
        return select(typeParameter)!= null;
    }
    
    public boolean matches(TypeParameter astTp){
        return select(astTp) != null;
    }
    
    public boolean matches(_typeParameter _tp){
        return select(_tp) != null;
    }
    
    public Select select(String typeParameter){
        return select( _typeParameter.of(typeParameter));
    }
    
    public Select select(_typeParameter _tp){
        if( !this.constraint.test(_tp)){
            System.out.println( "Failed constraint");
            return null;
        }
        $annos.Select asel = this.$anns.select(_tp);

        if( asel != null ){
            Tokens ts = asel.tokens().asTokens();
            ts = this.$name.parseTo(_tp.getName(), ts);
            if( ts == null ){
                return null;
            }
            List<ClassOrInterfaceType> availableTypeBounds = new ArrayList<>();
            availableTypeBounds.addAll(_tp.getTypeBound());
            
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
        return select( _typeParameter.of(astTp));
    }
    
    public static class Select 
        implements $proto.selected,
            selectAst<TypeParameter>,
            select_java<_typeParameter> {

        public Select(_typeParameter _tp, Tokens ts){
            this._tp = _tp;
            this.$tokens = $tokens.of(ts);
        }
        
        public $tokens $tokens;
        public _typeParameter _tp;
        
        @Override
        public $tokens tokens() {
            return $tokens;
        }

        @Override
        public TypeParameter ast() {
            return _tp.ast();
        }

        
        @Override
        public _typeParameter _node() {
            return _tp;
        }        
        
        public boolean is( String typeParam ){
            return _tp.is(typeParam);
        }
        
        public NodeList<ClassOrInterfaceType> typeBound(){
            return _tp.getTypeBound();
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
