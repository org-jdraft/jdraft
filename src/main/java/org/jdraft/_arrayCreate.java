package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.type.Type;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _arrayCreate implements _expression<ArrayCreationExpr, _arrayCreate>,
        _java._compound<ArrayCreationExpr, _arrayCreate>,
        _java._nodeList<ArrayCreationLevel, _arrayDimension, _arrayCreate> {

    public static _arrayCreate of( ){
        return new _arrayCreate(new ArrayCreationExpr());
    }
    public static _arrayCreate of(ArrayCreationExpr ac){
        return new _arrayCreate(ac);
    }
    public static _arrayCreate of( String...code){
        return new _arrayCreate(Ex.arrayCreationEx( code));
    }


    public static <A extends Object> _arrayCreate of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _arrayCreate of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _arrayCreate of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayCreate of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayCreate of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _arrayCreate of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayCreate of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayCreate of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _arrayCreate from( LambdaExpr le){
        Optional<ArrayCreationExpr> ows = le.getBody().findFirst(ArrayCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No binary expression found in lambda");
    }

    public ArrayCreationExpr astNode;

    public _arrayCreate(ArrayCreationExpr astNode){
        this.astNode = astNode;
    }

    @Override
    public _arrayCreate copy() {
        return new _arrayCreate(this.astNode.clone());
    }

    @Override
    public List<_arrayDimension> list() {
        List<_arrayDimension> _ads = new ArrayList<>();
        this.astNode.getLevels().forEach(l-> _ads.add( _arrayDimension.of(l)));
        return _ads;
    }

    @Override
    public NodeList<ArrayCreationLevel> listAstElements() {
        return this.astNode.getLevels();
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.arrayCreationEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(ArrayCreationExpr astNode) {
        return this.ast( ).equals(astNode);
    }


    public ArrayCreationExpr ast(){
        return astNode;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if( astNode.getInitializer().isPresent()) {
            comps.put(_java.Component.INIT, astNode.getInitializer());
        }
        comps.put(_java.Component.ARRAY_DIMENSIONS, astNode.getLevels());
        comps.put(_java.Component.TYPE, astNode.getElementType());
        return comps;
    }

    public _arrayCreate setInit(_arrayInitialize ae){
        this.astNode.setInitializer(ae.ast());
        return this;
    }

    public _arrayCreate setInit(ArrayInitializerExpr ae){
        this.astNode.setInitializer(ae);
        return this;
    }

    public boolean isElementType(String typeRef){
        return Ast.typesEqual( this.astNode.getElementType(), _typeRef.of(typeRef).ast());
    }


    public boolean isElementType( _typeRef _t ){
        return Ast.typesEqual( this.astNode.getElementType(), _t.ast());
    }

    public boolean isElementType(Type t){
        return Ast.typesEqual( this.astNode.getElementType(),t);
    }

    public boolean hasInit(){
        return this.astNode.getInitializer().isPresent();
    }

    public boolean isInit(String... initCode){
        try{
            return isInit(Ex.arrayInitializerEx(initCode));
        }catch(Exception e){
            return false;
        }
    }

    public boolean isInit(ArrayInitializerExpr aie){
        if( this.astNode.getInitializer().isPresent()) {
            return Objects.equals(this.astNode.getInitializer().get(), aie);
        }
        return aie == null;
    }

    public _expression getInit(){
        if( this.astNode.getInitializer().isPresent()) {
            return _expression.of(this.astNode.getInitializer().get());
        }
        return null;
    }

    public _typeRef getElementType(){
        return _typeRef.of(this.astNode.getElementType());
    }

    public _arrayCreate setElementType(_typeRef _tr){
        this.astNode.setElementType(_tr.ast());
        return this;
    }

    public _arrayCreate setElementType(Class clazz){
        this.astNode.setElementType(clazz);
        return this;
    }

    public _arrayCreate setElementType(String str){
        this.astNode.setElementType(_typeRef.of(str).ast());
        return this;
    }

    public List<_arrayDimension> getArrayDimensions(){

        List<_arrayDimension> ads = new ArrayList<>();
        this.astNode.getLevels().forEach(d -> ads.add( _arrayDimension.of(d)));
        return ads;
    }

    public _arrayCreate setArrayDimensions(String...code){
        this.astNode.setLevels( Ex.arrayCreationLevels(code) );
        return this;
    }

    public _arrayCreate setArrayDimensions(NodeList<ArrayCreationLevel> acls){
        this.astNode.setLevels( acls );
        return this;
    }

    public boolean isArrayDimensions(String... dimensions){
        try {
            return isArrayDimensions( Ex.arrayCreationLevels(dimensions));
        } catch(Exception e){
            return false;
        }
    }

    public boolean isArrayDimensions(NodeList<ArrayCreationLevel> acl){
       return Objects.equals( this.astNode.getLevels(), acl);
    }

    public boolean equals(Object other){
        if( other instanceof _arrayCreate){
            return ((_arrayCreate)other).astNode.equals( this.astNode);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.astNode.hashCode();
    }

    public String toString(){
        return this.astNode.toString();
    }
}
