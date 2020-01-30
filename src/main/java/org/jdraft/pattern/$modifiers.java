package org.jdraft.pattern;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jdraft.*;
import org.jdraft._modifiers._hasModifiers;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * instead of directly matching (some expected set of modifiers) against a target
 * set of modifiers.  we specify the Modifiers that MUST be present in a set 
 * and other Modifiers that must NOT be present in the set...
 * 
 * if we want som EXACT set of modifiers (i.e. only "private"  )
 * @author Eric
 */
public class $modifiers
    implements $pattern<_modifiers, $modifiers>, $pattern.$java<_modifiers,$modifiers>, $constructor.$part, $method.$part,
        $field.$part, $class.$part, $interface.$part, $enum.$part, $annotation.$part, $type.$part {


    public Class<_modifiers> _modelType(){
        return _modifiers.class;
    }

    public static $modifiers PUBLIC = $modifiers.of("public");
    public static $modifiers PRIVATE = $modifiers.of("private");
    public static $modifiers PROTECTED = $modifiers.of("protected");

    public static $modifiers ABSTRACT = $modifiers.of("abstract");
    public static $modifiers STATIC = $modifiers.of("static");
    public static $modifiers FINAL = $modifiers.of("final");

    public static $modifiers NOT_ABSTRACT = $modifiers.not("abstract");
    public static $modifiers NOT_STATIC = $modifiers.not("static");
    public static $modifiers NOT_FINAL = $modifiers.not("final");

    public static $modifiers SYNCHRONIZED = $modifiers.of("synchronized");
    public static $modifiers TRANSIENT = $modifiers.of("transient");
    public static $modifiers VOLATILE = $modifiers.of("volatile");
    public static $modifiers NATIVE = $modifiers.of("native");
    public static $modifiers STRICT_FP = $modifiers.of("strictfp");

    public static $modifiers of(){
        return new $modifiers();
    }
    
    public static $modifiers of( Predicate<_modifiers> constraint ){
        return of().$and(constraint);
    }
    
    public static $modifiers of( _hasModifiers _hm ){
        return $modifiers.of( _hm.getModifiers() );
    }
    
    public static $modifiers of(String...mods){
        return of(_modifiers.of(mods));
    }

    /**
     * Composite many $modifiers together
     * including their constraints and mustInclude / mustExclude
     * @param $mods
     * @return
     */
    public static $modifiers of( $modifiers...$mods ){
        $modifiers $mm = of();
        for(int i=0;i<$mods.length;i++){
            $mm.constraint = $mm.constraint.and($mods[i].constraint);
            $mm.mustInclude.addAll($mods[i].mustInclude);
            $mm.mustExclude.addAll($mods[i].mustExclude);
        }
        return $mm;
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

    private static final List<Modifier> ALL_MODIFIERS = Ast.MODS_KEYWORD_TO_ENUM_MAP.values().stream().collect(Collectors.toList());

    /**
     * Matches "Exactly these" modifiers...
     * <PRE>
     * for example
     * $modifiers $onlyStatic = $modifiers.as("static");
     * assertTrue( $onlyStatic.matches("static"));
     *
     * //ANY modifiers NOT in the constructor are explicitly forbidden to match
     * assertFalse( $onlyStatic.matches("public static"));
     * assertFalse( $onlyStatic.matches("public static final "));
     * </PRE>
     * @param mods
     * @return
     */
    public static $modifiers as(String...mods) {
        $modifiers $ms = of( mods );
        //make all the other modifiers MUST exclude
        List<Modifier> leftModifiers = ALL_MODIFIERS.stream().collect(Collectors.toList());
        leftModifiers.removeAll($ms.mustInclude);
        for(int i=0;i<Ast.MODS_KEYWORD_TO_ENUM_MAP.values().size(); i++){
            $ms.mustExclude.addAll(leftModifiers);
        }
        return $ms;
    }

    public static $modifiers as(_hasModifiers _hm){
        return as(_hm.getModifiers());
    }

    public static $modifiers as(_modifiers _ms ){
        return as(_ms.asKeywords());
    }

    public static $modifiers as($modifiers...mods) {
        $modifiers $ms = of( mods );
        //make all the other modifiers MUST exclude
        List<Modifier> leftModifiers = ALL_MODIFIERS.stream().collect(Collectors.toList());
        leftModifiers.removeAll($ms.mustInclude);
        for(int i=0;i<Ast.MODS_KEYWORD_TO_ENUM_MAP.values().size(); i++){
            $ms.mustExclude.addAll(leftModifiers);
        }
        return $ms;
    }

    public static $modifiers.Or or( $modifiers...$tps ){
        return new $modifiers.Or($tps);
    }

    /**
     *
     * @param keywords
     * @return
     */
    public static $modifiers not(String...keywords){
        $modifiers $ms = $modifiers.of();
        _modifiers _ms = _modifiers.of(keywords);

        Arrays.stream( _ms.asKeywords() ).forEach(m -> $ms.mustExclude.add( Ast.MODS_KEYWORD_TO_ENUM_MAP.get(m) ) );
        return $ms;
    }



    /**
     * Matches sets of modifiers that have None of these modifiers
     * @param mods
     * @return 
     */
    public static $modifiers not(Collection<Modifier> mods ){
        $modifiers $mods = new $modifiers();
        $mods.mustExclude.addAll(mods);
        return $mods;
    }
    
    /**
     * 
     * @param mods
     * @return 
     */
    public static $modifiers not(Modifier... mods ){
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

    public $modifiers $(String target, String $paramName){
        return this;
    }

    public boolean matches(String...modifiers ){
        try {
            return matches(_modifiers.of(modifiers));
        }catch(Exception e){
            return false;
        }
    }
    public boolean matches( _modifiers _ms ){
        return select(_ms) != null;
    }

    public $modifiers $and(Predicate<_modifiers> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    /**
     *
     * @param mods
     * @return
     */
    public $modifiers $not($modifiers... mods ){
        for(int i=0;i<mods.length;i++) {
            this.mustExclude.addAll(mods[i].mustInclude);
            this.mustInclude.addAll(mods[i].mustExclude);
            boolean isMatchAny = true;
            try{
                mods[i].constraint.test(null);
            } catch(Exception e){
                //ONLY add and negate the constraint if it's NOT a match any constraint
                //OTHERWISE WE'LL NEVER MATCH
                $and(mods[i].constraint.negate());
            }
        }
        return this;
    }

    @Override
    public $modifiers hardcode$(Translator translator, Tokens kvs) {
        //hmm... just return i guess
        return this;
    }

    public String toString(){
        if(this.isMatchAny() ){
            return "$modifiers{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$modifiers{").append(System.lineSeparator());
        if( !mustInclude.isEmpty() ){
            sb.append("    { ");
            Modifier[] kws = this.mustInclude.toArray(new Modifier[0]);
            for(int i=0;i<kws.length;i++){
                if( i > 0){
                    sb.append(", ");
                }
                sb.append(kws[i].getKeyword().asString());
            }
            sb.append(" }").append(System.lineSeparator());
        }
        if( !mustExclude.isEmpty()){
            sb.append("    NOT { ");
            Modifier[] kws = this.mustExclude.toArray(new Modifier[0]);
            for(int i=0;i<kws.length;i++){
                if( i > 0){
                    sb.append(",");
                }
                sb.append(kws[i].getKeyword().asString());
            }
            sb.append(" }").append(System.lineSeparator());
        }
        sb.append("}");
        return sb.toString();
    }

    public _modifiers draft(Translator translator, Map<String,Object> keyValues){
        //parameter override
        if( keyValues.get("$modifiers") != null){
            Object mods = keyValues.get("$modifiers");
            Map<String,Object> ii = new HashMap<>();
            ii.putAll(keyValues);
            ii.remove("$modifiers");
            if( mods instanceof $modifiers ){
                return (($modifiers)mods).draft(translator, ii);
            }
            return $modifiers.of( mods.toString() ).draft( translator, ii);
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
     * @param astStartNode the
     * @param _modsMatchFn 
     * @return 
     */
    @Override
    public _modifiers firstIn(Node astStartNode, Predicate<_modifiers> _modsMatchFn){
        Optional<Node> f =                 
            astStartNode.findFirst(
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
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        forSelectedIn(astNode, f-> found.add(f) );
        return found;
    }

    @Override
    public <N extends Node> N removeIn(N astNode) {
        //"remove" for modifiers is.... interesting... it "should" mean take them 
        //out AND leave the underlying NodeList to be empty
        //it sorta doesnt make sense
        throw new UnsupportedOperationException("Cant remove modifiers"); 
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_modifiers> _modifiersMatchFn, Consumer<_modifiers> _nodeActionFn) {
        return Walk.in(astNode,
            Node.class, 
            n->{
                if(n instanceof NodeWithModifiers ){
                    Select sel = select( (NodeWithModifiers)n );
                    if( sel != null && _modifiersMatchFn.test(sel._mods)){
                        _nodeActionFn.accept(sel._node());
                    }                
                }
            });        
    }

    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
        return Walk.in( astRootNode,
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
        return Walk.in( astRootNode,
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
    
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    public <_J extends _mrJava> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return Walk.in(_j,
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
    public <_CT extends _type> _CT  forSelectedIn(Class clazz, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectActionFn
     * @return 
     */
    public <_J extends _mrJava> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
        return Walk.in(_j,
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

    public boolean isMatchAny(){
        try{
            return this.constraint.test(null) && this.mustExclude.isEmpty() && this.mustInclude.isEmpty();
        }catch(Exception e){
            //System.out.println("MODIFIERS NOT MATCH ANY" );
            return false;
        }
    }

    public boolean match( _mrJava _j ) {
        if (_j instanceof _modifiers) {
            return matches( (_modifiers)_j);
        }
        if( _j instanceof _modifiers._hasModifiers) {
            return matches( (_modifiers._hasModifiers)_j);
        }
        return false;
    }

    public boolean matches( _hasModifiers _hm){
        return select(_hm) != null;
    }

    public boolean matches( NodeWithModifiers nwm ){
        return select( nwm ) != null;
    }

    public $tokens parse(_modifiers._hasModifiers _ms){
        if( select(_ms) != null){
            return $tokens.of();
        }
        return null;
    }

    public $tokens parse(_modifiers _ms){
        if( select(_ms) != null){
            return $tokens.of();
        }
        return null;
    }

    public Select select(_hasModifiers _hm ){
        return select(_hm.getModifiers());
    }
    
    public Select select(NodeWithModifiers astNwm ){
        return select(_modifiers.of(astNwm));
    }
    
    public Select select(_modifiers _ms){
        /* OLD
        if( this.constraint.test(_ms) &&
            _ms.containsAll(this.mustInclude) &&
            !_ms.containsAny(this.mustExclude) ){
            return new Select( _ms );
        }
         */
        //if it's "connected" to an AST tree node... I should check the IMPLIED modifiers
        NodeWithModifiers nwm = _ms.astHolder();
        if( nwm != null ){
            //System.out.println( "testing " + nwm );
            //System.out.println( "before " +_modifiers.of(nwm));
            NodeList<Modifier> all = Ast.getImpliedModifiers(nwm);
            //try {
                all = Ast.merge(all, nwm.getModifiers());
            //}catch(Exception e){
                //System.out.println(" FFFF "+ e );
            //}
            //System.out.println( "after " +all);
            _modifiers _all = _modifiers.of( all );
            //System.out.println( "after " +_all);
            if( this.constraint.test(_all) &&
                    _all.containsAll(this.mustInclude) &&
                    !_all.containsAny(this.mustExclude) ){
                return new Select( _all );
            }
        }
        if( this.constraint.test(_ms) &&
                _ms.containsAll(this.mustInclude) &&
                !_ms.containsAny(this.mustExclude) ){
            return new Select( _ms );
        }
        return null;
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $modifiers{

        final List<$modifiers>ors = new ArrayList<>();

        public Or($modifiers...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $modifiers hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$modifiers.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param _m
         * @return
         */
        public $modifiers.Select select(_modifiers _m){
            $modifiers $a = whichMatch(_m);
            if( $a != null ){
                return $a.select(_m);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $method that matches the Method or null if none of the match
         * @param ae
         * @return
         */
        public $modifiers whichMatch(_modifiers ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$modifiers> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }
    
    public static class Select implements selected, select_java<_modifiers> {

        public _modifiers _mods;

        public $tokens tokens = new $tokens(new Tokens());
        
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
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public _modifiers _node() {
            return _mods;
        }

        @Override
        public String toString(){
            return "$modifiers.Select{"+ System.lineSeparator()+
                    Text.indent( _mods.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                    "}";
        }
    }
}
