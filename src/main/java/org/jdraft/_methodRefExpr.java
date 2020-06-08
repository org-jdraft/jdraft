package org.jdraft;

import com.github.javaparser.ast.expr.MethodReferenceExpr;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public final class _methodRefExpr implements _expr<MethodReferenceExpr, _methodRefExpr>,
        _java._node<MethodReferenceExpr, _methodRefExpr>,
        _java._withScope<MethodReferenceExpr, _methodRefExpr>,
        _typeArgs._withTypeArgs<MethodReferenceExpr, _methodRefExpr> {

    public static final Function<String, _methodRefExpr> PARSER = s-> _methodRefExpr.of(s);

    public static _methodRefExpr of(){
        return new _methodRefExpr( new MethodReferenceExpr());
    }

    public static _methodRefExpr of(MethodReferenceExpr mre){
        return new _methodRefExpr(mre);
    }

    public static _methodRefExpr of(String...code){
        return new _methodRefExpr(Expr.methodReferenceExpr( code));
    }

    public static _feature._one<_methodRefExpr, _expr> SCOPE = new _feature._one<>(_methodRefExpr.class, _expr.class,
            _feature._id.SCOPE,
            a -> a.getScope(),
            (_methodRefExpr a, _expr _e) -> a.setScope(_e), PARSER);

    public static _feature._one<_methodRefExpr, _typeArgs> TYPE_ARGS = new _feature._one<>(_methodRefExpr.class, _typeArgs.class,
            _feature._id.TYPE_ARGS,
            a -> a.getTypeArgs(),
            (_methodRefExpr a, _typeArgs _ta) -> a.setTypeArgs(_ta), PARSER);

    public static _feature._one<_methodRefExpr, String> IDENTIFIER = new _feature._one<>(_methodRefExpr.class, String.class,
            _feature._id.IDENTIFIER,
            a -> a.getIdentifier(),
            (_methodRefExpr a, String s) -> a.setIdentifier(s), PARSER);

    public static _feature._features<_methodRefExpr> FEATURES = _feature._features.of(_methodRefExpr.class,  PARSER, SCOPE, TYPE_ARGS, IDENTIFIER );

    public MethodReferenceExpr mre;

    public _feature._features<_methodRefExpr> features(){
        return FEATURES;
    }

    public _methodRefExpr(MethodReferenceExpr mre){
        this.mre = mre;
    }

    @Override
    public _methodRefExpr copy() {
        return new _methodRefExpr(this.mre.clone());
    }

    public MethodReferenceExpr ast(){
        return mre;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        comps.put(_java.Feature.SCOPE_EXPR, mre.getScope());
        if( mre.getTypeArguments().isPresent()) {
            comps.put(_java.Feature.TYPE_ARGS, mre.getTypeArguments().get());
        }
        comps.put(_java.Feature.IDENTIFIER, mre.getId());
        return comps;
    }
     */

    public boolean isIdentifier( String id){
        return Objects.equals( this.mre.getIdentifier(), id);
    }

    public boolean isIdentifier( Predicate<String> matchFn){
        return matchFn.test( this.mre.getIdentifier() );
    }

    public _methodRefExpr setIdentifier(String id){
        this.mre.setIdentifier(id);
        return this;
    }

    public String getIdentifier(){
        return this.mre.getIdentifier();
    }

    public boolean equals(Object other){
        if( other instanceof _methodRefExpr){
            return ((_methodRefExpr)other).mre.equals( this.mre);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.mre.hashCode();
    }
    
    public String toString(){
        return this.mre.toString();
    }
}
