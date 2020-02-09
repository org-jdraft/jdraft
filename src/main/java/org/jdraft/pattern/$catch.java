package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.CatchClause;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jdraft.*;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

/**
 * prototype for querying and composing a {@link CatchClause}
 *
 * @author Eric
 */
public class $catch implements $pattern<_catch, $catch>, $body.$part, $method.$part, $constructor.$part {
    
    public static $catch of( String...catchCode ){
        return new $catch( Ast.catchClause(catchCode));
    }

    public static $catch of( Class<? extends Throwable>... caughtExceptions ){
        return of( $parameter.of( $typeRef.or(caughtExceptions) ) );
    }

    public static $catch of( CatchClause astCatch){
        return new $catch( astCatch );
    }
    
    public static $catch of( CatchClause astCatch, Predicate<_catch> constraint){
        return new $catch( astCatch ).$and(constraint);
    }
    
    public static $catch of( Predicate<_catch> constraint ){
        return of().$and(constraint);
    }

    public static $catch of( $parameter $param ){
        return of($param, $body.of());
    }

    public static $catch of( $body.$part...bodyParts ){
        return of( $body.of(bodyParts) );
    }

    public static $catch of( $body $b ){
        return of( $parameter.of(), $b);
    }

    public static $catch of( $parameter $param, $body $b ){
        return new $catch( $param, $b);
    }

    public static $catch of(){
        return new $catch( $parameter.of(), $body.of() );
    }

    public static $catch.Or or( CatchClause... _protos ){
        $catch[] arr = new $catch[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $catch.of( _protos[i]);
        }
        return or(arr);
    }

    public static $catch.Or or( $catch...$tps ){
        return new $catch.Or($tps);
    }

    public static $catch as( String...catchCode ){
        return as(Ast.catchClause(catchCode));
    }

    /**
     * exact match for catchClause
     */
    public static $catch as(CatchClause astCatch ){
        return new $catch( $parameter.as(astCatch.getParameter()), $body.as(astCatch.getBody()) );
    }

    public Predicate<_catch> constraint = t-> true;
    
    public $parameter $param = $parameter.of();
    
    public $body $bd = $body.of();

    $catch(){
    }

    public $catch(CatchClause astCC){
        $param = $parameter.of( astCC.getParameter() );
        $bd = $body.of( astCC );
    }
    
    public $catch($parameter $param, $body $bd){
        this.$param = $param;
        this.$bd = $bd;
    }

    public $catch $and( $parameter $p ){
        Predicate<_catch>pcc = cc-> $p.matches( cc.ast().getParameter() );
        return $and( pcc );
    }

    public $catch $and( $body $bd ){
        Predicate<_catch>pcc = cc-> $bd.matches( _body.of(cc.getBody()) );
        return $and( pcc );
    }

    public $catch $not( $body $bd ){
        Predicate<_catch>pcc = cc-> $bd.matches( cc.getBody() );
        return $and( pcc.negate() );
    }

    public $catch $not( $parameter $p ){
        Predicate<_catch>pcc = cc-> $p.matches( cc.ast().getParameter() );
        return $and( pcc.negate() );
    }

