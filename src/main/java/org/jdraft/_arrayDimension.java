package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class _arrayDimension implements _node<ArrayCreationLevel, _arrayDimension>  {

    public static _arrayDimension of(ArrayCreationLevel acl){
        return new _arrayDimension(acl);
    }

    public final ArrayCreationLevel arrayCreationLevel;

    public _arrayDimension(ArrayCreationLevel acl){
        this.arrayCreationLevel = acl;
    }

    @Override
    public _arrayDimension copy() {
        return _arrayDimension.of(this.arrayCreationLevel);
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
        return Objects.equals( arrayCreationLevel, astNode);
    }

    @Override
    public ArrayCreationLevel ast() {
        return this.arrayCreationLevel;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object>mm = new HashMap<>();
        mm.put(_java.Component.ARRAY_DIMENSION, this.arrayCreationLevel.getDimension());
        return mm;
    }
}
