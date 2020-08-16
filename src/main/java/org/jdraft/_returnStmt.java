package org.jdraft;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * return new Component();
 */
public final class _returnStmt implements
        _stmt._returns<ReturnStmt, _returnStmt>,
        _java._withExpression<ReturnStmt, _returnStmt>{

    public static final Function<String, _returnStmt> PARSER = s-> _returnStmt.of(s);

    public static _returnStmt of(){
        return new _returnStmt( new ReturnStmt( ));
    }

    public static _returnStmt of(ReturnStmt rs){
        return new _returnStmt( rs);
    }

    public static _returnStmt of(_expr _e){
        return of().setExpression(_e.node());
    }

    public static _returnStmt of(Expression e){
        return of().setExpression(e);
    }

    public static _returnStmt of(Enum e ){
        return of().setExpression(_fieldAccessExpr.of(e.getClass().getCanonicalName()+"."+e.name()).node());
    }
    public static _returnStmt of(int literal){
        return new _returnStmt( new ReturnStmt( new IntegerLiteralExpr(literal)));
    }

    public static _returnStmt of(long literal){
        return new _returnStmt( new ReturnStmt( _longExpr.of(literal).node()));
    }

    public static _returnStmt of(char literal){
        return new _returnStmt( new ReturnStmt( new CharLiteralExpr(literal)));
    }

    public static _returnStmt of(boolean literal){
        return new _returnStmt( new ReturnStmt( new BooleanLiteralExpr(literal)));
    }

    public static _returnStmt of(float literal){
        return new _returnStmt( new ReturnStmt( new DoubleLiteralExpr(literal+"F")));
    }

    public static _returnStmt of(double literal){
        return new _returnStmt( new ReturnStmt( new DoubleLiteralExpr(literal+"D")));
    }

    public static _returnStmt ofString(String literal){
        return new _returnStmt( new ReturnStmt( new StringLiteralExpr(literal)));
    }

    public static _returnStmt ofName(String name){
        return new _returnStmt( new ReturnStmt( new NameExpr(name)));
    }

    public static _returnStmt of( String...code){
        return new _returnStmt(Stmt.returnStmt( code));
    }

    public static _feature._one<_returnStmt, _expr> EXPRESSION = new _feature._one<>(_returnStmt.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_returnStmt p, _expr _e) -> p.setExpression(_e), PARSER);

    public static _feature._features<_returnStmt> FEATURES = _feature._features.of(_returnStmt.class,  PARSER, EXPRESSION );

    private ReturnStmt node;

    public _returnStmt(ReturnStmt node){
        this.node = node;
    }

    public _feature._features<_returnStmt> features(){
        return FEATURES;
    }

    @Override
    public _returnStmt copy() {
        return new _returnStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _returnStmt replace(ReturnStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public ReturnStmt node(){
        return node;
    }

    public _returnStmt removeExpression(){
        this.node.removeExpression();
        return this;
    }

    public boolean isExpression(String...expression){
        if( !this.hasExpression() ){
            return false;
        }
        try{
            return isExpression(Expr.of(expression));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isExpression(_expr _ex){
        return Expr.equal( this.getExpression().node(), _ex.node());
    }

    public boolean isExpression(Expression ex){
        return Expr.equal( this.getExpression().node(), ex);
    }

    public boolean isExpression(Predicate<_expr> matchFn){
        return matchFn.test(getExpression());
    }

    public boolean isExpression( int i){
        return isExpression( Expr.of(i) );
    }

    public boolean isExpression( boolean b){
        return isExpression( Expr.of(b) );
    }

    public boolean isExpression( float f){
        return isExpression( Expr.of(f) );
    }

    public boolean isExpression( long l){
        return isExpression( Expr.of(l) );
    }

    public boolean isExpression( double d){
        return isExpression( Expr.of(d) );
    }

    public boolean isExpression( char c){
        return isExpression( Expr.of(c) );
    }

    public _returnStmt setExpression(String...expression){
        return setExpression(Expr.of(expression));
    }

    public _returnStmt setExpression(_expr e){
        return setExpression(e.node());
    }

    public _returnStmt setExpression(Expression e){
        this.node.setExpression(e);
        return this;
    }

    public _expr getExpression(){
        if( this.hasExpression() ){
            return _expr.of(this.node.getExpression().get());
        }
        return null;
    }

    public boolean hasExpression(){
        return this.node.getExpression().isPresent();
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _returnStmt ){
            return Objects.equals( ((_returnStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
