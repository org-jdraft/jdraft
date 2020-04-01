package org.jdraft;

import java.util.*;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;

/**
 * Representation of the source code of a Javadoc Comment
 * i.e. <PRE>/** comment &#42;/</PRE>
 *
 * @author Eric
 */
public final class _javadoc
    implements _java._node<JavadocComment, _javadoc> {

    public static _javadoc of( String...jd){
        MethodDeclaration md = new MethodDeclaration();
        md.setJavadocComment(Text.combine(jd));
        return of( md );
    }

    public static _javadoc of( NodeWithJavadoc jdnode ) {
        return new _javadoc( jdnode );
    }

    private final NodeWithJavadoc astJavadocedNode;

    public _javadoc( NodeWithJavadoc jdnode ) {
        this.astJavadocedNode = jdnode;
    }

    public NodeWithJavadoc astHolder() {
        return this.astJavadocedNode;
    }

    public _javadoc setContent( String... content ) {
        astJavadocedNode.setJavadocComment( Text.combine( content ) );
        return this;
    }

    public boolean contains( CharSequence content ){
        if( !isEmpty() ){
            return getContent().contains(content);
        }
        return false;
    }

    /**
     * Can we find an instanceof the the stencil pattern anywhere in the code?
     * @param stencil the stencil to match within the javadoc contents
     * @return true if this Stencil pattern is found, false otherwise
     */
    public boolean contains( Stencil stencil ){
        return parseFirst(stencil) != null;
    }

    /**
     * create a Stencil from the stencilText, then look for the Stencil inside the text matching
     * and extracting the first instance found into tokens and returning them.
     * Return null if no instance of the text is found
     *
     * @param stencilText text to build a Stencil to match against
     * @return Tokens of the first Stencil match, or null if no match
     */
    private Tokens parseFirst( String stencilText ){
        return parseFirst(Stencil.of(stencilText));
    }

    /**
     *
     * @param stencil
     * @return
     */
    private Tokens parseFirst(Stencil stencil ){
        return stencil.parseFirst(getContent());
    }

    public boolean isEmpty(){
        return !astJavadocedNode.hasJavaDocComment();
    }

    @Override
    public _javadoc copy() {
        Node n = (Node)this.astJavadocedNode;
        return of( (NodeWithJavadoc)n.clone() );
    }

    public JavadocComment ast() {
        if( this.astJavadocedNode.getJavadocComment().isPresent() ) {
            return (JavadocComment)this.astJavadocedNode.getJavadocComment().get();
        }
        return null;
    }

    @Override
    public boolean is(String... stringRep) {
        return getContent().equals( Ast.javadocComment(stringRep).getContent() );
    }

    public String getContent() {
        if( astJavadocedNode.getJavadocComment().isPresent() ) {
            return Comments.getContent((com.github.javaparser.ast.comments.Comment)astJavadocedNode.getJavadocComment().get() );
        }
        return null;
    }

    @Override
    public String toString(){
        if( astJavadocedNode.getJavadocComment().isPresent() ) {
            return astJavadocedNode.getJavadocComment().get().toString();
        }
        return "";
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
        final _javadoc other = (_javadoc)obj;
        if( this.astJavadocedNode == other.astJavadocedNode ) {
            return true; //two _javadoc instances pointing to the same NodeWithJavadoc
        }
        if( !Objects.equals( this.getContent(), other.getContent() ) ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode( this.getContent() );
        return hash;
    }
    
    /**
     * Some things (specifically module-info and package info) do not implement
     * NodeWithJavadoc, so we adapter
     */
    public static class JavadocHolderAdapter implements NodeWithJavadoc {
        
        final Node n;
        
        public JavadocHolderAdapter( Node n ){
            this.n = n;
        }
        
        @Override
        public Optional getComment() {
            return n.getComment();
        }

        @Override
        public Node setComment(com.github.javaparser.ast.comments.Comment comment) {
            return n.setComment(comment);
        }        
    }
    
    /**
     * Model entity that optionally has a Javadoc Comment attributed to it
     *
     * @author Eric
     * @param <_WJ>
     */
    public interface _withJavadoc<_WJ extends _withJavadoc>
            extends _java._domain {

        /** @return the JAVADOC for this element (or returns null) */
        _javadocComment getJavadoc();

        /**
         * Add a javadoc to the entity and return the modified entity
         * @param content the javadoc content
         * @return
         */
        _WJ setJavadoc(String... content );

        /**
         * set the javadoc comment with this JavadocComment
         * @param astJavadocComment the
         * @return the modified T
         */
        _WJ setJavadoc(JavadocComment astJavadocComment );

        /**
         * Does this component have a Javadoc entry?
         * @return true if there is a javadoc, false otherwise
         */
        boolean hasJavadoc();

        /** @return the JAVADOC for this element (or returns null) */
        //_javadoc getJavadoc();

        /** 
         * Add a javadoc to the entity and return the modified entity
         * @param content the javadoc content
         * @return 
         */
        //_WJ setJavadoc(String... content );

        /**
         * set the javadoc comment with this JavadocComment
         * @param astJavadocComment the
         * @return the modified T
         */
        //_WJ setJavadoc(JavadocComment astJavadocComment );
        
        /**
         * Does this component have a Javadoc entry?
         * @return true if there is a javadoc, false otherwise
         */
        //boolean hasJavadoc();

        /**
         * Remove the javadoc entry from the entity and return the modified entity
         * @return the modified entity

        _HJ removeJavadoc();
        */
    }    
}
