package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An expression representing a single Array dimension
 * i.e. for the following (3) dimensional array
 * int[10][arrs.length][getY()] vals;
 * we have (3) distinct _arrayDimension instances
 *  [10] an intLiteral expression denoted dimension
 *  [arrs.length] a fieldaccessExpression denoted diumension
 *  [getY()] a methodCallExpr denoted dimension
 *
 * @see _arrayCreate where we use these _arrayDimensions to declare arrays
 */
public class _arrayDimension implements _java._node<ArrayCreationLevel, _arrayDimension> {

    public static _arrayDimension of(ArrayCreationLevel acl){
        return new _arrayDimension(acl);
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

    @Override
    public boolean is(ArrayCreationLevel astNode) {
        return Objects.equals(this.astNode, astNode);
    }

    @Override
    public ArrayCreationLevel ast() {
        return this.astNode;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object>mm = new HashMap<>();
        mm.put(_java.Component.ARRAY_DIMENSION, this.astNode.getDimension());
        return mm;
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
