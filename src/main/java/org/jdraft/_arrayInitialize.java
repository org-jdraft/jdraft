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
public class _arrayInitialize implements _expression<ArrayInitializerExpr, _arrayInitialize>,
        _java._list<Expression, _expression, _arrayInitialize> {

    public static _arrayInitialize of( ){
        return new _arrayInitialize(new ArrayInitializerExpr());
    }
    public static _arrayInitialize of(ArrayInitializerExpr ai){
        return new _arrayInitialize(ai);
    }

    public static <A extends Object> _arrayInitialize of(Expressions.Command c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _arrayInitialize of(Consumer<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _arrayInitialize of(BiConsumer<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayInitialize of( Expressions.TriConsumer<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayInitialize of( Expressions.QuadConsumer<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _arrayInitialize of( Function<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayInitialize of( BiFunction<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayInitialize of( Expressions.TriFunction<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _arrayInitialize from( LambdaExpr le){
        Optional<ArrayInitializerExpr> ows = le.getBody().findFirst(ArrayInitializerExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No ArrayCreationExpr found in lambda");
    }

    public static _arrayInitialize of( String...code){
        return new _arrayInitialize(Expressions.arrayInitializerEx( code));
    }

    public static _arrayInitialize ofStrings( String[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(new StringLiteralExpr(i)));
        return of(aie);
    }

    public static _arrayInitialize of( int[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(new IntegerLiteralExpr(i)));
        return of(aie);
    }

    public static _arrayInitialize of( boolean[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new BooleanLiteralExpr(arr[i]));
        }
        return of(aie);
    }

    public static _arrayInitialize of( float[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new DoubleLiteralExpr( arr[i] +"F" ));
        }
        return of(aie);
    }

    public static _arrayInitialize of( double[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new DoubleLiteralExpr( arr[i] +"D" ));
        }
        return of(aie);
    }

    public static _arrayInitialize of( long[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new LongLiteralExpr( arr[i] ));
        }
        return of(aie);
    }

    public static _arrayInitialize of( char[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new CharLiteralExpr( arr[i] ));
        }
        return of(aie);
    }

    public ArrayInitializerExpr aie;

    public _arrayInitialize(ArrayInitializerExpr aie){
        this.aie = aie;
    }

    @Override
    public _arrayInitialize copy() {
        return new _arrayInitialize(this.aie.clone());
    }

    @Override
    public NodeList<Expression> listAstElements() {
        return this.aie.getValues();
    }

    public _arrayInitialize set(int index, _expression _e){
        aie.getValues().set(index, _e.ast());
        return this;
    }

    public _arrayInitialize set(int index, Expression e){
        aie.getValues().set(index, e);
        return this;
    }

    public ArrayInitializerExpr ast(){
        return aie;
    }

    @Override
    public List<_expression> list(){
        List<_expression> vs = new ArrayList<>();
        this.aie.getValues().forEach(v-> vs.add( _expression.of(v)));
        return vs;
    }

    public boolean equals(Object other){
        if( other instanceof _arrayInitialize){
            return ((_arrayInitialize)other).aie.equals( this.aie);
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
