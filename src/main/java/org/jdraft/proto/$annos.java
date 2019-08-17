package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import org.jdraft.*;
import org.jdraft._walk;
import org.jdraft._anno;
import org.jdraft._anno._annos;
import org.jdraft._anno._hasAnnos;
import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._node;
import org.jdraft._type;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Prototype of (group of) {@link _annos} for composing and query
 * @author Eric
 */
public final class $annos
    implements Template<_annos>, $proto<_annos, $annos>, $constructor.$part, $method.$part,
        $field.$part, $parameter.$part, $typeParameter.$part {

    /**
     * 
     * @return 
     */
    public static $annos any(){
        return of();
    }
    
    /**
     * Entities that have NO annotations applied to them */
    public static $annos none(){
        return of().and(as -> as.isEmpty());
    }
    
    /**
     * prototype that matches any grouping of annos
     * @return 
     */
    public static $annos of(){
        return new $annos();
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $annos of( Predicate<_annos> constraint ){
       return of().and(constraint);
    }
    
    /**
     * 
     * @param _ha
     * @return 
     */
    public static $annos of( _anno._hasAnnos _ha){
        return new $annos( _ha.getAnnos() ); 
    }
    
    /**
     * 
     * @param $anns
     * @return 
     */
    public static $annos of( $anno...$anns ){
        $annos $as = new $annos();
        Arrays.stream($anns).forEach(a -> $as.$annosList.add(a));
        return $as;
    }
    
    public static $annos of(String...annoPatterns){
        return new $annos(annoPatterns);
    }
    
    public static $annos of(_annos annos){
        return new $annos(annos);
    }
    
    public $annos( String...annoPatterns ){
        this(_annos.of(annoPatterns));
    }
    
    public $annos( _annos _anns){
        if( _anns != null ){
            for(int i=0;i<_anns.size();i++){
                $anno a = $anno.of(_anns.get(i) );
                $annosList.add(a);
            }    
        }        
    }
    
    /** 
     * List of anno prototypes, note: an empty list means it matches ANY list 
     * of _annos
     */
    public List<$anno> $annosList = new ArrayList<>();
    
    /**
     * A Matching predicate for _annos
     */
    public Predicate<_annos> constraint = t-> true;
    
    public boolean isEmpty(){
        return this.$annosList.isEmpty();
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public $annos and(Predicate<_annos> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    /**
     * Will this prototype match any group of annos?
     * @return 
     */
    public boolean isMatchAny(){
        try{
            return( this.constraint.test(null) && 
                this.$annosList.isEmpty() || 
                this.$annosList.size() == 1 
                && this.$annosList.get(0).isMatchAny());
        } catch(Exception e){
            return false;
        }
    }
    
    /**
     * 
     * @param translator
     * @param keyValues
     * @return 
     */
    public String draftToString(Translator translator, Map<String, Object> keyValues){
        return draft(translator, keyValues, System.lineSeparator() );
    }
    
    /**
     * 
     * @param translator
     * @param keyValues
     * @param separator the separator to use between each anno 
     * (usually this will be a line break, but, for parameters we use an " " 
     * empty space
     * 
     * @return 
     */        
    public String draft(Translator translator, Map<String, Object> keyValues, String separator) {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<$annosList.size();i++){            
            if( i > 0 ){
                sb.append(separator);
            }
            sb.append( $annosList.get(i).draft(translator, keyValues) );
        }
        if( $annosList.size() > 0 ){
            //add another separator AFTER the end (a line break of space) so
            // annos dont bleed into other code
            sb.append( separator );
        }
        if( keyValues.get("$annos") != null){ //they can supply the annos            
            $annos $as = $annos.of( keyValues.get("$annos").toString() );
            sb.append($as.draft(translator, keyValues, separator));
        }
        return sb.toString();
    }
    
    @Override
    public _annos draft(Translator translator, Map<String, Object> keyValues) {
        _annos _as = _annos.of();
        for(int i=0;i<$annosList.size();i++){            
            _as.add( $annosList.get(i).draft(translator, keyValues) );
        }
        //handle $name OVERLOAD i.e. if they pass in a $annos
        if( keyValues.get("$annos") != null){ //they can supply the annos            
            $annos $as = $annos.of( keyValues.get("$annos").toString() );
            //must extract the $annos to avoid a stack overflow
            Map<String,Object> kvs = new HashMap<>();
            kvs.putAll(keyValues);
            kvs.remove("$annos");
            _as.addAll( $as.draft(translator, kvs).list() );
            //sb.append($as.compose(translator, keyValues, separator));                
        }
        return _as;
    }

    @Override
    public $annos $(String target, String $Name) {
        $annosList.forEach(a -> a.$(target, $Name));
        return this;
    }

    /**
     *
     * @param tr
     * @param keyValues
     * @return
     */
    public $annos hardcode$( Translator tr, Object... keyValues ) {
        $annosList.forEach(a -> a.hardcode$(tr, keyValues));
        return this;
    }
    
    @Override
    public List<String> list$() {
        List<String>found = new ArrayList<>();
        $annosList.forEach(a -> found.addAll(a.list$()) );
        return found;
    }

    @Override
    public List<String> list$Normalized() {
        List<String>found = new ArrayList<>();
        $annosList.forEach(a -> found.addAll(a.list$Normalized()) );        
        return found.stream().distinct().collect(Collectors.toList());
    }

    public $annos add(String...annoPatterns){
        Arrays.stream(annoPatterns).forEach( a -> this.$annosList.add( $anno.of(a) ) );
        return this;
    }
    
    public boolean matches( NodeWithAnnotations astAnnoNode ){
        return select(astAnnoNode)!= null;
    }
    
    public boolean matches( _annos _as) {
        return select(_as)!= null;
    }
    
    public boolean matches( _hasAnnos _ha) {
        return select(_ha)!= null;
    }
    
    public Select select( NodeWithAnnotations astAnnoNode ){
        return select(_annos.of(astAnnoNode) );
    }

    /**
     *
     * @param _anns
     * @return
     */
    public Select select( _annos _anns ){
        if( ! this.constraint.test(_anns)){
            return null;
        }
        //System.out.println( "annos BEFORE "+_anns);
        //System.out.println( "annos BEFORE "+_anns.list());
        List<_anno> annosLeft = new ArrayList<>();
        annosLeft.addAll( _anns.list() );
        Tokens tokens = new Tokens();
        //_annos _as = _annotated.getAnnos();
        for(int i=0;i<this.$annosList.size();i++){
            
            $anno $a = $annosList.get(i);
            //System.out.println("checking "+ $a );
            Optional<_anno> oa = annosLeft.stream().filter(a-> $a.matches(a)).findFirst();
            if( !oa.isPresent() ){
                //System.out.println( "Not present anno "+$a +"  : "+annosLeft);
                return null; //didnf find a matching anno
            }
            _anno got = oa.get();
           // System.out.println( "annosLeft BEFORE "+annosLeft +" "+ got);
            annosLeft.remove(got);
            //System.out.println( "annosLeft AFTER  "+annosLeft +" "+got);
            $anno.Select $as = $a.select(oa.get());
            if( tokens.isConsistent($as.args.asTokens())){ //args are consistent                
                tokens.putAll($as.args.asTokens());
            } else{
                //System.out.println( "Consistency error for "+tokens +"  : "+$as.args.asTokens() );
                return null;
            }
        }
        //System.out.println("returning new Select for "+_anns);
        return new Select(_anns, tokens);
    }

    /**
     *
     * @param _annotated
     * @return
     */
    public Select select( _hasAnnos _annotated ){
        return select( _annotated.getAnnos() );        
    }

    public boolean match( Node n){
        if( n instanceof NodeWithAnnotations ){
            return matches( (NodeWithAnnotations) n);
        }
        return false;
    }

    /**
     *
     * @param _as
     * @param allTokens
     * @return
     */
    public Tokens parseTo(_annos _as, Tokens allTokens ){
        if(allTokens == null){
            return allTokens;
        }
        Select sel = select(_as);
        if( sel != null ){
            if( allTokens.isConsistent(sel.args.asTokens()) ){
                allTokens.putAll(sel.args.asTokens());
                return allTokens;
            }
        }
        return null;
    }
    
    /**
     * Returns the first Statement that matches the 
     * @param astStartNode the
     * @param _annosMatchFn 
     * @return 
     */
    @Override
    public _annos firstIn(Node astStartNode, Predicate<_annos> _annosMatchFn){
        Optional<Node> f =                 
            astStartNode.findFirst( Node.class,
                n -> {
                    if(n instanceof NodeWithAnnotations){
                        Select sel = select((NodeWithAnnotations)n);
                        return (sel != null && _annosMatchFn.test(sel._anns));
                    } 
                    //TODO test with module-info and package-info
                    return false;
                }
            );         
        
        if( f.isPresent()){
            return _annos.of( (NodeWithAnnotations)f.get());
        }
        return null;
    }    
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn( Class clazz, Predicate<Select>selectConstraint ){
        return selectFirstIn(_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn( _java _j, Predicate<Select> selectConstraint){
         if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return selectFirstIn(_t.ast(), selectConstraint); //return the TypeDeclaration, not the CompilationUnit            
        }
        return selectFirstIn(((_node)_j).ast(), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @return 
     */
    @Override
    public Select selectFirstIn( Node astNode ){
        Optional<Node> f = 
                
            astNode.findFirst( Node.class, 
                n -> (n instanceof NodeWithAnnotations) 
                && matches((NodeWithAnnotations)n) 
            );         
        
        if( f.isPresent()){
            return select( (NodeWithAnnotations)f.get());
        }
        return null;
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn( Node astNode, Predicate<Select> selectConstraint){
        Optional<Node> f = 
                
            astNode.findFirst( Node.class, 
                n -> {
                    if (n instanceof NodeWithAnnotations){ 
                        Select sel = select( (NodeWithAnnotations)n);
                        return sel != null && selectConstraint.test(sel);
                    }
                    return false;
                }
            );         
        
        if( f.isPresent()){
            return select( (NodeWithAnnotations)f.get());
        }
        return null;
    }    
    
    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        _walk.in(astNode, Node.class, _ha-> {
            if( _ha instanceof NodeWithAnnotations ){
                Select sel = select( (NodeWithAnnotations)_ha);
                if( sel != null ){
                    found.add( sel );
                }
            }
        });
        return found;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( Class clazz, Predicate<Select> selectConstraint) {
        return listSelectedIn(_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param <N>
     * @param n
     * @param selectConstraint
     * @return 
     */
    public <N extends Node> List<Select> listSelectedIn( N n, Predicate<Select> selectConstraint ){
        List<Select> sel = new ArrayList<>();
        forSelectedIn( n, selectConstraint, s-> sel.add(s));
        return sel;        
    }
    
    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select> listSelectedIn( _java _j, Predicate<Select> selectConstraint) {
         if( _j instanceof _code ){
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return listSelectedIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return listSelectedIn(_t.ast(), selectConstraint);
        }
         
        return listSelectedIn(((_node)_j).ast(), selectConstraint);
    }

    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_annos> _annosMatchFn, Consumer<_annos> _annosActionFn) {
        return _walk.in(astNode, Node.class, n-> {
            if( n instanceof NodeWithAnnotations ){
                Select sel = select( (NodeWithAnnotations)n );
                if( sel != null && _annosMatchFn.test(sel._anns)){
                    _annosActionFn.accept(sel._anns);
                }
            }
        });        
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
        astRootNode.walk(Node.class, 
            n -> {
                if( n instanceof NodeWithAnnotations ){
                    Select sel = select( (NodeWithAnnotations)n);
                    if( sel != null ){
                        selectActionFn.accept(sel);
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
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode,Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        astRootNode.walk(Node.class, 
            n -> {
                if( n instanceof NodeWithAnnotations ){
                    Select sel = select( (NodeWithAnnotations)n);
                    if( sel != null && selectConstraint.test(sel)){
                        selectActionFn.accept(sel);
                    }
                }            
            });
        return astRootNode;
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
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public _type forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return forSelectedIn( _java.type(clazz), selectConstraint, selectActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectActionFn);
            return _j;
        }
        forSelectedIn((_node) _j, selectActionFn);
        return _j;
    }

    /**
     *
     * @param _j
     * @param selectConstraint
     * @param selectActionFn
     * @param <_J>
     * @return
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectConstraint, selectActionFn);
                return _j;
            }
            _type _t = (_type) _j; //only possible
            forSelectedIn(_t.ast(), selectConstraint, selectActionFn);
            return _j;
        }
        forSelectedIn( ((_node) _j).ast(), selectConstraint, selectActionFn);
        return _j;
    } 
    
    @Override
    public String toString(){
        if( this.isMatchAny()){
            return "$annos(MATCH ANY)";
        }
        StringBuilder sb = new StringBuilder();
        this.$annosList.forEach(a -> sb.append("    ").append(a));
        return sb.toString();
    }
    
    /**
     * A Matched Selection result returned from matching a prototype $a
     * inside of some Node or _node
     */
    public static class Select
        implements $proto.selected, selected_model<_annos> {

        public final _annos _anns;
        public final $args args;

        public Select(_annos _a, Tokens tokens) {
            this(_a, $args.of(tokens));
        }

        public Select(_annos _as, $args tokens) {
            this._anns = _as;
            args = tokens;
        }

        @Override
        public $args args() {
            return args;
        }

        @Override
        public String toString() {
            return "$annos.Select {" + System.lineSeparator()
                + Text.indent(_anns.toString()) + System.lineSeparator()
                + Text.indent("$args : " + args) + System.lineSeparator()
                + "}";
        }

        @Override
        public _annos _node() {
            return _anns;
        }
    }
}
