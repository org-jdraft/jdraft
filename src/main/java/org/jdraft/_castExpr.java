package org.jdraft;

import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.LambdaExpr;

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
public final class _castExpr implements _expr<CastExpr, _castExpr>, _java._multiPart<CastExpr, _castExpr>,
        _typeRef._withTypeRef<CastExpr, _castExpr>, _java._withExpression<CastExpr, _castExpr> {

    public static _castExpr of(){
        return new _castExpr( new CastExpr());
    }
    public static _castExpr of(CastExpr ce){
        return new _castExpr(ce);
    }
    public static _castExpr of(String...code){
        return new _castExpr(Exprs.castExpr( code));
    }

    public static <A extends Object> _castExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _castExpr of(Supplier<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _castExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _castExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _castExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _castExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _castExpr of(Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _castExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _castExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _castExpr from(LambdaExpr le){
        Optional<CastExpr> ows = le.getBody().findFirst(CastExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No cast found in lambda");
    }

    public CastExpr ce;

    public _castExpr(CastExpr ce){
        this.ce = ce;
    }

    @Override
    public _castExpr copy() {
        return new _castExpr(this.ce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.castExpr(stringRep));
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
        if( other instanceof _castExpr){
            return ((_castExpr)other).ce.equals( this.ce);
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
