package org.jdraft;

import java.util.*;
import java.util.function.*;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.Statement;

import org.jdraft._params._withParams;

/**
 * Representation of the source of a Java lambda expression
 * 
 * @author Eric
 */
public final class _lambdaExpr
    implements _expr<LambdaExpr, _lambdaExpr>,
        _java._node<LambdaExpr, _lambdaExpr>,
        _withParams<_lambdaExpr> {

    public static final Function<String, _lambdaExpr> PARSER = s-> _lambdaExpr.of(s);

    /**
     * create a _lambda based on the code (as String)
     * @param code
     * @return
     */
    public static _lambdaExpr of(String code ){
        return of( new String[]{code});
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object,U extends Object> _lambdaExpr of(Function<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param <V>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object,U extends Object, V extends Object> _lambdaExpr of(BiFunction<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    } 

    public boolean equals(Object o){
        if( o instanceof _lambdaExpr){
            return this.astLambda.equals( ((_lambdaExpr)o).astLambda );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.astLambda.hashCode();
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <W>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object,U extends Object, V extends Object, W extends Object> _lambdaExpr of(Expr.TriFunction<T, U, V, W> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <W>
     * @param <X>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object,U extends Object, V extends Object, W extends Object, X extends Object> _lambdaExpr of(Expr.QuadFunction<T, U, V, W, X> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param c a lambda
     * @return the LambdaExpr instance
     */ 
    public static <T extends Object> _lambdaExpr of(Consumer<T> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object, U extends Object> _lambdaExpr of(BiConsumer<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * Expr.lamdba( ()-> assert(true) );  will return the same as
     * Expr.lambda("()->assert(true)");
     * </PRE>
     * @param <T>
     * @param <U>
     * @param <V>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <T extends Object, U extends Object, V extends Object> _lambdaExpr of(Expr.TriConsumer<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Builds a lambda expression from the *CODE* passed in...i,.e.<PRE>
     * _lamdba.of( ()-> assert(true) );  will return the same as
     * _lambda.of("()->assert(true)");
     * </PRE>
     * @param <A>
     * @param <B>
     * @param <C>
     * @param <D>
     * @param c a lambda
     * @return the LambdaExpr instance
     */
    public static <A extends Object, B extends Object, C extends Object, D extends Object> _lambdaExpr of(Expr.QuadConsumer<A,B,C,D> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     *
     * @param anonymousObjectWithLambda
     * @return
     */
    public static _lambdaExpr of(Object anonymousObjectWithLambda ){
        ObjectCreationExpr oce = Expr.newExpr(Thread.currentThread().getStackTrace()[2]);
        Optional<LambdaExpr> ole = oce.findFirst(LambdaExpr.class);
        if(ole.isPresent()){
            return _lambdaExpr.of( ole.get());
        }
        throw new _jdraftException("Could not find lambda within anonymous Object "+ oce );
    }
    
    public static _lambdaExpr of(Expr.Command lambda ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }
    
    public static _lambdaExpr from(StackTraceElement ste ){
        return _lambdaExpr.of( Expr.lambdaExpr(ste));
    }

    public static _lambdaExpr of(){
        return new _lambdaExpr( new LambdaExpr());
    }

    public static _lambdaExpr of(String... lambda){
        return new _lambdaExpr( Expr.lambdaExpr(lambda) );
    }
    
    public static _lambdaExpr of(LambdaExpr astLambda ){
        return new _lambdaExpr( astLambda );
    }

    /**
     * are the lambda parameter(s) parenthesized : i.e.
     * a-> "100"; //param "a" NOT parenthesized
     * (a)-> "100"; //param "a' IS parenthesized
     */
    public static _feature._one<_lambdaExpr, Boolean> IS_PARAM_PARENTHESIZED = new _feature._one<>(_lambdaExpr.class, Boolean.class,
            _feature._id.IS_PARAM_PARENTHESIZED,
            a -> a.isParenthesizedParams(),
            (_lambdaExpr a, Boolean b) -> a.setParenthesizedParams(b), PARSER);

    public static _feature._one<_lambdaExpr, _params> PARAMS = new _feature._one<>(_lambdaExpr.class, _params.class,
            _feature._id.PARAMS,
            a -> a.getParams(),
            (_lambdaExpr a, _params _ps) -> a.setParams(_ps), PARSER);

    public static _feature._one<_lambdaExpr, _stmt> BODY = new _feature._one<>(_lambdaExpr.class, _stmt.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_lambdaExpr a, _stmt _s) -> a.setBody(_s), PARSER);

    public static _feature._features<_lambdaExpr> FEATURES = _feature._features.of(_lambdaExpr.class, IS_PARAM_PARENTHESIZED, PARAMS, BODY );

    public final LambdaExpr astLambda;
    
    public _lambdaExpr(LambdaExpr astLambda ){
        this.astLambda = astLambda;
    }
    
    @Override
    public _params getParams() {
        return _params.of( astLambda );
    }

    public _lambdaExpr setParams(_params _ps){
        this.astLambda.getParameters().clear();
        _ps.forEach(_p -> this.astLambda.getParameters().add(_p.ast()));
        return this;
    }

    @Override
    public _lambdaExpr copy() {
        return _lambdaExpr.of(this.astLambda.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is(Expr.lambdaExpr(stringRep));
        } catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean is(LambdaExpr astNode) {
        return this.astLambda.equals(astNode);
    }

    @Override
    public LambdaExpr ast() {
        return astLambda;
    }

    /**
     * 
     * @return 
     */
    public Statement getAstStatementBody(){
        return astLambda.getBody();                
    }

    public _stmt getBody(){
        return _stmt.of(getAstStatementBody());
    }

    public _lambdaExpr setBody(_stmt _s ){
        return setBody(_s.ast());
    }

    public _lambdaExpr setBody(_expr _e){
        this.astLambda.setBody( _exprStmt.of(_e).ast());
        return this;
    }

    public _lambdaExpr setBody(String...body){
        return setBody(Ast.statement(body));
    }

    /**
     *
     * @param _b
     * @return
     */
    public _lambdaExpr setBody(_body _b ){
        if( _b.isImplemented() ){
            this.astLambda.setBody(_b.ast());
        }
        else{
            this.astLambda.setBody(new EmptyStmt());
        }
        return this;
    }

    /**
     *
     * @param body
     * @return
     */
    public _lambdaExpr setBody(Statement body ){
        this.astLambda.setBody(body);
        return this;
    }

    public _lambdaExpr setBody(BlockStmt body) {
        this.astLambda.setBody(body);
        return this;
    }

    public _lambdaExpr addStatements(String... statements){
        if( this.astLambda.getBody().isBlockStmt() ){
            return addStatements(this.astLambda.getBody().asBlockStmt().getStatements().size(), statements);
        }
        Statement lsb = this.astLambda.getBody();
        //if   return; //this is the "default" body
        if( lsb.isReturnStmt() && !lsb.asReturnStmt().getExpression().isPresent()){
            return addStatements(0, statements);
        }
        return addStatements(1, statements);
    }

    /**
     * Add statements to the existing lambda expression at the given index (0-based)
     * @param index
     * @param statements
     * @return
     */
    public _lambdaExpr addStatements(int index, String... statements){
        BlockStmt bs = Ast.blockStmt(statements);
        if( this.astLambda.getBody().isBlockStmt() ){
            BlockStmt lbs = this.astLambda.getBody().asBlockStmt();
            for( int i=0;i<bs.getStatements().size();i++) {
                lbs.addStatement(index+i, bs.getStatement(i));
            }
            return this;
        }
        Statement lsb = this.astLambda.getBody();
        //if   return; //this is the "default" body
        if( lsb.isReturnStmt() && !lsb.asReturnStmt().getExpression().isPresent()){
            this.astLambda.setBody(bs);
            return this;
        }
        if( index == 0 ){
            bs.addStatement(bs.getStatements().size(), lsb);
            this.astLambda.setBody(bs);
            return this;
        }
        bs.addStatement(0, lsb);
        this.astLambda.setBody(bs);
        return this;
    }



    public _lambdaExpr clearBody() {
        this.setBody( _body.empty() );
        return this;
    }

    public boolean isParenthesizedParams(){
        return astLambda.isEnclosingParameters();
    }

    public _lambdaExpr setParenthesizedParams(boolean toSet){
        this.astLambda.setEnclosingParameters(toSet);
        return this;
    }

    /**
     * Sets the parameters by taking in a String
     * @param parameters the String representation of the parameters
     * @return the _hasParameters entity
     */
    public _lambdaExpr setParams(String...parameters){
        setParams( Ast.parameters(parameters) );
        if( this.getParams().size() > 1){
            this.setParenthesizedParams(true);
        }
        return this;
    }

    public String toString(){
        return this.astLambda.toString();
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> map = new HashMap<>();
        map.put(_java.Feature.BODY, _body.of(this.astLambda));
        map.put(_java.Feature.IS_ENCLOSED_PARAMS, this.astLambda.isEnclosingParameters());
        map.put(_java.Feature.PARAMS, _params.of(this.astLambda.getParameters()));
        return null;
    }
}
