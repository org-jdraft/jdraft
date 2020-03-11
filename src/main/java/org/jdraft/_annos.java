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
 * Representation of the Java source code of a grouping of {@link _anno} (s)
 * expressions ({@link AnnotationExpr}) annotating a Class, Field, Method, Enum Constant, etc.
 *
 * @see java.lang.reflect.AnnotatedElement Eleemnt that is annotated with the VM
 * @see _anno a single @anno reference
 * @see _annotation the _type representation of the source declaration of an _anno instance (@interface Ann{})
 * @author Eric
 */
public class _annos
        implements _java._set<AnnotationExpr, _anno, _annos> {

    /** A reference to the container entity that is being annotated*/
    public final NodeWithAnnotations astAnnNode;

    public NodeWithAnnotations astHolder() {
        return astAnnNode;
    }

    public static _annos of( String... anns ) {
        String f = Text.combine( anns ) + System.lineSeparator() + "NOT_A_REAL_FIELD AST_ANNO_HOLDER;";
        FieldDeclaration fd = field( f );
        _annos _as = new _annos( fd );
        return _as;
    }

    public static _annos of(){
        return new _annos(field( "NOT_A_REAL_FIELD AST_ANNO_HOLDER;") );
    }

    public static _annos of(String annos){
        return of( new String[]{annos});
    }

    /**
     * Look through the anonymous Object to find some annotated entity
     * and extract & model the first annotation as a _draft {@link _annos}
     * @param anonymousObject an anonymous Object containing an annotated entity
     * @return the {@link _annos} _draft object representing the annos
     */
    public static _annos of( Object anonymousObject ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Ex.newEx(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        BodyDeclaration bd = bds.stream().filter(b -> b.getAnnotations().isNonEmpty() ).findFirst().get();
        return of( bd );
    }

    public static _annos of( List<AnnotationExpr> aes){
        _annos _as = _annos.of();
        aes.forEach(_a -> _as.add(_a));
        return _as;
    }

    public static _annos of( TypeParameter astTypeParam ){
        return new _annos( astTypeParam );
    }


    public static <T extends Type> _annos of(T t ){
        return new _annos( new _anno.Type_annoPatch(t) );
    }

    public static _annos of( NodeWithAnnotations astAnns ) {
        return new _annos( astAnns );
    }

    public _annos( NodeWithAnnotations astAnns ) {
        this.astAnnNode = astAnns;
    }

    public _annos add( String... annos ) {
        for( String anno : annos ) {
            this.astAnnNode.addAnnotation(Ast.anno(anno ) );
        }
        return this;
    }

    public _annos add( Class<? extends Annotation>... anns ) {
        for( Class<? extends Annotation> ann : anns ) {
            this.astAnnNode.addAnnotation( Ast.anno( "@" + ann.getSimpleName() ) );
        }
        return this;
    }

    public _annos addAll( Collection<_anno> _as ){
        _as.forEach(a -> add(a));
        return this;
    }

    public _anno get(String name ) {
        List<_anno> a = this.list( name );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

    public _anno get(Class<? extends Annotation> clazz ) {
        List<_anno> a = this.list( clazz );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

    @Override
    public List<_anno> list() {
        return list( t -> true );
    }

    /**
     * Lists all ANNOTATIONS of the NAME
     *
     * @param name
     * @return
     */
    public List<_anno> list( String name ) {
        return list( a -> a.getName().equals( name ) );
    }

    /**
     * Lists all _anno that are of the Class
     *
     * @param clazz
     * @return
     */
    public List<_anno> list( Class<? extends Annotation> clazz ) {
        return list( a -> a.getName().equals( clazz.getSimpleName() )
                || a.getName().equals( clazz.getCanonicalName() )
                || a.getName().endsWith( "."+clazz.getSimpleName() ) );
    }

    /**
     *
     * @param _annoMatchFn
     * @return
     */
    public List<_anno> list( Predicate<_anno> _annoMatchFn ) {
        List<_anno> l = new ArrayList<>();
        this.astAnnNode.getAnnotations().forEach(a -> {
            _anno _a = _anno.of( (AnnotationExpr)a );
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
    public _annos remove( Class<? extends Annotation>... annClasses ) {
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
    public _annos remove( String... annName ) {
        List<String> ls = new ArrayList<>();
        Arrays.stream( annName ).forEach( c -> ls.add( c ) );

        return remove( a -> ls.stream().anyMatch(
                n -> n.equals( a.getName() ) ));
    }

    public _annos clear() {
        this.astAnnNode.getAnnotations().clear();
        return this;
    }

    public boolean has(Class<? extends Annotation> clazz ) {
        return list( clazz ).size() > 0;
    }

    public void forEach( Class<? extends Annotation> annotationClazz,
                         Consumer<_anno> _annoAction ) {
        list( annotationClazz ).forEach(_annoAction );
    }

    /**
     * does this String array represent ALL of the annos?
     * @param annos representation of annos
     * @return true if they are the same
     */
    public boolean is( String... annos ) {
        try {
            return _annos.of( annos ).equals( this );
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
        Set<_anno> s = new HashSet<>();
        this.astAnnNode.getAnnotations().forEach( a -> s.add( _anno.of((AnnotationExpr)a) ) ); //add each of the exprs to the set for order
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
        final _annos other = (_annos)obj;
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
                    a -> Ex.equivalent(e, (AnnotationExpr)a ) ).findFirst().isPresent() ) {

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

    public _annos copy() {
        FieldDeclaration fd = Ast.field( "NOT_A_REAL_FIELD AST_ANNO_PARENT;" );
        this.astAnnNode.getAnnotations().forEach( a -> fd.addAnnotation( ((AnnotationExpr)a).clone() ) );
        return new _annos( fd );
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
    public interface _withAnnos<_WA extends _withAnnos>
        extends _java._domain {

        /**
         * @return the annos
         */
        _annos getAnnos();

        /**
         * Check if all individual annos match the function
         * @param matchFn
         * @return
         */
        default boolean allAnnos( Predicate<_anno> matchFn){
            return listAnnos().stream().allMatch(matchFn);
        }

        /**
         * gets the anno at the index
         * @param index
         * @return
         */
        default _anno getAnno(int index) {
            return getAnnos().getAt( index );
        }

        /**
         * apply a function to all annos
         * @param _annoActionFn
         * @return the modified T
         */
        default _WA forAnnos(Consumer<_anno> _annoActionFn){
            getAnnos().forEach(_annoActionFn);
            return (_WA) this;
        }

        /**
         * Selectively apply a function to annos
         * @param _annoMatchFn
         * @param _annoActionFn
         * @return
         */
        default _WA forAnnos(Predicate<_anno> _annoMatchFn, Consumer<_anno> _annoActionFn){
            getAnnos().forEach(_annoMatchFn, _annoActionFn);
            return (_WA) this;
        }

        /**
         * Gets the FIRST annotation that matches the _annoMatchFn predicate
         *
         * @param _annoMatchFn
         * @return the first matching {@link _anno}, or null if none query
         */
        default _anno getAnno(Predicate<_anno> _annoMatchFn) {
            return getAnnos().get( _annoMatchFn );
        }

        /**
         * Gets the FIRST _anno that matches the annotationClass
         *
         * @param annotationClass the annotationClass
         * @return the first annotation that is of the annotationClass or null
         */
        default _anno getAnno(Class<? extends Annotation> annotationClass) {
            return getAnnos().get( annotationClass );
        }

        /**
         * Gets the FIRST _anno that matches the annoName
         *
         * @param annoName the NAME of the anno
         * @return the first _anno that has the NAME, or null
         */
        default _anno getAnno(String annoName) {
            if( annoName.startsWith("@")){
                return getAnnos().get(annoName.substring(1));
            }
            return getAnnos().get( annoName );
        }

        default boolean isAnnos(_anno... _as){
            _annos _tas = getAnnos();
            return Objects.equals(_tas, of(_as));
        }

        default boolean isAnnos(_annos _as){
            _annos _tas = getAnnos();
            return Objects.equals(_tas, _as);
        }

        /**
         *
         * @param annotationClass
         * @return
         */
        default boolean hasAnno(Class<? extends Annotation> annotationClass) {
            return getAnno( annotationClass ) != null;
        }

        /**
         * Do we have an anno that has this name?
         * @param name the name of the anno
         * @return
         */
        default boolean hasAnno(String name) {
            return getAnno( name ) != null;
        }

        default boolean hasAnno(Predicate<_anno> _annoMatchFn) {
            return getAnno( _annoMatchFn ) != null;
        }

        default List<_anno> listAnnos() {
            return getAnnos().list();
        }

        default List<_anno> listAnnos(Predicate<_anno> _annMatchFn) {
            return getAnnos().list( _annMatchFn );
        }

        default List<_anno> listAnnos(String annoName) {
            return getAnnos().list( annoName );
        }

        default List<_anno> listAnnos(Class<? extends Annotation> annotationClass) {
            return getAnnos().list( annotationClass );
        }

        /**
         * replace the annotations with the _annos provided
         * @param _as
         * @return

        default _HA replace(_annos _as ) {
        this.getAnnos().clear().add( _as.list().toArray( new _anno[ 0 ] ) );
        return (_HA)this;
        }
         */

        default _WA setAnnos(_annos _as){
            _as.forEach( a -> getAnnos().add(a) );
            return (_WA)this;
        }

        default _WA addAnnos(List<AnnotationExpr> astAnnoList){
            astAnnoList.forEach( a -> getAnnos().add(a) );
            return (_WA)this;
        }

        /**
         * annotate with the ANNOTATIONS and return the _annotated
         *
         * @param _anns ANNOTATIONS to to
         * @return the annotated entity
         */
        default _WA addAnnos(_anno... _anns) {
            getAnnos().add(_anns );
            return (_WA)this;
        }

        /**
         * annotate with the ANNOTATIONS and return the model
         *
         * @param annoClasses
         * @return
         */
        default _WA addAnnos(Class<? extends Annotation>... annoClasses) {
            getAnnos().add(annoClasses );
            return (_WA)this;
        }

        /**
         * accept one or more ANNOTATIONS
         *
         * @param annotations
         * @return
         */
        default _WA addAnnos(String... annotations) {
            getAnnos().add( annotations );
            return (_WA)this;
        }

        /**
         * remove the annotation and return the modified T
         * @param astAnn the annotation
         * @return
         */
        default _WA removeAnno(AnnotationExpr astAnn){
            getAnnos().remove(astAnn);
            return (_WA)this;
        }

        /**
         * remove the annotation and return the modified T
         * @param _a
         * @return the modified T
         */
        default _WA removeAnno(_anno _a){
            return removeAnno( _a.ast() );
        }

        /**
         * removeAll all _anno that accept the _annoMatchFn
         *
         * @param _annoMatchFn
         * @return
         */
        default _WA removeAnnos(Predicate<_anno> _annoMatchFn) {
            getAnnos().remove( _annoMatchFn );
            return (_WA)this;
        }

        /**
         *
         * @return
         */
        default boolean hasAnnos() {
            return !getAnnos().isEmpty();
        }

        /**
         * removeIn all ANNOTATIONS that are of the annotationClass
         *
         * @param annotationClass
         * @return
         */
        default _WA removeAnnos(Class<? extends Annotation> annotationClass) {
            getAnnos().remove( annotationClass );
            return (_WA)this;
        }

        /**
         * removeAll all ANNOTATIONS that have the annoName
         *
         * @param annoNames
         * @return
         */
        default _WA removeAnnos(String... annoNames) {
            getAnnos().remove( annoNames );
            return (_WA)this;
        }

        /**
         * removeIn all ANNOTATIONS
         *
         * @param _annosToRemove
         * @return
         */
        default _WA removeAnnos(List<_anno> _annosToRemove) {
            getAnnos().remove(_annosToRemove.toArray( new _anno[ 0 ] ) );
            return (_WA)this;
        }
    }
}