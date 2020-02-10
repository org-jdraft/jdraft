package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.expr.Expression;

import java.util.Objects;

/**
 * An expression representing a single Array dimension
 * i.e. for the following (3) dimensional array
 * int[10][arrs.length][getY()] vals;
 * we have (3) distinct _arrayDimension instances
 *  [10] an intLiteral expression denoted dimension
 *  [arrs.length] a fieldAccessExpression denoted dimension
 *  [getY()] a methodCallExpr denoted dimension
 *
 * @see _arrayCreate where we use these _arrayDimensions to declare arrays
 */
public class _arrayDimension implements _java._simple<ArrayCreationLevel, _arrayDimension> {

    public static _arrayDimension of(ArrayCreationLevel acl){
        return new _arrayDimension(acl);
    }

    public static _arrayDimension of(int index){
        return new _arrayDimension(new ArrayCreationLevel().setDimension( Ex.of(index) ));
    }

    public static _arrayDimension of(Expression e){
        return new _arrayDimension(new ArrayCreationLevel().setDimension( e ));
    }

    public static _arrayDimension of(_expression _e){
        return new _arrayDimension(new ArrayCreationLevel().setDimension( _e.ast() ));
    }

    public final ArrayCreationLevel astNode;

    public _arrayDimension(ArrayCreationLevel acl){
        this.astNode = acl;
    }

    @Override
    public _arrayDimension copy() {
        return _arrayDimension.of(this.astNode);
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.arrayCreationLevel(stringRep));
        } catch(Exception e){
            return false;
        }
    }

    public _arrayDimension setExpression(String... dimension){
        this.astNode.setDimension(Ex.of(dimension));
        return this;
    }

    public _arrayDimension setExpression(Expression dimension){
        this.astNode.setDimension(dimension);
        return this;
    }

    public _arrayDimension setExpression(_expression dimension){
        this.astNode.setDimension(dimension.ast());
        return this;
    }

    @Override
    public boolean is(ArrayCreationLevel astNode) {
        return Objects.equals(this.astNode, astNode);
    }

    @Override
    public ArrayCreationLevel ast() {
        return this.astNode;
    }

    public String toString(){
        return this.astNode.toString();
    }

    public int hashCode(){
        return 31 * this.astNode.hashCode();
    }

    public boolean equals( Object o ){
        if( o instanceof _arrayDimension ){
            _arrayDimension _a = (_arrayDimension)o;
            return Objects.equals( _a.astNode, this.astNode);
        }
        return false;
    }
}
