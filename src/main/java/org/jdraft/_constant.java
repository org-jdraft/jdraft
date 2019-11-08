package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * individual constant within an _enum
 * <PRE>
 * //A, and B are CONSTANTS of _enum E
 * enum E{
 *    A(),
 *    B('a');
 * }
 * </PRE>
 */
public class _constant implements _javadoc._hasJavadoc<_constant>,
        _anno._hasAnnos<_constant>,_method._hasMethods<_constant>, _field._hasFields<_constant>,
        _declared<EnumConstantDeclaration, _constant> {

    public static _constant of( String... ecd ){
        return of(Ast.constantDecl(ecd));
    }

    public static _constant of( EnumConstantDeclaration ecd ){
        return new _constant( ecd);
    }

    public _constant( EnumConstantDeclaration ecd ){
        this.astConstant = ecd;
    }

    /**
     * This is a "prototype" object, it exists as a temporary
     * "holder" to represent an enum constant
     * <PRE>
     *     _enum _e = _enum.of("E", new Object(){
     *         _constant Yes, No, MaybeSo;
     *     }
     *     //is equivalent to :
     *     public enum E{
     *         Yes, No, MaybeSo;
     *     }
     * </PRE>
     * <PRE>
     *     //for example:
     *     _enum _e = _enum.of("E", new Object(){
     *         _constant C;
     *         _constant F = new _constant("A");    //prototype constructor (to pass args in)
     *         _constant G = new _constant("A", 1);
     *         _constant H = new _constant("B",2){ //prototype constructor (args & body w field(s) and method(s))
     *             int field = 200;
     *             public String toString(){
     *                 return "Hello "+field;
     *             }
     *         }
     *     }
     *     //equivlanet to:
     *     enum E{
     *         C,
     *         F("A"),
     *         G("A",1),
     *         H("B",2){
     *             int field = 200;
     *             public String toString(){
     *                 return "Hello "+field;
     *             }
     *         }
     *     }
     *
     * </PRE>
     */
    public _constant(Object...constructorArgs){

        //throw new RuntimeException("This constant constructor only exists for 'prototypes', it does not actualy build a _constant");
    }

    public EnumConstantDeclaration astConstant;

    @Override
    public EnumConstantDeclaration ast(){
        return astConstant;
    }

    @Override
    public _constant copy(){
        return of(this.astConstant.toString());
    }

    public boolean hasArguments(){
        return this.astConstant.getArguments().size() > 0;
    }

    @Override
    public boolean is(String...stringRep){
        try{
            return is(Ast.constantDecl(stringRep));
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean is(EnumConstantDeclaration ecd ){
        return of(ecd).equals(this);
    }

    @Override
    public _constant name( String name ){
        this.astConstant.setName( name );
        return this;
    }

    public _constant addArgument( int i){
        return addArgument( Ex.of(i) );
    }

    public _constant addArgument( boolean b){
        return addArgument( Ex.of(b) );
    }

    public _constant addArgument( float f){
        return addArgument( Ex.of(f) );
    }

    public _constant addArgument( long l){
        return addArgument( Ex.of(l) );
    }

    public _constant addArgument( double d){
        return addArgument( Ex.of(d) );
    }

    public _constant addArgument( char c){
        return addArgument( Ex.of(c) );
    }

    public _constant addArgument( Expression e ){
        this.astConstant.addArgument( e );
        return this;
    }

    public _constant addArgument( String str ){
        this.astConstant.addArgument( str );
        return this;
    }

    public _constant clearArguments(){
        this.astConstant.getArguments().clear();
        return this;
    }

    public _constant setArgument( int index, Expression e){
        this.astConstant.getArguments().set( index, e );
        return this;
    }

    public _constant setArguments( NodeList<Expression> arguments ){
        this.astConstant.setArguments(arguments);
        return this;
    }

    public _constant setArguments( List<Expression> arguments ){
        NodeList<Expression> nles = new NodeList<>();
        nles.addAll(arguments);
        this.astConstant.setArguments(nles);
        return this;
    }

    public _constant setArgument( int index, boolean b){
        return setArgument(index, Ex.of(b));
    }

    public _constant setArgument( int index, int i){
        return setArgument(index, Ex.of(i));
    }

    public _constant setArgument( int index, char c){
        return setArgument(index, Ex.of(c));
    }

    public _constant setArgument( int index, float f){
        return setArgument(index, Ex.of(f));
    }

    public _constant setArgument( int index, long l){
        return setArgument(index, Ex.of(l));
    }

    public _constant setArgument(int index, double d){
        return setArgument(index, Ex.of(d));
    }

    public _constant forArguments( Consumer<Expression> expressionAction ){
        this.listArguments().forEach(expressionAction);
        return this;
    }

    public _constant forArguments(Predicate<Expression> expressionMatchFn, Consumer<Expression> expressionAction ){
        this.listArguments(expressionMatchFn).forEach(expressionAction);
        return this;
    }

    public _constant removeArgument( int index ){
        this.astConstant.getArguments().remove(index);
        return this;
    }

    public _constant removeArguments( Predicate<Expression> argumentMatchFn ){
        listArguments(argumentMatchFn).forEach(e -> e.removeForced() );
        return this;
    }

    public List<Expression> listArguments(){
        return this.astConstant.getArguments();
    }

    public List<Expression>listArguments(Predicate<Expression> expressionMatchFn){
        return this.astConstant.getArguments().stream().filter( expressionMatchFn ).collect(Collectors.toList() );
    }


    public Expression getArgument( int index ){
        return listArguments().get( index );
    }

    @Override
    public _annos getAnnos() {
        return _annos.of(this.astConstant );
    }

    @Override
    public List<_method> listMethods() {
        List<_method> ms = new ArrayList<>();
        this.astConstant.getClassBody().stream().filter(b -> b instanceof MethodDeclaration).forEach(m -> ms.add(_method.of( (MethodDeclaration)m )) );
        return ms;
    }

    @Override
    public List<_method> listMethods(Predicate<_method> _methodMatchFn ){
        return listMethods().stream().filter(_methodMatchFn).collect(Collectors.toList());
    }


    @Override
    public _constant method( MethodDeclaration method ) {
        this.astConstant.getClassBody().add( method );
        return this;
    }

    @Override
    public _constant field( VariableDeclarator field ) {
        if(! field.getParentNode().isPresent()){
            throw new _jdraftException("cannot add Var without parent FieldDeclaration");
        }
        FieldDeclaration fd = (FieldDeclaration)field.getParentNode().get();
        //we already added it to the parent
        if( this.astConstant.getClassBody().contains( fd ) ){
            if( !fd.containsWithinRange( field ) ){
                fd.addVariable( field );
            }
            return this;
        }
        this.astConstant.getClassBody().add( fd );
        return this;
    }

    @Override
    public List<_field> listFields() {
        List<_field> ms = new ArrayList<>();
        this.astConstant.getClassBody().stream().filter(b -> b instanceof FieldDeclaration)
                .forEach( f->{
                    if( ((FieldDeclaration)f).getVariables().size() == 1 ){
                        ms.add(_field.of( ((FieldDeclaration)f).getVariable( 0 ) ) );
                    } else{
                        for(int i=0;i<((FieldDeclaration)f).getVariables().size();i++){
                            ms.add(_field.of( ((FieldDeclaration)f).getVariable( i ) ) );
                        }
                    }
                });
        return ms;
    }

    @Override
    public String getName(){
        return this.astConstant.getNameAsString();
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
        final _constant other = (_constant)obj;
        if( this.astConstant == other.astConstant){
            return true; //two _constant pointing to the same AstEnumDeclaration
        }
        if( !Ex.equivalentAnnos(this.astConstant, other.astConstant ) ){
            return false;
        }
        if( !Objects.equals( this.getJavadoc(), other.getJavadoc() ) ) {
            return false;
        }
        if( !Objects.equals( this.getName(), other.getName() ) ) {
            return false;
        }
        if( !Objects.equals(this.listArguments(), other.listArguments() ) ) {
            return false;
        }
        Set<_method> tms = new HashSet<>();
        Set<_method> oms = new HashSet<>();
        tms.addAll( this.listMethods());
        oms.addAll( other.listMethods());

        if( !Objects.equals( tms, oms ) ) {
            return false;
        }
        Set<_field> tfs = new HashSet<>();
        Set<_field> ofs = new HashSet<>();
        tfs.addAll( this.listFields());
        ofs.addAll( other.listFields());
        if( !Objects.equals( tfs, ofs ) ) {
            return false;
        }
        return true;
    }

    @Override
    public Map<_java.Component, Object> components( ) {
        Map<_java.Component, Object> parts = new HashMap<>();
        parts.put( _java.Component.ANNOS, this.listAnnos() );
        parts.put( _java.Component.JAVADOC, this.getJavadoc() );
        parts.put( _java.Component.NAME, this.getName());
        parts.put( _java.Component.ARGUMENTS, this.listArguments());
        parts.put( _java.Component.METHODS, this.listMethods() );
        parts.put( _java.Component.FIELDS, this.listFields() );
        return parts;
    }

    @Override
    public String toString(){
        return this.astConstant.toString();
    }


    @Override
    public int hashCode() {
        int hash = 7;
        Set<_method> tms = new HashSet<>();
        tms.addAll( this.listMethods());

        Set<_field> tfs = new HashSet<>();
        tfs.addAll( this.listFields());

        hash = 13 * hash + Objects.hash( tms, tfs,
                Ex.hashAnnos(astConstant),
                getJavadoc(),
                getName(), listArguments() );
        return hash;
    }
}