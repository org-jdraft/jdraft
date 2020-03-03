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
public final class _typeParameters
        implements _java._set<TypeParameter, _typeParameter, _typeParameters> {

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

    public _typeParameters remove( List<? super _typeParameter> tps ) {
        astNodeWithTypeParams.getTypeParameters().removeAll( tps );
        return this;
    }

    public NodeList<TypeParameter> ast() {
        return astNodeWithTypeParams.getTypeParameters();
    }

    @Override
    public _typeParameters copy() {
        return _typeParameters.of(this.astNodeWithTypeParams);
    }

    @Override
    public NodeList<TypeParameter> listAstElements() {
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

    /**
     *
     * @author Eric
     * @param <_WTP>
     */
    public interface _withTypeParameters<_WTP extends _withTypeParameters>
        extends _java._domain {

        /**
         * Check if all individual ({@link _typeParameter}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allTypeParameters( Predicate<_typeParameter> matchFn){
            return getTypeParameters().list().stream().allMatch(matchFn);
        }

        default _typeParameters getTypeParameters(){
            _java._declaredBodyPart _m = (_java._declaredBodyPart) this;
            return of( (NodeWithTypeParameters)_m.ast() );
        }

        default _typeParameter getTypeParameter(Predicate<_typeParameter> _typeParameterMatchFn){
            List<_typeParameter> tps = getTypeParameters().list(_typeParameterMatchFn);
            if( tps.isEmpty() ){
                return null;
            }
            return tps.get(0);
        }

        /* return a list of AST typeParameters */
        default NodeList<TypeParameter> listAstTypeParameters() {
            _java._declaredBodyPart _m = (_java._declaredBodyPart) this;
            return ((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        /**
         * parse the string as TypeParameters and set the typeParameters
         * @param typeParameters string of typeParameters
         * @return the modified T
         */
        default _WTP typeParameters(String typeParameters){
            _typeParameters _tps = of(typeParameters);
            return typeParameters( _tps) ; //((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        /**
         * @param typeParameters Strings that represent individual TypeParameters
         * @return the modified T
         */
        default _WTP typeParameters(String... typeParameters){
            _typeParameters _tps = of(typeParameters);
            return typeParameters( _tps) ; //((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        default _WTP typeParameters(_typeParameters _tps){
            _java._declaredBodyPart _m = (_java._declaredBodyPart) this;
            ((NodeWithTypeParameters)_m.ast()).setTypeParameters(_tps.ast());
            return (_WTP)this;
        }

        default _WTP typeParameters(NodeList<TypeParameter> astTypeParams){
            _java._declaredBodyPart _m = (_java._declaredBodyPart) this;
            ((NodeWithTypeParameters)_m.ast()).setTypeParameters(astTypeParams);
            return (_WTP)this;
        }

        default _WTP removeTypeParameter(TypeParameter tp){
            _java._declaredBodyPart _m = (_java._declaredBodyPart) this;
            ((NodeWithTypeParameters)_m.ast()).getTypeParameters().remove(tp);
            return (_WTP)this;
        }

        /**
         * remove all typeParameters from the entity
         * @return
         */
        default _WTP removeTypeParameters(){
            _java._declaredBodyPart _m = (_java._declaredBodyPart) this;
            ((NodeWithTypeParameters)_m.ast()).getTypeParameters().clear();
            return (_WTP)this;
        }

        /** does this have non empty type parameters */
        default boolean hasTypeParameters(){
            _java._declaredBodyPart _m = (_java._declaredBodyPart) this;
            return ((NodeWithTypeParameters)_m.ast()).getTypeParameters().isNonEmpty();
        }
    }
}
