package org.jdraft;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;

public class _boolean implements _expression._literal<BooleanLiteralExpr, _boolean> {

    public static _boolean of( ){
        return new _boolean(new BooleanLiteralExpr());
    }
    public static _boolean of(boolean b){
        return new _boolean(new BooleanLiteralExpr(b));
    }
    public static _boolean of( BooleanLiteralExpr bl){
        return new _boolean(bl);
    }
    public static _boolean of( String...code){
        return new _boolean(Ex.booleanLiteralEx( code));
    }

    public BooleanLiteralExpr be;

    public _boolean(BooleanLiteralExpr be){
        this.be = be;
    }

    @Override
    public _boolean copy() {
        return new _boolean(this.be.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.booleanLiteralEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public boolean is(boolean b){
        return this.ast().getValue() == b;
    }

    @Override
    public boolean is(BooleanLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public boolean isTrue(){
        return this.be.getValue();
    }

    public boolean isFalse(){
        return !this.be.getValue();
    }

    public BooleanLiteralExpr ast(){
        return be;
    }

    public boolean equals(Object other){
        if( other instanceof _boolean){
            return ((_boolean)other).be.equals( this.be);
        }
        return false;
    }

    public _boolean set( BooleanLiteralExpr  b){
        this.be = b;
        return this;
    }

    public _boolean set( boolean b){
        this.be.setValue(b);
        return this;
    }

    public boolean getValue(){
        return this.be.getValue();
    }

    public int hashCode(){
        return 31 * this.be.hashCode();
    }

    public String toString(){
        return this.be.toString();
    }

    @Override
    public String valueAsString() {
        return be.getValue()+"";
    }
}
