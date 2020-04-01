package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;

import java.util.*;
import java.util.function.*;

public final class _whileStmt implements _statement._controlFlow._branching<WhileStmt, _whileStmt>,
        _java._multiPart<WhileStmt, _whileStmt>,
        _statement._controlFlow._loop<WhileStmt, _whileStmt>,
        _java._withCondition<WhileStmt, _whileStmt>,
        _body._hasBody<_whileStmt>{

    public static _whileStmt of(){
        return new _whileStmt( new WhileStmt( ));
    }
    public static _whileStmt of(WhileStmt ws){
        return new _whileStmt(ws);
    }
    public static _whileStmt of(String...code){
        return new _whileStmt(Statements.whileStmt( code));
    }

    public static <A extends Object> _whileStmt of(Expressions.Command c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _whileStmt of(Consumer<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _whileStmt of(BiConsumer<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _whileStmt of( Expressions.TriConsumer<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _whileStmt of( Expressions.QuadConsumer<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _whileStmt of( Function<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _whileStmt of( BiFunction<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _whileStmt of( Expressions.TriFunction<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _whileStmt from( LambdaExpr le){
        Optional<WhileStmt> ows = le.getBody().findFirst(WhileStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No while statement found in lambda");
    }

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
            return is( Statements.whileStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    /*
    public boolean isCondition(String...expression){
        try{
            return isCondition(Ex.of(expression));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isCondition(_expression _ex){
        return Objects.equals( this.getCondition(), _ex.ast());
    }

    public boolean isCondition(Predicate<_expression> matchFn){
        return matchFn.test(getCondition());
    }

    public boolean isCondition(Expression ex){
        return Objects.equals( this.getCondition(), ex);
    }

    public _whileStmt setCondition(String...expression){
        return setCondition(Ex.of(expression));
    }

    public _whileStmt setCondition(Expression e){
        this.ast().setCondition(e);
        return this;
    }

    public _whileStmt setCondition(_expression e){
        this.whileStmt.setCondition(e.ast());
        return this;
    }

    public _expression getCondition(){
        return _expression.of(this.whileStmt.getCondition());
    }
    */

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

    public _whileStmt setBody(_statement _st){
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

    @Override
    public boolean is(WhileStmt astNode) {
        return this.whileStmt.equals( astNode);
    }

    public WhileStmt ast(){
        return whileStmt;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.CONDITION, whileStmt.getCondition());
        comps.put(_java.Component.BODY, whileStmt.getBody());
        return comps;
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
