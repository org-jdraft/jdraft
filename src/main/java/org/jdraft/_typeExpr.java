package org.jdraft;

import com.github.javaparser.ast.expr.TypeExpr;

/**
 * For example:
 * In {@code World::greet} the ClassOrInterfaceType "World" is a _typeEx
 */
public final class _typeExpr
        implements _expr<TypeExpr, _typeExpr>,
        _java._uniPart<TypeExpr, _typeExpr>,
        _typeRef._withTypeRef<TypeExpr, _typeExpr> {

    public static _typeExpr of(){
        return new _typeExpr( new TypeExpr());
    }
    public static _typeExpr of(TypeExpr te){
        return new _typeExpr(te);
    }
    public static _typeExpr of(String...code){
        return new _typeExpr(Exprs.typeEx( code));
    }

    public TypeExpr te;

    public _typeExpr(TypeExpr te){
        this.te = te;
    }

    @Override
    public _typeExpr copy() {
        return new _typeExpr(this.te.clone());
    }

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

    public boolean equals(Object other){
        if( other instanceof _typeExpr){
            _typeExpr _te = ((_typeExpr)other);
            return Types.equal( _te.ast().getType(), this.te.getType());
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
