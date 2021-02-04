package org.jdraft;

import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Model of a field access expr
 * i.e. System.LINE_SEPARATOR
 *      SCOPE: System
 *      SEPARATOR: .
 *      NAME: LINE_SEPARATOR
 */
public final class _fieldAccessExpr implements _expr<FieldAccessExpr, _fieldAccessExpr>,
        _tree._node<FieldAccessExpr, _fieldAccessExpr>,
        _java._withName<_fieldAccessExpr>,
        _typeArgs._withTypeArgs<FieldAccessExpr, _fieldAccessExpr>,
        _java._withScope<FieldAccessExpr, _fieldAccessExpr> {


    public static final Function<String, _fieldAccessExpr> PARSER = s-> _fieldAccessExpr.of(s);

    public static _fieldAccessExpr of(){
        return new _fieldAccessExpr(new FieldAccessExpr());
    }

    public static _fieldAccessExpr of(FieldAccessExpr fae){
        return new _fieldAccessExpr(fae);
    }

    public static _fieldAccessExpr of(String...code){
        return new _fieldAccessExpr(Expr.fieldAccessExpr( code));
    }

    public static <A extends Object> _fieldAccessExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _fieldAccessExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _fieldAccessExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _fieldAccessExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _fieldAccessExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _fieldAccessExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _fieldAccessExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _fieldAccessExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _fieldAccessExpr from(LambdaExpr le){
        Optional<FieldAccessExpr> ows = le.getBody().findFirst(FieldAccessExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No field access expression found in lambda");
    }

    public static _feature._one<_fieldAccessExpr, _expr> SCOPE = new _feature._one<>(_fieldAccessExpr.class, _expr.class,
            _feature._id.SCOPE,
            a -> a.getScope(),
            (_fieldAccessExpr a, _expr _e) -> a.setScope(_e), PARSER);

    public static _feature._one<_fieldAccessExpr, String> NAME = new _feature._one<>(_fieldAccessExpr.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_fieldAccessExpr a, String s) -> a.setName(s), PARSER);


    public static _feature._one<_fieldAccessExpr, _typeArgs> TYPE_ARGS = new _feature._one<>(_fieldAccessExpr.class, _typeArgs.class,
            _feature._id.TYPE_ARGS,
            a -> a.getTypeArgs(),
            (_fieldAccessExpr a, _typeArgs _e) -> a.setTypeArgs(_e), PARSER);


    public static _feature._features<_fieldAccessExpr> FEATURES = _feature._features.of(_fieldAccessExpr.class, PARSER, SCOPE, TYPE_ARGS, NAME );

    public FieldAccessExpr node;

    public _fieldAccessExpr(FieldAccessExpr node){
        this.node = node;
    }

    @Override
    public _fieldAccessExpr copy() {
        return new _fieldAccessExpr(this.node.clone());
    }

    public _feature._features<_fieldAccessExpr> features(){
        return FEATURES;
    }

    public FieldAccessExpr node(){
        return node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _fieldAccessExpr replace(FieldAccessExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public SimpleName getNameNode() { return this.node.getName(); }

    public _fieldAccessExpr setName(String name){
        this.node.setName(name);
        return this;
    }

    /**
     * Returns the List of Type Arguments or an empty list if there are no type arguments
     * @return
     * 1) null if there are NO typeArguments
     * 2) an empty list if the &lt;> (Diamond operator) is being used
     * 3) a populated list if there are type arguments

    public List<_typeRef> getTypeArguments(){

        if( this.fe.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            this.fe.getTypeArguments().get().forEach(ta-> tas.add(_typeRef.of(ta)));
        }
        return null;
    }
    */

    public String getName(){
        return node.getNameAsString();
    }

    public boolean equals(Object other){
        if( other instanceof _fieldAccessExpr){
            return ((_fieldAccessExpr)other).node.equals( this.node);
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
