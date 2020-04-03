package org.jdraft;

import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.type.Type;
import org.jdraft.text.Text;

/**
 * 
 * <PRE><CODE>String.class</CODE></PRE>
 */
public final class _classExpression implements _expression<ClassExpr, _classExpression>, _java._uniPart<ClassExpr, _classExpression> {

    public static _classExpression of(){
        return new _classExpression(new ClassExpr());
    }
    public static _classExpression of( ClassExpr ce){
        return new _classExpression(ce);
    }
    public static _classExpression of( String...code){
        return new _classExpression(Expressions.classEx( code));
    }

    public ClassExpr ce;

    public _classExpression(ClassExpr ce){
        this.ce = ce;
    }

    @Override
    public _classExpression copy() {
        return new _classExpression(this.ce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expressions.classEx(stringRep));
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

    public _classExpression setType(Type t){
        this.ce.setType(t);
        return this;
    }

    public _classExpression setType(_typeRef _t){
        this.ce.setType(_t.ast());
        return this;
    }

    public _classExpression setType(String...type ){
        return setType( _typeRef.of(Text.combine(type)));
    }

    public boolean equals(Object other){
        if( other instanceof _classExpression){
            return ((_classExpression)other).ce.equals( this.ce);
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
