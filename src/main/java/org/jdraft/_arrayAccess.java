package org.jdraft;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;

import java.util.HashMap;
import java.util.Map;

public class _arrayAccess implements _expression<ArrayAccessExpr, _arrayAccess> {

    public static _arrayAccess of(){
        return new _arrayAccess(new ArrayAccessExpr());
    }
    public static _arrayAccess of( ArrayAccessExpr ae ){
        return new _arrayAccess(ae);
    }

    public static _arrayAccess of( String code){
        return new _arrayAccess(Ex.arrayAccessEx( code));
    }

    public static _arrayAccess of( String...code){
        return new _arrayAccess(Ex.arrayAccessEx( code));
    }

    //a simple literal name[3]
    public static _arrayAccess of( String name, int index){
        return of( Ex.nameEx(name), index);
    }
    /**
     *
     * @param name
     * @param indexes
     * @return
     */
    public static _arrayAccess of( String name, int... indexes){
        return of( Ex.nameEx(name), indexes);
    }

    public static _arrayAccess of(Expression e, int...indexes){

        if( indexes.length < 1) {
            throw new _jdraftException("must provide at least (1) index");
        }
        ArrayAccessExpr ae = new ArrayAccessExpr();
        ae.setName(e);
        ae.setIndex(new IntegerLiteralExpr(indexes[0]));
        if( indexes.length == 1) {
            return of(ae);
        }
        int[] left = new int[indexes.length-1];
        System.arraycopy(indexes, 1, left, 0, left.length);
        return of(ae, left);
    }
    public ArrayAccessExpr astNode;

    public _arrayAccess(ArrayAccessExpr astNode){
        this.astNode = astNode;
    }

    @Override
    public _arrayAccess copy() {
        return new _arrayAccess(this.astNode.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.arrayAccessEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ArrayAccessExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ArrayAccessExpr ast(){
        return astNode;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.INDEX, astNode.getIndex());
        comps.put(_java.Component.NAME, astNode.getName().toString());
        return comps;
    }

    public _expression getName(){
        return _expression.of(this.astNode.getName());
    }

    public _expression getIndex(){
        return _expression.of(this.astNode.getIndex());
    }

    public boolean equals(Object other){
        if( other instanceof _arrayAccess){
            return ((_arrayAccess)other).astNode.equals( this.astNode);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.astNode.hashCode();
    }


    public String toString(){
        return this.astNode.toString();
    }
}
