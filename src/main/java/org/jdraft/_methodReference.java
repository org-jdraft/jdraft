package org.jdraft;

import com.github.javaparser.ast.expr.MethodReferenceExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _methodReference implements _expression<MethodReferenceExpr, _methodReference> {

    public static _methodReference of(){
        return new _methodReference( new MethodReferenceExpr());
    }
    public static _methodReference of(MethodReferenceExpr mre){
        return new _methodReference(mre);
    }
    public static _methodReference of( String...code){
        return new _methodReference(Ex.methodReferenceEx( code));
    }

    public MethodReferenceExpr ile;

    public _methodReference(MethodReferenceExpr ile){
        this.ile = ile;
    }

    @Override
    public _methodReference copy() {
        return new _methodReference(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.methodReferenceEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(MethodReferenceExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public MethodReferenceExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.SCOPE, ile.getScope());
        if( ile.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, ile.getTypeArguments().get());
        }
        comps.put(_java.Component.IDENTIFIER, ile.getId());
        return comps;
    }

    public _expression getScope(){
        return _expression.of(this.ile.getScope());
    }

    public String getIdentifier(){
        return this.ile.getId();
    }

    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public List<_typeRef> getTypeArguments(){
        if( ile.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            ile.getTypeArguments().get().forEach(t -> tas.add(_typeRef.of(t)));
            return tas;
        }
        return new ArrayList<>();
    }

    public boolean equals(Object other){
        if( other instanceof _methodReference){
            return ((_methodReference)other).ile.equals( this.ile );
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
