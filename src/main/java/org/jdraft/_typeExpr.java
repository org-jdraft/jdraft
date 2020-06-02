package org.jdraft;

import com.github.javaparser.ast.expr.TypeExpr;

import java.util.function.Function;

/**
 * For example:
 * In {@code World::greet} the ClassOrInterfaceType "World" is a _typeEx
 */
public final class _typeExpr
        implements _expr<TypeExpr, _typeExpr>,
        _java._node<TypeExpr, _typeExpr>,
        _typeRef._withTypeRef<TypeExpr, _typeExpr> {

    public static final Function<String, _typeExpr> PARSER = s-> _typeExpr.of(s);

    public static _typeExpr of(){
        return new _typeExpr( new TypeExpr());
    }
    public static _typeExpr of(TypeExpr te){
        return new _typeExpr(te);
    }
    public static _typeExpr of(String...code){
        return new _typeExpr(Expr.typeExpr( code));
    }

    public static _feature._one<_typeExpr, _typeRef> TYPE = new _feature._one<>(_typeExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getTypeRef(),
            (_typeExpr a, _typeRef value) -> a.setTypeRef(value), PARSER);

    public static _feature._features<_typeExpr> FEATURES = _feature._features.of(_typeExpr.class, TYPE);

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
