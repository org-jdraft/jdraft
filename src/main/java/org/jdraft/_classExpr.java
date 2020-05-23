package org.jdraft;

import com.github.javaparser.ast.expr.ClassExpr;
import org.jdraft.text.Text;

/**
 * 
 * <PRE><CODE>String.class</CODE></PRE>
 */
public final class _classExpr implements _expr<ClassExpr, _classExpr>, _typeRef._withTypeRef<ClassExpr, _classExpr>, _java._node<ClassExpr, _classExpr> {

    public static _classExpr of(){
        return new _classExpr(new ClassExpr());
    }

    public static _classExpr of(Class clazz){
        return new _classExpr(Exprs.classExpr( clazz) );
    }

    public static _classExpr of(ClassExpr ce){
        return new _classExpr(ce);
    }

    public static _classExpr of(String...code){
        return new _classExpr(Exprs.classExpr( code));
    }

    public static _feature._one<_classExpr, _typeRef> TYPE = new _feature._one<>(_classExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getTypeRef(),
            (_classExpr a, _typeRef o) -> a.setTypeRef(o));

    public static _feature._meta<_classExpr> META = _feature._meta.of(_classExpr.class, TYPE);

    public ClassExpr ce;

    public _classExpr(ClassExpr ce){
        this.ce = ce;
    }

    @Override
    public _classExpr copy() {
        return new _classExpr(this.ce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.classExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ClassExpr ast(){
        return ce;
    }

    /*
    public boolean isTypeRef(String type){
        return isTypeRef(Types.of(type));
    }

    public boolean isTypeRef(_typeRef _t){
        return isTypeRef(_t.ast());
    }

    public boolean isTypeRef(Type t){
        return Types.equal(this.ce.getType(), t);
    }

    public _typeRef getTypeRef(){
        return _typeRef.of(this.ce.getType());
    }

    public _classExpr setTypeRef(Type t){
        this.ce.setType(t);
        return this;
    }

    public _classExpr setTypeRef(_typeRef _t){
        this.ce.setType(_t.ast());
        return this;
    }
    */

    public _classExpr setTypeRef(String...type ){
        return setTypeRef( _typeRef.of(Text.combine(type)));
    }

    public boolean equals(Object other){
        if( other instanceof _classExpr){
            return ((_classExpr)other).ce.equals( this.ce);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ce.hashCode();
    }
    
    public String toString(){
        return this.ce.toString();
    }
}
