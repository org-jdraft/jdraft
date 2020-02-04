package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class _tryStmt implements _statement._controlFlow._branching<TryStmt, _tryStmt>,
        _java._nodeList<CatchClause, _catch, _tryStmt>,
        _body._hasBody<_tryStmt>{

    public static _tryStmt of(){
        return new _tryStmt( new TryStmt( ));
    }
    public static _tryStmt of(TryStmt ts){
        return new _tryStmt(ts);
    }
    public static _tryStmt of(String...code){
        return new _tryStmt(Stmt.tryStmt(code));
    }


    public static <A extends Object> _tryStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _tryStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _tryStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _tryStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _tryStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _tryStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _tryStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _tryStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _tryStmt from( LambdaExpr le){
        Optional<TryStmt> ows = le.getBody().findFirst(TryStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No Try statement found in lambda");
    }

    private TryStmt tryStmt;

    public _tryStmt(TryStmt tryStmt){
        this.tryStmt = tryStmt;
    }

    public TryStmt ast(){
        return tryStmt;
    }

    @Override
    public _tryStmt copy() {
        return new _tryStmt( this.tryStmt.clone());
    }

    @Override
    public List<_catch> list() {
        return this.listCatches();
    }

    @Override
    public NodeList<CatchClause> listAstElements() {
        return  this.ast().getCatchClauses();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.tryStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(TryStmt astNode) {
        return this.tryStmt.equals( astNode);
    }

    public boolean hasBody(){
        return !this.ast().getTryBlock().isEmpty();
    }

    public _body getBody(){
        return _body.of( tryStmt.getTryBlock());
    }

    @Override
    public _tryStmt setBody(BlockStmt body) {
        this.tryStmt.setTryBlock(body);
        return this;
    }

    public _tryStmt setBody(_body _b){
        this.tryStmt.setTryBlock(_b.ast());
        return this;
    }

    @Override
    public _tryStmt clearBody() {
        this.tryStmt.setTryBlock(new BlockStmt());
        return this;
    }

    @Override
    public _tryStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.tryStmt.getTryBlock();
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


    public boolean hasWithResources(){
        return ! this.ast().getResources().isEmpty();
    }

    public _tryStmt setWithResources(_expression... _exs){
        NodeList<Expression> nle = new NodeList<>();
        Arrays.stream(_exs).forEach( _e -> nle.add(_e.ast()));
        this.ast().setResources(nle);
        return this;
    }

    public _tryStmt addWithResources(String...exs){
        Arrays.stream(exs).forEach( e -> this.ast().getResources().add(Ex.of(e)));
        return this;
    }

    public _tryStmt addWithResources(Expression...exs){
        Arrays.stream(exs).forEach( e -> this.ast().getResources().add(e));
        return this;
    }


    public _tryStmt addWithResources(_expression..._exs){
        Arrays.stream(_exs).forEach( _e -> this.ast().getResources().add(_e.ast()));
        return this;
    }

    public _tryStmt removeWithResource(_expression..._exs){
        Arrays.stream(_exs).forEach( _e -> this.ast().getResources().remove(_e.ast()));
        return this;
    }

    public _tryStmt removeWithResource(Predicate<_expression> matchFn){
        List<_expression> toRemove = listWithResources(matchFn);
        toRemove.forEach( _e -> this.ast().getResources().remove(_e.ast()));
        return this;
    }

    public List<_expression> listWithResources(){
        List<_expression> exs = new ArrayList<>();
        this.tryStmt.getResources().forEach( e -> exs.add(_expression.of(e)));
        return exs;
    }

    public List<_expression> listWithResources(Predicate<_expression> matchFn){
        List<_expression> exs = listWithResources();
        return exs.stream().filter(matchFn).collect(Collectors.toList());
    }


    public boolean hasCatch(){
        return ! this.ast().getCatchClauses().isEmpty();
    }

    public boolean hasCatch(Predicate<_catch> matchFn){
        return list(matchFn).size() > 0;
    }

    /**
     * List the catchClauses
     * @return
     */
    public List<_catch> listCatches(){
        List<_catch> _ccs = new ArrayList<>();
        this.ast().getCatchClauses().forEach(cc -> _ccs.add(_catch.of(cc)));
        return _ccs;
    }

    public _tryStmt addCatch(String...catchClause){
        CatchClause cc = Ast.catchClause(catchClause);
        return add(cc);
    }

    //does this try statement have a finally body that is NON-EMPTY
    public boolean hasFinallyBody(){
        return this.ast().getFinallyBlock().isPresent()
                &&  !( this.ast().getFinallyBlock().get().isEmpty());
    }

    public _body getFinallyBody(){
        if( this.tryStmt.getFinallyBlock().isPresent()){
            return _body.of( tryStmt.getFinallyBlock() );
        }
        return null;
    }

    public _tryStmt setFinallyBody(_body _b){
        this.tryStmt.setFinallyBlock(_b.ast());
        return this;
    }

    public _tryStmt setFinallyBody(_blockStmt finallyBlock){
        this.tryStmt.setFinallyBlock(finallyBlock.ast());
        return this;
    }

    public _tryStmt removeFinally(){
        this.tryStmt.removeFinallyBlock();
        return this;
    }

    public String toString(){
        return this.tryStmt.toString();
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put( _java.Component.TRY_BODY, tryStmt.getTryBlock());
        comps.put( _java.Component.WITH_RESOURCES, tryStmt.getResources());
        comps.put(_java.Component.CATCH_CLAUSES, tryStmt.getCatchClauses());
        if( tryStmt.getFinallyBlock().isPresent() ){
            comps.put(_java.Component.FINALLY_BODY, tryStmt.getFinallyBlock().get());
        }
        return comps;
    }


    public boolean equals(Object other){
        if( other instanceof _tryStmt ){
            return Objects.equals( ((_tryStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
