package org.jdraft;

import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.type.Type;
import org.jdraft.text.Text;

/**
 * 
 * <PRE><CODE>String.class</CODE></PRE>
 */
public final class _classExpr implements _expr<ClassExpr, _classExpr>, _java._uniPart<ClassExpr, _classExpr> {

    public static _classExpr of(){
        return new _classExpr(new ClassExpr());
    }
    public static _classExpr of(ClassExpr ce){
        return new _classExpr(ce);
    }
    public static _classExpr of(String...code){
        return new _classExpr(Exprs.classEx( code));
    }

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
            return is( Exprs.classEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ClassExpr ast(){
        return ce;
    }

    public boolean isType(String type){
        return isType(Types.of(type));
    }

    public boolean isType( _typeRef _t){
        return isType(_t.ast());
    }

    public boolean isType( Type t){
        return Types.equal(this.ce.getType(), t);
    }

    public _typeRef getType(){
        return _typeRef.of(this.ce.getType());
    }

    public _classExpr setType(Type t){
        this.ce.setType(t);
        return this;
    }

    public _classExpr setType(_typeRef _t){
        this.ce.setType(_t.ast());
        return this;
    }

    public _classExpr setType(String...type ){
        return setType( _typeRef.of(Text.combine(type)));
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
