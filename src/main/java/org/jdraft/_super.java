package org.jdraft;

import com.github.javaparser.ast.expr.SuperExpr;

import java.util.HashMap;
import java.util.Map;

/**
 * Usage of the super keyword
 *
 * <code>super.doIt()</code> is a MethodCallExpr of method doIt(), and a SuperExpr as its scope.
 * This SuperExpr has no typeName.
 *
 * <code>super.MyType.doIt()</code> is a MethodCallExpr of method doIt(), and a SuperExpr as its scope.
 * This SuperExpr has typeName "MyType".
 *
 * <code>super.name</code> is a FieldAccessExpr of field greet, and a SuperExpr as its scope.
 * This SuperExpr has no typeName.
 *
 * @see _constructorCallStmt
 */
public class _super implements _expression<SuperExpr, _super>, _java._simple<SuperExpr,_super> {

    public static _super of(){
        return new _super( new SuperExpr());
    }
    public static _super of(SuperExpr se){
        return new _super(se);
    }
    public static _super of( String...code){
        return new _super(Ex.superEx( code));
    }

    public SuperExpr se;

    public _super(SuperExpr se){
        this.se = se;
    }

    @Override
    public _super copy() {
        return new _super(this.se.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.superEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(SuperExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public SuperExpr ast(){
        return se;
    }

    /*
    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        if(this.se.getTypeName().isPresent()){
            comps.put(_java.Component.NAME, se.getTypeName().get().asString());
        }
        return comps;
    }
     */

    public String getName(){
        if(this.se.getTypeName().isPresent()){
            return se.getTypeName().get().asString();
        }
        return "";
    }

    public boolean equals(Object other){
        if( other instanceof _super){
            return ((_super)other).se.equals( this.se);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.se.hashCode();
    }

    public String toString(){
        return this.se.toString();
    }
}
