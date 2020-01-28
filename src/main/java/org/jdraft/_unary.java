package org.jdraft;

import com.github.javaparser.ast.expr.UnaryExpr;

import java.util.HashMap;
import java.util.Map;

/**
 * The unary expression i.e. ("!done")
 * In <code>b==0?x:y</code>, b==0 is the condition, x is thenExpr, and y is elseExpr.
 */
public class _unary implements _expression<UnaryExpr, _unary> {

    public static _unary of(){
        return new _unary(new UnaryExpr());
    }
    public static _unary of(UnaryExpr ue){
        return new _unary(ue);
    }
    public static _unary of( String...code){
        return new _unary(Ex.unaryEx( code));
    }

    public UnaryExpr ile;

    public _unary(UnaryExpr ile){
        this.ile = ile;
    }

    @Override
    public _unary copy() {
        return new _unary(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.unaryEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(UnaryExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public UnaryExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.UNARY_OPERATOR, ile.getOperator());
        comps.put(_java.Component.EXPRESSION, ile.getExpression());
        return comps;
    }

    public _expression getExpression(){
        return _expression.of(this.ile.getExpression());
    }

    public UnaryExpr.Operator getOperator(){
        return this.ile.getOperator();
    }

    public boolean equals(Object other){
        if( other instanceof _unary){
            return ((_unary)other).ile.equals( this.ile );
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
