package org.jdraft;

import com.github.javaparser.ast.expr.FieldAccessExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _fieldAccess implements _expression<FieldAccessExpr, _fieldAccess> {

    public static _fieldAccess of(){
        return new _fieldAccess(new FieldAccessExpr());
    }

    public static _fieldAccess of( String...code){
        return new _fieldAccess(Ex.fieldAccessEx( code));
    }

    public FieldAccessExpr ile;

    public _fieldAccess(FieldAccessExpr ile){
        this.ile = ile;
    }

    @Override
    public _fieldAccess copy() {
        return new _fieldAccess(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.fieldAccessEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(FieldAccessExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public FieldAccessExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.SCOPE, ile.getScope());
        if( ile.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, ile.getTypeArguments().get());
        }
        comps.put(_java.Component.NAME, ile.getNameAsString());
        return comps;
    }

    public _expression getScope(){
        return _expression.of(this.ile.getScope());
    }

    /**
     * Returns the List of Type Arguments or an empty list if there are no type arguments
     * @return
     */
    public List<_typeRef> getTypeArguments(){
        List<_typeRef> tas = new ArrayList<>();
        if( this.ile.getTypeArguments().isPresent() ){
            this.ile.getTypeArguments().get().forEach( ta-> tas.add(_typeRef.of(ta)));
        }
        return tas;
    }

    public String getName(){
        return ile.getNameAsString();
    }

    public boolean equals(Object other){
        if( other instanceof _fieldAccess){
            return ((_fieldAccess)other).ile.equals( this.ile );
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
