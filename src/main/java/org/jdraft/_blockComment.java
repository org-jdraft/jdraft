package org.jdraft;

import com.github.javaparser.ast.comments.BlockComment;
import org.jdraft.text.Text;

import java.util.Objects;

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





    /**
     * upon creation, or after modifying through this interface,
     * do we prefix the comment to
     * after this is modified

    public boolean autoFormatPrefixStar = true;
    */

    public BlockComment astComment;

    public _blockComment(BlockComment lc ){
        this.astComment = lc;
    }

    /*
    public String getContents(){
        return this.astComment.getContent();
    }
     */



    /*
    @Override
    public _blockComment setContents( String...contents ){
        return this;

        String str = Text.combine(contents);
        List<String> lines = Text.lines(str);
        if( lines.size() == 1){
            this.astComment.setContent(lines.get(0));
        }
        //multiple line block comment
        StringBuilder sb = new StringBuilder();
        for( int i=0;i<lines.size(); i++ ){
            if( i == 0 ){
                lines.get(i).trim().startsWith("/*")
            }
            if( i > 0 ) {
                if (! (lines.get(i).trim().startsWith("*") )){
                    sb.append( " * ").append(lines.get(i) );
                }
            }
        }
        this.astComment.setContent( )

    }
     */

    public _java._domain getCommentedNode(){
        if( astComment.getCommentedNode().isPresent()){
            return _java.of( astComment.getCommentedNode().get());
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
