package org.jdraft;

import com.github.javaparser.ast.comments.LineComment;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.function.Function;

/**
 * Single line comment
 * i.e.
 * <CODE>
 * //prints the name
 * System.out.println( "name" +name);
 * </CODE>
 */
public final class _lineComment implements _comment<LineComment, _lineComment>, _java._node<LineComment, _lineComment> {

    public static final Function<String, _lineComment> PARSER = s-> _lineComment.of(s);

    public static _lineComment of(LineComment lc ){
        return new _lineComment( lc );
    }

    public static _lineComment of( String... commentContents){
        return new _lineComment( Ast.lineComment(commentContents) );
    }

    public static _lineComment of( String commentContents){
        return new _lineComment( Ast.lineComment(commentContents) );
    }

    public static _feature._one<_lineComment, String> TEXT = new _feature._one<>(_lineComment.class, String.class,
            _feature._id.TEXT,
            a -> a.getText(),
            (_lineComment a, String text) -> a.setText(text), PARSER);

    public static _feature._features<_lineComment> FEATURES = _feature._features.of(_lineComment.class,  PARSER, TEXT);

    public _feature._features<_lineComment> features(){
        return FEATURES;
    }

    public LineComment astComment;

    public _lineComment(LineComment lc ){
        this.astComment = lc;
    }

    public _java._node getAttributedNode(){
        if( astComment.getCommentedNode().isPresent()){
            return (_java._node)_java.of( astComment.getCommentedNode().get());
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

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return Objects.equals( of( Text.combine(stringRep) ), this);
        }
        catch(Exception e){
            return false;
        }
    }
     */

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
