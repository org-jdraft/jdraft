package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.ForStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

/**
 * assert true;    //check only
 * assert( true ); //check only
 * assert i==3 : "unexpected Expected this"; //check with optional message
 */
public final class _assertStmt implements _stmt<AssertStmt, _assertStmt>, _java._node<AssertStmt,_assertStmt> {

    public static final Function<String, _assertStmt> PARSER = s-> _assertStmt.of(s);

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
    public static _assertStmt of(_expr check){
        return of( new AssertStmt().setCheck(check.ast()));
    }

    public static _assertStmt of(Expression check, String message){
        return of( new AssertStmt().setCheck(check).setMessage(Expr.stringLiteralExpr(message)));
    }

    public static _assertStmt of(_expr check, String message){
        return of( new AssertStmt().setCheck(check.ast()).setMessage(Expr.stringLiteralExpr(message)));
    }

    public static _assertStmt of(Expression check, Expression message){
        return of( new AssertStmt().setCheck(check).setMessage(message));
    }

    public static <A extends Object> _assertStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _assertStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _assertStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assertStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assertStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _assertStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assertStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assertStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static _assertStmt from(StackTraceElement ste ){
        return from(_lambdaExpr.from(ste).astLambda);
    }

    private static _assertStmt from( LambdaExpr le){
        Optional<AssertStmt> ows = le.getBody().findFirst(AssertStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No binary expression found in lambda");
    }

    public static _feature._one<_assertStmt, _expr> CHECK = new _feature._one<>(_assertStmt.class, _expr.class,
            _feature._id.CHECK,
            a -> a.getCheck(),
            (_assertStmt a, _expr _e) -> a.setCheck(_e), PARSER);

    public static _feature._one<_assertStmt, _expr> MESSAGE = new _feature._one<>(_assertStmt.class, _expr.class,
            _feature._id.MESSAGE,
            a -> a.getMessage(),
            (_assertStmt a, _expr _e) -> a.setMessage(_e), PARSER)
            .setNullable(true);

    public static _feature._features<_assertStmt> FEATURES = _feature._features.of(_assertStmt.class, CHECK, MESSAGE);

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
            return isCheck(Expr.of(checkCode));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isCheck(Expression check){
        return Objects.equals( this.astStmt.getCheck(), check);
    }

    public boolean isCheck(_expr _e){
        return isCheck(_e.ast());
    }

    public boolean isCheck( Predicate<_expr> matchFn){
        return matchFn.test(getCheck());
    }


    public boolean hasMessage(){
        return this.astStmt.getMessage().isPresent();
    }

    public boolean isMessage(String message){
        try {
            return isMessage(Expr.of(message));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isMessage( Predicate<_expr> matchFn){
        if( hasMessage() ){
            return matchFn.test(getMessage());
        }
        return false;
    }

    public boolean isMessage(Expression message){
        if( this.hasMessage()){
            return Objects.equals(astStmt.getMessage().get(), message);
        }
        return message == null;
    }

    public boolean isMessage(_expr message){
        if( this.hasMessage()){
            return Objects.equals(astStmt.getMessage().get(), message.ast());
        }
        return message == null;
    }

    public AssertStmt ast(){
        return astStmt;
    }

    public _assertStmt setMessage(String message){
        this.astStmt.setMessage(Expr.stringLiteralExpr(message));
        return this;
    }

    public _assertStmt setMessage(_expr _e){
        this.astStmt.setMessage(_e.ast());
        return this;
    }

    public _assertStmt setMessage(Expression e){
        this.astStmt.setMessage(e);
        return this;
    }

    public _expr getMessage(){
        if( astStmt.getMessage().isPresent()) {
            return _expr.of(astStmt.getMessage().get());
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

    public _assertStmt setCheck(_expr _e){
        this.astStmt.setCheck(_e.ast());
        return this;
    }

    public _assertStmt setCheck(String... str){
        this.astStmt.setCheck( Expr.of(str));
        return this;
    }

    public _expr getCheck(){
        return _expr.of(astStmt.getCheck());
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.EXPRESSION, astStmt.getCheck() );
        if( astStmt.getMessage().isPresent()){
            comps.put(_java.Feature.MESSAGE_EXPR, astStmt.getMessage().get());
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
