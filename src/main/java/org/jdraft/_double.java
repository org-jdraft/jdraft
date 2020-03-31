package org.jdraft;

import com.github.javaparser.ast.expr.DoubleLiteralExpr;

public class _double implements _expression._literal<DoubleLiteralExpr, _double> {

    public static _double of(){
        return new _double( new DoubleLiteralExpr());
    }
    public static _double of( DoubleLiteralExpr dl){
        return new _double(dl);
    }
    public static _double of( String...code){
        return new _double(Expressions.doubleLiteralEx( code));
    }

    public static _double of( double d){
        return new _double(Expressions.doubleLiteralEx( d));
    }

    public DoubleLiteralExpr de;

    public _double(DoubleLiteralExpr de){
        this.de = de;
    }

    @Override
    public _double copy() {
        return new _double(this.de.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expressions.doubleLiteralEx(stringRep));
        } catch(Exception e){

        }
        return false;
    }

    @Override
    public boolean is(DoubleLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public boolean is(double d){
        return Expressions.equal(de, d);
    }

    public Double getValue(){
        return this.de.asDouble();
    }

    public _double set(String value){
        this.de.setValue(value);
        return this;
    }

    public _double set(Double value){
        this.de.setDouble(value);
        return this;
    }

    public _double set(Float value){
        this.de.setValue(value+"F");
        return this;
    }

    /**
     * Important that there are multiple representations for the same float value
     * 1.23f
     * 1.23d
     *
     * @return
     */
    public String valueAsString(){
        return this.de.toString();
    }

    public DoubleLiteralExpr ast(){
        return de;
    }

    public boolean equals(Object other){
        if( other instanceof _double){
            return ((_double)other).de.equals( this.de);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.de.hashCode();
    }

    public String toString(){
        return this.de.toString();
    }
}
