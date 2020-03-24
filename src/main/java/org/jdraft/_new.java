package org.jdraft;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 *
 */
public class _new implements _expression<ObjectCreationExpr, _new>,
        _java._multiPart<ObjectCreationExpr, _new>,
        _typeRef._withTypeRef<ObjectCreationExpr, _new>,
        _java._withScope<ObjectCreationExpr, _new>,
        _arguments._withArguments<ObjectCreationExpr, _new>,
        _typeArguments._withTypeArguments<ObjectCreationExpr, _new> {

    public static _new of(){
        return new _new( new ObjectCreationExpr() );
    }
    public static _new of( Class clazz){
        if( clazz.isPrimitive() ){
            throw new _jdraftException("cannot create a new primitive type of "+ clazz);
        }
        ObjectCreationExpr oce = new ObjectCreationExpr();
        oce.setType(clazz);
        return new _new ( oce);
    }
    public static _new of(ObjectCreationExpr oce){
        return new _new( oce );
    }
    public static _new of( String...code){
        return new _new(Ex.newEx(code));
    }

    public static <A extends Object> _new of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _new of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _new of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _new of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _new of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _new of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _new of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _new of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _new from( LambdaExpr le){
        Optional<ObjectCreationExpr> ows = le.getBody().findFirst(ObjectCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No new expression found in lambda");
    }

    public ObjectCreationExpr oce;

    public _new(ObjectCreationExpr oce){
        this.oce = oce;
    }

    @Override
    public _new copy() {
        return new _new(this.oce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ex.newEx(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public ObjectCreationExpr ast(){
        return oce;
    }

    /**
     * is this a new
     * @return
     */
    public boolean isAnonymous(){
        return this.oce.getAnonymousClassBody().isPresent();
    }

    /*
    public _new setType(_typeRef _t){
        this.oce.setType((ClassOrInterfaceType)_t.ast());
        return this;
    }

    public _new setType(Class clazz){
        this.oce.setType(clazz);
        return this;
    }

    public _new setType(ClassOrInterfaceType ct){
        this.oce.setType(ct);
        return this;
    }

    public _new setType(String type){
        this.oce.setType( (ClassOrInterfaceType)Ast.typeRef(type));
        return this;
    }

    public _typeRef getType(){
        return _typeRef.of(this.oce.getType());
    }

    public boolean isType(ClassOrInterfaceType coit ){
        return Ast.typesEqual(this.oce.getType(), coit);
    }

    public boolean isType(Class clazz ){
        return Ast.typesEqual( this.oce.getType(), Ast.typeRef(clazz) );
    }

    public boolean isType(String type){
        return Ast.typesEqual( this.oce.getType(), Ast.typeRef(type) );
    }
    */

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();

        if( oce.getAnonymousClassBody().isPresent()){
            comps.put(_java.Component.ANONYMOUS_CLASS_BODY, oce.getAnonymousClassBody().get());
        }
        comps.put(_java.Component.ARGUMENTS, oce.getArguments());

        if( oce.getScope().isPresent() ) {
            comps.put(_java.Component.SCOPE, oce.getScope().get());
        }
        comps.put(_java.Component.TYPE, oce.getType());

        if( oce.getTypeArguments().isPresent()) {
            comps.put(_java.Component.TYPE_ARGUMENTS, oce.getTypeArguments().get());
        }
        return comps;
    }

    /*
    public boolean hasScope(){
        return this.oce.getScope().isPresent();
    }

    public boolean isScope(String...expr){
        if( this.oce.getScope().isPresent()){
            return Objects.equals( oce.getScope().get(), Ex.of(expr));
        }
        return false;
    }

    public boolean isScope(Expression e){
        if( this.oce.getScope().isPresent()){
            return Objects.equals( oce.getScope().get(), e);
        }
        return e == null;
    }

    public boolean isScope(_expression _e){
        if( this.oce.getScope().isPresent()){
            return Objects.equals( oce.getScope().get(), _e.ast());
        }
        return _e == null;
    }

    public _new removeScope(){
        this.oce.removeScope();
        return this;
    }

    public _new setScope( _expression _e){
        return setScope(_e.ast());
    }

    public _new setScope( Expression e){
        this.oce.setScope(e);
        return this;
    }

    public _new setScope(String... scope){
        return setScope( Ex.of(scope));
    }

    public _expression getScope(){
        if( oce.getScope().isPresent()){
            return _expression.of(this.oce.getScope().get());
        }
        return null;
    }
    */

    /*
    public _expression getArgument( int index){
        return _expression.of( this.oce.getArgument(index) );
    }

    public _new setArgument(int index, _expression _e){
        this.oce.getArguments().set(index, _e.ast());
        return this;
    }

    public _new setArgument(int index, Expression e){
        this.oce.getArguments().set(index, e);
        return this;
    }

    public _new setArguments(_expression ... _es){
        NodeList<Expression> nle = new NodeList<>();
        Arrays.stream(_es).forEach(n -> nle.add(n.ast()));
        this.oce.setArguments(nle);
        return this;
    }

    public _new setArguments(Expression ... es){
        NodeList<Expression> nle = new NodeList<>();
        Arrays.stream(es).forEach(n -> nle.add(n));
        this.oce.setArguments(nle);
        return this;
    }

    public List<_expression> listArguments(){
        List<_expression> args = new ArrayList<>();
        this.oce.getArguments().forEach(a -> args.add(_expression.of(a)));
        return args;
    }

    public List<_expression> listArguments(Predicate<_expression> matchFn){
        return listArguments().stream().filter(matchFn).collect(Collectors.toList());
    }

    public _new forArguments(Consumer<_expression> argFn){
        this.oce.getArguments().stream().map( a-> _expression.of(a)).forEach(e->  argFn.accept(e) );
        return this;
    }

    public _new forArguments(Predicate<_expression> expressionMatchFn, Consumer<_expression> argFn){
        this.oce.getArguments().stream().map( a-> _expression.of(a))
                .filter(expressionMatchFn).forEach(e->  argFn.accept(e) );
        return this;
    }
    */

    /**
     * Returns a list of declared entities defined in the anonymous body
     * (or an empty
     * <PRE>
     * new Object(){
     *     int i=0;     //anonymous body member
     *     void m(){ }  //anonymous body member
     * }
     * </PRE>
     * @return
     */
    public List<_java._declared> listAnonymousDeclarations(){
        List<_java._declared> ds =  new ArrayList<>();
        if( this.oce.getAnonymousClassBody().isPresent()){
            oce.getAnonymousClassBody().get().forEach(b -> ds.add((_java._declared)_java.of(b)));
        }
        return ds;
    }

    public List<_java._declared> listAnonymousDeclarations(Predicate<_java._declared> _matchFn){
        return listAnonymousDeclarations().stream().filter(_matchFn).collect(Collectors.toList());
    }


    /**
     *
     * @param _dec
     * @return
     */
    public _new addAnonymousBodyDeclarations( _java._declared... _dec ){
        Arrays.stream(_dec).forEach( d -> this.oce.addAnonymousClassBody( (BodyDeclaration)d.ast() ) );
        return this;
    }

    /**
     * Add anonymous declarations to the new
     * @param dec
     * @return
     */
    public _new addAnonymousBodyDeclarations( BodyDeclaration ... dec ){
        Arrays.stream(dec).forEach( d -> this.oce.addAnonymousClassBody( d ) );
        return this;
    }

    /**
     * <PRE>
     * new Object(){
     *     int i=0;     //anonymous body declaration
     * }
     * </PRE>
     * @param memberFn
     * @return
     */
    public _new forAnonymousBodyDeclarations(Consumer<_java._declared> memberFn){
        if( this.oce.getAnonymousClassBody().isPresent() ){
            this.oce.getAnonymousClassBody().get().stream().map(m -> (_java._declared)_java.of(m)).forEach(m -> memberFn.accept(m));
        }
        return this;
    }

    /**
     * <PRE>
     * new Object(){
     *     int i=0;     //anonymous body member
     *     void m(){ }  //anonymous body member
     * }
     * </PRE>
     * @param memberMatchFn
     * @param memberFn
     * @return
     */
    public _new forAnonymousBodyDeclarations(Predicate<_java._declared> memberMatchFn, Consumer<_java._declared> memberFn){
        if( this.oce.getAnonymousClassBody().isPresent() ){
            this.oce.getAnonymousClassBody().get().stream().map(m -> (_java._declared)_java.of(m))
                    .filter(memberMatchFn).forEach(m -> memberFn.accept(m));
        }
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _new){
            return ((_new)other).oce.equals( this.oce);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.oce.hashCode();
    }
    
    public String toString(){
        return this.oce.toString();
    }
}
