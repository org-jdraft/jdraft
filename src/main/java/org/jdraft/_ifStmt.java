package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


public class _ifStmt implements _statement._controlFlow._branching<IfStmt, _ifStmt>, _body._hasBody<_ifStmt>,
        _java._compoundNode<IfStmt, _ifStmt> {

    public static _ifStmt of(){
        return new _ifStmt( new IfStmt( ));
    }
    public static _ifStmt of(IfStmt is){
        return new _ifStmt( is);
    }

    public static _ifStmt of(String...code){
        return new _ifStmt(Stmt.ifStmt( code));
    }


    public static <A extends Object> _ifStmt of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _ifStmt of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _ifStmt of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _ifStmt of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _ifStmt of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _ifStmt of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _ifStmt of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _ifStmt of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _ifStmt from( LambdaExpr le){
        Optional<IfStmt> ows = le.getBody().findFirst(IfStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No if statement found in lambda");
    }


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
            return is( Stmt.ifStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public _expression getCondition(){
        return _expression.of(this.astStmt.getCondition());
    }

    public _statement getThen(){
        return _statement.of( this.astStmt.getThenStmt() );
    }

    public _statement getElse(){
        if( this.astStmt.getElseStmt().isPresent()) {
            return _statement.of(this.astStmt.getElseStmt().get());
        }
        return null;
    }

    public _ifStmt setCondition(_expression e){
        this.astStmt.setCondition(e.ast());
        return this;
    }

    public _ifStmt setThen(_statement _st){
        this.astStmt.setThenStmt(_st.ast());
        return this;
    }

    public _ifStmt setThen(_body _bd){
        this.astStmt.setThenStmt(_bd.ast());
        return this;
    }

    public _ifStmt setElse(_statement _st){
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

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.CONDITION, astStmt.getCondition());
        comps.put(_java.Component.THEN, astStmt.getThenStmt());
        if( this.astStmt.getElseStmt().isPresent()) {
            comps.put(_java.Component.ELSE, astStmt.getElseStmt().get());
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
