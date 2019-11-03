package org.jdraft;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;
import com.github.javaparser.ast.type.Type;

import org.jdraft._modifiers._hasFinal;
import org.jdraft._anno.*;

/**
 * model of a parameter declaration (for {@link _method}s or {@link _constructor}s)
 *
 * @author Eric
 */
public final class _parameter
    implements _namedType<_parameter>, _anno._hasAnnos<_parameter>, 
        _hasFinal<_parameter>, _node<Parameter, _parameter> {

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
    
    @Override
    public _parameter name( String name ) {
        this.astParameter.setName( name );
        return this;
    }

    @Override
    public _typeRef getType() {
        return _typeRef.of( this.astParameter.getType() );
    }

    @Override
    public _parameter type( Type _tr ) {
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
    public boolean isType( String type ) {
        Type t = Ast.typeRef( type );
        return this.astParameter.getType().equals( t );
    }

    @Override
    public boolean isType( Type type ) {
        return this.astParameter.getType().equals( type );
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
        parts.put( _java.Component.TYPE, getType() );
        parts.put( _java.Component.NAME, getName() );
        parts.put( _java.Component.VAR_ARG, isVarArg() );
        return parts;
    }

    /**
     *
     * @author Eric
     * @param <_HP>
     */
    public interface _hasParameters<_HP extends _hasParameters>
        extends _draft {

        _parameters getParameters();
        
        /**
         * Return the Ast Node that has the parameters (i.e. {@link MethodDeclaration}, 
         * {@link com.github.javaparser.ast.body.ConstructorDeclaration})
         * @return the NodeWithParameters instance
         */
        NodeWithParameters ast();
        
        default _parameter getParameter( int index ){
            return _parameter.of( ast().getParameter( index ) );
        }
        
        default _parameter getParameter(Class type) {
            Optional<Parameter> op = ast().getParameterByType(type);
            if (op.isPresent()) {
                return _parameter.of(op.get());
            }
            return null;
        }

        default _parameter getParameter(_typeRef _type) {
            Optional<Parameter> op = ast().getParameterByType(_type.toString());
            if (op.isPresent()) {
                return _parameter.of(op.get());
            }
            return null;
        }
        
        default _parameter getParameter(String parameterName) {
            Optional<Parameter> op = this.ast().getParameterByName(parameterName);
            if (op.isPresent()) {
                return _parameter.of(op.get());
            }
            return null;
        }

        default boolean hasParameters() {
            return !ast().getParameters().isEmpty();
        }

        default List<_parameter> listParameters() {
            return getParameters().list();
        }

        default List<_parameter> listParameters(
                Predicate<_parameter> paramMatchFn ) {
            return getParameters().list( paramMatchFn );
        }

        default _HP forParameters(Consumer<_parameter> paramActionFn ) {
            listParameters().forEach( paramActionFn );
            return (_HP)this;
        }

        default _HP forParameters(Predicate<_parameter> paramMatchFn,
                                  Consumer<_parameter> paramActionFn ) {
            listParameters( paramMatchFn ).forEach( paramActionFn );
            return (_HP)this;
        }

        default _HP addParameters(String... parameters ) {
            Arrays.stream( parameters ).forEach( p -> addParameter( p ) );
            return (_HP)this;
        }

        default _HP addParameter(_typeRef type, String name ) {
            return addParameter( new Parameter( type.ast(), name ) );
        }

        default _HP addParameter(String parameter ) {
            addParameter( Ast.parameter( parameter ) );
            return (_HP)this;
        }

        default _HP addParameters(_parameter... parameters ) {
            Arrays.stream( parameters ).forEach( p -> addParameter( p ) );
            return (_HP)this;
        }

        default _HP addParameter(_parameter parameter ) {
            addParameter( parameter.ast() );
            return (_HP)this;
        }
        
        default _HP addParameter(Parameter p ){
            ast().addParameter(p);
            return (_HP)this;
        }

        default _HP addParameters(Parameter... ps ){
            Arrays.stream(ps).forEach( p -> addParameter(p));
            return (_HP)this;
        }
        
        default _HP setParameters(NodeList<Parameter> astPs){
            ast().setParameters(astPs);
            return (_HP)this;
        }
        
        default _HP setParameters(_parameters _ps){
            return (_HP)setParameters( _ps.ast() );
        }        
        
        default _HP setParameters(Parameter... astPs ){
            NodeList<Parameter>nl = new NodeList<>();
            Arrays.stream(astPs).forEach(p -> nl.add(p));
            return setParameters(nl);
        }
               
        default boolean isVarArg() {
            return getParameters().isVarArg();
        }
    }
   
    /**
     *
     * Parameter is the AST node TYPE (the syntax and storage TYPE in the AST)
     * _parameter is the individual element TYPE (provides a interface for
     * modifying to the AST)
     * _parameters is a container of ELEMENTS
     *
     * @author Eric
     */
    public static final class _parameters
            implements _draft {

        public static _parameters of( List<Parameter> ps ){
            _parameters _ps = of();
            ps.forEach(p->  _ps.add(p) );
            return _ps;
        }

        /**
         *
         * @param strs
         * @return
         */
        public static _parameters of( String... strs ) {
            //we need a holder for the Nodes
            StringBuilder params = new StringBuilder();
            for( int i = 0; i < strs.length; i++ ) {
                if( i > 0 && !(params.charAt( params.length() - 1 ) == ',') ) {
                    params.append( ',' );
                }
                params.append( strs[ i ] );
            }
            String ps = params.toString();
            if( ps.startsWith("(") ){
                ps = ps.substring(1);
            }
            if( ps.endsWith(")")){
                ps = ps.substring(0, ps.length() -1);
            }
            return of( Ast.method( "void $$(" + ps + ");" ) );
        }

        /**
         *
         * @param np
         * @return
         */
        public static _parameters of( NodeWithParameters np ) {
            return new _parameters( np );
        }

        public boolean isVarArg() {
            if( !isEmpty() ) {
                return get( size() - 1 ).isVarArg();
            }
            return false;
        }

        /**
         *
         * @param params
         * @return
         */
        public boolean is( String... params ) {
            try {
                return of( params ).equals( this );
            }
            catch( Exception e ) {

            }
            return false;
        }

        /**
         *
         */
        private final NodeWithParameters astNodeWithParams;

        /**
         *
         * @return
         */
        public NodeList<Parameter> ast() {
            return astNodeWithParams.getParameters();
        }

        /**
         *
         * @param nwp
         */
        public _parameters( NodeWithParameters nwp ) {
            this.astNodeWithParams = nwp;
        }

        /**
         *
         * @param index
         * @return
         */
        public _parameter get( int index ) {
            return _parameter.of( astNodeWithParams.getParameter( index ) );
        }

        /**
         *
         * @return
         */
        public boolean isEmpty() {
            return this.astNodeWithParams.getParameters().isEmpty();
        }

        /**
         *
         * @return
         */
        public int size() {
            return this.astNodeWithParams.getParameters().size();
        }

        /**
         *
         * @return
         */
        public _parameters clear() {
            this.astNodeWithParams.getParameters().clear();
            return this;
        }

        /**
         *
         * @return
         */
        public _parameters copy(){
            NodeList<Parameter> ps = new NodeList<>();
            MethodDeclaration md = Ast.method("void a(){}");
            ast().forEach( p-> md.addParameter(p) );
            return _parameters.of( md );
        }

        /**
         *
         * @param _p
         * @return
         */
        public int indexOf( _parameter _p ) {
            return indexOf( _p.ast() );
        }

        /**
         *
         * @param p
         * @return
         */
        public int indexOf( Parameter p ) {
            for( int i = 0; i < this.astNodeWithParams.getParameters().size(); i++ ) {
                if( p.equals( this.astNodeWithParams.getParameter( i ) ) ) {
                    return i;
                }
            }
            return -1;
        }

        /**
         *
         * @return
         */
        public _typeRef[] types(){
            _typeRef[] ts = new _typeRef[size()];
            for(int i=0;i<this.size();i++){
                ts[i] = _typeRef.of( this.astNodeWithParams.getParameter(i).getType() );
            }
            return ts;
        }

        /**
         *
         * @param ps
         * @return
         */
        public _parameters remove( Parameter... ps ) {
            for( int i = 0; i < ps.length; i++ ) {
                this.astNodeWithParams.getParameters().remove( ps[ i ] );
            }
            return this;
        }

        /**
         *
         * @param _ps
         * @return
         */
        public _parameters remove( _parameter... _ps ) {
            for( int i = 0; i < _ps.length; i++ ) {
                this.astNodeWithParams.getParameters().remove( _ps[ i ].ast() );
            }
            return this;
        }

        /**
         *
         * @return
         */
        public List<_parameter> list() {
            List<_parameter> ps = new ArrayList<>();
            this.astNodeWithParams.getParameters().forEach( p -> ps.add( _parameter.of( (Parameter)p ) ) );
            return ps;
        }

        /**
         *
         * @param paramAction
         */
        public void forEach( Consumer<? super _parameter> paramAction ) {
            list().forEach( paramAction );
        }

        /**
         *
         * @param paramMatchFn
         * @param paramAction
         */
        public void forEach( Predicate<_parameter> paramMatchFn,
                             Consumer<_parameter> paramAction ) {
            list( paramMatchFn ).forEach( paramAction );
        }

        /**
         *
         * @param paramMatchFn
         * @return
         */
        public List<_parameter> list( Predicate<_parameter> paramMatchFn ) {
            return list().stream().filter( paramMatchFn ).collect( Collectors.toList() );
        }

        /**
         *
         * @param paramMatchFn
         * @return
         */
        public _parameters remove( Predicate<_parameter> paramMatchFn ) {
            list( paramMatchFn ).forEach( p -> remove( p ) );
            return this;
        }

        /**
         *
         * @param name
         * @return
         */
        public _parameter get( String name ) {
            Optional<Parameter> p = astNodeWithParams.getParameterByName( name );
            if( p.isPresent() ) {
                return _parameter.of( p.get() );
            }
            return null;
        }

        /**
         *
         * @param clazz
         * @return
         */
        public _parameter get( Class clazz ) {
            Optional<Parameter> p = astNodeWithParams.getParameterByType( clazz );
            if( p.isPresent() ) {
                return _parameter.of( p.get() );
            }
            return null;
        }

        /**
         *
         * @param index
         * @param _p
         * @return
         */
        public _parameters set( int index, _parameter _p ) {
            astNodeWithParams.setParameter( index, _p.ast() );
            return this;
        }

        /**
         *
         * @param typeRefs
         * @return
         */
        public boolean hasParametersOfType( _typeRef... typeRefs ) {
            String[] pts = new String[typeRefs.length];
            for(int i=0;i<typeRefs.length;i++){
                pts[i] = typeRefs[i].toString();
            }
            return astNodeWithParams.hasParametersOfType( pts );
        }

        /**
         *
         * @param paramTypes
         * @return
         */
        public boolean hasParametersOfType( String... paramTypes ) {
            return astNodeWithParams.hasParametersOfType( paramTypes );
        }

        /**
         *
         * @param paramTypes
         * @return
         */
        public boolean hasParametersOfType( Class... paramTypes ) {
            return astNodeWithParams.hasParametersOfType( paramTypes );
        }

        /**
         *
         * @param _ps
         * @return
         */
        public _parameters add( String... _ps ) {
            for( int i = 0; i < _ps.length; i++ ) {
                astNodeWithParams.addParameter( Ast.parameter( _ps[ i ] ) );
            }
            return this;
        }

        /**
         *
         * @param _ps
         * @return
         */
        public _parameters add( _parameter... _ps ) {
            for( int i = 0; i < _ps.length; i++ ) {
                astNodeWithParams.addParameter( _ps[ i ].ast() );
            }
            return this;
        }

        /**
         *
         * @param ps
         * @return
         */
        public _parameters add( Parameter... ps ) {
            for( int i = 0; i < ps.length; i++ ) {
                astNodeWithParams.addParameter( ps[ i ] );
            }
            return this;
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
            final _parameters other = (_parameters)obj;
            if( this.astNodeWithParams.getParameters().size() != other.astNodeWithParams.getParameters().size() ) {
                return false;
            }
            for( int i = 0; i < this.size(); i++ ) {
                _parameter t = _parameter.of(this.astNodeWithParams.getParameter( i ));
                _parameter o = _parameter.of(other.astNodeWithParams.getParameter( i ));

                if( ! Objects.equals(t, o)){
                    return false;
                }                
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            //list because order matters
            List<_parameter> _ps = new ArrayList<>();
            this.astNodeWithParams.getParameters().forEach( p -> _ps.add( _parameter.of( (Parameter)p) ));
            hash = 79 * hash + Objects.hashCode( _ps );
            return hash;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append( "(" );
            int size = this.astNodeWithParams.getParameters().size();
            for( int i = 0; i < size; i++ ) {
                if( i > 0 ) {
                    sb.append( ", " );
                }
                sb.append( this.astNodeWithParams.getParameter( i ).toString() );
            }
            sb.append( ")" );
            return sb.toString();
        }

        public NodeWithParameters astHolder() {
            return this.astNodeWithParams;
        }
    }    
}
