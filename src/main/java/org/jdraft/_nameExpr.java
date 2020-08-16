package org.jdraft;

import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.Objects;
import java.util.function.Function;

public final class _nameExpr
        implements _expr<NameExpr, _nameExpr>,
        _tree._node<NameExpr, _nameExpr>,
        _java._withName<_nameExpr> {

    public static final Function<String, _nameExpr> PARSER = s-> _nameExpr.of(s);

    public static _nameExpr of(){
        return new _nameExpr( new NameExpr( ));
    }
    public static _nameExpr of(NameExpr ne){
        return new _nameExpr( ne);
    }
    public static _nameExpr of(String...code){
        return new _nameExpr(Expr.nameExpr( code));
    }

    public static _feature._one<_nameExpr, SimpleName> NAME = new _feature._one<>(_nameExpr.class, SimpleName.class,
            _feature._id.NAME,
            a -> a.getNameNode(),
            (_nameExpr a, SimpleName sn) -> a.setName(sn), PARSER);

    public static _feature._features<_nameExpr> FEATURES = _feature._features.of(_nameExpr.class, PARSER, NAME);

    public NameExpr node;

    public _nameExpr(NameExpr node){
        this.node = node;
    }

    public _feature._features<_nameExpr> features(){
        return FEATURES;
    }

    @Override
    public _nameExpr copy() {
        return new _nameExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _nameExpr replace(NameExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(NameExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public NameExpr node(){
        return node;
    }

    public SimpleName getNameNode(){ return this.node.getName(); }

    public String getName(){
        return this.node.getNameAsString();
    }

    public String toString(){
        return this.node.toString();
    }

    public _nameExpr setName( SimpleName sn) {
        this.node.setName(sn);
        return this;
    }

    public _nameExpr setName(String name){
        this.node.setName(name);
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _nameExpr){
            return Objects.equals( ((_nameExpr)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}
