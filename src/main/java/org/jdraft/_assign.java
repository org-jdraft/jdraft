package org.jdraft;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class _assign implements _expression<AssignExpr, _assign> {

    public static _assign of(){
        return new _assign( new AssignExpr());
    }
    public static _assign of( AssignExpr ae){
        return new _assign(ae);
    }

    public static _assign of( String...code){
        return new _assign(Ex.assignEx( code));
    }

    public AssignExpr ile;

    public _assign(AssignExpr ile){
        this.ile = ile;
    }

    @Override
    public _assign copy() {
        return new _assign(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.assignEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(AssignExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public AssignExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.TARGET, ile.getTarget());
        comps.put(_java.Component.VALUE, ile.getValue());
        return comps;
    }

    public boolean isValue(String str){
        return Objects.equals( this.ile.getValue(), Ex.of(str));
    }

    public boolean isTarget(String str){
        return Objects.equals( this.ile.getTarget(), Ex.of(str));
    }

    public boolean isValue(Expression e){
        return Objects.equals( this.ile.getValue(), e);
    }

    public boolean isTarget(Expression e){
        return Objects.equals( this.ile.getTarget(), e);
    }

    public _assign setTarget(Expression e){
        this.ile.setTarget(e);
        return this;
    }

    public _assign setValue(Expression e){
        this.ile.setValue(e);
        return this;
    }

    public _expression getValue(){
        return _expression.of(this.ile.getValue());
    }

    public _expression getTarget(){
        return _expression.of(this.ile.getTarget());
    }

    public boolean equals(Object other){
        if( other instanceof _assign){
            return ((_assign)other).ile.equals( this.ile );
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