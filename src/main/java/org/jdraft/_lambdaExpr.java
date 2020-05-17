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
        _java._multiPart<LambdaExpr, _lambdaExpr>,
        _withParams<_lambdaExpr> {

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
    public static <T extends Object,U extends Object, V extends Object, W extends Object> _lambdaExpr of(Exprs.TriFunction<T, U, V, W> c ){
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
    public static <T extends Object,U extends Object, V extends Object, W extends Object, X extends Object> _lambdaExpr of(Exprs.QuadFunction<T, U, V, W, X> c ){
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
    public static <T extends Object, U extends Object, V extends Object> _lambdaExpr of(Exprs.TriConsumer<T, U, V> c ){
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
    public static <A extends Object, B extends Object, C extends Object, D extends Object> _lambdaExpr of(Exprs.QuadConsumer<A,B,C,D> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     *
     * @param anonymousObjectWithLambda
     * @return
     */
    public static _lambdaExpr of(Object anonymousObjectWithLambda ){
        ObjectCreationExpr oce = Exprs.newExpr(Thread.currentThread().getStackTrace()[2]);
        Optional<LambdaExpr> ole = oce.findFirst(LambdaExpr.class);
        if(ole.isPresent()){
            return _lambdaExpr.of( ole.get());
        }
        throw new _jdraftException("Could not find lambda within anonymous Object "+ oce );
    }
    
    public static _lambdaExpr of(Exprs.Command lambda ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }
    
    public static _lambdaExpr from(StackTraceElement ste ){
        return _lambdaExpr.of( Exprs.lambdaExpr(ste));
    }

    public static _lambdaExpr of(){
        return new _lambdaExpr( new LambdaExpr());
    }

    public static _lambdaExpr of(String... lambda){
        return new _lambdaExpr( Exprs.lambdaExpr(lambda) );
    }
    
    public static _lambdaExpr of(LambdaExpr astLambda ){
        return new _lambdaExpr( astLambda );
    }
    public final LambdaExpr astLambda;
    
    public _lambdaExpr(LambdaExpr astLambda ){
        this.astLambda = astLambda;
    }
    
    @Override
    public _params getParams() {
        return _params.of( astLambda );
    }

    @Override
    public _lambdaExpr copy() {
        return _lambdaExpr.of(this.astLambda.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is(Exprs.lambdaExpr(stringRep));
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
    public Statement getBody(){        
        return astLambda.getBody();                
    }

    public _lambdaExpr setBody(String...body){
        return setBody(Ast.statement(body));
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
     * Sets the parameters by taking in a String
     * @param parameters the String representation of the parameters
     * @return the _hasParameters entity
     */
    public _lambdaExpr setParams(String...parameters){
        setParams( Ast.parameters(parameters) );
        if( this.getParams().size() > 1){
            this.setEnclosingParameters(true);
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
    
    public boolean isEnclosingParameters(){
        return astLambda.isEnclosingParameters();
    }
    
    public _lambdaExpr setEnclosingParameters(boolean toSet){
        this.astLambda.setEnclosingParameters(toSet);
        return this;
    }

    public _lambdaExpr setBody(BlockStmt body) {
        this.astLambda.setBody(body);
        return this;
    }

    public _lambdaExpr clearBody() {
        this.setBody( _body.empty() );
        return this;
    }

    public String toString(){
        return this.astLambda.toString();
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> map = new HashMap<>();
        map.put(_java.Component.BODY, _body.of(this.astLambda));
        map.put(_java.Component.ENCLOSED_PARAMETERS, this.astLambda.isEnclosingParameters());
        map.put(_java.Component.PARAMETERS, _params.of(this.astLambda.getParameters()));
        return null;
    }
}
