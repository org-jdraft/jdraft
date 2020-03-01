package org.jdraft;

import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * <PRE><CODE>
 * (String)getCharSeq() //"String" as type; "getCharSeq();" is expression
 * (List<String>)a      //List<String> as type; "a" as expression
 * </CODE></PRE>
 *
 */
public class _cast implements _expression<CastExpr, _cast>, _java._multiPart<CastExpr, _cast>,
        _java._withTypeRef<CastExpr, _cast>, _java._withExpression<CastExpr, _cast> {

    public static _cast of(){
        return new _cast( new CastExpr());
    }
    public static _cast of( CastExpr ce){
        return new _cast(ce);
    }
    public static _cast of( String...code){
        return new _cast(Ex.castEx( code));
    }

    public static <A extends Object> _cast of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _cast of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _cast of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _cast of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _cast of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _cast of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _cast of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _cast of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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
            return is( Ex.castEx(stringRep));
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

    /*
    public boolean isType( Class clazz){
        return isType( _typeRef.of(clazz) );
    }
    public boolean isType(String type){
        return isType(Ast.typeRef(type));
    }

    public boolean isType( _typeRef _t){
        return isType(_t.ast());
    }

    public boolean isType( Type t){
        return Ast.typesEqual(this.ce.getType(), t);
    }

    public _typeRef getType(){
        return _typeRef.of(this.ce.getType());
    }

    public boolean isExpression(String exp){
        return isExpression(Ex.of(exp));
    }

    public boolean isExpression( _expression _e){
        return isExpression(_e.ast());
    }

    public boolean isExpression( Expression e){
        return Objects.equals( this.ce.getExpression(), e);
    }

    public _expression getExpression(){
        return _expression.of(this.ce.getExpression());
    }

    public _cast setType(String type){
        this.ce.setType(type);
        return this;
    }
    public _cast setType(_typeRef type){
        this.ce.setType(type.ast());
        return this;
    }
    public _cast setType(Type type){
        this.ce.setType(type);
        return this;
    }

    public _cast setType(Class clazz){
        this.ce.setType(clazz);
        return this;
    }
    */
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
