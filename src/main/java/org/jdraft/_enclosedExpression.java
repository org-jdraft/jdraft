package org.jdraft;

import com.github.javaparser.ast.expr.EnclosedExpr;

import java.util.HashMap;
import java.util.Map;

/**
 *  An expression between ( ).
 *  <br/><code>(1+1)</code>
 */
public class _enclosedExpression implements _expression<EnclosedExpr, _enclosedExpression> {

    public static _enclosedExpression of( ){
        return new _enclosedExpression(new EnclosedExpr( ));
    }

    public static _enclosedExpression of( String...code){
        return new _enclosedExpression(Ex.enclosedEx( code));
    }

    public EnclosedExpr ile;

    public _enclosedExpression(EnclosedExpr ile){
        this.ile = ile;
    }

    @Override
    public _enclosedExpression copy() {
        return new _enclosedExpression(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.enclosedEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(EnclosedExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public EnclosedExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.INNER, ile.getInner());
        return comps;
    }

    public _expression getInner(){
        return _expression.of(this.ile.getInner());
    }

    public boolean equals(Object other){
        if( other instanceof _enclosedExpression){
            return ((_enclosedExpression)other).ile.equals( this.ile );
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
