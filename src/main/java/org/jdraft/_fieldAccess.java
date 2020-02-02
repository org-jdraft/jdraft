package org.jdraft;

import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.ForEachStmt;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _fieldAccess implements _expression<FieldAccessExpr, _fieldAccess> {

    public static _fieldAccess of(){
        return new _fieldAccess(new FieldAccessExpr());
    }
    public static _fieldAccess of(FieldAccessExpr fae){
        return new _fieldAccess(fae);
    }
    public static _fieldAccess of( String...code){
        return new _fieldAccess(Ex.fieldAccessEx( code));
    }


    public static <A extends Object> _fieldAccess of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _fieldAccess of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _fieldAccess of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _fieldAccess of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _fieldAccess of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _fieldAccess of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _fieldAccess of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _fieldAccess of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _fieldAccess from( LambdaExpr le){
        Optional<FieldAccessExpr> ows = le.getBody().findFirst(FieldAccessExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No field access expression found in lambda");
    }

    public FieldAccessExpr ile;

    public _fieldAccess(FieldAccessExpr ile){
        this.ile = ile;
    }

    @Override
    public _fieldAccess copy() {
        return new _fieldAccess(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.fieldAccessEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(FieldAccessExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public FieldAccessExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.SCOPE, ile.getScope());
        if( ile.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, ile.getTypeArguments().get());
        }
        comps.put(_java.Component.NAME, ile.getNameAsString());
        return comps;
    }

    public _expression getScope(){
        return _expression.of(this.ile.getScope());
    }

    /**
     * Returns the List of Type Arguments or an empty list if there are no type arguments
     * @return
     * 1) null if there are NO typeArguments
     * 2) an empty list if the &lt;> (Diamond operator) is being used
     * 3) a populated list if there are type arguments
     */
    public List<_typeRef> getTypeArguments(){

        if( this.ile.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            this.ile.getTypeArguments().get().forEach( ta-> tas.add(_typeRef.of(ta)));
        }
        return null;
    }

    public String getName(){
        return ile.getNameAsString();
    }

    public boolean equals(Object other){
        if( other instanceof _fieldAccess){
            return ((_fieldAccess)other).ile.equals( this.ile );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ile.hashCode();
    }
    
    public String toString(){
        return this.ile.toString();
    }
}
