package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.AssertStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * assert true;    //check only
 * assert( true ); //check only
 * assert i==3 : "unexpected Expected this"; //check with optional message
 */
public class _assertStmt implements _statement<AssertStmt, _assertStmt>, _java._multiPart<AssertStmt,_assertStmt> {

    public static _assertStmt of(){
        return new _assertStmt( new AssertStmt( ));
    }
    public static _assertStmt of(AssertStmt as){
        return new _assertStmt(as);
    }
    public static _assertStmt of(String...code){
        return new _assertStmt(Stmt.assertStmt( code));
    }

    public static _assertStmt of(Expression check){
        return of( new AssertStmt().setCheck(check));
    }
    public static _assertStmt of(_expression check){
        return of( new AssertStmt().setCheck(check.ast()));
    }

    public static _assertStmt of(Expression check, String message){
        return of( new AssertStmt().setCheck(check).setMessage(Ex.stringLiteralEx(message)));
    }

    public static _assertStmt of(_expression check, String message){
        return of( new AssertStmt().setCheck(check.ast()).setMessage(Ex.stringLiteralEx(message)));
    }

    public static _assertStmt of(Expression check, Expression message){
        return of( new AssertStmt().setCheck(check).setMessage(message));
    }

    public static <A extends Object> _assertStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _assertStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _assertStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assertStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assertStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _assertStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assertStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assertStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _assertStmt from( LambdaExpr le){
        Optional<AssertStmt> ows = le.getBody().findFirst(AssertStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No binary expression found in lambda");
    }

    private AssertStmt astStmt;

    public _assertStmt(AssertStmt astStmt){
        this.astStmt = astStmt;
    }

    @Override
    public _assertStmt copy() {
        return new _assertStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.assertStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public boolean isCheck(String...checkCode){
        try{
            return isCheck(Ex.of(checkCode));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isCheck(Expression check){
        return Objects.equals( this.astStmt.getCheck(), check);
    }

    public boolean isCheck(_expression _e){
        return isCheck(_e.ast());
    }

    public boolean hasMessage(){
        return this.astStmt.getMessage().isPresent();
    }

    public boolean isMessage(String message){
        try {
            return isMessage(Ex.of(message));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isMessage(Expression message){
        if( this.hasMessage()){
            return Objects.equals(astStmt.getMessage().get(), message);
        }
        return message == null;
    }

    public boolean isMessage(_expression message){
        if( this.hasMessage()){
            return Objects.equals(astStmt.getMessage().get(), message.ast());
        }
        return message == null;
    }

    @Override
    public boolean is(AssertStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public AssertStmt ast(){
        return astStmt;
    }

    public _assertStmt setMessage(String message){
        this.astStmt.setMessage(Ex.stringLiteralEx(message));
        return this;
    }

    public _assertStmt setMessage(Expression e){
        this.astStmt.setMessage(e);
        return this;
    }

    public _expression getMessage(){
        if( astStmt.getMessage().isPresent()) {
            return _expression.of(astStmt.getMessage().get());
        }
        return null;
    }

    public _assertStmt removeMessage(){
        this.astStmt.removeMessage();
        return this;
    }

    public _assertStmt setCheck(Expression e){
        this.astStmt.setCheck(e);
        return this;
    }

    public _assertStmt setCheck(_expression _e){
        this.astStmt.setCheck(_e.ast());
        return this;
    }

    public _assertStmt setCheck(String... str){
        this.astStmt.setCheck( Ex.of(str));
        return this;
    }

    public _expression getCheck(){
        return _expression.of(astStmt.getCheck());
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.EXPRESSION, astStmt.getCheck() );
        if( astStmt.getMessage().isPresent()){
            comps.put(_java.Component.MESSAGE, astStmt.getMessage().get());
        }
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _assertStmt ){
            return Objects.equals( ((_assertStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
