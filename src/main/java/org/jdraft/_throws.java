package org.jdraft;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Text;

/**
 * Representation of the source of a Java throws clause attached to a constructor or method
 * i.e. <PRE> "throws IOException, URISyntaxException" in:
 *     public static void main(String[] args) throws IOException, URISyntaxException {
 *      //...
 *     }
 * </PRE>
 *
 * @author Eric
 */
public final class _throws
        implements _tree._group<ReferenceType, _typeRef, _throws> {

    public static final Function<String, _throws> PARSER = s-> _throws.of(s);

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
        MethodDeclaration md = Ast.methodDeclaration( "void $$(){}");
        Arrays.stream( clazzes ).forEach(c -> md.addThrownException(c) );
        return new _throws( md );
    }

    public static _throws of(){
        MethodDeclaration md = Ast.methodDeclaration( "void $$(){}");
        return of( md );
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
            return of(Ast.methodDeclaration( "void a(){}"));
        }
        if( t.startsWith( "throws " ) ) {
            t = t.substring( "throws ".length() );
        }
        MethodDeclaration md = Ast.methodDeclaration( "void a() throws " + t + System.lineSeparator() + ";" );
        return new _throws( md );
    }

    public static _feature._many<_throws, _typeRef> THROWS = new _feature._many<>(_throws.class, _typeRef.class,
            _feature._id.THROWS,
            _feature._id.THROW,
            a -> a.list(),
            (_throws p, List<_typeRef> _ses) -> p.set(_ses), PARSER, s->_typeRef.of(s))
            .setOrdered(false);

    public static _feature._features<_throws> FEATURES = _feature._features.of(_throws.class,  PARSER, THROWS );

    public final NodeWithThrownExceptions astNodeWithThrows;

    public _throws(){
        this( Ast.methodDeclaration("void m(){}") );
    }

    public _throws( NodeWithThrownExceptions th ) {
        this.astNodeWithThrows = th;
    }

    public _feature._features<_throws> features(){
        return FEATURES;
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

    public boolean has( Type rt ) {
        return astNodeWithThrows.isThrown(rt.asString());
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

    public boolean is(String code){
        return is(new String[]{code});
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
        Arrays.stream( elements ).forEach( t -> this.astNodeWithThrows.getThrownExceptions().add( Types.of( t)  ) );
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

    @Override
    public _throws copy() {
        return _throws.of(this.astNodeWithThrows);
    }

    @Override
    public List<_typeRef> list() {
        List<_typeRef> trs = new ArrayList<>();
        this.astNodeWithThrows.getThrownExceptions().stream().forEach( (t) -> trs.add( _typeRef.of((ReferenceType)t)) );
        return trs;
    }

    @Override
    public NodeList<ReferenceType> listAstElements() {
        return this.astNodeWithThrows.getThrownExceptions();
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
        if( !Types.equal( astNodeWithThrows.getThrownExceptions(), other.astNodeWithThrows.getThrownExceptions())){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Types.hash( this.astNodeWithThrows.getThrownExceptions() );
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

    //((Node)a).toString(ppc)


    @Override
    public String toString(PrettyPrinterConfiguration ppc) {
        if( this.astNodeWithThrows.getThrownExceptions().isEmpty() ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append( "throws " );
        for( int i = 0; i < this.astNodeWithThrows.getThrownExceptions().size(); i++ ) {
            if( i > 0 ) {
                sb.append( ", " );
            }
            sb.append( this.astNodeWithThrows.getThrownExceptions().get( i ).toString(ppc) );
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

    public _throws set(List<_typeRef> trs ){
        this.astNodeWithThrows.getThrownExceptions().clear();
        trs.forEach( t-> this.astNodeWithThrows.addThrownException( (ReferenceType)t.ast()));
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
     * @param <_WT> the _withThrows container {@link _method} {@link _constructor}
     */
    public interface _withThrows<_WT extends _withThrows> extends _java._domain {

        _throws getThrows();

        default boolean hasThrows() {
            return !getThrows().isEmpty();
        }
        
        default _WT addThrows(String... throwExceptions) {
            Arrays.stream(throwExceptions).forEach(t -> addThrows(t));
            return (_WT)this;
        }
        
        default _WT addThrows(String throwException) {
            getThrows().astNodeWithThrows.addThrownException((ReferenceType) Types.of(throwException));
            return (_WT)this;
        }    

        default _WT addThrows(Class<? extends Throwable>... throwExceptions) {
            Arrays.stream(throwExceptions).forEach(t -> addThrows(t));
            return (_WT)this;
        }
    
        default _WT addThrows(Class<? extends Throwable> throwException) {
            getThrows().astNodeWithThrows.addThrownException((ReferenceType) Types.of(throwException));
            return (_WT)this;
        }

        default _WT setThrows(_throws _th){
            getThrows().astNodeWithThrows.setThrownExceptions(_th.listAstElements());
            return (_WT)this;
        }

        default _WT setThrows(NodeList<ReferenceType> thrws ){
            getThrows().astNodeWithThrows.setThrownExceptions(thrws);
            return (_WT)this;
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

        default _WT forThrows(Consumer<_typeRef> cm){
            this.getThrows().list().forEach(cm);
            return (_WT)this;
        }

        default _WT forThrows(Predicate<_typeRef> pc, Consumer<_typeRef> cm){
            this.getThrows().list(pc).forEach(cm);
            return (_WT)this;
        }

        default _WT removeThrows(Class<? extends Throwable> thrownClass ){
            getThrows().listAstElements( t -> t.asString().equals( thrownClass.getCanonicalName()) ||
                    t.asString().equals( thrownClass.getSimpleName()) ).forEach( t -> t.remove() );
            return (_WT)this;
        }
        
        default _WT removeThrows(Predicate<ReferenceType> throwPredicate ){
            getThrows().astNodeWithThrows.getThrownExceptions().removeIf(throwPredicate);
            return (_WT)this;
        }
    }
}
