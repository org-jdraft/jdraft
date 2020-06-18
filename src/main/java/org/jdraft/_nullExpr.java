package org.jdraft;

import com.github.javaparser.ast.expr.NullLiteralExpr;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

public final class _nullExpr implements _expr._literal<NullLiteralExpr, _nullExpr> {

    public static final Function<String, _nullExpr> PARSER = s-> _nullExpr.of(s);

    public static _nullExpr of(){
        return new _nullExpr(new NullLiteralExpr());
    }

    public static _nullExpr of(NullLiteralExpr nl){
        return new _nullExpr(nl);
    }
    public static _nullExpr of(String...code){
        if( Text.combine(code).equals("null")){
            return new _nullExpr( new NullLiteralExpr());
        }
        throw new _jdraftException("invalid code for null literal "+System.lineSeparator()+ Text.combine(code));
    }

    public static _feature._features<_nullExpr> FEATURES = _feature._features.of(_nullExpr.class, PARSER);

    public NullLiteralExpr node;

    public _nullExpr(NullLiteralExpr node){
        this.node = node;
    }

    public _feature._features<_nullExpr> features(){
        return FEATURES;
    }

    @Override
    public _nullExpr copy() {
        return new _nullExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _nullExpr replace(NullLiteralExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(NullLiteralExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public NullLiteralExpr node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public String getValue(){
        return "null";
    }

    public String valueAsString(){
        return "null";
    }

    public boolean equals(Object other){
        if( other instanceof _nullExpr){
            return Objects.equals( ((_nullExpr)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
