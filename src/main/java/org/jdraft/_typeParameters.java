package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author Eric
 */
public final class _typeParameters
        implements _nodeList<TypeParameter, _typeParameter, _typeParameters> {

    public static _typeParameters of(){
        return of( Ast.classDecl("class Dummy{}" ));
    }

    public static _typeParameters of( String...tps){
        String typeParams = Text.combine(tps);
        if( typeParams.length() == 0 ){
            return of();
        }
        if( !typeParams.startsWith("<") ){
            typeParams = "<"+ typeParams +">";
        }
        ClassOrInterfaceDeclaration coid = Ast.classDecl("class Dummy"+ typeParams +"{}");
        return of( coid );
    }

    public static _typeParameters of( NodeWithTypeParameters tps ) {
        return new _typeParameters( tps );
    }

    private final NodeWithTypeParameters astNodeWithTypeParams;

    public _typeParameters( NodeWithTypeParameters ntp ) {
        this.astNodeWithTypeParams = ntp;
    }

    /*
    public int indexOf( _typeParameter tp ) {
        return list().indexOf( tp );
    }

    public int indexOf( TypeParameter tp ) {
        return astNodeWithTypeParams.getTypeParameters().indexOf( tp );
    }
     */

    public _typeParameters clear() {
        astNodeWithTypeParams.getTypeParameters().clear();
        return this;
    }

    public List<_typeParameter> list() {
        List<_typeParameter> lp = new ArrayList<>();
        astNodeWithTypeParams.getTypeParameters().forEach( t -> lp.add( _typeParameter.of( (TypeParameter)t ) ) );
        return lp;
    }

    public List<_typeParameter> list( Predicate<_typeParameter> tps ) {
        return list().stream().filter( tps ).collect( Collectors.toList() );
    }

    /*
    public _typeParameters remove( _typeParameter... tps ) {
        Arrays.stream( tps ).forEach(t -> remove( t.ast() ) );
        return this;
    }

    public _typeParameters remove( TypeParameter... tps ) {
        Arrays.stream( tps ).forEach( t -> remove( t ) );
        return this;
    }
     */

    /*
    public _typeParameters remove( Predicate<? super _typeParameter> tps ) {
        remove( list( tps ) );
        return this;
    }
    */

    public _typeParameters remove( List<? super _typeParameter> tps ) {
        astNodeWithTypeParams.getTypeParameters().removeAll( tps );
        return this;
    }

    public NodeList<TypeParameter> ast() {
        return astNodeWithTypeParams.getTypeParameters();
    }

    //public int size() {
    //    return astNodeWithTypeParams.getTypeParameters().size();
    //}

    @Override
    public _typeParameters copy() {
        return _typeParameters.of(this.astNodeWithTypeParams);
    }

    @Override
    public List<TypeParameter> listAstElements() {
        return this.astNodeWithTypeParams.getTypeParameters();
    }

    public boolean is( String typeParameters ) {
        try {
            _typeParameters _tps = _typeParameters.of(typeParameters);
            return _tps.equals(this);
        }
        catch( Exception e ) {
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        Set<_typeParameter> _tps = new HashSet<>();
        this.astNodeWithTypeParams.getTypeParameters().forEach( t -> _tps.add( _typeParameter.of( (TypeParameter)t)) );
        hash = 29 * hash + Objects.hashCode( _tps );
        return hash;
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
        final _typeParameters other = (_typeParameters)obj;
        if( this.astNodeWithTypeParams.getTypeParameters().size() !=  other.astNodeWithTypeParams.getTypeParameters().size()){
            return false;
        }
        Set<_typeParameter> _tps = new HashSet<>();
        this.astNodeWithTypeParams.getTypeParameters().forEach( t -> _tps.add( _typeParameter.of( (TypeParameter)t) ) );

        for(int i=0; i<other.astNodeWithTypeParams.getTypeParameters().size(); i++){
            _typeParameter _tp = _typeParameter.of( other.astNodeWithTypeParams.getTypeParameter(i));
            if( ! _tps.contains(_tp) ){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if( this.astNodeWithTypeParams.getTypeParameters().isNonEmpty() ) {
            NodeList<TypeParameter> tps = this.ast();
            StringBuilder sb = new StringBuilder();
            sb.append("<");
            for(int i=0;i<tps.size(); i++ ){
                if( i > 0 ){
                    sb.append(",");
                }
                sb.append( tps.get(i) );
            }
            sb.append(">");
            return sb.toString();
            //return this.astNodeWithTypeParams.getTypeParameters().toString();
        }
        return "";
    }

    public NodeWithTypeParameters astHolder() {
        return this.astNodeWithTypeParams;
    }

    /*
    public void forEach( Consumer<? super _typeParameter> elementAction ) {
        list().forEach( elementAction );
    }

    public void forEach( Predicate<? super _typeParameter> matchFn,
                         Consumer<? super _typeParameter> elementAction ) {

        list( matchFn ).forEach( elementAction );
    }
    */
    /*
    public _typeParameters add( _typeParameter... elements ) {
        Arrays.stream( elements ).forEach( t -> this.astNodeWithTypeParams.addTypeParameter( t.ast() ) );
        return this;
    }

    public _typeParameters add( TypeParameter... nodes ) {
        Arrays.stream( nodes ).forEach( t -> this.astNodeWithTypeParams.addTypeParameter( t ) );
        return this;
    }
     */
}
