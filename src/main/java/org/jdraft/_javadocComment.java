package org.jdraft;

import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft.text.Text;

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

    /*
    @Override
    public String getContents(){
        return astComment.getContent();
    }
     */

    public _java._domain getCommentedNode(){
        if( astComment.getCommentedNode().isPresent()){
            return _java.of( astComment.getCommentedNode().get());
        }
        return null;
    }

    /*
    @Override
    public _javadocComment setContents( String...contents ) {
        return this;
    }

    @Override
    public List<String> listContents(){
        return Text.lines( this.astComment.getContent() );
    }
     */

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
        try{
            return Objects.equals( of( Text.combine(stringRep) ), this);
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean equals(Object o){
        if( o instanceof _javadocComment){
            _javadocComment _bc = (_javadocComment)o;
            return Objects.equals( _bc.astComment, this.astComment);
        }
        return false;
    }

    public int hashCode(){
        return this.astComment.hashCode() * 31;
    }

    public String toString(){
        return this.astComment.toString();
    }
}
