package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public final class _doStmt implements _stmt._controlFlow._loop<DoStmt, _doStmt>,
        _stmt._controlFlow._conditional<DoStmt, _doStmt>,
        _java._node<DoStmt, _doStmt>,
        _java._withCondition<DoStmt,_doStmt>,
        _body._withBody<_doStmt> {

    public static final Function<String, _doStmt> PARSER = s-> _doStmt.of(s);

    public static _doStmt of(){
        return new _doStmt( new DoStmt( ));
    }
    public static _doStmt of(DoStmt ds){
        return new _doStmt(ds);
    }
    public static _doStmt of(String...code){
        return new _doStmt(Stmt.doStmt( code));
    }

    public static <A extends Object> _doStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _doStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _doStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _doStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _doStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _doStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _doStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _doStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _doStmt from( LambdaExpr le){
        Optional<DoStmt> ows = le.getBody().findFirst(DoStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No do statement found in lambda");
    }

    public static _feature._one<_doStmt, _expr> CONDITION = new _feature._one<>(_doStmt.class, _expr.class,
            _feature._id.CONDITION,
            a -> a.getCondition(),
            (_doStmt a, _expr _e) -> a.setCondition(_e), PARSER);

    /** could be a single statement of a block statement or null */
    public static _feature._one<_doStmt, _body> BODY = new _feature._one<>(_doStmt.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_doStmt a, _body b) -> a.setBody(b), PARSER);

    public static _feature._features<_doStmt> FEATURES = _feature._features.of(_doStmt.class, CONDITION, BODY );

    private DoStmt astStmt;

    public _doStmt(DoStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _doStmt copy() {
        return new _doStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.doStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _body getBody(){
        return _body.of( this.astStmt.getBody() );
    }

    @Override
    public _doStmt setBody(BlockStmt body) {
        this.astStmt.setBody(body);
        return this;
    }

    public _doStmt setBody(_stmt _st){
        this.astStmt.setBody(_st.ast());
        return this;
    }

    public _doStmt setBody(_body _bd){
        this.astStmt.setBody(_bd.ast());
        return this;
    }

    public _doStmt clearBody(){
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _doStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.astStmt.getBody();
        if( bd instanceof BlockStmt ){
            for(int i=0;i<statements.length; i++) {
                bd.asBlockStmt().addStatement(i+startStatementIndex, statements[i]);
            }
            return this;
        }
        BlockStmt bs = new BlockStmt();
        bs.addStatement(bd);
        for(int i=0;i<statements.length; i++) {
            bd.asBlockStmt().addStatement(1+startStatementIndex, statements[i]);
        }
        return this;
    }

    public DoStmt ast(){
        return astStmt;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.CONDITION_EXPR, astStmt.getCondition());
        comps.put(_java.Feature.BODY, astStmt.getBody());
        return comps;
    }
     */

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _doStmt ){
            return Objects.equals( ((_doStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
