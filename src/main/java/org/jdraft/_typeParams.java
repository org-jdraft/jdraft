package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author Eric
 */
public final class _typeParams
        implements _java._set<TypeParameter, _typeParam, _typeParams> {

    public static _typeParams of(){
        return of( (ClassOrInterfaceDeclaration)Ast.typeDecl("class Dummy{}" ));
    }

    public static _typeParams of(String...tps){
        String typeParams = Text.combine(tps);
        if( typeParams.length() == 0 ){
            return of();
        }
        if( !typeParams.startsWith("<") ){
            typeParams = "<"+ typeParams +">";
        }
        ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)Ast.typeDecl("class Dummy"+ typeParams +"{}");
        return of( coid );
    }

    public static _typeParams of(NodeWithTypeParameters tps ) {
        return new _typeParams( tps );
    }

    private final NodeWithTypeParameters astNodeWithTypeParams;

    public _typeParams(NodeWithTypeParameters ntp ) {
        this.astNodeWithTypeParams = ntp;
    }

    public _typeParams clear() {
        astNodeWithTypeParams.getTypeParameters().clear();
        return this;
    }

    public List<_typeParam> list() {
        List<_typeParam> lp = new ArrayList<>();
        astNodeWithTypeParams.getTypeParameters().forEach( t -> lp.add( _typeParam.of( (TypeParameter)t ) ) );
        return lp;
    }

    public List<_typeParam> list(Predicate<_typeParam> tps ) {
        return list().stream().filter( tps ).collect( Collectors.toList() );
    }

    public _typeParams remove(List<? super _typeParam> tps ) {
        astNodeWithTypeParams.getTypeParameters().removeAll( tps );
        return this;
    }

    public NodeList<TypeParameter> ast() {
        return astNodeWithTypeParams.getTypeParameters();
    }

    @Override
    public _typeParams copy() {
        return _typeParams.of(this.astNodeWithTypeParams);
    }

    @Override
    public NodeList<TypeParameter> listAstElements() {
        return this.astNodeWithTypeParams.getTypeParameters();
    }

    public boolean is( String typeParameters ) {
        try {
            _typeParams _tps = _typeParams.of(typeParameters);
            return _tps.equals(this);
        }
        catch( Exception e ) {
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        Set<_typeParam> _tps = new HashSet<>();
        this.astNodeWithTypeParams.getTypeParameters().forEach( t -> _tps.add( _typeParam.of( (TypeParameter)t)) );
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
        final _typeParams other = (_typeParams)obj;
        if( this.astNodeWithTypeParams.getTypeParameters().size() !=  other.astNodeWithTypeParams.getTypeParameters().size()){
            return false;
        }
        Set<_typeParam> _tps = new HashSet<>();
        this.astNodeWithTypeParams.getTypeParameters().forEach( t -> _tps.add( _typeParam.of( (TypeParameter)t) ) );

        for(int i=0; i<other.astNodeWithTypeParams.getTypeParameters().size(); i++){
            _typeParam _tp = _typeParam.of( other.astNodeWithTypeParams.getTypeParameter(i));
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
        }
        return "";
    }

    public NodeWithTypeParameters astHolder() {
        return this.astNodeWithTypeParams;
    }

    /**
     *
     * @author Eric
     * @param <_WTP>
     */
    public interface _withTypeParams<_WTP extends _withTypeParams>
        extends _java._domain {

        /**
         * Check if all individual ({@link _typeParam}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allTypeParams(Predicate<_typeParam> matchFn){
            return getTypeParams().list().stream().allMatch(matchFn);
        }

        default _typeParams getTypeParams(){
            _java._declared _m = (_java._declared) this;
            return of( (NodeWithTypeParameters)_m.ast() );
        }

        default _typeParam getTypeParam(Predicate<_typeParam> _typeParameterMatchFn){
            List<_typeParam> tps = getTypeParams().list(_typeParameterMatchFn);
            if( tps.isEmpty() ){
                return null;
            }
            return tps.get(0);
        }

        /* return a list of AST typeParameters */
        default NodeList<TypeParameter> listAstTypeParameters() {
            _java._declared _m = (_java._declared) this;
            return ((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        /**
         * parse the string as TypeParameters and set the typeParameters
         * @param typeParameters string of typeParameters
         * @return the modified T
         */
        default _WTP typeParams(String typeParameters){
            _typeParams _tps = of(typeParameters);
            return typeParams( _tps) ; //((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        /**
         * @param typeParameters Strings that represent individual TypeParameters
         * @return the modified T
         */
        default _WTP typeParams(String... typeParameters){
            _typeParams _tps = of(typeParameters);
            return typeParams( _tps) ; //((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        default _WTP typeParams(_typeParams _tps){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.ast()).setTypeParameters(_tps.ast());
            return (_WTP)this;
        }

        default _WTP typeParams(NodeList<TypeParameter> astTypeParams){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.ast()).setTypeParameters(astTypeParams);
            return (_WTP)this;
        }

        default _WTP removeTypeParam(TypeParameter tp){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.ast()).getTypeParameters().remove(tp);
            return (_WTP)this;
        }

        /**
         * remove all typeParameters from the entity
         * @return
         */
        default _WTP removeTypeParams(){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.ast()).getTypeParameters().clear();
            return (_WTP)this;
        }

        /** does this have non empty type parameters */
        default boolean hasTypeParams(){
            _java._declared _m = (_java._declared) this;
            return ((NodeWithTypeParameters)_m.ast()).getTypeParameters().isNonEmpty();
        }
    }
}