package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;
import java.util.function.*;

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

    /**
     * Adds this code (as statements) to the body of the block statement (at the end)
     * @param code a String representing Statement(s)
     * @return the modified _blockStmt
     */
    public _blockStmt add(String...code){
         _blockStmt _bs = of( code );
         _bs.forEach(s-> this.add(s));
         return this;
    }

    /**
     * Add all of the statements represented by the code at the index
     * @param index the index where to start adding
     * @param code the code representing the statements to be added
     * @return the modified blockStmt
     */
    public _blockStmt addAt(int index, String...code ){
        _blockStmt _bs = of( code );
        for(int i=0;i< _bs.size(); i++){
            this.addAt(i+ index, _bs.getAt(i));
        }
        return this;
    }

    /**
     * Using the body of the lambda add statements to the end of this blockStmt
     *
     * @param ec the lambda containing the body
     * @return
     */
    public _blockStmt add( Expr.Command ec ){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( Consumer<? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( Supplier<? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( Function<? extends Object, ? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( BiFunction<? extends Object, ? extends Object, ? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( Expr.TriFunction<? extends Object, ? extends Object, ? extends Object, ? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( Expr.QuadFunction<? extends Object, ? extends Object, ? extends Object, ? extends Object, ? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( BiConsumer<? extends Object, ? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( Expr.TriConsumer<? extends Object, ? extends Object, ? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt add( Expr.QuadConsumer<? extends Object, ? extends Object, ? extends Object, ? extends Object> c){
        return addFrom( Thread.currentThread().getStackTrace()[2] );
    }

    /**
     * Using the body of the lambda add statements to the end of this blockStmt
     *
     * @param ec the lambda containing the body
     * @return
     */
    public _blockStmt addAt(int index, Expr.Command ec ){
        return addFrom( index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, Consumer<? extends Object> c){
        return addFrom( index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, Supplier<? extends Object> c){
        return addFrom( index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, Function<? extends Object, ? extends Object> c){
        return addFrom( index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, BiFunction<? extends Object, ? extends Object, ? extends Object> c){
        return addFrom( index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, Expr.TriFunction<? extends Object, ? extends Object, ? extends Object, ? extends Object> c){
        return addFrom( index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, Expr.QuadFunction<? extends Object, ? extends Object, ? extends Object, ? extends Object, ? extends Object> c){
        return addFrom( index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, BiConsumer<? extends Object, ? extends Object> c){
        return addFrom(index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, Expr.TriConsumer<? extends Object, ? extends Object, ? extends Object> c){
        return addFrom(index, Thread.currentThread().getStackTrace()[2] );
    }

    public _blockStmt addAt(int index, Expr.QuadConsumer<? extends Object, ? extends Object, ? extends Object, ? extends Object> c){
        return addFrom(index, Thread.currentThread().getStackTrace()[2] );
    }

    private _blockStmt addFrom( StackTraceElement ste ){
        _lambdaExpr _le = _lambdaExpr.from(ste);
        _stmt _s = _le.getBody();
        if( _s instanceof _blockStmt ){
            _blockStmt _bs = (_blockStmt)_s;
            _bs.forEach(s-> this.add(s));
        } else{
            add( _s );
        }
        return this;
    }
    private _blockStmt addFrom(int index, StackTraceElement ste){
        _lambdaExpr _le = _lambdaExpr.from(ste);
        _stmt _s = _le.getBody();
        if( _s instanceof _blockStmt ){
            _blockStmt _bs = (_blockStmt)_s;
            this.addAt(index, _bs.list().toArray(new _stmt[0]));
        } else{
            this.addAt(index, _s );
        }
        return this;
    }

    /**
     * Remove all statements from the blockStmts "top level" statement that are of the stmtClass
     * and match the predicate.
     *
     * NOTE: this is NOT a walk, where Statements within Statements are removed (i.e.
     * if I have this block:
     * <PRE>
     * {
     *     assert(1==1) : "message" ;
     *     if(true){
     *         assert(1==1) : "message";
     *     }
     * }
     * </PRE>
     * _blockStmt bs = ...;
     * bs.remove(_assertStmt.class, _as-> as.hasMessage());
     *
     * ...will produce this:
     * <PRE>
     * {
     *     if(true){
     *         assert(1==1) : "message";
     *     }
     * }
     * </PRE>
     * ...because the assertStmt WITHIN the ifStmt is NOT a top level statement
     *
     * @see _blockStmt#walk().remove(
     * @param stmtClass
     * @param matchFn
     * @param <_S>
     * @return
     */
    public <_S extends _stmt> _blockStmt remove(Class<_S> stmtClass, Predicate<_S> matchFn){
        remove(_s-> {
            if( stmtClass.isAssignableFrom( _s.getClass())){
                return matchFn.test( (_S) _s);
            }
            return false;
        });
        return this;
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.blockStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public BlockStmt ast(){
        return bs;
    }

    public String toString(){
        return this.bs.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _blockStmt ){
            List<_stmt> tss = this.list();
            List<_stmt> oss = ((_blockStmt) other).list();
            if( tss.size() != oss.size() ){
                return false;
            }
            for(int i=0;i<tss.size(); i++){
                if( !Objects.equals( tss.get(i), oss.get(i) )) {
                    return false;
                }
            }
            return true;
            //this.toString(Print.PRINT_NO_COMMENTS).equals( ((_blockStmt)other).toString(Print.PRINT_NO_COMMENTS));
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}
