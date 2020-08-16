package org.jdraft;

import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SuperExpr;
import org.jdraft.text.Text;

import java.util.function.Function;

/**
 * Usage of the super keyword
 *
 * <code>super.doIt()</code> is a MethodCallExpr of method doIt(), and a SuperExpr as its scope.
 * This SuperExpr has no typeName.
 *
 * <code>super.MyType.doIt()</code> is a MethodCallExpr of method doIt(), and a SuperExpr as its scope.
 * This SuperExpr has typeName "MyType".
 *
 * <code>super.name</code> is a FieldAccessExpr of field greet, and a SuperExpr as its scope.
 * This SuperExpr has no typeName.
 *
 * @see _constructorCallStmt
 */
public final class _superExpr implements _expr<SuperExpr, _superExpr>, _tree._node<SuperExpr, _superExpr> {

    public static final Function<String, _superExpr> PARSER = s-> _superExpr.of(s);

    public static _superExpr of(){
        return new _superExpr( new SuperExpr());
    }
    public static _superExpr of(SuperExpr se){
        return new _superExpr(se);
    }
    public static _superExpr of(String...code){
        String st = Text.combine(code);
        if(st.startsWith("super")) {
            st = st.substring("super".length() -1).trim();
        }
        if( st.length() > 0) {
            return new _superExpr(new SuperExpr(new Name(st)));
        }
        return new _superExpr( new SuperExpr());
    }

    public static _feature._one<_superExpr, String> TYPE_NAME = new _feature._one<>(_superExpr.class, String.class,
            _feature._id.TYPE_NAME,
            a -> a.getTypeName(),
            (_superExpr p, String s) -> p.setTypeName(s), PARSER);

    public static _feature._features<_superExpr> FEATURES = _feature._features.of(_superExpr.class,  PARSER, TYPE_NAME );

    public SuperExpr node;

    public _feature._features<_superExpr> features(){
        return FEATURES;
    }

    public _superExpr(SuperExpr node){
        this.node = node;
    }

    @Override
    public _superExpr copy() {
        return new _superExpr(this.node.clone());
    }

    @Override
    public boolean is(SuperExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public SuperExpr node(){
        return node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _superExpr replace(SuperExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public _superExpr setTypeName( String name ){
        this.node.setTypeName(Ast.name(name));
        return this;
    }

    public _superExpr setTypeName( Name name ){
        this.node.setTypeName(name);
        return this;
    }

    public Name getTypeNameNode(){
        if(this.node.getTypeName().isPresent()){
            return node.getTypeName().get();
        }
        return null;
    }

    public String getTypeName(){
        if(this.node.getTypeName().isPresent()){
            return node.getTypeName().get().asString();
        }
        return "";
    }

    public boolean equals(Object other){
        if( other instanceof _superExpr){
            return ((_superExpr)other).node.equals( this.node);
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
