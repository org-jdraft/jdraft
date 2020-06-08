package org.jdraft;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;

import java.util.function.Function;

public final class _booleanExpr implements _expr._literal<BooleanLiteralExpr, _booleanExpr> {

    public static final Function<String, _booleanExpr> PARSER = s-> _booleanExpr.of(s);

    public static _booleanExpr of( ){
        return new _booleanExpr(new BooleanLiteralExpr());
    }

    public static _booleanExpr of(boolean b){
        return new _booleanExpr(new BooleanLiteralExpr(b));
    }
    public static _booleanExpr of(BooleanLiteralExpr bl){
        return new _booleanExpr(bl);
    }
    public static _booleanExpr of(String...code){
        return new _booleanExpr(Expr.booleanLiteralExpr( code));
    }

    public static _feature._one<_booleanExpr, Boolean> LITERAL_VALUE = new _feature._one<>(_booleanExpr.class, Boolean.class,
            _feature._id.LITERAL_VALUE,
            a -> a.getValue(),
            (_booleanExpr a, Boolean b) -> a.set(b), PARSER);


    public static _feature._features<_booleanExpr> FEATURES = _feature._features.of(_booleanExpr.class,  PARSER, LITERAL_VALUE);

    public BooleanLiteralExpr be;

    public _feature._features<_booleanExpr> features(){
        return FEATURES;
    }

    public _booleanExpr(BooleanLiteralExpr be){
        this.be = be;
    }

    @Override
    public _booleanExpr copy() {
        return new _booleanExpr(this.be.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.booleanLiteralExpr(stringRep));
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
        if( other instanceof _booleanExpr){
            return ((_booleanExpr)other).be.equals( this.be);
        }
        return false;
    }

    public _booleanExpr set(String...value){
        this.set(Expr.booleanLiteralExpr(value));
        return this;
    }

    public _booleanExpr set(BooleanLiteralExpr  b){
        this.be = b;
        return this;
    }

    public _booleanExpr set(boolean b){
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
