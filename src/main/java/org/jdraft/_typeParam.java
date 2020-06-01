package org.jdraft;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft.text.Text;

/**
 * Model of a Java TypeParameter
 *
 * @author Eric
 */
public final class _typeParam
        implements _java._node<TypeParameter, _typeParam>,
        _java._withName<_typeParam>, _annoExprs._withAnnoExprs<_typeParam> {

    public static final Function<String, _typeParam> PARSER = s-> _typeParam.of(s);

    public static _typeParam of() {
        return of( new TypeParameter());
    }

    public static _typeParam of(String typeParam ) {
        return of( Types.typeParam( typeParam ) );
    }

    public static _typeParam of(TypeParameter typeParameter ) {
        return new _typeParam( typeParameter );
    }

    public static _feature._one<_typeParam, String> NAME = new _feature._one<>(_typeParam.class, String.class,
            _feature._id.TYPE_PARAM,
            a -> a.getName(),
            (_typeParam a, String name) -> a.setName(name), PARSER);

    public static _feature._many<_typeParam, _typeRef> EXTENDS_TYPE_BOUND = new _feature._many<>(_typeParam.class, _typeRef.class,
            _feature._id.EXTENDS_TYPE_BOUNDS,
            _feature._id.EXTENDS_TYPE_BOUND,
            a -> a.listExtendsTypeBound(),
            (_typeParam p, List<_typeRef> _ccs) -> p.setExtendsTypeBound(_ccs), PARSER, s->_typeRef.of(s)).isOrdered(false);

    public static _feature._meta<_typeParam> META = _feature._meta.of(_typeParam.class, NAME, EXTENDS_TYPE_BOUND);

    private final TypeParameter typeParam;

    /**
     * build and return an independent copy of this model
     * @return 
     */
    public _typeParam copy(){
        return of( this.typeParam.toString() );
    }

    /**
     * sets the extends type bound, i.e.
     * "A extends Base & Pair<A,B>"
     *
     * @param nc
     * @return
     */
    public _typeParam setExtendsTypeBound(NodeList<ClassOrInterfaceType> nc){
        this.typeParam.setTypeBound(nc);
        return this;
    }

    /**
     * does this type parameter have a type bound?
     * @return
     */
    public boolean hasExtendsTypeBound(){
        return this.typeParam.getTypeBound().isNonEmpty();
    }

    public List<_typeRef> listExtendsTypeBound(){
        return this.ast().getTypeBound().stream().map( t-> _typeRef.of(t)).collect(Collectors.toList());
    }

    /**
     *
     * @param typeBound
     * @return
     */
    public _typeParam setExtendsTypeBound(List<_typeRef> typeBound){
        return setExtendsTypeBound(typeBound.toArray(new _typeRef[0]));
    }

    /**
     * sets the extends type bound, i.e.
     * "A extends Base & Pair<A,B>"
     *
     * @param types
     * @return
     */
    public _typeParam setExtendsTypeBound(_typeRef...types){
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
    public _typeParam setExtendsTypeBound(ClassOrInterfaceType...types){
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
    public _typeParam addExtendsTypeBound(ClassOrInterfaceType...types){
        Arrays.stream(types).forEach(t -> this.typeParam.getTypeBound().add(t));
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
    public _typeParam addExtendsTypeBound(_typeRef...types){
        Arrays.stream(types).forEach(t -> this.typeParam.getTypeBound().add((ClassOrInterfaceType)t.ast()));
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
    public _typeParam addExtendsTypeBound(String...types){
        Arrays.stream(types).forEach(t -> this.typeParam.getTypeBound().add((ClassOrInterfaceType)Types.of(t))); //StaticJavaParser.parseClassOrInterfaceType(t)));
        return this;
    }

    /**
     *
     * @return
     */
    public NodeList<ClassOrInterfaceType> getAstExtendsTypeBound() {
        return typeParam.getTypeBound();
    }

    @Override
    public TypeParameter ast() {        
        return this.typeParam;
    }

    public _typeParam(TypeParameter tp ) {
        this.typeParam = tp;
    }

    @Override
    public boolean is( String... typeParam ){
        try{
            return of( Text.combine(typeParam) ).equals(this);
        }
        catch( Exception e) {
            return false;
        }
    }

    @Override
    public boolean is( TypeParameter typeParam ){
        try{
            return of( typeParam ).equals(this);
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
        final _typeParam other = (_typeParam)obj;

        if( Objects.equals(this.typeParam, other.typeParam)){
            return true;
        }
        if( !Objects.equals( this.getName(),other.getName()) ){
            System.out.println("Name \""+ this.getName()+"\" not same \""+other.getName()+"\"");
            return false;
        }
        //List<_typeRef> tetb = this.listExtendsTypeBound();
        //List<_typeRef> otb = other.listExtendsTypeBound();
        NodeList<ClassOrInterfaceType> tetb = this.getAstExtendsTypeBound();
        NodeList<ClassOrInterfaceType> otb = other.getAstExtendsTypeBound();

        if( tetb.size() != otb.size() ){
            return false;
        }
        //make sure the bounds match (IN ANY ORDER)
        return tetb.stream().allMatch(t-> otb.stream().anyMatch(a-> Types.equal( t, a)));
        //List<String>ttp = Types.normalizeTypeParam( this.typeParam);
        //List<String>otp = Types.normalizeTypeParam( other.typeParam);
        //return Objects.equals( ttp, otp );
    }

    public Map<_java.Feature, Object> features( ) {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put( _java.Feature.TYPE_PARAM, this.typeParam);
        return parts;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode( Types.normalizeTypeParam(this.typeParam) );
    }

    @Override
    public String toString() {
        //JavaParser already does a great job, no need to interfere
        return typeParam.toString();
    }

    @Override
    public _typeParam setName(String name) {
        this.typeParam.setName(name);
        return this;
    }

    public SimpleName getNameNode(){ return this.typeParam.getName(); }

    @Override
    public String getName() {
        return typeParam.getNameAsString();
    }

    @Override
    public _annoExprs getAnnoExprs() {
        return _annoExprs.of( this.typeParam);
    }

}
