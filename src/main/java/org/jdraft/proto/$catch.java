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
 * prototype for querying and composing a {@link CatchClause}
 *
 * @author Eric
 */
public final class $catch implements $proto<CatchClause> {
    
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
    public CatchClause firstIn(Node astStartNode, Predicate<CatchClause> catchMatchFn) {
        Optional<CatchClause> occ = 
            astStartNode.findFirst(CatchClause.class, cc-> {
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
        return selectFirstIn(_java.type(clazz), selectConstraint);
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
        return listSelectedIn( _java.type(clazz), selectConstraint);
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
     * @param astRootNode
     * @param selectConstraint
     * @return
     */
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

    public boolean match(Node node ){
        if( node instanceof CatchClause ) {
            return select( (CatchClause)node) != null;
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
        implements $proto.selected, $proto.selectedAstNode<CatchClause>{

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
