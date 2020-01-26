package org.jdraft;

import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _methodCall implements _expression<MethodCallExpr, _methodCall> {

    public static _methodCall of(){
        return new _methodCall( new MethodCallExpr( ));
    }

    public static _methodCall of( String...code){
        return new _methodCall(Ex.methodCallEx( code));
    }

    public MethodCallExpr ile;

    public _methodCall(MethodCallExpr ile){
        this.ile = ile;
    }

    @Override
    public _methodCall copy() {
        return new _methodCall(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.methodCallEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(MethodCallExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public MethodCallExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if( ile.getScope().isPresent() ) {
            comps.put(_java.Component.SCOPE, ile.getScope().get());
        }
        comps.put(_java.Component.NAME, ile.getNameAsString());
        if( ile.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, ile.getTypeArguments().get());
        }
        comps.put(_java.Component.ARGUMENTS, ile.getArguments());
        return comps;
    }

    public _expression getScope(){
        if( ile.getScope().isPresent()){
            return _expression.of(this.ile.getScope().get());
        }
        return null;
    }

    public String getName(){
        return this.ile.getNameAsString();
    }

    public List<_expression> getArguments(){
        List<_expression> args = new ArrayList<>();
        this.ile.getArguments().forEach(a -> args.add(_expression.of(a)));
        return args;
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
        if( other instanceof _methodCall){
            return ((_methodCall)other).ile.equals( this.ile );
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
