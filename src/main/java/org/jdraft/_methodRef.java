package org.jdraft;

import com.github.javaparser.ast.expr.MethodReferenceExpr;

import java.util.*;
import java.util.function.Predicate;

public final class _methodRef implements _expression<MethodReferenceExpr, _methodRef>,
        _java._multiPart<MethodReferenceExpr, _methodRef>,
        _java._withScope<MethodReferenceExpr, _methodRef>,
        _typeArguments._withTypeArguments<MethodReferenceExpr, _methodRef> {

    public static _methodRef of(){
        return new _methodRef( new MethodReferenceExpr());
    }

    public static _methodRef of(MethodReferenceExpr mre){
        return new _methodRef(mre);
    }

    public static _methodRef of(String...code){
        return new _methodRef(Expressions.methodReferenceEx( code));
    }

    public MethodReferenceExpr mre;

    public _methodRef(MethodReferenceExpr mre){
        this.mre = mre;
    }

    @Override
    public _methodRef copy() {
        return new _methodRef(this.mre.clone());
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

    public _methodRef setIdentifier(String id){
        this.mre.setIdentifier(id);
        return this;
    }

    public String getIdentifier(){
        return this.mre.getIdentifier();
    }

    public boolean equals(Object other){
        if( other instanceof _methodRef){
            return ((_methodRef)other).mre.equals( this.mre);
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
