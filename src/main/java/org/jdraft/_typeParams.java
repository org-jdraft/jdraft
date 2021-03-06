package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Eric
 */
public final class _typeParams
        implements _tree._view<_typeParams>, _tree._group<TypeParameter, _typeParam, _typeParams> {

    public static final Function<String, _typeParams> PARSER = s-> _typeParams.of(s);

    public static _typeParams of(){
        return of( (ClassOrInterfaceDeclaration)Ast.typeDeclaration("class $name${}" ));
    }

    public static _typeParams of(String...tps){
        String typeParams = Text.combine(tps);
        if( typeParams.length() == 0 ){
            return of();
        }
        if( !typeParams.startsWith("<") ){
            typeParams = "<"+ typeParams +">";
        }
        ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)Ast.typeDeclaration("class $name$"+ typeParams +"{}");
        return of( coid );
    }

    public static _typeParams of( _typeParam... _tps){
        ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)Ast.typeDeclaration("class $name${}");
        NodeList<TypeParameter> ntp = new NodeList<>();
        Stream.of(_tps).forEach(_tp -> ntp.add( _tp.node()));
        coid.setTypeParameters(ntp);
        return of( coid );
    }

    public static _typeParams of(NodeWithTypeParameters tps ) {
        return new _typeParams( tps );
    }

    public static _feature._many<_typeParams, _typeParam> TYPE_PARAMS = new _feature._many<>(_typeParams.class, _typeParam.class,
            _feature._id.TYPE_PARAMS,
            _feature._id.TYPE_PARAM,
            a -> a.list(),
            (_typeParams p, List<_typeParam> _tps) -> p.setTypeParams(_tps), PARSER, s-> _typeParam.of(s))
            .setOrdered(false);

    public static _feature._features<_typeParams> FEATURES = _feature._features.of(_typeParams.class,  PARSER, TYPE_PARAMS);

    private final NodeWithTypeParameters parentNode;

    public _typeParams(NodeWithTypeParameters ntp ) {
        this.parentNode = ntp;
    }

    public _feature._features<_typeParams> features(){
        return FEATURES;
    }

    public _typeParams clear() {
        parentNode.getTypeParameters().clear();
        return this;
    }

    public _typeParams setTypeParams(List<_typeParam> typeParams){
        this.parentNode.getTypeParameters().clear();
        typeParams.forEach(tp-> this.parentNode.getTypeParameters().add( tp.node()));
        return this;
    }

    public List<_typeParam> list() {
        List<_typeParam> lp = new ArrayList<>();
        parentNode.getTypeParameters().forEach(t -> lp.add( _typeParam.of( (TypeParameter)t ) ) );
        return lp;
    }

    public List<_typeParam> list(Predicate<_typeParam> tps ) {
        return list().stream().filter( tps ).collect( Collectors.toList() );
    }

    public _typeParams remove(List<? super _typeParam> tps ) {
        parentNode.getTypeParameters().removeAll( tps );
        return this;
    }

    public NodeList<TypeParameter> ast() {
        return parentNode.getTypeParameters();
    }

    @Override
    public _typeParams copy() {
        return _typeParams.of(this.parentNode);
    }

    @Override
    public NodeList<TypeParameter> astList() {
        return this.parentNode.getTypeParameters();
    }

    public boolean is( String... typeParameters ) {
        String str = Text.combine(typeParameters);
        if( str.startsWith("$") && str.endsWith("$")){
            Stencil st = Stencil.of(str);
            if( st.isMatchAny() ){
                return true;
            }
        }
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
        this.parentNode.getTypeParameters().forEach(t -> _tps.add( _typeParam.of( (TypeParameter)t)) );
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
        if( this.parentNode.getTypeParameters().size() !=  other.parentNode.getTypeParameters().size()){
            return false;
        }
        Set<_typeParam> _tps = new HashSet<>();
        this.parentNode.getTypeParameters().forEach(t -> _tps.add( _typeParam.of( (TypeParameter)t) ) );

        for(int i = 0; i<other.parentNode.getTypeParameters().size(); i++){
            _typeParam _tp = _typeParam.of( other.parentNode.getTypeParameter(i));
            if( ! _tps.contains(_tp) ){
                return false;
            }
        }
        return true;
    }

    public boolean is(String code){
        return is(new String[]{code});
    }

    @Override
    public String toString() {
        return toString( new PrettyPrinterConfiguration());
    }

    public String toString(PrettyPrinterConfiguration ppc){
        if( this.parentNode.getTypeParameters().isNonEmpty() ) {
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

    /**
     * The node that is an implementation of NodeWithTypeParameters
     * @return
     */
    public <N extends Node> N anchorNode() {
        return (N)this.parentNode;
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
            return of( (NodeWithTypeParameters)_m.node() );
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
            return ((NodeWithTypeParameters)_m.node()).getTypeParameters();
        }

        default _WTP addTypeParam( _typeParam _tp){
            if( hasTypeParams() ){
                listAstTypeParameters().add( _tp.node());
            } else{
                setTypeParams(_typeParams.of(_tp));
            }
            return (_WTP) this;
        }

        default _WTP addTypeParam( String typeParam ){
            return addTypeParam( _typeParam.of(typeParam));
        }

        /**
         * parse the string as TypeParameters and set the typeParameters
         * @param typeParameters string of typeParameters
         * @return the modified T
         */
        default _WTP setTypeParams(String typeParameters){
            _typeParams _tps = of(typeParameters);
            return setTypeParams( _tps) ; //((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        /**
         * @param typeParameters Strings that represent individual TypeParameters
         * @return the modified T
         */
        default _WTP setTypeParams(String... typeParameters){
            _typeParams _tps = of(typeParameters);
            return setTypeParams( _tps) ; //((NodeWithTypeParameters)_m.ast()).getTypeParameters();
        }

        default _WTP setTypeParams(_typeParams _tps){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.node()).setTypeParameters(_tps.ast());
            return (_WTP)this;
        }

        default _WTP setTypeParams(NodeList<TypeParameter> astTypeParams){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.node()).setTypeParameters(astTypeParams);
            return (_WTP)this;
        }

        default _WTP removeTypeParam(TypeParameter tp){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.node()).getTypeParameters().remove(tp);
            return (_WTP)this;
        }

        default _WTP removeTypeParams( Predicate<_typeParam> _matchFn ){
            return (_WTP)this;
        }

        /**
         * remove all typeParameters from the entity
         * @return
         */
        default _WTP removeTypeParams(){
            _java._declared _m = (_java._declared) this;
            ((NodeWithTypeParameters)_m.node()).getTypeParameters().clear();
            return (_WTP)this;
        }

        /** does this have non empty type parameters */
        default boolean hasTypeParams(){
            _java._declared _m = (_java._declared) this;
            return ((NodeWithTypeParameters)_m.node()).getTypeParameters().isNonEmpty();
        }
    }
}
