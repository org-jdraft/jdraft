package org.jdraft;


import com.github.javaparser.ast.expr.ThisExpr;

/**
 * An occurrence of the "this" keyword. <br/>
 * <code>World.this.greet()</code> is a MethodCallExpr of method name greet,
 * and scope "World.this" which is a ThisExpr with typeName "World". <br/>
 * <code>this.name</code> is a FieldAccessExpr of field greet, and a ThisExpr as its scope.
 * This ThisExpr has no typeName.
 */
public final class _thisExpr implements _expr<ThisExpr, _thisExpr>, _java._uniPart<ThisExpr, _thisExpr> {
    public static _thisExpr of(){
        return new _thisExpr(new ThisExpr());
    }
    public static _thisExpr of(ThisExpr te){
        return new _thisExpr(te);
    }
    public static _thisExpr of(String...code){
        return new _thisExpr(Exprs.thisExpr( code));
    }

    public ThisExpr ile;

    public _thisExpr(ThisExpr ile){
        this.ile = ile;
    }

    @Override
    public _thisExpr copy() {
        return new _thisExpr(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.thisExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ThisExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public ThisExpr ast(){
        return ile;
    }

    public String getName(){
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
