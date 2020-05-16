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
 * Representation of the Java source code of a grouping of {@link _annoExpr} (s)
 * expressions ({@link AnnotationExpr}) annotating a Class, Field, Method, Enum Constant, etc.
 *
 * @see java.lang.reflect.AnnotatedElement Eleemnt that is annotated with the VM
 * @see _annoExpr a single @anno reference
 * @see _annotation the _type representation of the source declaration of an _anno instance (@interface Ann{})
 * @author Eric
 */
public final class _annoExprs
        implements _java._set<AnnotationExpr, _annoExpr, _annoExprs> {

    /** A reference to the container entity that is being annotated*/
    public final NodeWithAnnotations astAnnNode;

    public NodeWithAnnotations astHolder() {
        return astAnnNode;
    }

    public static _annoExprs of(String... anns ) {
        String f = Text.combine( anns ) + System.lineSeparator() + "NOT_A_REAL_FIELD AST_ANNO_HOLDER;";
        FieldDeclaration fd = field( f );
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
        return new _annoExprs(field( "NOT_A_REAL_FIELD AST_ANNO_HOLDER;") );
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
        ObjectCreationExpr oce = Exprs.newEx(ste);
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
            this.astAnnNode.addAnnotation(Ast.anno(anno ) );
        }
        return this;
    }

    public _annoExprs add(Class<? extends Annotation>... anns ) {
        for( Class<? extends Annotation> ann : anns ) {
            this.astAnnNode.addAnnotation( Ast.anno( "@" + ann.getSimpleName() ) );
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
                    a -> Exprs.equal(e, (AnnotationExpr)a ) ).findFirst().isPresent() ) {

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
        FieldDeclaration fd = Ast.field( "NOT_A_REAL_FIELD AST_ANNO_PARENT;" );
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
     * {@link _parameter}
     * {@link _annotation._entry}
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
        _annoExprs getAnnoRefs();

        /**
         * Check if all individual annos match the function
         * @param matchFn
         * @return
         */
        default boolean isAllAnnoRefs(Predicate<_annoExpr> matchFn){
            return listAnnoRefs().stream().allMatch(matchFn);
        }

        /**
         * gets the anno at the index
         * @param index
         * @return
         */
        default _annoExpr getAnnoRef(int index) {
            return getAnnoRefs().getAt( index );
        }

        /**
         * apply a function to all annos
         * @param _annoActionFn
         * @return the modified T
         */
        default _WA forAnnoRefs(Consumer<_annoExpr> _annoActionFn){
            getAnnoRefs().forEach(_annoActionFn);
            return (_WA) this;
        }

        /**
         * Selectively apply a function to annos
         * @param _annoMatchFn
         * @param _annoActionFn
         * @return
         */
        default _WA forAnnoRefs(Predicate<_annoExpr> _annoMatchFn, Consumer<_annoExpr> _annoActionFn){
            getAnnoRefs().forEach(_annoMatchFn, _annoActionFn);
            return (_WA) this;
        }

        /**
         * Gets the FIRST annotation that matches the _annoMatchFn predicate
         *
         * @param _annoMatchFn
         * @return the first matching {@link _annoExpr}, or null if none query
         */
        default _annoExpr getAnnoRef(Predicate<_annoExpr> _annoMatchFn) {
            return getAnnoRefs().get( _annoMatchFn );
        }

        /**
         * Gets the FIRST _anno that matches the annotationClass
         *
         * @param annotationClass the annotationClass
         * @return the first annotation that is of the annotationClass or null
         */
        default _annoExpr getAnnoRef(Class<? extends Annotation> annotationClass) {
            return getAnnoRefs().get( annotationClass );
        }

        /**
         * Gets the FIRST _anno that matches the annoName
         *
         * @param annoName the NAME of the anno
         * @return the first _anno that has the NAME, or null
         */
        default _annoExpr getAnnoRef(String annoName) {
            if( annoName.startsWith("@")){
                return getAnnoRefs().get(annoName.substring(1));
            }
            return getAnnoRefs().get( annoName );
        }

        default boolean isAnnoRefs(_annoExpr... _as){
            _annoExprs _tas = getAnnoRefs();
            return Objects.equals(_tas, of(_as));
        }

        default boolean isAnnoRefs(_annoExprs _as){
            _annoExprs _tas = getAnnoRefs();
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

        default boolean hasAnnoRef(Predicate<_annoExpr> _annoMatchFn) {
            return getAnnoRef( _annoMatchFn ) != null;
        }

        default List<_annoExpr> listAnnoRefs() {
            return getAnnoRefs().list();
        }

        default List<_annoExpr> listAnnoRefs(Predicate<_annoExpr> _annMatchFn) {
            return getAnnoRefs().list( _annMatchFn );
        }

        default List<_annoExpr> listAnnoRefs(String annoName) {
            return getAnnoRefs().list( annoName );
        }

        default List<_annoExpr> listAnnoRefs(Class<? extends Annotation> annotationClass) {
            return getAnnoRefs().list( annotationClass );
        }

        default _WA setAnnoRefs(_annoExprs _as){
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
        default _WA addAnnoRefs(_annoExpr... _anns) {
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
        default _WA removeAnnoRef(_annoExpr _a){
            return removeAnnoRef( _a.ast() );
        }

        /**
         * removeAll all _anno that accept the _annoMatchFn
         *
         * @param _annoMatchFn
         * @return
         */
        default _WA removeAnnoRefs(Predicate<_annoExpr> _annoMatchFn) {
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
        default _WA removeAnnoRefs(List<_annoExpr> _annosToRemove) {
            getAnnoRefs().remove(_annosToRemove.toArray( new _annoExpr[ 0 ] ) );
            return (_WA)this;
        }
    }
}