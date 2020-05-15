package org.jdraft;

import com.github.javaparser.ast.expr.DoubleLiteralExpr;

public final class _doubleExpr implements _expr._literal<DoubleLiteralExpr, _doubleExpr> {

    public static _doubleExpr of(){
        return new _doubleExpr( new DoubleLiteralExpr());
    }
    public static _doubleExpr of(DoubleLiteralExpr dl){
        return new _doubleExpr(dl);
    }
    public static _doubleExpr of(String...code){
        return new _doubleExpr(Exprs.doubleLiteralEx( code));
    }

    public static _doubleExpr of(double d){
        return new _doubleExpr(Exprs.doubleLiteralEx( d));
    }

    public DoubleLiteralExpr de;

    public _doubleExpr(DoubleLiteralExpr de){
        this.de = de;
    }

    @Override
    public _doubleExpr copy() {
        return new _doubleExpr(this.de.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.doubleLiteralEx(stringRep));
        } catch(Exception e){

        }
        return false;
    }

    @Override
    public boolean is(DoubleLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public boolean is(double d){
        return Exprs.equal(de, d);
    }

    public Double getValue(){
        return this.de.asDouble();
    }

    public _doubleExpr set(String value){
        this.de.setValue(value);
        return this;
    }

    public _doubleExpr set(Double value){
        this.de.setDouble(value);
        return this;
    }

    public _doubleExpr set(Float value){
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
        if( other instanceof _doubleExpr){
            return ((_doubleExpr)other).de.equals( this.de);
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
