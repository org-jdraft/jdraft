package org.jdraft;

import com.github.javaparser.ast.expr.StringLiteralExpr;

import java.util.HashMap;
import java.util.Map;

public class _string implements _expression<StringLiteralExpr, _string> {

    public static _string of(){
        return new _string( new StringLiteralExpr());
    }
    public static _string of(StringLiteralExpr sle){
        return new _string(sle);
    }
    public static _string of( String...code){
        return new _string(Ex.stringLiteralEx( code));
    }

    public StringLiteralExpr se;

    public _string(String str){
        this( new StringLiteralExpr(str));
    }

    public _string(StringLiteralExpr se){
        this.se = se;
    }

    @Override
    public _string copy() {
        return new _string(this.se.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.stringLiteralEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(StringLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public StringLiteralExpr ast(){
        return se;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LITERAL, this.se.getValue());
        return comps;
    }

    public boolean equals(Object other){
        if( other instanceof _string){
            return ((_string)other).se.equals( this.se);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.se.hashCode();
    }

    public String toString(){
        return this.se.toString();
    }
}
