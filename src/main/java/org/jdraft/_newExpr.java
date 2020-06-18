package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 *
 */
public final class _newExpr implements _expr<ObjectCreationExpr, _newExpr>,
        _tree._node<ObjectCreationExpr, _newExpr>,
        _typeRef._withType<ObjectCreationExpr, _newExpr>,
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
        return new _newExpr(Expr.newExpr(code));
    }

    public static <A extends Object> _newExpr of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _newExpr of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _newExpr of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _newExpr of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _newExpr of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _newExpr of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _newExpr of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _newExpr of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _newExpr from(LambdaExpr le){
        Optional<ObjectCreationExpr> ows = le.getBody().findFirst(ObjectCreationExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No new expression found in lambda");
    }

    public ObjectCreationExpr node;

    public _newExpr(ObjectCreationExpr node){
        this.node = node;
    }

    public _feature._features<_newExpr> features(){
        return FEATURES;
    }

    @Override
    public _newExpr copy() {
        return new _newExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _newExpr replace(ObjectCreationExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public ObjectCreationExpr node(){
        return node;
    }

    /**
     * is this a new
     * @return
     */
    public boolean isAnonymous(){
        return this.node.getAnonymousClassBody().isPresent();
    }

    public static _feature._one<_newExpr, _expr> SCOPE = new _feature._one<>(_newExpr.class, _expr.class,
            _feature._id.SCOPE,
            a -> a.getScope(),
            (_newExpr a, _expr _e) -> a.setScope(_e), PARSER);

    public static _feature._one<_newExpr, _typeArgs> TYPE_ARGS = new _feature._one<>(_newExpr.class, _typeArgs.class,
            _feature._id.TYPE_ARGS,
            a -> a.getTypeArgs(),
            (_newExpr a, _typeArgs _ta) -> a.setTypeArgs(_ta), PARSER);

    public static _feature._one<_newExpr, _typeRef> TYPE = new _feature._one<>(_newExpr.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_newExpr a, _typeRef _tr) -> a.setType(_tr), PARSER);

    public static _feature._one<_newExpr, _args> ARGS = new _feature._one<>(_newExpr.class, _args.class,
            _feature._id.ARGS,
            a -> a.getArgs(),
            (_newExpr a, _args _a) -> a.setArgs(_a), PARSER);

    public static _feature._many<_newExpr, _java._declared> ANONYMOUS_BODY_MEMBERS = new _feature._many<>(_newExpr.class, _java._declared.class,
            _feature._id.ANONYMOUS_BODY_MEMBERS,
            _feature._id.MEMBER,
            a -> a.listAnonymousBodyMembers(),
            (_newExpr a, List<_java._declared> _ms) -> a.setAnonymousBodyMembers(_ms), PARSER, s-> (_java._declared)_java._member.of(_class.class, s))
            .featureImplementations(_method.class, _field.class)
            .setOrdered(false);

    public static _feature._features<_newExpr> FEATURES = _feature._features.of(_newExpr.class,  PARSER, SCOPE, TYPE_ARGS, TYPE, ARGS, ANONYMOUS_BODY_MEMBERS);

    /**
     * replaces all of the Anonymous Class body
     * @param _dcls
     * @return
     */
    public _newExpr setAnonymousBodyMembers(List<_java._declared> _dcls){
        if( this.node.getAnonymousClassBody().isPresent()){
            NodeList<BodyDeclaration<?>> b =  this.node.getAnonymousClassBody().get();
            b.clear();

        }
        _dcls.forEach(d -> node.addAnonymousClassBody( (BodyDeclaration)d.node()));
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
    public List<_java._declared> listAnonymousBodyMembers(){
        List<_java._declared> ds =  new ArrayList<>();
        if( this.node.getAnonymousClassBody().isPresent()){
            node.getAnonymousClassBody().get().forEach(b -> {
                if( b instanceof FieldDeclaration && b.asFieldDeclaration().getVariables().size() > 1){
                    FieldDeclaration fd = b.asFieldDeclaration();
                    fd.getVariables().forEach(f-> ds.add( _field.of( f)));
                } else {
                    ds.add((_java._declared) _java.of(b));
                }
            });
        }
        return ds;
    }

    public List<_java._declared> listAnonymousBodyMembers(Predicate<_java._declared> _matchFn){
        return listAnonymousBodyMembers().stream().filter(_matchFn).collect(Collectors.toList());
    }

    /**
     *
     * @param _dec
     * @return
     */
    public _newExpr addAnonymousBodyMembers(_java._declared... _dec ){
        Arrays.stream(_dec).forEach( d -> this.node.addAnonymousClassBody( (BodyDeclaration)d.node() ) );
        return this;
    }

    /**
     * Add anonymous declarations to the new
     * @param dec
     * @return
     */
    public _newExpr addAnonymousBodyMembers(BodyDeclaration ... dec ){
        Arrays.stream(dec).forEach( d -> this.node.addAnonymousClassBody( d ) );
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
    public _newExpr forAnonymousBodyMembers(Consumer<_java._declared> memberFn){
        if( this.node.getAnonymousClassBody().isPresent() ){
            this.node.getAnonymousClassBody().get().stream().map(m -> (_java._declared)_java.of(m)).forEach(m -> memberFn.accept(m));
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
    public _newExpr forAnonymousBodyMembers(Predicate<_java._declared> memberMatchFn, Consumer<_java._declared> memberFn){
        if( this.node.getAnonymousClassBody().isPresent() ){
            this.node.getAnonymousClassBody().get().stream().map(m -> (_java._declared)_java.of(m))
                    .filter(memberMatchFn).forEach(m -> memberFn.accept(m));
        }
        return this;
    }

    public boolean equals(Object other){
        if( other instanceof _newExpr){
            return ((_newExpr)other).node.equals( this.node);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }
    
    public String toString(){
        return this.node.toString();
    }
}
