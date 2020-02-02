package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class _tryStmt implements _statement<TryStmt, _tryStmt>, _nodeList<CatchClause, _catch, _tryStmt> {

    public static _tryStmt of(){
        return new _tryStmt( new TryStmt( ));
    }
    public static _tryStmt of(TryStmt ts){
        return new _tryStmt(ts);
    }
    public static _tryStmt of(String...code){
        return new _tryStmt(Stmt.tryStmt(code));
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
    public List<CatchClause> listAstElements() {
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

    public boolean hasTryBody(){
        return !this.ast().getTryBlock().isEmpty();
    }
    public _body getTryBody(){
        return _body.of( tryStmt.getTryBlock());
    }

    public _tryStmt setTryBody(_body _b){
        this.tryStmt.setTryBlock(_b.ast());
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
        comps.put( _java.Component.TRY_BLOCK, tryStmt.getTryBlock());
        comps.put( _java.Component.RESOURCES, tryStmt.getResources());
        comps.put(_java.Component.CATCH_CLAUSES, tryStmt.getCatchClauses());
        if( tryStmt.getFinallyBlock().isPresent() ){
            comps.put(_java.Component.FINALLY_BLOCK, tryStmt.getFinallyBlock().get());
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
