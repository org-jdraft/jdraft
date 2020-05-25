package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithParameters;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Parameter is the AST node TYPE (the syntax and storage TYPE in the AST)
 * _parameter is the individual element TYPE (provides a interface for
 * modifying to the AST)
 * _parameters is a container of ELEMENTS
 *
 * @author Eric
 *
 *
 */
public final class _params
        implements _java._list<Parameter, _param, _params> {

    public static <T extends Object> _params of(Consumer<T> c){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    public static <T extends Object, U extends Object> _params of(BiConsumer<T,U> bc){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    public static <T extends Object, U extends Object, V extends Object> _params of(Exprs.TriConsumer<T,U, V> bc){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    public static <T extends Object, U extends Object, V extends Object, W extends Object> _params of(Exprs.QuadConsumer<T,U, V,W> bc){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    private static _params from(StackTraceElement lambdaStackTrace ){
        LambdaExpr le = Exprs.lambdaExpr( lambdaStackTrace );
        _params _ps = of( le.getParameters() );
        le.remove(); //dont connect the lambda with the caller
        return _ps;
    }

    public static _params of(List<Parameter> ps ){
        _params _ps = of();
        ps.forEach(p->  _ps.add(p) );
        return _ps;
    }

    public static _params of(){
        return of(  Exprs.lambdaExpr("()->true") ); //Ast.method( "void $$();" ));
    }

    /**
     *
     * @param strs
     * @return
     */
    public static _params of(String... strs ) {
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
        return of( Exprs.lambdaExpr( "(" + ps + ") -> true" ) );
    }

    /**
     *
     * @param np
     * @return
     */
    public static _params of(NodeWithParameters np ) {
        return new _params( np );
    }

    /**
     * Does the params list end with a varArg parameter
     * @return
     */
    public boolean isVarArg() {
        if( !isEmpty() ) {
            return getAt( size() - 1 ).isVarArg();
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

    public static _feature._many<_params, _param> PARAMS = new _feature._many<>(_params.class, _param.class,
            _feature._id.PARAMS, _feature._id.PARAM,
            a->a.list(),
            (_params a, List<_param> ps)-> a.set(ps));

    public static _feature._meta<_params> META = _feature._meta.of(_params.class, PARAMS);

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
     * Returns the Parameter names as a List of String names
     * @return
     */
    public List<String> names(){
        List<String> names = new ArrayList();
        this.astNodeWithParams.getParameters().forEach( (p) -> names.add( ((Parameter)p).getNameAsString()));
        return names;
    }

    /**
     *
     * @param nwp
     */
    public _params(NodeWithParameters nwp ) {
        this.astNodeWithParams = nwp;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return this.astNodeWithParams.getParameters().isEmpty();
    }

    @Override
    public NodeList<Parameter> listAstElements() {
        return this.astNodeWithParams.getParameters();
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
    public _params clear() {
        this.astNodeWithParams.getParameters().clear();
        return this;
    }

    /**
     *
     * @return
     */
    public _params copy(){
        NodeList<Parameter> ps = new NodeList<>();
        LambdaExpr le = Exprs.lambdaExpr("()->true");
        ast().forEach( p-> le.addParameter(p) );
        return _params.of( le );
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
     * @return
     */
    public List<_param> list() {
        List<_param> ps = new ArrayList<>();
        this.astNodeWithParams.getParameters().forEach( p -> ps.add( _param.of( (Parameter)p ) ) );
        return ps;
    }

    /**
     *
     * @param name
     * @return
     */
    public _param get(String name ) {
        Optional<Parameter> p = astNodeWithParams.getParameterByName( name );
        if( p.isPresent() ) {
            return _param.of( p.get() );
        }
        return null;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public _param get(Class clazz ) {
        Optional<Parameter> p = astNodeWithParams.getParameterByType( clazz );
        if( p.isPresent() ) {
            return _param.of( p.get() );
        }
        return null;
    }

    /**
     *
     * @param index
     * @param _p
     * @return
     */
    public _params set(int index, _param _p ) {
        astNodeWithParams.setParameter( index, _p.ast() );
        return this;
    }

    /**
     *
     * @param typeRefs
     * @return
     */
    public boolean hasParamsOfType(_typeRef... typeRefs ) {
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
    public boolean hasParamsOfType(String... paramTypes ) {
        return astNodeWithParams.hasParametersOfType( paramTypes );
    }

    /**
     *
     * @param paramTypes
     * @return
     */
    public boolean hasParamsOfType(Class... paramTypes ) {
        return astNodeWithParams.hasParametersOfType( paramTypes );
    }

    /**
     *
     * @param _ps
     * @return
     */
    public _params add(String... _ps ) {
        for( int i = 0; i < _ps.length; i++ ) {
            astNodeWithParams.addParameter( Ast.parameter( _ps[ i ] ) );
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
        final _params other = (_params)obj;
        if( this.astNodeWithParams.getParameters().size() != other.astNodeWithParams.getParameters().size() ) {
            return false;
        }
        for( int i = 0; i < this.size(); i++ ) {
            _param t = _param.of(this.astNodeWithParams.getParameter( i ));
            _param o = _param.of(other.astNodeWithParams.getParameter( i ));

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
        List<_param> _ps = new ArrayList<>();
        this.astNodeWithParams.getParameters().forEach( p -> _ps.add( _param.of( (Parameter)p) ));
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

    /**
     *
     * @author Eric
     * @param <_WP>
     */
    public interface _withParams<_WP extends _withParams>
        extends _java._domain {

        _params getParams();

        /**
         * Return the Ast Node that has the parameters (i.e. {@link MethodDeclaration},
         * {@link com.github.javaparser.ast.body.ConstructorDeclaration})
         * @return the NodeWithParameters instance
         */
        NodeWithParameters ast();

        /**
         * Check if all individual arg ({@link _param}s) match the function
         * @param matchFn
         * @return
         */
        default boolean allParams(Predicate<_param> matchFn){
            return listParams().stream().allMatch(matchFn);
        }

        default _param getParam(int index){
            return _param.of( ast().getParameter( index ) );
        }

        default _param getParam(Class type) {
            Optional<Parameter> op = ast().getParameterByType(type);
            if (op.isPresent()) {
                return _param.of(op.get());
            }
            return null;
        }

        default _param getParam(_typeRef _type) {
            Optional<Parameter> op = ast().getParameterByType(_type.toString());
            if (op.isPresent()) {
                return _param.of(op.get());
            }
            return null;
        }

        default _param getParam(String parameterName) {
            Optional<Parameter> op = this.ast().getParameterByName(parameterName);
            if (op.isPresent()) {
                return _param.of(op.get());
            }
            return null;
        }

        default boolean hasParams() {
            return !ast().getParameters().isEmpty();
        }

        default List<_param> listParams() {
            return getParams().list();
        }

        default List<_param> listParams(
                Predicate<_param> paramMatchFn) {
            return getParams().list( paramMatchFn );
        }

        default _WP forParams(Consumer<_param> paramActionFn) {
            listParams().forEach( paramActionFn );
            return (_WP)this;
        }

        default _WP forParams(Predicate<_param> paramMatchFn,
                              Consumer<_param> paramActionFn) {
            listParams( paramMatchFn ).forEach( paramActionFn );
            return (_WP)this;
        }

        default _WP addParams(String... parameters) {
            Arrays.stream( parameters ).forEach(p -> addParam( p ) );
            return (_WP)this;
        }

        default _WP addParam(_typeRef type, String name) {
            return addParam( new Parameter( type.ast(), name ) );
        }

        default _WP addParam(String parameter) {
            addParam( Ast.parameter( parameter ) );
            return (_WP)this;
        }

        default _WP addParams(_param... parameters) {
            Arrays.stream( parameters ).forEach( p -> addParam( p ) );
            return (_WP)this;
        }

        default _WP addParam(_param parameter) {
            addParam( parameter.ast() );
            return (_WP)this;
        }

        default _WP addParam(Parameter p){
            ast().addParameter(p);
            return (_WP)this;
        }

        default _WP addParams(Parameter... ps){
            Arrays.stream(ps).forEach( p -> addParam(p));
            return (_WP)this;
        }

        default _WP setParams(NodeList<Parameter> astPs){
            ast().setParameters(astPs);
            return (_WP)this;
        }

        default _WP setParams(_params _ps){
            return (_WP) setParams( _ps.ast() );
        }

        /**
         * Sets the parameters by taking in a String
         * @param parameters the String representation of the parameters
         * @return the _hasParameters entity
         */
        default _WP setParams(String... parameters){
            return setParams( Ast.parameters(parameters) );
        }

        default _WP setParams(Parameter... astPs){
            NodeList<Parameter>nl = new NodeList<>();
            Arrays.stream(astPs).forEach(p -> nl.add(p));
            return setParams(nl);
        }

        default boolean isParam(int index, _param _p){
            return Objects.equals( getParam(index), _p);
        }

        default boolean isParams(Predicate<List<_param>> matchFn){
            return matchFn.test(listParams());
        }

        default boolean isParams(Parameter... ps){
            List<_param> _tps = listParams();
            if( _tps.size() == ps.length ){
                for(int i=0;i<ps.length; i++){
                    if( !Objects.equals( _param.of(ps[i]), _tps.get(i))){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        default boolean isParams(_param... _ps){
            List<_param> _tps = listParams();
            if( _tps.size() == _ps.length ){
                for(int i=0;i<_ps.length; i++){
                    if( !Objects.equals( _ps[i], _tps.get(i))){
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        default boolean isVarArg() {
            return getParams().isVarArg();
        }
    }
}
