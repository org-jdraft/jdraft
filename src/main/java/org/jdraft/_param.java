package org.jdraft;

import java.util.*;
import java.util.function.Function;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;

/**
 * model of a parameter declaration (for {@link _method}s or {@link _constructor}s)
 *
 * @author Eric
 */
public final class _param
    implements _java._withNameType<Parameter, _param>, _annos._withAnnos<_param>,
        _modifiers._withFinal<_param>, _tree._node<Parameter, _param> {

    public static final Function<String, _param> PARSER = s-> _param.of(s);

    public static _param from (StackTraceElement ste ){
        _param _p = _lambdaExpr.from(ste).getParam(0);
        _p.node().remove();
        return of( _p.node() );
    }
    /**
     *
     * @param type
     * @param name
     * @return
     */
    public static _param of(Class type, String name ) {
        return of( new Parameter( Types.of( type ), name ) );
    }

    /**
     *
     * @param type
     * @param name
     * @return
     */
    public static _param of(Type type, String name ) {
        return of( new Parameter( type, name ) );
    }

    /**
     *
     * @param p
     * @return
     */
    public static _param of(Parameter p ) {
        return new _param( p );
    }

    public static _param of(){
        return of( new Parameter() );
    }
    /**
     *
     * @param parameter
     * @return
     */
    public static _param of(String parameter ) {
        return of( Ast.parameter( parameter ) );
    }


    public static _feature._one<_param, _annos> ANNOS = new _feature._one<>(_param.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_param p, _annos _ae) -> p.setAnnos(_ae), PARSER);

    public static _feature._one<_param, Boolean> IS_FINAL = new _feature._one<>(_param.class, Boolean.class,
            _feature._id.IS_FINAL,
            a -> a.isFinal(),
            (_param p, Boolean b) -> p.setFinal(b), PARSER);

    public static _feature._one<_param, _typeRef> TYPE = new _feature._one<>(_param.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_param p, _typeRef _tr) -> p.setType(_tr), PARSER);

    public static _feature._one<_param, Boolean> IS_VAR_ARG = new _feature._one<>(_param.class, Boolean.class,
            _feature._id.IS_VAR_ARG,
            a -> a.isVarArg(),
            (_param p, Boolean b) -> p.setVarArg(b), PARSER);

    public static _feature._one<_param, _annos> VAR_ARG_ANNO_EXPRS = new _feature._one<>(_param.class, _annos.class,
            _feature._id.VAR_ARG_ANNO_EXPRS,
            a -> a.getVarArgAnnoExprs(),
            (_param p, _annos _aes) -> p.setVarArgAnnoExprs(_aes), PARSER);

    public static _feature._one<_param, String> NAME = new _feature._one<>(_param.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_param p, String s) -> p.setName(s), PARSER);

    public static _feature._features<_param> FEATURES = _feature._features.of(_param.class,  PARSER, ANNOS, IS_FINAL, TYPE, IS_VAR_ARG, VAR_ARG_ANNO_EXPRS, NAME)
            .setStrictlyOrdered(false);

    private Parameter node;

    public _param(Parameter p ) {
        this.node = p;
    }

    public _feature._features<_param> features(){
        return FEATURES;
    }

    @Override
    public Parameter node() {
        return this.node;
    }

    @Override
    public _param copy(){
        return of( this.node.toString());
    }

    public SimpleName getNameNode() { return this.node.getName(); }

    @Override
    public _param setName(String name ) {
        this.node.setName( name );
        return this;
    }

    /**
     *
     * @return
     */
    public _annos getVarArgAnnoExprs(){
        return _annos.of(this.node.getVarArgsAnnotations());
    }

    public _param setVarArgAnnoExprs( _annos _aes){
        this.node.getVarArgsAnnotations().clear();
        _aes.toEach(a-> this.node.getVarArgsAnnotations().add(a.node()) );
        return this;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _param replace(Parameter replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public String getName() {
        return this.node.getNameAsString();
    }

    @Override
    public _annos getAnnos() {
        return _annos.of( this.node);
    }

    public _param setVarArg(boolean b ){
        this.node.setVarArgs(b);
        return this;
    }

    public boolean isVarArg() {
        return this.node.isVarArgs();
    }

    @Override
    public boolean isFinal() {
        return this.node.isFinal();
    }

    @Override
    public boolean isType(String type ) {
        return Types.equal(this.node.getType(), Types.of( type ));
    }

    @Override
    public boolean isType(Type type ) {
        return Types.equal(this.node.getType(), type);
    }

    @Override
    public boolean isNamed( String name ) {
        return this.node.getNameAsString().equals( name );
    }

    @Override
    public boolean is( Parameter astParam ){
        try {
            return equals( _param.of( astParam ) );
        }
        catch( Exception e ) {
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash +
                Objects.hash( 
                        Expr.hashAnnos(node),
                        this.getName(),
                        Types.hash(node.getType()),
                        this.isVarArg(),
                        this.isFinal() );
        return hash;
    }

    /**
     *
     * @param left
     * @param right
     * @return
     */
    public static boolean isEqual( Parameter left, Parameter right ) {
        if( left == right ) {
            return true;
        }
        if( left == null ) {
            return false;
        }
        if( right == null ) {
            return false;
        }
        if( !left.getNameAsString().equals( right.getNameAsString())){
            return false;
        }
        if( left.isVarArgs() != right.isVarArgs()){
            return false;
        }
        if( left.isFinal() != right.isFinal()){
            return false;
        }
        if( ! Expr.equalAnnos(left, right)){
            return false;
        }
        if( ! Types.equal(left.getType(), right.getType())){
            return false;
        }
        return true;
    }

    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) {
            return true;
        }
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final _param other = (_param)obj;
        if( !Objects.equals( this.node.getName(), other.node.getName() )){
            return false;
        }
        if( !Objects.equals( this.node.isFinal(), other.node.isFinal()) ){
            return false;
        }
        if( !Objects.equals( this.node.isVarArgs(), other.node.isVarArgs() )){
            return false;
        }
        if( !Types.equal(node.getType(), other.node.getType())){
            return false;
        }
        if( !Expr.equalAnnos(node, other.node)){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.node.toString();
    }

    @Override
    public _param setFinal(boolean toSet ) {
        this.node.setFinal( toSet );
        return this;
    }

}
