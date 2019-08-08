package org.jdraft;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.type.ReferenceType;

/**
 * Model of a Java throws clause
 *
 * @author Eric
 */
public final class _throws
        implements _java {

    /**
     *
     * @param nte
     * @return
     */
    public static _throws of( NodeWithThrownExceptions nte ) {
        return new _throws( nte );
    }

    /**
     *
     * @param clazzes
     * @return
     */
    public static _throws of( Class<? extends Throwable>...clazzes ){
        MethodDeclaration md = Ast.method( "void a(){}");
        Arrays.stream( clazzes ).forEach(c -> md.addThrownException(c) );
        return new _throws( md );
    }

    /**
     * Here we are creating a throws clause without a target, so create
     * a "dummy" method to hold the ELEMENTS
     *
     * @param throwsClause
     * @return
     */
    public static _throws of( String... throwsClause ) {
        String t = Text.combine( throwsClause ).trim();
        if( t.length() == 0 ) {
            return of(Ast.method( "void a(){}"));
        }
        if( t.startsWith( "throws " ) ) {
            t = t.substring( "throws ".length() );
        }
        MethodDeclaration md = Ast.method( "void a() throws " + t + System.lineSeparator() + ";" );
        return new _throws( md );
    }

    public final NodeWithThrownExceptions astNodeWithThrows;

    public _throws(){
        this( Ast.method("void m(){}") );
    }

    public _throws( NodeWithThrownExceptions th ) {
        this.astNodeWithThrows = th;
    }

    public NodeList<ReferenceType> ast() {
        return astNodeWithThrows.getThrownExceptions();
    }

    public boolean has( Class<? extends Throwable>... thrownType ){
        Optional<Class<? extends Throwable>> oc = 
            Arrays.stream(thrownType).filter( t -> !astNodeWithThrows.isThrown(t) ).findFirst();
        return !oc.isPresent();
    }
    
    public boolean has( String... thrownType ){
        Optional<String> oc = 
            Arrays.stream(thrownType).filter( t -> !astNodeWithThrows.isThrown(t) ).findFirst();
        return !oc.isPresent();
    }
    
    public boolean has( Class<? extends Throwable> throwType ){
        return astNodeWithThrows.isThrown(throwType);
    }
    
    public boolean has( String name ) {
        return astNodeWithThrows.isThrown( name );
    }
    
    public boolean has( ReferenceType rt ) {
        return astNodeWithThrows.isThrown( rt.asString() );
    }

    /** verify this throws contains all of the ReferenceTypes in rt */
    public boolean hasAll( List<ReferenceType> rt ){

        for(int i=0;i<rt.size();i++){
            if( !has(rt.get(i) ) ){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param clazzes
     * @return
     */
    public boolean is( Class<? extends Throwable>...clazzes ){
        try {
            return of( clazzes ).equals( this );
        }
        catch( Exception e ) {
        }
        return false;
    }

    /**
     *
     * @param str
     * @return
     */
    public boolean is( String... str ) {
        try {
            return of( str ).equals( this );
        }
        catch( Exception e ) {
        }
        return false;
    }

    /**
     *
     * @param elementAction
     */
    public void forEach( Consumer<ReferenceType> elementAction ) {
        this.astNodeWithThrows.getThrownExceptions().forEach( elementAction );
    }

    /**
     *
     * @param matchFn
     * @param elementAction
     */
    public void forEach( Predicate<ReferenceType> matchFn,
                         Consumer<ReferenceType> elementAction ) {
        this.astNodeWithThrows.getThrownExceptions().stream().filter( matchFn ).forEach( elementAction );
    }

    /**
     *
     * @return
     */
    public List<ReferenceType> list() {
        return this.astNodeWithThrows.getThrownExceptions();
    }

    /**
     *
     * @param matchFn
     * @return
     */
    public List<ReferenceType> list(Predicate<ReferenceType> matchFn ) {
        return (List<ReferenceType>)this.astNodeWithThrows.getThrownExceptions().stream().filter( matchFn ).collect( Collectors.toList() );
    }

    /**
     *
     * @param throwTypes
     * @return
     */
    public _throws addAll( Collection<ReferenceType> throwTypes ){
        throwTypes.forEach( t ->this.astNodeWithThrows.addThrownException(t));
        return this;
    }

    /**
     *
     * @param throwsClasses
     * @return
     */
    public _throws add( Class<? extends Throwable>... throwsClasses ) {
        Arrays.stream( throwsClasses ).forEach( t ->this.astNodeWithThrows.addThrownException(t));
        return this;
    }

    /**
     *
     * @param elements
     * @return
     */
    public _throws add( String... elements ) {
        Arrays.stream( elements ).forEach( t -> this.astNodeWithThrows.getThrownExceptions().add( Ast.typeRef( t)  ) );
        return this;
    }

    /**
     *
     * @param elements
     * @return
     */
    public _throws add( ReferenceType... elements ) {
        Arrays.stream( elements ).forEach( t -> this.astNodeWithThrows.getThrownExceptions().add( t ) );
        return this;
    }

    /**
     *
     * @param elements
     * @return
     */
    public _throws remove( ReferenceType... elements ) {
        Arrays.stream( elements ).forEach( t -> this.astNodeWithThrows.getThrownExceptions().remove( t ) );
        return this;
    }

    /**
     *
     * @param matchFn
     * @return
     */
    public _throws remove( Predicate<ReferenceType> matchFn ) {
        list( matchFn ).forEach( t -> this.astNodeWithThrows.getThrownExceptions().remove( t ) );
        return this;
    }

    /**
     *
     * @return
     */
    public _throws clear() {
        this.astNodeWithThrows.getThrownExceptions().clear();
        return this;
    }

    /**
     *
     * @return
     */
    public int size() {
        return this.astNodeWithThrows.getThrownExceptions().size();
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return this.astNodeWithThrows.getThrownExceptions().isEmpty();
    }

    /**
     *
     * @param node
     * @return
     */
    public int indexOf( ReferenceType node ) {
        return this.astNodeWithThrows.getThrownExceptions().indexOf( node );
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
        final _throws other = (_throws)obj;
        if( this.astNodeWithThrows == other.astNodeWithThrows ) {
            return true; //two _throws pointing to the same NodeWithThrownException
        }
        if( !Ast.typesEqual( astNodeWithThrows.getThrownExceptions(), other.astNodeWithThrows.getThrownExceptions())){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Ast.typesHashCode( this.astNodeWithThrows.getThrownExceptions() );
    }

    @Override
    public String toString() {
        if( this.astNodeWithThrows.getThrownExceptions().isEmpty() ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append( "throws " );
        for( int i = 0; i < this.astNodeWithThrows.getThrownExceptions().size(); i++ ) {
            if( i > 0 ) {
                sb.append( ", " );
            }
            sb.append( this.astNodeWithThrows.getThrownExceptions().get( i ) );
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public NodeWithThrownExceptions astHolder() {
        return this.astNodeWithThrows;
    }

    /**
     *
     * @param index
     * @return
     */
    public ReferenceType get(int index){
        return this.astNodeWithThrows.getThrownException(index);       
    }

    /**
     *
     * @param elements
     * @return
     */
    public _throws add( _typeRef<ReferenceType>... elements ) {
        Arrays.stream( elements ).forEach( t -> this.astNodeWithThrows.addThrownException( (ReferenceType)t.ast() ) );
        return this;
    }

    /**
     *
     * @param elements
     * @return
     */
    public _throws remove( _typeRef<ReferenceType>... elements ) {
        Arrays.stream( elements ).forEach( t -> this.astNodeWithThrows.getThrownExceptions().remove( (ReferenceType)t.ast() ) );
        return this;
    }

    /**
     *
     * @param element
     * @return
     */
    public int indexOf( _typeRef<ReferenceType> element ) {
        return this.astNodeWithThrows.getThrownExceptions().indexOf( (ReferenceType)element.ast() );
    }
    
    /**
     * examples:
     * {@link _method}
     * {@link _constructor}
     *
     * @author Eric
     * @param <_HT> the hasThrows container {@link _method} {@link _constructor}
     */
    public interface _hasThrows<_HT extends _hasThrows> extends _java {

        _throws getThrows();

        default boolean hasThrows() {
            return !getThrows().isEmpty();
        }
        
        default _HT addThrows(String... throwExceptions) {
            Arrays.stream(throwExceptions).forEach(t -> addThrows(t));
            return (_HT)this;
        }
        
        default _HT addThrows(String throwException) {
            getThrows().astNodeWithThrows.addThrownException((ReferenceType) Ast.typeRef(throwException));
            return (_HT)this;
        }    

        default _HT addThrows(Class<? extends Throwable>... throwExceptions) {
            Arrays.stream(throwExceptions).forEach(t -> addThrows(t));
            return (_HT)this;
        }
    
        default _HT addThrows(Class<? extends Throwable> throwException) {
            getThrows().astNodeWithThrows.addThrownException((ReferenceType) Ast.typeRef(throwException));
            return (_HT)this;
        }
        
        default _HT setThrows(NodeList<ReferenceType> thrws ){
            getThrows().astNodeWithThrows.setThrownExceptions(thrws);
            return (_HT)this;
        }
    
        default boolean hasThrow(Class<? extends Throwable> clazz) {
            return getThrows().astNodeWithThrows.isThrown(clazz)
                || getThrows().astNodeWithThrows.isThrown(clazz.getCanonicalName());
        }

        default boolean hasThrow(String name) {
            return getThrows().astNodeWithThrows.isThrown(name);
        }

        default boolean hasThrow(ReferenceType rt) {
            return this.getThrows().has(rt);
        }
        
        default _HT removeThrow(Class<? extends Throwable> thrownClass ){
            getThrows().list( t -> t.asString().equals( thrownClass.getCanonicalName()) || 
                    t.asString().equals( thrownClass.getSimpleName()) ).forEach( t -> t.remove() );
            return (_HT)this;
        }
        
        default _HT removeThrow(Predicate<ReferenceType> throwPredicate ){
            getThrows().astNodeWithThrows.getThrownExceptions().removeIf(throwPredicate);
            return (_HT)this;
        }
    }
}
