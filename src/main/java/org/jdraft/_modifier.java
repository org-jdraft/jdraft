package org.jdraft;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.expr.CharLiteralExpr;

import java.util.*;
import java.util.function.Function;

public enum _modifier implements _tree._node<Modifier, _modifier> {

    /** Making the internal AST modifiers more accessible */
    PUBLIC( com.github.javaparser.ast.Modifier.publicModifier(), java.lang.reflect.Modifier.PUBLIC ),
    PRIVATE( com.github.javaparser.ast.Modifier.privateModifier(), java.lang.reflect.Modifier.PRIVATE ),
    PROTECTED( com.github.javaparser.ast.Modifier.protectedModifier(), java.lang.reflect.Modifier.PROTECTED ),
    STATIC( com.github.javaparser.ast.Modifier.staticModifier(), java.lang.reflect.Modifier.STATIC ),
    FINAL( com.github.javaparser.ast.Modifier.finalModifier(), java.lang.reflect.Modifier.FINAL ),
    ABSTRACT( com.github.javaparser.ast.Modifier.abstractModifier(), java.lang.reflect.Modifier.ABSTRACT ),
    SYNCHRONIZED( com.github.javaparser.ast.Modifier.synchronizedModifier(), java.lang.reflect.Modifier.SYNCHRONIZED ),
    STRICT( com.github.javaparser.ast.Modifier.strictfpModifier(), java.lang.reflect.Modifier.STRICT ),
    VOLATILE(com.github.javaparser.ast.Modifier.volatileModifier(), java.lang.reflect.Modifier.VOLATILE ),
    TRANSIENT(com.github.javaparser.ast.Modifier.transientModifier(), java.lang.reflect.Modifier.TRANSIENT ),
    DEFAULT(new com.github.javaparser.ast.Modifier(Modifier.Keyword.DEFAULT), 0 ),
    NATIVE( com.github.javaparser.ast.Modifier.nativeModifier(), java.lang.reflect.Modifier.NATIVE );

    public final com.github.javaparser.ast.Modifier node;

    public static final Function<String, _modifier> PARSER = s-> _modifier.of(s);

    public static _feature._features<_modifier> FEATURES = _feature._features.of(_modifier.class, PARSER);

    public final int bitMask;

    _modifier(com.github.javaparser.ast.Modifier node, int bitMask){
        this.node = node;
        this.bitMask = bitMask;
    }

    @Override
    public _modifier copy() {
        return this; //dont need to copy enums
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _modifier replace(Modifier replaceNode){
        throw new _jdraftException ("cannot replace modifiers");
    }

    public static _modifier of(com.github.javaparser.ast.Modifier astMod ){
        Optional<_modifier> om = Arrays.stream(values()).filter(m-> m.node.equals(astMod)).findFirst();
        if( om.isPresent() ){
            return om.get();
        }
        //return new _modifier(astMod, 0);
        //System.out.println("NO MODIFIER FOR "+ astMod);
        return null;
    }

    public static _modifier of(String str ){
        Optional<_modifier> om = Arrays.stream(values()).filter(m-> m.node.getKeyword().asString().equals(str)).findFirst();
        if( om.isPresent() ){
            return om.get();
        }
        return null;
    }

    public static _modifier of(int bitMask){
        Optional<_modifier> om = Arrays.stream(values()).filter(m-> m.bitMask== bitMask).findFirst();
        if( om.isPresent() ){
            return om.get();
        }
        return null;
    }

    public String toString(){
        return this.node.toString();
    }

    @Override
    public Modifier node() {
        return this.node;
    }

    public _feature._features<_modifier> features(){
        return FEATURES;
    }


    /**
     * getting the modifiers reflectively will return a bitMask
     * check if this modifier is present based on the bitmask
     *
     * @param bitMask
     * @return
     */
    public boolean isIn(int bitMask){
        return (this.bitMask & bitMask) != 0;
    }
}
