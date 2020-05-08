package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithAnnotations;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import org.jdraft.text.Text;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.jdraft.Ast.field;

/**
 * Representation of the Java source code of a grouping of {@link _annoRef} (s)
 * expressions ({@link AnnotationExpr}) annotating a Class, Field, Method, Enum Constant, etc.
 *
 * @see java.lang.reflect.AnnotatedElement Eleemnt that is annotated with the VM
 * @see _annoRef a single @anno reference
 * @see _annotation the _type representation of the source declaration of an _anno instance (@interface Ann{})
 * @author Eric
 */
public final class _annoRefs
        implements _java._set<AnnotationExpr, _annoRef, _annoRefs> {

    /** A reference to the container entity that is being annotated*/
    public final NodeWithAnnotations astAnnNode;

    public NodeWithAnnotations astHolder() {
        return astAnnNode;
    }

    public static _annoRefs of(String... anns ) {
        String f = Text.combine( anns ) + System.lineSeparator() + "NOT_A_REAL_FIELD AST_ANNO_HOLDER;";
        FieldDeclaration fd = field( f );
        _annoRefs _as = new _annoRefs( fd );
        return _as;
    }

    public static _annoRefs of(Class<? extends Annotation> annotationClass){
        return of( _annoRef.of(annotationClass));
    }

    public static _annoRefs of(_annoRef _ar){
        _annoRefs _ars = of();
        _ars.add(_ar);
        return _ars;
    }

    public static _annoRefs of(_annoRef... _arr){
        _annoRefs _ars = of();
        _ars.add(_arr);
        return _ars;
    }

    public static _annoRefs of(Class<? extends Annotation>... annotationClasses){
        _annoRefs _ars = of();
        _ars.add(annotationClasses);
        return _ars;

    }

    public static _annoRefs of(){
        return new _annoRefs(field( "NOT_A_REAL_FIELD AST_ANNO_HOLDER;") );
    }

    public static _annoRefs of(String annos){
        return of( new String[]{annos});
    }

    /**
     * Look through the anonymous Object to find some annotated entity
     * and extract & model the first annotation as a _draft {@link _annoRefs}
     * @param anonymousObject an anonymous Object containing an annotated entity
     * @return the {@link _annoRefs} _draft object representing the annos
     */
    public static _annoRefs of(Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Expressions.newEx(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( bd );
    }

    public static _annoRefs of(List<AnnotationExpr> aes){
        _annoRefs _as = _annoRefs.of();
        aes.forEach(_a -> _as.add(_a));
        return _as;
    }

    public static _annoRefs of(TypeParameter astTypeParam ){
        return new _annoRefs( astTypeParam );
    }


    public static <T extends Type> _annoRefs of(T t ){
        return new _annoRefs( new _annoRef.Type_annoPatch(t) );
    }

    public static _annoRefs of(NodeWithAnnotations astAnns ) {
        return new _annoRefs( astAnns );
    }

    public _annoRefs(NodeWithAnnotations astAnns ) {
        this.astAnnNode = astAnns;
    }

    public _annoRefs add(String... annos ) {
        for( String anno : annos ) {
            this.astAnnNode.addAnnotation(Ast.anno(anno ) );
        }
        return this;
    }

    public _annoRefs add(Class<? extends Annotation>... anns ) {
        for( Class<? extends Annotation> ann : anns ) {
            this.astAnnNode.addAnnotation( Ast.anno( "@" + ann.getSimpleName() ) );
        }
        return this;
    }

    public _annoRefs addAll(Collection<_annoRef> _as ){
        _as.forEach(a -> add(a));
        return this;
    }

    public _annoRef get(String name ) {
        List<_annoRef> a = this.list( name );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

    public _annoRef get(Class<? extends Annotation> clazz ) {
        List<_annoRef> a = this.list( clazz );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

    @Override
    public List<_annoRef> list() {
        return list( t -> true );
    }

    /**
     * Lists all ANNOTATIONS of the NAME
     *
     * @param name
     * @return
     */
    public List<_annoRef> list(String name ) {
        return list( a -> a.getName().equals( name ) );
    }

    /**
     * Lists all _anno that are of the Class
     *
     * @param clazz
     * @return
     */
    public List<_annoRef> list(Class<? extends Annotation> clazz ) {
        return list( a -> a.getName().equals( clazz.getSimpleName() )
                || a.getName().equals( clazz.getCanonicalName() )
                || a.getName().endsWith( "."+clazz.getSimpleName() ) );
    }

    /**
     *
     * @param _annoMatchFn
     * @return
     */
    public List<_annoRef> list(Predicate<_annoRef> _annoMatchFn ) {
        List<_annoRef> l = new ArrayList<>();
        this.astAnnNode.getAnnotations().forEach(a -> {
            _annoRef _a = _annoRef.of( (AnnotationExpr)a );
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
    public _annoRefs remove(Class<? extends Annotation>... annClasses ) {
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
    public _annoRefs remove(String... annName ) {
        List<String> ls = new ArrayList<>();
        Arrays.stream( annName ).forEach( c -> ls.add( c ) );

        return remove( a -> ls.stream().anyMatch(
                n -> n.equals( a.getName() ) ));
    }

    public _annoRefs clear() {
        this.astAnnNode.getAnnotations().clear();
        return this;
    }

    public boolean has(Class<? extends Annotation> clazz ) {
        return list( clazz ).size() > 0;
    }

    public void forEach( Class<? extends Annotation> annotationClazz,
                         Consumer<_annoRef> _annoAction ) {
        list( annotationClazz ).forEach(_annoAction );
    }

    /**
     * does this String array represent ALL of the annos?
     * @param annos representation of annos
     * @return true if they are the same
     */
    public boolean is( String... annos ) {
        try {
            return _annoRefs.of( annos ).equals( this );
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

    @Override
    public int hashCode() {
        if( this.astAnnNode == null ){
            return 0;
        }
        Set<_annoRef> s = new HashSet<>();
        this.astAnnNode.getAnnotations().forEach( a -> s.add( _annoRef.of((AnnotationExpr)a) ) ); //add each of the exprs to the set for order
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
        final _annoRefs other = (_annoRefs)obj;
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
                    a -> Expressions.equal(e, (AnnotationExpr)a ) ).findFirst().isPresent() ) {

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

    public _annoRefs copy() {
        FieldDeclaration fd = Ast.field( "NOT_A_REAL_FIELD AST_ANNO_PARENT;" );
        this.astAnnNode.getAnnotations().forEach( a -> fd.addAnnotation( ((AnnotationExpr)a).clone() ) );
        return new _annoRefs( fd );
    }

    /**
     * _model of an entity that may be annotated with multiple Annotations
     * this includes:
     * <UL>
     * {@link _type}
     * {@link _field}
     * {@link _method}
     * {@link _constructor}
     * {@link _parameter}
     * {@link _annotation._entry}
     * {@link _constant}
     * </UL>
     *
     * @author Eric
     *
     * @param <_WA> the container type (that has Annos)
     */
    public interface _withAnnoRefs<_WA extends _withAnnoRefs>
        extends _java._domain {

        /**
         * @return the annos
         */
        _annoRefs getAnnoRefs();

        /**
         * Check if all individual annos match the function
         * @param matchFn
         * @return
         */
        default boolean isAllAnnoRefs(Predicate<_annoRef> matchFn){
            return listAnnoRefs().stream().allMatch(matchFn);
        }

        /**
         * gets the anno at the index
         * @param index
         * @return
         */
        default _annoRef getAnnoRef(int index) {
            return getAnnoRefs().getAt( index );
        }

        /**
         * apply a function to all annos
         * @param _annoActionFn
         * @return the modified T
         */
        default _WA forAnnoRefs(Consumer<_annoRef> _annoActionFn){
            getAnnoRefs().forEach(_annoActionFn);
            return (_WA) this;
        }

        /**
         * Selectively apply a function to annos
         * @param _annoMatchFn
         * @param _annoActionFn
         * @return
         */
        default _WA forAnnoRefs(Predicate<_annoRef> _annoMatchFn, Consumer<_annoRef> _annoActionFn){
            getAnnoRefs().forEach(_annoMatchFn, _annoActionFn);
            return (_WA) this;
        }

        /**
         * Gets the FIRST annotation that matches the _annoMatchFn predicate
         *
         * @param _annoMatchFn
         * @return the first matching {@link _annoRef}, or null if none query
         */
        default _annoRef getAnnoRef(Predicate<_annoRef> _annoMatchFn) {
            return getAnnoRefs().get( _annoMatchFn );
        }

        /**
         * Gets the FIRST _anno that matches the annotationClass
         *
         * @param annotationClass the annotationClass
         * @return the first annotation that is of the annotationClass or null
         */
        default _annoRef getAnnoRef(Class<? extends Annotation> annotationClass) {
            return getAnnoRefs().get( annotationClass );
        }

        /**
         * Gets the FIRST _anno that matches the annoName
         *
         * @param annoName the NAME of the anno
         * @return the first _anno that has the NAME, or null
         */
        default _annoRef getAnnoRef(String annoName) {
            if( annoName.startsWith("@")){
                return getAnnoRefs().get(annoName.substring(1));
            }
            return getAnnoRefs().get( annoName );
        }

        default boolean isAnnoRefs(_annoRef... _as){
            _annoRefs _tas = getAnnoRefs();
            return Objects.equals(_tas, of(_as));
        }

        default boolean isAnnoRefs(_annoRefs _as){
            _annoRefs _tas = getAnnoRefs();
            return Objects.equals(_tas, _as);
        }

        /**
         *
         * @param annotationClass
         * @return
         */
        default boolean hasAnnoRef(Class<? extends Annotation> annotationClass) {
            return getAnnoRef( annotationClass ) != null;
        }

        /**
         * Do we have an anno that has this name?
         * @param name the name of the anno
         * @return
         */
        default boolean hasAnnoRef(String name) {
            return getAnnoRef( name ) != null;
        }

        default boolean hasAnnoRef(Predicate<_annoRef> _annoMatchFn) {
            return getAnnoRef( _annoMatchFn ) != null;
        }

        default List<_annoRef> listAnnoRefs() {
            return getAnnoRefs().list();
        }

        default List<_annoRef> listAnnoRefs(Predicate<_annoRef> _annMatchFn) {
            return getAnnoRefs().list( _annMatchFn );
        }

        default List<_annoRef> listAnnoRefs(String annoName) {
            return getAnnoRefs().list( annoName );
        }

        default List<_annoRef> listAnnoRefs(Class<? extends Annotation> annotationClass) {
            return getAnnoRefs().list( annotationClass );
        }

        default _WA setAnnoRefs(_annoRefs _as){
            _as.forEach( a -> getAnnoRefs().add(a) );
            return (_WA)this;
        }

        default _WA addAnnoRefs(List<AnnotationExpr> astAnnoList){
            astAnnoList.forEach( a -> getAnnoRefs().add(a) );
            return (_WA)this;
        }

        /**
         * annotate with the ANNOTATIONS and return the _annotated
         *
         * @param _anns ANNOTATIONS to to
         * @return the annotated entity
         */
        default _WA addAnnoRefs(_annoRef... _anns) {
            getAnnoRefs().add(_anns );
            return (_WA)this;
        }

        /**
         * annotate with the ANNOTATIONS and return the model
         *
         * @param annoClasses
         * @return
         */
        default _WA addAnnoRefs(Class<? extends Annotation>... annoClasses) {
            getAnnoRefs().add(annoClasses );
            return (_WA)this;
        }

        /**
         * accept one or more ANNOTATIONS
         *
         * @param annotations
         * @return
         */
        default _WA addAnnoRefs(String... annotations) {
            getAnnoRefs().add( annotations );
            return (_WA)this;
        }

        /**
         * remove the annotation and return the modified T
         * @param astAnn the annotation
         * @return
         */
        default _WA removeAnnoRef(AnnotationExpr astAnn){
            getAnnoRefs().remove(astAnn);
            return (_WA)this;
        }

        /**
         * remove the annotation and return the modified T
         * @param _a
         * @return the modified T
         */
        default _WA removeAnnoRef(_annoRef _a){
            return removeAnnoRef( _a.ast() );
        }

        /**
         * removeAll all _anno that accept the _annoMatchFn
         *
         * @param _annoMatchFn
         * @return
         */
        default _WA removeAnnoRefs(Predicate<_annoRef> _annoMatchFn) {
            getAnnoRefs().remove( _annoMatchFn );
            return (_WA)this;
        }

        /**
         *
         * @return
         */
        default boolean hasAnnoRefs() {
            return !getAnnoRefs().isEmpty();
        }

        /**
         * removeIn all ANNOTATIONS that are of the annotationClass
         *
         * @param annotationClass
         * @return
         */
        default _WA removeAnnoRefs(Class<? extends Annotation> annotationClass) {
            getAnnoRefs().remove( annotationClass );
            return (_WA)this;
        }

        /**
         * removeAll all ANNOTATIONS that have the annoName
         *
         * @param annoNames
         * @return
         */
        default _WA removeAnnoRefs(String... annoNames) {
            getAnnoRefs().remove( annoNames );
            return (_WA)this;
        }

        /**
         * removeIn all ANNOTATIONS
         *
         * @param _annosToRemove
         * @return
         */
        default _WA removeAnnoRefs(List<_annoRef> _annosToRemove) {
            getAnnoRefs().remove(_annosToRemove.toArray( new _annoRef[ 0 ] ) );
            return (_WA)this;
        }
    }
}