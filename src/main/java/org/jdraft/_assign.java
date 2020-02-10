package org.jdraft;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _assign implements _expression<AssignExpr, _assign>, _java._compound<AssignExpr, _assign> {

    public static _assign of(){
        return new _assign( new AssignExpr());
    }
    public static _assign of( AssignExpr ae){
        return new _assign(ae);
    }

    public static _assign of( String...code){
        return new _assign(Ex.assignEx( code));
    }


    public static <A extends Object> _assign of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _assign of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _assign of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assign of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assign of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _assign of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _assign of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _assign of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _assign from( LambdaExpr le){
        Optional<AssignExpr> ows = le.getBody().findFirst(AssignExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No assignment expression found in lambda");
    }

    public AssignExpr ae;

    public _assign(AssignExpr ae){
        this.ae = ae;
    }

    @Override
    public _assign copy() {
        return new _assign(this.ae.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.assignEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(AssignExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public AssignExpr ast(){
        return ae;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TARGET, ae.getTarget());
        comps.put(_java.Component.VALUE, ae.getValue());
        return comps;
    }

    public boolean isValue(String str){
        return Objects.equals( this.ae.getValue(), Ex.of(str));
    }

    public boolean isTarget(String str){
        return Objects.equals( this.ae.getTarget(), Ex.of(str));
    }

    public boolean isValue(Expression e){
        return Objects.equals( this.ae.getValue(), e);
    }

    public boolean isTarget(Expression e){
        return Objects.equals( this.ae.getTarget(), e);
    }

    public _assign setTarget(String...target){
        this.ae.setTarget(Ex.of(target));
        return this;
    }

    public _assign setTarget(_expression _e){
        this.ae.setTarget(_e.ast());
        return this;
    }

    public _assign setTarget(Expression e){
        this.ae.setTarget(e);
        return this;
    }

    public _assign setValue(String...value){
        this.ae.setValue(Ex.of(value));
        return this;
    }

    public _assign setValue(_expression _e){
        this.ae.setValue(_e.ast());
        return this;
    }

    public _assign setValue(Expression e){
        this.ae.setValue(e);
        return this;
    }

    public _expression getValue(){
        return _expression.of(this.ae.getValue());
    }

    public _expression getTarget(){
        return _expression.of(this.ae.getTarget());
    }

    public boolean equals(Object other){
        if( other instanceof _assign){
            return ((_assign)other).ae.equals( this.ae);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ae.hashCode();
    }

    public String toString(){
        return this.ae.toString();
    }
}
