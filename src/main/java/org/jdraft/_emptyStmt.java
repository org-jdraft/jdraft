package org.jdraft;

import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.stmt.EmptyStmt;

import java.util.Objects;
import java.util.function.Function;

/**
 * models the absence of a statement (specifically a placeholder for statements in loops)
 *
 * i.e. ";" (i.e. "for(int i=0;;)")
 */
public final class _emptyStmt implements _stmt<EmptyStmt, _emptyStmt> {

    public static final Function<String, _emptyStmt> PARSER = s-> _emptyStmt.of();

    public static _emptyStmt of(){
        return new _emptyStmt( new EmptyStmt( ));
    }
    public static _emptyStmt of(EmptyStmt es){
        return new _emptyStmt( es);
    }
    public static _emptyStmt of(String...code){
        return new _emptyStmt(Stmt.emptyStmt( code));
    }

    public static _feature._features<_emptyStmt> FEATURES = _feature._features.of(_emptyStmt.class, PARSER);

    private EmptyStmt node;

    public _feature._features<_emptyStmt> features(){
        return FEATURES;
    }

    public _emptyStmt(EmptyStmt rs){
        this.node = rs;
    }

    @Override
    public _emptyStmt copy() {
        return new _emptyStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _emptyStmt replace(EmptyStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(EmptyStmt astNode) {
        return this.node.equals( astNode);
    }

    public EmptyStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _emptyStmt ){
            return Objects.equals( ((_emptyStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
