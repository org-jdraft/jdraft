package org.jdraft;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Name;

import java.util.Objects;
import java.util.function.Function;

public final class _package implements _tree._node<PackageDeclaration, _package> {

    public static final Function<String, _package> PARSER = s-> _package.of(s);

    public static _package of(){
        return of( new PackageDeclaration());
    }

    public static _package of( String packageName ){
        return of( Ast.packageDeclaration(packageName));
    }

    public static _package of( PackageDeclaration astPackage){
        return new _package(astPackage);
    }

    public PackageDeclaration node;

    public static _feature._one<_package, Name> NAME = new _feature._one<>(_package.class, Name.class,
            _feature._id.NAME,
            a -> a.getNameNode(),
            (_package p, Name s) -> p.setName(s), PARSER);

    public static _feature._features<_package> FEATURES = _feature._features.of(_package.class, PARSER, NAME);

    public _package( PackageDeclaration astPd){
        this.node = astPd;
    }

    public _feature._features<_package> features(){
        return FEATURES;
    }

    @Override
    public _package copy() {
        return of( node.clone() );
    }

    @Override
    public PackageDeclaration node() {
        return this.node;
    }

    public Name getNameNode(){
        return this.node.getName();
    }

    public _package setName(Name n){
        this.node.setName(n);
        return this;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _package replace(PackageDeclaration replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public boolean equals(Object o){
        if( o instanceof _package ){
            _package _p = (_package)o;
            return Objects.equals( this.node, _p.node);
        }
        return false;
    }

    public String toString(){
        return this.node.toString();
    }
}
