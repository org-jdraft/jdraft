package org.jdraft;

import java.util.*;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;

import org.jdraft.text.Text;

/**
 * model of a parameter declaration (for {@link _method}s or {@link _constructor}s)
 *
 * @author Eric
 */
public final class _param
    implements _java._withNameTypeRef<Parameter, _param>, _annoExprs._withAnnoExprs<_param>,
        _modifiers._withFinal<_param>, _java._multiPart<Parameter, _param> {

    public static _param from (StackTraceElement ste ){
        _param _p = _lambdaExpr.from(ste).getParam(0);
        _p.ast().remove();
        return of( _p.ast() );
    }
    /**
     *
     * @param type
     * @param name
     * @return
     */
    public static _param of(Class type, String name ) {
        return of( new Parameter( Types.of( type ), name ) );
    }

    /**
     *
     * @param type
     * @param name
     * @return
     */
    public static _param of(Type type, String name ) {
        return of( new Parameter( type, name ) );
    }

    /**
     *
     * @param p
     * @return
     */
    public static _param of(Parameter p ) {
        return new _param( p );
    }

    public static _param of(){
        return of( new Parameter() );
    }
    /**
     *
     * @param parameter
     * @return
     */
    public static _param of(String parameter ) {
        return of( Ast.parameter( parameter ) );
    }

    private final Parameter astParameter;

    public _param(Parameter p ) {
        this.astParameter = p;
    }

    @Override
    public Parameter ast() {
        return this.astParameter;
    }

    @Override
    public _param copy(){
        return of( this.astParameter.toString());
    }

    public SimpleName getNameNode() { return this.astParameter.getName(); }

    @Override
    public _param setName(String name ) {
        this.astParameter.setName( name );
        return this;
    }

    @Override
    public _typeRef getTypeRef() {
        return _typeRef.of( this.astParameter.getType() );
    }

    @Override
    public _param setTypeRef(Type _tr ) {
        this.astParameter.setType( _tr );
        return this;
    }

    @Override
    public String getName() {
        return this.astParameter.getNameAsString();
    }

    @Override
    public _annoExprs getAnnoExprs() {
        return _annoExprs.of( this.astParameter );
    }

    public _param setVarArg(boolean b ){
        this.astParameter.setVarArgs(b);
        return this;
    }

    public boolean isVarArg() {
        return this.astParameter.isVarArgs();
    }

    @Override
    public boolean isFinal() {
        return this.astParameter.isFinal();
    }

    @Override
    public boolean isTypeRef(String type ) {
        return Types.equal(this.astParameter.getType(), Types.of( type ));
    }

    @Override
    public boolean isTypeRef(Type type ) {
        return Types.equal(this.astParameter.getType(), type);
    }

    @Override
    public boolean isNamed( String name ) {
        return this.astParameter.getNameAsString().equals( name );
    }

    @Override
    public boolean is( String... paramDecl ) {
        try {
            return equals( _param.of( Text.combine( paramDecl ) ) );
        }
        catch( Exception e ) {
        }
        return false;
    }

    @Override
    public boolean is( Parameter astParam ){
        try {
            return equals( _param.of( astParam ) );
        }
        catch( Exception e ) {
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash +
                Objects.hash( 
                        Exprs.hashAnnos(astParameter),
                        this.getName(),
                        Types.hash(astParameter.getType()),
                        this.isVarArg(),
                        this.isFinal() );
        return hash;
    }

    /**
     *
     * @param left
     * @param right
     * @return
     */
    public static boolean isEqual( Parameter left, Parameter right ) {
        if( left == right ) {
            return true;
        }
        if( left == null ) {
            return false;
        }
        if( right == null ) {
            return false;
        }
        if( !left.getNameAsString().equals( right.getNameAsString())){
            return false;
        }
        if( left.isVarArgs() != right.isVarArgs()){
            return false;
        }
        if( left.isFinal() != right.isFinal()){
            return false;
        }
        if( ! Exprs.equalAnnos(left, right)){
            return false;
        }
        if( ! Types.equal(left.getType(), right.getType())){
            return false;
        }
        return true;
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
        final _param other = (_param)obj;
        if( !Objects.equals( this.astParameter.getName(), other.astParameter.getName() )){
            return false;
        }
        if( !Objects.equals( this.astParameter.isFinal(), other.astParameter.isFinal()) ){
            return false;
        }
        if( !Objects.equals( this.astParameter.isVarArgs(), other.astParameter.isVarArgs() )){
            return false;
        }
        if( !Types.equal(astParameter.getType(), other.astParameter.getType())){
            return false;
        }
        if( !Exprs.equalAnnos(astParameter, other.astParameter)){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.astParameter.toString();
    }

    @Override
    public _param setFinal(boolean toSet ) {
        this.astParameter.setFinal( toSet );
        return this;
    }

    @Override
    public Map<_java.Feature, Object> components( ) {
        Map<_java.Feature, Object> parts = new HashMap<>();
        parts.put( _java.Feature.IS_FINAL, isFinal() );
        parts.put( _java.Feature.ANNO_EXPRS, getAnnoExprs() );
        parts.put( _java.Feature.TYPE, getTypeRef() );
        parts.put( _java.Feature.NAME, getName() );
        parts.put( _java.Feature.IS_VAR_ARG, isVarArg() );
        return parts;
    }

}