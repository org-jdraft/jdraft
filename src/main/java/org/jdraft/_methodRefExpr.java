package org.jdraft;

import com.github.javaparser.ast.expr.MethodReferenceExpr;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * ContainingClass::staticMethodName
 * containingObject::instanceMethodName
 * ContainingType::methodName
 * ClassName::new
 */
public final class _methodRefExpr implements _expr<MethodReferenceExpr, _methodRefExpr>,
        _tree._node<MethodReferenceExpr, _methodRefExpr>,
        _java._withScope<MethodReferenceExpr, _methodRefExpr>,
        _typeArgs._withTypeArgs<MethodReferenceExpr, _methodRefExpr> {

    public static final Function<String, _methodRefExpr> PARSER = s-> _methodRefExpr.of(s);

    public static _methodRefExpr of(){
        return new _methodRefExpr( new MethodReferenceExpr());
    }

    public static _methodRefExpr of(MethodReferenceExpr mre){
        return new _methodRefExpr(mre);
    }

    public static _methodRefExpr of(String...code){
        return new _methodRefExpr(Expr.methodReferenceExpr( code));
    }

    public static _feature._one<_methodRefExpr, _expr> SCOPE = new _feature._one<>(_methodRefExpr.class, _expr.class,
            _feature._id.SCOPE,
            a -> a.getScope(),
            (_methodRefExpr a, _expr _e) -> a.setScope(_e), PARSER);

    public static _feature._one<_methodRefExpr, _typeArgs> TYPE_ARGS = new _feature._one<>(_methodRefExpr.class, _typeArgs.class,
            _feature._id.TYPE_ARGS,
            a -> a.getTypeArgs(),
            (_methodRefExpr a, _typeArgs _ta) -> a.setTypeArgs(_ta), PARSER);

    public static _feature._one<_methodRefExpr, String> IDENTIFIER = new _feature._one<>(_methodRefExpr.class, String.class,
            _feature._id.IDENTIFIER,
            a -> a.getIdentifier(),
            (_methodRefExpr a, String s) -> a.setIdentifier(s), PARSER);

    public static _feature._features<_methodRefExpr> FEATURES = _feature._features.of(_methodRefExpr.class,  PARSER, SCOPE, TYPE_ARGS, IDENTIFIER );

    public MethodReferenceExpr node;

    public _feature._features<_methodRefExpr> features(){
        return FEATURES;
    }

    public _methodRefExpr(MethodReferenceExpr node){
        this.node = node;
    }

    @Override
    public _methodRefExpr copy() {
        return new _methodRefExpr(this.node.clone());
    }

    public MethodReferenceExpr node(){
        return node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _methodRefExpr replace(MethodReferenceExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean isIdentifier( String id){
        return Objects.equals( this.node.getIdentifier(), id);
    }

    public boolean isIdentifier( Predicate<String> matchFn){
        return matchFn.test( this.node.getIdentifier() );
    }

    public _methodRefExpr setIdentifier(String id){
        this.node.setIdentifier(id);
        return this;
    }

    public String getIdentifier(){
        return this.node.getIdentifier();
    }

    public boolean equals(Object other){
        if( other instanceof _methodRefExpr){
            return ((_methodRefExpr)other).node.equals( this.node);
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
