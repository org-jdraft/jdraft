package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.*;
import java.util.function.*;

public final class _forStmt implements _stmt._controlFlow._loop<ForStmt, _forStmt>,
        _java._node<ForStmt, _forStmt>,
        _stmt._controlFlow._branching<ForStmt,_forStmt>, _body._hasBody<_forStmt> {

    public static _forStmt of(){
        return new _forStmt( new ForStmt( ));
    }
    public static _forStmt of(ForStmt fs){
        return new _forStmt(fs);
    }
    public static _forStmt of(String...code){
        return new _forStmt(Stmts.forStmt( code));
    }

    public static <A extends Object> _forStmt of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _forStmt of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _forStmt of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forStmt of( Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forStmt of( Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _forStmt of( Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forStmt of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forStmt of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _forStmt from( LambdaExpr le){
        Optional<ForStmt> ows = le.getBody().findFirst(ForStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No for statement found in lambda");
    }

    private ForStmt astStmt;

    public _forStmt(ForStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _forStmt copy() {
        return new _forStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmts.forStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _forStmt forInitializations(Consumer<_expr> consumer){
        listInitializations().forEach(consumer);
        return this;
    }

    public _forStmt forUpdates(Consumer<_expr> consumer){
        listUpdates().forEach(consumer);
        return this;
    }

    public List<_expr> listInitializations(){
        List<_expr>init = new ArrayList<>();
        this.astStmt.getInitialization().forEach(i -> init.add(_expr.of(i)));
        return init;
    }

    public <_E extends _expr> List<_E> listInitializations(Class<_E> expressionClass){
        return listInitializations( expressionClass, t->true);
    }

    public List<_expr> listInitializations(Predicate<_expr> matchFn){
        return listInitializations(_expr.class, matchFn);
    }

    public <_E extends _expr> List<_E> listInitializations(Class<_E> expressionClass, Predicate<_E> matchFn){
        List<_E> inits = new ArrayList<>();
        this.astStmt.getInitialization().forEach(i -> {
            _expr _e = _expr.of(i);
            if( expressionClass.isAssignableFrom(_e.getClass()) && matchFn.test( (_E)_e)){
                inits.add((_E)_e);
            }
        });
        return inits;
    }

    public _forStmt addInitializations( _expr... _es){
        Arrays.stream(_es).forEach(_e -> this.astStmt.getInitialization().add(_e.ast()));
        return this;
    }

    public _forStmt addUpdates( _expr... _es){
        Arrays.stream(_es).forEach(_e -> this.astStmt.getUpdate().add(_e.ast()));
        return this;
    }

    public List<_expr> listUpdates(){
        List<_expr>update = new ArrayList<>();
        this.astStmt.getUpdate().forEach(i -> update.add(_expr.of(i)));
        return update;
    }

    public List<_expr> listUpdates(Predicate<_expr> matchFn){
        return listUpdates(_expr.class, matchFn);
    }

    public <_E extends _expr> List<_E> listUpdates(Class<_E> expressionClass){
        return listUpdates( expressionClass, t->true);
    }

    public <_E extends _expr> List<_E> listUpdates(Class<_E> expressionClass, Predicate<_E> matchFn){
        List<_E> updates = new ArrayList<>();
        this.astStmt.getUpdate().forEach(i -> {
            _expr _e = _expr.of(i);
            if( expressionClass.isAssignableFrom(_e.getClass()) && matchFn.test( (_E)_e)){
                updates.add((_E)_e);
            }
        });
        return updates;
    }

    public _expr getCompare(){
        if( this.astStmt.getCompare().isPresent()) {
            return _expr.of(this.astStmt.getCompare().get());
        }
        return null;
    }

    /**
     * checks if the compare part of the forStmt equals the expression
     * @param expressionCode
     * @return
     */
    public boolean isCompare(String...expressionCode){
        try{
            return Objects.equals( _expr.of(expressionCode), getCompare() );
        } catch(Exception e){
            return false;
        }
    }

    public boolean isCompare(Class<? extends _expr>...expressionClass ){
        _expr _ec = getCompare();
        try{
            return Arrays.stream(expressionClass).anyMatch(ec -> ec.isAssignableFrom( _ec.getClass() ) );
        } catch(Exception e){
            return false;
        }
    }

    public boolean isCompare( Predicate<_expr> matchFn){
        _expr _e = getCompare();
        if( _e == null ){
            try{
                return matchFn.test(null);
            } catch(Exception e){
                return false;
            }
        }
        return matchFn.test(_e);
    }

    public _body getBody(){
        return _body.of( this.astStmt.getBody() );
    }

    @Override
    public _forStmt setBody(BlockStmt body) {
        this.astStmt.setBody(body);
        return this;
    }

    public _forStmt setCompare(String...str){
        this.astStmt.setCompare(Exprs.of(str));
        return this;
    }

    public _forStmt setCompare(_expr e){
        this.astStmt.setCompare(e.ast());
        return this;
    }

    public _forStmt setCompare(Expression e){
        this.astStmt.setCompare(e);
        return this;
    }

    public _forStmt removeCompare(){
        this.astStmt.removeCompare();
        return this;
    }

    public _forStmt setInitialization(_expr... es){
        NodeList<Expression> init = new NodeList<>();
        Arrays.stream(es).forEach(e-> init.add(e.ast()));
        this.astStmt.setInitialization(init);
        return this;
    }

    public _forStmt setUpdate(_expr... es){
        NodeList<Expression> upd = new NodeList<>();
        Arrays.stream(es).forEach(e-> upd.add(e.ast()));
        this.astStmt.setInitialization(upd);
        return this;
    }

    public _forStmt setBody(_stmt _st){
        this.astStmt.setBody(_st.ast());
        return this;
    }

    public _forStmt setBody(_body _bd){
        this.astStmt.setBody(_bd.ast());
        return this;
    }

    public _forStmt clearBody(){
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _forStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.astStmt.getBody();
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

    @Override
    public boolean is(ForStmt astNode) {
        return this.astStmt.equals( astNode);
    }

    public ForStmt ast(){
        return astStmt;
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.INIT_EXPR, astStmt.getInitialization());
        comps.put(_java.Feature.UPDATE_EXPR, astStmt.getUpdate());
        comps.put(_java.Feature.COMPARE_EXPR, astStmt.getCompare());
        comps.put(_java.Feature.BODY, astStmt.getBody());
        return comps;
    }

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _forStmt ){
            return Objects.equals( ((_forStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
