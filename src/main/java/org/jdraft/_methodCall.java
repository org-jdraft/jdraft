package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _methodCall implements _expression<MethodCallExpr, _methodCall>,
        _java._compoundNode<MethodCallExpr, _methodCall>,
        _java._nodeList<Expression, _expression, _methodCall> {

    public static _methodCall of(){
        return new _methodCall( new MethodCallExpr( ));
    }
    public static _methodCall of(MethodCallExpr mce){
        return new _methodCall(mce);
    }
    public static _methodCall of( String...code){
        return new _methodCall(Ex.methodCallEx( code));
    }

    public static <A extends Object> _methodCall of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _methodCall of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _methodCall of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _methodCall of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _methodCall of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _methodCall of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _methodCall of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _methodCall of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _methodCall from( LambdaExpr le){
        Optional<MethodCallExpr> ows = le.getBody().findFirst(MethodCallExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No method call expression found in lambda");
    }

    public MethodCallExpr mce;

    public _methodCall(MethodCallExpr mce){
        this.mce = mce;
    }

    @Override
    public _methodCall copy() {
        return new _methodCall(this.mce.clone());
    }

    @Override
    public List<_expression> list() {
        List<_expression>_exs = new ArrayList<>();
        listAstElements().forEach(e -> _exs.add(_expression.of(e)));
        return _exs;
    }

    @Override
    public NodeList<Expression> listAstElements() {
        return this.mce.getArguments();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.methodCallEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(MethodCallExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public MethodCallExpr ast(){
        return mce;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if( mce.getScope().isPresent() ) {
            comps.put(_java.Component.SCOPE, mce.getScope().get());
        }
        comps.put(_java.Component.NAME, mce.getNameAsString());
        if( mce.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, mce.getTypeArguments().get());
        }
        comps.put(_java.Component.ARGUMENTS, mce.getArguments());
        return comps;
    }

    public _expression getScope(){
        if( mce.getScope().isPresent()){
            return _expression.of(this.mce.getScope().get());
        }
        return null;
    }

    public String getName(){
        return this.mce.getNameAsString();
    }

    public List<_expression> getArguments(){
        List<_expression> args = new ArrayList<>();
        this.mce.getArguments().forEach(a -> args.add(_expression.of(a)));
        return args;
    }

    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public List<_typeRef> getTypeArguments(){
        if( mce.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            mce.getTypeArguments().get().forEach(t -> tas.add(_typeRef.of(t)));
            return tas;
        }
        return new ArrayList<>();
    }

    public boolean equals(Object other){
        if( other instanceof _methodCall){
            return ((_methodCall)other).mce.equals( this.mce);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.mce.hashCode();
    }
    
    public String toString(){
        return this.mce.toString();
    }
}
