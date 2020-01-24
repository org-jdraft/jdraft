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
        implements _draft {

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

    public _annos add( _anno... anns ) {
        for( int i = 0; i < anns.length; i++ ) {
            astAnnNode.addAnnotation( anns[ i ].ast() );
        }
        return this;
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

    public _annos add( AnnotationExpr... astAnnos ) {
        for( AnnotationExpr ann : astAnnos ) {
            this.astAnnNode.addAnnotation( ann );
        }
        return this;
    }

    public _annos addAll( Collection<_anno> _as ){
        _as.forEach(a -> add(a));
        return this;
    }

    public _anno get( int index ) {
        return _anno.of( this.astAnnNode.getAnnotation( index ) );
    }

    public _anno first( String name ) {
        List<_anno> a = this.list( name );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

    public _anno first( Class<? extends Annotation> clazz ) {
        List<_anno> a = this.list( clazz );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

    public _anno first( Predicate<_anno> _annoMatchFn ) {
        List<_anno> a = this.list( _annoMatchFn );
        if( a.size() >= 1 ) {
            return a.get( 0 );
        }
        return null;
    }

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

    public int size() {
        return this.astAnnNode.getAnnotations().size();
    }

    public boolean isEmpty() {
        return this.astAnnNode.getAnnotations().isEmpty();
    }

    public _annos remove( _anno... annos ) {
        Arrays.stream( annos ).forEach(a -> this.astAnnNode.getAnnotations().remove( a.ast() ) );
        return this;
    }

    public _annos remove( AnnotationExpr... astAnns ) {
        Arrays.stream(astAnns ).forEach( a -> this.astAnnNode.getAnnotations().remove( a ) );
        return this;
    }

    public _annos remove( Predicate<_anno> _annoMatchFn ) {
        list(_annoMatchFn ).forEach( a -> this.astAnnNode.getAnnotations().remove( a.ast() ) );
        return this;
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

    public int indexOf( _anno _a ) {
        return indexOf( _a.ast() );
    }

    public int indexOf( AnnotationExpr astAnno ) {
        for( int i = 0; i < this.astAnnNode.getAnnotations().size(); i++ ) {
            if( Ex.equivalent((AnnotationExpr)this.astAnnNode.getAnnotations().get( i ), astAnno ) ) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains( _anno _a ) {
        return contains( _a.ast() );
    }

    public boolean contains( AnnotationExpr astAnno ) {
        return this.astAnnNode.getAnnotations().stream().filter(
                a -> Ex.equivalent((AnnotationExpr)a, astAnno ) ).findFirst().isPresent();
    }

    public boolean contains( Class<? extends Annotation> clazz ) {
        return list( clazz ).size() > 0;
    }

    public void forEach( Consumer<_anno> _annoActionFn ) {
        list().forEach(_annoActionFn );
    }

    public void forEach( Predicate<_anno> _annoMatchFn,
                         Consumer<_anno> _annoActionFn ) {
        list(_annoMatchFn ).forEach(_annoActionFn );
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
}