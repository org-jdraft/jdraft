package org.jdraft.pattern;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.utils.Log;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import org.jdraft.*;
import org.jdraft.text.Template;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;
import org.jdraft.text.Translator;

import java.util.*;
import java.util.function.*;

/**
 * Prototype for composing and querying a {@link _body}
 *
 * @author Eric
 */
public class $body implements Template<_body>, $pattern<_body, $body>, $pattern.$java<_body, $body>, $constructor.$part, $method.$part{

    /** a part of the body... a $ex, $stmt, $stmts, $case, $comment, $var */
    public interface $part { }

    public Class<_body> _modelType(){
        return _body.class;
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


    /**
     *
     * @param parts
     * @return
     */
    public static $body of( $part...parts ){
        $body $b = of();
        for(int i=0;i<parts.length;i++){
            $b.$and( parts[i] );
        }
        return $b;
    }

    public static $body of( String...body ){
        return new $body( _body.of(body));
    }

    public static $body of( BlockStmt bs ){
        return of( _body.of(bs));
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

    public static $body of(Ex.Command commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body of(Consumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body of(BiConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body of(Ex.TriConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body of(Ex.QuadConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static <A extends Object, B extends Object> $body of(Function<A,B> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static <A extends Object, B extends Object, C extends Object>  $body of(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $body of(Ex.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> $body of(Ex.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.of( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body.Or or( _body... _protos ){
        $body[] arr = new $body[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $body.of( _protos[i]);
        }
        return or(arr);
    }

    public static $body.Or or( $body...$tps ){
        return new $body.Or($tps);
    }

    public static $body as(){
        return notImplemented();
    }

    public static $body as( String... body ){
        String s = Text.combine(body);
        if( s.trim().length() == 0 || s.equals(";")){
            return as();
        }
        return as( _body.of(s));
    }

    public static $body as(BlockStmt bs ){
        return as(_body.of(bs));
    }

    public static $body as(_body b){
        $body $b = of( b );
        if( b.isImplemented()) {

            //System.out.println( $s );
            if( b.isEmpty() ){
                $b.$and( _b -> _b.isEmpty() );
            } else {
                final $stmt $s = $stmt.of(b.ast());
                //make sure the number of statements is the same AND the top BlockStmt it matches (exactly)
                $b.$and(_b -> _b.getStatements().size() == b.getStatements().size() && $s.matches(_b.ast()));
            }
        }
        return $b;
    }

    public static $body as(Ex.Command commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body as(Consumer<? extends Object>  commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body as(BiConsumer<? extends Object, ? extends Object> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body as(Ex.TriConsumer<? extends Object, ? extends Object, ? extends Object>  commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body as(Ex.QuadConsumer<? extends Object, ? extends Object,? extends Object, ? extends Object>  commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static <A extends Object, B extends Object> $body as(Function<A,B> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static <A extends Object, B extends Object, C extends Object>  $body as(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $body as(Ex.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> $body as(Ex.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $body.as( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }

    public static $body notImplemented(){
        return new $body().$and(b-> !b.isImplemented());
    }

    public static $body not($part...parts){
        return of().$not(parts);
    }
    
    public static $body empty(){
        return new $body().$and(b -> b.isEmpty() );
    }
    
    public Predicate<_body> constraint = t->true;
    
    public Boolean isImplemented = true;    
    
    public $stmt<BlockStmt> bodyStmts = null;
    
    /**
     * This represents a "non implemented body"
     * which is 
     */
    private $body( ){
        this.isImplemented = null;
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
    public $body $and(Predicate<_body> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }

    /**
     * A Body containing some parts (i.e. a $stmt, $stmts, $ex, $case, $catch)
     * @param parts
     * @return
     */
    public $body $and( $part...parts){
        for(int i=0;i<parts.length;i++){
            final $part $p = parts[i];
            $and( b-> (($pattern)$p).firstIn(b) != null);
        }
        return this;
    }

    /**
     * Match only bodies that DO NOT contain these parts
     * @param parts
     * @return
     */
    public $body $not( $part...parts ){
        for(int i=0;i<parts.length;i++){
            final $part $p = parts[i];
            Predicate<_body> pb = b-> (($pattern)$p).firstIn(b) != null;
            $and( pb.negate() );
        }
        return this;
    }

    public String toString(){
        if( this.isMatchAny() ){
            return "$body{ $ANY$ }";
        }
        if( this.isImplemented != null && !this.isImplemented ){
            return "$body{ ; } (not implemented)";
        }
        //if( !this.bodyStmts.stmtStencil.isMatchAny() ){
        //    return "$body" + this.bodyStmts.stmtStencil +System.lineSeparator();
        //}
        return "$body" + this.bodyStmts.stmtStencil + System.lineSeparator();
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

    public boolean match( _java _j ){
        if( _j instanceof _body._hasBody ){
            return matches( (_body._hasBody)_j );
        }
        return false;
    }

    public boolean matches(_body._hasBody _hb ){ return select( _hb) != null; }

    public boolean matches(BlockStmt bs){
        return matches(_body.of(bs));
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


    public Select select(Ex.Command commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public Select select(Consumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public Select select(BiConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public Select select(Ex.TriConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public Select select(Ex.QuadConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public <A extends Object, B extends Object> Select select(Function<A,B> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public <A extends Object, B extends Object, C extends Object> Select select(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public <A extends Object, B extends Object, C extends Object, D extends Object>Select select(Ex.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
    
    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> Select select(Ex.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return select( le.getBody().toString(Ast.PRINT_NO_COMMENTS ) );
    }
     /**
     * 
     * @return 
     */
    public boolean isMatchAny(){

        try{
            if(this.constraint.test(null)){
                if( this.bodyStmts == null ){
                    return true;
                }
                return this.bodyStmts.stmtStencil.isMatchAny();
            }
            return false;
        }catch(Exception e){
            //System.out.println("BODY NOT MATCH ANY" );
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

    public Tokens parse(_body body ){

        if( this.isImplemented == null && this.bodyStmts ==null ){
            return new Tokens();
        }
        if( isMatchAny() && body == null ){
            return new Tokens();
        }
        if( !this.constraint.test(body)){
            return null;
        }

        if( !body.isImplemented() ){
            if( this.isImplemented == true){
                return null;
            }
            return new Tokens();
        }
        if( this.isImplemented ){
            $stmt.Select ss = this.bodyStmts.select((Statement)body.ast());
            if( ss != null ){
                return ss.tokens().asTokens();
                //return new Select( body, ss.tokens);
            }
            return null;
        }
        return new Tokens();
    }

    /**
     *
     * @param body
     * @return
     */
    public Tokens parseOf( _body body ){

        if( this.isImplemented == null && this.bodyStmts ==null ){
            return new Tokens();
        }
        if( isMatchAny() && body == null ){
            return new Tokens();
        }
        if( !this.constraint.test(body)){
            Log.info("failed $body constaint");
            return null;
        }

        if( !body.isImplemented() ){
            if( this.isImplemented == true){
                Log.info("failed Expected Body implemented");
                return null;
            }
            return new Tokens();
        }
        if( this.isImplemented ){
            $stmt.Select ss = this.bodyStmts.select((Statement)body.ast());
            if( ss != null ){
                return ss.tokens().asTokens();
                //return new Select( body, ss.tokens);
            } else{
                try {
                    BlockStmt bs = Ast.blockStmt( this.bodyStmts.stmtStencil.toString() );
                    Tokens ts = new Tokens();
                    NodeList<Statement> stmts = bs.getStatements();
                    for(int i=0; i<stmts.size(); i++){
                        $stmt $s = $stmt.of( stmts.get(i) );
                        $stmt.Select sts = $s.selectFirstIn(body);
                        if( sts == null ){
                            return null;
                        }
                        ts.putAll( sts.tokens );
                    }
                    return ts;
                } catch(Exception e){
                    throw e;
                    //Log.info("Exception in parsing code %s", ()->e.getMessage());
                    //return null;
                }
            }
            //return null;
        }
        return new Tokens();
    }

    public Select select( _body._hasBody _hb ){
        return select(_hb.getBody());
    }

    /**
     * 
     * @param body
     * @return 
     */
    public Select select( _body body ){

        if( !this.constraint.test(body) ){
            return null;
        }
        //Log.info("passed body constraint");
        //Old
        //Tokens ts = parse(body);
        //NEW
        Tokens ts = parseOf(body);
        if( ts == null ){
            return null;
        }
        return new Select(body, $tokens.of( ts ));
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
        if( this.isImplemented == null || !this.isImplemented ){
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
    public $body $(String target, String $paramName) {
        if( this.isImplemented ){            
            this.bodyStmts = this.bodyStmts.$(target, $paramName);
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
    public Select selectFirstIn(_draft _j, Predicate<Select> selectConstraint) {
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
    public <_J extends _draft> _J forSelectedIn(_J _j, Consumer<Select> selectActionFn) {
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
    public <_J extends _draft> _J forSelectedIn(_J _j, Predicate<Select>selectConstraint, Consumer<Select> selectActionFn) {
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
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $body{

        final List<$body>ors = new ArrayList<>();

        public Or($body...$as){
            super();
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $body hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$body.Or{");
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
        public $body.Select select(_body astNode){
            $body $a = whichMatch(astNode);
            if( $a != null ){
                return $a.select(astNode);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
         * @param ae
         * @return
         */
        public $body whichMatch(_body ae){
            if( !this.constraint.test(ae ) ){
                return null;
            }
            Optional<$body> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
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
