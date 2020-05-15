package org.jdraft;

import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.SimpleName;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class _fieldAccessExpr implements _expr<FieldAccessExpr, _fieldAccessExpr>,
        _java._multiPart<FieldAccessExpr, _fieldAccessExpr>,
        _java._withName<_fieldAccessExpr>,
        _typeArguments._withTypeArguments<FieldAccessExpr, _fieldAccessExpr>,
        _java._withScope<FieldAccessExpr, _fieldAccessExpr> {

    public static _fieldAccessExpr of(){
        return new _fieldAccessExpr(new FieldAccessExpr());
    }
    public static _fieldAccessExpr of(FieldAccessExpr fae){
        return new _fieldAccessExpr(fae);
    }
    public static _fieldAccessExpr of(String...code){
        return new _fieldAccessExpr(Exprs.fieldAccessEx( code));
    }


    public static <A extends Object> _fieldAccessExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _fieldAccessExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _fieldAccessExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _fieldAccessExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _fieldAccessExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _fieldAccessExpr of(Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _fieldAccessExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _fieldAccessExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _fieldAccessExpr from(LambdaExpr le){
        Optional<FieldAccessExpr> ows = le.getBody().findFirst(FieldAccessExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No field access expression found in lambda");
    }

    public FieldAccessExpr fe;

    public _fieldAccessExpr(FieldAccessExpr fe){
        this.fe = fe;
    }

    @Override
    public _fieldAccessExpr copy() {
        return new _fieldAccessExpr(this.fe.clone());
    }

    public FieldAccessExpr ast(){
        return fe;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.SCOPE, fe.getScope());
        if( fe.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, fe.getTypeArguments().get());
        }
        comps.put(_java.Component.NAME, fe.getNameAsString());
        return comps;
    }

    public SimpleName getNameNode() { return this.fe.getName(); }

    public _fieldAccessExpr setName(String name){
        this.fe.setName(name);
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
        return fe.getNameAsString();
    }

    public boolean equals(Object other){
        if( other instanceof _fieldAccessExpr){
            return ((_fieldAccessExpr)other).fe.equals( this.fe);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.fe.hashCode();
    }
    
    public String toString(){
        return this.fe.toString();
    }
}
