package org.jdraft;


import com.github.javaparser.ast.expr.ThisExpr;

/**
 * An occurrence of the "this" keyword. <br/>
 * <code>World.this.greet()</code> is a MethodCallExpr of method name greet,
 * and scope "World.this" which is a ThisExpr with typeName "World". <br/>
 * <code>this.name</code> is a FieldAccessExpr of field greet, and a ThisExpr as its scope.
 * This ThisExpr has no typeName.
 */
public class _this implements _expression<ThisExpr, _this>, _java._uniPart<ThisExpr, _this> {
    public static _this of(){
        return new _this(new ThisExpr());
    }
    public static _this of(ThisExpr te){
        return new _this(te);
    }
    public static _this of( String...code){
        return new _this(Expressions.thisEx( code));
    }

    public ThisExpr ile;

    public _this(ThisExpr ile){
        this.ile = ile;
    }

    @Override
    public _this copy() {
        return new _this(this.ile.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expressions.thisEx(stringRep));
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
        if( other instanceof _this){
            return ((_this)other).ile.equals( this.ile );
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
