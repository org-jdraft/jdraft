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

public final class _doStmt implements _stmt._controlFlow._loop<DoStmt, _doStmt>,
        _stmt._controlFlow._branching<DoStmt, _doStmt>,
        _java._multiPart<DoStmt, _doStmt>,
        _java._withCondition<DoStmt,_doStmt>,
        _body._hasBody<_doStmt> {

    public static _doStmt of(){
        return new _doStmt( new DoStmt( ));
    }
    public static _doStmt of(DoStmt ds){
        return new _doStmt(ds);
    }
    public static _doStmt of(String...code){
        return new _doStmt(Stmts.doStmt( code));
    }

    public static <A extends Object> _doStmt of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _doStmt of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _doStmt of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _doStmt of( Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _doStmt of( Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _doStmt of( Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _doStmt of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _doStmt of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _doStmt from( LambdaExpr le){
        Optional<DoStmt> ows = le.getBody().findFirst(DoStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No do statement found in lambda");
    }

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
            return is( Stmts.doStmt(stringRep));
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

    @Override
    public Map<_java.Feature, Object> components() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.CONDITION_EXPR, astStmt.getCondition());
        comps.put(_java.Feature.BODY, astStmt.getBody());
        return comps;
    }

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
