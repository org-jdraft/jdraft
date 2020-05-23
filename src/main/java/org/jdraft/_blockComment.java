package org.jdraft;

import com.github.javaparser.ast.comments.BlockComment;
import org.jdraft.text.Text;

import java.util.List;
import java.util.Objects;

/**
 * Representation of a block comment within code
 * <CODE>
 * /* prints the name * /
 * System.out.println( "name" +name);
 * </CODE>
 */
public final class _blockComment implements _comment<BlockComment, _blockComment>, _java._node<BlockComment, _blockComment> {

    public static _blockComment of(BlockComment bc ){
        return new _blockComment( bc );
    }

    public static _blockComment of(){
        return of( new BlockComment());
    }

    public static _blockComment of(String commentContents){
        return new _blockComment( Ast.blockComment( commentContents) );
    }

    public static _blockComment of(String... commentContents){
        return new _blockComment( Ast.blockComment( commentContents) );
    }

    public static _feature._one<_blockComment, String> TEXT = new _feature._one<>(_blockComment.class, String.class,
            _feature._id.TEXT,
            a -> a.getText(),
            (_blockComment a, String text) -> a.setText(text));


    public static _feature._meta<_blockComment> META = _feature._meta.of(_blockComment.class, TEXT);

    public BlockComment astComment;

    public _blockComment(BlockComment lc ){
        this.astComment = lc;
    }

    public _java._node getAttributedNode(){
        if( astComment.getCommentedNode().isPresent()){
            return (_java._node)_java.of( astComment.getCommentedNode().get());
        }
        return null;
    }

    @Override
    public _blockComment copy() {
        return new _blockComment( this.astComment.clone());
    }

    @Override
    public BlockComment ast() {
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
        if( o instanceof _blockComment){
            _blockComment _bc = (_blockComment)o;
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
