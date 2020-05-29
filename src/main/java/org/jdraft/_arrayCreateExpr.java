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

/**
 *  <code>new int[5][4][][]</code> or <code>new int[][]{{1},{2,3}}</code>.
 *
 *  <br/>"int" is the element type.
 *  <br/>All the brackets are stored in the levels field, from left to right.
 */
public final class _arrayCreateExpr implements _expr<ArrayCreationExpr, _arrayCreateExpr>,
        _java._node<ArrayCreationExpr, _arrayCreateExpr>,
        _java._list<ArrayCreationLevel, _arrayDimension, _arrayCreateExpr> {

    public static final Function<String, _arrayCreateExpr> PARSER = s-> _arrayCreateExpr.of(s);

    public static _arrayCreateExpr of( ){
        return new _arrayCreateExpr(new ArrayCreationExpr());
    }
    public static _arrayCreateExpr of(ArrayCreationExpr ac){
        return new _arrayCreateExpr(ac);
    }
    public static _arrayCreateExpr of(String...code){
        return new _arrayCreateExpr(Expr.arrayCreationExpr( code));
    }


    public static <A extends Object> _arrayCreateExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _arrayCreateExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _arrayCreateExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayCreateExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayCreateExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _arrayCreateExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _arrayCreateExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _arrayCreateExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _arrayCreateExpr from(LambdaExpr le){
        Optional<ArrayCreationExpr> ows = le.getBody().findFirst(ArrayCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No ArrayCreationExpr found in lambda");
    }

    public static _feature._one<_arrayCreateExpr, _typeRef> TYPE = new _feature._one<>(_arrayCreateExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> ((_arrayCreateExpr) a).getElementType(),
            (_arrayCreateExpr a, _typeRef _t) -> a.setElementType(_t), PARSER);


    public static _feature._many<_arrayCreateExpr, _arrayDimension> DIMENSIONS = new _feature._many<>(_arrayCreateExpr.class, _arrayDimension.class,
            _feature._id.ARRAY_DIMENSIONS,
            _feature._id.ARRAY_DIMENSION,
            a -> ((_arrayCreateExpr)a).list(),
            (_arrayCreateExpr a, List<_arrayDimension> _ads) -> a.setArrayDimensions(_ads), PARSER, s-> _arrayDimension.of(s));

    public static _feature._one<_arrayCreateExpr, _arrayInitExpr> INIT = new _feature._one<>(_arrayCreateExpr.class, _arrayInitExpr.class,
            _feature._id.INIT,
            a -> a.getInit(),
            (_arrayCreateExpr a, _arrayInitExpr _t) -> a.setInit(_t), PARSER);

    public static _feature._meta<_arrayCreateExpr> META = _feature._meta.of(_arrayCreateExpr.class, TYPE, DIMENSIONS, INIT );

    public ArrayCreationExpr astNode;

    public _arrayCreateExpr(ArrayCreationExpr astNode){
        this.astNode = astNode;
    }

    @Override
    public _arrayCreateExpr copy() {
        return new _arrayCreateExpr(this.astNode.clone());
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
            return is( Expr.arrayCreationExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ArrayCreationExpr ast(){
        return astNode;
    }

    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        if( astNode.getInitializer().isPresent()) {
            comps.put(_java.Feature.INIT, astNode.getInitializer());
        }
        comps.put(_java.Feature.ARRAY_DIMENSIONS, astNode.getLevels());
        comps.put(_java.Feature.TYPE, astNode.getElementType());
        return comps;
    }

    public _arrayCreateExpr setInit(String...init){
        this.astNode.setInitializer(Expr.arrayInitExpr(init));
        return this;
    }

    public _arrayCreateExpr setInit(_arrayInitExpr ae){
        this.astNode.setInitializer(ae.ast());
        return this;
    }

    public _arrayCreateExpr setInit(ArrayInitializerExpr ae){
        this.astNode.setInitializer(ae);
        return this;
    }

    public boolean isElementType(String typeRef){
        return Types.equal( this.astNode.getElementType(), _typeRef.of(typeRef).ast());
    }

    public boolean isElementType( _typeRef _t ){
        return Types.equal( this.astNode.getElementType(), _t.ast());
    }

    public boolean isElementType(Type t){
        return Types.equal( this.astNode.getElementType(),t);
    }

    public boolean hasInit(){
        return this.astNode.getInitializer().isPresent();
    }

    public boolean isInit(String... initCode){
        try{
            return isInit(Expr.arrayInitExpr(initCode));
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
    public boolean isInit(_arrayInitExpr _ai){
        if( this.astNode.getInitializer().isPresent()) {
            return Objects.equals(this.astNode.getInitializer().get(), _ai.ast());
        }
        return _ai == null;
    }

    public _arrayInitExpr getInit(){
        if( this.astNode.getInitializer().isPresent()) {
            return _arrayInitExpr.of(this.astNode.getInitializer().get());
        }
        return null;
    }

    public _typeRef getElementType(){
        return _typeRef.of(this.astNode.getElementType());
    }

    public _arrayCreateExpr setElementType(_typeRef _tr){
        this.astNode.setElementType(_tr.ast());
        return this;
    }

    public _arrayCreateExpr setElementType(Class clazz){
        this.astNode.setElementType(clazz);
        return this;
    }

    public _arrayCreateExpr setElementType(String str){
        this.astNode.setElementType(_typeRef.of(str).ast());
        return this;
    }

    public List<_arrayDimension> listArrayDimensions(){
        List<_arrayDimension> ads = new ArrayList<>();
        this.astNode.getLevels().forEach(d -> ads.add( _arrayDimension.of(d)));
        return ads;
    }

    public _arrayCreateExpr setArrayDimensions(List<_arrayDimension> ads){
        NodeList<ArrayCreationLevel> lvls = new NodeList<>();
        ads.forEach(ad -> lvls.add(ad.astNode));
        return setArrayDimensions(lvls);
    }

    public _arrayCreateExpr setArrayDimensions(String...code){
        this.astNode.setLevels( Expr.arrayCreationLevels(code) );
        return this;
    }

    public _arrayCreateExpr setArrayDimensions(NodeList<ArrayCreationLevel> acls){
        this.astNode.setLevels( acls );
        return this;
    }

    public boolean isArrayDimensions(String... dimensions){
        try {
            return isArrayDimensions( Expr.arrayCreationLevels(dimensions));
        } catch(Exception e){
            return false;
        }
    }

    public boolean isArrayDimensions(NodeList<ArrayCreationLevel> acl){
       return Objects.equals( this.astNode.getLevels(), acl);
    }

    public boolean equals(Object other){
        if( other instanceof _arrayCreateExpr){
            return ((_arrayCreateExpr)other).astNode.equals( this.astNode);
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
