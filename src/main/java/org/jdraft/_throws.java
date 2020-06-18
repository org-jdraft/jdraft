package org.jdraft;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithThrownExceptions;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Stencil;
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
        implements _tree._view<_throws>, _tree._group<ReferenceType, _typeRef, _throws> {

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
        MethodDeclaration md = Ast.methodDeclaration( "void $name$(){}");
        Arrays.stream( clazzes ).forEach(c -> md.addThrownException(c) );
        return new _throws( md );
    }

    public static _throws of(){
        MethodDeclaration md = Ast.methodDeclaration( "void $name$(){}");
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
            return of(Ast.methodDeclaration( "void $name$(){}"));
        }
        if( t.startsWith( "throws " ) ) {
            t = t.substring( "throws ".length() );
        }
        MethodDeclaration md = Ast.methodDeclaration( "void $name$() throws " + t + System.lineSeparator() + ";" );
        return new _throws( md );
    }

    public static _feature._many<_throws, _typeRef> THROWS = new _feature._many<>(_throws.class, _typeRef.class,
            _feature._id.THROWS,
            _feature._id.THROW,
            a -> a.list(),
            (_throws p, List<_typeRef> _ses) -> p.set(_ses), PARSER, s->_typeRef.of(s))
            .setOrdered(false);

    public static _feature._features<_throws> FEATURES = _feature._features.of(_throws.class,  PARSER, THROWS );

    public final NodeWithThrownExceptions parentNode;

    public _throws(){
        this( Ast.methodDeclaration("void $name$(){}") );
    }

    public _throws( NodeWithThrownExceptions th ) {
        this.parentNode = th;
    }

    public <N extends Node> N anchorNode(){
        return (N) parentNode;
    }

    public _feature._features<_throws> features(){
        return FEATURES;
    }

    public NodeList<ReferenceType> ast() {
        return parentNode.getThrownExceptions();
    }

    public boolean isThrown(Class<? extends Throwable>... thrownType ){
        Optional<Class<? extends Throwable>> oc =
            Arrays.stream(thrownType).filter( t -> !parentNode.isThrown(t) ).findFirst();
        return !oc.isPresent();
    }
    
    public boolean isThrown(String... thrownType ){
        Optional<String> oc = 
            Arrays.stream(thrownType).filter( t -> !parentNode.isThrown(t) ).findFirst();
        return !oc.isPresent();
    }
    
    public boolean isThrown(Class<? extends Throwable> throwType ){
        return parentNode.isThrown(throwType);
    }
    
    public boolean isThrown(String name ) {
        return parentNode.isThrown( name );
    }

    public boolean isThrown(Type rt ) {
        return parentNode.isThrown(rt.asString());
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
     * @param strs
     * @return
     */
    public boolean is( String... strs ) {
        String str = Text.combine(strs);
        if( str.startsWith("$") && str.endsWith("$")){
            Stencil st = Stencil.of(str);
            if( st.isMatchAny() ){
                return true;
            }
        }
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
        throwTypes.forEach( t ->this.parentNode.addThrownException(t));
        return this;
    }

    /**
     *
     * @param throwsClasses
     * @return
     */
    public _throws add( Class<? extends Throwable>... throwsClasses ) {
        Arrays.stream( throwsClasses ).forEach( t ->this.parentNode.addThrownException(t));
        return this;
    }

    /**
     *
     * @param elements
     * @return
     */
    public _throws add( String... elements ) {
        Arrays.stream( elements ).forEach( t -> this.parentNode.getThrownExceptions().add( Types.of( t)  ) );
        return this;
    }

    /**
     *
     * @return
     */
    public _throws clear() {
        this.parentNode.getThrownExceptions().clear();
        return this;
    }

    @Override
    public _throws copy() {
        return _throws.of(this.parentNode);
    }

    @Override
    public List<_typeRef> list() {
        List<_typeRef> trs = new ArrayList<>();
        this.parentNode.getThrownExceptions().stream().forEach( (t) -> trs.add( _typeRef.of((ReferenceType)t)) );
        return trs;
    }

    @Override
    public NodeList<ReferenceType> astList() {
        return this.parentNode.getThrownExceptions();
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
        if( this.parentNode == other.parentNode) {
            return true; //two _throws pointing to the same NodeWithThrownException
        }
        if( !Types.equal( parentNode.getThrownExceptions(), other.parentNode.getThrownExceptions())){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Types.hash( this.parentNode.getThrownExceptions() );
    }

    @Override
    public String toString() {
        if( this.parentNode.getThrownExceptions().isEmpty() ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append( "throws " );
        for(int i = 0; i < this.parentNode.getThrownExceptions().size(); i++ ) {
            if( i > 0 ) {
                sb.append( ", " );
            }
            sb.append( this.parentNode.getThrownExceptions().get( i ) );
        }
        return sb.toString();
    }

    @Override
    public String toString(PrettyPrinterConfiguration ppc) {
        if( this.parentNode.getThrownExceptions().isEmpty() ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append( "throws " );
        for(int i = 0; i < this.parentNode.getThrownExceptions().size(); i++ ) {
            if( i > 0 ) {
                sb.append( ", " );
            }
            sb.append( this.parentNode.getThrownExceptions().get( i ).toString(ppc) );
        }
        return sb.toString();
    }

    /**
     *
     * @param elements
     * @return
     */
    public _throws add( _typeRef<ReferenceType>... elements ) {
        Arrays.stream( elements ).forEach( t -> this.parentNode.addThrownException( (ReferenceType)t.node() ) );
        return this;
    }

    /**
     *
     * @param elements
     * @return
     */
    public _throws remove( _typeRef<ReferenceType>... elements ) {
        Arrays.stream( elements ).forEach( t -> this.parentNode.getThrownExceptions().remove( (ReferenceType)t.node() ) );
        return this;
    }

    public _throws set(List<_typeRef> trs ){
        this.parentNode.getThrownExceptions().clear();
        trs.forEach( t-> this.parentNode.addThrownException( (ReferenceType)t.node()));
        return this;
    }

    /**
     *
     * @param element
     * @return
     */
    public int indexOf( _typeRef<ReferenceType> element ) {
        return this.parentNode.getThrownExceptions().indexOf( (ReferenceType)element.node() );
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
            getThrows().parentNode.addThrownException((ReferenceType) Types.of(throwException));
            return (_WT)this;
        }    

        default _WT addThrows(Class<? extends Throwable>... throwExceptions) {
            Arrays.stream(throwExceptions).forEach(t -> addThrows(t));
            return (_WT)this;
        }
    
        default _WT addThrows(Class<? extends Throwable> throwException) {
            getThrows().parentNode.addThrownException((ReferenceType) Types.of(throwException));
            return (_WT)this;
        }

        default _WT setThrows(_throws _th){
            getThrows().parentNode.setThrownExceptions(_th.astList());
            return (_WT)this;
        }

        default _WT setThrows(NodeList<ReferenceType> thrws ){
            getThrows().parentNode.setThrownExceptions(thrws);
            return (_WT)this;
        }
    
        default boolean hasThrow(Class<? extends Throwable> clazz) {
            return getThrows().parentNode.isThrown(clazz)
                || getThrows().parentNode.isThrown(clazz.getCanonicalName());
        }

        default boolean hasThrow(String name) {
            return getThrows().parentNode.isThrown(name);
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
            getThrows().astList(t -> t.asString().equals( thrownClass.getCanonicalName()) ||
                    t.asString().equals( thrownClass.getSimpleName()) ).forEach( t -> t.remove() );
            return (_WT)this;
        }
        
        default _WT removeThrows(Predicate<ReferenceType> throwPredicate ){
            getThrows().parentNode.getThrownExceptions().removeIf(throwPredicate);
            return (_WT)this;
        }
    }
}
