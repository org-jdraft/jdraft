package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.AssertStmt;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

/**
 * assert true;    //check only
 * assert( true ); //check only
 * assert i==3 : "unexpected Expected this"; //check with optional message
 */
public final class _assertStmt implements _stmt<AssertStmt, _assertStmt>, _tree._node<AssertStmt,_assertStmt> {

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
        return of( new AssertStmt().setCheck(check.node()));
    }

    public static _assertStmt of(Expression check, String message){
        return of( new AssertStmt().setCheck(check).setMessage(Expr.stringLiteralExpr(message)));
    }

    public static _assertStmt of(_expr check, String message){
        return of( new AssertStmt().setCheck(check.node()).setMessage(Expr.stringLiteralExpr(message)));
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
        return from(_lambdaExpr.from(ste).node);
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

    public static _feature._features<_assertStmt> FEATURES = _feature._features.of(_assertStmt.class,  PARSER, CHECK, MESSAGE);

    private AssertStmt node;

    public _assertStmt(AssertStmt node){
        this.node = node;
    }

    public _feature._features<_assertStmt> features(){
        return FEATURES;
    }

    @Override
    public _assertStmt copy() {
        return new _assertStmt( this.node.clone());
    }

    public _assertStmt replace(AssertStmt ae){
        this.node.replace(ae);
        this.node = ae;
        return this;
    }

    public boolean isCheck(String...checkCode){
        try{
            return isCheck(Expr.of(checkCode));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isCheck(Expression check){
        return Objects.equals( this.node.getCheck(), check);
    }

    public boolean isCheck(_expr _e){
        return isCheck(_e.node());
    }

    public boolean isCheck( Predicate<_expr> matchFn){
        return matchFn.test(getCheck());
    }


    public boolean hasMessage(){
        return this.node.getMessage().isPresent();
    }

    public boolean isMessage(String message){
        try {
            return isMessage(Expr.of(message)) || isMessage(new StringLiteralExpr(message));
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
            return Objects.equals(node.getMessage().get(), message);
        }
        return message == null;
    }

    public boolean isMessage(_expr message){
        if( this.hasMessage()){
            return Objects.equals(node.getMessage().get(), message.node());
        }
        return message == null;
    }

    public AssertStmt node(){
        return node;
    }

    public _assertStmt setMessage(String message){
        this.node.setMessage(Expr.stringLiteralExpr(message));
        return this;
    }

    public _assertStmt setMessage(_expr _e){
        this.node.setMessage(_e.node());
        return this;
    }

    public _assertStmt setMessage(Expression e){
        this.node.setMessage(e);
        return this;
    }

    public _expr getMessage(){
        if( node.getMessage().isPresent()) {
            return _expr.of(node.getMessage().get());
        }
        return null;
    }

    public _assertStmt removeMessage(){
        this.node.removeMessage();
        return this;
    }

    public _assertStmt setCheck(Expression e){
        this.node.setCheck(e);
        return this;
    }

    public _assertStmt setCheck(_expr _e){
        this.node.setCheck(_e.node());
        return this;
    }

    public _assertStmt setCheck(String... str){
        this.node.setCheck( Expr.of(str));
        return this;
    }

    public _expr getCheck(){
        return _expr.of(node.getCheck());
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _assertStmt ){
            _assertStmt _o = (_assertStmt)other;
            if( !Objects.equals( getCheck(), _o.getCheck() )){
                return false;
            }
            if( !Objects.equals( getMessage(), _o.getMessage())){
                return false;
            }
            return true;
            //return Objects.equals( ((_assertStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
