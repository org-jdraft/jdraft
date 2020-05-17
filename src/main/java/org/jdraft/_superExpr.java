package org.jdraft;

import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SuperExpr;
import org.jdraft.text.Text;

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
public final class _superExpr implements _expr<SuperExpr, _superExpr>, _java._uniPart<SuperExpr, _superExpr> {

    public static _superExpr of(){
        return new _superExpr( new SuperExpr());
    }
    public static _superExpr of(SuperExpr se){
        return new _superExpr(se);
    }
    public static _superExpr of(String...code){
        String st = Text.combine(code);
        if(st.startsWith("super")) {
            st = st.substring("super".length() -1).trim();
        }
        if( st.length() > 0) {
            return new _superExpr(new SuperExpr(new Name(st)));
        }
        return new _superExpr( new SuperExpr());
        //return new _super(Ex.superEx( code));
    }

    public SuperExpr se;

    public _superExpr(SuperExpr se){
        this.se = se;
    }

    @Override
    public _superExpr copy() {
        return new _superExpr(this.se.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.superExpr(stringRep));
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

    public String getName(){
        if(this.se.getTypeName().isPresent()){
            return se.getTypeName().get().asString();
        }
        return "";
    }

    public boolean equals(Object other){
        if( other instanceof _superExpr){
            return ((_superExpr)other).se.equals( this.se);
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
