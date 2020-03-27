package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _blockStmt implements _statement<BlockStmt, _blockStmt>,
        _java._list<Statement, _statement, _blockStmt> {

    public static _blockStmt of(){
        return new _blockStmt( new BlockStmt( ));
    }
    public static _blockStmt of(BlockStmt bs){
        return new _blockStmt(bs);
    }
    public static _blockStmt of(String...code){
        return new _blockStmt(Statements.blockStmt( code));
    }


    public static <A extends Object> _blockStmt of(Expressions.Command c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _blockStmt of(Consumer<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _blockStmt of(BiConsumer<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _blockStmt of( Expressions.TriConsumer<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _blockStmt of( Expressions.QuadConsumer<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _blockStmt of( Function<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _blockStmt of( BiFunction<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _blockStmt of( Expressions.TriFunction<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _blockStmt from( LambdaExpr le){
        Optional<BlockStmt> ows = le.getBody().findFirst(BlockStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No block statement found in lambda");
    }

    private BlockStmt bs;

    public _blockStmt(BlockStmt rs){
        this.bs = rs;
    }

    @Override
    public _blockStmt copy() {
        return new _blockStmt( this.bs.clone());
    }

    @Override
    public List<_statement> list() {
        List<_statement> _sts  = new ArrayList<>();
        this.bs.getStatements().forEach(s -> _sts.add( _statement.of(s)));
        return _sts;
    }

    @Override
    public NodeList<Statement> listAstElements() {
        return this.bs.getStatements();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Statements.blockStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(BlockStmt astNode) {
        return this.bs.equals( astNode);
    }

    public BlockStmt ast(){
        return bs;
    }

    public String toString(){
        return this.bs.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _blockStmt ){
            return Objects.equals( ((_blockStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
