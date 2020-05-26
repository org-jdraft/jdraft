package org.jdraft;

import com.github.javaparser.ast.Modifier;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.Function;

public enum _modifier implements _java._node<Modifier, _modifier> {



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

    public final com.github.javaparser.ast.Modifier mod;

    public static final Function<String, _modifier> PARSER = s-> _modifier.of(s);

    public static _feature._meta<_modifier> META = _feature._meta.of(_modifier.class);

    public final int bitMask;


    _modifier(com.github.javaparser.ast.Modifier mod, int bitMask){
        this.mod = mod;
        this.bitMask = bitMask;
    }

    @Override
    public _modifier copy() {
        return this; //dont need to copy enums
    }

    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( Text.combine(stringRep), this.mod.getKeyword().asString());
    }

    public static _modifier of(com.github.javaparser.ast.Modifier astMod ){
        Optional<_modifier> om = Arrays.stream(values()).filter(m-> m.mod.equals(astMod)).findFirst();
        if( om.isPresent() ){
            return om.get();
        }
        //return new _modifier(astMod, 0);
        //System.out.println("NO MODIFIER FOR "+ astMod);
        return null;
    }

    public static _modifier of(String str ){
        Optional<_modifier> om = Arrays.stream(values()).filter(m-> m.mod.getKeyword().asString().equals(str)).findFirst();
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
        return this.mod.toString();
    }

    @Override
    public Modifier ast() {
        return this.mod;
    }

    public Map<_java.Feature, Object> features() {
        return Collections.emptyMap();
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
