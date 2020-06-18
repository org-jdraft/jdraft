package org.jdraft;

import com.github.javaparser.ast.expr.ThisExpr;

import java.util.function.Function;

/**
 * An occurrence of the "this" keyword. <br/>
 * <code>World.this.greet()</code> is a MethodCallExpr of method name greet,
 * and scope "World.this" which is a ThisExpr with typeName "World". <br/>
 * <code>this.name</code> is a FieldAccessExpr of field greet, and a ThisExpr as its scope.
 * This ThisExpr has no typeName.
 */
public final class _thisExpr implements _expr<ThisExpr, _thisExpr>, _tree._node<ThisExpr, _thisExpr> {
    public static _thisExpr of(){
        return new _thisExpr(new ThisExpr());
    }
    public static _thisExpr of(ThisExpr te){
        return new _thisExpr(te);
    }
    public static _thisExpr of(String...code){
        return new _thisExpr(Expr.thisExpr( code));
    }

    public static final Function<String, _thisExpr> PARSER = s-> _thisExpr.of(s);

    public static _feature._one<_thisExpr, String> TYPE_NAME = new _feature._one<>(_thisExpr.class, String.class,
            _feature._id.TYPE_NAME,
            a -> a.getTypeName(),
            (_thisExpr a, String value) -> a.setTypeName(value), PARSER);

    public static _feature._features<_thisExpr> FEATURES = _feature._features.of(_thisExpr.class,  PARSER, TYPE_NAME);

    public ThisExpr node;

    public _thisExpr(ThisExpr node){
        this.node = node;
    }

    public _feature._features<_thisExpr> features(){
        return FEATURES;
    }

    @Override
    public _thisExpr copy() {
        return new _thisExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _thisExpr replace(ThisExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(ThisExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public ThisExpr node(){
        return node;
    }

    public _thisExpr removeTypeName(){
        this.node.removeTypeName();
        return this;
    }

    public _thisExpr setTypeName(String name){
        this.node.setTypeName(Ast.name(name) );
        return this;
    }

    public String getTypeName(){
        if(this.node.getTypeName().isPresent()){
            return node.getTypeName().get().asString();
        }
        return "";
    }

    public boolean equals(Object other){
        if( other instanceof _thisExpr){
            return ((_thisExpr)other).node.equals( this.node);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public String toString(){
        return this.node.toString();
    }
}
