package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The initialization of an array. In the following sample, the outer { } is an ArrayInitializerExpr.
 * It has two expressions inside: two ArrayInitializerExprs.
 * These have two expressions each, one has 1 and 1, the other two and two.
 * <br/><code>new int[][]{{1, 1}, {2, 2}};</code>
 */
public final class _arrayInitializeExpr implements _expr<ArrayInitializerExpr, _arrayInitializeExpr>,
        _java._list<Expression, _expr, _arrayInitializeExpr> {

    public static _arrayInitializeExpr of( ){
        return new _arrayInitializeExpr(new ArrayInitializerExpr());
    }
    public static _arrayInitializeExpr of(ArrayInitializerExpr ai){
        return new _arrayInitializeExpr(ai);
    }

    public static <A extends Object> _arrayInitializeExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _arrayInitializeExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _arrayInitializeExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayInitializeExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayInitializeExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _arrayInitializeExpr of(Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayInitializeExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayInitializeExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _arrayInitializeExpr from(LambdaExpr le){
        Optional<ArrayInitializerExpr> ows = le.getBody().findFirst(ArrayInitializerExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No ArrayCreationExpr found in lambda");
    }

    public static _arrayInitializeExpr of(String...code){
        return new _arrayInitializeExpr(Exprs.arrayInitializerEx( code));
    }

    public static _arrayInitializeExpr ofStrings(String[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(new StringLiteralExpr(i)));
        return of(aie);
    }

    public static _arrayInitializeExpr of(int[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(new IntegerLiteralExpr(i)));
        return of(aie);
    }

    public static _arrayInitializeExpr of(boolean[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new BooleanLiteralExpr(arr[i]));
        }
        return of(aie);
    }

    public static _arrayInitializeExpr of(float[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new DoubleLiteralExpr( arr[i] +"F" ));
        }
        return of(aie);
    }

    public static _arrayInitializeExpr of(double[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new DoubleLiteralExpr( arr[i] +"D" ));
        }
        return of(aie);
    }

    public static _arrayInitializeExpr of(long[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new LongLiteralExpr( arr[i] ));
        }
        return of(aie);
    }

    public static _arrayInitializeExpr of(char[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new CharLiteralExpr( arr[i] ));
        }
        return of(aie);
    }

    public ArrayInitializerExpr aie;

    public _arrayInitializeExpr(ArrayInitializerExpr aie){
        this.aie = aie;
    }

    @Override
    public _arrayInitializeExpr copy() {
        return new _arrayInitializeExpr(this.aie.clone());
    }

    @Override
    public NodeList<Expression> listAstElements() {
        return this.aie.getValues();
    }

    public _arrayInitializeExpr set(int index, _expr _e){
        aie.getValues().set(index, _e.ast());
        return this;
    }

    public _arrayInitializeExpr set(int index, Expression e){
        aie.getValues().set(index, e);
        return this;
    }

    public ArrayInitializerExpr ast(){
        return aie;
    }

    @Override
    public List<_expr> list(){
        List<_expr> vs = new ArrayList<>();
        this.aie.getValues().forEach(v-> vs.add( _expr.of(v)));
        return vs;
    }

    public boolean equals(Object other){
        if( other instanceof _arrayInitializeExpr){
            return ((_arrayInitializeExpr)other).aie.equals( this.aie);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.aie.hashCode();
    }

    public String toString(){
        return this.aie.toString();
    }
}
