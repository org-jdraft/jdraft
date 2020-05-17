package org.jdraft;

import java.util.*;

import com.github.javaparser.StaticJavaParser;
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
        implements _java._multiPart<TypeParameter, _typeParam>,
        _java._withName<_typeParam>, _annoExprs._withAnnoExprs<_typeParam> {

    public static _typeParam of() {
        return of( new TypeParameter());
    }

    public static _typeParam of(String typeParam ) {
        return of( Types.typeParam( typeParam ) );
    }

    public static _typeParam of(TypeParameter typeParameter ) {
        return new _typeParam( typeParameter );
    }

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
    public boolean hasTypeBound(){
        return this.typeParam.getTypeBound().isNonEmpty();
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
        Arrays.stream(types).forEach(t -> this.typeParam.getTypeBound().add(StaticJavaParser.parseClassOrInterfaceType(t)));
        return this;
    }

    /**
     *
     * @return
     */
    public NodeList<ClassOrInterfaceType> getTypeBound() {
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
        List<String>ttp = Types.normalizeTypeParam( this.typeParam);
        List<String>otp = Types.normalizeTypeParam( other.typeParam);
        return Objects.equals( ttp, otp );
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.TYPE_PARAMETER, this.typeParam);
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
