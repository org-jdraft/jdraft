package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


public class _throwStmt implements _statement<ThrowStmt, _throwStmt> {

    public static _throwStmt of(){
        return new _throwStmt( new ThrowStmt( ));
    }
    public static _throwStmt of(ThrowStmt ts){
        return new _throwStmt(ts);
    }
    public static _throwStmt of(String...code){
        return new _throwStmt(Stmt.throwStmt( code));
    }

    public static <A extends Object> _throwStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _throwStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _throwStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _throwStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _throwStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _throwStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _throwStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _throwStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _throwStmt from( LambdaExpr le){
        Optional<ThrowStmt> ows = le.getBody().findFirst(ThrowStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No throw statement found in lambda");
    }

    private ThrowStmt astStmt;

    public _throwStmt(ThrowStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _throwStmt copy() {
        return new _throwStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.throwStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getExpression(){
        return _expression.of(astStmt.getExpression());
    }

    public _throwStmt setExpression(Expression e){
        this.astStmt.setExpression(e);
        return this;
    }

    public _throwStmt setExpression(_expression _e){
        this.astStmt.setExpression(_e.ast());
        return this;
    }

    @Override
    public boolean is(ThrowStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ThrowStmt ast(){
        return astStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.EXPRESSION, astStmt.getExpression());
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _throwStmt ){
            return Objects.equals( ((_throwStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
