package org.jdraft;

import java.util.*;
import java.util.function.Function;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.Type;

/**
 * Representation of the source of a Java Receiver Parameter
 * i.e. <PRE>
 * public class Calculator {
 *
 *     public Result calc(@ServerObject Calculator this){
 *        this.getSomething();
 *         ...
 *     }
 *  }
 * </PRE>
 * NOTE: the purpose of the Receiver Parameter is to allow Type Annotations
 * to be applied to the instance parameter (here the instance this is annotated
 * with @ServerObject in the calc method via a ReceiverParameter)
 *
 * <A HREF="https://blog.joda.org/2015/12/explicit-receiver-parameters.html">About Explicit Receiver Parameters</A>
 *
 * NOTE: All ANNOTATIONS preceding the TYPE will be set on this object, not on the TYPE.
 * @author Eric
 */
public final class _receiverParam
        implements _tree._node<ReceiverParameter, _receiverParam>,
        _java._withNameType<ReceiverParameter, _receiverParam>, _annos._withAnnos<_receiverParam> {

    public static final Function<String, _receiverParam> PARSER = s-> _receiverParam.of(s);

    public static _receiverParam of(){
        return of( new ReceiverParameter());
    }

    public static _receiverParam of(String rp ) {
        return of( Ast.receiverParameter( rp ) );
    }

    public static _receiverParam of(ReceiverParameter rp ) {
        return new _receiverParam( rp );
    }

    public static _feature._one<_receiverParam, _annos> ANNOS = new _feature._one<>(_receiverParam.class, _annos.class,
            _feature._id.ANNOS,
            a -> a.getAnnos(),
            (_receiverParam p, _annos _e) -> p.setAnnos(_e), PARSER);

    public static _feature._one<_receiverParam, _typeRef> TYPE = new _feature._one<>(_receiverParam.class, _typeRef.class,
            _feature._id.TYPE,
            a -> a.getType(),
            (_receiverParam p, _typeRef _t) -> p.setType(_t), PARSER);

    public static _feature._one<_receiverParam, String> NAME = new _feature._one<>(_receiverParam.class, String.class,
            _feature._id.NAME,
            a -> a.getName(),
            (_receiverParam p, String s) -> p.setName(s), PARSER);

    public static _feature._features<_receiverParam> FEATURES = _feature._features.of(_receiverParam.class,  PARSER, ANNOS, TYPE, NAME );

    public ReceiverParameter node;

    public _receiverParam(ReceiverParameter rp ) {
        this.node = rp;
    }

    public _feature._features<_receiverParam> features(){
        return FEATURES;
    }

    /**
     * Build and return an independent copy of this receiverParameter
     * @return 
     */
    public _receiverParam copy(){
        return of( this.node.toString() );
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _receiverParam replace(ReceiverParameter replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is ( ReceiverParameter astRp ){
        return of( astRp ).equals( this );
    }


    @Override
    public ReceiverParameter node() {
        return this.node;
    }

    @Override
    public _receiverParam setName(String name ) {
        this.node.setName( name );
        return this;
    }

    public Name getNameNode() { return this.node.getName(); }

    @Override
    public String getName() {
        return this.node.getNameAsString();
    }

    @Override
    public _typeRef getType() {
        return _typeRef.of( this.node.getType() );
    }

    @Override
    public _receiverParam setType(Type astType ) {
        this.node.setType( astType );
        return this;
    }

    @Override
    public _annos getAnnos() {
        return _annos.of( this.node);
    }

    @Override
    public String toString() {
        return this.node.toString();
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
        final _receiverParam other = (_receiverParam)obj;
        if( this.node == other.node) {
            return true; //two _receiverParameter s pointing to the same ReceiverParameter
        }
        if( ! Expr.equalAnnos(this.node, other.node)){
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName() ) ) {
            return false;
        }
        if( ! Types.equal( node.getType(), other.node.getType())){
            return false;
        }
        return true;
    }

    /*
    public Map<_java.Feature, Object> features( ) {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put( _java.Feature.ANNO_EXPRS, getAnnoExprs() );
        parts.put( _java.Feature.TYPE, getTypeRef() );
        parts.put( _java.Feature.NAME, getName() );
        return parts;
    }
     */

    @Override
    public int hashCode() {
        int hash = 7;
        //hash = 97 * hash + Ast.annotationsHash( astReceiverParam ); //Objects.hashCode( this.getAnnos() );
        hash = 97 * hash + Expr.hashAnnos(node);
        hash = 97 * hash + Objects.hashCode( this.getName() );
        hash = 97 * hash + Types.hash( node.getType()); //Objects.hashCode( this.getType() );
        return hash;
    }

    /**
     * <A HREF="https://blog.joda.org/2015/12/explicit-receiver-parameters.html">
     * Receiver Parameters</A>
     * (A rarely used feature in Java METHODS and CONSTRUCTORS
     *
     * @author Eric
     * @param <_WRP>
     */
    public interface _withReceiverParam<_WRP extends _withReceiverParam>
            extends _java._domain {

        default boolean hasReceiverParam() {
            return getAstReceiverParam() != null;
        }

        default _receiverParam getReceiverParam() {
            ReceiverParameter astRp = getAstReceiverParam();
            if( astRp != null ){
                return _receiverParam.of(astRp);
            }
            return null;            
        }
        
        default ReceiverParameter getAstReceiverParam(){
            Node n = (Node) ((_tree._node)this).node();
            if( n instanceof MethodDeclaration ){
                MethodDeclaration md = (MethodDeclaration)n;
                if(md.getReceiverParameter().isPresent()){
                    return md.getReceiverParameter().get();
                }
                return null;
            }
            ConstructorDeclaration cd = (ConstructorDeclaration)n;
            if(cd.getReceiverParameter().isPresent()){
                return cd.getReceiverParameter().get();
            }
            return null;
        }
        
        default _WRP removeReceiverParam() {
            if( hasReceiverParam()){
                Node n = (Node) ((_tree._node)this).node();
                if( n instanceof MethodDeclaration ){
                    MethodDeclaration md = (MethodDeclaration)n;
                    md.removeReceiverParameter();
                } else{
                    ConstructorDeclaration cd = (ConstructorDeclaration)n;
                    cd.removeReceiverParameter();
                }
            }            
            return (_WRP)this;
        }

        default _WRP setReceiverParam(String receiverParam ) {
            return setReceiverParam( Ast.receiverParameter( receiverParam ) );
        }

        default _WRP setReceiverParam(_receiverParam _rp ) {
            return setReceiverParam( _rp.node() );
        }
        
        default _WRP setReceiverParam(ReceiverParameter rp ) {
            Node n = (Node) ((_tree._node)this).node();
            if( n instanceof MethodDeclaration ){
                MethodDeclaration md = (MethodDeclaration)n;
                md.setReceiverParameter(rp);
            } else{
                ConstructorDeclaration cd = (ConstructorDeclaration)n;
                cd.setReceiverParameter(rp);
            }
            return (_WRP) this;
        }    
    }    
}