    public $catch $and(Predicate<_catch> constraint){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    public String toString(){
        if( isMatchAny() ){
            return "$catch{ $ANY$ }";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$catch{").append(System.lineSeparator());
        sb.append( Text.indent( this.$param.toString()) );
        sb.append( Text.indent( this.$bd.toString()) );
        sb.append("}");
        return sb.toString();
    }

    @Override
    public $catch hardcode$(Translator translator, Tokens kvs) {
        this.$bd = this.$bd.hardcode$(translator, kvs);
        this.$param = this.$param.hardcode$(translator, kvs);
        return this;
    }

    public $catch $(String target, String $paramName){
        this.$bd.$(target, $paramName);
        this.$param.$(target, $paramName);
        return this;
    }
    public $catch $parameter(){
        this.$param = $parameter.of();
        return this;
    }
    
    public $catch $body(){
        this.$bd = $body.of();
        return this;
    }
    
    @Override
    public _catch firstIn(Node astNode, Predicate<_catch> catchMatchFn) {
        Optional<CatchClause> occ = 
            astNode.findFirst(CatchClause.class, cc-> {
                Select sel = select(cc);
                return sel != null && catchMatchFn.test(sel._cc);
            });
        if( occ.isPresent() ){
            return _catch.of(occ.get());
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node astNode) {
        Optional<CatchClause> occ = 
            astNode.findFirst(CatchClause.class, cc-> matches(cc));
        if( occ.isPresent() ){
            return select(occ.get());
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Class clazz, Predicate<Select> selectConstraint) {
        return selectFirstIn((_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(_java._domain _j, Predicate<Select> selectConstraint) {
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                return selectFirstIn( ((_compilationUnit)_j).astCompilationUnit(), selectConstraint);
            } else{                
                return selectFirstIn( ((_type)_j).ast(), selectConstraint);
            }
        }        
        return selectFirstIn( ((_java._compound)_j).ast(), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Node astNode, Predicate<Select> selectConstraint) {
        Optional<CatchClause> occ = 
            astNode.findFirst(CatchClause.class, cc->{
                Select sel = select(cc);
                return sel != null && selectConstraint.test(sel);                    
            });
        if( occ.isPresent() ){
            return select(occ.get());
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> sels = new ArrayList<>();
        astNode.walk(CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null ){
                    sels.add( sel );
                }
            });
        return sels;
    }

    /**
     *
     * @param clazz
     * @param selectConstraint
     * @return
     */
    public List<Select> listSelectedIn(Class clazz, Predicate<Select> selectConstraint) {
        return listSelectedIn( (_type)_java.type(clazz), selectConstraint);
    }

    /**
     *
     * @param _j
     * @param selectConstraint
     * @param <_J>
     * @return
     */
    public <_J extends _java._domain> List<Select> listSelectedIn(_J _j, Predicate<Select> selectConstraint) {
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                return listSelectedIn( ((_compilationUnit) _j).astCompilationUnit(), selectConstraint);
            } else{                
                return listSelectedIn( ((_type) _j).ast(), selectConstraint);
            }
        }        
        return listSelectedIn( ((_java._compound) _j).ast(), selectConstraint);
    }

    /**
     *
     * @param astNode
     * @param selectConstraint
     * @return
     */
    public List<Select> listSelectedIn(Node astNode, Predicate<Select> selectConstraint) {
        List<Select> sels = new ArrayList<>();
        astNode.walk(CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null && selectConstraint.test(sel)){
                    sels.add( sel );
                }
            });
        return sels;
    }    
        
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_catch> catchMatchFn, Consumer<_catch> catchActionFn) {
        astNode.walk(CatchClause.class, cc-> {
            _catch _cc = _catch.of(cc);
                if( matches(cc) && catchMatchFn.test(_cc)){
                    catchActionFn.accept(_cc);
                }
            });
        return astNode;
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param nodeActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> nodeActionFn) {
        astRootNode.walk(CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null){
                    nodeActionFn.accept(sel);
                }
            });
        return astRootNode;
    }

    /**
     * 
     * @param clazz
     * @param nodeActionFn
     * @return 
     */
    public  <_CT extends _type> _CT  forSelectedIn(Class clazz, Consumer<Select> nodeActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), nodeActionFn);
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public  <_CT extends _type> _CT  forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param nodeActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select> nodeActionFn) {
        Walk.in(_j, CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null ){
                    nodeActionFn.accept(sel);
                }
            });
        return _j;
    } 
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param nodeActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> nodeActionFn) {
        Walk.in(_j, CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null && selectConstraint.test(sel)){
                    nodeActionFn.accept(sel);
                }
            });
        return _j;
    } 
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConstraint
     * @param nodeActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select> selectConstraint, Consumer<Select> nodeActionFn) {
        astNode.walk(CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null && selectConstraint.test(sel)){
                    nodeActionFn.accept(sel);
                }
            });
        return astNode;
    }

    public boolean match(Node astNode ){
        if( astNode instanceof CatchClause ) {
            return select( (CatchClause)astNode) != null;
        }
        return false;
    }

    public boolean matches(String...catchClause){
        return select(catchClause) != null;
    }
    
    public boolean matches(CatchClause astCatch ){
        return select(astCatch) != null;
    }
    
    public Select select(String...catchClause ){
        return select(Ast.catchClause(catchClause));
    }

    public Select select(_catch _cc){
        if( !constraint.test(_cc)){
            return null;
        }
        $parameter.Select ps = this.$param.select(_cc.ast().getParameter());
        if( ps == null ){
            return null;
        }
        Tokens ts = ps.tokens.asTokens();
        $body.Select bs = this.$bd.select(_cc);
        if( bs == null ){
            return null;
        }
        if( ts.isConsistent(bs.tokens.asTokens())){
            ts.putAll(bs.tokens.asTokens());
            return new Select(_cc, $tokens.of(ts));
        }
        return null;
    }
    public Select select(CatchClause astCatch){
        if( !constraint.test(_catch.of(astCatch))){
            return null;
        }
        $parameter.Select ps = this.$param.select(astCatch.getParameter());
        if( ps == null ){
            return null;
        }
        Tokens ts = ps.tokens.asTokens();
        $body.Select bs = this.$bd.select(astCatch);
        if( bs == null ){
            return null;
        }
        if( ts.isConsistent(bs.tokens.asTokens())){
            ts.putAll(bs.tokens.asTokens());
            return new Select(astCatch, $tokens.of(ts));
        }
        return null;        
    }

    public boolean isMatchAny(){
        try{
            return this.constraint.test(null) && this.$param.isMatchAny() && this.$bd.isMatchAny();
        } catch(Exception e){
            return false;
        }
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $catch $isAfter( $pattern... patternsOccurringBeforeThisNode ){
        Predicate<_catch> prev = e -> $pattern.BodyScope.findPrevious(e.ast(), patternsOccurringBeforeThisNode) != null;
        return $and(prev);
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $catch $isNotAfter( $pattern... patternsOccurringBeforeThisNode ){
        Predicate<_catch> prev = e -> $pattern.BodyScope.findPrevious(e.ast(), patternsOccurringBeforeThisNode) != null;
        return $not(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $catch $isBefore( $pattern... patternsOccurringAfterThisNode ){
        Predicate<_catch> prev = e -> $pattern.BodyScope.findNext(e.ast(), patternsOccurringAfterThisNode) != null;
        return $and(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $catch $isNotBefore( $pattern... patternsOccurringAfterThisNode ){
        Predicate<_catch> prev = e -> $pattern.BodyScope.findNext(e.ast(), patternsOccurringAfterThisNode) != null;
        return $not(prev);
    }


    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $catch{

        final List<$catch>ors = new ArrayList<>();

        public Or($catch...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $catch hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$catch.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param astNode
         * @return
         */
        public $catch.Select select(CatchClause astNode){
            $catch $a = whichMatch(astNode);
            if( $a != null ){
                return $a.select(astNode);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        public $catch whichMatch(_catch _cc){
            if( !this.constraint.test( _cc ) ){
                return null;
            }
            Optional<$catch> orsel  = this.ors.stream().filter( $p-> $p.match(_cc) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
        /**
         * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
         * @param ae
         * @return
         */
        public $catch whichMatch(CatchClause ae){
            return whichMatch(_catch.of(ae));
        }
    }

    public static class Select 
        implements $pattern.selected, selectAst<CatchClause> {

        public $tokens tokens;
        public _catch _cc;

        public Select(_catch _cc, $tokens tokens){
            this._cc = _cc;
            this.tokens = tokens;
        }

        public Select(CatchClause cc, $tokens tokens){
            this._cc = _catch.of(cc);
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public CatchClause ast() {
            return _cc.ast();
        }

        @Override
        public String toString(){
            return "$catch.Select{"+ System.lineSeparator()+
                    Text.indent( _cc.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                    "}";
        }
    }    
}
