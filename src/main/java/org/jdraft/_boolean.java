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

    public BooleanLiteralExpr ile;

    public _boolean(BooleanLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _boolean copy() {
        return new _boolean(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.booleanLiteralEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(BooleanLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public BooleanLiteralExpr ast(){
        return ile;
    }

    /*
    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LITERAL, this.ile.getValue());
        return comps;
    }
     */

    public boolean equals(Object other){
        if( other instanceof _boolean){
            return ((_boolean)other).ile.equals( this.ile );
        }
        return false;
    }

    public boolean getValue(){
        return this.ile.getValue();
    }

    public int hashCode(){
        return 31 * this.ile.hashCode();
    }

    public String toString(){
        return this.ile.toString();
    }

    @Override
    public String valueAsString() {
        return ile.getValue()+"";
    }
}
