package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _blockStmt implements _stmt<BlockStmt, _blockStmt>,
        _java._list<Statement, _stmt, _blockStmt> {

    public static final Function<String, _blockStmt> PARSER = s-> _blockStmt.of(s);

    public static _blockStmt of(){
        return new _blockStmt( new BlockStmt( ));
    }
    public static _blockStmt of(BlockStmt bs){
        return new _blockStmt(bs);
    }
    public static _blockStmt of(String...code){
        return new _blockStmt(Stmt.blockStmt( code));
    }

    public static <A extends Object> _blockStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _blockStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _blockStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _blockStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _blockStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _blockStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _blockStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _blockStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _blockStmt from( LambdaExpr le){
        Optional<BlockStmt> ows = le.getBody().findFirst(BlockStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No block statement found in lambda");
    }

    public static _feature._many<_blockStmt, _stmt> STATEMENTS = new _feature._many<>(_blockStmt.class, _stmt.class,
            _feature._id.STATEMENTS, _feature._id.STATEMENT,
            a->a.list(),
            (_blockStmt a, List<_stmt> es)-> a.set(es), PARSER,s-> _stmt.of(s))
            .setOrdered(true);/** the order of the statements matter */

    public static _feature._features<_blockStmt> FEATURES = _feature._features.of(_blockStmt.class, STATEMENTS);

    private BlockStmt bs;

    public _blockStmt(BlockStmt rs){
        this.bs = rs;
    }

    @Override
    public _blockStmt copy() {
        return new _blockStmt( this.bs.clone());
    }

    @Override
    public List<_stmt> list() {
        List<_stmt> _sts  = new ArrayList<>();
        this.bs.getStatements().forEach(s -> _sts.add( _stmt.of(s)));
        return _sts;
    }

    @Override
    public NodeList<Statement> listAstElements() {
        return this.bs.getStatements();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.blockStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    /*
    @Override
    public boolean is(BlockStmt astNode) {
        return this.toString(Print.PRINT_NO_COMMENTS).equals(astNode.toString(Print.PRINT_NO_COMMENTS));
    }
     */

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
