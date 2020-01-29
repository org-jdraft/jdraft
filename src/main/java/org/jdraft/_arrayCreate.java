package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;

import java.util.*;

public class _arrayCreate implements _expression<ArrayCreationExpr, _arrayCreate> {

    public static _arrayCreate of( ){
        return new _arrayCreate(new ArrayCreationExpr());
    }
    public static _arrayCreate of(ArrayCreationExpr ac){
        return new _arrayCreate(ac);
    }
    public static _arrayCreate of( String...code){
        return new _arrayCreate(Ex.arrayCreationEx( code));
    }

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

        if( ile.getInitializer().isPresent()) {
            comps.put(_java.Component.INIT, ile.getInitializer());
        }
        comps.put(_java.Component.ARRAY_DIMENSIONS, ile.getLevels());
        comps.put(_java.Component.TYPE, ile.getElementType());
        return comps;
    }

    public boolean isInit(String... initCode){
        try{
            return isInit(Ex.arrayInitializerEx(initCode));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isInit(ArrayInitializerExpr aie){
        if( this.ile.getInitializer().isPresent()) {
            return Objects.equals(this.ile.getInitializer().get(), aie);
        }
        return aie == null;
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

    public List<_arrayDimension> getArrayDimensions(){
        List<_arrayDimension> ads = new ArrayList<>();
        this.ile.getLevels().forEach(d -> ads.add( _arrayDimension.of(d)));
        return ads;
    }
    /*
    //TODO remodel this or just leave this??
    public NodeList<ArrayCreationLevel> getArrayLevel(){
        return this.ile.getLevels();
    }
     */

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
