package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The initialization of an array. In the following sample, the outer { } is an ArrayInitializerExpr.
 * It has two expressions inside: two ArrayInitializerExprs.
 * These have two expressions each, one has 1 and 1, the other two and two.
 * <br/><code>new int[][]{{1, 1}, {2, 2}};</code>
 */
public final class _arrayInitExpr implements _expr<ArrayInitializerExpr, _arrayInitExpr>,
        _java._list<Expression, _expr, _arrayInitExpr> {

    public static final Function<String, _arrayInitExpr> PARSER = s-> _arrayInitExpr.of(s);

    public static _arrayInitExpr of( ){
        return new _arrayInitExpr(new ArrayInitializerExpr());
    }
    public static _arrayInitExpr of(ArrayInitializerExpr ai){
        return new _arrayInitExpr(ai);
    }

    public static <A extends Object> _arrayInitExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _arrayInitExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _arrayInitExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayInitExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayInitExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _arrayInitExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayInitExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayInitExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _arrayInitExpr from(LambdaExpr le){
        Optional<ArrayInitializerExpr> ows = le.getBody().findFirst(ArrayInitializerExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No ArrayCreationExpr found in lambda");
    }

    public static _arrayInitExpr of(String...code){
        return new _arrayInitExpr(Expr.arrayInitializerExpr( code));
    }

    public static _arrayInitExpr ofStrings(String[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(new StringLiteralExpr(i)));
        return of(aie);
    }

    public static _arrayInitExpr of(Class... arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add( _classExpr.of(i).ast() ));
        return of(aie);
    }

    public static _arrayInitExpr of(_expr... arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(i.ast()));
        return of(aie);
    }

    public static _arrayInitExpr of(int... arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(new IntegerLiteralExpr(i)));
        return of(aie);
    }

    public static _arrayInitExpr of(boolean... arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new BooleanLiteralExpr(arr[i]));
        }
        return of(aie);
    }

    public static _arrayInitExpr of(float... arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new DoubleLiteralExpr( arr[i] +"F" ));
        }
        return of(aie);
    }

    public static _arrayInitExpr of(double... arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new DoubleLiteralExpr( arr[i] +"D" ));
        }
        return of(aie);
    }

    public static _arrayInitExpr of(long... arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new LongLiteralExpr( arr[i]+"L" ));
        }
        return of(aie);
    }

    public static _arrayInitExpr of(char... arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new CharLiteralExpr( arr[i] ));
        }
        return of(aie);
    }

    public static _feature._many<_arrayInitExpr, _expr> INITS = new _feature._many<>(_arrayInitExpr.class, _expr.class,
            _feature._id.INITS, _feature._id.INIT,
            a->a.list(),
            (_arrayInitExpr a, List<_expr> es)-> a.set(es), PARSER, s-> _expr.of(s))
            .setOrdered(false);/** order of init exprs doesnt matter semantically { (int i=0; int j=1;) === (int j=1, int i=0) } */

    public static _feature._features<_arrayInitExpr> FEATURES = _feature._features.of(_arrayInitExpr.class, INITS);

    public ArrayInitializerExpr aie;

    public _arrayInitExpr(ArrayInitializerExpr aie){
        this.aie = aie;
    }

    @Override
    public _arrayInitExpr copy() {
        return new _arrayInitExpr(this.aie.clone());
    }

    @Override
    public NodeList<Expression> listAstElements() {
        return this.aie.getValues();
    }

    /**
     * gets the number of values initialized
     * @return the size
     */
    public int getSize(){
        return this.aie.getValues().size();
    }

    public ArrayInitializerExpr ast(){
        return aie;
    }

    /**
     * Adds expressions to the array initializer
     * @param exprs Strings representing expressions to be added to the array initialization
     * @return the modified _arrayInitExpr
     */
    public _arrayInitExpr add(String...exprs){
        Stream.of(exprs).forEach(e -> add( _expr.of(e)));
        return this;
    }

    /**
     *
     * @param index
     * @param exprCode
     * @return
     */
    public boolean isAt(int index, String exprCode){
        try{
            return isAt( index, _expr.of(exprCode));
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public List<_expr> list(){
        List<_expr> vs = new ArrayList<>();
        this.aie.getValues().forEach(v-> vs.add( _expr.of(v)));
        return vs;
    }

    public boolean equals(Object other){
        if( other instanceof _arrayInitExpr){
            return ((_arrayInitExpr)other).aie.equals( this.aie);
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
