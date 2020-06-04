package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;

import java.util.*;
import java.util.function.*;

public final class _whileStmt implements
        _stmt._conditional<WhileStmt, _whileStmt>,
        _stmt._loop<WhileStmt, _whileStmt>,
        _java._withCondition<WhileStmt, _whileStmt>,
        _body._withBody<_whileStmt> {

    public static final Function<String, _whileStmt> PARSER = s-> _whileStmt.of(s);

    public static _whileStmt of(){
        return new _whileStmt( new WhileStmt( ));
    }
    public static _whileStmt of(WhileStmt ws){
        return new _whileStmt(ws);
    }
    public static _whileStmt of(String...code){
        return new _whileStmt(Stmt.whileStmt( code));
    }

    public static <A extends Object> _whileStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _whileStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _whileStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _whileStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _whileStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _whileStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _whileStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _whileStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _whileStmt from( LambdaExpr le){
        Optional<WhileStmt> ows = le.getBody().findFirst(WhileStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No while statement found in lambda");
    }

    public static _feature._one<_whileStmt, _expr> CONDITION = new _feature._one<>(_whileStmt.class, _expr.class,
            _feature._id.CONDITION,
            a -> a.getCondition(),
            (_whileStmt a, _expr _e) -> a.setCondition(_e), PARSER);

    public static _feature._one<_whileStmt, _body> BODY = new _feature._one<>(_whileStmt.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_whileStmt a, _body _b) -> a.setBody(_b), PARSER);

    public static _feature._features<_whileStmt> FEATURES = _feature._features.of(_whileStmt.class, CONDITION, BODY);

    private WhileStmt whileStmt;

    public _whileStmt(WhileStmt rs){
        this.whileStmt = rs;
    }

    @Override
    public _whileStmt copy() {
        return new _whileStmt( this.whileStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.whileStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _body getBody(){
        return _body.of( this.whileStmt.getBody() );
    }

    @Override
    public _whileStmt setBody(BlockStmt body) {
        this.whileStmt.setBody(body);
        return this;
    }

    public boolean isBody(_body _b){
        return Objects.equals( _b, this.getBody());
    }

    public _whileStmt setBody(_stmt _st){
        this.whileStmt.setBody(_st.ast());
        return this;
    }

    public _whileStmt clearBody(){
        this.whileStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _whileStmt add(int startStatementIndex, Statement... statements) {
        Statement st = this.whileStmt.getBody();
        if( st instanceof BlockStmt ){
            for(int i=0;i<statements.length; i++) {
                st.asBlockStmt().addStatement(i + startStatementIndex, statements[i]);
            }
            return this;
        }
        BlockStmt bs = new BlockStmt();
        bs.addStatement(st); //add the old statement
        Arrays.stream(statements).forEach(s -> bs.addStatement(startStatementIndex, s) );
        this.whileStmt.setBody(bs);
        return this;
    }

    public WhileStmt ast(){
        return whileStmt;
    }

    public boolean equals(Object other){
        if( other instanceof _whileStmt ){
            return Objects.equals( ((_whileStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }

    public String toString(){
        return this.whileStmt.toString();
    }
}
