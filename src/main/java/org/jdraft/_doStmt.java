package org.jdraft;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public final class _doStmt implements
        _stmt._conditional<DoStmt, _doStmt>,
        _stmt._loop<DoStmt, _doStmt>,
        _java._withCondition<DoStmt,_doStmt>,
        _body._withBody<_doStmt> {

    public static final Function<String, _doStmt> PARSER = s-> _doStmt.of(s);

    /**
     * change this to do nothing (not RETURN)
     * @return
     */
    public static _doStmt of(){
        return new _doStmt( new DoStmt(null, new EmptyStmt(), new BooleanLiteralExpr()));
    }

    public static _doStmt of(DoStmt ds){
        return new _doStmt(ds);
    }
    public static _doStmt of(String...code){
        return new _doStmt(Stmt.doStmt( code));
    }

    public static <A extends Object> _doStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _doStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _doStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _doStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _doStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _doStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _doStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _doStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _doStmt from( LambdaExpr le){
        Optional<DoStmt> ows = le.getBody().findFirst(DoStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No do statement found in lambda");
    }

    public static _feature._one<_doStmt, _expr> CONDITION = new _feature._one<>(_doStmt.class, _expr.class,
            _feature._id.CONDITION,
            a -> a.getCondition(),
            (_doStmt a, _expr _e) -> a.setCondition(_e), PARSER);

    /** could be a single statement of a block statement or null */
    public static _feature._one<_doStmt, _body> BODY = new _feature._one<>(_doStmt.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_doStmt a, _body b) -> a.setBody(b), PARSER);

    public static _feature._features<_doStmt> FEATURES = _feature._features.of(_doStmt.class,  PARSER, CONDITION, BODY );

    private DoStmt node;

    public _doStmt(DoStmt rs){
        this.node = rs;
    }

    public _feature._features<_doStmt> features(){
        return FEATURES;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _doStmt replace(DoStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public _doStmt copy() {
        return new _doStmt( this.node.clone());
    }

    public _doStmt setCondition(String...expression){
        return setCondition(Expr.of(expression));
    }

    public _doStmt setCondition(_expr e){
        return setCondition(e.node());
    }

    public _doStmt setCondition(Expression e){
        this.node().setCondition(e);
        return this;
    }

    public _expr getCondition(){
        return _expr.of(this.node().getCondition());
    }

    public _body getBody(){
        return _body.of( this.node.getBody() );
    }

    @Override
    public _doStmt setBody(BlockStmt body) {
        this.node.setBody(body);
        return this;
    }

    public _doStmt setBody(_stmt _st){
        this.node.setBody(_st.node());
        return this;
    }

    public _doStmt clearBody(){
        this.node.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _doStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.node.getBody();
        if( bd instanceof BlockStmt ){
            for(int i=0;i<statements.length; i++) {
                bd.asBlockStmt().addStatement(i+startStatementIndex, statements[i]);
            }
            return this;
        }
        //just a statement
        if( bd instanceof EmptyStmt ){
            if( statements.length == 1 ){
                this.node.setBody( statements[0]);
                return this;
            }
        }
        BlockStmt bs = new BlockStmt();
        bs.addStatement(bd);
        for(int i=0;i<statements.length; i++) {
            bs.addStatement(i+startStatementIndex, statements[i]);
        }
        this.node().setBody(bs);
        return this;
    }

    public DoStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _doStmt ){
            _doStmt _o = (_doStmt)other;
            if( ! Objects.equals( getCondition(), _o.getCondition())){
                //System.out.println("condition failed");
                return false;
            }
            if( ! Objects.equals( getBody(), _o.getBody())){
                //System.out.println("body failed" +getBody()+" "+ _o.getBody());
                return false;
            }
            return true;
            //return Objects.equals( ((_doStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
