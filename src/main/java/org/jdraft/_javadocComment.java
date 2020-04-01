package org.jdraft;

import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;
import org.jdraft.text.Tokens;

import java.util.Objects;

public class _javadocComment implements _comment<JavadocComment, _javadocComment>, _java._node<JavadocComment, _javadocComment> {

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

    public JavadocComment astComment;

    public _javadocComment(JavadocComment lc ){
        this.astComment = lc;
    }

    public _java._domain getCommentedNode(){
        if( astComment.getCommentedNode().isPresent()){
            return _java.of( astComment.getCommentedNode().get());
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

    public boolean contains( CharSequence content ){
        return getContents().contains(content);
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
    private Tokens parseFirst(String stencilText ){
        return parseFirst(Stencil.of(stencilText));
    }

    /**
     *
     * @param stencil
     * @return
     */
    private Tokens parseFirst(Stencil stencil ){
        return stencil.parseFirst(getContents());
    }

    public boolean equals(Object o){
        if( o instanceof _javadocComment){
            _javadocComment _bc = (_javadocComment)o;
            return Objects.equals( _bc.getContents(), getContents() );
        }
        return false;
    }

    public int hashCode(){
        return this.getContents().hashCode() * 31;
        //return this.astComment.hashCode() * 31;
    }

    public String toString(){
        return this.astComment.toString();
    }
}
