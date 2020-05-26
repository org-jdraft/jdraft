package org.jdraft;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
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
public final class _castExpr implements _expr<CastExpr, _castExpr>, _java._node<CastExpr, _castExpr>,
        _typeRef._withTypeRef<CastExpr, _castExpr>, _java._withExpression<CastExpr, _castExpr> {

    public static final Function<String, _castExpr> PARSER = s-> _castExpr.of(s);

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

    public static _feature._one<_castExpr, _typeRef> TYPE = new _feature._one<>(_castExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getTypeRef(),
            (_castExpr a, _typeRef o) -> a.setType(o), PARSER);

    public static _feature._one<_castExpr, _expr> EXPRESSION = new _feature._one<>(_castExpr.class, _expr.class,
            _feature._id.EXPRESSION,
            a -> a.getExpression(),
            (_castExpr a, _expr _e) -> a.setExpression(_e), PARSER);

    public static _feature._meta<_castExpr> META = _feature._meta.of(_castExpr.class, TYPE, EXPRESSION);

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

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        comps.put(_java.Feature.TYPE, ce.getType());
        comps.put(_java.Feature.EXPRESSION, ce.getExpression());
        return comps;
    }

    public boolean equals(Object other){
        if( other instanceof _castExpr){
            return ((_castExpr)other).ce.equals( this.ce);
        }
        return false;
    }

    public _castExpr setType( String typeRef ){
        return setType(_typeRef.of(typeRef));
    }

    public _castExpr setType( _typeRef _t ){
        this.ce.setType(_t.ast());
        return this;
    }

    public _castExpr setType( Type t ){
        this.ce.setType(t);
        return this;
    }

    public boolean isType(String type ){
        return isType(_typeRef.of(type));
    }

    public boolean isType( Type t){
        return Types.equal(t, this.ce.getType());
    }

    public boolean isType( _typeRef _t){
        return Types.equal(_t.ast(), this.ce.getType());
    }

    public _castExpr setExpression(String ex){
        return setExpression(_expr.of(ex));
    }

    public _castExpr setExpression(Expression e){
        this.ce.setExpression(e);
        return this;
    }

    public _castExpr setExpression(_expr _e){
        this.ce.setExpression(_e.ast());
        return this;
    }

    public boolean isExpression(String expression ){
        return isExpression(_expr.of(expression));
    }

    public boolean isExpression( Expression e){
        return Exprs.equal(e, this.ce.getExpression());
    }

    public boolean isExpression( _expr _e){
        return Exprs.equal(_e.ast(), this.ce.getExpression());
    }

    public int hashCode(){
        return 31 * this.ce.hashCode();
    }
    
    public String toString(){
        return this.ce.toString();
    }
}
