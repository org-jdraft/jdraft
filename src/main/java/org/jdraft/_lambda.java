package org.jdraft;

import java.util.Map;
import java.util.Optional;
import java.util.function.*;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.Statement;

import org.jdraft._parameter._hasParameters;

/**
 * Representation of the source of a Java lambda expression
 * 
 * @author Eric
 */
public class _lambda 
    implements _expression<LambdaExpr, _lambda>, _mrJava, _hasParameters<_lambda> {

    /**
     * create a _lamba based on the code (as String)
     * @param code
     * @return
     */
    public static _lambda of( String code ){
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
    public static <T extends Object,U extends Object> _lambda of( Function<T, U> c ){
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
    public static <T extends Object,U extends Object, V extends Object> _lambda of( BiFunction<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    } 

    public boolean equals(Object o){
        if( o instanceof _lambda){
            return this.astLambda.equals( ((_lambda)o).astLambda );
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
    public static <T extends Object,U extends Object, V extends Object, W extends Object> _lambda of( Ex.TriFunction<T, U, V, W> c ){
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
    public static <T extends Object,U extends Object, V extends Object, W extends Object, X extends Object> _lambda of( Ex.QuadFunction<T, U, V, W, X> c ){
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
    public static <T extends Object> _lambda of( Consumer<T> c ){
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
    public static <T extends Object, U extends Object> _lambda of( BiConsumer<T, U> c ){
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
    public static <T extends Object, U extends Object, V extends Object> _lambda of( Ex.TriConsumer<T, U, V> c ){
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
    public static <A extends Object, B extends Object, C extends Object, D extends Object> _lambda of( Ex.QuadConsumer<A,B,C,D> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     *
     * @param anonymousObjectWithLambda
     * @return
     */
    public static _lambda of( Object anonymousObjectWithLambda ){
        ObjectCreationExpr oce = Ex.newEx(Thread.currentThread().getStackTrace()[2]);
        Optional<LambdaExpr> ole = oce.findFirst(LambdaExpr.class);
        if(ole.isPresent()){
            return _lambda.of( ole.get());
        }
        throw new _jdraftException("Could not find lambda within anonymous Object "+ oce );
    }
    
    public static _lambda of( Ex.Command lambda ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }
    
    public static _lambda from( StackTraceElement ste ){
        return _lambda.of( Ex.lambdaEx(ste));
    }

    public static _lambda of(){
        return new _lambda( new LambdaExpr());
    }

    public static _lambda of( String... lambda){
        return new _lambda( Ex.lambdaEx(lambda) );
    }
    
    public static _lambda of( LambdaExpr astLambda ){
        return new _lambda( astLambda );
    }
    public final LambdaExpr astLambda;
    
    public _lambda( LambdaExpr astLambda ){
        this.astLambda = astLambda;
    }
    
    @Override
    public _parameters getParameters() {
        return _parameters.of( astLambda );
    }

    @Override
    public _lambda copy() {
        return _lambda.of(this.astLambda.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is(Ex.lambdaEx(stringRep));
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

    public _lambda setBody(String...body){
        return setBody(Ast.stmt(body));
    }

    public _lambda addStatements( String... statements){
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
    public _lambda addStatements( int index, String... statements){
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
    public _lambda setBody( _body _b ){
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
    public _lambda setParameters(String...parameters){
        setParameters( Ast.parameters(parameters) );
        if( this.getParameters().size() > 1){
            this.setEnclosingParameters(true);
        }
        return this;
    }

    /**
     * 
     * @param body
     * @return 
     */
    public _lambda setBody( Statement body ){
        this.astLambda.setBody(body);
        return this;
    }
    
    public boolean isEnclosingParameters(){
        return astLambda.isEnclosingParameters();
    }
    
    public _lambda setEnclosingParameters(boolean toSet){
        this.astLambda.setEnclosingParameters(toSet);
        return this;
    }

    public _lambda setBody(BlockStmt body) {
        this.astLambda.setBody(body);
        return this;
    }

    public _lambda clearBody() {
        this.setBody( _body.empty() );
        return this;
    }

    public String toString(){
        return this.astLambda.toString();
    }

    @Override
    public Map<_java.Component, Object> components() {
        return null;
    }
}
