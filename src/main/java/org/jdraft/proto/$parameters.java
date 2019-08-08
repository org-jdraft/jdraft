package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import org.jdraft.*;
import org.jdraft._walk;
import org.jdraft._java;
import org.jdraft._parameter._parameters;
import org.jdraft._type;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author Eric
 */
public final class $parameters implements Template<_parameters>, $proto<_parameters>,
        $constructor.$part, $method.$part {

    List<$parameter> $params = new ArrayList<>();
    
    Predicate<_parameters> constraint = t-> true;
    
    public static $parameters any(){
        return of();
    }
    
    /**
     * Matches empty parameters lists only
     * @return 
     */
    public static $parameters none(){
        return $parameters.of().addConstraint( ps-> ps.isEmpty());
    }
    
    public static $parameters of(){
        return new $parameters( _parameters.of() );
    }
        
    public static $parameters of( _parameters _ps ){
        return new $parameters(_ps);
    }
    
    public static $parameters of( String...pattern){
        return new $parameters(_parameters.of(pattern));
    }
    
    /**
     * 
     * @param _ps prototype parameters 
     */
    private $parameters( _parameters _ps ){
        _ps.forEach( p -> $params.add( $parameter.of(p)) );
    }
    
    private $parameters( Predicate<_parameters> constraint){
        this.constraint = constraint;
    }
    
    public $parameters addConstraint( Predicate<_parameters> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    public $parameters $add($parameter $param ){
        this.$params.add($param);
        return this;
    }
    
    @Override
    public _parameters compose(Translator translator, Map<String, Object> keyValues) {
        _parameters _ps = _parameters.of();
        
        for(int i=0;i<$params.size(); i++){            
            _ps.add($params.get(i).compose(translator, keyValues));
        }
        return _ps;        
    }

    @Override
    public $parameters $(String target, String $Name) {
        $params.forEach(p-> p.$(target, $Name));
        return this;
    }

    public boolean matches( NodeWithParameters astNodeWithParams ){
        return select(_parameters.of(astNodeWithParams)) == null;
    }
    
    public boolean matches( String parameters ){
        return select(parameters) != null;
    }
    
    public boolean matches( _parameters _ps ){
        return select(_ps) != null;
    }
    
    public Select select(NodeWithParameters astNodeWithParams ){
        return select( _parameters.of(astNodeWithParams) );
    }
    
    public Select select( String... parameters ){
        return select(_parameters.of( parameters));
    }
    
    public Select select( _parameters _ps ){
        if( this.constraint.test(_ps)){
            
            if( this.$params.isEmpty() ){
                return new Select(_ps, Tokens.of("parameters", _ps.toString() ));
            }
            if( _ps.size() != this.$params.size() ){
                return null;
            }
            Tokens ts = new Tokens();
            for(int i=0;i<_ps.size(); i++ ){
                $parameter.Select sel = this.$params.get(i).select( _ps.get(i) );
                if( sel != null && ts.isConsistent(sel.args.asTokens()) ){
                    ts.putAll( sel.args.asTokens() );
                } else{
                    return null;
                }
            }
            return new Select(_ps, ts);
        }        
        return null;
    } 
    
     public Tokens decomposeTo(_parameters p, Tokens all) {
        if (all == null) { /* Skip decompose if the tokens already null*/
            return null;
        }
            
        Select sel = select(p);
        if( sel != null ){
            if( all.isConsistent(sel.args.asTokens())){
                //System.out.println("adding "+sel.args.asTokens() );
                all.putAll(sel.args.asTokens());
                return all;
            }
        }
        return null;        
    }
    
    public $parameters hardcode$(Object...keyValues){
        this.$params.forEach(p -> p.hardcode$(keyValues));
        return this;
    } 
    
    public boolean isMatchAny(){
        if( this.$params.isEmpty() || 
            this.$params.size() == 1 && 
            this.$params.get(0).isMatchAny() ){
            
            try{
                return constraint.test(null);
            } catch(Exception e){
                return false;
            }    
        }
        return false;
    }
    
    @Override
    public List<String> list$() {
        List<String> $s = new ArrayList<>();
        $params.forEach(p-> $s.addAll( p.list$() ) );
        return $s;
    }

    @Override
    public List<String> list$Normalized() {
        List<String> $s = new ArrayList<>();
        $params.forEach(p-> $s.addAll( p.list$Normalized() ) );
        return $s.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @param _parametersMatchFn 
     * @return 
     */
    @Override
    public _parameters firstIn( Node astNode, Predicate<_parameters> _parametersMatchFn){
        Optional<Node> f =                 
            astNode.findFirst( Node.class, 
                n ->{
                    if (n instanceof NodeWithParameters){ 
                        Select sel = select( (NodeWithParameters)n);
                        return sel != null && _parametersMatchFn.test(sel._params);                        
                    }
                    return false;
                });         
        
        if( f.isPresent()){
            return _parameters.of( (NodeWithParameters)f.get());
        }
        return null;
    }    
    
    
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<Node> f =                 
            astNode.findFirst( Node.class, 
                n -> (n instanceof NodeWithParameters) 
                && matches((NodeWithParameters)n) 
            );         
        
        if( f.isPresent()){
            return select( (NodeWithParameters)f.get());
        }
        return null;
    }
    
    @Override
    public List<Select> listSelectedIn(Node astRootNode) {
        List<Select> found = new ArrayList<>();
        forSelectedIn( astRootNode, s-> found.add(s));
        return found;
    }

    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param parametersPattern
     * @return 
     */
    public <N extends Node> N replaceIn(N astRootNode, String...parametersPattern ) {        
        return replaceIn(astRootNode, $parameters.of(parametersPattern));        
    }

    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param $replacementProto
     * @return 
     */
    public <N extends Node> N replaceIn(N astRootNode, $parameters $replacementProto) {
        
        return forSelectedIn( astRootNode, s->{            
            _parameters _replaceParams = $replacementProto.compose(s.args);
            s._params.astHolder().setParameters(_replaceParams.ast());             
            } );
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param parametersPattern
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, String...parametersPattern ) {     
        return replaceIn(_n, $parameters.of(parametersPattern));    
    }

    /**
     * 
     * @param <N>
     * @param _n
     * @param $replacementProto
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, $parameters $replacementProto) {
        return forSelectedIn( _n, s->{            
            _parameters _replaceParams = $replacementProto.compose(s.args);
            s._params.astHolder().setParameters(_replaceParams.ast());             
            } );
    }
    
    @Override
    public <N extends Node> N forEachIn(N astRootNode, Predicate<_parameters> _parametersMatchFn, Consumer<_parameters> _parametersActionFn) {        
        astRootNode.walk( Node.class, n-> {            
            if( n instanceof NodeWithParameters){
                Select sel = select(_parameters.of( (NodeWithParameters)n ) );
                if( sel != null && _parametersMatchFn.test(sel._params)){
                    _parametersActionFn.accept(sel._params);
                }
            }
        });        
        return astRootNode;        
    }

    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param _parametersActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> _parametersActionFn) {
        
        astRootNode.walk( Node.class, n-> {            
            if( n instanceof NodeWithParameters){
                Select sel = select(_parameters.of( (NodeWithParameters)n ) );
                if( sel != null ){
                    _parametersActionFn.accept(sel);
                }
            }
        });        
        return astRootNode;        
    }

    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectConstraint
     * @param _parametersActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select> selectConstraint, Consumer<Select> _parametersActionFn) {
        
        astRootNode.walk( Node.class, n-> {            
            if( n instanceof NodeWithParameters){
                Select sel = select(_parameters.of( (NodeWithParameters)n ) );
                if( sel != null && selectConstraint.test(sel) ){
                    _parametersActionFn.accept(sel);
                }
            }
        });        
        return astRootNode;        
    }
    
    /**
     * 
     * @param clazz
     * @param _parametersActionFn
     * @return 
     */
    public _type forSelectedIn(Class clazz, Consumer<Select> _parametersActionFn) {
        return forSelectedIn(_java.type(clazz), _parametersActionFn);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param _parametersActionFn
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Consumer<Select> _parametersActionFn) {
        _walk.in(_n, _parameters.class, n-> {
            Select sel = select( n );
            if( sel != null ){
                _parametersActionFn.accept(sel);            
            }
        });        
        return _n;        
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param _parametersActionFn
     * @return 
     */
    public _type forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> _parametersActionFn) {
        return forSelectedIn(_java.type(clazz), selectConstraint, _parametersActionFn);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param selectConstraint
     * @param _parametersActionFn
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Predicate<Select> selectConstraint, Consumer<Select> _parametersActionFn) {
        _walk.in(_n, _parameters.class, n-> {
            Select sel = select( n );
            if( sel != null && selectConstraint.test(sel) ){
                _parametersActionFn.accept(sel);            
            }
        });        
        return _n;        
    }
    
    @Override
    public String toString(){
        if( isMatchAny() ){
            return "( $any$ )";
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.$params.size(); i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( $params.get(i) );
        }
        return sb.toString();    
    }
 
    /**
     * A Matched Selection result returned from matching a prototype $anno
     * inside of some Node or _node
     */
    public static class Select 
        implements $proto.selected, selected_model<_parameters> {
        
        public final _parameters _params;
        public final $args args;

        public Select ( _parameters _p, Tokens tokens){
            this._params = _p;
            args = $args.of(tokens);
        }
        
        public Select ( _parameters _p, $args $a){
            this._params = _p;
            args = $a;
        }
        
        public int size(){            
            return this._params.size();
        }
        
        public boolean isVarArg(){            
            return _params.isVarArg();
        }
        
        
        @Override
        public $args args(){
            return args;
        }
        
        @Override
        public String toString(){
            return "$parameters.Select {"+ System.lineSeparator()+
                Text.indent(_params.toString() )+ System.lineSeparator()+
                Text.indent("$args : " + args) + System.lineSeparator()+
                "}";
        }
        
        @Override
        public _parameters model() {
            return _params;
        }        
    }
}
