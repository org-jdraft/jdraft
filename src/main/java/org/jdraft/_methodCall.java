package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.*;
import java.util.function.*;

public class _methodCall implements _expression<MethodCallExpr, _methodCall>,
        _java._withName<_methodCall>,
        _java._multiPart<MethodCallExpr, _methodCall>,
        _java._withScope<MethodCallExpr, _methodCall>,
        _java._withArguments<MethodCallExpr, _methodCall>,
        _java._withTypeArguments<MethodCallExpr, _methodCall> {

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

    public _methodCall setName(String methodName){
        this.mce.setName(methodName);
        return this;
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.methodCallEx(stringRep));
        } catch(Exception e){ }
        return false;
    }
     */

    /*
    @Override
    public boolean is(MethodCallExpr astNode) {
        return this.ast( ).equals(astNode);
    }
     */

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

    /*
    public boolean hasScope(){
        return this.mce.getScope().isPresent();
    }

    public boolean isScope(String...expr){
        if( this.mce.getScope().isPresent()){
            return Objects.equals( mce.getScope().get(), Ex.of(expr));
        }
        return false;
    }

    public boolean isScope(Expression e){
        if( this.mce.getScope().isPresent()){
            return Objects.equals( mce.getScope().get(), e);
        }
        return e == null;
    }

    public boolean isScope(_expression _e){
        if( this.mce.getScope().isPresent()){
            return Objects.equals( mce.getScope().get(), _e.ast());
        }
        return _e == null;
    }

    public _methodCall removeScope(){
        this.mce.removeScope();
        return this;
    }

    public _methodCall setScope( _expression _e){
        return setScope(_e.ast());
    }

    public _methodCall setScope( Expression e){
        this.mce.setScope(e);
        return this;
    }

    public _methodCall setScope(String... scope){
        return setScope( Ex.of(scope));
    }
    */
    /*
    public _expression getScope(){
        if( mce.getScope().isPresent()){
            return _expression.of(this.mce.getScope().get());
        }
        return null;
    }
     */

    public String getName(){
        return this.mce.getNameAsString();
    }

    /*
    public List<_expression> getArguments(){
        List<_expression> args = new ArrayList<>();
        this.mce.getArguments().forEach(a -> args.add(_expression.of(a)));
        return args;
    }

     */

    /*
    public _methodCall setTypeArguments( Type...ts){
        mce.setTypeArguments(ts);
        return this;
    }

    public _methodCall setTypeArguments( _typeRef...tr){
        NodeList<Type> tas = new NodeList<>();
        Arrays.stream(tr).forEach( t -> tas.add(t.ast()));
        mce.setTypeArguments(tas);
        return this;
    }
    */
    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return

    public List<_typeRef> listTypeArguments(){
        if( mce.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            mce.getTypeArguments().get().forEach(t -> tas.add(_typeRef.of(t)));
            return tas;
        }
        return new ArrayList<>();
    }
    */

    /**
     *
     * @param index
     * @return

    public _typeRef getTypeArgument( int index ){
        if( this.mce.getTypeArguments().isPresent()){
            return _typeRef.of( this.mce.getTypeArguments().get().get(index) );
        }
        throw new _jdraftException("No type arguments");
    }
    */

    /**
     * Returns a list of Type arguments based on the predicate
     * if there are any or an empty list if there are none
     * @return

    public List<_typeRef> listTypeArguments(Predicate<_typeRef> matchFn){
        if( mce.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            mce.getTypeArguments().get().forEach(t -> tas.add(_typeRef.of(t)));
            return tas.stream().filter(matchFn).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    */

    /*
    public _methodCall forTypeArguments(Consumer<_typeRef> typeArgFn){
        if( this.mce.getTypeArguments().isPresent()){
            this.mce.getTypeArguments().get().stream().map( a-> _typeRef.of(a)).forEach(e-> typeArgFn.accept(e) );
        }
        return this;
    }
    */

    /*
    public _methodCall forTypeArguments(Predicate<_typeRef> matchFn, Consumer<_typeRef> typeArgFn){
        if( this.mce.getTypeArguments().isPresent()){
            this.mce.getTypeArguments().get().stream().map( a-> _typeRef.of(a)).filter(matchFn).forEach(e-> typeArgFn.accept(e) );
        }
        return this;
    }
    */
    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return

    public List<_typeRef> getTypeArguments(){
        if( mce.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            mce.getTypeArguments().get().forEach(t -> tas.add(_typeRef.of(t)));
            return tas;
        }
        return new ArrayList<>();
    }
    */

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
