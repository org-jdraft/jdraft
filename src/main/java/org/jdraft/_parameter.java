package org.jdraft;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.type.Type;

import org.jdraft.text.Text;

/**
 * model of a parameter declaration (for {@link _method}s or {@link _constructor}s)
 *
 * @author Eric
 */
public final class _parameter
    implements _java._withNameTypeRef<Parameter, _parameter>, _annos._withAnnos<_parameter>,
        _modifiers._withFinal<_parameter>, _java._multiPart<Parameter, _parameter> {

    /**
     *
     * @param type
     * @param name
     * @return
     */
    public static _parameter of( Class type, String name ) {
        return of( new Parameter( Ast.typeRef( type ), name ) );
    }

    /**
     *
     * @param type
     * @param name
     * @return
     */
    public static _parameter of( Type type, String name ) {
        return of( new Parameter( type, name ) );
    }

    /**
     *
     * @param p
     * @return
     */
    public static _parameter of( Parameter p ) {
        return new _parameter( p );
    }

    public static _parameter of(){
        return of( new Parameter() );
    }
    /**
     *
     * @param parameter
     * @return
     */
    public static _parameter of( String parameter ) {
        return of( Ast.parameter( parameter ) );
    }

    private final Parameter astParameter;

    public _parameter( Parameter p ) {
        this.astParameter = p;
    }

    @Override
    public Parameter ast() {
        return this.astParameter;
    }

    @Override
    public _parameter copy(){
        return of( this.astParameter.toString());
    }

    public SimpleName getNameNode() { return this.astParameter.getName(); }

    @Override
    public _parameter setName(String name ) {
        this.astParameter.setName( name );
        return this;
    }

    @Override
    public _typeRef getTypeRef() {
        return _typeRef.of( this.astParameter.getType() );
    }

    @Override
    public _parameter setTypeRef(Type _tr ) {
        this.astParameter.setType( _tr );
        return this;
    }

    @Override
    public String getName() {
        return this.astParameter.getNameAsString();
    }

    @Override
    public _annos getAnnos() {
        return _annos.of( this.astParameter );
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
        return Ast.typesEqual(this.astParameter.getType(), Ast.typeRef( type ));
    }

    @Override
    public boolean isTypeRef(Type type ) {
        return Ast.typesEqual(this.astParameter.getType(), type);
    }

    @Override
    public boolean isNamed( String name ) {
        return this.astParameter.getNameAsString().equals( name );
    }

    @Override
    public boolean is( String... paramDecl ) {
        try {
            return equals( _parameter.of( Text.combine( paramDecl ) ) );
        }
        catch( Exception e ) {
        }
        return false;
    }

    @Override
    public boolean is( Parameter astParam ){
        try {
            return equals( _parameter.of( astParam ) );
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
                        Ex.hashAnnos(astParameter),
                        this.getName(),
                        
                        Ast.typeHash(astParameter.getType()),
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
        if( ! Ex.equivalentAnnos(left, right)){
            return false;
        }
        if( ! Ast.typesEqual(left.getType(), right.getType())){
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
        final _parameter other = (_parameter)obj;
        if( !Objects.equals( this.astParameter.getName(), other.astParameter.getName() )){
            return false;
        }
        if( !Objects.equals( this.astParameter.isFinal(), other.astParameter.isFinal()) ){
            return false;
        }
        if( !Objects.equals( this.astParameter.isVarArgs(), other.astParameter.isVarArgs() )){
            return false;
        }
        if( !Ast.typesEqual(astParameter.getType(), other.astParameter.getType())){
            return false;
        }
        if( !Ex.equivalentAnnos(astParameter, other.astParameter)){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.astParameter.toString();
    }

    @Override
    public _parameter setFinal( boolean toSet ) {
        this.astParameter.setFinal( toSet );
        return this;
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.FINAL, isFinal() );
        parts.put( _java.Component.ANNOS, getAnnos() );
        parts.put( _java.Component.TYPE, getTypeRef() );
        parts.put( _java.Component.NAME, getName() );
        parts.put( _java.Component.VAR_ARG, isVarArg() );
        return parts;
    }

}
