package org.jdraft;

import com.github.javaparser.ast.expr.CharLiteralExpr;

import java.util.HashMap;
import java.util.Map;

public class _char implements _expression._literal<CharLiteralExpr, _char> {

    public static _char of(){
        return new _char(new CharLiteralExpr());
    }
    public static _char of(CharLiteralExpr cle){
        return new _char(cle);
    }
    public static _char of(char c){
        return new _char(new CharLiteralExpr(c));
    }
    public static _char of( String...code){
        return new _char(Ex.charLiteralEx( code));
    }

    public CharLiteralExpr ile;

    public _char(CharLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _char copy() {
        return new _char(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.charLiteralEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(CharLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public CharLiteralExpr ast(){
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LITERAL, this.ile.getValue());
        return comps;
    }

    public boolean equals(Object other){
        if( other instanceof _char){
            return ((_char)other).ile.equals( this.ile );
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
