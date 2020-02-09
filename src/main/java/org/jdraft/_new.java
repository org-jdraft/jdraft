package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _new implements _expression<ObjectCreationExpr, _new>, _java._compound<ObjectCreationExpr, _new> {

    public static _new of(){
        return new _new( new ObjectCreationExpr() );
    }
    public static _new of(ObjectCreationExpr oce){
        return new _new( oce );
    }
    public static _new of( String...code){
        return new _new(Ex.objectCreationEx(code));
    }


    public static <A extends Object> _new of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _new of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _new of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _new of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _new of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _new of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _new of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _new of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _new from( LambdaExpr le){
        Optional<ObjectCreationExpr> ows = le.getBody().findFirst(ObjectCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No new expression found in lambda");
    }

    public ObjectCreationExpr oce;

    public _new(ObjectCreationExpr oce){
        this.oce = oce;
    }

    @Override
    public _new copy() {
        return new _new(this.oce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.objectCreationEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ObjectCreationExpr ast(){
        return oce;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if( oce.getAnonymousClassBody().isPresent()){
            comps.put(_java.Component.ANONYMOUS_CLASS_BODY, oce.getAnonymousClassBody().get());
        }
        comps.put(_java.Component.ARGUMENTS, oce.getArguments());

        if( oce.getScope().isPresent() ) {
            comps.put(_java.Component.SCOPE, oce.getScope().get());
        }
        comps.put(_java.Component.TYPE, oce.getType());

        if( oce.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, oce.getTypeArguments().get());
        }
        return comps;
    }

    /**
     * Returns a list of declared entities defined in the anonymous body
     * (or an empty
     * @return
     */
    public List<_java._declared> listAnonymousBodyDeclarations(){
        List<_java._declared> ds =  new ArrayList<>();
        if( this.oce.getAnonymousClassBody().isPresent()){
            oce.getAnonymousClassBody().get().forEach(b -> ds.add((_java._declared)_java.of(b)));
        }
        return ds;
    }

    public _expression getScope(){
        if( oce.getScope().isPresent()){
            return _expression.of(this.oce.getScope().get());
        }
        return null;
    }

    public List<_expression> listArguments(){
        List<_expression> args = new ArrayList<>();
        this.oce.getArguments().forEach(a -> args.add(_expression.of(a)));
        return args;
    }

    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public List<_typeRef> listTypeArguments(){
        if( oce.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            oce.getTypeArguments().get().forEach(t -> tas.add(_typeRef.of(t)));
            return tas;
        }
        return new ArrayList<>();
    }

    public boolean equals(Object other){
        if( other instanceof _new){
            return ((_new)other).oce.equals( this.oce);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.oce.hashCode();
    }
    
    public String toString(){
        return this.oce.toString();
    }
}
