package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._type;
import org.jdraft._body;
import org.jdraft.Expr;
import org.jdraft.Ast;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.jdraft.*;
import org.jdraft._node;
import org.jdraft.proto.$proto.$args;
import org.jdraft.proto.$proto.selected;
import java.util.*;
import java.util.function.*;

/**
 * Prototype of a body
 * @author Eric
 */
public class $body implements Template<_body>, $proto<_body>, $constructor.$part, $method.$part{
    
    public static $body any(){
        return of();
    }
    
    /**
     * ANY body (or lack of body)... so 
     * 
     * @return 
     */
    public static $body of(){
        return new $body(); 
    }
    
    public static $body of( String body ){
        return new $body( _body.of(body));
    }
    
    public static $body of( String...body ){
        return new $body( _body.of(body));
    }
    
    public static $body of( _body _bd ){
        return new $body(_bd);
    }
    
    
    public static $body of( NodeWithBlockStmt astNodeWithBlock ){
        return new $body(_body.of(astNodeWithBlock));
    }
    
    public static $body of( NodeWithOptionalBlockStmt astNodeWithBlock ){
        return new $body(_body.of(astNodeWithBlock));
    }
    
    public static $body of(Expr.Command commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static $body of(Consumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static $body of(BiConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static $body of(Expr.TriConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static $body of(Expr.QuadConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static <A extends Object, B extends Object> $body of(Function<A,B> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static <A extends Object, B extends Object, C extends Object>  $body of(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static <A extends Object, B extends Object, C extends Object, D extends Object> $body of(Expr.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object>  $body of(Expr.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public static $body notImplemented(){
        return new $body().addConstraint( b-> !b.isImplemented());
    }
    
    public static $body empty(){
        return new $body().addConstraint(b -> b.isEmpty() );
    }
    
    public Predicate<_body> constraint = t->true;
    
    public Boolean isImplemented = true;    
    
    public $stmt bodyStmts = null;
    
    /**
     * This represents a "non implemented body"
     * which is 
     */
    private $body( ){
        this.isImplemented = false;        
    }
    
    
    public $body( LambdaExpr le ){
        this( _body.of(le.getBody().toString(Ast.PRINT_NO_COMMENTS )) );
    }
    
    /**
     * 
     * @param body 
     */
    private $body( _body body ){
        if( body.isImplemented() ){
            this.bodyStmts = $stmt.of(body.ast());
            this.isImplemented = true;
        } else{
            this.isImplemented = false;
        }        
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public $body addConstraint( Predicate<_body> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    /**
     * 
     * @param body
     * @return 
     */
    public $body $bodyStmts(_body body ){
        if( body == null ){
            this.isImplemented = false;
            this.bodyStmts = null;
        } else{
            this.isImplemented = true;
            this.bodyStmts = $stmt.of(body.ast());
        }
        return this;
    }
    
    @Override
    public List<String> list$(){
        if(this.isImplemented){
            return this.bodyStmts.list$();
        }
        return Collections.EMPTY_LIST;
    }
    
    
    @Override
    public List<String> list$Normalized(){
        if( isImplemented ){
            return this.bodyStmts.list$Normalized();
        }
        return Collections.EMPTY_LIST;
    }
    
    /**
     * 
     * @param astNwb
     * @return 
     */
    public boolean matches( NodeWithBlockStmt astNwb ){
        return select(astNwb) != null;
    }
    
    /**
     *
     * @param astNwob
     * @return 
     */
    public boolean matches( NodeWithOptionalBlockStmt astNwob ){
        return select(astNwob) != null;
    }
    
    /**
     * 
     * @param body
     * @return 
     */
    public boolean matches(String...body){
        return select(body) != null;
    }
    
    /**
     * 
     * @param body
     * @return 
     */
    public boolean matches( _body body){
        return select(body) != null;
    }
    
    public Select select(Expr.Command commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public Select select(Consumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public Select select(BiConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public Select select(Expr.TriConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public Select select(Expr.QuadConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public <A extends Object, B extends Object> Select select(Function<A,B> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public <A extends Object, B extends Object, C extends Object> Select select(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public <A extends Object, B extends Object, C extends Object, D extends Object>Select select(Expr.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> Select select(Expr.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
     /**
     * 
     * @return 
     */
    public boolean isMatchAny(){
        try{
            return this.constraint.test(null) && this.bodyStmts.isMatchAny();
        }catch(Exception e){
            return false;
        }
    }
    
    /**
     * 
     * @param body
     * @return 
     */
    public Select select (String body ){
        return select(_body.of(body));
    }
    
    /**
     * 
     * @param body
     * @return 
     */
    public Select select (String...body ){
        return select(_body.of(body));
    }
    
    /**
     * 
     * @param body
     * @return 
     */
    public Select select( _body body ){
        if( isMatchAny() ){
            return new Select( body, $args.of());
        }
        if( !this.constraint.test(body)){
            return null;
        }        
        if( !body.isImplemented() ){
            if( this.isImplemented ){
                return null;
            } 
            return new Select(body, $args.of());                
        }       
        if( this.isImplemented ){
            $stmt.Select ss = this.bodyStmts.select((Statement)body.ast());
            if( ss != null ){
                return new Select( body, ss.args);
            }
            return null;
        }
        return new Select( body, $args.of());        
    }
    
    /**
     * 
     * @param nwb
     * @return 
     */
    public Select select( NodeWithBlockStmt nwb ){
        return select(_body.of(nwb));        
    }
    
    /**
     * 
     * @param nwb
     * @return 
     */
    public Select select( NodeWithOptionalBlockStmt nwb ){
        return select(_body.of(nwb ));        
    }

    @Override
    public _body construct(Translator translator, Map<String, Object> keyValues) {
        //they can OVERRIDE the body construction if they pass in a "$body" parameter
        if( keyValues.get("$body")!= null ){
            //this means I want to override the body
            $body $bd = $body.of( keyValues.get("$body").toString() );
            Map<String,Object>tks = new HashMap<>();
            tks.putAll(keyValues);
            tks.remove("$body");
            return $bd.construct(translator, tks);
        }
        if( !this.isImplemented ){
            return _body.of(";");
        }
        return _body.of( $stmt.walkCompose$LabeledStmt(
            (Statement)this.bodyStmts.construct(translator, keyValues),keyValues) );        
    }
    
    /**
     * 
     * @param body
     * @param all
     * @return 
     */
    public Tokens decomposeTo(_body body, Tokens all) {
        if (all == null) { /* Skip decompose if the tokens already null*/
            return null;
        }    
        Select select = select( body );

        if (select != null) {                
            if (all.isConsistent(select.args.asTokens())) {                   
                all.putAll(select.args.asTokens());
                return all;
            }
        }
        return null;
    }
    
    @Override
    public $body $(String target, String $Name) {
        if( this.isImplemented ){            
            this.bodyStmts = this.bodyStmts.$(target, $Name);
        }
        return this;
    }
    
    public $body $( Statement st, String name){
        if( this.isImplemented ){
            this.bodyStmts = this.bodyStmts.$(st, name);
        }
        return this;
    }
    
    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $body hardcode$(Translator translator, Tokens kvs ) {
        if( this.isImplemented ){
            this.bodyStmts.hardcode$(translator, kvs);
        }
        return this;
    }
    
    @Override
    public _body firstIn(Node astRootNode, Predicate<_body> _bodyMatchFn) {
        Optional<Node> on = astRootNode.findFirst(Node.class, n-> { 
            if(n instanceof NodeWithBlockStmt){
                Select sel = select( (NodeWithBlockStmt)n );
                return sel != null && _bodyMatchFn.test(sel.body);
            }
            if(n instanceof NodeWithOptionalBlockStmt){
                Select sel = select( (NodeWithOptionalBlockStmt)n );
                return sel != null  && _bodyMatchFn.test(sel.body);
            }
            return false;
            });
        if( on.isPresent() ){
            Node node = on.get();
            if( node instanceof NodeWithBlockStmt ){
                return _body.of( (NodeWithBlockStmt)node );
            }
            return _body.of( (NodeWithOptionalBlockStmt)node );            
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn( Class clazz, Predicate<Select>selectConstraint){
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
            _code _c = (_code)_n;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_n; //only possible 
            return selectFirstIn(_t.ast(), selectConstraint);
        }
        return selectFirstIn( ((_node)_n).ast(), selectConstraint);
    }
    
    /**
     * 
     * @param astRootNode
     * @param selectConstraint
     * @return 
     */
    public Select selectFirstIn(Node astRootNode, Predicate<Select> selectConstraint) {
        Optional<Node> on = astRootNode.findFirst(Node.class, n-> { 
            if(n instanceof NodeWithBlockStmt){
                Select sel = select( (NodeWithBlockStmt)n );
                return sel != null && selectConstraint.test(sel);
            }
            if(n instanceof NodeWithOptionalBlockStmt){
                Select sel = select( (NodeWithOptionalBlockStmt)n );
                return sel != null && selectConstraint.test(sel);
            }
            return false;
            });
        if( on.isPresent() ){
            Node node = on.get();
            if( node instanceof NodeWithBlockStmt ){
                return select( (NodeWithBlockStmt)node );
            }
            return select( (NodeWithOptionalBlockStmt)node );            
        }
        return null;
    }
    
    @Override
    public Select selectFirstIn(Node astRootNode) {
        Optional<Node> on = astRootNode.findFirst(Node.class, n-> { 
            if(n instanceof NodeWithBlockStmt){
                Select sel = select( (NodeWithBlockStmt)n );
                return sel != null;
            }
            if(n instanceof NodeWithOptionalBlockStmt){
                Select sel = select( (NodeWithOptionalBlockStmt)n );
                return sel != null;
            }
            return false;
            });
        if( on.isPresent() ){
            Node node = on.get();
            if( node instanceof NodeWithBlockStmt ){
                return select( (NodeWithBlockStmt)node );
            }
            return select( (NodeWithOptionalBlockStmt)node );            
        }
        return null;
    }

    @Override
    public List<Select> listSelectedIn(Node astRootNode) {
        List<Select> found = new ArrayList<>();
        forSelectedIn( astRootNode, s-> found.add(s) );
        return found;
    }

    /**
     * 
     * @param clazz
     * @param selectActionFn
     * @return 
     */
    public _type forSelectedIn( Class clazz, Consumer<Select>selectActionFn){
        return forSelectedIn(_java.type(clazz), selectActionFn);
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public _type forSelectedIn( Class clazz, Predicate<Select>selectConstraint,  Consumer<Select>selectActionFn){
        return forSelectedIn(_java.type(clazz), selectConstraint, selectActionFn);
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Consumer<Select> selectActionFn) {
        astRootNode.walk(Node.class, n->{
            if(n instanceof NodeWithBlockStmt){
                Select sel = select( (NodeWithBlockStmt)n );
                if( sel != null ){
                    selectActionFn.accept( sel );
                }
            }
            if(n instanceof NodeWithOptionalBlockStmt){
                Select sel = select( (NodeWithOptionalBlockStmt)n );
                if( sel != null ){
                    selectActionFn.accept( sel);
                }
            }            
        });
        return astRootNode;
    }

    /**
     * 
     * @param <N>
     * @param _n
     * @param selectActionFn
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n,  Consumer<Select> selectActionFn) {
        forSelectedIn(_n, s->true, selectActionFn);
        return _n;
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Predicate<Select>selectConstraint, Consumer<Select> selectActionFn) {
        if( _n instanceof _code ){
            _code _c = (_code)_n;
            if( _c.isTopLevel() ){
                forSelectedIn(_c.astCompilationUnit(), selectActionFn);
                return _n;
            }
            _type _t = (_type)_n; //only possible 
            forSelectedIn(_t.ast(), selectActionFn);
            return _n;
        }
        forSelectedIn((_node)_n, selectActionFn);
        return _n;        
    }
    
    /**
     * 
     * @param <N>
     * @param astRootNode
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astRootNode, Predicate<Select>selectConstraint, Consumer<Select> selectActionFn) {
        astRootNode.walk(Node.class, n->{
            if(n instanceof NodeWithBlockStmt){
                Select sel = select( (NodeWithBlockStmt)n );
                if( sel != null && selectConstraint.test(sel)){
                    selectActionFn.accept( sel );
                }
            }
            if(n instanceof NodeWithOptionalBlockStmt){
                Select sel = select( (NodeWithOptionalBlockStmt)n );
                if( sel != null && selectConstraint.test(sel)){
                    selectActionFn.accept( sel);
                }
            }            
        });
        return astRootNode;
    }
    
    @Override
    public <N extends Node> N forEachIn(N astRootNode, Predicate<_body> _bodyMatchFn, Consumer<_body> _bodyActionFn) {
        astRootNode.walk(Node.class, n->{
            if(n instanceof NodeWithBlockStmt){
                Select sel = select( (NodeWithBlockStmt)n );
                if( sel != null && _bodyMatchFn.test(sel.body) ){
                    _bodyActionFn.accept( sel.body );
                }
            }
            if(n instanceof NodeWithOptionalBlockStmt){
                Select sel = select( (NodeWithOptionalBlockStmt)n );
                if( sel != null  && _bodyMatchFn.test(sel.body) ){
                    _bodyActionFn.accept( sel.body );
                }
            }            
        });
        return astRootNode;
    }
    
    /**
     * 
     */
    public static class Select implements selected<_body>, selected_model<_body>{

        public _body body;
        public $args args;
        
        public Select( _body body, $args args){
            this.body = body;
            this.args = args;
        }
        
        @Override
        public $args args() {
            return args;
        }        
        
        public boolean isImplemented(){
            return body.isImplemented();
        }
        
        public boolean isEmpty(){            
            return body.isEmpty();
        }
        
        public Statement getStatement( int index ){                  
            return body.getStatement(index);
        }

        @Override
        public _body model() {
            return this.body;
        }        
    }
}
