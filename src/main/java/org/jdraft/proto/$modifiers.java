package org.jdraft.proto;

import org.jdraft.*;
import org.jdraft._walk;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import org.jdraft._modifiers._hasModifiers;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * instead of directly matching (some expected set of modifiers) against a target
 * set of modifiers.  we specify the Modifiers that MUST be present in a set 
 * and other Modifiers that must NOT be present in the set...
 * 
 * if we want som EXACT set of modifiers (i.e. only "private"  )
 * @author Eric
 */
public final class $modifiers
    implements $proto<_modifiers>, $constructor.$part, $method.$part, $field.$part{
    
    public static $modifiers any(){
        return of();
    }
    
    public static $modifiers of(){
        return new $modifiers();
    }
    
    public static $modifiers of( Predicate<_modifiers> constraint ){
        return of().addConstraint(constraint);
    }
    
    public static $modifiers of( _hasModifiers _hm ){
        return $modifiers.of( _hm.getModifiers() );
    }
    
    public static $modifiers of(String...mods){
        return of(_modifiers.of(mods));
    }
    
    /**
     * 
     * @param ms
     * @return 
     */
    public static $modifiers of( Modifier...ms  ){
        return $modifiers.of( _modifiers.of(ms) );
    }
    
    /**
     * Matches sets of modifiers that have all of these modifiers
     * @param _ms
     * @return 
     */
    public static $modifiers of( _modifiers _ms ){
        return of( _ms.ast() );
    }
    
    /**
     * Matches sets of modifiers that have all of these modifiers
     * @param astNwm
     * @return 
     */
    public static $modifiers of( NodeWithModifiers astNwm ){
        return of( astNwm.getModifiers() );
    }
    
    /**
     * Matches sets of modifiers that have all of these modifiers
     * @param mods
     * @return 
     */
    public static $modifiers of( Collection<Modifier> mods ){
        $modifiers $mods = new $modifiers();
        $mods.mustInclude.addAll(mods);
        return $mods;
    }
    
    /**
     * Matches sets of modifiers that have None of these modifiers
     * @param mods
     * @return 
     */
    public static $modifiers noneOf( Collection<Modifier> mods ){
        $modifiers $mods = new $modifiers();
        $mods.mustExclude.addAll(mods);
        return $mods;
    }
    
    /**
     * 
     * @param mods
     * @return 
     */
    public static $modifiers noneOf( Modifier... mods ){
        $modifiers $mods = new $modifiers();
        Arrays.stream(mods).forEach( m -> $mods.mustExclude.add(m) );        
        return $mods;
    }
   
    /** A matching lambda constraint */
    public Predicate<_modifiers> constraint = t-> true;
    
    /** The modifiers that MUST be present*/
    public Set<Modifier>mustInclude = new HashSet<>();    
    
    /** Modifiers that MUST NOT be present */
    public Set<Modifier>mustExclude = new HashSet<>();

    public $modifiers(){        
    }

    public boolean matches( _modifiers _ms ){
        return select(_ms) != null;
    }

    public $modifiers addConstraint( Predicate<_modifiers> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    public _modifiers compose(Translator translator, Map<String,Object> keyValues){
        //parameter override
        if( keyValues.get("$modifiers") != null){
            Object mods = keyValues.get("$modifiers");
            Map<String,Object> ii = new HashMap<>();
            ii.putAll(keyValues);
            ii.remove("$modifiers");
            if( mods instanceof $modifiers ){
                return (($modifiers)mods).compose(translator, ii);
            }
            return $modifiers.of( mods.toString() ).compose( translator, ii);
        }
        
        _modifiers _ms = _modifiers.of();
        if( keyValues.get("modifiers") != null ){
            _ms = _modifiers.of( keyValues.get("modifiers").toString() );
        }
        final _modifiers _mods = _ms;
        this.mustInclude.forEach(m -> _mods.set(m) );
        return _mods;
    }
    
     /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @param _modsMatchFn 
     * @return 
     */
    @Override
    public _modifiers firstIn( Node astNode, Predicate<_modifiers> _modsMatchFn){
        Optional<Node> f =                 
            astNode.findFirst( 
                Node.class, 
                n -> {
                    if(n instanceof NodeWithModifiers){
                        Select sel = select( (NodeWithModifiers)n );
                        return sel != null && _modsMatchFn.test( sel._mods );
                    }
                    return false;
                });         
        
        if( f.isPresent()){
            return _modifiers.of( (NodeWithModifiers)f.get());
        }
        return null;
    }    
    
    /**
     * Selects the first instance
     * @param astNode
     * @return 
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<Node> f =                 
            astNode.findFirst( Node.class, 
                n -> (n instanceof NodeWithModifiers) 
                && select((NodeWithModifiers)n) != null 
            );         
        
        if( f.isPresent()){
            return select( (NodeWithModifiers)f.get());
        }
        return null;
    }
    
    @Override
    public List<Select> listSelectedIn(Node astRootNode) {
        List<Select> found = new ArrayList<>();
        forSelectedIn( astRootNode, f-> found.add(f) );
        return found;
    }

    @Override
    public <N extends Node> N removeIn(N astRootNode) {
        //"remove" for modifiers is.... interesting... it "should" mean take them 
        //out AND leave the underlying NodeList to be empty
        //it sorta doesnt make sense
        throw new UnsupportedOperationException("Cant remove modifiers"); 
    }

    @Override
    public <N extends Node> N forEachIn(N astRootNode, Predicate<_modifiers> _modifiersMatchFn, Consumer<_modifiers> _nodeActionFn) {
        return _walk.in( astRootNode,
            Node.class, 
            n->{
                if(n instanceof NodeWithModifiers ){
                    Select sel = select( (NodeWithModifiers)n );
                    if( sel != null && _modifiersMatchFn.test(sel._mods)){
                        _nodeActionFn.accept(sel.model());
                    }                
                }
            });        
    }

    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
        return _walk.in( astRootNode,
            Node.class, 
            nwm->{
                if( nwm instanceof NodeWithModifiers ){
                    Select sel = select( (NodeWithModifiers)nwm );
                    if( sel != null ){
                        selectActionFn.accept(sel);
                    }                
                }
            });
    }
    
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return _walk.in( astRootNode,
            Node.class, 
            nwm->{
                if( nwm instanceof NodeWithModifiers ){
                    Select sel = select( (NodeWithModifiers)nwm );
                    if( sel != null && selectConstraint.test(sel)){
                        selectActionFn.accept(sel);
                    }                
                }
            });
    }
    
    public _type forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return forSelectedIn(_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return _walk.in(_j,
            NodeWithModifiers.class, 
            nwm->{
                Select sel = select( nwm );
                if( sel != null && selectConstraint.test(sel)){
                    selectActionFn.accept(sel);
                }                
            });
    }
    
    /**
     * 
     * @param clazz
     * @param selectActionFn
     * @return 
     */
    public _type forSelectedIn(Class clazz, Consumer<Select> selectActionFn) {
        return forSelectedIn(_java.type(clazz), selectActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
        return _walk.in(_j,
            NodeWithModifiers.class, 
            nwm->{
                Select sel = select( nwm );
                if( sel != null ){
                    selectActionFn.accept(sel);
                }                
            });
    }

    public boolean match( Node n){
        if( n instanceof NodeWithModifiers ){
            return matches( (NodeWithModifiers) n);
        }
        return false;
    }

    public boolean matches( NodeWithModifiers nwm ){
        return select( nwm ) != null;
    }

    public Select select(_hasModifiers _hm ){
        return select(_hm.getModifiers());
    }
    
    public Select select(NodeWithModifiers astNwm ){
        return select(_modifiers.of(astNwm));
    }
    
    public Select select(_modifiers _ms){
        if( this.constraint.test(_ms) && 
            _ms.containsAll(this.mustInclude) &&  
            !_ms.containsAny(this.mustExclude) ){
            return new Select( _ms );
        }
        return null;
    }

    /*
    public _modifiers construct(Translator translator, Map<String, Object> keyValues) {
        return _modifiers.of(this.mustInclude.toArray(new Modifier[0]));
    }
     */
    
    public static class Select implements selected, selected_model<_modifiers>{

        public _modifiers _mods;
        public $args args = new $args(new Tokens());
        
        public Select(_modifiers _mods ) {
            this._mods = _mods;            
        }
        
        public boolean isStatic(){
            return _mods.isStatic();
        }
        
        public boolean isSynchronized(){
            return _mods.isSynchronized();
        }
        
        public boolean isTransient(){
            return _mods.isTransient();
        }
        
        public boolean isVolatile(){
            return _mods.isVolatile();
        }
        
        public boolean isStrictFp(){
            return _mods.isStrict();
        }
        
        public boolean isPublic(){
            return _mods.isPublic();
        }
        
        public boolean isPrivate(){
            return _mods.isPrivate();
        }
        
        public boolean isProtected(){
            return _mods.isProtected();
        }
        
        public boolean isPackagePrivate(){
            return _mods.isPackagePrivate();
        }
        
        public boolean isAbstract(){
            return _mods.isAbstract();
        }
        
        public boolean isFinal(){
            return _mods.isFinal();
        }
        
        public boolean isEmpty(){
            return _mods.isEmpty();
        }
        
        public boolean isDefault(){
            return _mods.isDefault();
        }
        
        public boolean isNative(){
            return _mods.isNative();
        }
        
        public boolean containsAll( Collection<Modifier> mods ){
            return _mods.containsAll(mods);
        }
        
        public boolean containsAny( Collection<Modifier> mods ){
            return _mods.containsAny(mods);
        }
        
        @Override
        public $args args() {
            return args;
        }

        @Override
        public _modifiers model() {
            return _mods;
        }        
    }
}
