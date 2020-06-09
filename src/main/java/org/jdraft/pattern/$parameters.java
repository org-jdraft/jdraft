package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.utils.Log;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._params;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * Pattern for parameter list
 * @author Eric
 */
public class $parameters implements Template<_params>,
        //$pattern<_parameters,$parameters>,
        $pattern.$java<_params,$parameters>,
        $constructor.$part, $method.$part {


    public static $parameters of(){
        return new $parameters( _params.of() );
    }
        
    public static $parameters of( _params _ps ){
        return new $parameters(_ps);
    }

    public static $parameters of( Class...parameterTypes){
        $parameters $ps = of();
        for(int i=0;i<parameterTypes.length;i++){
            $ps = $ps.$add( $parameter.of(parameterTypes[i]) );
        }
        return $ps;
    }

    public static $parameters of( $parameter...$ps ){
        $parameters $tps = of();
        Arrays.stream($ps).forEach( $p -> $tps.$add($p) );
        return $tps;
    }

    public static $parameters of( String...pattern){
        return new $parameters(_params.of(pattern));
    }

    public static $parameters.Or or( _params... _protos ){
        $parameters[] arr = new $parameters[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $parameters.of( _protos[i]);
        }
        return or(arr);
    }

    public static $parameters.Or or( $parameters...$tps ){
        return new $parameters.Or($tps);
    }

    public static $parameters as( List<Parameter> parameters ){
        return as( _params.of(parameters));
    }

    public static $parameters as( String...parameters ){
        return as( _params.of(parameters));
    }

    public static $parameters as( _params _ps ){
        if( _ps.size() == 0 ){
            return none();
        }
        $parameter[] $ps = new $parameter[_ps.size()];
        for(int i=0;i<_ps.size(); i++){
            $ps[i] = $parameter.as(_ps.getAt(i) );
        }
        $parameters $psa = of($ps);
        return $psa.$and( _pls -> _pls.size() == _ps.size() );
    }

    /**
     * Matches empty parameters lists only
     * @return
     */
    public static $parameters none(){
        return $parameters.of().$and(ps-> ps.isEmpty());
    }

    /**
     * private constructor
     */
    private $parameters(){
        $params = new ArrayList<>();
    }

    List<$parameter> $params = new ArrayList<>();

    Predicate<_params> constraint = t-> true;

    /**
     * 
     * @param _ps prototype parameters 
     */
    private $parameters( _params _ps ){
        _ps.forEach( p -> $params.add( $parameter.of(p)) );
    }
    
    private $parameters( Predicate<_params> constraint){
        this.constraint = constraint;
    }
    
    public $parameters $and(Predicate<_params> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    public $parameters $add($parameter $param ){
        //always rename the $id$ name to a parameter name based on the position in the parameters
        // i.e. we dont want this: ( int $name$, int $name$ ) because this assumes that the names $name$ and $name$
        // must be the same we rather want this: (int $p1$, int $p2$ ) so each name is distinct
        //first find out if there is a name conflict
        this.$params.add( $param );

        
        if( !this.$params.isEmpty() ) {
            for(int i=0;i<this.$params.size(); i++){
                $params.get(i).name.nameStencil = $params.get(i).name.nameStencil.rename$("name", "p" + (i + 1));
            }
        }
        return this;
    }

    @Override
    public Class<_params> _modelType(){
        return _params.class;
    }

    @Override
    public _params draft(Translator translator, Map<String, Object> keyValues) {
        _params _ps = _params.of();
        
        for(int i=0;i<$params.size(); i++){            
            _ps.add($params.get(i).draft(translator, keyValues));
        }
        return _ps;        
    }

    @Override
    public $parameters $(String target, String $paramName) {
        $params.forEach(p-> p.$(target, $paramName));
        return this;
    }

    @Override
    public Template<_params> $hardcode(Translator translator, Map<String, Object> keyValues) {
        return null;
    }

    @Override
    public $parameters $hardcode(Translator translator, Tokens kvs) {
        $params.forEach(p-> p.$hardcode(translator, kvs));
        return this;
    }

    public boolean match( Node node ){
        if( node instanceof NodeWithParameters ){
            return matches( (NodeWithParameters) node);
        }
        return false;
    }

    public boolean matches( NodeWithParameters astNodeWithParams ){
        return select(_params.of(astNodeWithParams)) == null;
    }
    
    public boolean matches( String parameters ){
        return select(parameters) != null;
    }
    
    public boolean matches( _params _ps ){
        return select(_ps) != null;
    }

    public boolean matches( _params._withParams _ps ){
        return select( _ps.getParams() ) != null;
    }

    public boolean match( _java _j) {
        if (_j instanceof _params._withParams) {
            return matches((_params._withParams) _j);
        }
        return false;
    }

    public Select select(NodeWithParameters astNodeWithParams ){
        return select( _params.of(astNodeWithParams) );
    }
    
    public Select select( String... parameters ){
        return select(_params.of( parameters));
    }

    public Select select( _params._withParams _hp){
        return select(_hp.getParams());
    }

    public Select select( _params _ps ){
        if( this.constraint.test(_ps)){
            
            if( this.$params.isEmpty() ){
                return new Select(_ps, Tokens.of("parameters", _ps.toString() ));
            }
            if( _ps.size() != this.$params.size() ){
                return null;
            }
            Tokens ts = new Tokens();
            for(int i=0;i<_ps.size(); i++ ){
                $parameter.Select sel = this.$params.get(i).select( _ps.getAt(i) );
                if( sel != null ) { //&& ts.isConsistent(sel.tokens.asTokens()) ){
                    ts.putAll( sel.tokens.asTokens() );
                } else{
                    final int f = i;
                    Log.error( "NO match on parameter %s with %s", ()->f, ()-> sel);
                    return null;
                }
            }
            return new Select(_ps, ts);
        }        
        return null;
    } 
    
     public Tokens parseTo(_params p, Tokens all) {
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

    public $parameters $hardcode(Object...keyValues){
        this.$params.forEach(p -> p.$hardcode(keyValues));
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
    public List<String> $list() {
        List<String> $s = new ArrayList<>();
        $params.forEach(p-> $s.addAll( p.$list() ) );
        return $s;
    }

    @Override
    public List<String> $listNormalized() {
        List<String> $s = new ArrayList<>();
        $params.forEach(p-> $s.addAll( p.$listNormalized() ) );
        return $s.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Returns the first Statement that matches the 
     * @param astStartNode the
     * @param _parametersMatchFn 
     * @return 
     */
    @Override
    public _params firstIn(Node astStartNode, Predicate<_params> _parametersMatchFn){
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
            return _params.of( (NodeWithParameters)f.get());
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
            _params _replaceParams = $replacementProto.draft(s.tokens);
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
    public <_J extends _java._domain> _J replaceIn(_J _j, String...parametersPattern ) {
        return replaceIn(_j, $parameters.of(parametersPattern));
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replacementProto
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, $parameters $replacementProto) {
        return forSelectedIn(_j, s->{
            _params _replaceParams = $replacementProto.draft(s.tokens);
            s._params.astHolder().setParameters(_replaceParams.ast());             
            } );
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_params> _parametersMatchFn, Consumer<_params> _parametersActionFn) {
        astNode.walk( Node.class, n-> {
            if( n instanceof NodeWithParameters){
                Select sel = select(_params.of( (NodeWithParameters)n ) );
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
                Select sel = select(_params.of( (NodeWithParameters)n ) );
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
                Select sel = select(_params.of( (NodeWithParameters)n ) );
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
        return (_CT)forSelectedIn((_type) _type.of(clazz), _parametersActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param _parametersActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select> _parametersActionFn) {
        Walk.in(_j, _params.class, n-> {
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
        return (_CT)forSelectedIn((_type) _type.of(clazz), selectConstraint, _parametersActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param _parametersActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> _parametersActionFn) {
        Walk.in(_j, _params.class, n-> {
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
            return "$parameters{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$parameters{ ");
        for(int i=0;i<this.$params.size(); i++){
            if( i > 0 ){
                sb.append(",");
            }
            sb.append( $params.get(i) );
        }
        sb.append(" }");
        return sb.toString();    
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $parameters{

        final List<$parameters>ors = new ArrayList<>();

        public Or($parameters...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $parameters $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$parameters.Or{");
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
        public $parameters.Select select(NodeWithParameters n){
            $parameters $a = whichMatch(n);
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
         * @param parameters
         * @return
         */
        public $parameters whichMatch(NodeWithParameters parameters){
            if( !this.constraint.test(_params.of(parameters) ) ){
                return null;
            }
            Optional<$parameters> orsel  = this.ors.stream().filter( $p-> $p.matches(parameters) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }


    /**
     * A Matched Selection result returned from matching a prototype $anno
     * inside of some Node or _node
     */
    public static class Select 
        implements $pattern.selected, select_java<org.jdraft._params> {
        
        public final org.jdraft._params _params;
        public final $tokens tokens;

        public Select (org.jdraft._params _p, Tokens tokens){
            this._params = _p;
            this.tokens = $tokens.of(tokens);
        }
        
        public Select (org.jdraft._params _p, $tokens $a){
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
        public org.jdraft._params _node() {
            return _params;
        }        
    }
}
