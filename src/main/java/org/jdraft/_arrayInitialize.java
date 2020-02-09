package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;

import java.util.*;

public class _arrayInitialize implements _expression<ArrayInitializerExpr, _arrayInitialize>,
        //_java._node<ArrayInitializerExpr, _arrayInitialize>,
        _java._nodeList<Expression, _expression, _arrayInitialize> {

    public static _arrayInitialize of( ){
        return new _arrayInitialize(new ArrayInitializerExpr());
    }
    public static _arrayInitialize of(ArrayInitializerExpr ai){
        return new _arrayInitialize(ai);
    }
    public static _arrayInitialize of( String...code){
        return new _arrayInitialize(Ex.arrayInitializerEx( code));
    }

    public static _arrayInitialize ofStrings( String[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(new StringLiteralExpr(i)));
        return of(aie);
    }

    public static _arrayInitialize of( int[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        Arrays.stream(arr).forEach(i -> aie.getValues().add(new IntegerLiteralExpr(i)));
        return of(aie);
    }

    public static _arrayInitialize of( boolean[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new BooleanLiteralExpr(arr[i]));
        }
        return of(aie);
    }

    public static _arrayInitialize of( float[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new DoubleLiteralExpr( arr[i] +"F" ));
        }
        return of(aie);
    }

    public static _arrayInitialize of( double[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new DoubleLiteralExpr( arr[i] +"D" ));
        }
        return of(aie);
    }

    public static _arrayInitialize of( long[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new LongLiteralExpr( arr[i] ));
        }
        return of(aie);
    }

    public static _arrayInitialize of( char[] arr){
        ArrayInitializerExpr aie = new ArrayInitializerExpr();
        for(int i=0;i<arr.length;i++){
            aie.getValues().add( new CharLiteralExpr( arr[i] ));
        }
        return of(aie);
    }

    public ArrayInitializerExpr ile;

    public _arrayInitialize(ArrayInitializerExpr ile){
        this.ile = ile;
    }

    @Override
    public _arrayInitialize copy() {
        return new _arrayInitialize(this.ile.clone());
    }

    @Override
    public NodeList<Expression> listAstElements() {
        return this.ile.getValues();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.arrayInitializerEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ArrayInitializerExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ArrayInitializerExpr ast(){
        return ile;
    }

    /*
    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.VALUES, ile.getValues());
        return comps;
    }
     */

    @Override
    public List<_expression> list(){
        List<_expression> vs = new ArrayList<>();
        this.ile.getValues().forEach(v-> vs.add( _expression.of(v)));
        return vs;
    }

    public boolean equals(Object other){
        if( other instanceof _arrayInitialize){
            return ((_arrayInitialize)other).ile.equals( this.ile );
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
