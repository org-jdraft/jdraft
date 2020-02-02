package org.jdraft;

import java.util.*;
import java.util.function.Predicate;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft.text.Text;

/**
 * Model of a Java TypeParameter
 *
 * @author Eric
 */
public final class _typeParameter
        implements _java._node<TypeParameter, _typeParameter>,
        _java._named<_typeParameter>, _anno._hasAnnos<_typeParameter> {

    public static _typeParameter of() {
        return of( new TypeParameter());
    }

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
     * sets the extends type bound, i.e.
     * "A extends Base & Pair<A,B>"
     *
     * @param nc
     * @return
     */
    public _typeParameter setExtendsTypeBound(NodeList<ClassOrInterfaceType> nc){
        this.typeParameter.setTypeBound(nc);
        return this;
    }

    /**
     * sets the extends type bound, i.e.
     * "A extends Base & Pair<A,B>"
     *
     * @param types
     * @return
     */
    public _typeParameter setExtendsTypeBound(_typeRef...types){
        NodeList<ClassOrInterfaceType> nc = new NodeList<>();
        Arrays.stream(types).forEach(t -> nc.add((ClassOrInterfaceType)t.ast()));
        setExtendsTypeBound(nc);
        return this;
    }

    /**
     * sets the extends type bound, i.e.
     * "A extends Base & Pair<A,B>"
     *
     * @param types
     * @return
     */
    public _typeParameter setExtendsTypeBound(ClassOrInterfaceType...types){
        NodeList<ClassOrInterfaceType> nc = new NodeList<>();
        Arrays.stream(types).forEach(t -> nc.add(t));
        setExtendsTypeBound(nc);
        return this;
    }

    /**
     * _typeParameter _tp = _typeParameter.of("A extends Base");
     * _tp.addExtendsTypeBound("Pair<A,B>")
     *
     * System.out.println(_tp);
     *   // "A extends Base & Pair<A,B>"
     * @param types
     * @return
     */
    public _typeParameter addExtendsTypeBound(ClassOrInterfaceType...types){
        Arrays.stream(types).forEach(t -> this.typeParameter.getTypeBound().add(t));
        return this;
    }

    /**
     * _typeParameter _tp = _typeParameter.of("A extends Base");
     * _tp.addExtendsTypeBound("Pair<A,B>")
     *
     * System.out.println(_tp);
     *   // "A extends Base & Pair<A,B>"
     * @param types
     * @return
     */
    public _typeParameter addExtendsTypeBound(_typeRef...types){
        Arrays.stream(types).forEach(t -> this.typeParameter.getTypeBound().add((ClassOrInterfaceType)t.ast()));
        return this;
    }

    /**
     * adds the extends type bound, i.e.
     * _typeParameter _tp = _typeParameter.of("A extends Base");
     * _tp.addExtendsTypeBound("Pair<A,B>")
     *
     * System.out.println(_tp);
     *   // "A extends Base & Pair<A,B>"
     * @param types
     * @return
     */
    public _typeParameter addExtendsTypeBound(String...types){
        Arrays.stream(types).forEach(t -> this.typeParameter.getTypeBound().add(StaticJavaParser.parseClassOrInterfaceType(t)));
        return this;
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
    public _typeParameter setName(String name) {
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
     * @param <_HTP>
     */
    public interface _hasTypeParameters<_HTP extends _hasTypeParameters>
        extends _java._domain {
        
        default _typeParameters getTypeParameters(){
            _java._declared _m = (_java._declared) this;
            return _typeParameters.of( (NodeWithTypeParameters)_m.ast() );
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
            _java._declared _m = (_java._declared) this;
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
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.ast()).setTypeParameters(_tps.ast());            
            return (_HTP)this;
        }

        default _HTP typeParameters(NodeList<TypeParameter> astTypeParams ){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.ast()).setTypeParameters(astTypeParams);            
            return (_HTP)this;
        }

        default _HTP removeTypeParameter(TypeParameter tp ){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.ast()).getTypeParameters().remove(tp);   
            return (_HTP)this;
        }
        
        /**
         * remove all typeParameters from the entity
         * @return 
         */
        default _HTP removeTypeParameters(){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.ast()).getTypeParameters().clear();   
            return (_HTP)this;
        }

        /** does this have non empty type parameters */
        default boolean hasTypeParameters(){
            _java._declared _m = (_java._declared) this;
            return ((NodeWithTypeParameters)_m.ast()).getTypeParameters().isNonEmpty();
        }
    }    
}
