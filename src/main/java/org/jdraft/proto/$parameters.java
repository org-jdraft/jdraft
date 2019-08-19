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
public final class $parameters implements Template<_parameters>, $proto<_parameters,$parameters>,
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
        return $parameters.of().and(ps-> ps.isEmpty());
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
    
    public $parameters and(Predicate<_parameters> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    public $parameters $add($parameter $param ){
        this.$params.add($param);
        return this;
    }
    
    @Override
    public _parameters draft(Translator translator, Map<String, Object> keyValues) {
        _parameters _ps = _parameters.of();
        
        for(int i=0;i<$params.size(); i++){            
            _ps.add($params.get(i).draft(translator, keyValues));
        }
        return _ps;        
    }

    @Override
    public $parameters $(String target, String $Name) {
        $params.forEach(p-> p.$(target, $Name));
        return this;
    }

    @Override
    public $parameters hardcode$(Translator translator, Tokens kvs) {
        $params.forEach(p-> p.hardcode$(translator, kvs));
        return this;
    }

    public boolean match( Node node ){
        if( node instanceof NodeWithParameters ){
            return matches( (NodeWithParameters) node);
        }
        return false;
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
                if( sel != null && ts.isConsistent(sel.tokens.asTokens()) ){
                    ts.putAll( sel.tokens.asTokens() );
                } else{
                    return null;
                }
            }
            return new Select(_ps, ts);
        }        
        return null;
    } 
    
     public Tokens parseTo(_parameters p, Tokens all) {
        if (all == null) { /* Skip decompose if the tokens already null*/
            return null;
        }
            
        Select sel = select(p);
        if( sel != null ){
            if( all.isConsistent(sel.tokens.asTokens())){
                all.putAll(sel.tokens.asTokens());
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
     * @param astStartNode the
     * @param _parametersMatchFn 
     * @return 
     */
    @Override
    public _parameters firstIn(Node astStartNode, Predicate<_parameters> _parametersMatchFn){
        Optional<Node> f =                 
            astStartNode.findFirst( Node.class,
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
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        forSelectedIn(astNode, s-> found.add(s));
        return found;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param parametersPattern
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, String...parametersPattern ) {
        return replaceIn(astNode, $parameters.of(parametersPattern));
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param $replacementProto
     * @return 
     */
    public <N extends Node> N replaceIn(N astNode, $parameters $replacementProto) {
        
        return forSelectedIn( astNode, s->{
            _parameters _replaceParams = $replacementProto.draft(s.tokens);
            s._params.astHolder().setParameters(_replaceParams.ast());             
            } );
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param parametersPattern
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, String...parametersPattern ) {
        return replaceIn(_j, $parameters.of(parametersPattern));
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replacementProto
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, $parameters $replacementProto) {
        return forSelectedIn(_j, s->{
            _parameters _replaceParams = $replacementProto.draft(s.tokens);
            s._params.astHolder().setParameters(_replaceParams.ast());             
            } );
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_parameters> _parametersMatchFn, Consumer<_parameters> _parametersActionFn) {
        astNode.walk( Node.class, n-> {
            if( n instanceof NodeWithParameters){
                Select sel = select(_parameters.of( (NodeWithParameters)n ) );
                if( sel != null && _parametersMatchFn.test(sel._params)){
                    _parametersActionFn.accept(sel._params);
                }
            }
        });        
        return astNode;
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
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select> _parametersActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), _parametersActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param _parametersActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select> _parametersActionFn) {
        _walk.in(_j, _parameters.class, n-> {
            Select sel = select( n );
            if( sel != null ){
                _parametersActionFn.accept(sel);            
            }
        });        
        return _j;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param _parametersActionFn
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> _parametersActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, _parametersActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param _parametersActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> _parametersActionFn) {
        _walk.in(_j, _parameters.class, n-> {
            Select sel = select( n );
            if( sel != null && selectConstraint.test(sel) ){
                _parametersActionFn.accept(sel);            
            }
        });        
        return _j;
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
        implements $proto.selected, select_java<_parameters> {
        
        public final _parameters _params;
        public final $tokens tokens;

        public Select ( _parameters _p, Tokens tokens){
            this._params = _p;
            this.tokens = $tokens.of(tokens);
        }
        
        public Select ( _parameters _p, $tokens $a){
            this._params = _p;
            tokens = $a;
        }
        
        public int size(){            
            return this._params.size();
        }
        
        public boolean isVarArg(){            
            return _params.isVarArg();
        }

        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$parameters.Select {"+ System.lineSeparator()+
                Text.indent(_params.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }
        
        @Override
        public _parameters _node() {
            return _params;
        }        
    }
}
