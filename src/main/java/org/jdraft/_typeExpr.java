package org.jdraft;

import com.github.javaparser.ast.expr.TypeExpr;

import java.util.function.Function;

/**
 * For example:
 * In {@code World::greet} the ClassOrInterfaceType "World" is a _typeEx
 */
public final class _typeExpr
        implements _expr<TypeExpr, _typeExpr>,
        _tree._node<TypeExpr, _typeExpr>,
        _typeRef._withType<TypeExpr, _typeExpr> {

    public static final Function<String, _typeExpr> PARSER = s-> _typeExpr.of(s);

    public static _typeExpr of(){
        return new _typeExpr( new TypeExpr());
    }
    public static _typeExpr of(TypeExpr te){
        return new _typeExpr(te);
    }
    public static _typeExpr of(String...code){
        return new _typeExpr(Expr.typeExpr( code));
    }

    public static _feature._one<_typeExpr, _typeRef> TYPE = new _feature._one<>(_typeExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_typeExpr a, _typeRef value) -> a.setType(value), PARSER);

    public static _feature._features<_typeExpr> FEATURES = _feature._features.of(_typeExpr.class,  PARSER, TYPE);

    public TypeExpr node;

    public _feature._features<_typeExpr> features(){
        return FEATURES;
    }

    public _typeExpr(TypeExpr node){
        this.node = node;
    }

    @Override
    public _typeExpr copy() {
        return new _typeExpr(this.node.clone());
    }

    public TypeExpr node(){
        return node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _typeExpr replace(TypeExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public _typeRef getType(){
        return _typeRef.of(this.node.getType());
    }

    public boolean equals(Object other){
        if( other instanceof _typeExpr){
            _typeExpr _te = ((_typeExpr)other);
            return Types.equal( _te.node().getType(), this.node.getType());
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
