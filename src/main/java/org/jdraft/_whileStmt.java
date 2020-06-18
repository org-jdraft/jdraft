package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;

import java.util.*;
import java.util.function.*;

public final class _whileStmt implements
        _stmt._conditional<WhileStmt, _whileStmt>,
        _stmt._loop<WhileStmt, _whileStmt>,
        _java._withCondition<WhileStmt, _whileStmt>,
        _body._withBody<_whileStmt> {

    public static final Function<String, _whileStmt> PARSER = s-> _whileStmt.of(s);

    public static _whileStmt of(){
        return new _whileStmt( new WhileStmt( ));
    }
    public static _whileStmt of(WhileStmt ws){
        return new _whileStmt(ws);
    }
    public static _whileStmt of(String...code){
        return new _whileStmt(Stmt.whileStmt( code));
    }

    public static <A extends Object> _whileStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _whileStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _whileStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _whileStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _whileStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _whileStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _whileStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _whileStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _whileStmt from( LambdaExpr le){
        Optional<WhileStmt> ows = le.getBody().findFirst(WhileStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No while statement found in lambda");
    }

    public static _feature._one<_whileStmt, _expr> CONDITION = new _feature._one<>(_whileStmt.class, _expr.class,
            _feature._id.CONDITION,
            a -> a.getCondition(),
            (_whileStmt a, _expr _e) -> a.setCondition(_e), PARSER);

    public static _feature._one<_whileStmt, _body> BODY = new _feature._one<>(_whileStmt.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_whileStmt a, _body _b) -> a.setBody(_b), PARSER);

    public static _feature._features<_whileStmt> FEATURES = _feature._features.of(_whileStmt.class,  PARSER, CONDITION, BODY);

    private WhileStmt node;

    public _feature._features<_whileStmt> features(){
        return FEATURES;
    }

    public _whileStmt(WhileStmt rs){
        this.node = rs;
    }

    @Override
    public _whileStmt copy() {
        return new _whileStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _whileStmt replace(WhileStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public _body getBody(){
        return _body.of( this.node.getBody() );
    }

    @Override
    public _whileStmt setBody(BlockStmt body) {
        this.node.setBody(body);
        return this;
    }

    public boolean isBody(_body _b){
        return Objects.equals( _b, this.getBody());
    }

    public _whileStmt setBody(_stmt _st){
        this.node.setBody(_st.node());
        return this;
    }

    public _whileStmt clearBody(){
        this.node.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _whileStmt add(int startStatementIndex, Statement... statements) {
        Statement st = this.node.getBody();
        if( st instanceof BlockStmt ){
            for(int i=0;i<statements.length; i++) {
                st.asBlockStmt().addStatement(i + startStatementIndex, statements[i]);
            }
            return this;
        }
        BlockStmt bs = new BlockStmt();
        bs.addStatement(st); //add the old statement
        Arrays.stream(statements).forEach(s -> bs.addStatement(startStatementIndex, s) );
        this.node.setBody(bs);
        return this;
    }

    public WhileStmt node(){
        return node;
    }

    public boolean equals(Object other){
        if( other instanceof _whileStmt ){
            return Objects.equals( ((_whileStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }

    public String toString(){
        return this.node.toString();
    }
}
