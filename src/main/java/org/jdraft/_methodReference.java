package org.jdraft;

import com.github.javaparser.ast.expr.MethodReferenceExpr;

import java.util.*;
import java.util.function.Predicate;

public class _methodReference implements _expression<MethodReferenceExpr, _methodReference>,
        _java._multiPart<MethodReferenceExpr, _methodReference>,
        _java._withScope<MethodReferenceExpr, _methodReference>,
        _typeArguments._withTypeArguments<MethodReferenceExpr, _methodReference> {

    public static _methodReference of(){
        return new _methodReference( new MethodReferenceExpr());
    }

    public static _methodReference of(MethodReferenceExpr mre){
        return new _methodReference(mre);
    }

    public static _methodReference of( String...code){
        return new _methodReference(Ex.methodReferenceEx( code));
    }

    public MethodReferenceExpr mre;

    public _methodReference(MethodReferenceExpr mre){
        this.mre = mre;
    }

    @Override
    public _methodReference copy() {
        return new _methodReference(this.mre.clone());
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

    public _methodReference setIdentifier( String id){
        this.mre.setIdentifier(id);
        return this;
    }

    public String getIdentifier(){
        return this.mre.getIdentifier();
    }

    public boolean equals(Object other){
        if( other instanceof _methodReference){
            return ((_methodReference)other).mre.equals( this.mre);
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
