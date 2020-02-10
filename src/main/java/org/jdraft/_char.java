package org.jdraft;

import com.github.javaparser.ast.expr.CharLiteralExpr;

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

    public CharLiteralExpr cle;

    public _char(CharLiteralExpr cle){
        this.cle = cle;
    }

    @Override
    public _char copy() {
        return new _char(this.cle.clone());
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

    public boolean is(char c){
        return this.cle.getValue().equals( c+"");
    }

    public CharLiteralExpr ast(){
        return cle;
    }

    public boolean equals(Object other){
        if( other instanceof _char){
            return ((_char)other).cle.equals( this.cle);
        }
        return false;
    }

    public _char setValue( char c){
        this.cle.setValue(c+"");
        return this;
    }

    public char getValue(){
        return this.cle.asChar();
    }

    public String valueAsString(){
        return this.cle.toString();
    }

    public int hashCode(){
        return 31 * this.cle.hashCode();
    }

    public String toString(){
        return this.cle.toString();
    }
}
