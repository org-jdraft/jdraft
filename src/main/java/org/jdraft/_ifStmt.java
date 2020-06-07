package org.jdraft;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithExpression;
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

    public static _feature._features<_ifStmt> FEATURES = _feature._features.of(_ifStmt.class, CONDITION, THEN, ELSE );

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

    public _ifStmt setCondition(String...expression){
        return setCondition(Expr.of(expression));
    }

    public _ifStmt setCondition(Expression e){
        this.astStmt.setCondition(e);
        return this;
    }

    public boolean isCondition(String...conditionExpr){
        return isCondition(_expr.of(conditionExpr));
    }

    public _expr getCondition(){
        return _expr.of(this.astStmt.getCondition());
    }

    public _stmt getThen(){
        return _stmt.of( this.astStmt.getThenStmt() );
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
        this.astStmt.setThenStmt(Stmt.of(sts));
        return this;
    }

    public _ifStmt setThen(Statement st){
        this.astStmt.setThenStmt(st);
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
        this.astStmt.setThenStmt(_st.ast());
        return this;
    }

    public _ifStmt setThen(_body _bd){
        this.astStmt.setThenStmt(_bd.ast());
        return this;
    }

    public _ifStmt setThen(BlockStmt body) {
        this.astStmt.setThenStmt(body);
        return this;
    }

    public _ifStmt clearThen() {
        this.astStmt.setThenStmt(new EmptyStmt());
        return this;
    }

    /**
     * NO AND THEN!
     * @param startStatementIndex
     * @param statements
     * @return
     */
    public _ifStmt addThen(int startStatementIndex, Statement... statements) {
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
        this.astStmt.removeElseStmt();
        return this;
    }

    public _stmt getElse(){
        if( this.astStmt.getElseStmt().isPresent()) {
            return _stmt.of(this.astStmt.getElseStmt().get());
        }
        return null;
    }

    public _ifStmt setElse(String...stmt){
        this.astStmt.setElseStmt(Stmt.of(stmt));
        return this;
    }

    public _ifStmt setElse(Statement st){
        this.astStmt.setElseStmt(st);
        return this;
    }

    public _ifStmt setElse(_stmt _st){
        this.astStmt.setElseStmt(_st.ast());
        return this;
    }

    public _ifStmt setElse(_body _bd){
        this.astStmt.setElseStmt(_bd.ast());
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

    public IfStmt ast(){
        return astStmt;
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
}
