package org.jdraft;

import com.github.javaparser.ast.expr.ConditionalExpr;

import java.util.HashMap;
import java.util.Map;

/**
 * The ternary conditional expression.
 * In <code>b==0?x:y</code>, b==0 is the condition, x is thenExpr, and y is elseExpr.
 */
public class _ternary implements _expression<ConditionalExpr, _ternary> {

    public static _ternary of(){
        return new _ternary( new ConditionalExpr( ));
    }
    public static _ternary of(ConditionalExpr ce){
        return new _ternary(ce);
    }
    public static _ternary of( String...code){
        return new _ternary(Ex.conditionalEx( code));
    }

    public ConditionalExpr ile;

    public _ternary(ConditionalExpr ile){
        this.ile = ile;
    }

    @Override
    public _ternary copy() {
        return new _ternary(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.conditionalEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ConditionalExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ConditionalExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        comps.put(_java.Component.CONDITION, ile.getCondition());
        comps.put(_java.Component.THEN, ile.getThenExpr());
        comps.put(_java.Component.ELSE, ile.getElseExpr());
        return comps;
    }

    public _expression getCondition(){
        return _expression.of(this.ile.getCondition());
    }

    public _expression getThen(){
        return _expression.of(this.ile.getThenExpr());
    }

    public _expression getElse(){
        return _expression.of(this.ile.getElseExpr());
    }

    public boolean equals(Object other){
        if( other instanceof _ternary){
            return ((_ternary)other).ile.equals( this.ile );
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
