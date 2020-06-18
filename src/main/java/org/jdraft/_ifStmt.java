package org.jdraft;

import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.*;
import java.util.function.*;

/**
 * <PRE>
 *     //if with single statement
 *     if(true)
 *        System.out.println("Its true");
 * </PRE>
 *
 * <PRE>
 *  //if with blockStmt
 *  if(true){
 *     Object res = callThing();
 *     return res;
 *  }
 *  </PRE>
 */
public final class _ifStmt implements
        _stmt._conditional<IfStmt, _ifStmt>,
        _java._withCondition<IfStmt, _ifStmt> {

    public static final Function<String, _ifStmt> PARSER = s-> _ifStmt.of(s);

    public static _ifStmt of(){
        return new _ifStmt( new IfStmt( ));
    }

    public static _ifStmt of(IfStmt is){
        return new _ifStmt( is);
    }

    public static _ifStmt of(String...code){
        return new _ifStmt(Stmt.ifStmt( code));
    }

    public static <A extends Object> _ifStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _ifStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _ifStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _ifStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _ifStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _ifStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _ifStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _ifStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _ifStmt from( LambdaExpr le){
        Optional<IfStmt> ows = le.getBody().findFirst(IfStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No if statement found in lambda");
    }

    public static _feature._one<_ifStmt, _expr> CONDITION = new _feature._one<>(_ifStmt.class, _expr.class,
            _feature._id.CONDITION,
            a -> a.getCondition(),
            (_ifStmt a, _expr _e) -> a.setCondition(_e), PARSER);

    /** could be a single statement, or a block stmt */
    public static _feature._one<_ifStmt, _stmt> THEN = new _feature._one<>(_ifStmt.class, _stmt.class,
            _feature._id.THEN,
            a -> a.getThen(),
            (_ifStmt a, _stmt b) -> a.setThen(b), PARSER);

    /** could be a single statement of a block statement or null */
    public static _feature._one<_ifStmt, _stmt> ELSE = new _feature._one<>(_ifStmt.class, _stmt.class,
            _feature._id.ELSE,
            a -> a.getElse(),
            (_ifStmt a, _stmt b) -> a.setElse(b), PARSER);

    public static _feature._features<_ifStmt> FEATURES = _feature._features.of(_ifStmt.class,  PARSER, CONDITION, THEN, ELSE );

    private IfStmt node;

    public _ifStmt(IfStmt rs){
        this.node = rs;
    }

    public _feature._features<_ifStmt> features(){
        return FEATURES;
    }

    @Override
    public _ifStmt copy() {
        return new _ifStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _ifStmt replace(IfStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public _ifStmt setCondition(String...expression){
        return setCondition(Expr.of(expression));
    }

    public _ifStmt setCondition(Expression e){
        this.node.setCondition(e);
        return this;
    }

    public boolean isCondition(String...conditionExpr){
        return isCondition(_expr.of(conditionExpr));
    }

    public _expr getCondition(){
        return _expr.of(this.node.getCondition());
    }

    public _stmt getThen(){
        return _stmt.of( this.node.getThenStmt() );
    }

    public <_TI extends _stmt> boolean isThen(Class<_TI> thenImplClass ){
        return thenImplClass.isAssignableFrom( getThen().getClass() );
    }

    public <_TI extends _stmt> boolean isThen(Class<_TI> thenImplClass, Predicate<_TI> matchFn ){
        return thenImplClass.isAssignableFrom( getThen().getClass() ) && matchFn.test( (_TI)getThen());
    }



    public boolean isThen(_stmt _st){
        return Objects.equals( getThen(), _st);
    }

    public boolean isThen(String... stmts){
        return Objects.equals( getThen(), _stmt.of(stmts));
    }

    public _ifStmt setThen(String...sts){
        this.node.setThenStmt(Stmt.of(sts));
        return this;
    }

    public _ifStmt setThen(Statement st){
        this.node.setThenStmt(st);
        return this;
    }

    public _ifStmt setThen( Expr.Command lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    public _ifStmt setThen( Consumer<? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    public _ifStmt setThen( Supplier<? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    public _ifStmt setThen( BiConsumer<? extends Object, ? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    public _ifStmt setThen( Expr.TriConsumer<? extends Object, ? extends Object, ? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    public _ifStmt setThen( Expr.QuadConsumer<? extends Object, ? extends Object, ? extends Object,? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    public _ifStmt setThen( Function<? extends Object, ? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    public _ifStmt setThen( BiFunction<? extends Object, ? extends Object, ? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    public _ifStmt setThen( Expr.TriFunction<? extends Object, ? extends Object, ? extends Object,? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setThen(_l.getBody());
    }

    /**
     * is the then branch a _blockStmt, i.e.
     * <PRE>
     * if( true ){
     *
     * }
     * </PRE>
     * @return
     */
    public boolean isThenBlock(){
        return getThen() instanceof _blockStmt;
    }

    public _ifStmt setThen(_stmt _st){
        this.node.setThenStmt(_st.node());
        return this;
    }

    public _ifStmt setThen(_body _bd){
        this.node.setThenStmt(_bd.ast());
        return this;
    }

    public _ifStmt setThen(BlockStmt body) {
        this.node.setThenStmt(body);
        return this;
    }

    public _ifStmt clearThen() {
        this.node.setThenStmt(new EmptyStmt());
        return this;
    }

    /**
     * NO AND THEN!
     * @param startStatementIndex
     * @param statements
     * @return
     */
    public _ifStmt addThen(int startStatementIndex, Statement... statements) {
        Statement bd = this.node.getThenStmt();
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

    public boolean isElseBlock(){
        return getElse() instanceof _blockStmt;
    }

    public boolean hasElse(){
        return getElse() != null;
    }

    public <_TI extends _stmt> boolean isElse(Class<_TI> elseImplClass ){
        if( hasElse() ) {
            return elseImplClass.isAssignableFrom(getElse().getClass());
        }
        return false;
    }

    public <_TI extends _stmt> boolean isElse(Class<_TI> elseImplClass, Predicate<_TI> matchFn ){
        if (hasElse()) {
            return elseImplClass.isAssignableFrom( getElse().getClass() ) && matchFn.test( (_TI)getElse());
        }
        return false;
    }

    public _ifStmt removeElse(){
        this.node.removeElseStmt();
        return this;
    }

    public _stmt getElse(){
        if( this.node.getElseStmt().isPresent()) {
            return _stmt.of(this.node.getElseStmt().get());
        }
        return null;
    }

    public _ifStmt setElse(String...stmt){
        this.node.setElseStmt(Stmt.of(stmt));
        return this;
    }

    public _ifStmt setElse(Statement st){
        this.node.setElseStmt(st);
        return this;
    }

    public _ifStmt setElse(_stmt _st){
        this.node.setElseStmt(_st.node());
        return this;
    }

    public _ifStmt setElse(_body _bd){
        this.node.setElseStmt(_bd.ast());
        return this;
    }

    public _ifStmt setElse( Expr.Command lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public _ifStmt setElse( Consumer<? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public _ifStmt setElse( Supplier<? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public _ifStmt setElse( BiConsumer<? extends Object, ? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public _ifStmt setElse( Expr.TriConsumer<? extends Object, ? extends Object, ? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public _ifStmt setElse( Expr.QuadConsumer<? extends Object, ? extends Object, ? extends Object,? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public _ifStmt setElse( Function<? extends Object, ? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public _ifStmt setElse( BiFunction<? extends Object, ? extends Object, ? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public _ifStmt setElse( Expr.TriFunction<? extends Object, ? extends Object, ? extends Object,? extends Object> lambda){
        _lambdaExpr _l = _lambdaExpr.from(Thread.currentThread().getStackTrace()[2]);
        return setElse(_l.getBody());
    }

    public IfStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _ifStmt ){
            return Objects.equals( ((_ifStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
