package org.jdraft;

import com.github.javaparser.ast.expr.ObjectCreationExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _new implements _expression<ObjectCreationExpr, _new> {

    public static _new of(){
        return new _new( new ObjectCreationExpr() );
    }
    public static _new of(ObjectCreationExpr oce){
        return new _new( oce );
    }
    public static _new of( String...code){
        return new _new(Ex.objectCreationEx(code));
    }

    public ObjectCreationExpr ile;

    public _new(ObjectCreationExpr ile){
        this.ile = ile;
    }

    @Override
    public _new copy() {
        return new _new(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.objectCreationEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ObjectCreationExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if( ile.getAnonymousClassBody().isPresent()){
            comps.put(_java.Component.ANONYMOUS_CLASS_BODY, ile.getAnonymousClassBody().get());
        }
        comps.put(_java.Component.ARGUMENTS, ile.getArguments());

        if( ile.getScope().isPresent() ) {
            comps.put(_java.Component.SCOPE, ile.getScope().get());
        }
        comps.put(_java.Component.TYPE, ile.getType());

        if( ile.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, ile.getTypeArguments().get());
        }
        return comps;
    }

    /**
     * Returns a list of declared entities defined in the anonymous body
     * (or an empty
     * @return
     */
    public List<_declared> listAnonymousBodyDeclarations(){
        List<_declared> ds =  new ArrayList<>();
        if( this.ile.getAnonymousClassBody().isPresent()){
            ile.getAnonymousClassBody().get().forEach(b -> ds.add((_declared)_java.of(b)));
        }
        return ds;
    }

    public _expression getScope(){
        if( ile.getScope().isPresent()){
            return _expression.of(this.ile.getScope().get());
        }
        return null;
    }

    public List<_expression> listArguments(){
        List<_expression> args = new ArrayList<>();
        this.ile.getArguments().forEach(a -> args.add(_expression.of(a)));
        return args;
    }

    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public List<_typeRef> listTypeArguments(){
        if( ile.getTypeArguments().isPresent() ){
            List<_typeRef> tas = new ArrayList<>();
            ile.getTypeArguments().get().forEach(t -> tas.add(_typeRef.of(t)));
            return tas;
        }
        return new ArrayList<>();
    }

    public boolean equals(Object other){
        if( other instanceof _new){
            return ((_new)other).ile.equals( this.ile );
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
