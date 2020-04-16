package org.jdraft;

import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.type.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.*;

/**
 *
 * <PRE><CODE>
 * (String)getCharSeq() //"String" as type; "getCharSeq();" is expression
 * (List<String>)a      //List<String> as type; "a" as expression
 * </CODE></PRE>
 *
 */
public final class _cast implements _expression<CastExpr, _cast>, _java._multiPart<CastExpr, _cast>,
        _typeRef._withTypeRef<CastExpr, _cast>, _java._withExpression<CastExpr, _cast> {

    public static _cast of(){
        return new _cast( new CastExpr());
    }
    public static _cast of( CastExpr ce){
        return new _cast(ce);
    }
    public static _cast of( String...code){
        return new _cast(Expressions.castEx( code));
    }

    public static <A extends Object> _cast of(Expressions.Command c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _cast of(Supplier<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _cast of(Consumer<A> c){
        LambdaExpr le = Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _cast of(BiConsumer<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _cast of( Expressions.TriConsumer<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _cast of( Expressions.QuadConsumer<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _cast of( Function<A,B> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _cast of( BiFunction<A,B,C> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _cast of( Expressions.TriFunction<A,B,C,D> command ){
        return from(Expressions.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _cast from( LambdaExpr le){
        Optional<CastExpr> ows = le.getBody().findFirst(CastExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No cast found in lambda");
    }

    public CastExpr ce;

    public _cast(CastExpr ce){
        this.ce = ce;
    }

    @Override
    public _cast copy() {
        return new _cast(this.ce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expressions.castEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(CastExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public CastExpr ast(){
        return ce;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.TYPE, ce.getType());
        comps.put(_java.Component.EXPRESSION, ce.getExpression());
        return comps;
    }

    public boolean equals(Object other){
        if( other instanceof _cast){
            return ((_cast)other).ce.equals( this.ce);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ce.hashCode();
    }
    
    public String toString(){
        return this.ce.toString();
    }
}
