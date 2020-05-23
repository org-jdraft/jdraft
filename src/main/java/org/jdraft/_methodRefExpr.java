package org.jdraft;

import com.github.javaparser.ast.expr.MethodReferenceExpr;

import java.util.*;
import java.util.function.Predicate;

public final class _methodRefExpr implements _expr<MethodReferenceExpr, _methodRefExpr>,
        _java._node<MethodReferenceExpr, _methodRefExpr>,
        _java._withScope<MethodReferenceExpr, _methodRefExpr>,
        _typeArgs._withTypeArgs<MethodReferenceExpr, _methodRefExpr> {

    public static _methodRefExpr of(){
        return new _methodRefExpr( new MethodReferenceExpr());
    }

    public static _methodRefExpr of(MethodReferenceExpr mre){
        return new _methodRefExpr(mre);
    }

    public static _methodRefExpr of(String...code){
        return new _methodRefExpr(Exprs.methodReferenceExpr( code));
    }

    public MethodReferenceExpr mre;

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

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        comps.put(_java.Feature.SCOPE_EXPR, mre.getScope());
        if( mre.getTypeArguments().isPresent()) {
            comps.put(_java.Feature.TYPE_ARGS, mre.getTypeArguments().get());
        }
        comps.put(_java.Feature.IDENTIFIER, mre.getId());
        return comps;
    }

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
