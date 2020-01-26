package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayCreationExpr;

import java.util.HashMap;
import java.util.Map;

public class _arrayCreate implements _expression<ArrayCreationExpr, _arrayCreate> {

    public ArrayCreationExpr ile;

    public _arrayCreate(ArrayCreationExpr ile){
        this.ile = ile;
    }

    @Override
    public _arrayCreate copy() {
        return new _arrayCreate(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.arrayCreationEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ArrayCreationExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ArrayCreationExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.INIT, ile.getInitializer());
        comps.put(_java.Component.ARRAY_LEVEL, ile.getLevels());
        comps.put(_java.Component.TYPE, ile.getElementType());
        return comps;
    }

    public _expression getInit(){
        if( this.ile.getInitializer().isPresent()) {
            return _expression.of(this.ile.getInitializer().get());
        }
        return null;
    }

    public _typeRef getType(){
        return _typeRef.of(this.ile.getElementType());
    }

    //TODO remodel this or just leave this??
    public NodeList<ArrayCreationLevel> getArrayLevel(){
        return this.ile.getLevels();
    }

    public boolean equals(Object other){
        if( other instanceof _arrayCreate){
            return ((_arrayCreate)other).ile.equals( this.ile );
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
