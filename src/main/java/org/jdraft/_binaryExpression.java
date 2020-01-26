package org.jdraft;

import com.github.javaparser.ast.expr.BinaryExpr;

import java.util.HashMap;
import java.util.Map;

public class _binaryExpression implements _expression<BinaryExpr, _binaryExpression> {

    public static _binaryExpression of(){
        return new _binaryExpression( new BinaryExpr());
    }

    public static _binaryExpression of( String...code){
        return new _binaryExpression(Ex.binaryEx( code));
    }

    public BinaryExpr ile;

    public _binaryExpression(BinaryExpr ile){
        this.ile = ile;
    }

    @Override
    public _binaryExpression copy() {
        return new _binaryExpression(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.binaryEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(BinaryExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public BinaryExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LEFT, ile.getLeft());
        comps.put(_java.Component.BINARY_OPERATOR, ile.getOperator());
        comps.put(_java.Component.RIGHT, ile.getRight());
        return comps;
    }

    public _expression getLeft(){
        return _expression.of(this.ile.getLeft());
    }
    public _expression getRight(){
        return _expression.of(this.ile.getRight());
    }

    public BinaryExpr.Operator getOperator(){
        return ile.getOperator();
    }

    public boolean equals(Object other){
        if( other instanceof _binaryExpression){
            return ((_binaryExpression)other).ile.equals( this.ile );
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
