package org.jdraft;

import com.github.javaparser.ast.comments.LineComment;
import org.jdraft.text.Text;

import java.util.List;
import java.util.Objects;

/**
 * Single line comment
 * i.e.
 * <CODE>
 * //prints the name
 * System.out.println( "name" +name);
 * </CODE>
 */
public class _lineComment implements _comment<LineComment, _lineComment>, _java._node<LineComment, _lineComment> {

    public static _lineComment of(LineComment lc ){
        return new _lineComment( lc );
    }

    public static _lineComment of( String... commentContents){
        return new _lineComment( Ast.lineComment(commentContents) );
    }

    public static _lineComment of( String commentContents){
        return new _lineComment( Ast.lineComment(commentContents) );
    }

    public LineComment astComment;

    public _lineComment(LineComment lc ){
        this.astComment = lc;
    }

    /*
    @Override
    public _lineComment setContents( String...contents ) {
        return this;
    }
    */

    /*
    @Override
    public String getContents(){
        return astComment.getContent();
    }
     */

    /*
    @Override
    public List<String> listContents(){
        return Text.lines( this.astComment.getContent() );
    }
     */

    public _java._domain getCommentedNode(){
        if( astComment.getCommentedNode().isPresent()){
            return _java.of( astComment.getCommentedNode().get());
        }
        return null;
    }

    @Override
    public _lineComment copy() {
        return new _lineComment( this.astComment.clone());
    }

    @Override
    public LineComment ast() {
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
        if( o instanceof _lineComment){
            _lineComment _bc = (_lineComment)o;
            return Objects.equals( _bc.astComment, this.astComment );
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
