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
        return new _doubleExpr(Exprs.doubleExpr( code));
    }

    public static _doubleExpr of(double d){
        return new _doubleExpr(Exprs.doubleExpr( d));
    }

    public static _feature._one<_doubleExpr, String> LITERAL_VALUE = new _feature._one<>(_doubleExpr.class, String.class,
            _feature._id.LITERAL_VALUE,
            a -> a.valueAsString(),
            (_doubleExpr a, String value) -> a.ast().setValue(value));


    public static _feature._meta<_doubleExpr> META = _feature._meta.of(_doubleExpr.class, LITERAL_VALUE);

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
            return is( Exprs.doubleExpr(stringRep));
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
