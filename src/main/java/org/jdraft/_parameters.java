package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * Parameter is the AST node TYPE (the syntax and storage TYPE in the AST)
 * _parameter is the individual element TYPE (provides a interface for
 * modifying to the AST)
 * _parameters is a container of ELEMENTS
 *
 * @author Eric
 */
public final class _parameters
        implements _draft {

    public static <T extends Object> _parameters of( Consumer<T> c){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    public static <T extends Object, U extends Object>_parameters of(BiConsumer<T,U> bc){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    public static <T extends Object, U extends Object, V extends Object>_parameters of(Ex.TriConsumer<T,U, V> bc){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    public static <T extends Object, U extends Object, V extends Object, W extends Object>_parameters of(Ex.QuadConsumer<T,U, V,W> bc){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    private static _parameters from(StackTraceElement lambdaStackTrace ){
        LambdaExpr le = Ex.lambdaEx( lambdaStackTrace );
        _parameters _ps = of( le.getParameters() );
        le.remove(); //dont connect the lambda with the caller
        return _ps;
    }

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
