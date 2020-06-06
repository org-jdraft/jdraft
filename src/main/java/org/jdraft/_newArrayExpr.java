package org.jdraft;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.type.Type;
import org.jdraft.text.Text;

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
public final class _newArrayExpr implements _expr<ArrayCreationExpr, _newArrayExpr>,
        _java._node<ArrayCreationExpr, _newArrayExpr>,
        _typeRef._withType<ArrayCreationExpr, _newArrayExpr>,
        _java._list<ArrayCreationLevel, _arrayDimension, _newArrayExpr> {

    public static final Function<String, _newArrayExpr> PARSER = s-> _newArrayExpr.of(s);

    public static _newArrayExpr of( ){
        return new _newArrayExpr(new ArrayCreationExpr());
    }
    public static _newArrayExpr of(ArrayCreationExpr ac){
        return new _newArrayExpr(ac);
    }
    public static _newArrayExpr of(String...code){
        String str = Text.combine(code);
        if( !str.startsWith("new ") ){
            str = "new "+ str;
        }
        return new _newArrayExpr(Expr.arrayCreationExpr( str));
    }

    public static <A extends Object> _newArrayExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _newArrayExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _newArrayExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _newArrayExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _newArrayExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _newArrayExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _newArrayExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _newArrayExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _newArrayExpr from(LambdaExpr le){
        Optional<ArrayCreationExpr> ows = le.getBody().findFirst(ArrayCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No ArrayCreationExpr found in lambda");
    }

    public static _feature._one<_newArrayExpr, _typeRef> TYPE = new _feature._one<>(_newArrayExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> ((_newArrayExpr) a).getElementType(),
            (_newArrayExpr a, _typeRef _t) -> a.setElementType(_t), PARSER);

    public static _feature._many<_newArrayExpr, _arrayDimension> DIMENSIONS = new _feature._many<>(_newArrayExpr.class, _arrayDimension.class,
            _feature._id.ARRAY_DIMENSIONS,
            _feature._id.ARRAY_DIMENSION,
            a -> ((_newArrayExpr)a).list(),
            (_newArrayExpr a, List<_arrayDimension> _ads) -> a.setArrayDimensions(_ads), PARSER, s-> _arrayDimension.of(s))
            .setOrdered(true); /** the order of the dimension declarations matters { int[100][200] =/= int [200][100] } */

    public static _feature._one<_newArrayExpr, _arrayInitExpr> INIT = new _feature._one<>(_newArrayExpr.class, _arrayInitExpr.class,
            _feature._id.INIT,
            a -> a.getInit(),
            (_newArrayExpr a, _arrayInitExpr _t) -> a.setInit(_t), PARSER);

    public static _feature._features<_newArrayExpr> FEATURES = _feature._features.of(_newArrayExpr.class, TYPE, DIMENSIONS, INIT );

    public ArrayCreationExpr astNode;

    public _newArrayExpr(ArrayCreationExpr astNode){
        this.astNode = astNode;
    }

    @Override
    public _newArrayExpr copy() {
        return new _newArrayExpr(this.astNode.clone());
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

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        if( astNode.getInitializer().isPresent()) {
            comps.put(_java.Feature.INIT, astNode.getInitializer());
        }
        comps.put(_java.Feature.ARRAY_DIMENSIONS, astNode.getLevels());
        comps.put(_java.Feature.TYPE, astNode.getElementType());
        return comps;
    }
     */

    public _newArrayExpr setInit(String...init){
        this.astNode.setInitializer(Expr.arrayInitializerExpr(init));
        return this;
    }

    public _newArrayExpr setInit(_arrayInitExpr ae){
        this.astNode.setInitializer(ae.ast());
        return this;
    }

    public _newArrayExpr setInit(ArrayInitializerExpr ae){
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
            return isInit(Expr.arrayInitializerExpr(initCode));
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

    public _newArrayExpr setElementType(_typeRef _tr){
        this.astNode.setElementType(_tr.ast());
        return this;
    }

    public _newArrayExpr setElementType(Class clazz){
        this.astNode.setElementType(clazz);
        return this;
    }

    public _newArrayExpr setElementType(String str){
        this.astNode.setElementType(_typeRef.of(str).ast());
        return this;
    }

    public List<_arrayDimension> listArrayDimensions(){
        List<_arrayDimension> ads = new ArrayList<>();
        this.astNode.getLevels().forEach(d -> ads.add( _arrayDimension.of(d)));
        return ads;
    }

    public _newArrayExpr setArrayDimensions(List<_arrayDimension> ads){
        NodeList<ArrayCreationLevel> lvls = new NodeList<>();
        ads.forEach(ad -> lvls.add(ad.astNode));
        return setArrayDimensions(lvls);
    }

    public _newArrayExpr setArrayDimensions(String...code){
        this.astNode.setLevels( Expr.arrayCreationLevels(code) );
        return this;
    }

    public _newArrayExpr setArrayDimensions(NodeList<ArrayCreationLevel> acls){
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
        if( other instanceof _newArrayExpr){
            return ((_newArrayExpr)other).astNode.equals( this.astNode);
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
