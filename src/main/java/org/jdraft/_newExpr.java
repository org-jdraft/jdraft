package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 *
 */
public final class _newExpr implements _expr<ObjectCreationExpr, _newExpr>,
        _java._node<ObjectCreationExpr, _newExpr>,
        _typeRef._withTypeRef<ObjectCreationExpr, _newExpr>,
        _java._withScope<ObjectCreationExpr, _newExpr>,
        _args._withArgs<ObjectCreationExpr, _newExpr>,
        _typeArgs._withTypeArgs<ObjectCreationExpr, _newExpr> {

    public static final Function<String, _newExpr> PARSER = s-> _newExpr.of(s);

    public static _newExpr of(){
        return new _newExpr( new ObjectCreationExpr() );
    }

    public static _newExpr of(Class clazz){
        if( clazz.isPrimitive() ){
            throw new _jdraftException("cannot create a new primitive type of "+ clazz);
        }
        ObjectCreationExpr oce = new ObjectCreationExpr();
        oce.setType(clazz);
        return new _newExpr( oce);
    }
    public static _newExpr of(ObjectCreationExpr oce){
        return new _newExpr( oce );
    }
    public static _newExpr of(String...code){
        return new _newExpr(Exprs.newExpr(code));
    }

    public static <A extends Object> _newExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _newExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _newExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _newExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _newExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _newExpr of(Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _newExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _newExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _newExpr from(LambdaExpr le){
        Optional<ObjectCreationExpr> ows = le.getBody().findFirst(ObjectCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No new expression found in lambda");
    }

    public ObjectCreationExpr oce;

    public _newExpr(ObjectCreationExpr oce){
        this.oce = oce;
    }

    @Override
    public _newExpr copy() {
        return new _newExpr(this.oce.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Exprs.newExpr(stringRep));
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

    public static _feature._one<_newExpr, _expr> SCOPE = new _feature._one<>(_newExpr.class, _expr.class,
            _feature._id.SCOPE_EXPR,
            a -> a.getScope(),
            (_newExpr a, _expr _e) -> a.setScope(_e), PARSER);

    public static _feature._one<_newExpr, _typeArgs> TYPE_ARGS = new _feature._one<>(_newExpr.class, _typeArgs.class,
            _feature._id.TYPE_ARGS,
            a -> a.getTypeArgs(),
            (_newExpr a, _typeArgs _ta) -> a.setTypeArgs(_ta), PARSER);

    public static _feature._one<_newExpr, _typeRef> TYPE = new _feature._one<>(_newExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getTypeRef(),
            (_newExpr a, _typeRef _tr) -> a.setTypeRef(_tr), PARSER);

    public static _feature._one<_newExpr, _args> ARGS = new _feature._one<>(_newExpr.class, _args.class,
            _feature._id.ARGS_EXPRS,
            a -> a.getArgs(),
            (_newExpr a, _args _a) -> a.setArgs(_a), PARSER);

    public static _feature._many<_newExpr, _java._declared> ANONYMOUS_MEMBER_DECLARATIONS = new _feature._many<>(_newExpr.class, _java._declared.class,
            _feature._id.MEMBERS,
            _feature._id.MEMBER,
            a -> a.listAnonymousDeclarations(),
            (_newExpr a, List<_java._declared> _ms) -> a.setAnonymousDeclarations(_ms), PARSER);

    public static _feature._meta<_newExpr> META = _feature._meta.of(_newExpr.class, SCOPE, TYPE_ARGS, TYPE, ARGS, ANONYMOUS_MEMBER_DECLARATIONS );


    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();

        if( oce.getAnonymousClassBody().isPresent()){
            comps.put(_java.Feature.ANONYMOUS_CLASS_BODY, oce.getAnonymousClassBody().get());
        }
        comps.put(_java.Feature.ARGS_EXPRS, oce.getArguments());

        if( oce.getScope().isPresent() ) {
            comps.put(_java.Feature.SCOPE_EXPR, oce.getScope().get());
        }
        comps.put(_java.Feature.TYPE, oce.getType());

        if( oce.getTypeArguments().isPresent()) {
            comps.put(_java.Feature.TYPE_ARGS, oce.getTypeArguments().get());
        }
        return comps;
    }
     */

    /**
     * replaces all of the Anonymous Class body
     * @param _dcls
     * @return
     */
    public _newExpr setAnonymousDeclarations(List<_java._declared> _dcls){
        if( this.oce.getAnonymousClassBody().isPresent()){
            NodeList<BodyDeclaration<?>> b =  this.oce.getAnonymousClassBody().get();
            b.clear();

        }
        _dcls.forEach(d -> oce.addAnonymousClassBody( (BodyDeclaration)d.ast()));
        return this;
    }

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
    public _newExpr addAnonymousBodyDeclarations(_java._declared... _dec ){
        Arrays.stream(_dec).forEach( d -> this.oce.addAnonymousClassBody( (BodyDeclaration)d.ast() ) );
        return this;
    }

    /**
     * Add anonymous declarations to the new
     * @param dec
     * @return
     */
    public _newExpr addAnonymousBodyDeclarations(BodyDeclaration ... dec ){
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
    public _newExpr forAnonymousBodyDeclarations(Consumer<_java._declared> memberFn){
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
    public _newExpr forAnonymousBodyDeclarations(Predicate<_java._declared> memberMatchFn, Consumer<_java._declared> memberFn){
        if( this.oce.getAnonymousClassBody().isPresent() ){
            this.oce.getAnonymousClassBody().get().stream().map(m -> (_java._declared)_java.of(m))
                    .filter(memberMatchFn).forEach(m -> memberFn.accept(m));
        }
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _newExpr){
            return ((_newExpr)other).oce.equals( this.oce);
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
