package org.jdraft;


import com.github.javaparser.ast.expr.ThisExpr;

import java.util.function.Function;

/**
 * An occurrence of the "this" keyword. <br/>
 * <code>World.this.greet()</code> is a MethodCallExpr of method name greet,
 * and scope "World.this" which is a ThisExpr with typeName "World". <br/>
 * <code>this.name</code> is a FieldAccessExpr of field greet, and a ThisExpr as its scope.
 * This ThisExpr has no typeName.
 */
public final class _thisExpr implements _expr<ThisExpr, _thisExpr>, _java._node<ThisExpr, _thisExpr> {
    public static _thisExpr of(){
        return new _thisExpr(new ThisExpr());
    }
    public static _thisExpr of(ThisExpr te){
        return new _thisExpr(te);
    }
    public static _thisExpr of(String...code){
        return new _thisExpr(Expr.thisExpr( code));
    }

    public static final Function<String, _thisExpr> PARSER = s-> _thisExpr.of(s);

    public static _feature._one<_thisExpr, String> TYPE_NAME = new _feature._one<>(_thisExpr.class, String.class,
            _feature._id.TYPE_NAME,
            a -> a.getTypeName(),
            (_thisExpr a, String value) -> a.setTypeName(value), PARSER);

    public static _feature._features<_thisExpr> FEATURES = _feature._features.of(_thisExpr.class, TYPE_NAME);

    public ThisExpr ile;

    public _thisExpr(ThisExpr ile){
        this.ile = ile;
    }

    public _feature._features<_thisExpr> features(){
        return FEATURES;
    }

    @Override
    public _thisExpr copy() {
        return new _thisExpr(this.ile.clone());
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.thisExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }
     */

    @Override
    public boolean is(ThisExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ThisExpr ast(){
        return ile;
    }


    public _thisExpr removeTypeName(){
        this.ile.removeTypeName();
        return this;
    }

    public _thisExpr setTypeName(String name){
        this.ile.setTypeName(Ast.name(name) );
        return this;
    }

    public String getTypeName(){
        if(this.ile.getTypeName().isPresent()){
            return ile.getTypeName().get().asString();
        }
        return "";
    }

    public boolean equals(Object other){
        if( other instanceof _thisExpr){
            return ((_thisExpr)other).ile.equals( this.ile );
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
