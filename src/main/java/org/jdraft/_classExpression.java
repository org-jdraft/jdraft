package org.jdraft;

import com.github.javaparser.ast.expr.ClassExpr;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * <PRE><CODE>String.class</CODE></PRE>
 */
public class _classExpression implements _expression<ClassExpr, _classExpression> {

    public static _classExpression of(){
        return new _classExpression(new ClassExpr());
    }
    public static _classExpression of( ClassExpr ce){
        return new _classExpression(ce);
    }
    public static _classExpression of( String...code){
        return new _classExpression(Ex.classEx( code));
    }

    public ClassExpr ile;

    public _classExpression(ClassExpr ile){
        this.ile = ile;
    }

    @Override
    public _classExpression copy() {
        return new _classExpression(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.classEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ClassExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ClassExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TYPE, ile.getType());
        return comps;
    }

    public _typeRef getType(){
        return _typeRef.of(this.ile.getType());
    }

    public boolean equals(Object other){
        if( other instanceof _classExpression){
            return ((_classExpression)other).ile.equals( this.ile );
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
