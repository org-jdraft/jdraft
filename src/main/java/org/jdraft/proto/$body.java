package org.jdraft.proto;

import com.github.javaparser.ast.stmt.BlockStmt;
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

import java.util.*;
import java.util.function.*;

/**
 * Prototype for composing and querying a {@link _body}
 *
 * @author Eric
 */
public final class $body implements Template<_body>, $proto<_body, $body>, $constructor.$part, $method.$part{
    
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
        $body $b = new $body( _body.of(body));
        return $b;
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
        return new $body().and(b-> !b.isImplemented());
    }
    
    public static $body empty(){
        return new $body().and(b -> b.isEmpty() );
    }
    
    public Predicate<_body> constraint = t->true;
    
    public Boolean isImplemented = true;    
    
    public $stmt<BlockStmt> bodyStmts = null;
    
    /**
     * This represents a "non implemented body"
     * which is 
     */
    private $body( ){
        this.isImplemented = false;        
    }

    /**
     *
     * @param le
     */
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
    public $body and(Predicate<_body> constraint ){
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

    public boolean match( Node node ){
        if( node instanceof NodeWithBlockStmt ){
            return matches( (NodeWithBlockStmt) node);
        }
        if( node instanceof NodeWithOptionalBlockStmt ){
            return matches( (NodeWithOptionalBlockStmt) node);
        }
        return false;
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
            return new Select( body, $tokens.of());
        }
        if( !this.constraint.test(body)){
            return null;
        }        
        if( !body.isImplemented() ){
            if( this.isImplemented ){
                return null;
            } 
            return new Select(body, $tokens.of());
        }       
        if( this.isImplemented ){
            $stmt.Select ss = this.bodyStmts.select((Statement)body.ast());
            if( ss != null ){
                return new Select( body, ss.tokens);
            }
            return null;
        }
        return new Select( body, $tokens.of());
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
    public _body draft(Translator translator, Map<String, Object> keyValues) {
        //they can OVERRIDE the body construction if they pass in a "$body" parameter
        if( keyValues.get("$body")!= null ){
            //this means I want to override the body
            //System.out.println( "$Body OVERRIDE");
            $body $bd = $body.of( keyValues.get("$body").toString() );
            Map<String,Object>tks = new HashMap<>();
            tks.putAll(keyValues);
            tks.remove("$body");
            return $bd.draft(translator, tks);
        }
        if( !this.isImplemented ){
            return _body.of(";");
        }
        Statement r = this.bodyStmts.draft( translator, keyValues );
        return _body.of( r );
    }
    
    /**
     * 
     * @param body
     * @param all
     * @return 
     */
    public Tokens parseTo(_body body, Tokens all) {
        if (all == null) { /* Skip decompose if the tokens already null*/
            return null;
        }    
        Select select = select( body );

        if (select != null) {                
            if (all.isConsistent(select.tokens.asTokens())) {
                all.putAll(select.tokens.asTokens());
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

    /**
     *
     * @param st
     * @param name
     * @return
     */
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
    public _body firstIn(Node astNode, Predicate<_body> _bodyMatchFn) {
        Optional<Node> on = astNode.findFirst(Node.class, n-> {
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
            _code _c = (_code)_j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit(), selectConstraint);
            }
            _type _t = (_type)_j; //only possible
            return selectFirstIn(_t.ast(), selectConstraint);
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
        Optional<Node> on = astNode.findFirst(Node.class, n-> {
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
    public Select selectFirstIn(Node astNode) {
        Optional<Node> on = astNode.findFirst(Node.class, n-> {
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
    public List<Select> listSelectedIn(Node astNode) {
        List<Select> found = new ArrayList<>();
        forSelectedIn(astNode, s-> found.add(s) );
        return found;
    }

    /**
     * 
     * @param clazz
     * @param selectActionFn
     * @return 
     */
    public  <_CT extends _type> _CT  forSelectedIn( Class clazz, Consumer<Select>selectActionFn){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectActionFn);
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public  <_CT extends _type> _CT  forSelectedIn( Class clazz, Predicate<Select>selectConstraint,  Consumer<Select>selectActionFn){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectActionFn);
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
     * @param <_J>
     * @param _j
     * @param selectActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
        forSelectedIn(_j, s->true, selectActionFn);
        return _j;
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectActionFn
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select>selectConstraint, Consumer<Select> selectActionFn) {
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
    public <N extends Node> N forEachIn(N astNode, Predicate<_body> _bodyMatchFn, Consumer<_body> _bodyActionFn) {
        astNode.walk(Node.class, n->{
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
        return astNode;
    }
    
    /**
     * 
     */
    public static class Select implements selected, select_java<_body> {

        public _body body;
        public $tokens tokens;
        
        public Select( _body body, $tokens tokens){
            this.body = body;
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens() {
            return tokens;
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
        public _body _node() {
            return this.body;
        }

        @Override
        public String toString(){
            return "$body.Select{"+ System.lineSeparator()+
                    Text.indent( body.toString() )+ System.lineSeparator()+
                    Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                    "}";
        }
    }
}
