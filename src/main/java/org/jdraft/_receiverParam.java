package org.jdraft;

import java.util.*;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.Type;
import org.jdraft.text.Text;

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
        implements _java._multiPart<ReceiverParameter, _receiverParam>,
        _java._withNameTypeRef<ReceiverParameter, _receiverParam>, _annoExprs._withAnnoExprs<_receiverParam> {

    public static _receiverParam of(){
        return of( new ReceiverParameter());
    }

    public static _receiverParam of(String rp ) {
        return of( Ast.receiverParameter( rp ) );
    }

    public static _receiverParam of(ReceiverParameter rp ) {
        return new _receiverParam( rp );
    }

    public final ReceiverParameter astReceiverParam;

    public _receiverParam(ReceiverParameter rp ) {
        this.astReceiverParam = rp;
    }

    /**
     * Build and return an independent copy of this receiverParameter
     * @return 
     */
    public _receiverParam copy(){
        return of( this.astReceiverParam.toString() );
    }
    
    @Override
    public boolean is( String... string ) {
        try {
            return of(Text.combine(string)).equals(this);
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean is ( ReceiverParameter astRp ){
        return of( astRp ).equals( this );
    }

    @Override
    public ReceiverParameter ast() {
        return this.astReceiverParam;
    }

    @Override
    public _receiverParam setName(String name ) {
        this.astReceiverParam.setName( name );
        return this;
    }

    public Name getNameNode() { return this.astReceiverParam.getName(); }

    @Override
    public String getName() {
        return this.astReceiverParam.getNameAsString();
    }

    @Override
    public _typeRef getTypeRef() {
        return _typeRef.of( this.astReceiverParam.getType() );
    }

    @Override
    public _receiverParam setTypeRef(Type astType ) {
        this.astReceiverParam.setType( astType );
        return this;
    }

    @Override
    public _annoExprs getAnnoExprs() {
        return _annoExprs.of( this.astReceiverParam );
    }

    @Override
    public String toString() {
        return this.astReceiverParam.toString();
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
        if( this.astReceiverParam == other.astReceiverParam ) {
            return true; //two _receiverParameter s pointing to the same ReceiverParameter
        }
        if( ! Exprs.equalAnnos(this.astReceiverParam, other.astReceiverParam)){
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName() ) ) {
            return false;
        }
        if( ! Types.equal( astReceiverParam.getType(), other.astReceiverParam.getType())){
            return false;
        }
        return true;
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.ANNOS, getAnnoExprs() );
        parts.put( _java.Component.TYPE, getTypeRef() );
        parts.put( _java.Component.NAME, getName() );
        return parts;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        //hash = 97 * hash + Ast.annotationsHash( astReceiverParam ); //Objects.hashCode( this.getAnnos() );
        hash = 97 * hash + Exprs.hashAnnos(astReceiverParam);
        hash = 97 * hash + Objects.hashCode( this.getName() );
        hash = 97 * hash + Types.hash( astReceiverParam.getType()); //Objects.hashCode( this.getType() );
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
            Node n = (Node) ((_java._multiPart)this).ast();
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
                Node n = (Node) ((_java._multiPart)this).ast();
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

        default _WRP receiverParam(String receiverParam ) {
            return receiverParam( Ast.receiverParameter( receiverParam ) );
        }

        default _WRP receiverParam(_receiverParam _rp ) {
            return receiverParam( _rp.ast() );
        }
        
        default _WRP receiverParam(ReceiverParameter rp ) {
            Node n = (Node) ((_java._multiPart)this).ast();
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
