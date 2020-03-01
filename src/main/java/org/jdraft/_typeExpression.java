package org.jdraft;

import com.github.javaparser.ast.expr.TypeExpr;

public class _typeExpression
        implements _expression<TypeExpr, _typeExpression>,
        _java._uniPart<TypeExpr, _typeExpression>,
        _java._withTypeRef<TypeExpr, _typeExpression> {

    public static _typeExpression of(){
        return new _typeExpression( new TypeExpr());
    }
    public static _typeExpression of(TypeExpr te){
        return new _typeExpression(te);
    }
    public static _typeExpression of( String...code){
        return new _typeExpression(Ex.typeEx( code));
    }

    public TypeExpr te;

    public _typeExpression(TypeExpr te){
        this.te = te;
    }

    @Override
    public _typeExpression copy() {
        return new _typeExpression(this.te.clone());
    }

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.typeEx(stringRep));
        } catch(Exception e){ }
        return false;
    }
     */

    /*
    @Override
    public boolean is(TypeExpr astNode) {
        return this.ast( ).equals(astNode);
    }
     */

    public TypeExpr ast(){
        return te;
    }

    /**
     * Returns a list of Type arguments if there are any or an empty list if there are none
     * @return
     */
    public _typeRef getTypeRef(){
        return _typeRef.of(this.te.getType());
    }

    /** these COULD be generated
    public _typeExpression setType( _typeRef _tr){
        this.te.setType(_tr.ast());
        return this;
    }*/

    /*
    public _typeExpression setType( Type type){
        this.te.setType(type);
        return this;
    }
     */

    /*
    public _typeExpression setType(Class clazz){
        this.te.setType(clazz);
        return this;
    }

    public _typeExpression setType(String typeRef){
        this.te.setType(Ast.typeRef(typeRef));
        return this;
    }
     */

    public boolean equals(Object other){
        if( other instanceof _typeExpression){
            _typeExpression _te = ((_typeExpression)other);
            return Ast.typesEqual( _te.ast().getType(), this.te.getType());
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.te.hashCode();
    }
    
    public String toString(){
        return this.te.toString();
    }
}
