package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Text;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.jdraft.Ast.fieldDeclaration;

/**
 * Representation of the Java source code of a grouping of {@link _annoExpr} (s)
 * expressions ({@link AnnotationExpr}) annotating a Class, Field, Method, Enum Constant, etc.
 *
 * @see java.lang.reflect.AnnotatedElement Eleemnt that is annotated with the VM
 * @see _annoExpr a single @anno reference
 * @see _annotation the _type representation of the source declaration of an _anno instance (@interface Ann{})
 * @author Eric
 */
public final class _annoExprs
        implements _tree._group<AnnotationExpr, _annoExpr, _annoExprs> {

    public static final Function<String, _annoExprs> PARSER = s-> _annoExprs.of(s);

    public _feature._features<_annoExprs> features(){
        return FEATURES;
    }

    public static _feature._many<_annoExprs, _annoExpr> ANNOS = new _feature._many<>(_annoExprs.class, _annoExpr.class,
            _feature._id.ANNOS, _feature._id.ANNO_EXPR,
            as->as.list(),
            (_annoExprs as, List<_annoExpr> anns)-> as.set(anns),
            PARSER, s-> _annoExpr.of(s))
            .setOrdered(false); /* the order of the annos isnt semantically important {@A @B @C === @B @A @C} */

    public static _feature._features<_annoExprs> FEATURES = _feature._features.of(_annoExprs.class,  PARSER, ANNOS);

    /** A reference to the container entity that is being annotated*/
    public final NodeWithAnnotations astAnnNode;

    public NodeWithAnnotations astHolder() {
        return astAnnNode;
    }

    public static _annoExprs of(String... anns ) {
        //String f = Text.combine( anns ) + System.lineSeparator() + "NOT_A_REAL_FIELD AST_ANNO_HOLDER;";
        String f = Text.combine( anns ) + System.lineSeparator() + "UNKNOWN unknown;";
        FieldDeclaration fd = fieldDeclaration( f );
        _annoExprs _as = new _annoExprs( fd );
        return _as;
    }

    public static _annoExprs of(Class<? extends Annotation> annotationClass){
        return of( _annoExpr.of(annotationClass));
    }

    public static _annoExprs of(_annoExpr _ar){
        _annoExprs _ars = of();
        _ars.add(_ar);
        return _ars;
    }

    public static _annoExprs of(_annoExpr... _arr){
        _annoExprs _ars = of();
        _ars.add(_arr);
        return _ars;
    }

    public static _annoExprs of(Class<? extends Annotation>... annotationClasses){
        _annoExprs _ars = of();
        _ars.add(annotationClasses);
        return _ars;

    }

    public static _annoExprs of(){
        return new _annoExprs(fieldDeclaration( "UNKNOWN unknown;") );
    }

    public static _annoExprs of(String annos){
        return of( new String[]{annos});
    }

    /**
     * Look through the anonymous Object to find some annotated entity
     * and extract & model the first annotation as a _draft {@link _annoExprs}
     * @param anonymousObject an anonymous Object containing an annotated entity
     * @return the {@link _annoExprs} _draft object representing the annos
     */
    public static _annoExprs of(Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expr.newExpr(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( bd );
    }

    public static _annoExprs of(List<AnnotationExpr> aes){
        _annoExprs _as = _annoExprs.of();
        aes.forEach(_a -> _as.add(_a));
        return _as;
    }

    public static _annoExprs of(TypeParameter astTypeParam ){
        return new _annoExprs( astTypeParam );
    }

    public static <T extends Type> _annoExprs of(T t ){
        return new _annoExprs( new _annoExpr.Type_annoPatch(t) );
    }

    public static _annoExprs of(NodeWithAnnotations astAnns ) {
        return new _annoExprs( astAnns );
    }

    public _annoExprs(NodeWithAnnotations astAnns ) {
        this.astAnnNode = astAnns;
    }

    public _annoExprs add(String... annos ) {
        for( String anno : annos ) {
            this.astAnnNode.addAnnotation(Ast.annotationExpr(anno ) );
        }
        return this;
    }

    public _annoExprs add(Class<? extends Annotation>... anns ) {
        for( Class<? extends Annotation> ann : anns ) {
            this.astAnnNode.addAnnotation( Ast.annotationExpr( "@" + ann.getSimpleName() ) );
        }
        return this;
    }

    public _annoExprs addAll(Collection<_annoExpr> _as ){
        _as.forEach(a -> add(a));
        return this;
    }

    public _annoExpr get(String name ) {
        List<_annoExpr> a = this.list( name );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

    public _annoExpr get(Class<? extends Annotation> clazz ) {
        List<_annoExpr> a = this.list( clazz );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

    public _annoExpr getAt( int index){
        return _annoExpr.of(this.ast().get(index));
    }

    @Override
    public List<_annoExpr> list() {
        return list( t -> true );
    }

    /**
     * Lists all ANNOTATIONS of the NAME
     *
     * @param name
     * @return
     */
    public List<_annoExpr> list(String name ) {
        return list( a -> a.getName().equals( name ) );
    }

    /**
     * Lists all _anno that are of the Class
     *
     * @param clazz
     * @return
     */
    public List<_annoExpr> list(Class<? extends Annotation> clazz ) {
        return list( a -> a.getName().equals( clazz.getSimpleName() )
                || a.getName().equals( clazz.getCanonicalName() )
                || a.getName().endsWith( "."+clazz.getSimpleName() ) );
    }

    /**
     *
     * @param _annoMatchFn
     * @return
     */
    public List<_annoExpr> list(Predicate<_annoExpr> _annoMatchFn ) {
        List<_annoExpr> l = new ArrayList<>();
        this.astAnnNode.getAnnotations().forEach(a -> {
            _annoExpr _a = _annoExpr.of( (AnnotationExpr)a );
            if( _annoMatchFn.test( _a ) ) {
                l.add( _a );
            }
        } );
        return l;
    }

    @Override
    public NodeList<AnnotationExpr> listAstElements() {
        return this.astAnnNode.getAnnotations();
    }

    /**
     * removeIn all ANNOTATIONS that are
     *
     * @param annClasses
     * @return
     */
    public _annoExprs remove(Class<? extends Annotation>... annClasses ) {
        List<Class<? extends Annotation>> ls = new ArrayList<>();
        Arrays.stream( annClasses ).forEach( c -> ls.add( c ) );

        //send a predicate
        return remove( a -> ls.stream().anyMatch(
                c -> c.getSimpleName().equals( a.getName() )
                        || c.getCanonicalName().equals( a.getName() ) ));
    }

    /**
     * removeIn all ANNOTATIONS that have any of the names provided
     *
     * @param annName
     * @return the _annos
     */
    public _annoExprs remove(String... annName ) {
        List<String> ls = new ArrayList<>();
        Arrays.stream( annName ).forEach( c -> ls.add( c ) );

        return remove( a -> ls.stream().anyMatch(
                n -> n.equals( a.getName() ) ));
    }

    public _annoExprs clear() {
        this.astAnnNode.getAnnotations().clear();
        return this;
    }

    public boolean has(Class<? extends Annotation> clazz ) {
        return list( clazz ).size() > 0;
    }

    public void forEach( Class<? extends Annotation> annotationClazz,
                         Consumer<_annoExpr> _annoAction ) {
        list( annotationClazz ).forEach(_annoAction );
    }

    public boolean is(String code){
        return is(new String[]{code});
    }
    /**
     * does this String array represent ALL of the annos?
     * @param annos representation of annos
     * @return true if they are the same
     */
    public boolean is( String... annos ) {
        try {
            return _annoExprs.of( annos ).equals( this );
        }
        catch( Exception e ) {
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if( this.astAnnNode == null ){
            return "";
        }
        this.astAnnNode.getAnnotations().forEach( a -> sb.append( a ).append( System.lineSeparator() ) );
        return sb.toString();
    }

    public String toString(PrettyPrinterConfiguration ppc){
        StringBuilder sb = new StringBuilder();
        if( this.astAnnNode == null ){
            return "";
        }

        this.astAnnNode.getAnnotations().forEach( a -> sb.append( ((Node)a).toString(ppc) ).append( System.lineSeparator() ) );
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if( this.astAnnNode == null ){
            return 0;
        }
        Set<_annoExpr> s = new HashSet<>();
        this.astAnnNode.getAnnotations().forEach( a -> s.add( _annoExpr.of((AnnotationExpr)a) ) ); //add each of the exprs to the set for order
        return s.hashCode();
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
        final _annoExprs other = (_annoExprs)obj;
        if( this.astAnnNode == null ){
            return false;
        }
        if( this.astAnnNode == other.astAnnNode ) {
            return true; //two _annos instances pointing to the saem NodeWithAnnotations instance
        }
        if( this.astAnnNode.getAnnotations().size() != other.astAnnNode.getAnnotations().size() ) {
            return false;
        }
        for( int i = 0; i < this.astAnnNode.getAnnotations().size(); i++ ) {
            AnnotationExpr e = (AnnotationExpr)this.astAnnNode.getAnnotations().get( i );
            //find a matching annotation in other, if one isnt found, then not equal
            if( !other.astAnnNode.getAnnotations().stream().filter(
                    a -> Expr.equal(e, (AnnotationExpr)a ) ).findFirst().isPresent() ) {

                return false;
            }
        }
        return true;
    }

    public NodeList<AnnotationExpr> ast() {
        if( this.astAnnNode != null ) {
            return (NodeList<AnnotationExpr>) astAnnNode.getAnnotations();
        }
        return new NodeList<>();
    }

    public _annoExprs copy() {
        FieldDeclaration fd = Ast.fieldDeclaration( "UnknownType UNKNOWN;" );
        this.astAnnNode.getAnnotations().forEach( a -> fd.addAnnotation( ((AnnotationExpr)a).clone() ) );
        return new _annoExprs( fd );
    }

    /**
     * _model of an entity that may be annotated with multiple Annotations
     * this includes:
     * <UL>
     * {@link _type}
     * {@link _field}
     * {@link _method}
     * {@link _constructor}
     * {@link _param}
     * {@link _entry}
     * {@link _constant}
     * </UL>
     *
     * @author Eric
     *
     * @param <_WA> the container type (that has Annos)
     */
    public interface _withAnnoExprs<_WA extends _withAnnoExprs>
        extends _java._domain {

        /**
         * @return the annos
         */
        _annoExprs getAnnoExprs();

        /**
         * Check if all individual annos match the function
         * @param matchFn
         * @return
         */
        default boolean isAllAnnoExprs(Predicate<_annoExpr> matchFn){
            return listAnnoExprs().stream().allMatch(matchFn);
        }

        /**
         * gets the anno at the index
         * @param index
         * @return
         */
        default _annoExpr getAnnoExpr(int index) {
            return listAnnoExprs().get(index);
        }

        /**
         * apply a function to all annos
         * @param _annoActionFn
         * @return the modified T
         */
        default _WA forAnnoExprs(Consumer<_annoExpr> _annoActionFn){
            getAnnoExprs().forEach(_annoActionFn);
            return (_WA) this;
        }

        /**
         * Selectively apply a function to annos
         * @param _annoMatchFn
         * @param _annoActionFn
         * @return
         */
        default _WA forAnnoExprs(Predicate<_annoExpr> _annoMatchFn, Consumer<_annoExpr> _annoActionFn){
            getAnnoExprs().forEach(_annoMatchFn, _annoActionFn);
            return (_WA) this;
        }

        /**
         * Gets the FIRST annotation that matches the _annoMatchFn predicate
         *
         * @param _annoMatchFn
         * @return the first matching {@link _annoExpr}, or null if none query
         */
        default _annoExpr getAnnoExpr(Predicate<_annoExpr> _annoMatchFn) {
            return getAnnoExprs().first( _annoMatchFn );
        }

        /**
         * Gets the FIRST _anno that matches the annotationClass
         *
         * @param annotationClass the annotationClass
         * @return the first annotation that is of the annotationClass or null
         */
        default _annoExpr getAnnoExpr(Class<? extends Annotation> annotationClass) {
            return getAnnoExprs().get( annotationClass );
        }

        /**
         * Gets the FIRST _anno that matches the annoName
         *
         * @param annoName the NAME of the anno
         * @return the first _anno that has the NAME, or null
         */
        default _annoExpr getAnnoExpr(String annoName) {
            if( annoName.startsWith("@")){
                return getAnnoExprs().get(annoName.substring(1));
            }
            return getAnnoExprs().get( annoName );
        }

        default boolean isAnnoExprs(_annoExpr... _as){
            _annoExprs _tas = getAnnoExprs();
            return Objects.equals(_tas, of(_as));
        }

        default boolean isAnnoExprs(_annoExprs _as){
            _annoExprs _tas = getAnnoExprs();
            return Objects.equals(_tas, _as);
        }

        /**
         *
         * @param annotationClass
         * @return
         */
        default boolean hasAnnoExpr(Class<? extends Annotation> annotationClass) {
            return getAnnoExpr( annotationClass ) != null;
        }

        /**
         * Do we have an anno that has this name?
         * @param name the name of the anno
         * @return
         */
        default boolean hasAnnoExpr(String name) {
            return getAnnoExpr( name ) != null;
        }

        default boolean hasAnnoExpr(Predicate<_annoExpr> _annoMatchFn) {
            return getAnnoExpr( _annoMatchFn ) != null;
        }

        default List<_annoExpr> listAnnoExprs() {
            return getAnnoExprs().list();
        }

        default List<_annoExpr> listAnnoExprs(Predicate<_annoExpr> _annMatchFn) {
            return getAnnoExprs().list( _annMatchFn );
        }

        default List<_annoExpr> listAnnoExprs(String annoName) {
            return getAnnoExprs().list( annoName );
        }

        default List<_annoExpr> listAnnoExprs(Class<? extends Annotation> annotationClass) {
            return getAnnoExprs().list( annotationClass );
        }

        default _WA setAnnoExprs(_annoExprs _as){
            _as.forEach( a -> getAnnoExprs().add(a) );
            return (_WA)this;
        }

        default _WA addAnnoExprs(List<AnnotationExpr> astAnnoList){
            astAnnoList.forEach( a -> getAnnoExprs().add(a) );
            return (_WA)this;
        }

        /**
         * annotate with the ANNOTATIONS and return the _annotated
         *
         * @param _anns ANNOTATIONS to to
         * @return the annotated entity
         */
        default _WA addAnnoExprs(_annoExpr... _anns) {
            getAnnoExprs().add(_anns );
            return (_WA)this;
        }

        /**
         * annotate with the ANNOTATIONS and return the model
         *
         * @param annoClasses
         * @return
         */
        default _WA addAnnoExprs(Class<? extends Annotation>... annoClasses) {
            getAnnoExprs().add(annoClasses );
            return (_WA)this;
        }

        /**
         * accept one or more ANNOTATIONS
         *
         * @param annotations
         * @return
         */
        default _WA addAnnoExprs(String... annotations) {
            getAnnoExprs().add( annotations );
            return (_WA)this;
        }

        /**
         * remove the annotation and return the modified T
         * @param astAnn the annotation
         * @return
         */
        default _WA removeAnnoExpr(AnnotationExpr astAnn){
            getAnnoExprs().remove(astAnn);
            return (_WA)this;
        }

        /**
         * remove the annotation and return the modified T
         * @param _a
         * @return the modified T
         */
        default _WA removeAnnoExpr(_annoExpr _a){
            return removeAnnoExpr( _a.ast() );
        }

        /**
         * removeAll all _anno that accept the _annoMatchFn
         *
         * @param _annoMatchFn
         * @return
         */
        default _WA removeAnnoExprs(Predicate<_annoExpr> _annoMatchFn) {
            getAnnoExprs().remove( _annoMatchFn );
            return (_WA)this;
        }

        /**
         *
         * @return
         */
        default boolean hasAnnoExprs() {
            return !getAnnoExprs().isEmpty();
        }

        /**
         * removeIn all ANNOTATIONS that are of the annotationClass
         *
         * @param annotationClass
         * @return
         */
        default _WA removeAnnoExprs(Class<? extends Annotation> annotationClass) {
            getAnnoExprs().remove( annotationClass );
            return (_WA)this;
        }

        /**
         * removeAll all ANNOTATIONS that have the annoName
         *
         * @param annoNames
         * @return
         */
        default _WA removeAnnoExprs(String... annoNames) {
            getAnnoExprs().remove( annoNames );
            return (_WA)this;
        }

        /**
         * removeIn all ANNOTATIONS
         *
         * @param _annosToRemove
         * @return
         */
        default _WA removeAnnoExprs(List<_annoExpr> _annosToRemove) {
            getAnnoExprs().remove(_annosToRemove.toArray( new _annoExpr[ 0 ] ) );
            return (_WA)this;
        }
    }
}