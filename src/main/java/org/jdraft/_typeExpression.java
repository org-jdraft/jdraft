package org.jdraft;

import com.github.javaparser.ast.expr.TypeExpr;

public final class _typeExpression
        implements _expression<TypeExpr, _typeExpression>,
        _java._uniPart<TypeExpr, _typeExpression>,
        _typeRef._withTypeRef<TypeExpr, _typeExpression> {

    public static _typeExpression of(){
        return new _typeExpression( new TypeExpr());
    }
    public static _typeExpression of(TypeExpr te){
        return new _typeExpression(te);
    }
    public static _typeExpression of( String...code){
        return new _typeExpression(Expressions.typeEx( code));
    }

    public TypeExpr te;

    public _typeExpression(TypeExpr te){
        this.te = te;
    }

    @Override
    public _typeExpression copy() {
        return new _typeExpression(this.te.clone());
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
        if( other instanceof _typeExpression){
            _typeExpression _te = ((_typeExpression)other);
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
