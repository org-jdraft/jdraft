package org.jdraft.proto;

import org.jdraft.*;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.CatchClause;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * prototype for querying and composing a {@link CatchClause}
 *
 * @author Eric
 */
public final class $catch implements $proto<CatchClause, $catch> {
    
    public static $catch of( String...catchCode ){
        return new $catch( Ast.catchClause(catchCode));
    }
    
    public static $catch of( CatchClause astCatch){
        return new $catch( astCatch );
    }
    
    public static $catch of( CatchClause astCatch, Predicate<CatchClause> constraint){
        return new $catch( astCatch ).$and(constraint);
    }
    
    public static $catch of( Predicate<CatchClause> constraint ){
        return of().$and(constraint);
    }
    
    public static $catch of(){
        return new $catch( $parameter.of(), $body.of() );
    }
    
    public Predicate<CatchClause> constraint = t-> true;
    
    public $parameter $param = $parameter.of();
    
    public $body $bd = $body.of();
    
    public $catch(CatchClause astCC){
        $param = $parameter.of( astCC.getParameter() );
        $bd = $body.of( astCC );
    }
    
    public $catch($parameter $param, $body $bd){
        this.$param = $param;
        this.$bd = $bd;
    }

    public $catch $and(Predicate<CatchClause> constraint){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    @Override
    public $catch hardcode$(Translator translator, Tokens kvs) {
        this.$bd = this.$bd.hardcode$(translator, kvs);
        this.$param = this.$param.hardcode$(translator, kvs);
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
    public CatchClause firstIn(Node astNode, Predicate<CatchClause> catchMatchFn) {
        Optional<CatchClause> occ = 
            astNode.findFirst(CatchClause.class, cc-> {
                Select sel = select(cc);
                return sel != null && catchMatchFn.test(cc);
            });
        if( occ.isPresent() ){
            return occ.get();
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
    public Select selectFirstIn(_java _j, Predicate<Select> selectConstraint) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return selectFirstIn( ((_code)_j).astCompilationUnit(), selectConstraint);
            } else{                
                return selectFirstIn( ((_type)_j).ast(), selectConstraint);
            }
        }        
        return selectFirstIn( ((_node)_j).ast(), selectConstraint);
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
    public <_J extends _java> List<Select> listSelectedIn(_J _j, Predicate<Select> selectConstraint) {
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return listSelectedIn( ((_code) _j).astCompilationUnit(), selectConstraint);
            } else{                
                return listSelectedIn( ((_type) _j).ast(), selectConstraint);
            }
        }        
        return listSelectedIn( ((_node) _j).ast(), selectConstraint);
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
    public <N extends Node> N forEachIn(N astNode, Predicate<CatchClause> catchMatchFn, Consumer<CatchClause> catchActionFn) {
        astNode.walk(CatchClause.class, cc-> {
                if( matches(cc) && catchMatchFn.test(cc)){
                    catchActionFn.accept(cc);
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
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select> nodeActionFn) {
        _walk.in(_j, CatchClause.class, cc-> {
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
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select> selectConstraint, Consumer<Select> nodeActionFn) {
        _walk.in(_j, CatchClause.class, cc-> {
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
    
    public Select select(CatchClause astCatch){
        if( !constraint.test(astCatch)){
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
    
    public static class Select 
        implements $proto.selected, selectAst<CatchClause> {

        public $tokens tokens;
        public CatchClause astCatchClause;
        
        public Select(CatchClause cc, $tokens tokens){
            this.astCatchClause = cc;
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens() {
            return tokens;
        }

        @Override
        public CatchClause ast() {
            return astCatchClause;
        }

        @Override
        public String toString(){
            return "$catch.Select{"+ System.lineSeparator()+
                    Text.indent( astCatchClause.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                    "}";
        }
    }    
}
