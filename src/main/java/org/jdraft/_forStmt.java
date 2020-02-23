package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _forStmt implements _statement._controlFlow._loop<ForStmt, _forStmt>,
        _java._multiPart<ForStmt, _forStmt>,
        //_java._withCondition<ForStmt, _forStmt>,
        _statement._controlFlow._branching<ForStmt,_forStmt>, _body._hasBody<_forStmt> {

    public static _forStmt of(){
        return new _forStmt( new ForStmt( ));
    }
    public static _forStmt of(ForStmt fs){
        return new _forStmt(fs);
    }
    public static _forStmt of(String...code){
        return new _forStmt(Stmt.forStmt( code));
    }

    public static <A extends Object> _forStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _forStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _forStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _forStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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
            return is( Stmt.forStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _forStmt forInitializations(Consumer<_expression> consumer){
        listInitializations().forEach(consumer);
        return this;
    }

    public _forStmt forUpdates(Consumer<_expression> consumer){
        listUpdates().forEach(consumer);
        return this;
    }

    public List<_expression> listInitializations(){
        List<_expression>init = new ArrayList<>();
        this.astStmt.getInitialization().forEach(i -> init.add(_expression.of(i)));
        return init;
    }

    public _forStmt addInitializations( _expression... _es){
        Arrays.stream(_es).forEach(_e -> this.astStmt.getInitialization().add(_e.ast()));
        return this;
    }

    public _forStmt addUpdates( _expression... _es){
        Arrays.stream(_es).forEach(_e -> this.astStmt.getUpdate().add(_e.ast()));
        return this;
    }

    public List<_expression> listUpdates(){
        List<_expression>update = new ArrayList<>();
        this.astStmt.getUpdate().forEach(i -> update.add(_expression.of(i)));
        return update;
    }

    public _expression getCompare(){
        if( this.astStmt.getCompare().isPresent()) {
            return _expression.of(this.astStmt.getCompare().get());
        }
        return null;
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
        this.astStmt.setCompare(Ex.of(str));
        return this;
    }

    public _forStmt setCompare(_expression e){
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

    public _forStmt setInitialization(_expression... es){
        NodeList<Expression> init = new NodeList<>();
        Arrays.stream(es).forEach(e-> init.add(e.ast()));
        this.astStmt.setInitialization(init);
        return this;
    }

    public _forStmt setUpdate(_expression... es){
        NodeList<Expression> upd = new NodeList<>();
        Arrays.stream(es).forEach(e-> upd.add(e.ast()));
        this.astStmt.setInitialization(upd);
        return this;
    }

    public _forStmt setBody(_statement _st){
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

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.INITIALIZATION, astStmt.getInitialization());
        comps.put(_java.Component.UPDATE, astStmt.getUpdate());
        comps.put(_java.Component.COMPARE, astStmt.getCompare());
        comps.put(_java.Component.BODY, astStmt.getBody());
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
