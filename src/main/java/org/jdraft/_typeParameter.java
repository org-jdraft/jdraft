package org.jdraft;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;

import org.jdraft._anno._annos;

/**
 * Model of a Java TypeParameter
 *
 * @author Eric
 */
public final class _typeParameter
        implements _node<TypeParameter, _typeParameter>, 
        _named<_typeParameter>, _anno._hasAnnos<_typeParameter> {

    public static _typeParameter of( String typeParam ) {
        return of( Ast.typeParameter( typeParam ) );
    }

    public static _typeParameter of( TypeParameter typeParameter ) {
        return new _typeParameter( typeParameter );
    }

    private final TypeParameter typeParameter;

    /**
     * build and return an independent copy of this model
     * @return 
     */
    public _typeParameter copy(){
        return of( this.typeParameter.toString() );
    }
    
    /**
     *
     * @return
     */
    public NodeList<ClassOrInterfaceType> getTypeBound() {
        return typeParameter.getTypeBound();
    }

    @Override
    public TypeParameter ast() {        
        return this.typeParameter;
    }

    public _typeParameter( TypeParameter tp ) {
        this.typeParameter = tp;
    }

    @Override
    public boolean is( String... typeParameterDecl ){
        try{
            return of( Text.combine(typeParameterDecl) ).equals(this);
        }
        catch( Exception e) {
            return false;
        }
    }

    @Override
    public boolean is( TypeParameter typeParameterDecl ){
        try{
            return of( typeParameterDecl ).equals(this);
        }
        catch( Exception e) {
            return false;
        }
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
        final _typeParameter other = (_typeParameter)obj;

        if( Objects.equals(this.typeParameter, other.typeParameter)){
            return true;
        }
        List<String>ttp = Ast.normalizeTypeParameter( this.typeParameter);
        List<String>otp = Ast.normalizeTypeParameter( other.typeParameter);
        return Objects.equals( ttp, otp );
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.TYPE_PARAMETER, this.typeParameter );
        return parts;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( Ast.normalizeTypeParameter(this.typeParameter) );
    }

    @Override
    public String toString() {
        //JavaParser already does a great job, no need to interfere
        return typeParameter.toString();
    }

    @Override
    public _typeParameter name(String name) {
        this.typeParameter.setName(name);
        return this;
    }

    @Override
    public String getName() {
        return typeParameter.getNameAsString();
    }

    @Override
    public _annos getAnnos() {
        return _annos.of( this.typeParameter);
    }

    /**
     *
     * @author Eric
     */
    public static final class _typeParameters
        implements _java {

        public static _typeParameters of(){
            return of( Ast.classDecl("class Dummy{}" ));
        }
        
        public static _typeParameters of( String...tps){
            String typeParams = Text.combine(tps);
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

        public int indexOf( _typeParameter tp ) {
            return list().indexOf( tp );
        }

        public int indexOf( TypeParameter tp ) {
            return astNodeWithTypeParams.getTypeParameters().indexOf( tp );
        }

        public _typeParameters clear() {
            astNodeWithTypeParams.getTypeParameters().clear();
            return this;
        }

        public List<_typeParameter> list() {
            List<_typeParameter> lp = new ArrayList<>();
            astNodeWithTypeParams.getTypeParameters().forEach( t -> lp.add( _typeParameter.of( (TypeParameter)t ) ) );
            return lp;
        }

        public List<_typeParameter> list( Predicate<? super _typeParameter> tps ) {
            return list().stream().filter( tps ).collect( Collectors.toList() );
        }

        public _typeParameters remove( _typeParameter... tps ) {
            Arrays.stream( tps ).forEach( t -> remove( t.ast() ) );
            return this;
        }

        public _typeParameters remove( TypeParameter... tps ) {
            Arrays.stream( tps ).forEach( t -> remove( t ) );
            return this;
        }

        public _typeParameters remove( Predicate<? super _typeParameter> tps ) {
            remove( list( tps ) );
            return this;
        }

        public _typeParameters remove( List<? super _typeParameter> tps ) {
            astNodeWithTypeParams.getTypeParameters().removeAll( tps );
            return this;
        }

        public NodeList<TypeParameter> ast() {
            return astNodeWithTypeParams.getTypeParameters();
        }

        public int size() {
            return astNodeWithTypeParams.getTypeParameters().size();
        }

        public boolean isEmpty() {
            return this.astNodeWithTypeParams.getTypeParameters().isEmpty();
        }

        public _typeParameter get( int index ) {
            return _typeParameter.of( (TypeParameter)this.astNodeWithTypeParams.getTypeParameters().get( index ) );
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

        public void forEach( Consumer<? super _typeParameter> elementAction ) {
            list().forEach( elementAction );
        }

        public void forEach( Predicate<? super _typeParameter> matchFn,
                             Consumer<? super _typeParameter> elementAction ) {

            list( matchFn ).forEach( elementAction );
        }

        public _typeParameters add( _typeParameter... elements ) {
            Arrays.stream( elements ).forEach( t -> this.astNodeWithTypeParams.addTypeParameter( t.ast() ) );
            return this;
        }

        public _typeParameters add( TypeParameter... nodes ) {
            Arrays.stream( nodes ).forEach( t -> this.astNodeWithTypeParams.addTypeParameter( t ) );
            return this;
        }
    }
    
    /**
     *
     * @author Eric
     * @param <_HTP>
     */
    public interface _hasTypeParameters<_HTP extends _hasTypeParameters>
        extends _java {
        
        default _typeParameters getTypeParameters(){
            _member _m = (_member) this;
            return _typeParameters.of( (NodeWithTypeParameters)_m.ast() );
        }

        /* return a list of AST typeParameters */
        default NodeList<TypeParameter> listAstTypeParameters() {
            _member _m = (_member) this;
            return ((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }
        
        /**
         * parse the string as TypeParameters and set the typeParameters
         * @param typeParameters string of typeParameters
         * @return the modified T
         */
        default _HTP typeParameters(String typeParameters ){
            _typeParameters _tps = _typeParameters.of(typeParameters);
            return typeParameters( _tps) ; //((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        /**
         * @param typeParameters Strings that represent individual TypeParameters
         * @return the modified T
         */
        default _HTP typeParameters(String... typeParameters ){
            _typeParameters _tps = _typeParameters.of(typeParameters);
            return typeParameters( _tps) ; //((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        default _HTP typeParameters(_typeParameters _tps ){
            _member _m = (_member) this;
            ((NodeWithTypeParameters)_m.ast()).setTypeParameters(_tps.ast());            
            return (_HTP)this;
        }

        default _HTP typeParameters(NodeList<TypeParameter> astTypeParams ){
            _member _m = (_member) this;
            ((NodeWithTypeParameters)_m.ast()).setTypeParameters(astTypeParams);            
            return (_HTP)this;
        }

        default _HTP removeTypeParameter(TypeParameter tp ){
            _member _m = (_member) this;
            ((NodeWithTypeParameters)_m.ast()).getTypeParameters().remove(tp);   
            return (_HTP)this;
        }
        
        /**
         * remove all typeParametersfrom the entity
         * @return 
         */
        default _HTP removeTypeParameters(){
            _member _m = (_member) this;
            ((NodeWithTypeParameters)_m.ast()).getTypeParameters().clear();   
            return (_HTP)this;
        }

        /** does this have non empty type parameters */
        default boolean hasTypeParameters(){
            _member _m = (_member) this;
            return ((NodeWithTypeParameters)_m.ast()).getTypeParameters().isNonEmpty();
        }
    }    
}
