package org.jdraft;

import com.github.javaparser.ast.expr.MethodReferenceExpr;

import java.util.*;
import java.util.function.Predicate;

public final class _methodRefExpr implements _expr<MethodReferenceExpr, _methodRefExpr>,
        _java._multiPart<MethodReferenceExpr, _methodRefExpr>,
        _java._withScope<MethodReferenceExpr, _methodRefExpr>,
        _typeArgs._withTypeArguments<MethodReferenceExpr, _methodRefExpr> {

    public static _methodRefExpr of(){
        return new _methodRefExpr( new MethodReferenceExpr());
    }

    public static _methodRefExpr of(MethodReferenceExpr mre){
        return new _methodRefExpr(mre);
    }

    public static _methodRefExpr of(String...code){
        return new _methodRefExpr(Exprs.methodReferenceEx( code));
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

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.SCOPE, mre.getScope());
        if( mre.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, mre.getTypeArguments().get());
        }
        comps.put(_java.Component.IDENTIFIER, mre.getId());
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
