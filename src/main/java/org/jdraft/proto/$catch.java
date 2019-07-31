package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._type;
import org.jdraft._walk;
import org.jdraft.Ast;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.CatchClause;
import org.jdraft.Tokens;
import org.jdraft._node;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @author Eric
 */
public class $catch implements $proto<CatchClause> {
    
    public static $catch of( String...catchCode ){
        return new $catch( Ast.catchClause(catchCode));
    }
    
    public static $catch of( CatchClause astCatch){
        return new $catch( astCatch );
    }
    
    public static $catch of( CatchClause astCatch, Predicate<CatchClause> constraint){
        return new $catch( astCatch ).addConstraint(constraint);
    }
    
    public static $catch of( Predicate<CatchClause> constraint ){
        return any().addConstraint(constraint);
    }
    
    public static $catch any(){
        return new $catch( $parameter.any(), $body.any() );
    }
    
    public Predicate<CatchClause> constraint = t-> true;
    
    public $parameter $param = $parameter.any();
    
    public $body $bd = $body.any();
    
    public $catch(CatchClause astCC){
        $param = $parameter.of( astCC.getParameter() );
        $bd = $body.of( astCC );
    }
    
    public $catch($parameter $param, $body $bd){
        this.$param = $param;
        this.$bd = $bd;
    }

    public $catch addConstraint( Predicate<CatchClause> constraint){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    public $catch $parameter(){
        this.$param = $parameter.any();
        return this;
    }
    
    public $catch $body(){
        this.$bd = $body.any();
        return this;
    }
    
    @Override
    public CatchClause firstIn(Node astRootNode, Predicate<CatchClause> catchMatchFn) {
        Optional<CatchClause> occ = 
            astRootNode.findFirst(CatchClause.class, cc-> {
                Select sel = select(cc);
                return sel != null && catchMatchFn.test(cc);
            });
        if( occ.isPresent() ){
            return occ.get();
        }
        return null;
    }

    @Override
    public Select selectFirstIn(Node n) {
        Optional<CatchClause> occ = 
            n.findFirst(CatchClause.class, cc-> matches(cc));
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
        return selectFirstIn(_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param _n
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(_java _n, Predicate<Select> selectConstraint) {
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return selectFirstIn( ((_code)_n).astCompilationUnit(), selectConstraint);
            } else{                
                return selectFirstIn( ((_type)_n).ast(), selectConstraint);
            }
        }        
        return selectFirstIn( ((_node)_n).ast(), selectConstraint);
    }
    
    /**
     * 
     * @param n
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Node n, Predicate<Select> selectConstraint) {
        Optional<CatchClause> occ = 
            n.findFirst(CatchClause.class, cc->{
                Select sel = select(cc);
                return sel != null && selectConstraint.test(sel);                    
            });
        if( occ.isPresent() ){
            return select(occ.get());
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astRootNode) {
        List<Select> sels = new ArrayList<>();
        astRootNode.walk(CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null ){
                    sels.add( sel );
                }
            });
        return sels;
    }
    
    public List<Select> listSelectedIn(Class clazz, Predicate<Select> selectConstraint) {
        return listSelectedIn( _java.type(clazz), selectConstraint);
    }
    
    public <N extends _java> List<Select> listSelectedIn(N _n, Predicate<Select> selectConstraint) {
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return listSelectedIn( ((_code)_n).astCompilationUnit(), selectConstraint);
            } else{                
                return listSelectedIn( ((_type)_n).ast(), selectConstraint);
            }
        }        
        return listSelectedIn( ((_node)_n).ast(), selectConstraint);
        
        //return listSelectedIn(_n.ast(), selectConstraint);
    }
    
    public List<Select> listSelectedIn(Node astRootNode, Predicate<Select> selectConstraint) {
        List<Select> sels = new ArrayList<>();
        astRootNode.walk(CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null && selectConstraint.test(sel)){
                    sels.add( sel );
                }
            });
        return sels;
    }    
        
    @Override
    public <N extends Node> N forEachIn(N astRootNode, Predicate<CatchClause> catchMatchFn, Consumer<CatchClause> catchActionFn) {
        astRootNode.walk(CatchClause.class, cc-> {                
                if( matches(cc) && catchMatchFn.test(cc)){
                    catchActionFn.accept(cc);
                }
            });
        return astRootNode;
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
    public _type forSelectedIn(Class clazz, Consumer<Select> nodeActionFn) {
        return forSelectedIn(_java.type(clazz), nodeActionFn);
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public _type forSelectedIn(Class clazz, Predicate<Select> selectConstraint, Consumer<Select> selectActionFn) {
        return forSelectedIn(_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param nodeActionFn
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Consumer<Select> nodeActionFn) {
        _walk.in(_n, CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null ){
                    nodeActionFn.accept(sel);
                }
            });
        return _n;
    } 
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param selectConstraint
     * @param nodeActionFn
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Predicate<Select> selectConstraint, Consumer<Select> nodeActionFn) {
        _walk.in(_n, CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null && selectConstraint.test(sel)){
                    nodeActionFn.accept(sel);
                }
            });
        return _n;
    } 
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectConstraint
     * @param nodeActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select> selectConstraint, Consumer<Select> nodeActionFn) {
        astRootNode.walk(CatchClause.class, cc-> {
                Select sel = select(cc);
                if( sel != null && selectConstraint.test(sel)){
                    nodeActionFn.accept(sel);
                }
            });
        return astRootNode;
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
        Tokens ts = ps.args.asTokens();
        $body.Select bs = this.$bd.select(astCatch);
        if( bs == null ){
            return null;
        }
        if( ts.isConsistent(bs.args.asTokens())){
            ts.putAll(bs.args.asTokens());
            return new Select(astCatch, $args.of(ts));
        }
        return null;        
    }
    
    public static class Select 
        implements $proto.selected<CatchClause>, $proto.selectedAstNode<CatchClause>{

        public $args args;
        public CatchClause astCatchClause;
        
        public Select(CatchClause cc, $args args){
            this.astCatchClause = cc;
            this.args = args;
        }
        
        @Override
        public $args args() {
            return args;
        }

        @Override
        public CatchClause ast() {
            return astCatchClause;
        }        
    }    
}
