package org.jdraft;

import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import org.jdraft.text.Text;

import java.util.function.Function;

/**
 * 
 * <PRE><CODE>String.class</CODE></PRE>
 */
public final class _classExpr implements _expr<ClassExpr, _classExpr>, _typeRef._withType<ClassExpr, _classExpr>, _tree._node<ClassExpr, _classExpr> {

    public static final Function<String, _classExpr> PARSER = s-> _classExpr.of(s);

    public static _classExpr of(){
        return new _classExpr(new ClassExpr());
    }

    public static _classExpr of(Class clazz){
        return new _classExpr(Expr.classExpr( clazz) );
    }

    public static _classExpr of(ClassExpr ce){
        return new _classExpr(ce);
    }

    public static _classExpr of(String...code){
        return new _classExpr(Expr.classExpr( code));
    }

    public static _feature._one<_classExpr, _typeRef> TYPE = new _feature._one<>(_classExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_classExpr a, _typeRef o) -> a.setType(o), PARSER);

    public static _feature._features<_classExpr> FEATURES = _feature._features.of(_classExpr.class,  PARSER, TYPE);

    public ClassExpr node;

    public _classExpr(ClassExpr ce){
        this.node = ce;
    }

    public _feature._features<_classExpr> features(){
        return FEATURES;
    }

    @Override
    public _classExpr copy() {
        return new _classExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _classExpr replace(ClassExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public ClassExpr node(){
        return node;
    }

    public _classExpr setType(String...type ){
        return setType( _typeRef.of(Text.combine(type)));
    }

    public boolean equals(Object other){
        if( other instanceof _classExpr){
            return ((_classExpr)other).node.equals( this.node);
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
