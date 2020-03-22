package org.jdraft;

import com.github.javaparser.ast.comments.BlockComment;
import org.jdraft.text.Text;

import java.util.Objects;

public class _blockComment implements _comment, _java._node<BlockComment, _blockComment> {

    public static _blockComment of(String... commentContents){
        return new _blockComment( Ast.blockComment( commentContents) );
    }

    /**
     * upon creation, or after modifying through this interface,
     * do we prefix the comment to
     * after this is modified
     */
    public boolean autoFormatPrefixStar = true;

    public BlockComment astBlockComment;

    public _blockComment(BlockComment lc ){
        this.astBlockComment = lc;
    }

    @Override
    public _blockComment copy() {
        return new _blockComment( this.astBlockComment.clone());
    }

    @Override
    public BlockComment ast() {
        return astBlockComment;
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
            return Objects.equals( _bc.astBlockComment, this.astBlockComment );
        }
        return false;
    }

    public int hashCode(){
        return this.astBlockComment.hashCode() * 31;
    }

    public String toString(){
        return this.astBlockComment.toString();
    }
}
