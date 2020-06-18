package org.jdraft;

import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.CharLiteralExpr;

import java.util.Objects;
import java.util.function.Function;

public final class _javadocComment implements _comment<JavadocComment, _javadocComment>, _tree._node<JavadocComment, _javadocComment> {

    public static final Function<String, _javadocComment> PARSER = s-> _javadocComment.of(s);

    public static _javadocComment of(){
        return of(new JavadocComment());
    }

    public static _javadocComment of(JavadocComment jdc ){
        return new _javadocComment( jdc );
    }

    public static _javadocComment of(String... commentContents){
        if( commentContents.length == 0 ){
            return of();
        }
        return new _javadocComment( Ast.javadocComment( commentContents) );
    }

    public static _feature._one<_javadocComment, String> TEXT = new _feature._one<>(_javadocComment.class, String.class,
            _feature._id.TEXT,
            a -> a.getText(),
            (_javadocComment a, String text) -> a.setText(text), PARSER);

    public static _feature._features<_javadocComment> FEATURES = _feature._features.of(_javadocComment.class, PARSER, TEXT);

    public JavadocComment node;

    public _javadocComment(JavadocComment lc ){
        this.node = lc;
    }

    public _feature._features<_javadocComment> features(){
        return FEATURES;
    }

    public _tree._node getAttributedNode(){
        if( node.getCommentedNode().isPresent()){
            return (_tree._node)_java.of( node.getCommentedNode().get());
        }
        return null;
    }

    @Override
    public _javadocComment copy() {
        return new _javadocComment( this.node.clone());
    }

    @Override
    public JavadocComment node() {
        return node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _javadocComment replace(JavadocComment replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean equals(Object o){
        if( o instanceof _javadocComment){
            _javadocComment _bc = (_javadocComment)o;
            return Objects.equals( _bc.getText(), getText() );
        }
        return false;
    }

    public int hashCode(){
        return this.getText().hashCode() * 31;
    }

    public String toString(){
        return this.node.toString();
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
        _WJ setJavadoc(String... content);

        /**
         * set the javadoc comment with this JavadocComment
         * @param astJavadocComment the
         * @return the modified T
         */
        _WJ setJavadoc(JavadocComment astJavadocComment);


        default _WJ setJavadoc(_javadocComment _jc){
            return setJavadoc(_jc.node());
        }

        /**
         * Does this component have a Javadoc entry?
         * @return true if there is a javadoc, false otherwise
         */
        boolean hasJavadoc();
    }
}
