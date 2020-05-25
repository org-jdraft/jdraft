package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;
import java.util.function.*;


public final class _ifStmt implements _stmt._controlFlow._conditional<IfStmt, _ifStmt>, _body._withBody<_ifStmt>,
        _java._withCondition<IfStmt, _ifStmt>,
        _java._node<IfStmt, _ifStmt> {

    public static _ifStmt of(){
        return new _ifStmt( new IfStmt( ));
    }
    public static _ifStmt of(IfStmt is){
        return new _ifStmt( is);
    }

    public static _ifStmt of(String...code){
        return new _ifStmt(Stmts.ifStmt( code));
    }


    public static <A extends Object> _ifStmt of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _ifStmt of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _ifStmt of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _ifStmt of( Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _ifStmt of( Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _ifStmt of( Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _ifStmt of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _ifStmt of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _ifStmt from( LambdaExpr le){
        Optional<IfStmt> ows = le.getBody().findFirst(IfStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No if statement found in lambda");
    }

    public static _feature._one<_ifStmt, _expr> CONDITION = new _feature._one<>(_ifStmt.class, _expr.class,
            _feature._id.CONDITION_EXPR,
            a -> a.getCondition(),
            (_ifStmt a, _expr _e) -> a.setCondition(_e));

    /** could be a single statement, or a block stmt */
    public static _feature._one<_ifStmt, _stmt> THEN = new _feature._one<>(_ifStmt.class, _stmt.class,
            _feature._id.THEN,
            a -> a.getThen(),
            (_ifStmt a, _stmt b) -> a.setThen(b));

    /** could be a single statement of a block statement or null */
    public static _feature._one<_ifStmt, _stmt> ELSE = new _feature._one<>(_ifStmt.class, _stmt.class,
            _feature._id.ELSE,
            a -> a.getElse(),
            (_ifStmt a, _stmt b) -> a.setElse(b));


    public static _feature._meta<_ifStmt> META = _feature._meta.of(_ifStmt.class, CONDITION, THEN, ELSE );

    private IfStmt astStmt;

    public _ifStmt(IfStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _ifStmt copy() {
        return new _ifStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmts.ifStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _stmt getThen(){
        return _stmt.of( this.astStmt.getThenStmt() );
    }

    public _stmt getElse(){
        if( this.astStmt.getElseStmt().isPresent()) {
            return _stmt.of(this.astStmt.getElseStmt().get());
        }
        return null;
    }

    public _ifStmt setThen(String...sts){
        this.astStmt.setThenStmt(Stmts.of(sts));
        return this;
    }

    public _ifStmt setThen(Statement st){
        this.astStmt.setThenStmt(st);
        return this;
    }

    public _ifStmt setThen(_stmt _st){
        this.astStmt.setThenStmt(_st.ast());
        return this;
    }

    public _ifStmt setThen(_body _bd){
        this.astStmt.setThenStmt(_bd.ast());
        return this;
    }

    public _ifStmt setElse(String...stmt){
        this.astStmt.setElseStmt(Stmts.of(stmt));
        return this;
    }

    public _ifStmt setElse(Statement st){
        this.astStmt.setElseStmt(st);
        return this;
    }

    public _ifStmt setElse(_stmt _st){
        this.astStmt.setElseStmt(_st.ast());
        return this;
    }

    public _ifStmt setElse(_body _bd){
        this.astStmt.setElseStmt(_bd.ast());
        return this;
    }

    @Override
    public boolean is(IfStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public IfStmt ast(){
        return astStmt;
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.CONDITION_EXPR, astStmt.getCondition());
        comps.put(_java.Feature.THEN_EXPR, astStmt.getThenStmt());
        if( this.astStmt.getElseStmt().isPresent()) {
            comps.put(_java.Feature.ELSE_EXPR, astStmt.getElseStmt().get());
        }
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _ifStmt ){
            return Objects.equals( ((_ifStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }

    @Override
    public _body getBody() {
        return _body.of( this.astStmt );
    }

    @Override
    public _ifStmt setBody(BlockStmt body) {
        this.astStmt.setThenStmt(body);
        return this;
    }

    @Override
    public _ifStmt clearBody() {
        this.astStmt.setThenStmt(new BlockStmt());
        return this;
    }

    @Override
    public _ifStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.astStmt.getThenStmt();
        if( bd instanceof BlockStmt){
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
}
