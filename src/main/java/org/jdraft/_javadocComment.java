package org.jdraft;

import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

public final class _javadocComment implements _comment<JavadocComment, _javadocComment>, _java._node<JavadocComment, _javadocComment> {

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

    public static _feature._features<_javadocComment> FEATURES = _feature._features.of(_javadocComment.class, TEXT);

    public JavadocComment astComment;

    public _javadocComment(JavadocComment lc ){
        this.astComment = lc;
    }

    public _java._node getAttributedNode(){
        if( astComment.getCommentedNode().isPresent()){
            return (_java._node)_java.of( astComment.getCommentedNode().get());
        }
        return null;
    }

    @Override
    public _javadocComment copy() {
        return new _javadocComment( this.astComment.clone());
    }

    @Override
    public JavadocComment ast() {
        return astComment;
    }

    @Override
    public boolean is(String... stringRep) {
        return Objects.equals( of( Text.combine(stringRep) ), this);
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
        return this.astComment.toString();
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
            return setJavadoc(_jc.ast());
        }

        /**
         * Does this component have a Javadoc entry?
         * @return true if there is a javadoc, false otherwise
         */
        boolean hasJavadoc();
    }
}
